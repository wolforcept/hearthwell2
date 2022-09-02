package wolforce.hearthwell.entities;

import java.util.Arrays;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.TokenNames;
import wolforce.hearthwell.particles.ParticleEnergyData;
import wolforce.hearthwell.registries.Entities;

public class EntitySpire extends Entity {

	public static final String REG_ID = "entity_spire";

	private static final EntityDataAccessor<Integer> FUEL = SynchedEntityData.defineId(EntitySpire.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<String> NAMES = SynchedEntityData.defineId(EntitySpire.class, EntityDataSerializers.STRING);

	public int currentColor;

	public EntitySpire(Level world) {
		this(Entities.entity_spire.get(), world);
	}

	public EntitySpire(EntityType<EntitySpire> type, Level world) {
		super(type, world);
	}

	@Override
	public void tick() {
		super.tick();

		if (getNames() == null) {
			setNames(TokenNames.getNamesOfPos(getOnPos()));
		}

//		if (Math.random() < .1)
//			setColor(Math.max(0, getColor() - 1));

		Vec3 pos = position();
		BlockPos blockpos = getOnPos();
		BlockState spire = level.getBlockState(blockpos);

		if (spire == null || spire.getBlock() != HearthWell.spire) {
			kill();
		}

		if (level.isClientSide)
			clientTick(pos, blockpos);
		else {
			if (getNames().equals(""))
				kill();
			else
				serverTick(pos, blockpos, spire);
		}
	}

	private void serverTick(Vec3 pos, BlockPos blockpos, BlockState spire) {
		List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(.3));
		for (ItemEntity itemEntity : entities) {
			if (itemEntity.getItem().getItem() == HearthWell.myst_dust) {
				setFuel(getFuel() + itemEntity.getItem().getCount() * 100);
				itemEntity.kill();
			}
		}
	}

	private void clientTick(Vec3 pos, BlockPos blockpos) {

		if (getFuel() > 0) {

			List<Integer> colors = Arrays.stream(getNames().split("\\.\\.")).map(x -> Integer.parseInt(x.replace(".", ""))).toList();
			int n = colors.size();
			if (n <= 0)
				return;

			int g = (int) ((System.currentTimeMillis() / 300) % (n * 100));
			int h = g / 100;
			float percent = g / 100f - (g / 100);

			if (Math.random() < percent)
				h++;
			if (h >= colors.size() || h < 0)
				h = 0;

			currentColor = (0xFF << 24) | TokenNames.getColorOfToken(colors.get(h));

			double nr = Math.min(4, Math.max(0, 0.01919 * getFuel() + .08080));

			for (int i = 0; i < nr; i++) {

				if (Math.random() > nr)
					continue;

				double x = random.nextGaussian() * .1;
				double y = random.nextGaussian() * .03;
				double z = random.nextGaussian() * .1;

				double vx = random.nextGaussian() * .002;
				double vy = Math.abs(random.nextGaussian() * .03);
				double vz = random.nextGaussian() * .002;

				level.addParticle(new ParticleEnergyData(currentColor), pos.x + x, pos.y + 1 + y, pos.z + z, vx, vy, vz);
			}
		}
	}

	public boolean tryAddFuel(int v) {
		int fuel = getFuel();
		if (v > 0 || (v < 0 && fuel >= v)) {
			setFuel(fuel + v);
			return true;
		}
		return false;
	}

	public void setFuel(int fuel) {
		entityData.set(FUEL, fuel);
	}

	public int getFuel() {
		return entityData.get(FUEL);
	}

	public void setNames(String letter) {
		entityData.set(NAMES, letter);
	}

	public String getNames() {
		return entityData.get(NAMES);
	}

	public boolean hasName(int n) {
		String names = getNames();
		if (names == null)
			return false;
		return TokenNames.containsNameIndex(names, n);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(FUEL, 0);
		entityData.define(NAMES, null);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("fuel", getFuel());
		compound.putString("names", getNames());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setFuel(compound.getInt("fuel"));
		setNames(compound.getString("names"));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
