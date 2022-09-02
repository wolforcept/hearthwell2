package wolforce.hearthwell.blocks;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.TokenNames;
import wolforce.hearthwell.bases.BaseBlock;
import wolforce.hearthwell.bases.BlockHasRenderLayer;
import wolforce.hearthwell.blocks.tiles.BeSpireDevice;
import wolforce.hearthwell.entities.EntitySpire;
import wolforce.hearthwell.items.ItemTokenOf;
import wolforce.hearthwell.particles.ParticleEnergyData;
import wolforce.hearthwell.registries.TileEntities;

public abstract class BaseSpireDevice extends BaseBlock implements BlockHasRenderLayer.Translucent, EntityBlock {

	private static Random random = new Random();

	private final VoxelShape shape;

	public BaseSpireDevice(Properties props, VoxelShape shape) {
		super(props.noOcclusion());
		this.shape = shape;
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
//		return Shapes.or(box(0, 0, 0, 16, 4, 16), Shapes.or(box(4, 4, 4, 12, 14, 12), box(2, 14, 2, 14, 16, 14)));
		return shape;
	}

	public BeSpireDevice getDeviceEntity(Level world, BlockPos pos) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof BeSpireDevice spireDeviceEntity)
			return spireDeviceEntity;
		return null;
	}

	@Override
	public InteractionResult use(BlockState bs, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

		BeSpireDevice entity = getDeviceEntity(world, pos);
		if (entity == null)
			return InteractionResult.FAIL;

		ItemStack heldItem = player.getItemInHand(hand);

		if (heldItem.getItem() instanceof ItemTokenOf tokenOf && HearthWell.getTokenItems().contains(tokenOf)) {

			if (entity.getFuel() <= 0) {
				entity.trySetFuelType(tokenOf.i);
				entity.tryAddFuel(1);
				return InteractionResult.SUCCESS;
			}
		}

		return this.onRightClick(bs, world, pos, player, hand, result, heldItem, entity);
	}

	protected abstract InteractionResult onRightClick(BlockState bs, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, ItemStack heldItem, BeSpireDevice entity);

	//
	// block entity ticking
	//

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState bs) {
		return new BeSpireDevice(pos, bs);
	}

	public static void clientTick(Level level, BlockPos blockpos, BlockState bs, BeSpireDevice be) {
		if (bs.getBlock() instanceof BaseSpireDevice device) {
			if (device.tickDeviceClient(level, blockpos, bs, be))
				return;

			// common to all devices

			if (be.getFuel() > 0) {

				Vec3 pos = device.getParticleCenterPos();
				if (pos != null) {
					pos = new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ()).add(pos);
					double nr = Math.max(.1, Math.min(4, Math.max(0, 0.001919 * be.getFuel() + .008080)));
					for (int i = 0; i < nr; i++) {

						if (Math.random() > nr)
							continue;

						int color = TokenNames.getColorOfToken(be.getFuelType());
//								(0xFF << 24) | HearthWell.getColorOfLetter((char) be.getFuelType());

						double dx = random.nextGaussian() * .05;
						double dz = random.nextGaussian() * .05;

						level.addParticle(new ParticleEnergyData(color), pos.x + dx, pos.y, pos.z + dz, 0, .005, 0);
					}
				}
			}
		}
	}

	public static void serverTick(Level _level, BlockPos blockpos, BlockState bs, BeSpireDevice be) {
		if (_level instanceof ServerLevel level && bs.getBlock() instanceof BaseSpireDevice device) {
			if (device.tickDeviceServer(level, blockpos, bs, be))
				return;

			int extractValue = 1;
			int myFuelType = be.getFuelType();

			if (be.getFuel() < 100000 && myFuelType >= 0) {

				Vec3 pos = new Vec3(blockpos.getX() + .5, blockpos.getY() + .5, blockpos.getZ() + .5);
				List<EntitySpire> spires = level.getEntitiesOfClass(EntitySpire.class, new AABB(blockpos).inflate(5));
				for (Iterator<EntitySpire> iterator = spires.iterator(); iterator.hasNext();) {
					EntitySpire entitySpire = iterator.next();
					if (entitySpire.getFuel() < extractValue) {
						iterator.remove();
						continue;
					}
					if (myFuelType >= 0 && !entitySpire.hasName(myFuelType)) {
						iterator.remove();
						continue;
					}
				}
				if (!spires.isEmpty()) {
					Collections.sort(spires, (s1, s2) -> (int) (s1.position().subtract(pos).length() - s2.position().subtract(pos).length()));
					EntitySpire spire = spires.get(0);
					if (spire.tryAddFuel(-extractValue)) {

						// TODO
						int color1 = TokenNames.getColorOfToken(be.getFuelType());
						long c1r = color1 >> 16 & 0xff;
						long c1g = color1 >> 8 & 0xff;
						long c1b = color1 & 0xff;
						int color2 = TokenNames.getColorOfToken(be.getFuelType());
						long c2r = color2 >> 16 & 0xff;
						long c2g = color2 >> 8 & 0xff;
						long c2b = color2 & 0xff;

						float l = (float) (((System.currentTimeMillis() / 7) % 400) / 400f);

						int cr = (int) (c1r * (1 - l) + c2r * l);
						int cg = (int) (c1g * (1 - l) + c2g * l);
						int cb = (int) (c1b * (1 - l) + c2b * l);

						int color = 0xFF << 24 | cr << 16 | cg << 8 | cb;

						Vec3 spirePos = spire.position().add(0, 1, 0);
						Vec3 dir = new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ()) //
								.add(device.getParticleCenterPos()) //
								.subtract(spirePos)//
								.scale(l);

						level.sendParticles(new ParticleEnergyData(color), //
								spirePos.x + dir.x, //
								spirePos.y + dir.y, //
								spirePos.z + dir.z, //
								2, 0, 0, 0, 0);

						level.addParticle(null, false, myFuelType, myFuelType, myFuelType, color, extractValue, myFuelType);
						be.tryAddFuel(extractValue);
					}
				}
			}
		}
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState bs, BlockEntityType<T> type) {
		return createTickerHelper(type, TileEntities.spire_device.get(), level.isClientSide ? BaseSpireDevice::clientTick : BaseSpireDevice::serverTick);
	}

	@SuppressWarnings("unchecked")
	public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> candidate, BlockEntityType<E> desired,
			BlockEntityTicker<? super E> ticker) {
		return desired == candidate ? (BlockEntityTicker<A>) ticker : null;
	}

	// abstract

	/**
	 * @return whether this tick was resolved
	 */
	abstract boolean tickDeviceClient(Level level, BlockPos blockpos, BlockState bs, BeSpireDevice be);

	/**
	 * @return whether this tick was resolved
	 */
	abstract boolean tickDeviceServer(ServerLevel level, BlockPos blockpos, BlockState bs, BeSpireDevice be);

	abstract Vec3 getParticleCenterPos();

}
