//package wolforce.hearthwell.client.render.item;
//
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Vector3f;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
//import net.minecraft.client.renderer.ItemBlockRenderTypes;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
//import net.minecraft.client.renderer.entity.ItemRenderer;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import wolforce.hearthwell.HearthWell;
//import wolforce.hearthwell.items.ItemMystContainer;
//
//public class BewlrMystContainer extends BlockEntityWithoutLevelRenderer {
//
//	private static final ResourceLocation MODEL_LOC = new ResourceLocation(HearthWell.MODID, "item/myst_container_model");
//	private static final MultiBufferSource.BufferSource GHOST_ENTITY_BUF = MultiBufferSource.immediate(new BufferBuilder(256));
//
//	private static BewlrMystContainer instance;
//	private static ItemRenderer irenderer;
//
//	public static BewlrMystContainer get() {
//		if (instance == null)
//			instance = new BewlrMystContainer();
//		return instance;
//	}
//
//	private BewlrMystContainer() {
//		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
//		irenderer = Minecraft.getInstance().getItemRenderer();
//	}
//
//	@Override
//	public void renderByItem(ItemStack stack, TransformType type, PoseStack matrix, MultiBufferSource buffer, int light, int overlay) {
//
////		ResourceLocation model_loc = new ResourceLocation(HearthWell.MODID, "item/myst_ingot");
////		BakedModel base = irenderer.getItemModelShaper().getModelManager().getModel(model_loc);
//		BakedModel base = irenderer.getItemModelShaper().getModelManager().getModel(MODEL_LOC);
//
//		matrix.pushPose();
//		if (type == TransformType.FIXED) {
//			matrix.translate(1, 1, 0);
//			float scale = 0.5F;
//			matrix.scale(scale, scale, scale);
//			matrix.translate(-1.5F, -0.5F, 0.5F);
//			matrix.mulPose(Vector3f.XP.rotationDegrees(90));
//			matrix.mulPose(Vector3f.XP.rotationDegrees(90));
//			matrix.translate(0, 0, -1);
//		} else if (type != TransformType.GUI) {
//			matrix.translate(1, 1, 0);
//			float scale = 0.5F;
//			matrix.scale(scale, scale, scale);
//			matrix.translate(-1.5F, -0.5F, 0.5F);
//			matrix.mulPose(Vector3f.XP.rotationDegrees(90));
//		} else {
//			matrix.translate(0, -.5F, -.5F);
//			matrix.mulPose(Vector3f.XN.rotationDegrees(60));
//			matrix.mulPose(Vector3f.ZP.rotationDegrees(45));
//			float scale = 0.9F;
//			matrix.scale(scale, scale, scale);
//			matrix.translate(0.775, 0, -0.0825);
//		}
//		irenderer.renderModelLists(base, stack, light, overlay, matrix, ItemRenderer.getFoilBufferDirect(GHOST_ENTITY_BUF, ItemBlockRenderTypes.getRenderType(stack, true), true, false));
//		GHOST_ENTITY_BUF.endBatch();
//		matrix.popPose();
//
//		if (stack.getItem() instanceof ItemMystContainer item) {
//
//			int fuel = item.getFuel(stack);
//			int fuelType = item.getFuelType(stack);
//			// render fuel
//		}
//	}
//}
