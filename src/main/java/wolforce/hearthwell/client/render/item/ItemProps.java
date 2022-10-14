package wolforce.hearthwell.client.render.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.items.ItemMystContainer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemProps {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {

		ItemProperties.register(HearthWell.myst_container, new ResourceLocation("fueltype"), (stack, level, entity, i) -> {
			if (stack.getItem() instanceof ItemMystContainer item) {
				return item.getFuelType(stack);
			}
			return 0;
		});
	}
}
