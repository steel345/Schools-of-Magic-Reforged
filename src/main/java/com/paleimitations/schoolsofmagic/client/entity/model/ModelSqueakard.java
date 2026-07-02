package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelSqueakard<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "squeakard"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer thighRight;
   public final MowzieModelRenderer head;
   public final MowzieModelRenderer thighLeft;
   public final MowzieModelRenderer tail1;
   public final MowzieModelRenderer armRight;
   public final MowzieModelRenderer armLeft;
   public final MowzieModelRenderer tunic;
   public final MowzieModelRenderer legBackRight;
   public final MowzieModelRenderer shinBackRight;
   public final MowzieModelRenderer nose;
   public final MowzieModelRenderer earRight;
   public final MowzieModelRenderer earLeft;
   public final MowzieModelRenderer whiskers;
   public final MowzieModelRenderer legBackLeft;
   public final MowzieModelRenderer shinBackLeft;
   public final MowzieModelRenderer tail2;
   public final MowzieModelRenderer tail3;
   public final MowzieModelRenderer tail4;
   public final MowzieModelRenderer sleeveRight;
   public final MowzieModelRenderer sleeveLeft;

   public ModelSqueakard(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.armRight = new MowzieModelRenderer(this.body.part.getChild("armRight"));
      this.sleeveRight = new MowzieModelRenderer(this.armRight.part.getChild("sleeveRight"));
      this.thighLeft = new MowzieModelRenderer(this.body.part.getChild("thighLeft"));
      this.legBackLeft = new MowzieModelRenderer(this.thighLeft.part.getChild("legBackLeft"));
      this.shinBackLeft = new MowzieModelRenderer(this.legBackLeft.part.getChild("shinBackLeft"));
      this.tunic = new MowzieModelRenderer(this.body.part.getChild("tunic"));
      this.tail1 = new MowzieModelRenderer(this.body.part.getChild("tail1"));
      this.tail2 = new MowzieModelRenderer(this.tail1.part.getChild("tail2"));
      this.tail3 = new MowzieModelRenderer(this.tail2.part.getChild("tail3"));
      this.tail4 = new MowzieModelRenderer(this.tail3.part.getChild("tail4"));
      this.armLeft = new MowzieModelRenderer(this.body.part.getChild("armLeft"));
      this.sleeveLeft = new MowzieModelRenderer(this.armLeft.part.getChild("sleeveLeft"));
      this.thighRight = new MowzieModelRenderer(this.body.part.getChild("thighRight"));
      this.legBackRight = new MowzieModelRenderer(this.thighRight.part.getChild("legBackRight"));
      this.shinBackRight = new MowzieModelRenderer(this.legBackRight.part.getChild("shinBackRight"));
      this.head = new MowzieModelRenderer(this.body.part.getChild("head"));
      this.nose = new MowzieModelRenderer(this.head.part.getChild("nose"));
      this.whiskers = new MowzieModelRenderer(this.nose.part.getChild("whiskers"));
      this.earLeft = new MowzieModelRenderer(this.head.part.getChild("earLeft"));
      this.earRight = new MowzieModelRenderer(this.head.part.getChild("earRight"));
      this.parts.add(this.body);
      this.parts.add(this.armRight);
      this.parts.add(this.sleeveRight);
      this.parts.add(this.thighLeft);
      this.parts.add(this.legBackLeft);
      this.parts.add(this.shinBackLeft);
      this.parts.add(this.tunic);
      this.parts.add(this.tail1);
      this.parts.add(this.tail2);
      this.parts.add(this.tail3);
      this.parts.add(this.tail4);
      this.parts.add(this.armLeft);
      this.parts.add(this.sleeveLeft);
      this.parts.add(this.thighRight);
      this.parts.add(this.legBackRight);
      this.parts.add(this.shinBackRight);
      this.parts.add(this.head);
      this.parts.add(this.nose);
      this.parts.add(this.whiskers);
      this.parts.add(this.earLeft);
      this.parts.add(this.earRight);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(24, 0).addBox(-3.5F, -10.0F, -2.0F, 7, 12, 5),
         PartPose.offset(0.0F, 15.4F, 0.0F));
      PartDefinition armRight = body.addOrReplaceChild("armRight",
         CubeListBuilder.create().texOffs(55, 21).addBox(-2.0F, 0.0F, -1.0F, 2, 8, 2),
         PartPose.offsetAndRotation(-3.5F, -9.5F, 0.0F, -0.31869712F, 0.0F, 0.31869712F));
      armRight.addOrReplaceChild("sleeveRight",
         CubeListBuilder.create().texOffs(29, 37).addBox(-1.5F, -0.5F, -1.5F, 3, 8, 3),
         PartPose.offset(-1.0F, 0.0F, 0.0F));
      PartDefinition thighLeft = body.addOrReplaceChild("thighLeft",
         CubeListBuilder.create().texOffs(28, 19).addBox(0.0F, -1.0F, -3.0F, 3, 6, 5),
         PartPose.offsetAndRotation(1.5F, 1.0F, 0.5F, -0.27314404F, 0.0F, 0.0F));
      PartDefinition legBackLeft = thighLeft.addOrReplaceChild("legBackLeft",
         CubeListBuilder.create().texOffs(39, 18).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2),
         PartPose.offsetAndRotation(1.5F, 4.0F, 0.0F, 1.4114478F, 0.0F, 0.0F));
      legBackLeft.addOrReplaceChild("shinBackLeft",
         CubeListBuilder.create().texOffs(46, 23).addBox(-0.8F, 0.0F, -1.0F, 2, 5, 2),
         PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, -1.4114478F, 0.0F, 0.0F));
      body.addOrReplaceChild("tunic",
         CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, 0.0F, -3.0F, 8, 14, 6),
         PartPose.offset(0.0F, -10.5F, 0.5F));
      PartDefinition tail1 = body.addOrReplaceChild("tail1",
         CubeListBuilder.create().texOffs(0, 24).addBox(-0.5F, 0.0F, -1.0F, 1, 5, 1),
         PartPose.offsetAndRotation(0.0F, 0.7F, 3.0F, 1.0927507F, 0.0F, 0.0F));
      PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
         CubeListBuilder.create().texOffs(4, 24).addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1),
         PartPose.offsetAndRotation(0.0F, 5.0F, -0.5F, 0.22759093F, 0.0F, 0.0F));
      PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
         CubeListBuilder.create().texOffs(8, 24).addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1),
         PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.22759093F, 0.0F, 0.0F));
      tail3.addOrReplaceChild("tail4",
         CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1),
         PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.22759093F, 0.0F, 0.0F));
      PartDefinition armLeft = body.addOrReplaceChild("armLeft",
         CubeListBuilder.create().texOffs(15, 22).addBox(0.0F, 0.0F, -1.0F, 2, 8, 2),
         PartPose.offsetAndRotation(3.5F, -9.5F, 0.0F, -0.31869712F, 0.0F, -0.31869712F));
      armLeft.addOrReplaceChild("sleeveLeft",
         CubeListBuilder.create().texOffs(42, 37).addBox(-1.5F, -0.5F, -1.5F, 3, 8, 3),
         PartPose.offset(1.0F, 0.0F, 0.0F));
      PartDefinition thighRight = body.addOrReplaceChild("thighRight",
         CubeListBuilder.create().texOffs(0, 12).addBox(-3.0F, -1.0F, -3.0F, 3, 6, 5),
         PartPose.offsetAndRotation(-1.5F, 1.0F, 0.5F, -0.27314404F, 0.0F, 0.0F));
      PartDefinition legBackRight = thighRight.addOrReplaceChild("legBackRight",
         CubeListBuilder.create().texOffs(16, 12).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2),
         PartPose.offsetAndRotation(-1.5F, 4.0F, 0.0F, 1.4114478F, 0.0F, 0.0F));
      legBackRight.addOrReplaceChild("shinBackRight",
         CubeListBuilder.create().texOffs(24, 17).addBox(-1.2F, 0.0F, -1.0F, 2, 5, 2),
         PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, -1.4114478F, 0.0F, 0.0F));
      PartDefinition head = body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6),
         PartPose.offset(0.0F, -10.0F, -0.4F));
      PartDefinition nose = head.addOrReplaceChild("nose",
         CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -2.0F, -3.0F, 4, 4, 3),
         PartPose.offset(0.0F, -2.5F, -3.0F));
      nose.addOrReplaceChild("whiskers",
         CubeListBuilder.create().texOffs(32, 52).addBox(-5.5F, -1.0F, 0.0F, 11, 7, 0),
         PartPose.offset(0.0F, -2.0F, -2.5F));
      head.addOrReplaceChild("earLeft",
         CubeListBuilder.create().texOffs(48, 14).addBox(-1.0F, -6.0F, 0.0F, 6, 6, 1),
         PartPose.offsetAndRotation(3.0F, -4.2F, 0.0F, -0.13665928F, -1.0927507F, 0.22759093F));
      head.addOrReplaceChild("earRight",
         CubeListBuilder.create().texOffs(48, 7).addBox(-5.0F, -6.0F, 0.0F, 6, 6, 1),
         PartPose.offsetAndRotation(-3.0F, -4.2F, 0.0F, -0.13665928F, 1.0927507F, -0.22759093F));
      return LayerDefinition.create(mesh, 64, 64);
   }

   public void postRenderArm(PoseStack pose, HumanoidArm side) {
      pose.scale(0.8F, 0.8F, 0.8F);
      this.body.part.translateAndRotate(pose);
      MowzieModelRenderer arm = side == HumanoidArm.LEFT ? this.armLeft : this.armRight;
      arm.part.translateAndRotate(pose);
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay, float r, float g, float b, float a) {
      pose.pushPose();
      pose.scale(0.8F, 0.8F, 0.8F);
      super.renderToBuffer(pose, buf, light, overlay, r, g, b, a);
      pose.popPose();
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      this.head.part.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
      this.head.part.xRot = headPitch * ((float) Math.PI / 180.0F);
      float globalSpeed = 1.25F;
      float globalDegree = 1.0F;
      float globalOffset = 0.0F;
      this.bob(this.body, 1.125F * globalSpeed * 2.0F, 1.0F * globalDegree, false, limbSwing, limbSwingAmount);
      this.flap(this.armLeft, 0.125F * globalSpeed, 0.125F * globalDegree, false, globalOffset, 0.0F, (float) entity.tickCount, 0.5F);
      this.flap(this.armRight, 0.125F * globalSpeed, 0.125F * globalDegree, true, globalOffset, 0.0F, (float) entity.tickCount, 0.5F);
      this.walk(this.armLeft, 1.125F * globalSpeed, 1.0F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.armRight, 1.125F * globalSpeed, 1.0F * globalDegree, false, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.thighLeft, 1.125F * globalSpeed, 1.0F * globalDegree, false, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.thighRight, 1.125F * globalSpeed, 1.0F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.legBackLeft, 1.125F * globalSpeed, 0.75F * globalDegree, false, (float) (Math.PI / 2) + globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.legBackRight, 1.125F * globalSpeed, 0.75F * globalDegree, true, (float) (Math.PI / 2) + globalOffset, 0.0F, limbSwing, limbSwingAmount);
   }
}
