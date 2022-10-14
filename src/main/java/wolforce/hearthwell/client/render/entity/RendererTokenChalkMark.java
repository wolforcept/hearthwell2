package wolforce.hearthwell.client.render.entity;

import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import wolforce.hearthwell.entities.EntityTokenChalkMark;

public class RendererTokenChalkMark extends EntityRenderer<EntityTokenChalkMark> implements EntityRendererProvider<EntityTokenChalkMark> {

	private final Minecraft mc = Minecraft.getInstance();
	private final Font font = mc.font;

	public RendererTokenChalkMark(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityTokenChalkMark entity) {
		return null;
	}

	@Override
	public EntityRenderer<EntityTokenChalkMark> create(Context context) {
		return new RendererTokenChalkMark(context);
	}

	@Override
	public void render(EntityTokenChalkMark ent, float yaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight) {

		Random random = new Random(ent.getOnPos().hashCode());
//		float randomRotation = (float) (random.nextFloat() * 2 * Math.PI);
		float randomRotation = (float) (-.5 + random.nextFloat());

		String string = ent.getText();
		Direction dir = ent.getTextDirection();

		pose.pushPose();

		Quaternion rotation = switch (dir) {
		case UP -> Quaternion.fromYXZ(randomRotation, (float) Math.PI / 2, 0);
		case DOWN -> Quaternion.fromYXZ(0, -(float) Math.PI / 2, 0);

		case NORTH -> Quaternion.fromYXZ(0f, 0f, randomRotation);
		case SOUTH -> Quaternion.fromXYZ(0, (float) Math.PI, randomRotation);
		case EAST -> Quaternion.fromYXZ(-(float) Math.PI / 2f, 0f, randomRotation);
		case WEST -> Quaternion.fromYXZ((float) Math.PI / 2f, 0f, randomRotation);
		};

		Vec3 pos = switch (dir) {
		case UP -> new Vec3(.1, .22, .1);
		case DOWN -> new Vec3(.1, .2, -.1);

		case NORTH -> new Vec3(.1, .3, 0);
		case SOUTH -> new Vec3(-.1, .3, 0);
		case EAST -> new Vec3(0, .3, .1);
		case WEST -> new Vec3(0, .3, -.1);
		};

		pose.translate(pos.x, pos.y, pos.z);
		pose.mulPose(rotation);

		pose.scale(-0.025F, -0.025F, 0.025F);
		font.drawInBatch(string, 0, 0, 0xFFFFFF, false, pose.last().pose(), buffer, false, 1, 15728880);

		pose.popPose();
	}

}
