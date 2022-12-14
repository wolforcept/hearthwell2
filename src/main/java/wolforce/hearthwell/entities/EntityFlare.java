package wolforce.hearthwell.entities;

import static wolforce.hearthwell.data.MapData.DATA;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.network.NetworkHooks;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.recipes.RecipeHandItem;
import wolforce.hearthwell.data.recipes.RecipeTransformation;
import wolforce.hearthwell.particles.ParticleEnergyData;
import wolforce.hearthwell.registries.Entities;
import wolforce.utils.UtilWorld;

public class EntityFlare extends Entity {

	public static final int PLAYER_DISTANCE = 5;
	public static final String REG_ID = "flare";
//	private static PerlinSimplexNoise perlin = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(2345L)), IntStream.rangeClosed(-3, 0).boxed().collect(Collectors.toList()));

	private static final EntityDataAccessor<Integer> FLARE_INDEX = SynchedEntityData.defineId(EntityFlare.class, EntityDataSerializers.INT);

	private String flareType = "";
	private byte uses = 1;
	private CompoundTag oldUnlockedNodes = new CompoundTag();
//	private Vec3 spireTarget = null;
	private float addedY;
	private UUID hwId;

	public EntityFlare(Level world) {
		this(Entities.entity_flare.get(), world);
	}

	public EntityFlare(EntityType<EntityFlare> type, Level world) {
		super(type, world);
	}

//	private EntityHearthWell hw() {
//		if (world instanceof ServerWorld) {
//			Entity entity = ((ServerWorld) world).getEntityByUuid(hwId);
//			if (entity != null && entity instanceof EntityHearthWell && entity.isAlive() && entity.isAddedToWorld())
//				return (EntityHearthWell) entity;
//		}
//		return null;
//	}

//	public void set(String flareType, int flareIndex, byte uses, CompoundTag unlockedNodes) {
	public void set(UUID hwId, String flareType, int flareIndex, byte uses) {
		this.hwId = hwId;
		this.flareType = flareType;
		this.setFlareIndex(flareIndex);
		this.uses = uses;
//		this.unlockedNodes = unlockedNodes;
	}

	//
	//
	//

	@Override
	public void tick() {
		super.tick();

//		if (ticksExisted > maxAge)
//			setDead();

		// SERVER ONLY
		if (!level.isClientSide)
			serverTick();
		else
			clientTick();
	}

	private void serverTick() {

		if (level instanceof ServerLevel serverLevel) {
			ChunkPos chunkPos = chunkPosition();
			ForgeChunkManager.forceChunk(serverLevel, HearthWell.MODID, blockPosition(), chunkPos.x, chunkPos.z, true, true);
		}

		BlockPos pos = blockPosition();
//		Vec3 posVec = position();
		Player nearPlayer = findNearestPlayer();

		if (nearPlayer != null)
			moveToPlayer(nearPlayer);
//		else
//			moveToSpire();

		if (Math.random() < .1) {
			float addY = .1f * (Math.random() < .5 ? -1 : 1);
			if (addedY + addY > 1)
				addY = -.3f;
			if (addedY + addY < -1)
				addY = .3f;

			addedY += addY;
			setPos(position().add(0, addY, 0));
		}

		if (!level.isEmptyBlock(pos))
			checkRecipes(pos, level.getBlockState(pos));
	}

//	private boolean moveToSpire() {
//
//		Vec3 pos = new Vec3(getX(), getY(), getZ());
////		Vec3 pos = new Vec3(getX(), realY, getZ());
//		if (spireTarget != null) {
//			Vec3 dirVec = spireTarget.subtract(pos);
////			moveTo(p);
////			moveTo(spireTarget);
//			Vec3 tar = pos.add(dirVec.normalize().scale(.1));
//			setRealPosition(tar.x, tar.y, tar.z);
//		}
//
//		if (tickCount % 20 == 0) {
//			List<EntitySpire> ents = level.getEntitiesOfClass(EntitySpire.class, new AABB(getOnPos()).inflate(2));
//			if (ents.size() > 0) {
//				double avgX = 0, avgY = 0, avgZ = 0;
//				for (EntitySpire s : ents) {
//					avgX += s.getX();
//					avgY += s.getY() + 1;
//					avgZ += s.getZ();
//				}
//				avgX /= ents.size();
//				avgY /= ents.size();
//				avgZ /= ents.size();
//
////				spireTarget = new Vec3(avgX +.5+ random.nextGaussian() * .1, avgY +.5+ random.nextGaussian() * .1,
////						avgZ +.5+ random.nextGaussian() * .1);
//				spireTarget = new Vec3(//
//						avgX + (ents.size() == 1 ? random.nextGaussian() * .5 : 0), //
//						avgY + (ents.size() == 1 ? 1.5 : .5), //
//						avgZ + (ents.size() == 1 ? random.nextGaussian() * .5 : 0)//
//				);
//			} else {
//				spireTarget = null;
//			}
//		}
//
//		return false;
//	}

