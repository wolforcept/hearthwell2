package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;

public class RecipeTransformation extends RecipeHearthWell {

	public static final int WIDTH = 117, HEIGHT = 58;
	public static final int IN1X = 8, IN1Y = 8;
	public static final int IN2X = 0, IN2Y = 0;
	public static final int OUTX = 0, OUTY = 0;

	private static final long serialVersionUID = HearthWell.VERSION.hashCode();

	public final String flareType;

	public RecipeTransformation(String recipeId, String flareType, String input, String output) {
		super("transformation", recipeId, WIDTH, HEIGHT, input, output);
		this.flareType = flareType;
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
