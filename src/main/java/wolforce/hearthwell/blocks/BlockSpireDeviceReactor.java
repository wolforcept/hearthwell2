package wolforce.hearthwell.blocks;

import static wolforce.hearthwell.data.MapData.DATA;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.TokenNames;
import wolforce.hearthwell.blocks.tiles.BeSpireDevice;
import wolforce.hearthwell.data.recipes.RecipeReacting;
import wolforce.hearthwell.particles.ParticleEnergyData;

public class BlockSpireDeviceReactor extends BaseSpireDevice {

	private static final VoxelShape shape = Shapes.or(box(2, 0, 2, 14, 2, 14), Shapes.or(box(5, 2, 5, 11, 9, 11), box(6, 9, 6, 10, 15, 10)));

	public BlockSpireDeviceReactor(Properties props) {
		super(props, shape);
	}

	@Override
	public Vec3 getParticleCenterPos() {
		return new Vec3(.5, .25, .5);
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

		Block blockBelow = level.getBlockState(blockpos.below()).getBlock();

		RecipeReacting recipe = getMatchingRecipe(blockBelow, be.getFuelType());

		if (recipe != null) {

			time++;
			System.out.println(time + "/" + be.getFuel());
			if (time >= recipe.cost) {
				level.setBlock(blockpos.below(), recipe.getRandomOuputBlock().defaultBlockState(), 3);
				level.playSound(null, blockpos, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 1, 1);
			} else if (be.tryRemoveFuel(1)) {

				level.sendParticles(new ParticleEnergyData(TokenNames.getColorOfToken(be.getFuelType())), //
						blockpos.getX() + .5f, //
						blockpos.getY() - .5f, //
						blockpos.getZ() + .5f, //
						1, 0, 0, 0, 0.006f);
				be.extraInfo.putInt("time", time);
			}
		} else {
			be.extraInfo.putInt("time", 0);
		}

		return false;
	}

	private RecipeReacting getMatchingRecipe(Block block, int fuelType) {
		for (RecipeReacting recipe : DATA().recipes_reacting) {
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
