package wolforce.hearthwell.data;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipePart {
	private final List<RecipePartItemstack> parts;

	public RecipePart(String string) {
		parts = new LinkedList<>();
		String[] stringParts = string.split("\\|");
		for (String stringPart : stringParts) {
			if (string.startsWith("#")) { // IS TAG
				parts.addAll(createPartsFromTag(stringPart));
			} else
				parts.add(new RecipePartItemstack(stringPart));
		}
	}

	public ItemStack randomStack() {
		if (parts.isEmpty())
			return ItemStack.EMPTY;
		int randomIndex = (int) (Math.random() * parts.size());
		return parts.get(randomIndex).toStack();
	}

	public Block randomBlock() {
		List<Block> blocks = getBlocks();
		if (blocks.isEmpty())
			return null;
		return blocks.get((int) (Math.random() * blocks.size()));
	}

	// get lists

	public List<Block> getBlocks() {
		return parts.stream().map(x -> x.block).toList();
	}

	public List<ItemStack> stacks() {
		return parts.stream().map(x -> x.toStack()).toList();
	}

	public List<Item> getItems() {
		return parts.stream().map(x -> x.item).toList();
	}

	@Override
	public String toString() {
		return "[" + this.parts + "]";
	}

	private List<RecipePartItemstack> createPartsFromTag(String string) {

		List<RecipePartItemstack> parts = new LinkedList<>();
		ResourceLocation resource = new ResourceLocation(string.substring(1));

		try {
			TagKey<Item> itemTag = ForgeRegistries.ITEMS.tags().createTagKey(resource);
			if (ForgeRegistries.ITEMS.tags().isKnownTagName(itemTag))
				parts.addAll(ForgeRegistries.ITEMS.tags().getTag(itemTag).stream().map(x -> new RecipePartItemstack(x, 1, null)).toList());
		} catch (Exception e) {
			System.err.println("Failed to resolve item tag <" + resource + ">");
		}

		try {
			TagKey<Block> blockTag = ForgeRegistries.BLOCKS.tags().createTagKey(resource);
			if (ForgeRegistries.BLOCKS.tags().isKnownTagName(blockTag))
				parts.addAll(ForgeRegistries.BLOCKS.tags().getTag(blockTag).stream().map(x -> new RecipePartItemstack(x, 1, null)).toList());
		} catch (Exception e) {
			System.err.println("Failed to resolve block tag <" + resource + ">");
		}

		return parts;
	}

	private static class RecipePartItemstack {

		int nr = 1;
		Item item;
		Block block;
		CompoundTag nbt;

		private RecipePartItemstack(Item item, int nr, CompoundTag nbt) {
			this.nr = nr;
			this.item = item;
			this.nbt = nbt;
			tryComplete();
		}

		private RecipePartItemstack(Block block, int nr, CompoundTag nbt) {
			this.nr = nr;
			this.block = block;
			this.nbt = nbt;
			this.item = block.asItem();
			tryComplete();
		}

		private RecipePartItemstack(String _string) {

			try {

				String[] parts = _string.split("\\*");
				String string = parts[parts.length > 1 ? 1 : 0];

				if (parts.length > 1)
					try {
						nr = Integer.parseInt(parts[0]);
					} catch (NumberFormatException e) {
						nr = 1;
					}

				ResourceLocation resource = new ResourceLocation(string);

				if (ForgeRegistries.ITEMS.containsKey(resource))
					item = ForgeRegistries.ITEMS.getValue(resource);

				if (ForgeRegistries.BLOCKS.containsKey(resource))
					block = ForgeRegistries.BLOCKS.getValue(resource);
			} catch (Exception e) {
				System.err.println("Failed to load recipe part <" + _string + ">");
			}

//			if (_string.equals("stone_crafting_materials"))
//				System.out.println(_string);

			tryComplete();

		}

		private void tryComplete() {
			if (item == null && block != null) {
				item = block.asItem();
				if (block instanceof LiquidBlock liquid) {
					item = liquid.getFluid().getBucket();
				}
			}

			if (block == null && item != null) {
				block = Block.byItem(item);
			}
		}

		@Override
		public String toString() {
			return nr + "*" + item + "/" + block + "{" + nbt + "}";
		}

		ItemStack toStack() {
			if (nbt != null)
				return new ItemStack(item, nr, nbt);
			return new ItemStack(item, nr);
		}

	}
}