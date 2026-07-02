package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelPhoenix;
import com.paleimitations.schoolsofmagic.client.entity.renders.RenderPhoenix;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

public class LayerPhoenixCold extends RenderLayer<EntityPhoenix, ModelPhoenix<EntityPhoenix>> {

   public LayerPhoenixCold(RenderLayerParent<EntityPhoenix, ModelPhoenix<EntityPhoenix>> parent) {
      super(parent);
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, EntityPhoenix entity,
                      float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                      float netHeadYaw, float headPitch) {
      float cold = entity.getCold();
      if (cold <= 0.02F || entity.isInvisible()) return;
      float alpha = Mth.clamp(cold * 0.9F, 0.0F, 0.85F);
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(RenderPhoenix.TEXTURES));
      poseStack.pushPose();
      poseStack.scale(1.06F, 1.06F, 1.06F);
      this.getParentModel().renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY,
         0.35F, 0.55F, 1.0F, alpha);
      poseStack.popPose();
   }
}