	private Player findNearestPlayer() {
		List<Player> players = level.getEntitiesOfClass(Player.class, getBoundingBox().inflate(PLAYER_DISTANCE));

		for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
			Player playerEntity = (Player) iterator.next();
			if (playerEntity.getMainHandItem().getItem() != HearthWell.flare_torch && matchesAnyRecipeHandItem(playerEntity.getMainHandItem()) == null)
				iterator.remove();
		}

		if (players.isEmpty())
			return null;

		Vec3 pos = position();
		players.sort(new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				double d1 = o1.position().distanceTo(pos);
				double d2 = o2.position().distanceTo(pos);
				return d1 < d2 ? -1 : d1 == d2 ? 0 : 1;
			}
		});
		return players.get(0);
	}

	private RecipeHandItem matchesAnyRecipeHandItem(ItemStack stack) {
		MapData DATA = DATA();
		CompoundTag unlockedNodes = getUnlockedNodes();
		for (RecipeHandItem recipe : DATA.recipes_handitem) {
			if (recipe.isUnlocked(unlockedNodes) && recipe.flareType.equals(flareType) && recipe.matches(stack)) {
				return recipe;
			}
		}
		return null;
	}

	private CompoundTag getUnlockedNodes() {
		if (level instanceof ServerLevel level) {
			Entity hwEnt = level.getEntity(hwId);
			if (hwEnt instanceof EntityHearthWell entity) {
				oldUnlockedNodes = entity.getUnlockedNodes();
			}
		}
		return oldUnlockedNodes;
	}

	private void moveToPlayer(Player player) {

		boolean torch = player.getMainHandItem().getItem() == HearthWell.flare_torch;
		if (torch && !player.isShiftKeyDown()) {
			List<EntityFlare> flaresNearPlayer = level.getEntitiesOfClass(EntityFlare.class,
					new AABB(player.getEyePosition().add(-PLAYER_DISTANCE, -PLAYER_DISTANCE, -PLAYER_DISTANCE), player.getEyePosition().add(PLAYER_DISTANCE, PLAYER_DISTANCE, PLAYER_DISTANCE)));
			double d = position().distanceTo(player.position());
			for (EntityFlare entityFlare : flaresNearPlayer) {
				if (entityFlare.position().distanceTo(player.position()) < d)
					return;
			}
		}

		Vec3 pos = torch//
				? player.getEyePosition().add(player.getLookAngle().normalize().scale(2)) //
				: player.getEyePosition();

		Vec3 linearPos = new Vec3(getX(), getY(), getZ());
//		Vec3 linearPos = new Vec3(getX(), realY, getZ());
		Vec3 direction = pos.subtract(linearPos);

		if (player != null && player.getMainHandItem().getItem() != HearthWell.flare_torch && direction.length() < 0.25) {
			RecipeHandItem recipe = matchesAnyRecipeHandItem(player.getMainHandItem());
			if (recipe != null) {
				player.getMainHandItem().shrink(1);
				List<ItemStack> stacks = recipe.getOutputStacksFlat();
				for (ItemStack stack : stacks)
					UtilWorld.spawnItem(level, position(), stack);
				uses--;
				if (uses <= 0)
					kill();
				level.playSound((Player) null, blockPosition(), SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS, 10000.0F, (float) (4 + Math.random()));
			}
		}

		Vec3 dir = direction.normalize().scale(.06 /* + Math.random() * .25 */);
//		double dy = Math.max(-1, Math.min(1, 0.02 * perlin.getValue(this.tickCount / 30.0, 0, false)));

//		realY += v.y;
//		setPos(linearPos.x + v.x, realY + dy * 100, linearPos.z + v.z);
		setPos(linearPos.x + dir.x, linearPos.y + dir.y, linearPos.z + dir.z);
	}

