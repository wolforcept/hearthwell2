package wolforce.hearthwell.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.bases.BaseBlock;
import wolforce.hearthwell.bases.BlockHasRenderLayer;

public class BlockMystContainer extends BaseBlock implements BlockHasRenderLayer.Translucent {

	final VoxelShape shape = box(4, 0, 4, 12, 11, 12);

	public BlockMystContainer(Properties props) {
		super(props.noOcclusion());
	}

//	public abstract InteractionResult use(BlockState bs, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result);

	//
	//
	//

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return shape;
	}

}
