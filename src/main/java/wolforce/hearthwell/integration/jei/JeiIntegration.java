package wolforce.hearthwell.integration.jei;

import static wolforce.hearthwell.data.MapData.DATA;

import java.util.Map.Entry;

import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.integration.jei.meta.IJeiIntegration;
import wolforce.utils.Util;
import wolforce.utils.collections.UtilList;

@JeiPlugin
public class JeiIntegration extends IJeiIntegration {

	@Override
	public ResourceLocation getPluginUid() {
		return Util.res(HearthWell.MODID, "jei_plugin");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		DATA();
		super.onRuntimeAvailable(jeiRuntime);
	}

	@Override
	public void registerCategories() {

		add(new JeiCatTransformation());
		add(new JeiCatBursting());
		add(new JeiCatInfluence());
		add(new JeiCatFlare());
		add(new JeiCatHandItem());
		add(new JeiCatCrushing());
		add(new JeiCatCoring());
//		add(new JeiCatCombining());
		add(new JeiCatReacting());
	}

	@Override
	public void registerIngredients(IModIngredientRegistration reg) {
		reg.register(IngredientFlare.INGREDIENT_TYPE, IngredientFlare.getAll(), new IngredientFlare.Helper(), new IngredientFlare.Renderer());
	}

	@Override
	protected void registerOther(IRecipeRegistration reg) {
		for (Entry<Item, String> e : HearthWell.descriptions.entrySet()) {
			reg.addIngredientInfo(UtilList.of(new ItemStack(e.getKey())), VanillaTypes.ITEM_STACK, new TranslatableComponent("description.hearthwell." + e.getValue()));
		}
	}

}
