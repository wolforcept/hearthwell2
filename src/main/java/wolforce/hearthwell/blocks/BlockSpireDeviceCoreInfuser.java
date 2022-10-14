package wolforce.hearthwell.blocks;

import static wolforce.hearthwell.data.MapData.DATA;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.blocks.tiles.BeSpireDevice;
import wolforce.hearthwell.data.recipes.RecipeCoring;
import wolforce.utils.UtilWorld;

public class BlockSpireDeviceCoreInfuser extends BaseSpireDevice {

	private static final VoxelShape shape = Shapes.or(box(0, 0, 0, 16, 4, 16), Shapes.or(box(4, 4, 4, 12, 14, 12), box(2, 14, 2, 14, 16, 14)));

	public BlockSpireDeviceCoreInfuser(Properties props) {
		super(props, shape);
	}

	@Override
	public Vec3 getParticleCenterPos() {
		return new Vec3(.5, .6, .5);
	}

	@Override
	boolean tickDeviceClient(Level level, BlockPos blockpos, BlockState bs, BeSpireDevice be) {
		return false;
	}

	@Override
	boolean tickDeviceServer(ServerLevel level, BlockPos blockpos, BlockState bs, BeSpireDevice be) {
		if (!be.extraInfo.contains("time"))
			be.extraInfo.putInt("time", 0);
		int time = be.extraInfo.getInt("time");

		Block blockAbove = level.getBlockState(blockpos.above()).getBlock();
		if (blockAbove instanceof BlockCore core) {

			RecipeCoring recipe = getMatchingRecipe(blockAbove, be.getFuelType());

			if (recipe != null) {

				time++;
				if (time >= recipe.cost) {
					level.setBlock(blockpos.above(), Blocks.AIR.defaultBlockState(), 3);
					level.playSound(null, blockpos, SoundEvents.HUSK_STEP, SoundSource.BLOCKS, 1, 1);
					for (ItemStack stack : recipe.getOutputStacks())
						UtilWorld.spawnItem(level, new Vec3(blockpos.getX() + .5, blockpos.getY() + 1.3, blockpos.getZ() + .5), stack, 10, 0, 0, 0);

				} else if (be.tryRemoveFuel(1)) {

					level.sendParticles(ParticleTypes.PORTAL, //
							blockpos.getX() + .5f, //
							blockpos.getY() + 1.2f, //
							blockpos.getZ() + .5f, //
							1, .1, .1, .1, 0.03f);

					be.extraInfo.putInt("time", time);
				}

			}
		} else {
			be.extraInfo.putInt("time", 0);
		}

//		double d = .3;
//		double angle = Math.random() * 2 * Math.PI;
//		angle = (System.currentTimeMillis() % 20) / 20.0 * 2 * Math.PI;
//		double dd = 1000;
//		angle = (System.currentTimeMillis() % dd) / (float) dd * 2 * PI;
//		float xx = (float) (d * Math.cos(angle));
//		float zz = (float) (d * Math.sin(angle));
//
////		SimpleParticleType part = ParticleTypes.COMPOSTER;
////		SimpleParticleType part = ParticleTypes.CRIT;
//		SimpleParticleType part = ParticleTypes.ELECTRIC_SPARK;
////		ParticleEnergyData part = new ParticleEnergyData(TokenNames.getColorOfToken(be.getFuelType()));
//
//		level.sendParticles(part, //
//				blockpos.getX() + .5f + xx, //
//				blockpos.getY() + 1.2f, // + Math.abs(rand.nextGaussian() * .3), //
//				blockpos.getZ() + .5f + zz, //
//				1, 0, 0, 0, 0.01f);
		return false;
	}

	private RecipeCoring getMatchingRecipe(Block block, int fuelType) {
		for (RecipeCoring recipe : DATA().recipes_coring) {
			if (recipe.getInputBlocksFlat().contains(block) && //
					recipe.tokenId == fuelType)
				return recipe;
		}
		return null;
	}

	@Override
	protected InteractionResult onRightClick(BlockState bs, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, ItemStack heldItem, BeSpireDevice entity) {
		return InteractionResult.PASS;
	}

}
