package wolforce.hearthwell.data.recipes;

import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;

public class RecipeReacting extends RecipeHearthWell {

	public static final int WIDTH = 120, HEIGHT = 60;
	private static final long serialVersionUID = HearthWell.NETDATA_VERSION.hashCode();

	public final int tokenId, cost;

	public RecipeReacting(String name, String input, int tokenId, int cost, String output) {
		super(name, input, output);
		this.tokenId = tokenId;
		this.cost = cost;
	}

	@Override
	public String getTypeString() {
		return "reacting";
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