//	private void moveToPos(Vec3 pos) {
//
//		Vec3 linearPos = new Vec3(getX(), getY(), getZ());
////		Vec3 linearPos = new Vec3(getX(), realY, getZ());
//		Vec3 difference = pos.subtract(linearPos);
//
//		Vec3 v = difference.normalize().scale(.06 /* + Math.random() * .25 */);
//		double dy = Math.max(-1, Math.min(1, 0.02 * perlin.getValue(this.tickCount / 30.0, 0, false)));
//
////		realY += v.y;
////		setPos(linearPos.x + v.x, realY + dy * 100, linearPos.z + v.z);
//		setPos(linearPos.x + v.x, v.y + dy * 100, linearPos.z + v.z);
//	}

	private void clientTick() {
		Vec3 pos = position();
		int color = getColor();

		for (int i = 0; i < 1; i++) {
			double x = random.nextGaussian() * .03;
			double y = random.nextGaussian() * .03;
			double z = random.nextGaussian() * .03;

			double vx = random.nextGaussian() * .002;
			double vy = random.nextGaussian() * .002;
			double vz = random.nextGaussian() * .002;

			if (i == 0) {
//				System.out.println(color);
				if (color == -1) {
					color = HearthWell.getRandomColorOfHearthwell();
//					color = 0xFF5500FF;
//					double r = Math.random();
//					if (r < .33) {
//						color += 0x330000;
//					} else if (r < .66) {
//						color += 0x550000;
//					}
				}
//					color = (int) (Math.random() * Integer.MAX_VALUE);
//				world.addParticle(new ParticleEnergyData(-color), pos.x + x, pos.y + y, pos.z + z, vx, vy, vz);
//				if (color == -16777216)
				level.addParticle(new ParticleEnergyData(color), pos.x + x, pos.y + y, pos.z + z, vx, vy, vz);
//				double prob = world.getDayTime() > 12000 ? 0.75 : 0.25;
			} else {
				level.addAlwaysVisibleParticle(new ParticleEnergyData(color), pos.x + x, pos.y + y, pos.z + z, vx, vy, vz);
			}
		}

	}

	private void checkRecipes(BlockPos pos, BlockState state) {
		MapData DATA = DATA();
		Block block = state.getBlock();

		CompoundTag unlockedNodes = getUnlockedNodes();

		for (RecipeTransformation recipe : DATA.recipes_transformation) {
			if (recipe.isUnlocked(unlockedNodes) && recipe.flareType.equals(this.flareType) && recipe.matches(block)) {
//				if (recipe.getOuputBlock() != null)
//					world.setBlockState(pos, recipe.getOuputBlock().getDefaultState());
//				else {

				Block newBlock = recipe.getRandomOuputBlock();
				if (newBlock != null) {
					level.setBlockAndUpdate(pos, newBlock.defaultBlockState());
				} else {
					level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					List<ItemStack> stacks = recipe.getOutputStacksFlat();
					for (ItemStack stack : stacks)
						UtilWorld.spawnItem(level, pos, stack);
				}

				level.playSound((Player) null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 10.0F, (float) (Math.random()));
				
				if (uses <= 0)
					kill();
			}
		}
	}

	public void setRealPosition(double x, double y, double z) {
		setPos(x, y, z);
		addedY = 0;
	}

//	private void setColor(int color) {
//		entityData.set(COLOR, color);
//	}

	private void setFlareIndex(int i) {
		entityData.set(FLARE_INDEX, i);
	}

	private int getColor() {
		MapData DATA = DATA();
		int colorRecipeIndex = entityData.get(FLARE_INDEX);
		if (colorRecipeIndex >= DATA.recipes_flare.size())
			colorRecipeIndex = 0;
		return DATA.recipes_flare.get(colorRecipeIndex).getColor();
//		return entityData.get(COLOR);
	}

	private int getFlareIndex() {
		return entityData.get(FLARE_INDEX);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(FLARE_INDEX, 0);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putUUID("hwuuid", hwId);
		compound.putString("flareType", flareType);
		compound.putInt("flareIndex", getFlareIndex());
		compound.putByte("uses", uses);
		compound.putFloat("addedY", addedY);

		compound.put("unlockedNodes", getUnlockedNodes());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		hwId = compound.getUUID("hwuuid");
		flareType = compound.getString("flareType");
		setFlareIndex(compound.getInt("flareIndex"));
		uses = compound.getByte("uses");
		addedY = compound.getFloat("addedY");
		oldUnlockedNodes = compound.getCompound("unlockedNodes");
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
