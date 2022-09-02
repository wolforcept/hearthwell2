package wolforce.hearthwell.client.render.particle;

import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.net.ClientProxy;

public class RenderTypeEnergy implements ParticleRenderType {

	private boolean dark;

	public RenderTypeEnergy() {
	}

	public RenderTypeEnergy dark() {
		dark = Math.random() < .5;
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {

//		RenderSystem.depthMask(false);
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();

//		if (Math.random() < .1)
//			RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		else

//		if (dark)
//			RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
//		else
		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);

//		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

//		RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.SRC_ALPHA, DestFactor.ZERO);
//		RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);

//		RenderSystem.blendFuncSeparate(//
//				/* COLOR */ SourceFactor.SRC_COLOR, DestFactor.ZERO, //
//				/* ALPHA */ SourceFactor.SRC_ALPHA, DestFactor.SRC_ALPHA //
//		);

//		RenderSystem.blendEquation(GL14.GL_ADD);

//		RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA);
//		RenderSystem.disableLighting();

//		RenderSystem.blendFunc(SourceFactor.ONE, DestFactor.ONE);
//		RenderSystem.blendEquation(GL14.GL_FUNC_REVERSE_SUBTRACT);

//		textureManager.bindForSetup(TextureAtlas.LOCATION_PARTICLES);
		RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
		textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).setBlurMipmap(true, false);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);

	}

	@SuppressWarnings("deprecation")
	@Override
	public void end(Tesselator tessellator) {
		tessellator.end();
		ClientProxy.MC.textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
//		RenderSystem.defaultAlphaFunc();
		RenderSystem.disableBlend();
		RenderSystem.enableDepthTest();
//		RenderSystem.depthMask(true);
//		RenderSystem.enableLighting();
		RenderSystem.blendEquation(GL14.GL_FUNC_ADD);
	}

	@Override
	public String toString() {
		return HearthWell.MODID + ":energy_particle_render_type";
	}

}
