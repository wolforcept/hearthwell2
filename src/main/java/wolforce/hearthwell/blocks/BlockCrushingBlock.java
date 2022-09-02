package wolforce.hearthwell.blocks;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import wolforce.hearthwell.bases.BaseFallingBlock;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.recipes.RecipeCrushing;
import wolforce.utils.stacks.UtilItemStack;

public class BlockCrushingBlock extends BaseFallingBlock {

	public BlockCrushingBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onLand(Level world, BlockPos pos, BlockState state1, BlockState state2, FallingBlockEntity entity) {
		List<ItemEntity> entities = world.getEntitiesOfClass(ItemEntity.class, new AABB(pos));

		for (ItemEntity itemEntity : entities) {
			for (RecipeCrushing recipe : MapData.DATA.recipes_crushing) {
				if (recipe.matches(itemEntity.getItem())) {
					itemEntity.kill();
					if (!world.isClientSide)
						for (ItemStack _newItem : recipe.getOutputStacks()) {
							ItemStack newItem = _newItem.copy();
							newItem.setCount(itemEntity.getItem().getCount() * _newItem.getCount());
							ItemEntity newItemEntity = new ItemEntity(world, pos.getX(), pos.getY() + .5f,
									pos.getZ() + .5f, newItem);
							world.addFreshEntity(newItemEntity);
						}
				}
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
			BlockHitResult res) {

//		if (player.isShiftKeyDown()) {
		if (UtilItemStack.isValid(player.getMainHandItem())) {
			world.destroyBlock(pos, true);
		} else {
			world.destroyBlock(pos, false);
			player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(this));
		}
		return InteractionResult.CONSUME;
//		}

//		return InteractionResult.PASS;
	}
}
