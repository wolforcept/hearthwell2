package wolforce.hearthwell.items;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import wolforce.hearthwell.TokenNames;
import wolforce.hearthwell.entities.EntityTokenChalkMark;

public class ItemTokenChalk extends Item {

	public ItemTokenChalk(Properties props) {
		super(props.durability(256));
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

		float SIZE = EntityTokenChalkMark.SIZE;
		Level world = context.getLevel();

		BlockPos blockpos = context.getClickedPos();
		
		String letterOfPos = "" + TokenNames.getLetterOfPos(blockpos);
		EntityTokenChalkMark mark = new EntityTokenChalkMark(world);
		Direction dir = context.getClickedFace();
		System.out.println(dir);
		Vec3 pos = switch (dir) {
		case UP -> new Vec3(.5, 1 - SIZE / 2, .5);
		case DOWN -> new Vec3(.5, -SIZE / 2, .5);
		case NORTH -> new Vec3(.5, .5 - SIZE / 2, -.011);
		case SOUTH -> new Vec3(.5, .5 - SIZE / 2, 1.011);
		case EAST -> new Vec3(1.011, .5 - SIZE / 2, .5);
		case WEST -> new Vec3(-.011, .5 - SIZE / 2, .5);
		};
		mark.setPos(blockpos.getX() + pos.x, blockpos.getY() + pos.y, blockpos.getZ() + pos.z);
		mark.set(letterOfPos, dir);

		List<EntityTokenChalkMark> entities = world.getEntitiesOfClass(EntityTokenChalkMark.class, mark.getBoundingBox());
		for (EntityTokenChalkMark entityTokenChalkMark : entities) {
			entityTokenChalkMark.kill();
		}

		if (entities.isEmpty())
			if (stack.isDamageableItem()) {
				stack.setDamageValue(stack.getDamageValue() + 1);
			}
		world.addFreshEntity(mark);
//		world.getEntitiesOfClass(EntityT, null)
		return InteractionResult.SUCCESS;
	}

}
