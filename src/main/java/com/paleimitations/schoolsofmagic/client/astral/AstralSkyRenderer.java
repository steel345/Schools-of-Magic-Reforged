package com.paleimitations.schoolsofmagic.client.astral;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

// everything is drawn on a shell around the camera. the camera turning is already in the pose we
// are handed, so all this has to add is the walking, and it adds a different amount of it to every
// layer. that difference is the whole illusion
public class AstralSkyRenderer {
   private static final float SHELL = 100.0F;
   private static final float REACH = 42.0F;

   static final int KIND_STAR = 0;
   static final int KIND_NEBULA = 1;
   static final int KIND_LINE = 2;
   static final int KIND_PLANET = 3;
   static final int KIND_GLOW = 4;
   static final int KIND_RING = 5;

   private static final List<Baked> BAKED = new ArrayList<>();
   private static final List<AstralObject> LIVE = new ArrayList<>();
   private static AstralObject deep;
   private static VertexBuffer scratch;
   private static int bakedRevision = -1;

   private record Baked(VertexBuffer buffer, float parallax, int kind, float opacity) {}

   public static void discard() {
      deep = null;
      AstralDeepField.discard();
      if (scratch != null) {
         scratch.close();
         scratch = null;
      }
      for (Baked baked : BAKED) baked.buffer.close();
      BAKED.clear();
      LIVE.clear();
      bakedRevision = -1;
   }

   private static void bake() {
      discard();
      bakedRevision = AstralScene.revision();

      for (AstralObject object : AstralScene.objects()) {
         switch (object.type) {
            case "deep" -> deep = object;
            case "starfield" -> BAKED.add(build(object, KIND_STAR, AstralSkyRenderer::starfield));
            case "galaxy" -> BAKED.add(build(object, KIND_STAR, AstralSkyRenderer::galaxy));
            case "nebula" -> BAKED.add(build(object, KIND_NEBULA, AstralSkyRenderer::nebula));
            case "constellation" -> {
               BAKED.add(build(object, KIND_STAR, AstralSkyRenderer::constellationStars));
               if (!object.links.isEmpty()) BAKED.add(build(object, KIND_LINE, AstralSkyRenderer::constellationLinks));
            }
            default -> LIVE.add(object);
         }
      }
   }

   private interface Shape {
      void emit(AstralObject object, VertexConsumer out);
   }

   private static Baked build(AstralObject object, int kind, Shape shape) {
      BufferBuilder builder = new BufferBuilder(2048);
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      shape.emit(object, builder);

      VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
      buffer.bind();
      buffer.upload(builder.end());
      VertexBuffer.unbind();
      return new Baked(buffer, object.parallax, kind, object.opacity);
   }

   // ---------------------------------------------------------------- shapes

   private static void starfield(AstralObject object, VertexConsumer out) {
      RandomSource random = RandomSource.create(object.seed);

      for (int i = 0; i < object.count; i++) {
         Vector3f dir = onSphere(random).mul(SHELL);
         float bright = 0.35F + random.nextFloat() * 0.65F;
         float size = object.scale * (0.45F + random.nextFloat() * 0.9F);
         quad(out, dir, size,
            object.color[0] * bright, object.color[1] * bright, object.color[2] * bright, object.opacity);
      }
   }

   private static void galaxy(AstralObject object, VertexConsumer out) {
      RandomSource random = RandomSource.create(object.seed);
      Vector3f centre = place(object.position);
      float span = span(object.scale, object.position);

      // a flat disc of arms, then tipped over so it is not always face on
      float tilt = object.rotation * Mth.DEG_TO_RAD;
      Vector3f right = new Vector3f(Mth.cos(tilt), 0.0F, -Mth.sin(tilt));
      Vector3f up = new Vector3f(Mth.sin(tilt) * 0.45F, 0.9F, Mth.cos(tilt) * 0.45F).normalize();

      int grains = Math.max(600, object.count);
      for (int i = 0; i < grains; i++) {
         float along = random.nextFloat();
         int arm = random.nextInt(Math.max(1, object.arms));
         float angle = along * 5.2F + arm * (Mth.TWO_PI / Math.max(1, object.arms));
         float reach = (float) Math.pow(along, 0.65D) * span;
         float scatter = (1.0F - along) * 0.25F + 0.05F;

         float dx = Mth.cos(angle) * reach + (random.nextFloat() - 0.5F) * span * scatter;
         float dy = Mth.sin(angle) * reach + (random.nextFloat() - 0.5F) * span * scatter;
         float lift = (random.nextFloat() - 0.5F) * span * 0.06F;

         Vector3f at = new Vector3f(centre)
            .add(right.x * dx + up.x * dy, right.y * dx + up.y * dy, right.z * dx + up.z * dy)
            .add(up.x * lift, up.y * lift, up.z * lift);

         float core = 1.0F - along;
         float r = Mth.lerp(core, object.color[0], object.color2[0]);
         float g = Mth.lerp(core, object.color[1], object.color2[1]);
         float b = Mth.lerp(core, object.color[2], object.color2[2]);
         float bright = 0.25F + core * 0.75F;

         quad(out, at, span * 0.012F * (0.6F + random.nextFloat()),
            r * bright, g * bright, b * bright, object.opacity);
      }
   }

