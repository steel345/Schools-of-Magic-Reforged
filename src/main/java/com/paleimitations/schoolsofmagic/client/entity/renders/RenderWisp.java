package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelWisp;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityWisp;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderWisp extends EntityRenderer<EntityWisp> {
   public static final ResourceLocation TEXTURES = new ResourceLocation("som", "textures/entity/wisp.png");
   private final ModelWisp<EntityWisp> model;

   public RenderWisp(EntityRendererProvider.Context context) {
      super(context);
      this.model = new ModelWisp<>(context.bakeLayer(ModelWisp.LAYER_LOCATION));
   }

   @Override
   public ResourceLocation getTextureLocation(EntityWisp entity) { return TEXTURES; }

   @Override
   public void render(EntityWisp entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      pose.pushPose();
      float f2 = (float) entity.tickCount + partialTicks;
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
      this.model.setupAnim(entity, 0.0F, 0.0F, f2, 0.0F, 0.0F);
      var vc = buf.getBuffer(RenderType.entityTranslucent(TEXTURES));
      this.model.renderToBuffer(pose, vc, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.5F);
      pose.popPose();
   }
}
