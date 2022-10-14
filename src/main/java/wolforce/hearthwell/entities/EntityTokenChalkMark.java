package wolforce.hearthwell.entities;

import org.apache.commons.lang3.Validate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.TokenNames;
import wolforce.hearthwell.client.events.EventsForge;
import wolforce.hearthwell.registries.Entities;

public class EntityTokenChalkMark extends Entity {

	public static final float SIZE = 7 / 16f;
	public static final String REG_ID = "token_chalk_mark";

	private static final EntityDataAccessor<String> TEXT = SynchedEntityData.defineId(EntityTokenChalkMark.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Integer> TEXT_DIRECTION = SynchedEntityData.defineId(EntityTokenChalkMark.class, EntityDataSerializers.INT);

	public EntityTokenChalkMark(Level world) {
		this(Entities.entity_tokenchalkmark.get(), world);
	}

	public EntityTokenChalkMark(EntityType<EntityTokenChalkMark> type, Level world) {
		super(type, world);
	}

	public void set(String text, Direction textDirection) {
		this.setText(text);
		this.setDirection(textDirection);
	}

	public String getText() {
		return this.getEntityData().get(TEXT);
	}

	public Direction getTextDirection() {
		return Direction.values()[this.getEntityData().get(TEXT_DIRECTION)];
	}

	private void setText(String text) {
		this.getEntityData().set(TEXT, text);
	}

	private void setTextDirection(Direction dir) {
		this.getEntityData().set(TEXT_DIRECTION, dir.ordinal());
	}

	private void setDirection(Direction dir) {
		Validate.notNull(dir);
		setTextDirection(dir);
		if (dir.getAxis().isHorizontal()) {
			this.setXRot(0.0F);
			this.setYRot((float) (dir.get2DDataValue() * 90));
		} else {
			this.setXRot((float) (-90 * dir.getAxisDirection().getStep()));
			this.setYRot(0.0F);
		}

		this.xRotO = this.getXRot();
		this.yRotO = this.getYRot();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() == HearthWell.token_base) {
			BlockPos blockpos = this.blockPosition();
			int n = TokenNames.getLetterOfPos(blockpos);
			if (player.level.isClientSide) {
				EventsForge.setRenderLetter(blockpos, n);
			} else {
				int match = TokenNames.addFromPlayer(player.getStringUUID(), (char) n);
				if (match >= 0) {
					player.setItemInHand(hand, new ItemStack(HearthWell.getTokenItem(match)));
				}
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean hurt(DamageSource p_19946_, float p_19947_) {
		kill();
		return super.hurt(p_19946_, p_19947_);
	}

	@Override
	public float getEyeHeight(Pose p_20237_) {
		return 0;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	//
	//
	//

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(TEXT, "");
		this.getEntityData().define(TEXT_DIRECTION, 0);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("text", getText());
		compound.putInt("textDirection", getTextDirection().ordinal());
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setText(compound.getString("text"));
		setTextDirection(Direction.values()[compound.getInt("textDirection")]);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