   private static void nebula(AstralObject object, VertexConsumer out) {
      RandomSource random = RandomSource.create(object.seed);
      Vector3f centre = place(object.position);
      float span = span(object.scale, object.position);

      for (int layer = 0; layer < object.layers; layer++) {
         float depth = 1.0F - layer / (float) Math.max(1, object.layers);
         float size = span * (0.55F + depth * 0.75F);
         Vector3f at = new Vector3f(centre).add(
            (random.nextFloat() - 0.5F) * span * 0.5F,
            (random.nextFloat() - 0.5F) * span * 0.5F,
            (random.nextFloat() - 0.5F) * span * 0.5F);

         float mix = random.nextFloat();
         quad(out, at, size, random.nextFloat() * Mth.TWO_PI,
            Mth.lerp(mix, object.color[0], object.color2[0]),
            Mth.lerp(mix, object.color[1], object.color2[1]),
            Mth.lerp(mix, object.color[2], object.color2[2]),
            object.opacity * (0.25F + depth * 0.35F));
      }
   }

   private static void constellationStars(AstralObject object, VertexConsumer out) {
      for (Vec3 star : object.stars) {
         quad(out, place(star), span(object.scale, star),
            object.color[0], object.color[1], object.color[2], object.opacity);
      }
   }

   private static void constellationLinks(AstralObject object, VertexConsumer out) {
      for (int[] link : object.links) {
         if (link[0] < 0 || link[1] < 0 || link[0] >= object.stars.size() || link[1] >= object.stars.size()) continue;
         Vec3 from = object.stars.get(link[0]);
         Vec3 to = object.stars.get(link[1]);
         line(out, place(from), place(to), span(object.scale, from) * 0.18F,
            object.color2[0], object.color2[1], object.color2[2], object.opacity * 0.5F);
      }
   }

   // ---------------------------------------------------------------- geometry helpers

   private static Vector3f place(Vec3 position) {
      Vector3f out = new Vector3f((float) position.x, (float) position.y, (float) position.z);
      if (out.lengthSquared() < 1.0E-6F) return new Vector3f(0.0F, 0.0F, -SHELL);
      return out.normalize().mul(SHELL);
   }

   private static float span(float scale, Vec3 position) {
      double away = position.length();
      return away < 1.0E-3D ? scale : (float) (scale / away * SHELL);
   }

   private static Vector3f onSphere(RandomSource random) {
      float z = random.nextFloat() * 2.0F - 1.0F;
      float a = random.nextFloat() * Mth.TWO_PI;
      float r = Mth.sqrt(Math.max(0.0F, 1.0F - z * z));
      return new Vector3f(Mth.cos(a) * r, z, Mth.sin(a) * r);
   }

   // a billboard built where it stands, facing back at the middle of the shell. the camera never
   // strays far enough from the middle for the difference to show
   private static void quad(VertexConsumer out, Vector3f at, float half, float r, float g, float b, float a) {
      quad(out, at, half, 0.0F, r, g, b, a);
   }

   private static void quad(VertexConsumer out, Vector3f at, float half, float roll,
                            float r, float g, float b, float a) {
      Vector3f normal = new Vector3f(at).normalize();
      Vector3f right = new Vector3f(0.0F, 1.0F, 0.0F).cross(normal);
      if (right.lengthSquared() < 1.0E-4F) right.set(1.0F, 0.0F, 0.0F);
      right.normalize();
      Vector3f up = new Vector3f(normal).cross(right).normalize();

      float c = Mth.cos(roll);
      float s = Mth.sin(roll);
      Vector3f spunRight = new Vector3f(right).mul(c).add(new Vector3f(up).mul(s)).mul(half);
      Vector3f spunUp = new Vector3f(up).mul(c).sub(new Vector3f(right).mul(s)).mul(half);
      right = spunRight;
      up = spunUp;

      corner(out, at, right, up, -1, -1, r, g, b, a, 0.0F, 0.0F);
      corner(out, at, right, up, -1, 1, r, g, b, a, 0.0F, 1.0F);
      corner(out, at, right, up, 1, 1, r, g, b, a, 1.0F, 1.0F);
      corner(out, at, right, up, 1, -1, r, g, b, a, 1.0F, 0.0F);
   }

   private static void line(VertexConsumer out, Vector3f from, Vector3f to, float half,
                            float r, float g, float b, float a) {
      Vector3f along = new Vector3f(to).sub(from);
      Vector3f normal = new Vector3f(from).add(to).mul(0.5F).normalize();
      Vector3f side = new Vector3f(along).cross(normal).normalize().mul(half);

      out.vertex(from.x - side.x, from.y - side.y, from.z - side.z).color(r, g, b, a).uv(0.0F, 0.0F).endVertex();
      out.vertex(from.x + side.x, from.y + side.y, from.z + side.z).color(r, g, b, a).uv(0.0F, 1.0F).endVertex();
      out.vertex(to.x + side.x, to.y + side.y, to.z + side.z).color(r, g, b, a).uv(1.0F, 1.0F).endVertex();
      out.vertex(to.x - side.x, to.y - side.y, to.z - side.z).color(r, g, b, a).uv(1.0F, 0.0F).endVertex();
   }

