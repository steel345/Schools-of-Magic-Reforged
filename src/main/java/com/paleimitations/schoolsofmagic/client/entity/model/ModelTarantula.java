package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import org.joml.Vector3f;

import com.paleimitations.schoolsofmagic.common.entity.EntityTarantula;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelTarantula<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "tarantula"), "main");

   public final MowzieModelRenderer body, abdomen, head, connector, maw1, maw2;
   public final MowzieModelRenderer eye1, eye2, eye3, eye4, eye5, eye6, eye7, eye8;
   public final MowzieModelRenderer leg1L1, leg1L2, leg1L3, leg1L4, leg1L5, leg1L6;
   public final MowzieModelRenderer leg1R1, leg1R2, leg1R3, leg1R4, leg1R5, leg1R6;
   public final MowzieModelRenderer leg2L1, leg2L2, leg2L3, leg2L4, leg2L5, leg2L6;
   public final MowzieModelRenderer leg2R1, leg2R2, leg2R3, leg2R4, leg2R5, leg2R6;
   public final MowzieModelRenderer leg3L1, leg3L2, leg3L3, leg3L4, leg3L5, leg3L6;
   public final MowzieModelRenderer leg3R1, leg3R2, leg3R3, leg3R4, leg3R5, leg3R6;
   public final MowzieModelRenderer leg4L1, leg4L2, leg4L3, leg4L4, leg4L5, leg4L6;
   public final MowzieModelRenderer leg4R1, leg4R2, leg4R3, leg4R4, leg4R5, leg4R6;

   public ModelTarantula(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.abdomen = new MowzieModelRenderer(this.body.part.getChild("abdomen"));
      this.connector = new MowzieModelRenderer(this.body.part.getChild("connector"));
      this.maw1 = new MowzieModelRenderer(this.body.part.getChild("maw1"));
      this.maw2 = new MowzieModelRenderer(this.body.part.getChild("maw2"));
      this.head = new MowzieModelRenderer(this.body.part.getChild("head"));
      this.eye1 = new MowzieModelRenderer(this.head.part.getChild("eye1"));
      this.eye2 = new MowzieModelRenderer(this.head.part.getChild("eye2"));
      this.eye3 = new MowzieModelRenderer(this.head.part.getChild("eye3"));
      this.eye4 = new MowzieModelRenderer(this.head.part.getChild("eye4"));
      this.eye5 = new MowzieModelRenderer(this.head.part.getChild("eye5"));
      this.eye6 = new MowzieModelRenderer(this.head.part.getChild("eye6"));
      this.eye7 = new MowzieModelRenderer(this.head.part.getChild("eye7"));
      this.eye8 = new MowzieModelRenderer(this.head.part.getChild("eye8"));

      this.leg1L1 = new MowzieModelRenderer(this.body.part.getChild("leg1L1"));
      this.leg1L2 = new MowzieModelRenderer(this.leg1L1.part.getChild("leg1L2"));
      this.leg1L3 = new MowzieModelRenderer(this.leg1L2.part.getChild("leg1L3"));
      this.leg1L4 = new MowzieModelRenderer(this.leg1L3.part.getChild("leg1L4"));
      this.leg1L5 = new MowzieModelRenderer(this.leg1L4.part.getChild("leg1L5"));
      this.leg1L6 = new MowzieModelRenderer(this.leg1L5.part.getChild("leg1L6"));
      this.leg1R1 = new MowzieModelRenderer(this.body.part.getChild("leg1R1"));
      this.leg1R2 = new MowzieModelRenderer(this.leg1R1.part.getChild("leg1R2"));
      this.leg1R3 = new MowzieModelRenderer(this.leg1R2.part.getChild("leg1R3"));
      this.leg1R4 = new MowzieModelRenderer(this.leg1R3.part.getChild("leg1R4"));
      this.leg1R5 = new MowzieModelRenderer(this.leg1R4.part.getChild("leg1R5"));
      this.leg1R6 = new MowzieModelRenderer(this.leg1R5.part.getChild("leg1R6"));
      this.leg2L1 = new MowzieModelRenderer(this.body.part.getChild("leg2L1"));
      this.leg2L2 = new MowzieModelRenderer(this.leg2L1.part.getChild("leg2L2"));
      this.leg2L3 = new MowzieModelRenderer(this.leg2L2.part.getChild("leg2L3"));
      this.leg2L4 = new MowzieModelRenderer(this.leg2L3.part.getChild("leg2L4"));
      this.leg2L5 = new MowzieModelRenderer(this.leg2L4.part.getChild("leg2L5"));
      this.leg2L6 = new MowzieModelRenderer(this.leg2L5.part.getChild("leg2L6"));
      this.leg2R1 = new MowzieModelRenderer(this.body.part.getChild("leg2R1"));
      this.leg2R2 = new MowzieModelRenderer(this.leg2R1.part.getChild("leg2R2"));
      this.leg2R3 = new MowzieModelRenderer(this.leg2R2.part.getChild("leg2R3"));
      this.leg2R4 = new MowzieModelRenderer(this.leg2R3.part.getChild("leg2R4"));
      this.leg2R5 = new MowzieModelRenderer(this.leg2R4.part.getChild("leg2R5"));
      this.leg2R6 = new MowzieModelRenderer(this.leg2R5.part.getChild("leg2R6"));
      this.leg3L1 = new MowzieModelRenderer(this.body.part.getChild("leg3L1"));
      this.leg3L2 = new MowzieModelRenderer(this.leg3L1.part.getChild("leg3L2"));
      this.leg3L3 = new MowzieModelRenderer(this.leg3L2.part.getChild("leg3L3"));
      this.leg3L4 = new MowzieModelRenderer(this.leg3L3.part.getChild("leg3L4"));
      this.leg3L5 = new MowzieModelRenderer(this.leg3L4.part.getChild("leg3L5"));
      this.leg3L6 = new MowzieModelRenderer(this.leg3L5.part.getChild("leg3L6"));
      this.leg3R1 = new MowzieModelRenderer(this.body.part.getChild("leg3R1"));
      this.leg3R2 = new MowzieModelRenderer(this.leg3R1.part.getChild("leg3R2"));
      this.leg3R3 = new MowzieModelRenderer(this.leg3R2.part.getChild("leg3R3"));
      this.leg3R4 = new MowzieModelRenderer(this.leg3R3.part.getChild("leg3R4"));
      this.leg3R5 = new MowzieModelRenderer(this.leg3R4.part.getChild("leg3R5"));
      this.leg3R6 = new MowzieModelRenderer(this.leg3R5.part.getChild("leg3R6"));
      this.leg4L1 = new MowzieModelRenderer(this.body.part.getChild("leg4L1"));
      this.leg4L2 = new MowzieModelRenderer(this.leg4L1.part.getChild("leg4L2"));
      this.leg4L3 = new MowzieModelRenderer(this.leg4L2.part.getChild("leg4L3"));
      this.leg4L4 = new MowzieModelRenderer(this.leg4L3.part.getChild("leg4L4"));
      this.leg4L5 = new MowzieModelRenderer(this.leg4L4.part.getChild("leg4L5"));
      this.leg4L6 = new MowzieModelRenderer(this.leg4L5.part.getChild("leg4L6"));
      this.leg4R1 = new MowzieModelRenderer(this.body.part.getChild("leg4R1"));
      this.leg4R2 = new MowzieModelRenderer(this.leg4R1.part.getChild("leg4R2"));
      this.leg4R3 = new MowzieModelRenderer(this.leg4R2.part.getChild("leg4R3"));
      this.leg4R4 = new MowzieModelRenderer(this.leg4R3.part.getChild("leg4R4"));
      this.leg4R5 = new MowzieModelRenderer(this.leg4R4.part.getChild("leg4R5"));
      this.leg4R6 = new MowzieModelRenderer(this.leg4R5.part.getChild("leg4R6"));
      this.parts.add(this.body); this.parts.add(this.abdomen); this.parts.add(this.head);
      this.parts.add(this.connector); this.parts.add(this.maw1); this.parts.add(this.maw2);
      this.parts.add(this.eye1); this.parts.add(this.eye2); this.parts.add(this.eye3); this.parts.add(this.eye4);
      this.parts.add(this.eye5); this.parts.add(this.eye6); this.parts.add(this.eye7); this.parts.add(this.eye8);
      this.parts.add(this.leg1L1); this.parts.add(this.leg1L2); this.parts.add(this.leg1L3); this.parts.add(this.leg1L4); this.parts.add(this.leg1L5); this.parts.add(this.leg1L6);
      this.parts.add(this.leg1R1); this.parts.add(this.leg1R2); this.parts.add(this.leg1R3); this.parts.add(this.leg1R4); this.parts.add(this.leg1R5); this.parts.add(this.leg1R6);
      this.parts.add(this.leg2L1); this.parts.add(this.leg2L2); this.parts.add(this.leg2L3); this.parts.add(this.leg2L4); this.parts.add(this.leg2L5); this.parts.add(this.leg2L6);
      this.parts.add(this.leg2R1); this.parts.add(this.leg2R2); this.parts.add(this.leg2R3); this.parts.add(this.leg2R4); this.parts.add(this.leg2R5); this.parts.add(this.leg2R6);
      this.parts.add(this.leg3L1); this.parts.add(this.leg3L2); this.parts.add(this.leg3L3); this.parts.add(this.leg3L4); this.parts.add(this.leg3L5); this.parts.add(this.leg3L6);
      this.parts.add(this.leg3R1); this.parts.add(this.leg3R2); this.parts.add(this.leg3R3); this.parts.add(this.leg3R4); this.parts.add(this.leg3R5); this.parts.add(this.leg3R6);
      this.parts.add(this.leg4L1); this.parts.add(this.leg4L2); this.parts.add(this.leg4L3); this.parts.add(this.leg4L4); this.parts.add(this.leg4L5); this.parts.add(this.leg4L6);
      this.parts.add(this.leg4R1); this.parts.add(this.leg4R2); this.parts.add(this.leg4R3); this.parts.add(this.leg4R4); this.parts.add(this.leg4R5); this.parts.add(this.leg4R6);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {

      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 45).addBox(-6.5F, -4.0F, -13.0F, 13, 6, 15),
         PartPose.offsetAndRotation(0.0F, 16.0F, 3.0F, 0.2268928F, 0.0F, 0.0F));
      body.addOrReplaceChild("abdomen",
         CubeListBuilder.create().texOffs(0, 66).addBox(-7.5F, -6.0F, 0.0F, 15, 12, 18),
         PartPose.offsetAndRotation(0.0F, -2.0F, 3.0F, (float) (Math.PI / 9), 0.0F, 0.0F));
      body.addOrReplaceChild("connector",
         CubeListBuilder.create().texOffs(0, 25).addBox(-4.5F, -3.0F, 0.0F, 9, 7, 13),
         PartPose.offset(0.0F, -2.5F, -8.0F));
      body.addOrReplaceChild("maw1",
         CubeListBuilder.create().texOffs(34, 11).addBox(-2.5F, -1.5F, -3.0F, 5, 6, 3),
         PartPose.offset(2.8F, -1.5F, -12.0F));
      body.addOrReplaceChild("maw2",
         CubeListBuilder.create().texOffs(34, 11).addBox(-2.5F, -1.5F, -3.0F, 5, 6, 3),
         PartPose.offset(-2.8F, -1.5F, -12.0F));
      PartDefinition head = body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 10).addBox(-5.5F, -3.0F, -8.0F, 11, 4, 11),
         PartPose.offsetAndRotation(0.0F, -1.0F, -4.5F, -0.5009095F, 0.0F, 0.0F));
      head.addOrReplaceChild("eye1", CubeListBuilder.create().texOffs(36, 4).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3), PartPose.offset(2.0F, -0.1F, -7.5F));
      head.addOrReplaceChild("eye2", CubeListBuilder.create().texOffs(36, 4).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3), PartPose.offset(-2.0F, -0.1F, -7.5F));
      head.addOrReplaceChild("eye3", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2), PartPose.offset(-5.0F, 0.1F, -7.5F));
      head.addOrReplaceChild("eye4", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2), PartPose.offset(5.0F, 0.1F, -7.5F));
      head.addOrReplaceChild("eye5", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(4.5F, -2.0F, -7.8F));
      head.addOrReplaceChild("eye6", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(-4.5F, -2.0F, -7.8F));
      head.addOrReplaceChild("eye7", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(-3.0F, -2.7F, -7.7F));
      head.addOrReplaceChild("eye8", CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(3.0F, -2.7F, -7.7F));

      ModelPetSpiderLegHelper.addLegL(body, "leg1L", 6.8F, -0.5F, -10.0F, 0.4098033F, -0.5462881F, 0.27314404F, -0.95609134F, 0.7285004F);
      ModelPetSpiderLegHelper.addLegR(body, "leg1R", -6.8F, -0.5F, -10.0F, -0.4098033F, -0.5462881F, -0.27314404F, 0.95609134F, -0.7285004F);
      ModelPetSpiderLegHelper.addLegL(body, "leg2L", 7.8F, -1.5F, -6.6F, 0.13962634F, -0.31869712F, 0.045553092F, -0.91053826F, 0.91053826F);
      ModelPetSpiderLegHelper.addLegR(body, "leg2R", -7.8F, -1.5F, -6.6F, -0.13962634F, -0.31869712F, -0.045553092F, 0.91053826F, -0.91053826F);
      ModelPetSpiderLegHelper.addLegL(body, "leg3L", 7.8F, -1.0F, -3.0F, -0.13962634F, -0.18203785F, -0.18203785F, -0.7740535F, 0.87266463F);
      ModelPetSpiderLegHelper.addLegR(body, "leg3R", -7.8F, -1.0F, -3.0F, 0.13962634F, -0.18203785F, 0.18203785F, 0.7740535F, -0.87266463F);
      ModelPetSpiderLegHelper.addLegL(body, "leg4L", 7.0F, -0.5F, 0.4F, -0.4098033F, 0.0F, -0.3642502F, -0.5009095F, 0.7285004F);
      ModelPetSpiderLegHelper.addLegR(body, "leg4R", -7.0F, -0.5F, 0.4F, 0.4098033F, 0.0F, 0.3642502F, 0.5009095F, -0.7285004F);

      return LayerDefinition.create(mesh, 80, 96);
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {

      pose.pushPose();
      pose.translate(this.body.part.x / 16.0F, this.body.part.y / 16.0F, this.body.part.z / 16.0F);
      pose.scale(2.0F, 2.0F, 2.0F);
      pose.translate(-this.body.part.x / 16.0F, -this.body.part.y / 16.0F, -this.body.part.z / 16.0F);
      this.body.part.render(pose, buf, light, overlay, r, g, b, a);
      pose.popPose();
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      if (!(entity instanceof EntityTarantula tar)) return;
      float globalSpeed = 0.25F;
      float globalDegree = 1.125F;
      int countDown = tar.getAttackSwitchTick();

      switch (tar.getAttackType()) {
         case BITE -> doFront4LegsCycle(limbSwing, limbSwingAmount, globalSpeed, globalDegree, true);
         case MELEE -> {
            this.rear(300 - countDown, countDown);
            doFront4LegsCycle((float) entity.tickCount, 0.5F, globalSpeed, globalDegree * 0.5F, true);
         }
         case POUNCE -> {
            this.jump(tar.getJumpTick());
            doFront4LegsCycle(limbSwing, limbSwingAmount, globalSpeed, globalDegree, true);
         }
         case RANGED -> {
            this.shootWeb(300 - countDown, countDown);
            doFront4LegsCycle(limbSwing, limbSwingAmount, globalSpeed, globalDegree, true);
         }
      }

      this.flap(this.leg3L2, 1.0F * globalSpeed, 0.25F * globalDegree, false, (float) Math.PI, -0.25F * globalDegree, limbSwing, limbSwingAmount);
      this.flap(this.leg4L2, 1.0F * globalSpeed, 0.25F * globalDegree, false, (float) (Math.PI * 3.0 / 2.0), -0.25F * globalDegree, limbSwing, limbSwingAmount);
      this.flap(this.leg3R2, 1.0F * globalSpeed, 0.25F * globalDegree, true, 0.0F, 0.25F * globalDegree, limbSwing, limbSwingAmount);
      this.flap(this.leg4R2, 1.0F * globalSpeed, 0.25F * globalDegree, true, (float) (Math.PI / 2), 0.25F * globalDegree, limbSwing, limbSwingAmount);
      this.swing(this.leg3L2, 1.0F * globalSpeed, 0.25F * globalDegree, false, (float) (Math.PI * 3.0 / 2.0), 0.0F, limbSwing, limbSwingAmount);
      this.swing(this.leg4L2, 1.0F * globalSpeed, 0.25F * globalDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.swing(this.leg3R2, 1.0F * globalSpeed, 0.25F * globalDegree, true, (float) (Math.PI / 2), 0.0F, limbSwing, limbSwingAmount);
      this.swing(this.leg4R2, 1.0F * globalSpeed, 0.25F * globalDegree, true, (float) Math.PI, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.leg3L2, 1.0F * globalSpeed, 0.25F * globalDegree, false, (float) Math.PI, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.leg4L2, 1.0F * globalSpeed, 0.25F * globalDegree, false, (float) (Math.PI * 3.0 / 2.0), 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.leg3R2, 1.0F * globalSpeed, 0.25F * globalDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.leg4R2, 1.0F * globalSpeed, 0.25F * globalDegree, false, (float) (Math.PI / 2), 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.abdomen, 0.5F * globalSpeed, 0.125F * globalDegree, false, 0.0F, 0.0F, (float) entity.tickCount, 0.5F);
      this.walk(this.maw1, 1.0F * globalSpeed, 0.125F * globalDegree, false, 0.0F, 0.0F, (float) entity.tickCount, 0.5F);
      this.walk(this.maw2, 1.0F * globalSpeed, 0.125F * globalDegree, false, (float) (Math.PI / 2), 0.0F, (float) entity.tickCount, 0.5F);
   }

   private void doFront4LegsCycle(float f, float f1, float gs, float gd, boolean withSwing) {
      this.flap(this.leg1L2, 1.0F * gs, 0.25F * gd, false, 0.0F, -0.25F * gd, f, f1);
      this.flap(this.leg2L2, 1.0F * gs, 0.25F * gd, false, (float) (Math.PI / 2), -0.25F * gd, f, f1);
      this.flap(this.leg1R2, 1.0F * gs, 0.25F * gd, true, (float) Math.PI, 0.25F * gd, f, f1);
      this.flap(this.leg2R2, 1.0F * gs, 0.25F * gd, true, (float) (Math.PI * 3.0 / 2.0), 0.25F * gd, f, f1);
      if (withSwing) {
         this.swing(this.leg1L2, 1.0F * gs, 0.25F * gd, false, (float) (Math.PI / 2), 0.0F, f, f1);
         this.swing(this.leg2L2, 1.0F * gs, 0.25F * gd, false, (float) Math.PI, 0.0F, f, f1);
         this.swing(this.leg1R2, 1.0F * gs, 0.25F * gd, true, (float) (Math.PI * 3.0 / 2.0), 0.0F, f, f1);
         this.swing(this.leg2R2, 1.0F * gs, 0.25F * gd, true, 0.0F, 0.0F, f, f1);
      }
      this.walk(this.leg1L2, 1.0F * gs, 0.25F * gd, false, 0.0F, -0.25F * gd, f, f1);
      this.walk(this.leg2L2, 1.0F * gs, 0.25F * gd, false, (float) (Math.PI / 2), 0.0F, f, f1);
      this.walk(this.leg1R2, 1.0F * gs, 0.25F * gd, false, (float) Math.PI, 0.0F, f, f1);
      this.walk(this.leg2R2, 1.0F * gs, 0.25F * gd, false, (float) (Math.PI * 3.0 / 2.0), 0.0F, f, f1);
   }

   public void jump(int tickUp) {
      int tick = Math.min(tickUp, 10);
      Vector3f bodyAn = this.body.animateAnglesToLinear(this.body.getInitAngles(), new Vector3f((float) Math.toRadians(7.0), 0.0F, 0.0F), tick, 10);
      Vector3f bodyPos = this.body.animatePointsToLinear(this.body.getInitRotPoint(), new Vector3f(0.0F, 2.5F, 0.0F), tick, 10);
      Vector3f abdomenAn = this.abdomen.animateAnglesToLinear(this.abdomen.getInitAngles(), new Vector3f((float) Math.toRadians(16.0), 0.0F, 0.0F), tick, 10);
      Vector3f leg1L2An = this.leg1L2.animateAnglesToLinear(this.leg1L2.getInitAngles(), new Vector3f((float) Math.toRadians(2.6), (float) Math.toRadians(-15.65), (float) Math.toRadians(-18.26)), tick, 10);
      Vector3f leg1R2An = this.leg1R2.animateAnglesToLinear(this.leg1R2.getInitAngles(), new Vector3f((float) Math.toRadians(2.6), (float) Math.toRadians(15.65), (float) Math.toRadians(18.26)), tick, 10);
      Vector3f leg1L4An = this.leg1L4.animateAnglesToLinear(this.leg1L4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(18.26)), tick, 10);
      Vector3f leg1R4An = this.leg1R4.animateAnglesToLinear(this.leg1R4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-18.26)), tick, 10);
      Vector3f leg2L2An = this.leg2L2.animateAnglesToLinear(this.leg2L2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(-10.44), (float) Math.toRadians(-7.83)), tick, 10);
      Vector3f leg2R2An = this.leg2R2.animateAnglesToLinear(this.leg2R2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(10.44), (float) Math.toRadians(7.83)), tick, 10);
      Vector3f leg2L4An = this.leg2L4.animateAnglesToLinear(this.leg2L4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(2.61)), tick, 10);
      Vector3f leg2R4An = this.leg2R4.animateAnglesToLinear(this.leg2R4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-2.61)), tick, 10);
      Vector3f leg3L2An = this.leg3L2.animateAnglesToLinear(this.leg3L2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(-2.61), (float) Math.toRadians(-1.65)), tick, 10);
      Vector3f leg3R2An = this.leg3R2.animateAnglesToLinear(this.leg3R2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(2.61), (float) Math.toRadians(1.65)), tick, 10);
      Vector3f leg4L2An = this.leg4L2.animateAnglesToLinear(this.leg4L2.getInitAngles(), new Vector3f((float) Math.toRadians(-5.22), 0.0F, (float) Math.toRadians(2.61)), tick, 10);
      Vector3f leg4R2An = this.leg4R2.animateAnglesToLinear(this.leg4R2.getInitAngles(), new Vector3f((float) Math.toRadians(-5.22), 0.0F, (float) Math.toRadians(-2.61)), tick, 10);
      if (tickUp >= 20) {
         int time = 5;
         tick = Math.min(tickUp - 20, time);
         this.body.animatePointsToLinear(bodyPos, new Vector3f(0.0F, -37.5F, 0.0F), tick, time);
         this.leg1L2.animateAnglesToLinear(leg1L2An, new Vector3f((float) Math.toRadians(15.66), (float) Math.toRadians(15.65), (float) Math.toRadians(70.43)), tick, time);
         this.leg1R2.animateAnglesToLinear(leg1R2An, new Vector3f((float) Math.toRadians(15.66), (float) Math.toRadians(-15.65), (float) Math.toRadians(-70.43)), tick, time);
         this.leg1L4.animateAnglesToLinear(leg1L4An, new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-5.22)), tick, time);
         this.leg1R4.animateAnglesToLinear(leg1R4An, new Vector3f(0.0F, 0.0F, (float) Math.toRadians(5.22)), tick, time);
         this.leg1L6.animateAnglesToLinear(this.leg1L6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-65.22)), tick, time);
         this.leg1R6.animateAnglesToLinear(this.leg1R6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(65.22)), tick, time);
         this.leg2L2.animateAnglesToLinear(leg2L2An, new Vector3f((float) Math.toRadians(10.44), (float) Math.toRadians(7.83), (float) Math.toRadians(65.22)), tick, time);
         this.leg2R2.animateAnglesToLinear(leg2R2An, new Vector3f((float) Math.toRadians(10.44), (float) Math.toRadians(-7.83), (float) Math.toRadians(-65.22)), tick, time);
         this.leg2L4.animateAnglesToLinear(leg2L4An, new Vector3f(0.0F, 0.0F, 0.0F), tick, time);
         this.leg2R4.animateAnglesToLinear(leg2R4An, new Vector3f(0.0F, 0.0F, 0.0F), tick, time);
         this.leg2L6.animateAnglesToLinear(this.leg2L6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-64.09)), tick, time);
         this.leg2R6.animateAnglesToLinear(this.leg2R6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(64.09)), tick, time);
         this.leg3L2.animateAnglesToLinear(leg3L2An, new Vector3f(0.0F, 0.0F, (float) Math.toRadians(56.43)), tick, time);
         this.leg3R2.animateAnglesToLinear(leg3R2An, new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-56.43)), tick, time);
         this.leg3L4.animateAnglesToLinear(this.leg3L4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(2.17)), tick, time);
         this.leg3R4.animateAnglesToLinear(this.leg3R4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-2.17)), tick, time);
         this.leg3L6.animateAnglesToLinear(this.leg3L6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-60.87)), tick, time);
         this.leg3R6.animateAnglesToLinear(this.leg3R6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(60.87)), tick, time);
         this.leg4L2.animateAnglesToLinear(leg4L2An, new Vector3f((float) Math.toRadians(-10.43), (float) Math.toRadians(7.83), (float) Math.toRadians(46.96)), tick, time);
         this.leg4R2.animateAnglesToLinear(leg4R2An, new Vector3f((float) Math.toRadians(-10.43), (float) Math.toRadians(-7.83), (float) Math.toRadians(-46.96)), tick, time);
         this.leg4L4.animateAnglesToLinear(this.leg4L4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(5.22)), tick, time);
         this.leg4R4.animateAnglesToLinear(this.leg4R4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-5.22)), tick, time);
         this.leg4L6.animateAnglesToLinear(this.leg4L6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-60.0)), tick, time);
         this.leg4R6.animateAnglesToLinear(this.leg4R6.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(60.0)), tick, time);
      }
      if (tickUp >= 21) {
         this.abdomen.animateAnglesToLinear(abdomenAn, new Vector3f((float) Math.toRadians(-49.04), 0.0F, 0.0F), Math.min(tickUp - 21, 5), 5);
         this.body.animateAnglesToLinear(bodyAn, new Vector3f((float) Math.toRadians(-7.0), 0.0F, 0.0F), Math.min(tickUp - 21, 4), 4);
      }
      if (tickUp >= 28) {
         int time = 6;
         tick = Math.min(tickUp - 28, time);
         MowzieModelRenderer[] all = {
            body, abdomen, leg1L2, leg1R2, leg1L4, leg1R4, leg1L6, leg1R6,
            leg2L2, leg2R2, leg2L4, leg2R4, leg2L6, leg2R6,
            leg3L2, leg3R2, leg3L4, leg3R4, leg3L6, leg3R6,
            leg4L2, leg4R2, leg4L4, leg4R4, leg4L6, leg4R6
         };
         for (MowzieModelRenderer p : all) p.animateToInit(tick, time);
      }
   }

   public void shootWeb(int tickUp, int tickDown) {
      this.lower(tickUp, tickDown);
   }

   public void lower(int tickUp, int tickDown) {
      int time = 10;
      int tick = Math.min(tickUp, time);
      this.body.animateAnglesToLinear(this.body.getInitAngles(), new Vector3f((float) Math.toRadians(7.0), 0.0F, 0.0F), tick, time);
      this.body.animatePointsToLinear(this.body.getInitRotPoint(), new Vector3f(0.0F, 2.5F, 0.0F), tick, time);
      this.abdomen.animateAnglesToLinear(this.abdomen.getInitAngles(), new Vector3f((float) Math.toRadians(16.0), 0.0F, 0.0F), tick, time);
      this.leg1L2.animateAnglesToLinear(this.leg1L2.getInitAngles(), new Vector3f((float) Math.toRadians(2.6), (float) Math.toRadians(-15.65), (float) Math.toRadians(-18.26)), tick, time);
      this.leg1R2.animateAnglesToLinear(this.leg1R2.getInitAngles(), new Vector3f((float) Math.toRadians(2.6), (float) Math.toRadians(15.65), (float) Math.toRadians(18.26)), tick, time);
      this.leg1L4.animateAnglesToLinear(this.leg1L4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(18.26)), tick, time);
      this.leg1R4.animateAnglesToLinear(this.leg1R4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-18.26)), tick, time);
      this.leg2L2.animateAnglesToLinear(this.leg2L2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(-10.44), (float) Math.toRadians(-7.83)), tick, time);
      this.leg2R2.animateAnglesToLinear(this.leg2R2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(10.44), (float) Math.toRadians(7.83)), tick, time);
      this.leg2L4.animateAnglesToLinear(this.leg2L4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(2.61)), tick, time);
      this.leg2R4.animateAnglesToLinear(this.leg2R4.getInitAngles(), new Vector3f(0.0F, 0.0F, (float) Math.toRadians(-2.61)), tick, time);
      this.leg3L2.animateAnglesToLinear(this.leg3L2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(-2.61), (float) Math.toRadians(-1.65)), tick, time);
      this.leg3R2.animateAnglesToLinear(this.leg3R2.getInitAngles(), new Vector3f((float) Math.toRadians(-2.61), (float) Math.toRadians(2.61), (float) Math.toRadians(1.65)), tick, time);
      this.leg4L2.animateAnglesToLinear(this.leg4L2.getInitAngles(), new Vector3f((float) Math.toRadians(-5.22), 0.0F, (float) Math.toRadians(2.61)), tick, time);
      this.leg4R2.animateAnglesToLinear(this.leg4R2.getInitAngles(), new Vector3f((float) Math.toRadians(-5.22), 0.0F, (float) Math.toRadians(-2.61)), tick, time);
   }

   public void rear(int tickUp, int tickDown) {

   }

   private static final class ModelPetSpiderLegHelper {
      static void addLegL(PartDefinition parent, String prefix, float ox, float oy, float oz,
                          float yRot1, float xRot2, float yRot2, float zRot2, float zRot4) {
         PartDefinition s1 = parent.addOrReplaceChild(prefix + "1",
            CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4),
            PartPose.offsetAndRotation(ox, oy, oz, 0.0F, yRot1, 0.0F));
         PartDefinition s2 = s1.addOrReplaceChild(prefix + "2",
            CubeListBuilder.create().texOffs(45, 24).addBox(0.0F, -2.0F, -2.0F, 12, 4, 4),
            PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, xRot2, yRot2, zRot2));
         PartDefinition s3 = s2.addOrReplaceChild(prefix + "3",
            CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
            PartPose.offsetAndRotation(12.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.31869712F));
         PartDefinition s4 = s3.addOrReplaceChild(prefix + "4",
            CubeListBuilder.create().texOffs(45, 40).addBox(0.0F, -2.0F, -2.0F, 12, 4, 4),
            PartPose.offsetAndRotation(0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
         PartDefinition s5 = s4.addOrReplaceChild(prefix + "5",
            CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
            PartPose.offsetAndRotation(12.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.31869712F));
         boolean longTip = prefix.startsWith("leg2") || prefix.startsWith("leg3");
         if (longTip) {
            s5.addOrReplaceChild(prefix + "6",
               CubeListBuilder.create().texOffs(44, 48).addBox(0.0F, -2.0F, -2.0F, 14, 4, 4),
               PartPose.offsetAndRotation(0.5F, -0.2F, 0.1F, 0.0F, 0.0F, prefix.equals("leg2L") ? 0.6632251F : (float) (Math.PI * 2.0 / 9.0)));
         } else {
            s5.addOrReplaceChild(prefix + "6",
               CubeListBuilder.create().texOffs(45, 40).addBox(0.0F, -2.0F, -2.0F, 12, 4, 4),
               PartPose.offsetAndRotation(0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
         }
      }
      static void addLegR(PartDefinition parent, String prefix, float ox, float oy, float oz,
                          float yRot1, float xRot2, float yRot2, float zRot2, float zRot4) {
         PartDefinition s1 = parent.addOrReplaceChild(prefix + "1",
            CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4),
            PartPose.offsetAndRotation(ox, oy, oz, 0.0F, yRot1, 0.0F));
         PartDefinition s2 = s1.addOrReplaceChild(prefix + "2",
            CubeListBuilder.create().texOffs(45, 32).addBox(-12.0F, -2.0F, -2.0F, 12, 4, 4),
            PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, xRot2, yRot2, zRot2));
         PartDefinition s3 = s2.addOrReplaceChild(prefix + "3",
            CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
            PartPose.offsetAndRotation(-12.5F, 0.5F, 0.0F, 0.0F, 0.0F, -0.31869712F));
         PartDefinition s4 = s3.addOrReplaceChild(prefix + "4",
            CubeListBuilder.create().texOffs(45, 40).addBox(-12.0F, -2.0F, -2.0F, 12, 4, 4),
            PartPose.offsetAndRotation(-0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
         PartDefinition s5 = s4.addOrReplaceChild(prefix + "5",
            CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
            PartPose.offsetAndRotation(-12.5F, 0.5F, 0.0F, 0.0F, 0.0F, -0.31869712F));
         boolean longTip = prefix.startsWith("leg2") || prefix.startsWith("leg3");
         if (longTip) {
            s5.addOrReplaceChild(prefix + "6",
               CubeListBuilder.create().texOffs(44, 48).addBox(-14.0F, -2.0F, -2.0F, 14, 4, 4),
               PartPose.offsetAndRotation(-0.5F, -0.2F, 0.1F, 0.0F, 0.0F, prefix.equals("leg2R") ? -0.6632251F : (float) (-Math.PI * 2.0 / 9.0)));
         } else {
            s5.addOrReplaceChild(prefix + "6",
               CubeListBuilder.create().texOffs(45, 40).addBox(-12.0F, -2.0F, -2.0F, 12, 4, 4),
               PartPose.offsetAndRotation(-0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
         }
      }
   }
}
