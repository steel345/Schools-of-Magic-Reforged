package com.paleimitations.schoolsofmagic.client.tileentity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.MowzieModelBase;
import com.paleimitations.schoolsofmagic.client.entity.model.MowzieModelRenderer;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public class ModelDemonHeart extends MowzieModelBase<Entity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "demon_heart"), "main");

   public final MowzieModelRenderer heart_part1;
   public final MowzieModelRenderer valve1;
   public final MowzieModelRenderer valve2;
   public final MowzieModelRenderer valve3;
   public final MowzieModelRenderer heart_part2;
   public final MowzieModelRenderer heart_part3;
   public final MowzieModelRenderer valve1_1;
   public final MowzieModelRenderer valve1_2;
   public final MowzieModelRenderer valve1_3;
   public final MowzieModelRenderer valve1_3_1;
   public final MowzieModelRenderer valve2_1;
   public final MowzieModelRenderer valve3_1;

   public ModelDemonHeart(ModelPart root) {
      super(root);
      this.heart_part1 = new MowzieModelRenderer(root.getChild("heart_part1"));
      this.heart_part2 = new MowzieModelRenderer(root.getChild("heart_part2"));
      this.heart_part3 = new MowzieModelRenderer(root.getChild("heart_part3"));
      this.valve1 = new MowzieModelRenderer(root.getChild("valve1"));
      this.valve2 = new MowzieModelRenderer(root.getChild("valve2"));
      this.valve3 = new MowzieModelRenderer(root.getChild("valve3"));
      this.valve1_1 = new MowzieModelRenderer(this.valve1.part.getChild("valve1_1"));
      this.valve1_2 = new MowzieModelRenderer(this.valve1_1.part.getChild("valve1_2"));
      this.valve1_3 = new MowzieModelRenderer(this.valve1_2.part.getChild("valve1_3"));
      this.valve1_3_1 = new MowzieModelRenderer(this.valve1_3.part.getChild("valve1_3_1"));
      this.valve2_1 = new MowzieModelRenderer(this.valve2.part.getChild("valve2_1"));
      this.valve3_1 = new MowzieModelRenderer(this.valve3.part.getChild("valve3_1"));
      this.parts.add(this.heart_part1);
      this.parts.add(this.heart_part2);
      this.parts.add(this.heart_part3);
      this.parts.add(this.valve1);
      this.parts.add(this.valve2);
      this.parts.add(this.valve3);
      this.parts.add(this.valve1_1);
      this.parts.add(this.valve1_2);
      this.parts.add(this.valve1_3);
      this.parts.add(this.valve1_3_1);
      this.parts.add(this.valve2_1);
      this.parts.add(this.valve3_1);
      this.heart_part1.setInitValuesToCurrentPose();
      this.heart_part2.setInitValuesToCurrentPose();
      this.heart_part3.setInitValuesToCurrentPose();
      this.valve1.setInitValuesToCurrentPose();
      this.valve2.setInitValuesToCurrentPose();
      this.valve3.setInitValuesToCurrentPose();
      this.valve1_1.setInitValuesToCurrentPose();
      this.valve1_2.setInitValuesToCurrentPose();
      this.valve1_3.setInitValuesToCurrentPose();
      this.valve1_3_1.setInitValuesToCurrentPose();
      this.valve2_1.setInitValuesToCurrentPose();
      this.valve3_1.setInitValuesToCurrentPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition valve3 = root.addOrReplaceChild("valve3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(3.4F, 24.0F, 4.0F, 0.27314404F, -0.045553092F, -0.091106184F));
      valve3.addOrReplaceChild("valve3_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 2, 4, 2),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.5009095F, 0.045553092F, -0.22759093F));

      PartDefinition valve2 = root.addOrReplaceChild("valve2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, 0.0F, 2, 3, 2),
         PartPose.offsetAndRotation(6.0F, 24.0F, -5.0F, -0.13665928F, 0.0F, -0.045553092F));
      valve2.addOrReplaceChild("valve2_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -8.0F, 0.0F, 2, 8, 2),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.4553564F, -0.045553092F, -0.22759093F));

      PartDefinition valve1 = root.addOrReplaceChild("valve1",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -5.0F, 0.0F, 2, 5, 2),
         PartPose.offset(-6.0F, 24.0F, 0.0F));
      PartDefinition valve1_1 = valve1.addOrReplaceChild("valve1_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 2, 6, 2),
         PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.6981317F));
      PartDefinition valve1_2 = valve1_1.addOrReplaceChild("valve1_2",
         CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, -2.0F, -2.0F, 4, 2, 2),
         PartPose.offsetAndRotation(2.0F, -2.0F, 2.0F, 0.0F, 0.4553564F, 0.0F));
      PartDefinition valve1_3 = valve1_2.addOrReplaceChild("valve1_3",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, 0.0F, 3, 2, 2),
         PartPose.offsetAndRotation(4.0F, 0.0F, -2.0F, 0.091106184F, -0.22759093F, -0.68294734F));
      valve1_3.addOrReplaceChild("valve1_3_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, 0.0F, 2, 2, 2),
         PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.045553092F, 0.18203785F, -0.4553564F));

      root.addOrReplaceChild("heart_part1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -16.0F, -5.0F, 10, 16, 10),
         PartPose.offsetAndRotation(-2.0F, 16.0F, 1.0F, 0.0F, -0.17453292F, 0.08726646F));
      root.addOrReplaceChild("heart_part2",
         CubeListBuilder.create().texOffs(0, 26).addBox(0.0F, -12.0F, -4.0F, 8, 12, 12),
         PartPose.offset(-2.0F, 17.0F, -2.0F));
      root.addOrReplaceChild("heart_part3",
         CubeListBuilder.create().texOffs(0, 50).addBox(-3.0F, -8.0F, -3.0F, 6, 8, 6),
         PartPose.offset(-1.0F, 18.0F, -4.0F));

      return LayerDefinition.create(mesh, 64, 64);
   }

   public void setAngles() {
      this.valve1.setCurrentPoseToInitValues();
      this.valve2.setCurrentPoseToInitValues();
      this.valve3.setCurrentPoseToInitValues();
      this.valve1_1.setCurrentPoseToInitValues();
      this.valve1_2.setCurrentPoseToInitValues();
      this.valve1_3.setCurrentPoseToInitValues();
      this.valve1_3_1.setCurrentPoseToInitValues();
      this.valve2_1.setCurrentPoseToInitValues();
      this.valve3_1.setCurrentPoseToInitValues();
   }

   public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int tick, TileEntityDemonHeart te) {
      this.setAngles();
      this.heart_part1.part.render(poseStack, buffer, packedLight, packedOverlay);
      this.heart_part2.part.render(poseStack, buffer, packedLight, packedOverlay);
      this.heart_part3.part.render(poseStack, buffer, packedLight, packedOverlay);
      this.valve1.part.render(poseStack, buffer, packedLight, packedOverlay);
      this.valve2.part.render(poseStack, buffer, packedLight, packedOverlay);
      this.valve3.part.render(poseStack, buffer, packedLight, packedOverlay);
      if (te.isActivated() || te.isZigguratHeart()) {
         Vector3f h1Init = this.heart_part1.getInitAngles();
         this.heart_part1.animateAnglesToLinear(
            new Vector3f(h1Init.x, h1Init.y, h1Init.z + (float)Math.toRadians(4.0D)),
            new Vector3f(0.0F, 0.0F, (float)Math.toRadians(-4.0D)),
            tick, 7);
         if (tick > 7) {
            Vector3f h2Init = this.heart_part1.getInitAngles();
            this.heart_part2.animateAnglesToLinear(
               new Vector3f(h2Init.x, h2Init.y, h2Init.z + (float)Math.toRadians(-5.0D)),
               new Vector3f(0.0F, 0.0F, (float)Math.toRadians(5.0D)),
               tick - 8, 7);
         }
         Vector3f h3Init = this.heart_part1.getInitAngles();
         this.heart_part3.animateAnglesToLinear(
            new Vector3f(h3Init.x + (float)Math.toRadians(-2.5D), h3Init.y, h3Init.z),
            new Vector3f((float)Math.toRadians(2.5D), 0.0F, 0.0F),
            tick, 7);
         if (tick > 7) {
            Vector3f h3Init2 = this.heart_part1.getInitAngles();
            this.heart_part3.animateAnglesToLinear(
               new Vector3f(h3Init2.x + (float)Math.toRadians(-2.5D), h3Init2.y, h3Init2.z),
               new Vector3f((float)Math.toRadians(2.5D), 0.0F, 0.0F),
               tick - 8, 7);
         }
      }
   }

   @Override
   public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

   }
}
