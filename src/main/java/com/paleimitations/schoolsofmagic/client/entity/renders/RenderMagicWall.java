package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicWall;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderMagicWall extends EntityRenderer<EntityMagicWall> {
   private static final ResourceLocation TEX = new ResourceLocation("minecraft", "textures/block/bricks.png");

   public RenderMagicWall(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMagicWall entity) {
      return TEX;
   }

   @Override
   public void render(EntityMagicWall entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
      int color = entity.getColor();
      float r = ((color >> 16) & 0xFF) / 255.0F;
      float g = ((color >> 8) & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;
      float a = 0.78F;

      pose.pushPose();
      pose.mulPose(Axis.YP.rotationDegrees(-entity.getYRot()));
      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(TEX));
      Matrix4f m = pose.last().pose();
      Matrix3f n = pose.last().normal();
      float hw = 3.0F, h = 6.0F, tile = 6.0F;

      quad(vc, m, n, -hw, 0.0F, hw, h, 0.0F, tile, tile, 0.0F, 1.0F, r, g, b, a, packedLight);
      quad(vc, m, n, hw, 0.0F, -hw, h, tile, tile, 0.0F, 0.0F, -1.0F, r, g, b, a, packedLight);

      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
   }

   private static void quad(VertexConsumer vc, Matrix4f m, Matrix3f n,
                            float x0, float y0, float x1, float y1,
                            float u0, float v0, float u1, float v1, float nz,
                            float r, float g, float b, float a, int light) {
      vertex(vc, m, n, x0, y0, u0, v1, nz, r, g, b, a, light);
      vertex(vc, m, n, x1, y0, u1, v1, nz, r, g, b, a, light);
      vertex(vc, m, n, x1, y1, u1, v0, nz, r, g, b, a, light);
      vertex(vc, m, n, x0, y1, u0, v0, nz, r, g, b, a, light);
   }

   private static void vertex(VertexConsumer vc, Matrix4f m, Matrix3f n,
                              float x, float y, float u, float v, float nz,
                              float r, float g, float b, float a, int light) {
      vc.vertex(m, x, y, 0.0F).color(r, g, b, a).uv(u, v)
         .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(n, 0.0F, 0.0F, nz).endVertex();
   }
}
