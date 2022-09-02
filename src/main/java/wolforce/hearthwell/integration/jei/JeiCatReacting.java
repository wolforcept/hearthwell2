package wolforce.hearthwell.integration.jei;

import java.util.List;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.MapData;
import wolforce.hearthwell.data.recipes.RecipeReacting;
import wolforce.hearthwell.integration.jei.meta.JeiCat;

public class JeiCatReacting extends JeiCat<RecipeReacting> {

	public JeiCatReacting() {
		super(RecipeReacting.class, HearthWell.MODID, "Reacting Recipes", "reacting", RecipeReacting.WIDTH, RecipeReacting.HEIGHT, HearthWell.spire_device_reactor);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeReacting recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 52, 35).addItemStacks(recipe.getInputStack());
		builder.addSlot(RecipeIngredientRole.INPUT, 9, 22).addItemStack(new ItemStack(HearthWell.getTokenItem(recipe.tokenId)));
		builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 22).addItemStacks(recipe.getOutputStacksFlat());
	}

	@Override
	public List<RecipeReacting> getAllRecipes() {
		return MapData.DATA.recipes_reacting;
	}

	@Override
	public Rect2i getClickArea() {
		return new Rect2i(18, 26, 17, 22);
	}

}
