//package wolforce.hearthwell.blocks;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import wolforce.hearthwell.HearthWell;
//import wolforce.hearthwell.bases.BaseBlock;
//import wolforce.hearthwell.bases.BlockHasRenderLayer;
//import wolforce.hearthwell.items.ItemMystContainer;
//
//public class BlockMystContainer extends BaseBlock implements BlockHasRenderLayer.Translucent {
//
//	final VoxelShape shape = box(4, 0, 4, 12, 11, 12);
//
//	public BlockMystContainer(Properties props) {
//		super(props.noOcclusion());
//	}
//
//	//
//	//
//	//
//
//	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
//		return shape;
//	}
//
//	@Override
//	public Item getItem(Block block) {
//		Item.Properties props = new Item.Properties().tab(HearthWell.group).stacksTo(1);
//		Item item = new ItemMystContainer(block, props);
//		return item;
//	}
//}
