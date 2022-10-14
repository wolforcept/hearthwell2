package wolforce.hearthwell.bases;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.HearthWell;

public interface BlockItemProperties {

	public default Item.Properties getItemProperties() {
		return new Item.Properties().tab(HearthWell.group);
	}

	public default Item getItem(Block block) {
		return new BlockItem(block, getItemProperties());
	}

}
