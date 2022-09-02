package wolforce.hearthwell.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.entities.EntityHearthWell;

public abstract class RecipeHearthWell implements Serializable {

	private static final long serialVersionUID = HearthWell.VERSION.hashCode();

	public transient MapNode mapNode;
	public final ResourceLocation texture;
	public final String recipeId;

	private final String input;
	private final String output;

	private transient List<RecipePart> inputParts;
	private transient List<RecipePart> outputParts;

	public final int width, height;

	protected RecipeHearthWell(String tex, String recipeId, int width, int height, String input, String output) {
		this.recipeId = recipeId;
		this.input = input;
		this.output = output;
		this.width = width;
		this.height = height;
		this.texture = new ResourceLocation(HearthWell.MODID, "textures/gui/" + tex + ".png");
	}

	public void init(MapData data, MapNode mapNode) {
		this.mapNode = mapNode;

		inputParts = new ArrayList<RecipePart>();
		outputParts = new ArrayList<RecipePart>();

		for (String inputString : this.input.split(","))
			inputParts.add(new RecipePart(inputString));

		for (String string : this.output.split(","))
			outputParts.add(new RecipePart(string));

		System.out.println("Recipe Inited: " + this.getClass().getSimpleName() + " " + recipeId);
		System.out.println(input + " => " + getInputStacks());
		System.out.println(output + " => " + getOutputStacksFlat());
		System.out.println("--//--");

		postInitRecipe(data);
	}

	public boolean isUnlocked(EntityHearthWell hw) {
		return hw.isUnlocked(mapNode);
	}

	public boolean isUnlocked(CompoundTag unlockedNodes) {
		return unlockedNodes.contains(mapNode.hash() + "");
	}

	public void postInitRecipe(MapData data) {
	}

	// Inputs

	public List<ItemStack> getInputStack() {
		return inputParts.get(0).stacks();
	}

	public List<List<ItemStack>> getInputStacks() {
		return inputParts.stream().map(x -> x.stacks()).toList();
	}

	public List<ItemStack> getInputStacksFlat() {
		List<ItemStack> list = new LinkedList<>();
		inputParts.stream().forEach(x -> list.addAll(x.stacks()));
		return list;
	}

	public List<List<Block>> getInputBlocks() {
		return inputParts.stream().map(x -> x.getBlocks()).toList();
	}

	public List<Block> getInputBlocksFlat() {
		List<Block> list = new LinkedList<>();
		List<List<Block>> blocks = inputParts.stream().map(x -> x.getBlocks()).toList();
		blocks.stream().forEach(x -> list.addAll(x));
		return list;
	}

	// Outputs

//	public ItemStack getOutputStack() {
//		return outputParts.get(0).stack();
//	}
//
//	public Block getOuputBlock() {
//		return outputParts.get(0).getBlock();
//	}

	public Block getRandomOuputBlock() {
		List<Block> blocks = getOutputBlocks();
		return blocks.get((int) (Math.random() * blocks.size()));
	}

	public List<ItemStack> getOutputStacksFlat() {
		List<ItemStack> list = new LinkedList<>();
		outputParts.stream().forEach(x -> list.addAll(x.stacks()));
		return list;
	}

	/**
	 * Gets the all outputs stacks. If there are choices, its random
	 */
	public List<ItemStack> getOutputStacks() {
		return outputParts.stream().map(x -> x.randomStack()).toList();
	}

	public ItemStack getOutputStacksFlatRandom() {
		List<ItemStack> list = new LinkedList<>();
		outputParts.stream().forEach(x -> list.addAll(x.stacks()));
		return list.get((int) (list.size() * Math.random()));
	}

	public List<List<ItemStack>> getOutputStackLists() {
		return outputParts.stream().map(x -> x.stacks()).toList();
	}

	private List<Block> getOutputBlocks() {
		return outputParts.stream().map(x -> x.randomBlock()).toList();
	}
}