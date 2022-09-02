package wolforce.hearthwell.blocks;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.bases.BaseBlock;
import wolforce.hearthwell.bases.BlockHasRenderLayer;
import wolforce.utils.collections.UtilList;

public class BlockCore extends BaseBlock implements BlockHasRenderLayer.Translucent {

	private static final VoxelShape bigShape = Shapes.or(box(6, 0, 6, 16 - 6, 6, 16 - 6), Shapes.or(box(5, 1, 6, 16 - 5, 5, 16 - 6), box(6, 1, 5, 16 - 6, 5, 16 - 5)));
	private static final VoxelShape smallShape = box(6, 0, 6, 16 - 6, 4, 16 - 6);

	private final boolean isSmall;
	private final Block drop;

	public BlockCore(Properties properties) {
		super(properties);
		isSmall = true;
		drop = null;
	}

	public BlockCore(Properties properties, Block smallCore) {
		super(properties);
		isSmall = true;
		drop = smallCore;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return isSmall ? smallShape : bigShape;
	}

	@Override
	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, Builder builder) {
		if (drop == null)
			return super.getDrops(state, builder);
		return UtilList.of(new ItemStack(drop));
	}

}
