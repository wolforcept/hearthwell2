package wolforce.hearthwell.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wolforce.hearthwell.client.render.particle.RenderTypeEnergy;

@OnlyIn(Dist.CLIENT)
public class ParticleEnergy extends TextureSheetParticle {

	public static enum ColorType {
		NORMAL(new RenderTypeEnergy()), DARK(new RenderTypeEnergy().dark());

		ParticleRenderType renderType;

		ColorType(ParticleRenderType renderType) {
			this.renderType = renderType;
		}
	}

	public static final Minecraft mc = Minecraft.getInstance();
	public static final String REG_ID = "particle_energy";

	private int color;
	private ColorType type;

	public ParticleEnergy(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, int color, int type) {
		super(world, x, y, z);
		this.color = color;
		this.type = ColorType.values()[type];
		this.gravity = 0.0F;
		this.hasPhysics = false;
		this.xd = xSpeed;
		this.yd = ySpeed;
		this.zd = zSpeed;

		int b = (color) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int r = (color >> 16) & 0xFF;
		this.setColor(r / 255f, g / 255f, b / 255f);
		this.setAlpha(0);

		lifetime = 25 + (int) (Math.random() * 50);
		quadSize = (float) (0.01 + Math.random() * 0.1);
		setSprite(spriteSet.get(1, 2));

	}

	@Override
	public ParticleRenderType getRenderType() {
		return type.renderType;
	}

	@Override
	public float getQuadSize(float scaleFactor) {
		double scale = (lifetime - age) * .015;
		return .5f * (float) Math.max(scale, 0);
	}

	@Override
	public void tick() {
		float a = ((color >> 24) & 0xFF) / 255f;
		float agef = (float) age / (float) lifetime;
		setAlpha(a * agef);
//		System.out.println(age);
//		double initialAlpha = (color << 24) & 0xFF;
//		double alpha = initialAlpha - (age - 100) * (age + 10) * .00011;
//		setAlpha((float) Math.max(0, Math.min(1, alpha)));
		super.tick();
	}

	//
	//

//	public void setColorByType(byte type) {
//
//		if (Math.abs(type) == 1) {
//			float f = (float) (.2 + Math.random() * .3);
//			setColor(//
//					f + .2f, //
//					f, //
//					(float) (.5 + Math.random() * .5)//
//			);
//			return;
//		}
//
//		if (type > 1) {
////			EnergyType energyType = MapData.DATA.getEnergyType(type);
////			setColor(energyType.getR(), energyType.getG(), energyType.getB());
//			setColor(1, 0, 0);
//			return;
//		}
//
//		setColor(1, 1, 1);
//
//	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements ParticleProvider<ParticleEnergyData> {
		private final SpriteSet spriteSet;

		public Factory(SpriteSet spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle createParticle(ParticleEnergyData data, ClientLevel world, double x, double y, double z, double vx, double vy, double vz) {
			return new ParticleEnergy(world, x, y, z, vx, vy, vz, spriteSet, data.color, data.colorType);
		}
	}

}
