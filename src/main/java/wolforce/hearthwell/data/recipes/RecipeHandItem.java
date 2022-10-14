package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.stacks.UtilItemStack;

public class RecipeHandItem extends RecipeHearthWell {

	private static final long serialVersionUID = HearthWell.NETDATA_VERSION.hashCode();

	public final String flareType;
	public static final int WIDTH = 108, HEIGHT = 68;

	public RecipeHandItem(String name, String flareType, String input, String output) {
		super(name, input, output);
		this.flareType = flareType;
	}

	@Override
	public String getTypeString() {
		return "handitem";
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
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
