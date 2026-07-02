package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFocusBall;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderFocusBall extends EntityRenderer<EntityFocusBall> {
   private static final ResourceLocation TEX = new ResourceLocation("som", "textures/entity/magic_orb.png");

   public RenderFocusBall(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityFocusBall entity) {
      return TEX;
   }

   @Override
   public void render(EntityFocusBall entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
      int color = entity.getColor();
      float r = ((color >> 16) & 0xFF) / 255.0F;
      float g = ((color >> 8) & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;
      float pulse = 1.0F + (float) Math.sin((entity.tickCount + partialTicks) * 0.2D) * 0.1F;

      pose.pushPose();
      pose.mulPose(this.entityRenderDispatcher.cameraOrientation());
      float s = 1.0F * pulse;
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(TEX));
      Matrix4f m = pose.last().pose();
      int light = LightTexture.FULL_BRIGHT;

      v(vc, m, -s, -s, 0.0F, 1.0F, r, g, b, light);
      v(vc, m, s, -s, 1.0F, 1.0F, r, g, b, light);
      v(vc, m, s, s, 1.0F, 0.0F, r, g, b, light);
      v(vc, m, -s, s, 0.0F, 0.0F, r, g, b, light);

      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
   }

   private static void v(VertexConsumer vc, Matrix4f m, float x, float y, float u, float vv,
                         float r, float g, float b, int light) {
      vc.vertex(m, x, y, 0.0F).color(r, g, b, 0.85F).uv(u, vv)
         .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
   }
}
