package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.stacks.UtilItemStack;

public class RecipeCombining extends RecipeHearthWell {

	public static final int WIDTH = 120, HEIGHT = 66;
	private static final long serialVersionUID = HearthWell.NETDATA_VERSION.hashCode();

	public RecipeCombining(String name, String input, String output) {
		super(name, input, output);
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

	@Override
	public String getTypeString() {
		return "combining";
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

}
