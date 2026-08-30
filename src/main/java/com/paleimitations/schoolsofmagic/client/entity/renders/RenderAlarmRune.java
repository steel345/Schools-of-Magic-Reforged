package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.common.entity.EntityAlarmRune;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

// the ziggurat rune, laid flat where it was set. it turns slowly, and it burns brighter once it
// has been set off
public class RenderAlarmRune extends EntityRenderer<EntityAlarmRune> {
   private static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/blocks/sandstone_runes.png");
   private static final float SIDE = 1.6F;

   public RenderAlarmRune(EntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityAlarmRune entity) {
      return TEXTURE;
   }

   @Override
   public void render(EntityAlarmRune entity, float yaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int light) {
      float age = entity.tickCount + partialTicks;
      boolean loud = entity.isWailing();

      pose.pushPose();
      pose.translate(0.0D, 0.02D, 0.0D);
      pose.mulPose(Axis.XP.rotationDegrees(90.0F));
      pose.mulPose(Axis.ZP.rotationDegrees(age * (loud ? 3.0F : 0.6F)));

      float pulse = loud ? 0.75F + 0.25F * Mth.sin(age * 0.5F) : 0.55F + 0.15F * Mth.sin(age * 0.08F);
      float r = loud ? 1.0F : 0.45F;
      float g = loud ? 0.35F : 0.15F;
      float b = loud ? 0.28F : 0.70F;

      Matrix4f matrix = pose.last().pose();
      VertexConsumer out = buf.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
      quad(out, matrix, r * pulse, g * pulse, b * pulse, light);

      pose.popPose();
      super.render(entity, yaw, partialTicks, pose, buf, light);
   }

   private static void quad(VertexConsumer out, Matrix4f matrix, float r, float g, float b, int light) {
      float half = SIDE * 0.5F;
      float[][] corners = {{-half, -half, 0.0F, 0.0F}, {-half, half, 0.0F, 1.0F},
                           {half, half, 1.0F, 1.0F}, {half, -half, 1.0F, 0.0F}};
      for (float[] c : corners) {
         out.vertex(matrix, c[0], c[1], 0.0F).color(r, g, b, 0.9F).uv(c[2], c[3])
            .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
      }
   }
}
