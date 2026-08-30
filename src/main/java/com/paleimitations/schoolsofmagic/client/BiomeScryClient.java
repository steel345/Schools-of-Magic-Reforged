package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

// the way to the mark, laid on the ground rather than hung in the air. every dash is put at the
// height of whatever it is lying on, so the line climbs hills instead of running through them
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BiomeScryClient {
   private static float R = 83.0F / 255.0F;
   private static float G = 103.0F / 255.0F;
   private static float B = 41.0F / 255.0F;

   private static final double NEAR = 24.0D;
   private static final int DASHES = 26;
   private static final double SPACING = 1.1D;
   private static final double DASH_LONG = 0.55D;
   private static final double DASH_WIDE = 0.11D;

   private static BlockPos mark;

   public static void mark(BlockPos at, boolean on, int rgb) {
      mark = on ? at : null;
      R = ((rgb >> 16) & 0xFF) / 255.0F;
      G = ((rgb >> 8) & 0xFF) / 255.0F;
      B = (rgb & 0xFF) / 255.0F;
   }

   public static BlockPos mark() {
      return mark;
   }

   public static void clear() {
      mark = null;
   }

   @SubscribeEvent
   public static void onRender(RenderLevelStageEvent event) {
      if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
      if (mark == null) return;

      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || mc.level == null) return;

      Vec3 eye = event.getCamera().getPosition();
      Vec3 to = new Vec3(mark.getX() + 0.5D, mark.getY(), mark.getZ() + 0.5D);

      double dx = to.x - player.getX();
      double dz = to.z - player.getZ();
      double flat = Math.sqrt(dx * dx + dz * dz);
      if (flat < 1.0E-3D) return;

      PoseStack pose = event.getPoseStack();
      pose.pushPose();

      RenderSystem.enableBlend();
      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);

      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder builder = tesselator.getBuilder();
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      Matrix4f matrix = pose.last().pose();

      if (flat <= NEAR) {
         cross(builder, matrix, mc.level, to, eye, player.getY());
      } else {
         // hung in the air out in front rather than laid on the floor, so it can be followed
         // without looking down at your feet the whole way
         Vec3 from = player.position().add(0.0D, player.getEyeHeight() * 0.6D, 0.0D);
         Vec3 way = to.subtract(from).normalize();
         float drift = (player.tickCount + event.getPartialTick()) * 0.04F;

         for (int i = 0; i < DASHES; i++) {
            // the whole line creeps forward, so it reads as pointing somewhere
            double along = 1.4D + (i + drift % 1.0F) * SPACING;
            Vec3 at = from.add(way.scale(along));

            float fade = 1.0F - (float) i / DASHES;
            float wide = 0.075F + i * 0.006F;
            ribbon(builder, matrix, at.subtract(eye), way, at.subtract(eye).normalize(),
               wide, fade * 0.85F);
         }
      }

      tesselator.end();

      RenderSystem.defaultBlendFunc();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
      pose.popPose();
   }

   // two bars crossed over the spot, lying flat like the dashes do
   private static void cross(BufferBuilder out, Matrix4f matrix, Level level, Vec3 to, Vec3 eye, double from) {
      double y = ground(level, to.x, to.z, from);
      if (Double.isNaN(y)) y = to.y;

      double diag = Math.sqrt(0.5D);
      for (int i = -3; i <= 3; i++) {
         double reach = i * 0.34D;
         dash(out, matrix, to.x + reach - eye.x, y - eye.y + 0.02D, to.z + reach - eye.z,
            diag, diag, 0.95F);
         dash(out, matrix, to.x + reach - eye.x, y - eye.y + 0.02D, to.z - reach - eye.z,
            diag, -diag, 0.95F);
      }
   }

   // one mark of the line, turned along the way it points and rolled to face the camera, so a
   // dash stays a dash whichever side of it you are stood on
   private static void ribbon(BufferBuilder out, Matrix4f matrix, Vec3 at, Vec3 along,
                              Vec3 toEye, float wide, float alpha) {
      Vec3 side = along.cross(toEye);
      if (side.lengthSqr() < 1.0E-6D) return;
      side = side.normalize().scale(wide);
      Vec3 run = along.scale(DASH_LONG * 0.5D);

      out.vertex(matrix, (float) (at.x - run.x - side.x), (float) (at.y - run.y - side.y),
         (float) (at.z - run.z - side.z)).color(R, G, B, alpha).endVertex();
      out.vertex(matrix, (float) (at.x - run.x + side.x), (float) (at.y - run.y + side.y),
         (float) (at.z - run.z + side.z)).color(R, G, B, alpha).endVertex();
      out.vertex(matrix, (float) (at.x + run.x + side.x), (float) (at.y + run.y + side.y),
         (float) (at.z + run.z + side.z)).color(R, G, B, alpha).endVertex();
      out.vertex(matrix, (float) (at.x + run.x - side.x), (float) (at.y + run.y - side.y),
         (float) (at.z + run.z - side.z)).color(R, G, B, alpha).endVertex();
   }

   // one mark of the cross on the floor, turned to lie along the way it is pointing
   private static void dash(BufferBuilder out, Matrix4f matrix, double x, double y, double z,
                            double dirX, double dirZ, float alpha) {
      double sideX = -dirZ;
      double sideZ = dirX;

      double ax = dirX * DASH_LONG * 0.5D;
      double az = dirZ * DASH_LONG * 0.5D;
      double bx = sideX * DASH_WIDE * 0.5D;
      double bz = sideZ * DASH_WIDE * 0.5D;

      out.vertex(matrix, (float) (x - ax - bx), (float) y, (float) (z - az - bz)).color(R, G, B, alpha).endVertex();
      out.vertex(matrix, (float) (x - ax + bx), (float) y, (float) (z - az + bz)).color(R, G, B, alpha).endVertex();
      out.vertex(matrix, (float) (x + ax + bx), (float) y, (float) (z + az + bz)).color(R, G, B, alpha).endVertex();
      out.vertex(matrix, (float) (x + ax - bx), (float) y, (float) (z + az - bz)).color(R, G, B, alpha).endVertex();
   }

   // the first solid top face under the walker, so the line lies on the world instead of in it
   private static double ground(Level level, double x, double z, double from) {
      BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos();
      int top = Mth.floor(from) + 4;
      int floor = top - 28;

      for (int y = top; y >= floor; y--) {
         at.set(Mth.floor(x), y, Mth.floor(z));
         if (level.getBlockState(at).isFaceSturdy(level, at, Direction.UP)) return y + 1.0D;
      }
      return Double.NaN;
   }
}
