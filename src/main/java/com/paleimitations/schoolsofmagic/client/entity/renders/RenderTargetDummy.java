package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.ClientDummyDamage;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelDummyBase;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelTargetDummy;
import com.paleimitations.schoolsofmagic.common.entity.EntityTargetDummy;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderTargetDummy extends MobRenderer<EntityTargetDummy, ModelTargetDummy> {

   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/target_dummy.png");
   private static final ResourceLocation BASE_TEXTURE = new ResourceLocation("minecraft", "textures/entity/armorstand/wood.png");

   private final ModelDummyBase baseModel;

   public RenderTargetDummy(EntityRendererProvider.Context context) {
      super(context, new ModelTargetDummy(context.bakeLayer(ModelTargetDummy.LAYER_LOCATION)), 0.4F);
      this.baseModel = new ModelDummyBase(context.bakeLayer(ModelDummyBase.LAYER_LOCATION));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityTargetDummy entity) {
      return TEXTURE;
   }

   @Override
   public void render(EntityTargetDummy entity, float entityYaw, float partialTicks, PoseStack poseStack,
                      MultiBufferSource buffer, int packedLight) {
      super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

      poseStack.pushPose();
      poseStack.scale(-1.0F, -1.0F, 1.0F);
      poseStack.translate(0.0D, -1.501D, 0.0D);
      VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(BASE_TEXTURE));
      this.baseModel.render(poseStack, vc, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
      poseStack.popPose();

      renderPopups(entity, poseStack, buffer, packedLight);
   }

   private void renderPopups(EntityTargetDummy entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
      java.util.List<ClientDummyDamage.Popup> popups = ClientDummyDamage.get(entity.getId());
      if (popups.isEmpty()) {
         return;
      }
      Font font = this.getFont();
      int fullBright = 15728880;
      for (ClientDummyDamage.Popup p : popups) {
         float elapsed = p.elapsedSeconds();
         float rise = elapsed * 0.25F;

         String dmgText = fmt(p.damage);
         String dpsText = fmt(p.dps) + " DPS";
         float dmgX = -font.width(dmgText) / 2.0F;
         float dpsX = -font.width(dpsText) / 2.0F;

         poseStack.pushPose();
         poseStack.translate(p.offX, p.offY + rise, 0.0D);
         poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
         poseStack.scale(-0.025F, -0.025F, 0.025F);

         Matrix4f shadowMat = poseStack.last().pose();
         font.drawInBatch(Component.literal(dmgText), dmgX + 1.0F, 1.0F,
            shadowOf(0xFFFFFFFF), false, shadowMat, buffer, Font.DisplayMode.NORMAL, 0, fullBright);
         font.drawInBatch(Component.literal(dpsText), dpsX + 1.0F, 11.0F,
            shadowOf(0xFFFF2020), false, shadowMat, buffer, Font.DisplayMode.NORMAL, 0, fullBright);

         poseStack.translate(0.0F, 0.0F, -0.05F);
         Matrix4f mainMat = poseStack.last().pose();
         font.drawInBatch(Component.literal(dmgText), dmgX, 0.0F,
            0xFFFFFFFF, false, mainMat, buffer, Font.DisplayMode.NORMAL, 0, fullBright);
         font.drawInBatch(Component.literal(dpsText), dpsX, 10.0F,
            0xFFFF2020, false, mainMat, buffer, Font.DisplayMode.NORMAL, 0, fullBright);
         poseStack.popPose();
      }
   }

   private static int shadowOf(int color) {
      return (color & 0xFF000000) | ((color & 0xFCFCFC) >> 2);
   }

   private static String fmt(float v) {
      return v >= 10.0F ? String.valueOf(Math.round(v)) : String.format(java.util.Locale.ROOT, "%.1f", v);
   }
}
