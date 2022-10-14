package wolforce.hearthwell.data.recipes;

import java.util.List;

import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;

public class RecipeInfluence extends RecipeHearthWell {

	public static final int WIDTH = 82, HEIGHT = 78;
	private static final long serialVersionUID = HearthWell.NETDATA_VERSION.hashCode();

	public RecipeInfluence(String name, String input, String output) {
		super(name, input, output);
	}

	@Override
	public String getTypeString() {
		return "influence";
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
