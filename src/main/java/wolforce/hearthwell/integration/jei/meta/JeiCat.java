package wolforce.hearthwell.integration.jei.meta;

import java.util.LinkedList;
import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import wolforce.hearthwell.data.RecipeHearthWell;
import wolforce.utils.Util;
import wolforce.utils.stacks.UtilItemStack;

public abstract class JeiCat<T extends RecipeHearthWell> implements IRecipeCategory<T> {

	public static IJeiHelpers helpers;

	private String modid, title;
	private RecipeType<T> recipeType;
	private ResourceLocation uid;
	private ResourceLocation texture;
	// private int w, h;

	private IDrawableStatic back;
	private IDrawable icon;

	private LinkedList<ItemStack> catalysts;

	private Class<T> recipeClass;

	public final int w, h;

	public JeiCat(Class<T> recipeClass, String modid, String title, String recipeId, int w, int h, Object iconStack) {
		this.recipeClass = recipeClass;
		this.modid = modid;
		this.title = title;
		this.uid = Util.res(modid, recipeId);
		this.recipeType = new RecipeType<T>(uid, recipeClass);
		this.texture = Util.res(modid, "textures/gui/" + recipeId + ".png");
		this.w = w;
		this.h = h;

		this.back = helpers.getGuiHelper().drawableBuilder(texture, 0, 0, w, h).setTextureSize(w, h).build();
		this.icon = helpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, UtilItemStack.stack(iconStack));

		catalysts = new LinkedList<>();
		if (isToAddIconAsCatalyst())
			catalysts.add(UtilItemStack.stack(iconStack));

	}

	@Override
	public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView slots, double mouseX, double mouseY) {
		List<Component> list = new LinkedList<>();
		if (mouseX > w - 8 && mouseY < 8) {
			if (recipe.mapNode != null)
				list.add(new TextComponent("Requires Research: " + recipe.mapNode.name));
			else
				list.add(new TextComponent("Does not require Research."));
		}
		return list;
	}

//	@Override
//	public Class<T> getRecipeClass() {
//		return recipeClass;
//	}

	public boolean isToAddIconAsCatalyst() {
		return true;
	}

	@Override
	public ResourceLocation getUid() {
		return uid;
	}

	@Override
	public Component getTitle() {
		return new TextComponent(title);
	}

	public String getModid() {
		return modid;
	}

	@Override
	public IDrawable getBackground() {
		return back;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	public List<ItemStack> getCatalysts() {
		return catalysts;
	}

	public void addCatalyst(Item item) {
		catalysts.add(new ItemStack(item));
	}

	public void addCatalyst(Block block) {
		catalysts.add(new ItemStack(block));
	}

	public abstract List<T> getAllRecipes();

	public Rect2i getClickArea() {
		return null;
	}

	protected Class<AbstractContainerScreen<?>> getGuiClass() {
		return null;
	}

	@Override
	public RecipeType<T> getRecipeType() {
		return recipeType;
	}

	@Override
	public Class<? extends T> getRecipeClass() {
		return recipeClass;
	}

	public void registerAllRecipes(IRecipeRegistration reg) {
		reg.addRecipes(getRecipeType(), getAllRecipes());
	}

//	public Class<? extends GuiContainer> getGuiClass() {
//		return null;
//	}

}
