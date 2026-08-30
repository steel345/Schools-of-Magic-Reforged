package com.paleimitations.schoolsofmagic.client.astral;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

// the stars are built out of quads rather than stamped off a texture. end_sky.png averages a
// purple 115,88,156 and is a dense field of noise, so anything drawn with it came out purple and
// grainy whatever colour it was tinted. built this way the colour and the size are both ours
public class AstralDeepField {
   private static final ResourceLocation WISP = new ResourceLocation("textures/environment/clouds.png");

   private static final float REACH = 100.0F;
   private static final int WISPS = 6;

   // stars are baked once, so a single one cannot be told to blink on its own. instead each layer
   // is split across this many buffers and every buffer is given its own beat, which comes out as
   // the whole field twinkling out of step with itself
   private static final int PHASES = 18;

   // count, size, red, green, blue, turn speed, brightness, how hard it blinks
   private static final float[][] LAYERS = {
      {1100.0F, 0.34F, 0.72F, 0.80F, 1.00F, 0.010F, 0.72F, 0.92F},
      {480.0F, 0.62F, 0.92F, 0.95F, 1.00F, 0.017F, 0.88F, 0.95F},
      {180.0F, 1.15F, 1.00F, 0.96F, 0.88F, 0.026F, 1.00F, 0.98F}
   };

   private static final float[][] CLOUD_TINT = {
      {0.14F, 0.38F, 0.68F},
      {0.10F, 0.52F, 0.55F},
      {0.24F, 0.30F, 0.44F}
   };
   private static final float[] CLOUD_GAIN = {0.013F, 0.010F, 0.008F};

   private static VertexBuffer[][] stars;

