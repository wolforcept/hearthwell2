package wolforce.hearthwell.integration.jei;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.recipes.RecipeHandItem;
import wolforce.hearthwell.integration.jei.meta.JeiCat;

public class JeiCatHandItem extends JeiCat<RecipeHandItem> {

	public JeiCatHandItem() {
		super(RecipeHandItem.class, HearthWell.MODID, "Flare Item Recipes", "handitem", RecipeHandItem.WIDTH, RecipeHandItem.HEIGHT, HearthWell.flare_torch);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHandItem recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addIngredients(IngredientFlare.INGREDIENT_TYPE, IngredientFlare.get(recipe.flareType));
		builder.addSlot(RecipeIngredientRole.INPUT, 36, 34).addItemStacks(recipe.getInputStack());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 83, 37).addItemStacks(recipe.getOutputStacksFlat());
	}

	@Override
	public List<RecipeHandItem> getAllRecipes() {
		return MapData.DATA.recipes_handitem;
	}

}
