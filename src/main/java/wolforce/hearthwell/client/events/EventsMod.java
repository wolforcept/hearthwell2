package wolforce.hearthwell.client.events;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.bases.BlockHasRenderLayer;
import wolforce.hearthwell.client.render.entity.RendererFlare;
import wolforce.hearthwell.client.render.entity.RendererHearthWell;
import wolforce.hearthwell.client.render.entity.RendererSpire;
import wolforce.hearthwell.client.render.entity.RendererTokenChalkMark;
import wolforce.hearthwell.client.render.te.TerBurstSeed;
import wolforce.hearthwell.net.ClientProxy;
import wolforce.hearthwell.particles.ParticleEnergy;
import wolforce.hearthwell.particles.ParticleEnergyData;
import wolforce.hearthwell.registries.Entities;
import wolforce.hearthwell.registries.TileEntities;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventsMod {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {

		for (Block block : HearthWell.blocks.values()) {

			if (block instanceof BlockHasRenderLayer)
				registerRenderLayer(block);

		}
	}

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
//		event.getBlockColors().register(HearthWell.spireColor, HearthWell.spire);
	}

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(TileEntities.burst_seed.get(), TerBurstSeed::new);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(Entities.entity_hearthwell.get(), RendererHearthWell::new);
		event.registerEntityRenderer(Entities.entity_flare.get(), RendererFlare::new);
		event.registerEntityRenderer(Entities.entity_spire.get(), RendererSpire::new);
		event.registerEntityRenderer(Entities.entity_tokenchalkmark.get(), RendererTokenChalkMark::new);
	}

	private static void registerRenderLayer(Block block) {
		if (block instanceof BlockHasRenderLayer.Cutout)
			ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout()::equals);
		if (block instanceof BlockHasRenderLayer.CutoutMipped)
			ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()::equals);
		if (block instanceof BlockHasRenderLayer.Translucent)
			ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent()::equals);
	}

	@SubscribeEvent
	public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
		ParticleEngine particleEngine = ClientProxy.MC.particleEngine;
		particleEngine.register(ParticleEnergyData.TYPE, ParticleEnergy.Factory::new);
	}

}
