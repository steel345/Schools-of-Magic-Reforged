package com.paleimitations.schoolsofmagic.client.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

// the fist itself. it is not an entity, the spell draws it where it says it is, so the model keeps
// its own animation clock instead of being handed one
public class ModelPoseidonsFist extends HierarchicalModel<Entity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "poseidons_fist"), "main");
   public static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/poseidons_fist.png");

   private static final Vector3f SCRATCH = new Vector3f();

   public static final AnimationDefinition SPAWN = AnimationDefinition.Builder.withLength(0.5F)
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 38.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.3F, KeyframeAnimations.degreeVec(0.0F, -7.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, -22.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 3.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, -1.2F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.SCALE, 
         new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.2F, KeyframeAnimations.scaleVec(0.72F, 1.22F, 0.72F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.3F, KeyframeAnimations.scaleVec(1.14F, 0.9F, 1.14F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.scaleVec(0.95F, 1.05F, 0.95F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.5F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .build();
   
   public static final AnimationDefinition DESPAWN = AnimationDefinition.Builder.withLength(0.45F)
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.45F, KeyframeAnimations.degreeVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.15F, KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.SCALE, 
         new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.15F, KeyframeAnimations.scaleVec(1.1F, 0.68F, 1.1F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.3F, KeyframeAnimations.scaleVec(0.55F, 0.82F, 0.55F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.45F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
      ))
      .build();
   
   public static final AnimationDefinition PULL = AnimationDefinition.Builder.withLength(0.4F)
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.9F, 2.5F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("finger_curl", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.degreeVec(-34.56F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("finger_curl", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fingers", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fingers", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 2.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("tip_l_j", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("tip_l_j", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.4F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .build();
   
   public static final AnimationDefinition PULL_UNDER = AnimationDefinition.Builder.withLength(0.7F)
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.2F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.65F, KeyframeAnimations.degreeVec(0.0F, -32.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.2F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.35F, KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.65F, KeyframeAnimations.posVec(0.0F, -20.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fist", new AnimationChannel(AnimationChannel.Targets.SCALE, 
         new Keyframe(0.2F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.35F, KeyframeAnimations.scaleVec(1.1F, 0.68F, 1.1F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.5F, KeyframeAnimations.scaleVec(0.55F, 0.82F, 0.55F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.65F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
      ))
      .addAnimation("finger_curl", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
         new Keyframe(0.45F, KeyframeAnimations.degreeVec(-34.56F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("finger_curl", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fingers", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.45F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("fingers", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 2.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("tip_l_j", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
         new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.45F, KeyframeAnimations.degreeVec(-40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .addAnimation("tip_l_j", new AnimationChannel(AnimationChannel.Targets.POSITION, 
         new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
         new Keyframe(0.45F, KeyframeAnimations.posVec(0.0F, 1.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM)
      ))
      .build();

   private final ModelPart root;
   private final ModelPart fist;

   public ModelPoseidonsFist(ModelPart root) {
      this.root = root;
      this.fist = root.getChild("fist");
   }

   @Override
   public ModelPart root() {
      return this.root;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
   
      PartDefinition fist = partdefinition.addOrReplaceChild("fist", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 0.0F, -3.0F, 12.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 0.0F));
   
      PartDefinition fingers = fist.addOrReplaceChild("fingers", CubeListBuilder.create(), PartPose.offset(4.0F, 6.0F, -5.0F));
   
      PartDefinition tip_l_j = fingers.addOrReplaceChild("tip_l_j", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -11.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
   
      PartDefinition tip_l_r1 = tip_l_j.addOrReplaceChild("tip_l_r1", CubeListBuilder.create().texOffs(2, 17).addBox(-0.7983F, -4.8659F, -2.5446F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.7017F, 3.9677F, -1.6755F, -1.7704F, -0.2926F, -0.8139F));
   
      PartDefinition tip_l_r2 = tip_l_j.addOrReplaceChild("tip_l_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-6.5651F, -3.1629F, -4.0049F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(0, 16).addBox(-2.5651F, -5.0107F, -4.7703F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(0, 16).addBox(1.4349F, -3.1629F, -4.0049F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4349F, 2.3207F, 1.7778F, -1.9635F, 0.0F, 0.0F));
   
      PartDefinition tip_l_r3 = tip_l_j.addOrReplaceChild("tip_l_r3", CubeListBuilder.create().texOffs(0, 16).addBox(5.9605F, -2.1535F, -1.5778F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4349F, 2.3207F, 1.7778F, -1.616F, 0.2615F, -0.0117F));
   
      PartDefinition finger_curl = fist.addOrReplaceChild("finger_curl", CubeListBuilder.create().texOffs(48, 12).addBox(-2.0F, -8.0F, -3.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(16, 16).addBox(2.0F, -7.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(16, 28).addBox(-6.0F, -6.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));
   
      PartDefinition thumb_r1 = finger_curl.addOrReplaceChild("thumb_r1", CubeListBuilder.create().texOffs(32, 26).addBox(-2.0751F, -0.7276F, -2.4487F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.7017F, -3.3245F, 3.9677F, -0.1033F, 0.7699F, -0.4135F));
   
      PartDefinition wrist_r1 = finger_curl.addOrReplaceChild("wrist_r1", CubeListBuilder.create().texOffs(41, 1).addBox(-3.0F, -3.0F, 0.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.4363F));
   
      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float age, float yaw, float pitch) {
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer out, int light, int overlay,
                              float r, float g, float b, float a) {
      this.fist.render(pose, out, light, overlay, r, g, b, a);
   }

   // seconds since the pose started, not ticks. nothing ticks this model for us
   public void poseAt(AnimationDefinition clip, float seconds, boolean hold) {
      this.root.getAllParts().forEach(ModelPart::resetPose);
      if (clip == null) return;
      float at = hold ? Math.min(seconds, clip.lengthInSeconds() - 0.001F) : seconds % clip.lengthInSeconds();
      KeyframeAnimations.animate(this, clip, (long) (at * 1000.0F), 1.0F, SCRATCH);
   }

   public void render(PoseStack pose, MultiBufferSource buffer, Entity caster, float scale,
                      Vec3 at, float partial, int light) {
      Minecraft mc = Minecraft.getInstance();
      Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

      pose.pushPose();
      pose.translate(at.x - cam.x, at.y - cam.y, at.z - cam.z);
      pose.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partial, caster.yRotO, caster.getYRot()) + 180.0F));
      pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
      pose.scale(scale, scale, scale);
      pose.translate(0.0D, -1.5D, 0.0D);

      VertexConsumer out = buffer.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
      this.fist.render(pose, out, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.86F);
      pose.popPose();

      // flushed here rather than left in the batch. waiting meant it came out at a different
      // point every frame against everything else translucent, which is what the flicker was
      if (buffer instanceof MultiBufferSource.BufferSource batch) {
         batch.endBatch(RenderType.entityTranslucentCull(TEXTURE));
      }
   }

   // the water it is dragging along under itself
   public static void puddle(PoseStack pose, Vec3 at, float radius, float age) {
      Minecraft mc = Minecraft.getInstance();
      Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();

      com.mojang.blaze3d.systems.RenderSystem.enableBlend();
      com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(false);
      com.mojang.blaze3d.systems.RenderSystem.disableCull();
      com.mojang.blaze3d.systems.RenderSystem.setShader(
         net.minecraft.client.renderer.GameRenderer::getPositionColorShader);

      pose.pushPose();
      pose.translate(at.x - cam.x, at.y - cam.y + 0.02D, at.z - cam.z);
      org.joml.Matrix4f matrix = pose.last().pose();

      com.mojang.blaze3d.vertex.Tesselator tesselator = com.mojang.blaze3d.vertex.Tesselator.getInstance();
      com.mojang.blaze3d.vertex.BufferBuilder builder = tesselator.getBuilder();
      builder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.TRIANGLE_FAN,
         com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR);

      // a ring of points round a bright middle, breathing in and out a little as it goes
      builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(0.35F, 0.62F, 0.95F, 0.55F).endVertex();
      int steps = 20;
      for (int i = 0; i <= steps; i++) {
         float turn = (float) i / steps * Mth.TWO_PI;
         float wobble = radius * (0.86F + 0.14F * Mth.sin(turn * 3.0F + age * 0.18F));
         builder.vertex(matrix, Mth.cos(turn) * wobble, 0.0F, Mth.sin(turn) * wobble)
            .color(0.22F, 0.48F, 0.88F, 0.0F).endVertex();
      }
      tesselator.end();

      pose.popPose();
      com.mojang.blaze3d.systems.RenderSystem.depthMask(true);
      com.mojang.blaze3d.systems.RenderSystem.enableCull();
   }
}
