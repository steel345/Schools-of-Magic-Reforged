package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector4f;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT,
   bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MirrorPortalRenderer {
   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();

   private static final ResourceLocation OVERLAY =
      new ResourceLocation("som", "textures/items/gold_mirror_overlay.png");

   private static RenderTarget target;
   private static boolean portalPass;
   private static boolean broken;
   private static net.minecraft.client.renderer.LevelRenderer portalLevel;
   private static net.minecraft.client.renderer.RenderBuffers portalBuffers;

   private static boolean[][] mask;
   private static int maskCols;
   private static int maskRows;

   public static boolean isPortalPass() {
      return portalPass;
   }

   public static void dispose() {
      if (target != null) {
         target.destroyBuffers();
         target = null;
      }
      if (portalLevel != null) {
         portalLevel.close();
         portalLevel = null;
      }
      portalBuffers = null;
      broken = false;
   }

   private static boolean loadMask() {
      if (mask != null) {
         return true;
      }
      try (InputStream in = Minecraft.getInstance().getResourceManager()
            .getResourceOrThrow(OVERLAY).open()) {
         NativeImage image = NativeImage.read(in);
         maskCols = image.getWidth();
         maskRows = image.getHeight();
         mask = new boolean[maskRows][maskCols];
         for (int row = 0; row < maskRows; row++) {
            for (int col = 0; col < maskCols; col++) {
               mask[row][col] = (image.getPixelRGBA(col, row) >>> 24) > 16;
            }
         }
         image.close();
         return true;
      } catch (Exception e) {
         LOGGER.error("Could not read the magic mirror overlay mask", e);
         mask = null;
         broken = true;
         return false;
      }
   }

   @SubscribeEvent
   public static void onRenderTick(TickEvent.RenderTickEvent event) {
      if (event.phase != TickEvent.Phase.START || broken) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || mc.player == null || !MirrorScryRenderHandler.isScrying()) {
         return;
      }
      Entity marker = MirrorScryRenderHandler.getCamera();
      if (marker == null) {
         return;
      }
      RenderTarget main = mc.getMainRenderTarget();
      if (target == null) {
         target = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
      } else if (target.width != main.width || target.height != main.height) {
         target.resize(main.width, main.height, Minecraft.ON_OSX);
      }
      net.minecraft.core.BlockPos dest = marker.blockPosition();
      int destX = dest.getX() >> 4;
      int destZ = dest.getZ() >> 4;
      int fromX = mc.player.blockPosition().getX() >> 4;
      int fromZ = mc.player.blockPosition().getZ() >> 4;
      int reach = mc.options.getEffectiveRenderDistance();
      if (Math.abs(destX - fromX) > reach || Math.abs(destZ - fromZ) > reach
            || !mc.level.getChunkSource().hasChunk(destX, destZ)) {
         target.setClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         target.clear(Minecraft.ON_OSX);
         main.bindWrite(true);
         return;
      }
      Entity restore = mc.getCameraEntity();
      net.minecraft.client.OptionInstance<net.minecraft.client.GraphicsStatus> graphics =
         mc.options.graphicsMode();
      net.minecraft.client.GraphicsStatus previousGraphics = graphics.get();
      boolean loweredGraphics = previousGraphics == net.minecraft.client.GraphicsStatus.FABULOUS;
      net.minecraft.client.renderer.LevelRenderer home = mc.levelRenderer;
      portalPass = true;
      try {
         if (portalLevel == null) {
            portalBuffers = new net.minecraft.client.renderer.RenderBuffers();
            portalLevel = new net.minecraft.client.renderer.LevelRenderer(mc,
               mc.getEntityRenderDispatcher(), mc.getBlockEntityRenderDispatcher(),
               portalBuffers);
            portalLevel.onResourceManagerReload(mc.getResourceManager());
            portalLevel.setLevel(mc.level);
         }
         mc.levelRenderer = portalLevel;
         if (loweredGraphics) {
            graphics.set(net.minecraft.client.GraphicsStatus.FANCY);
         }
         target.setClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         target.clear(Minecraft.ON_OSX);
         target.bindWrite(true);
         mc.setCameraEntity(marker);
         mc.gameRenderer.renderLevel(event.renderTickTime,
            System.nanoTime() + 2000000L, new PoseStack());
      } catch (Throwable t) {
         broken = true;
         LOGGER.error("Magic mirror scrying view disabled after a render failure", t);
      } finally {
         portalPass = false;
         mc.levelRenderer = home;
         if (loweredGraphics) {
            graphics.set(previousGraphics);
         }
         mc.setCameraEntity(restore == null ? mc.player : restore);
         if (target != null) {
            target.unbindWrite();
         }
         main.bindWrite(true);
      }
   }

   public static boolean renderScryingMirror(
         net.minecraftforge.client.event.RenderHandEvent event, Player player, ItemStack stack) {
      if (target == null || broken || !loadMask()) {
         return false;
      }
      Minecraft mc = Minecraft.getInstance();
      PoseStack pose = event.getPoseStack();
      boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;
      ItemDisplayContext context = leftHanded
         ? ItemDisplayContext.FIRST_PERSON_LEFT_HAND
         : ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
      BakedModel model = mc.getItemRenderer().getModel(stack, player.level(), player, 0);

      pose.pushPose();
      pose.translate((leftHanded ? -1.0F : 1.0F) * 0.56F,
         -0.52F + event.getEquipProgress() * -0.6F, -0.72F);
      model.applyTransform(context, pose, leftHanded);
      pose.translate(-0.5F, -0.5F, -0.5F);

      net.minecraft.client.renderer.RenderType itemType =
         net.minecraft.client.renderer.RenderType.entityCutout(
            net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS);
      com.mojang.blaze3d.vertex.VertexConsumer itemBuffer =
         net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect(
            event.getMultiBufferSource(), itemType, true, stack.hasFoil());
      mc.getItemRenderer().renderModelLists(model, stack, event.getPackedLight(),
         net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, pose, itemBuffer);

      float[] bounds = bounds(model);
      Matrix4f model2view = pose.last().pose();
      Matrix4f mvp = new Matrix4f(RenderSystem.getProjectionMatrix())
         .mul(RenderSystem.getModelViewMatrix())
         .mul(model2view);
      float z = nearFace(model2view, bounds[4], bounds[5]);

      RenderSystem.setShader(GameRenderer::getPositionTexShader);
      RenderSystem.setShaderTexture(0, target.getColorTextureId());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableBlend();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.depthMask(false);

      BufferBuilder buffer = Tesselator.getInstance().getBuilder();
      buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
      for (int row = 0; row < maskRows; row++) {
         int col = 0;
         while (col < maskCols) {
            if (!mask[row][col]) {
               col++;
               continue;
            }
            int run = col;
            while (run < maskCols && mask[row][run]) {
               run++;
            }
            float x0 = bounds[0] + (bounds[1] - bounds[0]) * ((float) col / maskCols);
            float x1 = bounds[0] + (bounds[1] - bounds[0]) * ((float) run / maskCols);
            float y0 = bounds[3] - (bounds[3] - bounds[2]) * ((float) (row + 1) / maskRows);
            float y1 = bounds[3] - (bounds[3] - bounds[2]) * ((float) row / maskRows);
            vertex(buffer, model2view, mvp, x0, y0, z);
            vertex(buffer, model2view, mvp, x1, y0, z);
            vertex(buffer, model2view, mvp, x1, y1, z);
            vertex(buffer, model2view, mvp, x0, y1, z);
            col = run;
         }
      }
      Tesselator.getInstance().end();

      RenderSystem.depthMask(true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      pose.popPose();
      return true;
   }

   private static float[] bounds(BakedModel model) {
      float[] box = {Float.MAX_VALUE, -Float.MAX_VALUE, Float.MAX_VALUE,
         -Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE};
      net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create(42L);
      java.util.List<net.minecraft.client.renderer.block.model.BakedQuad> quads =
         new java.util.ArrayList<>(model.getQuads(null, null, random));
      for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
         quads.addAll(model.getQuads(null, dir, random));
      }
      int stride = com.mojang.blaze3d.vertex.DefaultVertexFormat.BLOCK.getIntegerSize();
      for (net.minecraft.client.renderer.block.model.BakedQuad quad : quads) {
         int[] data = quad.getVertices();
         for (int v = 0; v + stride <= data.length; v += stride) {
            float x = Float.intBitsToFloat(data[v]);
            float y = Float.intBitsToFloat(data[v + 1]);
            float z = Float.intBitsToFloat(data[v + 2]);
            box[0] = Math.min(box[0], x);
            box[1] = Math.max(box[1], x);
            box[2] = Math.min(box[2], y);
            box[3] = Math.max(box[3], y);
            box[4] = Math.min(box[4], z);
            box[5] = Math.max(box[5], z);
         }
      }
      if (box[0] > box[1]) {
         return new float[]{0.0F, 1.0F, 0.0F, 1.0F, 7.5F / 16.0F, 8.5F / 16.0F};
      }
      return box;
   }

   private static float nearFace(Matrix4f model2view, float back, float front) {
      Vector4f a = new Vector4f(0.0F, 0.0F, front, 1.0F).mul(model2view);
      Vector4f b = new Vector4f(0.0F, 0.0F, back, 1.0F).mul(model2view);
      float z = a.z >= b.z ? front : back;
      return z + (a.z >= b.z ? 0.002F : -0.002F);
   }

   private static void vertex(BufferBuilder buffer, Matrix4f model2view, Matrix4f mvp,
         float x, float y, float z) {
      Vector4f clip = new Vector4f(x, y, z, 1.0F).mul(mvp);
      float inv = clip.w == 0.0F ? 1.0F : 1.0F / clip.w;
      buffer.vertex(model2view, x, y, z)
         .uv(clip.x * inv * 0.5F + 0.5F, clip.y * inv * 0.5F + 0.5F)
         .endVertex();
   }
}