   private static void corner(VertexConsumer out, Vector3f at, Vector3f right, Vector3f up,
                              int sx, int sy, float r, float g, float b, float a, float u, float v) {
      out.vertex(at.x + right.x * sx + up.x * sy,
                 at.y + right.y * sx + up.y * sy,
                 at.z + right.z * sx + up.z * sy)
         .color(r, g, b, a).uv(u, v).endVertex();
   }

   // ---------------------------------------------------------------- drawing

   public static void render(PoseStack pose, Matrix4f projection, Camera camera, float partialTick, long ticks) {
      if (bakedRevision != AstralScene.revision()) bake();

      float time = (ticks + partialTick) * 0.05F;
      Vec3 eye = camera.getPosition();

      // the portal field goes down first and it goes down whatever else is or is not loaded.
      // it used to sit behind the checks below, so a scene holding nothing but the field left
      // us returning early and looking at a black box
      AstralDeepField.render(pose, projection, eye, time);

      if (BAKED.isEmpty() && LIVE.isEmpty()) return;

      ShaderInstance shader = AstralShaders.astral();
      if (shader == null) return;

      RenderSystem.enableBlend();
      RenderSystem.blendFunc(com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA,
                             com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE);
      RenderSystem.depthMask(false);
      RenderSystem.disableCull();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      for (Baked baked : BAKED) {
         pose.pushPose();
         shift(pose, eye, baked.parallax);
         set(shader, baked.kind, time, 0.0F);
         baked.buffer.bind();
         baked.buffer.drawWithShader(pose.last().pose(), projection, shader);
         VertexBuffer.unbind();
         pose.popPose();
      }

      for (AstralObject object : LIVE) {
         planet(pose, projection, shader, object, eye, time);
      }

      RenderSystem.depthMask(true);
      RenderSystem.enableCull();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableBlend();
   }

   // walking moves each layer by its own share of the distance, wrapped so a long walk never runs
   // the sky off its own edge
   private static void shift(PoseStack pose, Vec3 eye, float parallax) {
      pose.translate(ease(-eye.x * parallax), ease(-eye.y * parallax), ease(-eye.z * parallax));
   }

   // linear while you are near where you started and easing off after, so a long walk shifts the
   // near layers hard without ever dragging them off the far plane, and it never jumps
   private static double ease(double value) {
      return Math.tanh(value / REACH) * REACH;
   }

   private static void set(ShaderInstance shader, int kind, float time, float spin) {
      if (shader.getUniform("SomKind") != null) shader.getUniform("SomKind").set((float) kind);
      if (shader.getUniform("SomTime") != null) shader.getUniform("SomTime").set(time);
      if (shader.getUniform("SomSpin") != null) shader.getUniform("SomSpin").set(spin);
   }

   // planets are few and they sit close, so they are rebuilt every frame and aimed properly at the
   // camera instead of being baked facing the middle
   private static void planet(PoseStack pose, Matrix4f projection, ShaderInstance shader,
                              AstralObject object, Vec3 eye, float time) {
      Vector3f at = place(object.position);
      float half = Math.max(0.05F, span(object.scale, object.position));

      float spin = object.rotation * Mth.DEG_TO_RAD + time * object.rotationSpeed;

      pose.pushPose();
      shift(pose, eye, object.parallax);

      if (object.atmosphere) {
         draw(pose, projection, shader, at, half * 1.35F, KIND_GLOW, time, spin,
            object.color2[0], object.color2[1], object.color2[2], object.opacity * 0.55F);
      }
      if (object.rings) {
         draw(pose, projection, shader, at, half * 2.1F, KIND_RING, time, spin,
            object.color2[0], object.color2[1], object.color2[2], object.opacity * 0.8F);
      }
      draw(pose, projection, shader, at, half, KIND_PLANET, time, spin,
         object.color[0], object.color[1], object.color[2], object.opacity);
      pose.popPose();
   }

   private static void draw(PoseStack pose, Matrix4f projection, ShaderInstance shader, Vector3f at, float half,
                            int kind, float time, float spin, float r, float g, float b, float a) {
      Tesselator tesselator = Tesselator.getInstance();
      BufferBuilder builder = tesselator.getBuilder();
      builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
      quad(builder, at, half, r, g, b, a);

      set(shader, kind, time, spin);
      RenderSystem.setShader(() -> shader);

      if (scratch == null) scratch = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
      scratch.bind();
      scratch.upload(builder.end());
      scratch.drawWithShader(pose.last().pose(), projection, shader);
      VertexBuffer.unbind();
   }
}
