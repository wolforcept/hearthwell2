package wolforce.hearthwell.data.recipes;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.Util;

public class RecipeFlare extends RecipeHearthWell {

	private static final long serialVersionUID = HearthWell.NETDATA_VERSION.hashCode();
	public static final int WIDTH = 134, HEIGHT = 82;

	public final String color_string;
	public final String flare_name;
	public final int initial_uses;

	private transient int color;
	public transient String flareType;

	@Override
	public void postInitRecipe(MapData data) {
		color = Integer.parseInt(color_string, 16) + 0xFF000000;
		flareType = recipeId;
	}

	public RecipeFlare(String recipeId, String flareName, String colorString, String input) {
		this(recipeId, flareName, colorString, 1, input);
	}

	public RecipeFlare(String recipeId, String flareName, String colorString, int uses, String input) {
		super(recipeId, input, "");
		this.flare_name = flareName;
		this.color_string = colorString;
		this.initial_uses = uses;
	}

	@Override
	public String getTypeString() {
		return "flare2";
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	public int getColor() {
		return color;
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
