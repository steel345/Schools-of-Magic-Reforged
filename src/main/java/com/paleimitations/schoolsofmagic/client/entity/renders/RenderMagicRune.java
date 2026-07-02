package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityMagicRune;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderMagicRune extends EntityRenderer<EntityMagicRune> {
   private static final ResourceLocation[] TEX = {
      new ResourceLocation("som", "textures/entity/magic_rune_0.png"),
      new ResourceLocation("som", "textures/entity/magic_rune_1.png"),
      new ResourceLocation("som", "textures/entity/magic_rune_2.png"),
      new ResourceLocation("som", "textures/entity/magic_rune_3.png")
   };

   public RenderMagicRune(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityMagicRune entity) {
      return TEX[Math.floorMod(entity.getGlyph(), TEX.length)];
   }

   @Override
   public void render(EntityMagicRune entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buffer, int packedLight) {
      int color = entity.getColor();
      float r = ((color >> 16) & 0xFF) / 255.0F;
      float g = ((color >> 8) & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;
      float spin = (entity.tickCount + partialTicks) * 1.2F % 360.0F;
      ResourceLocation tex = TEX[Math.floorMod(entity.getGlyph(), TEX.length)];

      pose.pushPose();
      switch (entity.getFace()) {
         case DOWN -> pose.mulPose(Axis.XP.rotationDegrees(180.0F));
         case NORTH -> pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
         case SOUTH -> pose.mulPose(Axis.XP.rotationDegrees(90.0F));
         case EAST -> pose.mulPose(Axis.ZP.rotationDegrees(-90.0F));
         case WEST -> pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
         default -> {}
      }
      pose.translate(0.0D, 0.02D, 0.0D);
      pose.mulPose(Axis.YP.rotationDegrees(spin));

      VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentCull(tex));
      Matrix4f m = pose.last().pose();
      float s = 0.5F;
      int light = LightTexture.FULL_BRIGHT;

      v(vc, m, -s, 0.0F, -s, 0.0F, 0.0F, r, g, b, light);
      v(vc, m, -s, 0.0F, s, 0.0F, 1.0F, r, g, b, light);
      v(vc, m, s, 0.0F, s, 1.0F, 1.0F, r, g, b, light);
      v(vc, m, s, 0.0F, -s, 1.0F, 0.0F, r, g, b, light);

      v(vc, m, s, 0.0F, -s, 1.0F, 0.0F, r, g, b, light);
      v(vc, m, s, 0.0F, s, 1.0F, 1.0F, r, g, b, light);
      v(vc, m, -s, 0.0F, s, 0.0F, 1.0F, r, g, b, light);
      v(vc, m, -s, 0.0F, -s, 0.0F, 0.0F, r, g, b, light);

      pose.popPose();
      super.render(entity, entityYaw, partialTicks, pose, buffer, packedLight);
   }

   private static void v(VertexConsumer vc, Matrix4f m, float x, float y, float z, float u, float vv,
                         float r, float g, float b, int light) {
      vc.vertex(m, x, y, z).color(r, g, b, 0.9F).uv(u, vv)
         .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
   }
}
