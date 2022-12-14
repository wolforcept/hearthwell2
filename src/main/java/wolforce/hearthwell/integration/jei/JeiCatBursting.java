package wolforce.hearthwell.integration.jei;

import static wolforce.hearthwell.data.MapData.DATA;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.recipes.RecipeBurstSeed;
import wolforce.hearthwell.integration.jei.meta.JeiCat;

public class JeiCatBursting extends JeiCat<RecipeBurstSeed> {

	public JeiCatBursting() {
		super(RecipeBurstSeed.class, HearthWell.MODID, "Burst Seed Recipes", "bursting", RecipeBurstSeed.WIDTH, RecipeBurstSeed.HEIGHT, HearthWell.burst_seed);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeBurstSeed recipe, IFocusGroup focuses) {
		DATA();
		builder.addSlot(RecipeIngredientRole.INPUT, 13, 35).addItemStacks(recipe.getInputStack());
	}

	@Override
	public List<RecipeBurstSeed> getAllRecipes() {
		return DATA().recipes_burst;
	}

}
