package wolforce.hearthwell.integration.jei;

import static wolforce.hearthwell.data.MapData.DATA;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.renderer.Rect2i;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.recipes.RecipeCombining;
import wolforce.hearthwell.integration.jei.meta.JeiCat;

public class JeiCatCombining extends JeiCat<RecipeCombining> {

	public JeiCatCombining() {
		super(RecipeCombining.class, HearthWell.MODID, "Combining Recipes", "combining", RecipeCombining.WIDTH, RecipeCombining.HEIGHT, HearthWell.myst_dust);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeCombining recipe, IFocusGroup focuses) {
		DATA();
		builder.addSlot(RecipeIngredientRole.INPUT, 5, 53).addItemStacks(recipe.getInputStack());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 19, 5).addItemStacks(recipe.getOutputStacksFlat());
	}

	@Override
	public List<RecipeCombining> getAllRecipes() {
		MapData DATA = DATA();
		return DATA.recipes_combining;
	}

	@Override
	public boolean isToAddIconAsCatalyst() {
		return false;
	}

	@Override
	public Rect2i getClickArea() {
		return new Rect2i(18, 26, 17, 22);
	}

}
