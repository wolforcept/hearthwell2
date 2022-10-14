package wolforce.hearthwell.integration.jei;

import static java.util.stream.Collectors.toList;
import static wolforce.hearthwell.data.MapData.DATA;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import wolforce.hearthwell.HearthWell;
import wolforce.utils.client.UtilRender;
import wolforce.utils.collections.UtilList;

public class IngredientFlare {

	public static final IIngredientType<IngredientFlare> INGREDIENT_TYPE = () -> IngredientFlare.class;

	public final String flareType;
	public final String name;
	public final float[] rgb;

	private IngredientFlare(String name, String flareId, String hexColor) {
		this.name = name;
		this.flareType = flareId;
		this.rgb = UtilRender.hex2Rgb(hexColor);
	}

	public static List<IngredientFlare> getAll() {
		return DATA().recipes_flare.stream().map(s -> new IngredientFlare(s.flare_name, s.recipeId, s.color_string)).collect(toList());
	}

	public static List<IngredientFlare> get(String flareType) {
		return DATA().recipes_flare.stream() //
				.map(recipe -> new IngredientFlare(recipe.flare_name, recipe.recipeId, recipe.color_string)) //
				.filter(ingredient -> ingredient.flareType.equals(flareType)) //
				.collect(toList());
	}

	@Override
	public String toString() {
		return "IngredientFlare [flareType=" + flareType + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		return flareType.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IngredientFlare other = (IngredientFlare) obj;
		if (flareType == null) {
			if (other.flareType != null)
				return false;
		} else if (!flareType.equals(other.flareType))
			return false;
		return true;
	}

	public static class Renderer implements IIngredientRenderer<IngredientFlare> {

		public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(HearthWell.MODID, "textures/gui/flare_icon.png");

		@Override
		public List<Component> getTooltip(IngredientFlare flare, TooltipFlag arg1) {
			return UtilList.of(new TextComponent(flare.name));
		}

		@Override
		public void render(PoseStack matrix, int x, int y, IngredientFlare flare) {
			RenderSystem.setShaderTexture(0, GUI_TEXTURE);
			RenderSystem.enableBlend();
//			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
			RenderSystem.setShaderColor(flare.rgb[0], flare.rgb[1], flare.rgb[2], 1);
			GuiComponent.blit(matrix, x, y, /* w,h */16, 16, /* u,v */0, 0, /* uw,vh */16, 16, /* w,h */16, 16);
		}

	}

	public static class Helper implements IIngredientHelper<IngredientFlare> {

		@Override
		public IngredientFlare copyIngredient(IngredientFlare flare) {
			return flare;
		}

		@Override
		public String getDisplayName(IngredientFlare flare) {
			return flare.name;
		}

		@Override
		public String getErrorInfo(IngredientFlare flare) {
			return flare.toString();
		}

		@Override
		public @Nullable IngredientFlare getMatch(Iterable<IngredientFlare> flares, IngredientFlare flare, UidContext context) {
			for (IngredientFlare ingredientFlare : flares) {
				if (ingredientFlare.flareType.equals(flare.flareType))
					return ingredientFlare;
			}
			return null;
		}

		@Override
		public String getModId(IngredientFlare flare) {
			return HearthWell.MODID;
		}

		@Override
		public String getResourceId(IngredientFlare flare) {
			return flare.flareType;
		}

		@Override
		public String getUniqueId(IngredientFlare flare, UidContext context) {
			return flare.flareType;
		}

		@Override
		public IIngredientType<IngredientFlare> getIngredientType() {
			return IngredientFlare.INGREDIENT_TYPE;
		}
	}

}
