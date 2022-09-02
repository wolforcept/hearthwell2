package wolforce.hearthwell.client.render.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.entities.EntitySpire;
import wolforce.utils.client.UtilRender;

public class RendererSpire extends EntityRenderer<EntitySpire> implements EntityRendererProvider<EntitySpire> {

	private static ResourceLocation imgLoc = new ResourceLocation(HearthWell.MODID, "textures/blocks/tokens.png");

	private static List<Integer> nameIndexes = new ArrayList<>(10 * 12);

	static {
		for (int i = 0; i < 10; i++) {
			List<Integer> temp = new LinkedList<>();
			for (int j = 0; j < 12; j++) {
				temp.add(j);
			}
			Collections.shuffle(temp);
			nameIndexes.addAll(temp);
		}
	}

	public RendererSpire(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EntitySpire entity) {
		return null;
	}

	@Override
	public EntityRenderer<EntitySpire> create(Context context) {
		return new RendererSpire(context);
	}

	@Override
	public void render(EntitySpire ent, float x, float y, PoseStack pose, MultiBufferSource buffer, int combinedLight) {

		Random rand = new Random(ent.getOnPos().hashCode());

		int nameIndex = 0;

//		RenderSystem.enableBlend();
//		RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

		for (float[] d : new float[][] { //
				// count height radii size
				{ 12, -.464f, .55f, .4f }, //
//				{ 12, -.2f, .475f, .35f }, //
//				{ 10, .05f, .4f, .3f }, //
				{ 10, -.1f, .4f, .4f }, //
//				{ 10, .3f, .4f, .27f }, //
//				{ 10, .5f, .3f, .245f }, //
				{ 9, .4f, .3f, .3f }, //
//				{ 8, .7f, .22f, .22f }, //
				{ 6, .75f, .19f, .3f }, //
		}) {

			int n = (int) d[0];
			int dir = (rand.nextBoolean() ? -1 : 1);

			for (int i = 0; i < n; i++) {

				int name = nameIndexes.get(nameIndex);

				pose.pushPose();

				float r = d[3];

				int t = dir * (int) ((int) System.currentTimeMillis() * .14 * d[3] * d[3]);
				int time = t % 360;
				pose.mulPose(new Quaternion(Vector3f.YP, 360 * i / n + time, true));
				pose.translate(0, d[1], d[2]);
				pose.scale(r, r, r);

				int v = ent.hasName(name) ? 0 : 16;
				UtilRender.renderImage(pose, imgLoc, 192, 32, // texture wh
						-8, 0, 16, 16, // xywh
						name * 16, v, name * 16 + 16, v + 16, // uv1 uv2
						255);

				pose.popPose();

				nameIndex++;
			}
		}
//		RenderSystem.disableBlend();

	}

}
