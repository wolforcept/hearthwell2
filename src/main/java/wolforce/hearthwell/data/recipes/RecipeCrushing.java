package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.stacks.UtilItemStack;

public class RecipeCrushing extends RecipeHearthWell {

	private static final long serialVersionUID = HearthWell.VERSION.hashCode();

	public static final int WIDTH = 120, HEIGHT = 50;

	public RecipeCrushing(String name, String input, String output) {
		super("crushing", name, WIDTH, HEIGHT, input, output);
	}

	public boolean matches(ItemStack stack) {
		for (List<ItemStack> stacks : getInputStacks()) {
			for (ItemStack stack2 : stacks) {
				if (UtilItemStack.equalExceptAmount(stack, stack2))
					return true;
			}
		}
		return false;
	}

}
