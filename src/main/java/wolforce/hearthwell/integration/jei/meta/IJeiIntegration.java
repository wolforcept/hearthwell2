package wolforce.hearthwell.integration.jei.meta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import wolforce.utils.Util;

public abstract class IJeiIntegration implements IModPlugin {

	private LinkedList<JeiCat<?>> jeiCats;
	private HashMap<Object, String[]> infos;
	private boolean catsAreRegistered = false;

	public IJeiIntegration() {
		// MinecraftForge.EVENT_BUS.register(this);
		jeiCats = new LinkedList<JeiCat<?>>();
		infos = new HashMap<>();
	}

	// CATEGORIES

	public void add(JeiCat<?> cat) {
		jeiCats.add(cat);
	}

	public LinkedList<JeiCat<?>> getCats() {
		return jeiCats;
	}

	// INGREDIENT INFOS

	public void addInfo(Object object, String... infoArray) {
		infos.put(object, infoArray);
	}

	public Set<Entry<Object, String[]>> getInfos() {
		return infos.entrySet();
	}

	public abstract void registerCategories();

	@Override
	public void registerCategories(IRecipeCategoryRegistration reg) {

		JeiCat.helpers = reg.getJeiHelpers();
		if (!catsAreRegistered) {
			registerCategories();
			catsAreRegistered = true;
		}

		for (JeiCat<?> cat : getCats())
			reg.addRecipeCategories(cat);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
		for (JeiCat<?> cat : getCats()) {
			for (ItemStack catalyst : cat.getCatalysts()) {
				reg.addRecipeCatalyst(catalyst, cat.getRecipeType());
			}
		}
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration reg) {
		for (JeiCat<?> cat : getCats()) {
			Rect2i clickArea = cat.getClickArea();
			if (clickArea != null && cat.getGuiClass() != null)
				reg.addRecipeClickArea(cat.getGuiClass(), clickArea.getX(), clickArea.getY(), clickArea.getWidth(), clickArea.getHeight(), cat.getRecipeType());
		}
	}

	@Override
	public void registerRecipes(IRecipeRegistration reg) {

		for (JeiCat<?> cat : getCats())
			cat.registerAllRecipes(reg);

		for (Entry<Object, String[]> entry : getInfos()) {
			Object obj = entry.getKey();

			if (obj instanceof Item) {
				reg.addIngredientInfo(new ItemStack((Item) obj), VanillaTypes.ITEM_STACK, Util.stringsToComponents(entry.getValue()));

			} else if (obj instanceof Block) {
				reg.addIngredientInfo(new ItemStack((Block) obj), VanillaTypes.ITEM_STACK, Util.stringsToComponents(entry.getValue()));
			}
		}

		registerOther(reg);
	}

	protected void registerOther(IRecipeRegistration reg) {

	}

}
