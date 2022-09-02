package wolforce.hearthwell.data.recipes;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.Util;

public class RecipeFlare extends RecipeHearthWell {

	private static final long serialVersionUID = HearthWell.VERSION.hashCode();

//	private final String[] inputs;
	public final String color_string;
	public final String flare_name;
	public final int uses;

	private transient int color;
	public transient String flareType;
//	private transient Object[] realInputs;

	public RecipeFlare(String recipeId, String flareName, String colorString, String input) {
		this(recipeId, flareName, colorString, 1, input);
	}

	public static final int WIDTH = 134, HEIGHT = 82;

	public RecipeFlare(String recipeId, String flareName, String colorString, int uses, String input) {
		super("flare2", recipeId, WIDTH, HEIGHT, input, "");
		this.flareType = recipeId;
		this.flare_name = flareName;
		this.color_string = colorString;
		this.uses = uses;
//		this.inputs = inputs;
//		color = 0xFFFFFFFF;
//		Integer.decode("0x" + color_string + "FF");
		color = Integer.parseInt(color_string, 16);
	}

	public int getColor() {
		return color + 0xFF000000;
	}

	public boolean matchesAllInputs(List<ItemStack> _available) {

		List<List<ItemStack>> _inputs = getInputStacks();

		List<List<ItemStack>> ingredients = new LinkedList<>();
		for (List<ItemStack> list : _inputs) {
			ingredients.add(new LinkedList<ItemStack>(list));
		}

		List<ItemStack> available = new LinkedList<>();
		for (ItemStack availableStack : _available) {
			available.add(availableStack.copy());
		}

		for (List<ItemStack> ingred : ingredients) {
			if (!findSuitableAndRemove(available, ingred))
				return false;
		}
		return true;
	}

	private boolean findSuitableAndRemove(List<ItemStack> available, List<ItemStack> ingredient) {

		for (ItemStack ingred : ingredient) {

			ItemStack searchNeededStack = Util.stackListFind_moreOrEqualNr(ingred, available);
			if (searchNeededStack != null) {
				searchNeededStack.shrink(ingred.getCount());
				return true;
			}
		}
		return false;
	}

}
