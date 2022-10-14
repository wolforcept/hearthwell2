package wolforce.hearthwell.integration.jei;

import static wolforce.hearthwell.data.MapData.DATA;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.recipes.RecipeTransformation;
import wolforce.hearthwell.integration.jei.meta.JeiCat;

public class JeiCatTransformation extends JeiCat<RecipeTransformation> {

	public JeiCatTransformation() {
		super(RecipeTransformation.class, HearthWell.MODID, "Transformation Recipes", "transformation", RecipeTransformation.WIDTH, RecipeTransformation.HEIGHT, HearthWell.myst_dust);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeTransformation recipe, IFocusGroup focuses) {
		DATA();
		builder.addSlot(RecipeIngredientRole.INPUT, 8, 8).addIngredients(IngredientFlare.INGREDIENT_TYPE, IngredientFlare.get(recipe.flareType));
		builder.addSlot(RecipeIngredientRole.INPUT, 40, 31).addItemStacks(recipe.getInputStack());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 31).addItemStacks(recipe.getOutputStacksFlat());
	}

	@Override
	public List<RecipeTransformation> getAllRecipes() {
		return DATA().recipes_transformation;
	}

	@Override
	public boolean isToAddIconAsCatalyst() {
		return false;
	}

}
