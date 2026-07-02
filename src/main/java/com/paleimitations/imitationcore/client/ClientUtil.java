package com.paleimitations.imitationcore.client;

import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;

import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public final class ClientUtil {

   private static final VertexFormat VERTEX_FORMAT = DefaultVertexFormat.NEW_ENTITY;

   public ClientUtil() {
   }

   @OnlyIn(Dist.CLIENT)
   public static void renderImitationElectricity(PoseStack pose, int number, int NumberOfBranches, double scale) {
      pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
      pose.pushPose();
      pose.translate(0.0, 8.0 * scale, 0.0);
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(180.0F));
      pose.pushPose();

      RenderSystem.depthMask(true);
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(
              com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
              com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
      RenderSystem.setShader(GameRenderer::getPositionColorShader);

      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuilder();

      double scale16 = scale / 16.0;
      int NumberOfPossibleSubBranches = 3;
      double[] translateXArray = new double[8];
      double[] translateZArray = new double[8];
      double tempX = 0.0;
      double tempZ = 0.0;
      Random random = new Random((long) number);

      for (int counter_ = 7; counter_ >= 0; counter_--) {
         translateXArray[counter_] = tempX;
         translateZArray[counter_] = tempZ;
         tempX += (double) random.nextInt(10) * 0.1;
         tempZ += (double) random.nextInt(10) * 0.1;
         tempX *= (double) (-3 * -counter_) * 0.11;
         tempZ *= (double) (-3 * -counter_) * 0.11;
         if (new Random((long) (counter_ - 1)).nextBoolean()) {
            tempX *= -1.0;
            tempZ *= -1.0;
         }
      }

      for (int shells = 0; shells < 4; shells++) {
         Random random1 = new Random((long) number);

         for (int branches = 0; branches < NumberOfBranches; branches++) {
            for (int possibleSubBranches = 0; possibleSubBranches < 4; possibleSubBranches++) {
               int position = 7;
               int decendingHeight = 0;
               if (possibleSubBranches > 0) {
                  position = Math.abs((7 - possibleSubBranches) % translateXArray.length);
               }
               if (possibleSubBranches > 0) {
                  decendingHeight = position - 2;
               }
               double topTranslateX = translateXArray[position];
               double topTranslateZ = translateZArray[position];

               for (int yPos = position; yPos >= decendingHeight; yPos--) {
                  double bottomTranslateX = topTranslateX;
                  double bottomTranslateZ = topTranslateZ;
                  topTranslateX += (double) random1.nextInt(10) * 0.1;
                  topTranslateZ += (double) random1.nextInt(10) * 0.1;
                  topTranslateX *= (double) (-3 * yPos) * 0.11;
                  topTranslateZ *= (double) (-3 * yPos) * 0.11;
                  if (new Random((long) (possibleSubBranches - 1)).nextBoolean()) {
                     topTranslateX *= -1.0;
                     topTranslateZ *= -1.0;
                  }
                  if (possibleSubBranches != 0) {
                     topTranslateX += (double) (random1.nextInt(9) - 3);
                     topTranslateZ += (double) (random1.nextInt(9) - 3);
                  }

                  bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
                  double topWidth = 0.1 + (double) shells * 0.2;
                  if (yPos == 0) topWidth *= (double) yPos * 0.1 + 1.0;
                  double bottomWidth = 0.1 + (double) shells * 0.2;
                  if (yPos == 0) bottomWidth *= (double) (yPos - 1) * 0.1 + 1.0;
                  topWidth *= scale / 16.0;
                  bottomWidth *= scale / 16.0;

                  for (int side = 0; side < 5; side++) {
                     double topOffsetX = -topWidth;
                     double topOffsetZ = -topWidth;
                     if (side == 1 || side == 2) topOffsetX += topWidth * 2.0;
                     if (side == 2 || side == 3) topOffsetZ += topWidth * 2.0;
                     double bottomOffsetX = -bottomWidth;
                     double bottomOffsetZ = -bottomWidth;
                     if (side == 1 || side == 2) bottomOffsetX += bottomWidth * 2.0;
                     if (side == 2 || side == 3) bottomOffsetZ += bottomWidth * 2.0;

                     org.joml.Matrix4f mat = pose.last().pose();
                     bufferbuilder.vertex(mat,
                                     (float) (bottomOffsetX + topTranslateX * scale16),
                                     (float) ((double) yPos * scale),
                                     (float) (bottomOffsetZ + topTranslateZ * scale16))
                             .color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                     bufferbuilder.vertex(mat,
                                     (float) (topOffsetX + bottomTranslateX * scale16),
                                     (float) ((double) (yPos + 1) * scale),
                                     (float) (topOffsetZ + bottomTranslateZ * scale16))
                             .color(0.45F, 0.45F, 0.5F, 0.3F).endVertex();
                  }
                  tessellator.end();
               }
            }
         }
      }

      RenderSystem.disableBlend();
      pose.popPose();
      pose.popPose();
   }

   @OnlyIn(Dist.CLIENT)
   public static void drawCuboid(PoseStack pose, float minU, float maxU, float minV, float maxV,
                                 double x_size, double y_size, double z_size, double scale) {
      drawCuboidAt(pose, 0.0, 0.0, 0.0, minU, maxU, minV, maxV, x_size, y_size, z_size, scale);
   }

   public static void drawCuboidAt(PoseStack pose, double x, double y, double z,
                                   float minU, float maxU, float minV, float maxV,
                                   double x_size, double y_size, double z_size, double scale) {
      pose.pushPose();
      pose.scale((float) scale, (float) scale, (float) scale);
      RenderSystem.setShader(GameRenderer::getPositionTexShader);

      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuilder();
      bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      org.joml.Matrix4f m = pose.last().pose();

      bufferbuilder.vertex(m, (float)(-x_size + x), (float)( y_size + y), (float)(-z_size + z)).uv(maxU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)( y_size + y), (float)( z_size + z)).uv(maxU, minV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)( y_size + y), (float)( z_size + z)).uv(minU, minV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)( y_size + y), (float)(-z_size + z)).uv(minU, maxV).endVertex();

      bufferbuilder.vertex(m, (float)(-x_size + x), (float)(-y_size + y), (float)( z_size + z)).uv(minU, minV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)(-y_size + y), (float)(-z_size + z)).uv(minU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)(-y_size + y), (float)(-z_size + z)).uv(maxU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)(-y_size + y), (float)( z_size + z)).uv(maxU, minV).endVertex();

      bufferbuilder.vertex(m, (float)( x_size + x), (float)(-y_size + y), (float)( z_size + z)).uv(maxU, minV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)(-y_size + y), (float)(-z_size + z)).uv(maxU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)( y_size + y), (float)(-z_size + z)).uv(minU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)( y_size + y), (float)( z_size + z)).uv(minU, minV).endVertex();

      bufferbuilder.vertex(m, (float)(-x_size + x), (float)(-y_size + y), (float)(-z_size + z)).uv(minU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)(-y_size + y), (float)( z_size + z)).uv(minU, minV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)( y_size + y), (float)( z_size + z)).uv(maxU, minV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)( y_size + y), (float)(-z_size + z)).uv(maxU, maxV).endVertex();

      bufferbuilder.vertex(m, (float)(-x_size + x), (float)(-y_size + y), (float)(-z_size + z)).uv(minU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)( y_size + y), (float)(-z_size + z)).uv(minU, minV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)( y_size + y), (float)(-z_size + z)).uv(maxU, minV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)(-y_size + y), (float)(-z_size + z)).uv(maxU, maxV).endVertex();

      bufferbuilder.vertex(m, (float)( x_size + x), (float)(-y_size + y), (float)( z_size + z)).uv(maxU, minV).endVertex();
      bufferbuilder.vertex(m, (float)( x_size + x), (float)( y_size + y), (float)( z_size + z)).uv(maxU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)( y_size + y), (float)( z_size + z)).uv(minU, maxV).endVertex();
      bufferbuilder.vertex(m, (float)(-x_size + x), (float)(-y_size + y), (float)( z_size + z)).uv(minU, minV).endVertex();

      tessellator.end();
      pose.popPose();
   }

   public static void drawSpriteWithScale(PoseStack pose, double x, double y, double z,
                                          double rotationX, double rotationY, double rotationZ,
                                          double u, double v, double width, double height,
                                          float scale, float scaleX, float scaleY, float partialTicks,
                                          float red, float green, float blue, float alpha,
                                          int light, boolean facesPlayer) {
      Tesselator tes = Tesselator.getInstance();
      BufferBuilder buffer = tes.getBuilder();

      Minecraft mc = Minecraft.getInstance();
      Entity entity = mc.getCameraEntity();
      if (entity == null) entity = mc.player;

      Camera cam = mc.gameRenderer.getMainCamera();
      Vec3 camPos = cam.getPosition();
      double viewerX = camPos.x;
      double viewerY = camPos.y;
      double viewerZ = camPos.z;

      double dX = viewerX - x;
      double dY = viewerY - y;
      double dZ = viewerZ - z;

      int j = light >> 16 & 0xFFFF;
      int k = light & 0xFFFF;
      Vector3f vec1 = new Vector3f(-scale * scaleX, -scale * scaleY, 0.0F);
      Vector3f vec2 = new Vector3f(-scale * scaleX,  scale * scaleY, 0.0F);
      Vector3f vec3 = new Vector3f( scale * scaleX,  scale * scaleY, 0.0F);
      Vector3f vec4 = new Vector3f( scale * scaleX, -scale * scaleY, 0.0F);

      pose.pushPose();
      pose.translate(x - viewerX, y - viewerY, z - viewerZ);
      if (facesPlayer) {
         double yaw = (Math.PI / 2) - Math.atan2(dZ, dX);
         double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + (Math.PI / 2);
         pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees((float) Math.toDegrees(yaw)));
         pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees((float) Math.toDegrees(pitch)));
      }
      pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees((float) Math.toDegrees(rotationZ)));
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees((float) Math.toDegrees(rotationY)));
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees((float) Math.toDegrees(rotationX)));

      RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
      buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      org.joml.Matrix4f m = pose.last().pose();

      buffer.vertex(m, vec1.x(), vec1.y(), vec1.z())
              .uv((float)(u + width), (float)(v + height)).color(red, green, blue, alpha).endVertex();
      buffer.vertex(m, vec2.x(), vec2.y(), vec2.z())
              .uv((float)(u + width), (float) v).color(red, green, blue, alpha).endVertex();
      buffer.vertex(m, vec3.x(), vec3.y(), vec3.z())
              .uv((float) u, (float) v).color(red, green, blue, alpha).endVertex();
      buffer.vertex(m, vec4.x(), vec4.y(), vec4.z())
              .uv((float) u, (float)(v + height)).color(red, green, blue, alpha).endVertex();

      tes.end();
      pose.popPose();
   }

   public static void drawSprite(PoseStack pose, double x, double y, double z,
                                 double rotationX, double rotationY, double rotationZ,
                                 double u, double v, double width, double height,
                                 float scale, float partialTicks,
                                 float red, float green, float blue, float alpha,
                                 int light, boolean facesPlayer) {
      drawSpriteWithScale(pose, x, y, z, rotationX, rotationY, rotationZ, u, v, width, height,
              scale, 1.0F, 1.0F, partialTicks, red, green, blue, alpha, light, facesPlayer);
   }

   public static void drawGuiSprite(PoseStack pose, double x, double y, double z,
                                    float rotationX, float rotationY, float rotationZ,
                                    double u, double v, double width, double height,
                                    float scale, float partialTicks,
                                    float red, float green, float blue, float alpha) {
      Tesselator tes = Tesselator.getInstance();
      BufferBuilder buffer = tes.getBuilder();
      Vector3f vec1 = new Vector3f(-scale, -scale, 0.0F);
      Vector3f vec2 = new Vector3f(-scale,  scale, 0.0F);
      Vector3f vec3 = new Vector3f( scale,  scale, 0.0F);
      Vector3f vec4 = new Vector3f( scale, -scale, 0.0F);

      pose.pushPose();
      pose.translate(x, y, z);
      pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(rotationX));
      pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rotationY));
      pose.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(rotationZ));

      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);
      RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
      buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      org.joml.Matrix4f m = pose.last().pose();

      buffer.vertex(m, vec1.x(), vec1.y(), vec1.z()).uv((float)(u + width), (float)(v + height)).color(red, green, blue, alpha).endVertex();
      buffer.vertex(m, vec2.x(), vec2.y(), vec2.z()).uv((float)(u + width), (float) v          ).color(red, green, blue, alpha).endVertex();
      buffer.vertex(m, vec3.x(), vec3.y(), vec3.z()).uv((float) u,          (float) v          ).color(red, green, blue, alpha).endVertex();
      buffer.vertex(m, vec4.x(), vec4.y(), vec4.z()).uv((float) u,          (float)(v + height)).color(red, green, blue, alpha).endVertex();

      tes.end();
      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      pose.popPose();
   }

   public static double getYaw(Vec3 source, Vec3 destination) {
      double yaw = Math.atan2(destination.x - source.x, destination.z - source.z);
      yaw *= 180.0 / Math.PI;
      yaw = yaw < 0.0 ? 360.0 - -yaw : yaw;
      return yaw + 90.0;
   }

   @OnlyIn(Dist.CLIENT)
   public static BakedModel getModelFromStack(ItemStack stack, Level world) {
      BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, null, 0);

      return model;
   }

   @OnlyIn(Dist.CLIENT)
   public static BakedModel getModelFromStack(PoseStack pose, ItemStack stack, Level world) {
      BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, null, 0);
      return ForgeHooksClient.handleCameraTransforms(pose, model, ItemDisplayContext.NONE, false);
   }

   @OnlyIn(Dist.CLIENT)
   public static void drawNonStandardTexturedRect(PoseStack pose, int x, int y, int u, int v,
                                                  int width, int height, int textureWidth, int textureHeight) {
      double f = 1.0 / (double) textureWidth;
      double f1 = 1.0 / (double) textureHeight;
      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuilder();
      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      org.joml.Matrix4f m = pose.last().pose();
      bufferbuilder.vertex(m, (float) x,           (float) (y + height), 0.0F).uv((float)((double) u * f),           (float)((double) (v + height) * f1)).endVertex();
      bufferbuilder.vertex(m, (float) (x + width), (float) (y + height), 0.0F).uv((float)((double) (u + width) * f), (float)((double) (v + height) * f1)).endVertex();
      bufferbuilder.vertex(m, (float) (x + width), (float) y,            0.0F).uv((float)((double) (u + width) * f), (float)((double) v * f1)).endVertex();
      bufferbuilder.vertex(m, (float) x,           (float) y,            0.0F).uv((float)((double) u * f),           (float)((double) v * f1)).endVertex();
      tessellator.end();
   }
}
