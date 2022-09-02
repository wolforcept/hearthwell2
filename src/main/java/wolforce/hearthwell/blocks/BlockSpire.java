package wolforce.hearthwell.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.TokenNames;
import wolforce.hearthwell.bases.BaseBlock;
import wolforce.hearthwell.entities.EntitySpire;

public class BlockSpire extends BaseBlock {

	protected static final VoxelShape SHAPE = Shapes.or(box(0, 0, 0, 16, 4, 16), Shapes.or(box(4, 4, 4, 12, 14, 12), box(2, 14, 2, 14, 16, 14)));;

	public BlockSpire(Properties props) {
		super(props);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState prevState, boolean isMoving) {
		if (!world.isClientSide && !TokenNames.getNamesOfPos(pos).equals("")) {
			EntitySpire ent = new EntitySpire(world);
			ent.setPos(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f);
			world.addFreshEntity(ent);
		}
	}

//	@Override
//	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
//			BlockHitResult hit) {
//
//		if (isBurstRecipe(player.getItemInHand(hand))
//				|| !Util.isValid(player.getItemInHand(InteractionHand.MAIN_HAND))) {
//			if (!world.isClientSide) {
//				TeBurstSeed te = (TeBurstSeed) world.getBlockEntity(pos);
//				player.setItemInHand(hand, te.tryAddItem(player, player.getItemInHand(hand)));
//				world.sendBlockUpdated(pos, state, state, 3);
//			}
//			return InteractionResult.CONSUME;
//		}
//		return InteractionResult.PASS;
//	}

	//
	//
	//

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return Shapes.or(box(3, 0, 3, 13, 12, 13), Shapes.or(box(5, 12, 5, 11, 19, 11), box(6, 19, 6, 10, 25, 10)));
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

}
