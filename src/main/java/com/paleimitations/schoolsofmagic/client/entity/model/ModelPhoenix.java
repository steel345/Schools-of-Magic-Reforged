package com.paleimitations.schoolsofmagic.client.entity.model;

import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelPhoenix<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "phoenix"), "main");

   public final MowzieModelRenderer body, neck, butt_joint;
   public final MowzieModelRenderer left_wing1, right_wing1, left_wing2, right_wing2, left_wing3, right_wing3;
   public final MowzieModelRenderer leg_left1, leg_right1, leg_left2, leg_right2;
   public final MowzieModelRenderer toe_left1, toe_left2, toe_left3, backtoe_left;
   public final MowzieModelRenderer toe_right1, toe_right2, toe_right3, backtoe_right;
   public final MowzieModelRenderer neck_bit, neck2, head_joint, head;
   public final MowzieModelRenderer beak1, beak1_1, beak2, crest1, crest2, crest3;
   public final MowzieModelRenderer butt, tail_joint, tail_base, tail1, tail2, tail3, tail4, tail5;

   public ModelPhoenix(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.neck = new MowzieModelRenderer(this.body.part.getChild("neck"));
      this.neck_bit = new MowzieModelRenderer(this.neck.part.getChild("neck_bit"));
      this.neck2 = new MowzieModelRenderer(this.neck.part.getChild("neck2"));
      this.head_joint = new MowzieModelRenderer(this.neck2.part.getChild("head_joint"));
      this.head = new MowzieModelRenderer(this.head_joint.part.getChild("head"));
      this.beak1 = new MowzieModelRenderer(this.head.part.getChild("beak1"));
      this.beak2 = new MowzieModelRenderer(this.beak1.part.getChild("beak2"));
      this.beak1_1 = new MowzieModelRenderer(this.head.part.getChild("beak1_1"));
      this.crest1 = new MowzieModelRenderer(this.head.part.getChild("crest1"));
      this.crest2 = new MowzieModelRenderer(this.head.part.getChild("crest2"));
      this.crest3 = new MowzieModelRenderer(this.head.part.getChild("crest3"));
      this.butt_joint = new MowzieModelRenderer(this.body.part.getChild("butt_joint"));
      this.butt = new MowzieModelRenderer(this.butt_joint.part.getChild("butt"));
      this.tail_joint = new MowzieModelRenderer(this.butt_joint.part.getChild("tail_joint"));
      this.tail_base = new MowzieModelRenderer(this.tail_joint.part.getChild("tail_base"));
      this.tail1 = new MowzieModelRenderer(this.tail_joint.part.getChild("tail1"));
      this.tail2 = new MowzieModelRenderer(this.tail_joint.part.getChild("tail2"));
      this.tail3 = new MowzieModelRenderer(this.tail_joint.part.getChild("tail3"));
      this.tail4 = new MowzieModelRenderer(this.tail_joint.part.getChild("tail4"));
      this.tail5 = new MowzieModelRenderer(this.tail_joint.part.getChild("tail5"));
      this.left_wing1 = new MowzieModelRenderer(this.body.part.getChild("left_wing1"));
      this.left_wing2 = new MowzieModelRenderer(this.left_wing1.part.getChild("left_wing2"));
      this.left_wing3 = new MowzieModelRenderer(this.left_wing2.part.getChild("left_wing3"));
      this.right_wing1 = new MowzieModelRenderer(this.body.part.getChild("right_wing1"));
      this.right_wing2 = new MowzieModelRenderer(this.right_wing1.part.getChild("right_wing2"));
      this.right_wing3 = new MowzieModelRenderer(this.right_wing2.part.getChild("right_wing3"));
      this.leg_left1 = new MowzieModelRenderer(this.body.part.getChild("leg_left1"));
      this.leg_left2 = new MowzieModelRenderer(this.leg_left1.part.getChild("leg_left2"));
      this.toe_left1 = new MowzieModelRenderer(this.leg_left2.part.getChild("toe_left1"));
      this.toe_left2 = new MowzieModelRenderer(this.leg_left2.part.getChild("toe_left2"));
      this.toe_left3 = new MowzieModelRenderer(this.leg_left2.part.getChild("toe_left3"));
      this.backtoe_left = new MowzieModelRenderer(this.leg_left2.part.getChild("backtoe_left"));
      this.leg_right1 = new MowzieModelRenderer(this.body.part.getChild("leg_right1"));
      this.leg_right2 = new MowzieModelRenderer(this.leg_right1.part.getChild("leg_right2"));
      this.toe_right1 = new MowzieModelRenderer(this.leg_right2.part.getChild("toe_right1"));
      this.toe_right2 = new MowzieModelRenderer(this.leg_right2.part.getChild("toe_right2"));
      this.toe_right3 = new MowzieModelRenderer(this.leg_right2.part.getChild("toe_right3"));
      this.backtoe_right = new MowzieModelRenderer(this.leg_right2.part.getChild("backtoe_right"));
      this.parts.add(this.body); this.parts.add(this.neck); this.parts.add(this.butt_joint);
      this.parts.add(this.left_wing1); this.parts.add(this.right_wing1);
      this.parts.add(this.leg_left1); this.parts.add(this.leg_right1);
      this.parts.add(this.neck_bit); this.parts.add(this.neck2); this.parts.add(this.head_joint); this.parts.add(this.head);
      this.parts.add(this.beak1); this.parts.add(this.beak1_1); this.parts.add(this.crest1); this.parts.add(this.crest2); this.parts.add(this.crest3); this.parts.add(this.beak2);
      this.parts.add(this.butt); this.parts.add(this.tail_joint); this.parts.add(this.tail_base);
      this.parts.add(this.tail1); this.parts.add(this.tail2); this.parts.add(this.tail3); this.parts.add(this.tail4); this.parts.add(this.tail5);
      this.parts.add(this.left_wing2); this.parts.add(this.left_wing3);
      this.parts.add(this.right_wing2); this.parts.add(this.right_wing3);
      this.parts.add(this.leg_left2); this.parts.add(this.toe_left1); this.parts.add(this.toe_left2); this.parts.add(this.toe_left3); this.parts.add(this.backtoe_left);
      this.parts.add(this.leg_right2); this.parts.add(this.toe_right1); this.parts.add(this.toe_right2); this.parts.add(this.toe_right3); this.parts.add(this.backtoe_right);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 48).addBox(-3.0F, 0.0F, -2.0F, 6, 6, 10),
         PartPose.offsetAndRotation(0.0F, 10.0F, -5.6F, -0.43633232F, 0.0F, 0.0F));

      PartDefinition neck = body.addOrReplaceChild("neck",
         CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.1F, -6.0F, 4, 4, 6),
         PartPose.rotation(-0.43633232F, 0.0F, 0.0F));
      neck.addOrReplaceChild("neck_bit",
         CubeListBuilder.create().texOffs(0, 23).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 5),
         PartPose.offsetAndRotation(0.0F, 3.2F, -2.8F, -0.091106184F, 0.0F, 0.0F));
      PartDefinition neck2 = neck.addOrReplaceChild("neck2",
         CubeListBuilder.create().texOffs(0, 31).addBox(-1.5F, 0.0F, -3.0F, 3, 4, 3),
         PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, (float) (Math.PI / 12), 0.0F, 0.0F));
      PartDefinition head_joint = neck2.addOrReplaceChild("head_joint",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(0.0F, 1.0F, -3.0F));
      PartDefinition head = head_joint.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(34, 56).addBox(-2.0F, 0.0F, -4.0F, 4, 4, 4),
         PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.43633232F, 0.0F, 0.0F));
      PartDefinition beak1 = head.addOrReplaceChild("beak1",
         CubeListBuilder.create().texOffs(34, 50).addBox(-1.5F, 0.0F, -4.0F, 3, 1, 4),
         PartPose.offsetAndRotation(0.0F, 2.2F, -4.0F, 0.31869712F, 0.0F, 0.0F));
      beak1.addOrReplaceChild("beak2",
         CubeListBuilder.create().texOffs(24, 53).addBox(-1.0F, 0.0F, -3.0F, 2, 1, 3),
         PartPose.offsetAndRotation(0.0F, -1.0F, 0.5F, 0.22759093F, 0.0F, 0.0F));
      head.addOrReplaceChild("beak1_1",
         CubeListBuilder.create().texOffs(23, 47).addBox(-1.0F, 0.0F, -3.4F, 2, 1, 4),
         PartPose.offsetAndRotation(0.0F, 3.1F, -4.0F, 0.12217305F, 0.0F, 0.0F));
      head.addOrReplaceChild("crest1",
         CubeListBuilder.create().texOffs(34, 42).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 6),
         PartPose.offsetAndRotation(0.0F, 0.0F, -0.7F, 0.68294734F, 0.0F, 0.0F));
      head.addOrReplaceChild("crest2",
         CubeListBuilder.create().texOffs(21, 40).addBox(-1.0F, 0.0F, 0.0F, 2, 1, 4),
         PartPose.offsetAndRotation(1.4F, 1.1F, -1.0F, 1.0016445F, 0.22759093F, 1.0927507F));
      head.addOrReplaceChild("crest3",
         CubeListBuilder.create().texOffs(21, 34).addBox(-1.0F, 0.0F, 0.0F, 2, 1, 4),
         PartPose.offsetAndRotation(-1.4F, 1.1F, -1.0F, 1.0016445F, -0.22759093F, -1.0927507F));

      PartDefinition butt_joint = body.addOrReplaceChild("butt_joint",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offsetAndRotation(0.0F, 3.2F, 7.2F, -0.27314404F, 0.0F, 0.0F));
      butt_joint.addOrReplaceChild("butt",
         CubeListBuilder.create().texOffs(20, 0).addBox(-2.0F, -2.5F, -2.0F, 4, 4, 4),
         PartPose.rotation(0.0F, (float) (Math.PI / 4), 0.0F));
      PartDefinition tail_joint = butt_joint.addOrReplaceChild("tail_joint",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offsetAndRotation(0.0F, -0.8F, 2.2F, 0.22759093F, 0.0F, 0.0F));
      tail_joint.addOrReplaceChild("tail_base",
         CubeListBuilder.create().texOffs(17, 24).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.rotation(0.0F, (float) (Math.PI / 4), 0.0F));
      tail_joint.addOrReplaceChild("tail1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 14),
         PartPose.offset(0.0F, -0.7F, 0.0F));
      tail_joint.addOrReplaceChild("tail2",
         CubeListBuilder.create().texOffs(36, 66).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 10),
         PartPose.offsetAndRotation(-0.2F, 0.0F, 0.0F, 0.0F, 0.82030475F, 0.0F));
      tail_joint.addOrReplaceChild("tail3",
         CubeListBuilder.create().texOffs(36, 66).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 10),
         PartPose.offsetAndRotation(0.2F, 0.0F, 0.0F, 0.0F, -0.80285144F, 0.0F));
      tail_joint.addOrReplaceChild("tail4",
         CubeListBuilder.create().texOffs(0, 66).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 12),
         PartPose.offsetAndRotation(0.0F, -0.4F, 0.0F, 0.0F, (float) (Math.PI / 9), 0.0F));
      tail_joint.addOrReplaceChild("tail5",
         CubeListBuilder.create().texOffs(0, 66).addBox(-1.5F, 0.0F, 0.0F, 3, 1, 12),
         PartPose.offsetAndRotation(0.0F, -0.4F, 0.0F, 0.0F, (float) (-Math.PI / 9), 0.0F));

      PartDefinition left_wing1 = body.addOrReplaceChild("left_wing1",
         CubeListBuilder.create().texOffs(22, 22).addBox(-10.0F, 0.0F, -2.0F, 10, 2, 9),
         PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.13665928F, 0.0F));
      PartDefinition left_wing2 = left_wing1.addOrReplaceChild("left_wing2",
         CubeListBuilder.create().texOffs(29, 11).addBox(-10.0F, -1.0F, -1.0F, 10, 2, 7),
         PartPose.offsetAndRotation(-9.0F, 1.0F, -1.0F, 0.0F, -0.22759093F, 0.0F));
      left_wing2.addOrReplaceChild("left_wing3",
         CubeListBuilder.create().texOffs(0, 15).addBox(-8.0F, -1.0F, 0.0F, 8, 2, 6),
         PartPose.offsetAndRotation(-10.0F, 0.0F, -1.0F, 0.0F, 0.4537856F, 0.0F));
      PartDefinition right_wing1 = body.addOrReplaceChild("right_wing1",
         CubeListBuilder.create().texOffs(61, 22).addBox(0.0F, 0.0F, -2.0F, 10, 2, 9),
         PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, -0.13665928F, 0.0F));
      PartDefinition right_wing2 = right_wing1.addOrReplaceChild("right_wing2",
         CubeListBuilder.create().texOffs(65, 11).addBox(0.0F, -1.0F, -1.0F, 10, 2, 7),
         PartPose.offsetAndRotation(9.0F, 1.0F, -1.0F, 0.0F, 0.22759093F, 0.0F));
      right_wing2.addOrReplaceChild("right_wing3",
         CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, -1.0F, 0.0F, 8, 2, 6),
         PartPose.offsetAndRotation(10.0F, 0.0F, -1.0F, 0.0F, -0.4537856F, 0.0F));

      PartDefinition leg_left1 = body.addOrReplaceChild("leg_left1",
         CubeListBuilder.create().texOffs(0, 10).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-2.0F, 5.4F, 7.0F, 0.87266463F, 0.0F, 0.0F));
      PartDefinition leg_left2 = leg_left1.addOrReplaceChild("leg_left2",
         CubeListBuilder.create().texOffs(0, 5).addBox(-0.5F, -0.5F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-0.1F, 3.0F, 0.0F, -0.43633232F, 0.0F, 0.0F));
      leg_left2.addOrReplaceChild("toe_left1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 3),
         PartPose.offset(0.0F, 3.0F, 0.0F));
      leg_left2.addOrReplaceChild("toe_left2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 3),
         PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.87266463F, 0.0F));
      leg_left2.addOrReplaceChild("toe_left3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 3),
         PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -0.87266463F, 0.0F));
      leg_left2.addOrReplaceChild("backtoe_left",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2),
         PartPose.offset(0.0F, 3.0F, 0.0F));
      PartDefinition leg_right1 = body.addOrReplaceChild("leg_right1",
         CubeListBuilder.create().texOffs(6, 10).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(2.0F, 5.4F, 7.0F, 0.87266463F, 0.0F, 0.0F));
      PartDefinition leg_right2 = leg_right1.addOrReplaceChild("leg_right2",
         CubeListBuilder.create().texOffs(6, 5).addBox(-0.5F, -0.5F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(0.1F, 3.0F, 0.0F, -0.43633232F, 0.0F, 0.0F));
      leg_right2.addOrReplaceChild("toe_right1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 3),
         PartPose.offset(0.0F, 3.0F, 0.0F));
      leg_right2.addOrReplaceChild("toe_right2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 3),
         PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.87266463F, 0.0F));
      leg_right2.addOrReplaceChild("toe_right3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 3),
         PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, -0.87266463F, 0.0F));
      leg_right2.addOrReplaceChild("backtoe_right",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, 0.0F, 1, 1, 2),
         PartPose.offset(0.0F, 3.0F, 0.0F));
      return LayerDefinition.create(mesh, 100, 84);
   }

   private void rot(MowzieModelRenderer p, float x, float y, float z) {
      p.setRotationAngles(x, y, z);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      if (!(entity instanceof EntityPhoenix phoenix)) {
         this.faceTarget(this.head_joint, 2.0F, netHeadYaw, headPitch);
         return;
      }
      float ageF = ageInTicks;
      float f1Tail = 0.5F;
      if (phoenix.isDying()) {
         this.body.setRotationPoint(0.0F, 9.5F, -5.6F);
         rot(this.body, 0.5235988F, 0.0F, 0.0F);
         rot(this.neck, -0.6981317F, 0.0F, 0.0F);
         rot(this.neck2, -0.5235988F, 0.0F, 0.0F);
         rot(this.head_joint, -0.34906584F, 0.0F, 0.0F);
         rot(this.leg_left1, 0.13962634F, 0.0872665F, 0.0F);
         rot(this.leg_right1, 0.13962634F, -0.0872665F, 0.0F);
         rot(this.leg_left2, 0.0F, 0.0F, 0.0F);
         rot(this.leg_right2, 0.0F, 0.0F, 0.0F);
         rot(this.tail_joint, -0.4363323F, 0.0F, 0.0F);
         float beat = Mth.sin(ageInTicks * 1.6F);
         rot(this.left_wing1, beat * 0.6F, 0.4F, -0.9F - beat * 0.6F);
         rot(this.left_wing2, 0.2F, 0.9F, 0.2F + beat * 0.4F);
         rot(this.left_wing3, -0.2F, 0.5F, -0.3F);
         rot(this.right_wing1, beat * 0.6F, -0.4F, 0.9F + beat * 0.6F);
         rot(this.right_wing2, 0.2F, -0.9F, -0.2F - beat * 0.4F);
         rot(this.right_wing3, -0.2F, -0.5F, 0.3F);
         float shake = Mth.sin(ageInTicks * 3.4F) * 0.05F;
         this.body.part.zRot += shake;
         return;
      }
      boolean grounded = !phoenix.isRebirthing() && (phoenix.onGround()
         || (phoenix.isVehicle() && !phoenix.flyUp && Math.abs(phoenix.getDeltaMovement().y) < 0.06D));
      if (grounded) {
         if (phoenix.isInSittingPose()) {
            rot(this.left_wing1, 0.1832596F, 0.593412F, -1.36136F);
            rot(this.left_wing2, 0.226893F, 1.50098F, 0.0453786F);
            rot(this.left_wing3, -0.1832596F, 0.1832596F, -0.349066F);
            rot(this.right_wing1, 0.1832596F, -0.593412F, 1.36136F);
            rot(this.right_wing2, 0.226893F, -1.50098F, -0.0453786F);
            rot(this.right_wing3, -0.1832596F, -0.1832596F, 0.349066F);
            rot(this.tail2, 0.0F, 0.401426F, 0.0F);
            rot(this.tail3, 0.0F, -0.401426F, 0.0F);
            rot(this.tail4, 0.0F, 0.174533F, 0.0F);
            rot(this.tail5, 0.0F, -0.174533F, 0.0F);
            rot(this.leg_left1, 1.22173F, 0.0F, 0.0F);
            rot(this.leg_right1, 1.22173F, 0.0F, 0.0F);
            rot(this.leg_left2, -1.309F, 0.0F, 0.0F);
            rot(this.leg_right2, -1.309F, 0.0F, 0.0F);
            this.body.setRotationPoint(0.0F, 15.0F, -5.6F);
            rot(this.body, 0.0872665F, 0.0F, 0.0F);
            rot(this.neck2, 0.0F, 0.0F, 0.0F);
            rot(this.tail_joint, -0.226893F, 0.0F, 0.0F);
            this.walk(this.tail_joint, 0.125F, 0.125F, false, 0.0523599F, 0.0F, ageF, f1Tail);
            this.walk(this.leg_left1, 1.25F, 1.0F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leg_right1, 1.25F, 1.0F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
         } else {
            rot(this.left_wing1, 0.1832596F, 0.593412F, -1.36136F);
            rot(this.left_wing2, 0.226893F, 1.50098F, 0.0453786F);
            rot(this.left_wing3, -0.1832596F, 0.1832596F, -0.349066F);
            rot(this.right_wing1, 0.1832596F, -0.593412F, 1.36136F);
            rot(this.right_wing2, 0.226893F, -1.50098F, -0.0453786F);
            rot(this.right_wing3, -0.1832596F, -0.1832596F, 0.349066F);
            rot(this.tail2, 0.0F, 0.401426F, 0.0F);
            rot(this.tail3, 0.0F, -0.401426F, 0.0F);
            rot(this.tail4, 0.0F, 0.174533F, 0.0F);
            rot(this.tail5, 0.0F, -0.174533F, 0.0F);
            this.walk(this.tail_joint, 0.125F, 0.125F, false, 0.0523599F, 0.0F, ageF, f1Tail);
            this.walk(this.leg_left1, 1.25F, 1.0F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
            this.walk(this.leg_right1, 1.25F, 1.0F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
         }
      } else {
         float gs = 2.0F, gd = 1.25F, gh = 1.0F;
         rot(this.body, 0.0F, 0.0F, 0.0F);
         rot(this.neck, 0.0F, 0.0F, 0.0F);
         rot(this.neck2, 0.0F, 0.0F, 0.0F);
         rot(this.head_joint, -0.4555309F, 0.0F, 0.0F);
         rot(this.butt_joint, -0.18203785F, 0.0F, 0.0F);
         rot(this.leg_left2, -2.35619F, 0.0F, 0.0F);
         rot(this.leg_right2, -2.35619F, 0.0F, 0.0F);
         rot(this.toe_left1, 1.18682F, 0.0F, 0.0F);
         rot(this.toe_left2, 1.309F, 0.872665F, 0.349066F);
         rot(this.toe_left3, 1.309F, -0.872665F, -0.349066F);
         rot(this.backtoe_left, -0.907571F, 0.0F, 0.0F);
         rot(this.toe_right1, 1.18682F, 0.0F, 0.0F);
         rot(this.toe_right2, 1.309F, -0.872665F, -0.349066F);
         rot(this.toe_right3, 1.309F, 0.872665F, 0.349066F);
         rot(this.backtoe_right, -0.907571F, 0.0F, 0.0F);
         this.flap(this.left_wing1, 0.25F * gs, 0.25F * gd, false, 0.0F, 0.0F, ageF, f1Tail);
         this.flap(this.left_wing2, 0.25F * gs, 0.375F * gd, false, 0.125F, 0.0F, ageF, f1Tail);
         this.flap(this.left_wing3, 0.25F * gs, 0.5F * gd, false, 0.25F, 0.0F, ageF, f1Tail);
         this.flap(this.right_wing1, 0.25F * gs, 0.25F * gd, true, 0.0F, 0.0F, ageF, f1Tail);
         this.flap(this.right_wing2, 0.25F * gs, 0.375F * gd, true, 0.125F, 0.0F, ageF, f1Tail);
         this.flap(this.right_wing3, 0.25F * gs, 0.5F * gd, true, 0.25F, 0.0F, ageF, f1Tail);
         this.walk(this.tail_joint, 0.25F * gs, 0.025F * gd, false, 0.125F, 0.0F, ageF, f1Tail);
         this.walk(this.neck, 0.25F * gs, 0.025F * gd, true, 0.0F, 0.125F, ageF, f1Tail);
         this.walk(this.neck2, 0.25F * gs, 0.025F * gd, true, 0.0F, 0.25F, ageF, f1Tail);
         this.bob(this.body, 0.25F * gs, 1.75F * gh, false, ageF, f1Tail);
      }
      if (phoenix.isFireMode()) {
         float gape = 0.35F + 0.1F * Mth.sin(ageInTicks * 0.6F);
         rot(this.beak1, 0.31869712F + 0.55F + gape, 0.0F, 0.0F);
         rot(this.beak1_1, 0.12217305F + 0.35F, 0.0F, 0.0F);
      }
      if (phoenix.getCold() > 0.4F) {
         float shiver = Mth.sin(ageInTicks * 2.6F) * phoenix.getCold() * 0.07F;
         this.body.part.zRot += shiver;
         this.head.part.zRot += shiver * 1.5F;
      }
      this.faceTarget(this.head_joint, 2.0F, netHeadYaw, headPitch);
   }
}
