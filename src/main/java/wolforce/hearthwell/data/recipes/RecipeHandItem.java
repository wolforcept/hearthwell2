package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.stacks.UtilItemStack;

public class RecipeHandItem extends RecipeHearthWell {

	private static final long serialVersionUID = HearthWell.VERSION.hashCode();

//	private final String[] inputs;
//	public final String output;
	public final String flareType;
//	private transient Item realOutput;

	public static final int WIDTH = 108, HEIGHT = 68;

	public RecipeHandItem(String name, String flareType, String input, String output) {
		super("handitem", name, WIDTH, HEIGHT, input, output);
		this.flareType = flareType;
//		this.input = input;
//		this.output = output;
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
