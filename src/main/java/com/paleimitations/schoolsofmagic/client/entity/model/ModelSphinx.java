package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelSphinx<T extends Entity> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "sphinx"), "main");

   public final ModelPart body;

   public ModelSphinx(ModelPart root) {
      this.body = root.getChild("body");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(45, 35).addBox(-3.0F, -4.0F, -10.0F, 6, 8, 14),
         PartPose.offsetAndRotation(0.0F, 1.7F, 4.0F, -0.091106184F, 0.0F, 0.0F));
      PartDefinition thighRight = body.addOrReplaceChild("thighRight",
         CubeListBuilder.create().texOffs(0, 64).addBox(-3.0F, -3.0F, -3.0F, 3, 9, 6),
         PartPose.offsetAndRotation(-3.0F, 0.0F, 1.0F, 0.045553092F, 0.0F, 0.0F));
      PartDefinition legBackRight = thighRight.addOrReplaceChild("legBackRight",
         CubeListBuilder.create().texOffs(18, 70).addBox(-1.7F, 0.0F, -1.5F, 3, 6, 3),
         PartPose.offsetAndRotation(-1.5F, 5.0F, 0.0F, 1.0016445F, 0.0F, 0.0F));
      legBackRight.addOrReplaceChild("shinBackRight",
         CubeListBuilder.create().texOffs(30, 69).addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3),
         PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -0.95609134F, 0.0F, 0.0F));
      PartDefinition thighLeft = body.addOrReplaceChild("thighLeft",
         CubeListBuilder.create().texOffs(0, 47).addBox(0.0F, -3.0F, -3.0F, 3, 9, 6),
         PartPose.offsetAndRotation(3.0F, 0.0F, 1.0F, 0.045553092F, 0.0F, 0.0F));
      PartDefinition legBackLeft = thighLeft.addOrReplaceChild("legBackLeft",
         CubeListBuilder.create().texOffs(18, 53).addBox(-1.3F, 0.0F, -1.5F, 3, 6, 3),
         PartPose.offsetAndRotation(1.5F, 5.0F, 0.0F, 1.0016445F, 0.0F, 0.0F));
      legBackLeft.addOrReplaceChild("shinBackLeft",
         CubeListBuilder.create().texOffs(30, 52).addBox(-1.5F, 0.0F, -1.5F, 3, 7, 3),
         PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, -0.95609134F, 0.0F, 0.0F));
      PartDefinition tail1 = body.addOrReplaceChild("tail1",
         CubeListBuilder.create().texOffs(72, 40).addBox(-0.5F, -1.0F, -1.0F, 1, 6, 1),
         PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
      PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
         CubeListBuilder.create().texOffs(76, 40).addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1),
         PartPose.offsetAndRotation(0.0F, 5.0F, -0.5F, (float) (Math.PI / 4), 0.0F, 0.0F));
      PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
         CubeListBuilder.create().texOffs(80, 40).addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1),
         PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, (float) (Math.PI / 4), 0.0F, 0.0F));
      tail3.addOrReplaceChild("shape45",
         CubeListBuilder.create().texOffs(101, 57).addBox(-1.0F, -0.5F, -1.0F, 2, 4, 2),
         PartPose.offset(0.0F, 6.0F, 0.0F));
      PartDefinition chest = body.addOrReplaceChild("chest",
         CubeListBuilder.create().texOffs(46, 57).addBox(-4.0F, -5.0F, -10.0F, 8, 10, 10),
         PartPose.offsetAndRotation(0.0F, 0.5F, -8.0F, -0.27925268F, 0.0F, 0.0F));
      PartDefinition right_wing1 = chest.addOrReplaceChild("right_wing1",
         CubeListBuilder.create().texOffs(86, 0).addBox(0.0F, 0.0F, -2.0F, 12, 2, 9),
         PartPose.offsetAndRotation(4.0F, -5.0F, -7.5F, 0.0F, -0.13665928F, 0.0F));
      PartDefinition right_wing2 = right_wing1.addOrReplaceChild("right_wing2",
         CubeListBuilder.create().texOffs(86, 11).addBox(0.0F, -1.0F, -1.0F, 12, 2, 7),
         PartPose.offsetAndRotation(11.0F, 1.0F, -1.0F, 0.0F, 0.22759093F, 0.0F));
      right_wing2.addOrReplaceChild("right_wing3",
         CubeListBuilder.create().texOffs(86, 20).addBox(0.0F, -1.0F, 0.0F, 8, 2, 6),
         PartPose.offsetAndRotation(12.0F, 0.0F, -1.0F, 0.0F, -0.4537856F, 0.0F));
      PartDefinition left_wing1 = chest.addOrReplaceChild("left_wing1",
         CubeListBuilder.create().texOffs(86, 28).addBox(-12.0F, 0.0F, -2.0F, 12, 2, 9),
         PartPose.offsetAndRotation(-4.0F, -5.0F, -7.5F, 0.0F, 0.13665928F, 0.0F));
      PartDefinition left_wing2 = left_wing1.addOrReplaceChild("left_wing2",
         CubeListBuilder.create().texOffs(86, 39).addBox(-12.0F, -1.0F, -1.0F, 12, 2, 7),
         PartPose.offsetAndRotation(-11.0F, 1.0F, -1.0F, 0.0F, -0.22759093F, 0.0F));
      left_wing2.addOrReplaceChild("left_wing3",
         CubeListBuilder.create().texOffs(86, 48).addBox(-8.0F, -1.0F, 0.0F, 8, 2, 6),
         PartPose.offsetAndRotation(-12.0F, 0.0F, -1.0F, 0.0F, 0.4537856F, 0.0F));
      PartDefinition sholderLeft = chest.addOrReplaceChild("sholderLeft",
         CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, -3.0F, -3.0F, 3, 9, 6),
         PartPose.offsetAndRotation(3.0F, 0.0F, -4.5F, 0.4553564F, 0.0F, 0.0F));
      PartDefinition legFrontLeft = sholderLeft.addOrReplaceChild("legFrontLeft",
         CubeListBuilder.create().texOffs(18, 37).addBox(-1.3F, 0.0F, -1.5F, 3, 6, 4),
         PartPose.offset(1.5F, 5.0F, 0.0F));
      legFrontLeft.addOrReplaceChild("forelegFrontLeft",
         CubeListBuilder.create().texOffs(32, 38).addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3),
         PartPose.offsetAndRotation(0.0F, 5.8F, 0.0F, -0.091106184F, 0.0F, 0.0F));
      PartDefinition sholderRight = chest.addOrReplaceChild("sholderRight",
         CubeListBuilder.create().texOffs(0, 17).addBox(-3.0F, -3.0F, -3.0F, 3, 9, 6),
         PartPose.offsetAndRotation(-3.0F, 0.0F, -4.5F, 0.4553564F, 0.0F, 0.0F));
      PartDefinition legFrontRight = sholderRight.addOrReplaceChild("legFrontRight",
         CubeListBuilder.create().texOffs(18, 22).addBox(-1.7F, 0.0F, -1.5F, 3, 6, 4),
         PartPose.offset(-1.5F, 5.0F, 0.0F));
      legFrontRight.addOrReplaceChild("forelegFrontRight",
         CubeListBuilder.create().texOffs(32, 23).addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3),
         PartPose.offsetAndRotation(0.0F, 5.8F, 0.0F, -0.091106184F, 0.0F, 0.0F));
      PartDefinition neck = chest.addOrReplaceChild("neck",
         CubeListBuilder.create().texOffs(45, 77).addBox(-3.0F, -8.0F, -10.0F, 6, 7, 10),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, -0.5462881F, 0.0F, 0.0F));
      PartDefinition head_joint = neck.addOrReplaceChild("head_joint",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 1, 1, 1),
         PartPose.offsetAndRotation(0.0F, -5.2F, -10.0F, 0.91053826F, 0.0F, 0.0F));
      PartDefinition head = head_joint.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8, 9, 8),
         PartPose.ZERO);
      head.addOrReplaceChild("hair",
         CubeListBuilder.create().texOffs(34, 0).addBox(-4.5F, -6.0F, -6.1F, 9, 12, 9),
         PartPose.offset(0.0F, 1.5F, 0.5F));
      head.addOrReplaceChild("ear1",
         CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -3.0F, 0.0F, 2, 3, 1),
         PartPose.offsetAndRotation(-4.0F, -1.0F, -3.6F, 0.0F, 0.0F, -1.2747885F));
      head.addOrReplaceChild("ear2",
         CubeListBuilder.create().texOffs(30, 0).addBox(0.0F, -3.0F, 0.0F, 2, 3, 1),
         PartPose.offsetAndRotation(4.0F, -1.0F, -3.6F, 0.0F, 0.0F, 1.2747885F));
      head.addOrReplaceChild("wingL1",
         CubeListBuilder.create().texOffs(80, 0).addBox(-1.0F, -4.0F, -1.0F, 1, 4, 2),
         PartPose.offset(-4.5F, -3.0F, -2.3F));
      head.addOrReplaceChild("wingR1",
         CubeListBuilder.create().texOffs(80, 6).addBox(0.0F, -4.0F, -1.0F, 1, 4, 2),
         PartPose.offset(4.5F, -3.0F, -2.3F));
      PartDefinition crown1 = head.addOrReplaceChild("crown1",
         CubeListBuilder.create().texOffs(64, 0).addBox(-1.5F, -1.5F, -0.5F, 3, 3, 1),
         PartPose.offset(0.0F, -3.0F, -6.0F));
      crown1.addOrReplaceChild("wingL",
         CubeListBuilder.create().texOffs(73, 0).addBox(0.0F, -4.0F, 0.0F, 2, 4, 1),
         PartPose.offsetAndRotation(1.5F, 0.0F, 0.0F, 0.0F, 0.0F, -0.27314404F));
      crown1.addOrReplaceChild("wingR",
         CubeListBuilder.create().texOffs(73, 6).addBox(-2.0F, -4.0F, 0.0F, 2, 4, 1),
         PartPose.offsetAndRotation(-1.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.27314404F));
      crown1.addOrReplaceChild("shape44",
         CubeListBuilder.create().texOffs(82, 87).addBox(-3.5F, -7.0F, 0.0F, 7, 7, 1),
         PartPose.offset(0.0F, -0.9F, 0.5F));
      crown1.addOrReplaceChild("jewel",
         CubeListBuilder.create().texOffs(65, 5).addBox(-1.0F, -1.0F, -0.5F, 2, 2, 1),
         PartPose.offset(0.0F, 0.0F, -0.5F));
      PartDefinition necklace = chest.addOrReplaceChild("necklace",
         CubeListBuilder.create().texOffs(83, 63).addBox(-4.0F, 0.0F, 0.0F, 8, 14, 0),
         PartPose.offset(0.0F, -6.5F, -10.5F));
      PartDefinition bug_body = necklace.addOrReplaceChild("bug_body",
         CubeListBuilder.create().texOffs(0, 81).addBox(-2.0F, 0.0F, -1.0F, 4, 6, 2),
         PartPose.offsetAndRotation(0.0F, 0.5F, 2.9F, (float) (Math.PI / 2), 0.0F, 0.0F));
      bug_body.addOrReplaceChild("bug_legs",
         CubeListBuilder.create().texOffs(14, 80).addBox(-5.5F, -3.0F, 0.0F, 11, 12, 0),
         PartPose.ZERO);
      bug_body.addOrReplaceChild("bug_head",
         CubeListBuilder.create().texOffs(0, 91).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 1),
         PartPose.offset(0.0F, -1.0F, -0.5F));
      necklace.addOrReplaceChild("necklace1",
         CubeListBuilder.create().texOffs(83, 78).addBox(-2.5F, 0.0F, 0.0F, 5, 8, 0),
         PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, 0.3642502F, 0.0F, 0.0F));
      return LayerDefinition.create(mesh, 128, 96);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {

      pose.pushPose();
      pose.translate(this.body.x / 16.0F, this.body.y / 16.0F, this.body.z / 16.0F);
      pose.scale(1.5F, 1.5F, 1.5F);
      pose.translate(-this.body.x / 16.0F, -this.body.y / 16.0F, -this.body.z / 16.0F);
      this.body.render(pose, buf, light, overlay, r, g, b, a);
      pose.popPose();
   }
}
