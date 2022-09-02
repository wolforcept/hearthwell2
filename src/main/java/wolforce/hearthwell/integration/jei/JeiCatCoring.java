package wolforce.hearthwell.integration.jei;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.recipes.RecipeCoring;
import wolforce.hearthwell.integration.jei.meta.JeiCat;

public class JeiCatCoring extends JeiCat<RecipeCoring> {

	public JeiCatCoring() {
		super(RecipeCoring.class, HearthWell.MODID, "Coring Recipes", "coring", RecipeCoring.WIDTH, RecipeCoring.HEIGHT, HearthWell.spire_device_core_turning);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeCoring recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 9).addItemStacks(recipe.getInputStack());
		builder.addSlot(RecipeIngredientRole.INPUT, 9, 22).addItemStack(new ItemStack(HearthWell.getTokenItem(recipe.tokenId)));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 22).addItemStacks(recipe.getOutputStacksFlat());
	}

	@Override
	public List<RecipeCoring> getAllRecipes() {
		return MapData.DATA.recipes_coring;
	}

	@Override
	public Rect2i getClickArea() {
		return new Rect2i(18, 26, 17, 22);
	}

}
