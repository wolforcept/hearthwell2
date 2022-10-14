package wolforce.hearthwell.blocks;

import static wolforce.hearthwell.data.MapData.DATA;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import wolforce.hearthwell.data.recipes.RecipeCrushing;
import wolforce.utils.stacks.UtilItemStack;

public class BlockCrushingBlock extends BaseFallingBlock {

	public BlockCrushingBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onLand(Level level, BlockPos pos, BlockState state1, BlockState state2, FallingBlockEntity entity) {
		List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos));

		for (ItemEntity itemEntity : entities) {
			for (RecipeCrushing recipe : DATA().recipes_crushing) {
				if (recipe.matches(itemEntity.getItem())) {
					itemEntity.kill();
					level.playSound(null, pos, SoundEvents.HUSK_STEP, SoundSource.BLOCKS, 1, 1);
					if (!level.isClientSide)
						for (ItemStack _newItem : recipe.getOutputStacks()) {
							ItemStack newItem = _newItem.copy();
							newItem.setCount(itemEntity.getItem().getCount() * _newItem.getCount());
							ItemEntity newItemEntity = new ItemEntity(level, pos.getX(), pos.getY() + .5f, pos.getZ() + .5f, newItem);
							level.addFreshEntity(newItemEntity);
						}
				}
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res) {

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
