package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public class ModelFireBall<T extends Entity> extends HierarchicalModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "fire_ball"), "main");

   private final ModelPart root;
   private final ModelPart fireball;

   public ModelFireBall(ModelPart root) {
      this.root = root;
      this.fireball = root.getChild("fireball");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition fireball = partdefinition.addOrReplaceChild("fireball", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

      PartDefinition core = fireball.addOrReplaceChild("core", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
         .texOffs(0, 28).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
         .texOffs(0, 33).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
         .texOffs(0, 12).addBox(3.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
         .texOffs(10, 12).addBox(-4.0F, -2.0F, -2.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
         .texOffs(24, 32).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
         .texOffs(34, 32).addBox(-2.0F, -2.0F, 3.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flames = fireball.addOrReplaceChild("flames", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition cube_r1 = flames.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.3142F, 0.0F, 0.0F));

      PartDefinition cube_r2 = flames.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(24, 24).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, -0.3142F, 0.0F, 0.0F));

      PartDefinition cube_r3 = flames.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.3142F, 0.0F));

      PartDefinition cube_r4 = flames.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, -0.3142F, 0.0F));

      PartDefinition trail = fireball.addOrReplaceChild("trail", CubeListBuilder.create()
         .texOffs(0, 20).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
         .texOffs(44, 32).addBox(-1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

      PartDefinition embers = fireball.addOrReplaceChild("embers", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 10.0F));

      PartDefinition cube_r5 = embers.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(24, 37).addBox(-3.0F, -4.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.384F));

      PartDefinition cube_r6 = embers.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 37).addBox(2.0F, 1.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

      PartDefinition cube_r7 = embers.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(40, 37).addBox(-1.0F, -3.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2094F, 0.0F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public ModelPart root() {
      return this.root;
   }

   public void play(AnimationDefinition definition, float ageInTicks) {
      this.root.getAllParts().forEach(ModelPart::resetPose);
      KeyframeAnimations.animate(this, definition, (long) (ageInTicks * 1000.0F / 20.0F), 1.0F, new Vector3f());
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.fireball.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
