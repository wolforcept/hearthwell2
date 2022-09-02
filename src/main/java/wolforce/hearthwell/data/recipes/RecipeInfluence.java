package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;

public class RecipeInfluence extends RecipeHearthWell {

	private static final long serialVersionUID = HearthWell.VERSION.hashCode();

	public static final int WIDTH = 82, HEIGHT = 78;

	public RecipeInfluence(String name, String input, String output) {
		super("influence", name, WIDTH, HEIGHT, input, output);
	}

	public boolean matches(Block block) {
		for (List<Block> blocks : getInputBlocks()) {
			for (Block block2 : blocks) {
				if (block == block2)
					return true;
			}
		}
		return false;
	}

}
