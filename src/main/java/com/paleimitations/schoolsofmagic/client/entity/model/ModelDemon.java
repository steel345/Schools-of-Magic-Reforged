package com.paleimitations.schoolsofmagic.client.entity.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelDemon<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "demon"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer pelvis;
   public final MowzieModelRenderer chest;
   public final MowzieModelRenderer thigh_r;
   public final MowzieModelRenderer thigh_l;
   public final MowzieModelRenderer leg_r1;
   public final MowzieModelRenderer leg_r2;
   public final MowzieModelRenderer leg_l1;
   public final MowzieModelRenderer leg_l2;
   public final MowzieModelRenderer shoulder_r;
   public final MowzieModelRenderer shoulder_l;
   public final MowzieModelRenderer neck;
   public final MowzieModelRenderer arm_r1;
   public final MowzieModelRenderer arm_r2;
   public final MowzieModelRenderer arm_l1;
   public final MowzieModelRenderer arm_l2;
   public final MowzieModelRenderer head;
   public final MowzieModelRenderer nose;
   public final MowzieModelRenderer horn_base;
   public final MowzieModelRenderer crown;
   public final MowzieModelRenderer nose_ring;
   public final MowzieModelRenderer horn;
   public final MowzieModelRenderer horn_r;
   public final MowzieModelRenderer horn_l;
   public final MowzieModelRenderer jewlery;

   public ModelDemon(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.chest = new MowzieModelRenderer(this.body.part.getChild("chest"));
      this.shoulder_l = new MowzieModelRenderer(this.chest.part.getChild("shoulder_l"));
      this.arm_l1 = new MowzieModelRenderer(this.shoulder_l.part.getChild("arm_l1"));
      this.arm_l2 = new MowzieModelRenderer(this.arm_l1.part.getChild("arm_l2"));
      this.shoulder_r = new MowzieModelRenderer(this.chest.part.getChild("shoulder_r"));
      this.arm_r1 = new MowzieModelRenderer(this.shoulder_r.part.getChild("arm_r1"));
      this.arm_r2 = new MowzieModelRenderer(this.arm_r1.part.getChild("arm_r2"));
      this.neck = new MowzieModelRenderer(this.chest.part.getChild("neck"));
      this.head = new MowzieModelRenderer(this.neck.part.getChild("head"));
      this.horn_base = new MowzieModelRenderer(this.head.part.getChild("horn_base"));
      this.horn = new MowzieModelRenderer(this.horn_base.part.getChild("horn"));
      this.horn_l = new MowzieModelRenderer(this.horn.part.getChild("horn_l"));
      this.horn_r = new MowzieModelRenderer(this.horn.part.getChild("horn_r"));
      this.jewlery = new MowzieModelRenderer(this.horn.part.getChild("jewlery"));
      this.crown = new MowzieModelRenderer(this.head.part.getChild("crown"));
      this.nose = new MowzieModelRenderer(this.head.part.getChild("nose"));
      this.nose_ring = new MowzieModelRenderer(this.nose.part.getChild("nose_ring"));
      this.pelvis = new MowzieModelRenderer(this.body.part.getChild("pelvis"));
      this.thigh_l = new MowzieModelRenderer(this.pelvis.part.getChild("thigh_l"));
      this.leg_l1 = new MowzieModelRenderer(this.thigh_l.part.getChild("leg_l1"));
      this.leg_l2 = new MowzieModelRenderer(this.leg_l1.part.getChild("leg_l2"));
      this.thigh_r = new MowzieModelRenderer(this.pelvis.part.getChild("thigh_r"));
      this.leg_r1 = new MowzieModelRenderer(this.thigh_r.part.getChild("leg_r1"));
      this.leg_r2 = new MowzieModelRenderer(this.leg_r1.part.getChild("leg_r2"));
      this.parts.add(this.body);
      this.parts.add(this.chest);
      this.parts.add(this.shoulder_l);
      this.parts.add(this.arm_l1);
      this.parts.add(this.arm_l2);
      this.parts.add(this.shoulder_r);
      this.parts.add(this.arm_r1);
      this.parts.add(this.arm_r2);
      this.parts.add(this.neck);
      this.parts.add(this.head);
      this.parts.add(this.horn_base);
      this.parts.add(this.horn);
      this.parts.add(this.horn_l);
      this.parts.add(this.horn_r);
      this.parts.add(this.jewlery);
      this.parts.add(this.crown);
      this.parts.add(this.nose);
      this.parts.add(this.nose_ring);
      this.parts.add(this.pelvis);
      this.parts.add(this.thigh_l);
      this.parts.add(this.leg_l1);
      this.parts.add(this.leg_l2);
      this.parts.add(this.thigh_r);
      this.parts.add(this.leg_r1);
      this.parts.add(this.leg_r2);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -6.0F, -2.0F, 8, 10, 4),
         PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, -0.5009095F, 0.0F, 0.0F));
      PartDefinition chest = body.addOrReplaceChild("chest",
         CubeListBuilder.create().texOffs(25, 32).addBox(-6.0F, -5.0F, -3.0F, 12, 5, 6),
         PartPose.offsetAndRotation(0.0F, -4.4F, -0.4F, 0.31869712F, 0.0F, 0.0F));
      PartDefinition shoulder_l = chest.addOrReplaceChild("shoulder_l",
         CubeListBuilder.create().texOffs(47, 44).addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5),
         PartPose.offsetAndRotation(-6.6F, -2.6F, 0.0F, 0.18325958F, 0.0F, (float) (Math.PI / 12)));
      PartDefinition arm_l1 = shoulder_l.addOrReplaceChild("arm_l1",
         CubeListBuilder.create().texOffs(59, 18).addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4),
         PartPose.offset(0.0F, 1.0F, 0.0F));
      arm_l1.addOrReplaceChild("arm_l2",
         CubeListBuilder.create().texOffs(75, 32).addBox(-1.5F, 0.0F, -2.5F, 3, 8, 3),
         PartPose.offsetAndRotation(0.0F, 8.0F, 1.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
      PartDefinition shoulder_r = chest.addOrReplaceChild("shoulder_r",
         CubeListBuilder.create().texOffs(26, 44).addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5),
         PartPose.offsetAndRotation(6.6F, -2.6F, 0.0F, 0.18325958F, 0.0F, (float) (-Math.PI / 12)));
      PartDefinition arm_r1 = shoulder_r.addOrReplaceChild("arm_r1",
         CubeListBuilder.create().texOffs(42, 18).addBox(-2.0F, 0.0F, -2.0F, 4, 8, 4),
         PartPose.offset(0.0F, 1.0F, 0.0F));
      arm_r1.addOrReplaceChild("arm_r2",
         CubeListBuilder.create().texOffs(62, 32).addBox(-1.5F, 0.0F, -2.5F, 3, 8, 3),
         PartPose.offsetAndRotation(0.0F, 8.0F, 1.0F, (float) (-Math.PI / 4), 0.0F, 0.0F));
      PartDefinition neck = chest.addOrReplaceChild("neck",
         CubeListBuilder.create().texOffs(70, 44).addBox(-3.0F, -4.0F, -6.0F, 6, 4, 6),
         PartPose.offsetAndRotation(0.0F, -5.0F, 3.0F, 0.31869712F, 0.0F, 0.0F));
      PartDefinition head = neck.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 3).addBox(-3.5F, -6.0F, -3.5F, 7, 7, 7),
         PartPose.offsetAndRotation(0.0F, -4.0F, -3.0F, -0.13665928F, 0.0F, 0.0F));
      PartDefinition horn_base = head.addOrReplaceChild("horn_base",
         CubeListBuilder.create().texOffs(62, 8).addBox(-4.5F, -2.0F, -2.0F, 9, 4, 4),
         PartPose.offset(0.0F, -3.5F, 1.0F));
      PartDefinition horn = horn_base.addOrReplaceChild("horn",
         CubeListBuilder.create().texOffs(46, 0).addBox(-10.0F, -1.5F, -1.5F, 20, 3, 3),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      horn.addOrReplaceChild("horn_l",
         CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, -6.0F, -1.0F, 2, 6, 2),
         PartPose.offsetAndRotation(-10.0F, 1.5F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 4)));
      horn.addOrReplaceChild("horn_r",
         CubeListBuilder.create().texOffs(27, 0).addBox(-2.0F, -6.0F, -1.0F, 2, 6, 2),
         PartPose.offsetAndRotation(10.0F, 1.5F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
      horn.addOrReplaceChild("jewlery",
         CubeListBuilder.create().texOffs(58, 57).addBox(-9.0F, 0.0F, 0.0F, 18, 4, 0),
         PartPose.offset(0.0F, 1.5F, 0.0F));
      head.addOrReplaceChild("crown",
         CubeListBuilder.create().texOffs(57, 62).addBox(-3.0F, -2.0F, -3.0F, 6, 2, 6),
         PartPose.offset(0.0F, -6.0F, 0.0F));
      PartDefinition nose = head.addOrReplaceChild("nose",
         CubeListBuilder.create().texOffs(29, 9).addBox(-3.0F, -2.0F, -3.0F, 6, 4, 3),
         PartPose.offset(0.0F, -1.1F, -3.5F));
      nose.addOrReplaceChild("nose_ring",
         CubeListBuilder.create().texOffs(48, 9).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offsetAndRotation(0.0F, 1.1F, -2.0F, -0.3642502F, 0.0F, 0.0F));
      PartDefinition pelvis = body.addOrReplaceChild("pelvis",
         CubeListBuilder.create().texOffs(0, 49).addBox(-4.5F, 0.0F, -2.0F, 9, 5, 6),
         PartPose.offsetAndRotation(0.0F, 3.0F, -0.3F, 0.5009095F, 0.0F, 0.0F));
      PartDefinition thigh_l = pelvis.addOrReplaceChild("thigh_l",
         CubeListBuilder.create().texOffs(21, 18).addBox(-2.0F, 0.0F, -2.5F, 5, 8, 5),
         PartPose.offsetAndRotation(-3.1F, 1.4F, 0.3F, -0.045553092F, 0.13665928F, 0.4098033F));
      PartDefinition leg_l1 = thigh_l.addOrReplaceChild("leg_l1",
         CubeListBuilder.create().texOffs(13, 61).addBox(-1.5F, 0.0F, 0.0F, 3, 10, 3),
         PartPose.offsetAndRotation(0.0F, 8.0F, -2.0F, 1.2747885F, 0.0F, 0.0F));
      leg_l1.addOrReplaceChild("leg_l2",
         CubeListBuilder.create().texOffs(42, 58).addBox(-1.5F, 0.0F, -3.0F, 3, 12, 3),
         PartPose.offsetAndRotation(-0.1F, 10.0F, 2.1F, -1.548107F, -0.4098033F, 0.0F));
      PartDefinition thigh_r = pelvis.addOrReplaceChild("thigh_r",
         CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, 0.0F, -2.5F, 5, 8, 5),
         PartPose.offsetAndRotation(3.1F, 1.4F, 0.3F, -0.045553092F, -0.13665928F, -0.4098033F));
      PartDefinition leg_r1 = thigh_r.addOrReplaceChild("leg_r1",
         CubeListBuilder.create().texOffs(0, 61).addBox(-1.5F, 0.0F, 0.0F, 3, 10, 3),
         PartPose.offsetAndRotation(0.0F, 8.0F, -2.0F, 1.2747885F, 0.0F, 0.0F));
      leg_r1.addOrReplaceChild("leg_r2",
         CubeListBuilder.create().texOffs(29, 58).addBox(-1.5F, 0.0F, -3.0F, 3, 12, 3),
         PartPose.offsetAndRotation(-0.1F, 10.0F, 2.1F, -1.548107F, 0.4098033F, 0.0F));
      return LayerDefinition.create(mesh, 96, 74);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      this.head.part.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
      this.head.part.xRot = headPitch * ((float) Math.PI / 180.0F);
      float globalSpeed = 1.25F;
      float globalDegree = 1.0F;
      float globalOffset = (float) (Math.PI * 3.0 / 2.0);
      this.bob(this.body, 1.125F * globalSpeed * 1.5F, 1.0F * globalDegree, false, limbSwing, limbSwingAmount);
      this.flap(this.shoulder_l, 0.125F * globalSpeed, 0.125F * globalDegree, false, globalOffset, 0.0F, (float) entity.tickCount, 0.5F);
      this.flap(this.shoulder_r, 0.125F * globalSpeed, 0.125F * globalDegree, true, globalOffset, 0.0F, (float) entity.tickCount, 0.5F);
      this.walk(this.shoulder_l, 1.125F * globalSpeed, 1.0F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.shoulder_r, 1.125F * globalSpeed, 1.0F * globalDegree, false, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.arm_r2, 1.125F * globalSpeed, 1.0F * globalDegree, false, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.arm_l2, 1.125F * globalSpeed, 1.0F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.thigh_l, 1.125F * globalSpeed, 1.0F * globalDegree, false, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.thigh_r, 1.125F * globalSpeed, 1.0F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.leg_l2, 1.125F * globalSpeed, 0.75F * globalDegree, false, (float) (Math.PI / 2) + globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.leg_r2, 1.125F * globalSpeed, 0.75F * globalDegree, true, (float) (Math.PI / 2) + globalOffset, 0.0F, limbSwing, limbSwingAmount);
   }
}
