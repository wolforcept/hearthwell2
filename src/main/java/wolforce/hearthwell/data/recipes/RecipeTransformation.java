package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;

public class RecipeTransformation extends RecipeHearthWell {

	public static final int WIDTH = 117, HEIGHT = 58;
	private static final long serialVersionUID = HearthWell.NETDATA_VERSION.hashCode();

	public final String flareType;

	public RecipeTransformation(String recipeId, String flareType, String input, String output) {
		super(recipeId, input, output);
		this.flareType = flareType;
	}

	@Override
	public String getTypeString() {
		return "transformation";
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
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
