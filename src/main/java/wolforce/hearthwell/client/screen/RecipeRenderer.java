package wolforce.hearthwell.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.hearthwell.data.recipes.RecipeBurstSeed;
import wolforce.hearthwell.data.recipes.RecipeCombining;
import wolforce.hearthwell.data.recipes.RecipeCoring;
import wolforce.hearthwell.data.recipes.RecipeCrushing;
import wolforce.hearthwell.data.recipes.RecipeFlare;
import wolforce.hearthwell.data.recipes.RecipeHandItem;
import wolforce.hearthwell.data.recipes.RecipeInfluence;
import wolforce.hearthwell.data.recipes.RecipeReacting;
import wolforce.hearthwell.data.recipes.RecipeTransformation;
import wolforce.hearthwell.integration.jei.IngredientFlare;
import wolforce.utils.client.UtilRenderItem;
import wolforce.utils.collections.UtilList;

public class RecipeRenderer {
	private static final ResourceLocation texFlareIcon = new ResourceLocation(HearthWell.MODID, "textures/gui/flare_icon.png");

	private ScreenHearthWellMap mapScreen;
	private Vec2 mousePos;
	private PoseStack pose;
	private int secs;
	private int x;
	private int y;

	public RecipeRenderer(ScreenHearthWellMap mapScreen, Vec2 mousePos, PoseStack pose, int secs, int x, int y) {
		this.mapScreen = mapScreen;
		this.mousePos = mousePos;
		this.pose = pose;
		this.secs = secs;
		this.x = x;
		this.y = y;
	}

	public void render(RecipeHearthWell _recipe) {
		if (_recipe instanceof RecipeBurstSeed recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeCombining recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeCoring recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeCrushing recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeFlare recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeHandItem recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeInfluence recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeReacting recipe)
			renderRecipe(recipe);
		else if (_recipe instanceof RecipeTransformation recipe)
			renderRecipe(recipe);

		if (mousePos.y - this.y > 0 && mousePos.y - this.y - 8 < 0 && //
				mousePos.x - this.x - _recipe.getWidth() + 8 > 0 && mousePos.x - this.x - _recipe.getWidth() < 0) {
			String text = _recipe.mapNode != null ? "Requires Research: " + _recipe.mapNode.name : "Does not require Research.";
			mapScreen.renderComponentTooltip(new PoseStack(), UtilList.of(new TextComponent(text)), (int) mousePos.x, (int) mousePos.y);

		}

	}

	private void renderRecipe(RecipeBurstSeed recipe) {
		renderStacks(recipe.getInputStack(), 13, 35);
	}

	private void renderRecipe(RecipeCombining recipe) {
		renderStacks(recipe.getInputStack(), 9, 53);
		renderStacks(recipe.getOutputStacksFlat(), 57, 53);
	}

	private void renderRecipe(RecipeCoring recipe) {
		renderStacks(recipe.getInputStack(), 52, 9);
		renderStack(getTokenStack(recipe.tokenId), 9, 22);
		renderStacks(recipe.getOutputStacksFlat(), 95, 22);

	}

	private void renderRecipe(RecipeCrushing recipe) {
		renderStacks(recipe.getInputStack(), 9, 53);
		renderStacks(recipe.getOutputStacksFlat(), 57, 53);
	}

	private void renderRecipe(RecipeFlare recipe) {
		List<List<ItemStack>> inputLists = recipe.getInputStacks();
		int nInputLists = inputLists.size();
		double angle = Math.PI * 2 / nInputLists;
		for (int inputListIndex = 0; inputListIndex < nInputLists; inputListIndex++) {
			int dx = (int) (Math.cos(angle * inputListIndex) * 25);
			int dy = (int) (Math.sin(angle * inputListIndex) * 25);
			List<ItemStack> inputs = inputLists.get(inputListIndex);
			if (inputs != null && !inputs.isEmpty()) {
				int i = secs % inputs.size();
				UtilRenderItem.init(inputs.get(i)).pos(x + 41 - 9 + dx, y + 41 - 9 + dy, 500).screen(mapScreen).mouse(mousePos).renderGui();
			}
		}

		renderStacks(recipe.getOutputStacksFlat(), 57, 53);

		renderFlare(recipe.recipeId, 108, 32);
	}

	private void renderRecipe(RecipeHandItem recipe) {
		renderStacks(recipe.getInputStack(), 36, 34);
		renderStacks(recipe.getOutputStacksFlat(), 83, 37);
		renderFlare(recipe.flareType, 7, 7);
	}

	private void renderRecipe(RecipeInfluence recipe) {
		renderStacks(recipe.getInputStack(), 9, 53);
		renderStacks(recipe.getOutputStacksFlat(), 57, 53);

		renderFlare(recipe.recipeId, 108, 32);
	}

	private void renderRecipe(RecipeReacting recipe) {
		renderStack(getTokenStack(recipe.tokenId), 9, 22);

		renderStacks(recipe.getInputStack(), 95, 22);
		renderStacks(recipe.getOutputStacksFlat(), 52, 35);
	}

	private void renderRecipe(RecipeTransformation recipe) {
		renderStacks(recipe.getInputStack(), 40, 31);
		renderStacks(recipe.getOutputStacksFlat(), 92, 31);

		renderFlare(recipe.flareType, 8, 8);
	}

	//
	// HELPER METHODS
	//

	private ItemStack getTokenStack(int tokenId) {
		return new ItemStack(HearthWell.getTokenItem(tokenId));
	}

	private void renderStacks(List<ItemStack> stacks, int x, int y) {
		if (stacks != null && !stacks.isEmpty()) {
			int i = secs % stacks.size();
			UtilRenderItem.init(stacks.get(i)).pos(this.x + x, this.y + y, 500).screen(mapScreen).mouse(mousePos).renderGui();
		}
	}

	private void renderStack(ItemStack stack, int x, int y) {
		UtilRenderItem.init(stack).pos(this.x + x, this.y + y, 500).screen(mapScreen).mouse(mousePos).renderGui();
	}

	private void renderFlare(String flareType, int x, int y) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, texFlareIcon);
		List<IngredientFlare> flares = IngredientFlare.get(flareType);
		if (flares != null && !flares.isEmpty()) {
			int flare_index = secs % flares.size();
			IngredientFlare flare = flares.get(flare_index);
			RenderSystem.setShaderColor(flare.rgb[0], flare.rgb[1], flare.rgb[2], 1);
			GuiComponent.blit(pose, this.x + x, this.y + y, /* w,h */16, 16, /* u,v */0, 0, /* uw,vh */16, 16, /* w,h */16, 16);
			if (mousePos.x > this.x + x && mousePos.y > this.y + y && mousePos.x < this.x + x + 16 && mousePos.y < this.y + y + 16)
				mapScreen.renderComponentTooltip(new PoseStack(), UtilList.of(new TextComponent(flare.name)), (int) mousePos.x, (int) mousePos.y);

		}
	}

}