   public static void render(PoseStack pose, Matrix4f projection, Vec3 eye, float time) {
      if (stars == null) build();

      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.enableBlend();

      // the ground colour. a flat dark blue rather than a texture, so nothing tints it for us
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(GameRenderer::getPositionColorShader);
      darkness(pose);

      RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

      // the colour drifting through it. the clouds texture is white, so the tint is the whole colour
      RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
      RenderSystem.setShaderTexture(0, WISP);
      for (int band = 0; band < CLOUD_TINT.length; band++) {
         float[] tint = CLOUD_TINT[band];
         float gain = CLOUD_GAIN[band];
         RandomSource wisps = RandomSource.create(4400L + band * 977L);

         for (int j = 0; j < WISPS; j++) {
            pose.pushPose();
            turn(pose, wisps, j, time, 0.008F + wisps.nextFloat() * 0.02F);
            wispCube(pose, REACH * (0.92F - j * 0.03F), 2.5F,
               tint[0] * gain, tint[1] * gain, tint[2] * gain);
            pose.popPose();
         }
      }

      // and the stars over the top
      RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
      RenderSystem.setShader(GameRenderer::getPositionShader);
      RandomSource spin = RandomSource.create(2291L);

      for (int i = 0; i < stars.length; i++) {
         float[] layer = LAYERS[i];
         pose.pushPose();
         turn(pose, spin, i, time, layer[5]);
         Matrix4f turned = pose.last().pose();

         for (int phase = 0; phase < PHASES; phase++) {
            // every group keeps its own floor and its own ceiling, so some sink near black and
            // some barely dim, and the ones that climb highest are not the same ones each time
            int seed = i * 977 + phase * 31;
            float dim = 0.05F + roll(seed * 2 + 7) * 0.28F;
            float top = 0.70F + roll(seed * 3 + 13) * 0.36F;
            float lit = layer[6] * (dim + (top - dim) * wander(i, phase, time));

            RenderSystem.setShaderColor(layer[2] * lit, layer[3] * lit, layer[4] * lit, 1.0F);
            stars[i][phase].bind();
            stars[i][phase].drawWithShader(turned, projection, RenderSystem.getShader());
            VertexBuffer.unbind();
         }
         pose.popPose();
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.defaultBlendFunc();
      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
   }

   // a fresh roll every frame is white noise, it strobes. this rolls one number a second and
   // eases between them, so a group sinks dark and comes back up instead of snapping about
   private static float wander(int layer, int phase, float time) {
      float pace = 0.85F + phase * 0.09F;
      float walk = time * pace + phase * 3.7F + layer * 11.3F;
      int step = Mth.floor(walk);
      float across = walk - step;
      across = across * across * (3.0F - 2.0F * across);

      int seed = layer * 977 + phase * 31;
      return Mth.lerp(across, roll(step + seed), roll(step + 1 + seed));
   }

   private static float roll(int n) {
      n = (n << 13) ^ n;
      int m = (n * (n * n * 15731 + 789221) + 1376312589) & 0x7FFFFFFF;
      return m / (float) 0x7FFFFFFF;
   }

   private static void build() {
      stars = new VertexBuffer[LAYERS.length][PHASES];
      for (int i = 0; i < LAYERS.length; i++) {
         int each = Math.max(1, (int) LAYERS[i][0] / PHASES);
         for (int phase = 0; phase < PHASES; phase++) {
            stars[i][phase] = field(RandomSource.create(1337L + i * 7919L + phase * 104729L),
               each, LAYERS[i][1]);
         }
      }
   }

   private static VertexBuffer field(RandomSource random, int count, float size) {
      BufferBuilder builder = new BufferBuilder(count * 48);
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

      for (int i = 0; i < count; i++) {
         float z = random.nextFloat() * 2.0F - 1.0F;
         float a = random.nextFloat() * Mth.TWO_PI;
         float r = Mth.sqrt(Math.max(0.0F, 1.0F - z * z));
         Vector3f at = new Vector3f(Mth.cos(a) * r, z, Mth.sin(a) * r).mul(REACH);

         float half = size * (0.6F + random.nextFloat() * 0.8F);
         Vector3f normal = new Vector3f(at).normalize();
         Vector3f right = new Vector3f(0.0F, 1.0F, 0.0F).cross(normal);
         if (right.lengthSquared() < 1.0E-4F) right.set(1.0F, 0.0F, 0.0F);
         right.normalize();
         Vector3f up = new Vector3f(normal).cross(right).normalize();

         float roll = random.nextFloat() * Mth.TWO_PI;
         float c = Mth.cos(roll);
         float s = Mth.sin(roll);
         Vector3f rx = new Vector3f(right).mul(c).add(new Vector3f(up).mul(s)).mul(half);
         Vector3f ry = new Vector3f(up).mul(c).sub(new Vector3f(right).mul(s)).mul(half);

         corner(builder, at, rx, ry, -1, -1);
         corner(builder, at, rx, ry, -1, 1);
         corner(builder, at, rx, ry, 1, 1);
         corner(builder, at, rx, ry, 1, -1);
      }

      VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
      buffer.bind();
      buffer.upload(builder.end());
      VertexBuffer.unbind();
      return buffer;
   }

   private static void corner(BufferBuilder out, Vector3f at, Vector3f rx, Vector3f ry, int sx, int sy) {
      out.vertex(at.x + rx.x * sx + ry.x * sy,
                 at.y + rx.y * sx + ry.y * sy,
                 at.z + rx.z * sx + ry.z * sy).endVertex();
   }

   private static void turn(PoseStack pose, RandomSource random, int index, float time, float speed) {
      float x = (index * 68731L + time * speed * (random.nextFloat() - 0.5F)) % 360.0F;
      float y = (index * 31337L + time * speed * (random.nextFloat() - 0.5F)) % 360.0F;
      float z = (index * 15731L + time * speed * (random.nextFloat() - 0.5F)) % 360.0F;
      pose.mulPose(Axis.XP.rotationDegrees(x));
      pose.mulPose(Axis.YP.rotationDegrees(y));
      pose.mulPose(Axis.ZP.rotationDegrees(z));
   }

   private static void darkness(PoseStack pose) {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder builder = tesselator.getBuilder();
      float side = REACH * 1.02F;

      for (int face = 0; face < 6; face++) {
         pose.pushPose();
         spin(pose, face);
         Matrix4f matrix = pose.last().pose();
         builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
         builder.vertex(matrix, -side, -side, -side).color(0.015F, 0.018F, 0.035F, 1.0F).endVertex();
         builder.vertex(matrix, -side, -side, side).color(0.015F, 0.018F, 0.035F, 1.0F).endVertex();
         builder.vertex(matrix, side, -side, side).color(0.015F, 0.018F, 0.035F, 1.0F).endVertex();
         builder.vertex(matrix, side, -side, -side).color(0.015F, 0.018F, 0.035F, 1.0F).endVertex();
         tesselator.end();
         pose.popPose();
      }
   }

   private static void wispCube(PoseStack pose, float side, float tile, float r, float g, float b) {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder builder = tesselator.getBuilder();

      for (int face = 0; face < 6; face++) {
         pose.pushPose();
         spin(pose, face);
         Matrix4f matrix = pose.last().pose();
         builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
         builder.vertex(matrix, -side, -side, -side).uv(0.0F, 0.0F).color(r, g, b, 1.0F).endVertex();
         builder.vertex(matrix, -side, -side, side).uv(0.0F, tile).color(r, g, b, 1.0F).endVertex();
         builder.vertex(matrix, side, -side, side).uv(tile, tile).color(r, g, b, 1.0F).endVertex();
         builder.vertex(matrix, side, -side, -side).uv(tile, 0.0F).color(r, g, b, 1.0F).endVertex();
         tesselator.end();
         pose.popPose();
      }
   }

   private static void spin(PoseStack pose, int face) {
      switch (face) {
         case 1 -> pose.mulPose(Axis.XP.rotationDegrees(90.0F));
         case 2 -> pose.mulPose(Axis.XP.rotationDegrees(-90.0F));
         case 3 -> pose.mulPose(Axis.XP.rotationDegrees(180.0F));
         case 4 -> pose.mulPose(Axis.ZP.rotationDegrees(90.0F));
         case 5 -> pose.mulPose(Axis.ZP.rotationDegrees(-90.0F));
         default -> { }
      }
   }

   public static void discard() {
      if (stars == null) return;
      for (VertexBuffer[] layer : stars) {
         for (VertexBuffer buffer : layer) buffer.close();
      }
      stars = null;
   }
}
