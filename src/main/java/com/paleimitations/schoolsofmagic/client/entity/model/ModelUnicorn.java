package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class ModelUnicorn<T extends AbstractHorse> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "unicorn"), "main");

   public final ModelPart head, upperMouth, lowerMouth, horn;
   public final ModelPart horseLeftEar, horseRightEar, muleLeftEar, muleRightEar;
   public final ModelPart neck, horseFaceRopes, mane;
   public final ModelPart body, tailBase, tailMiddle, tailTip;
   public final ModelPart backLeftLeg, backLeftShin, backLeftHoof;
   public final ModelPart backRightLeg, backRightShin, backRightHoof;
   public final ModelPart frontLeftLeg, frontLeftShin, frontLeftHoof;
   public final ModelPart frontRightLeg, frontRightShin, frontRightHoof;
   public final ModelPart muleLeftChest, muleRightChest;
   public final ModelPart horseSaddleBottom, horseSaddleFront, horseSaddleBack;
   public final ModelPart horseLeftSaddleRope, horseLeftSaddleMetal;
   public final ModelPart horseRightSaddleRope, horseRightSaddleMetal;
   public final ModelPart horseLeftFaceMetal, horseRightFaceMetal, horseLeftRein, horseRightRein;

   public ModelUnicorn(ModelPart root) {
      this.head = root.getChild("head");
      this.upperMouth = head.getChild("upperMouth");
      this.lowerMouth = head.getChild("lowerMouth");
      this.horn = head.getChild("horn");
      this.horseLeftEar = root.getChild("horseLeftEar");
      this.horseRightEar = root.getChild("horseRightEar");
      this.muleLeftEar = root.getChild("muleLeftEar");
      this.muleRightEar = root.getChild("muleRightEar");
      this.neck = root.getChild("neck");
      this.horseFaceRopes = root.getChild("horseFaceRopes");
      this.mane = root.getChild("mane");
      this.body = root.getChild("body");
      this.tailBase = root.getChild("tailBase");
      this.tailMiddle = root.getChild("tailMiddle");
      this.tailTip = root.getChild("tailTip");
      this.backLeftLeg = root.getChild("backLeftLeg");
      this.backLeftShin = root.getChild("backLeftShin");
      this.backLeftHoof = root.getChild("backLeftHoof");
      this.backRightLeg = root.getChild("backRightLeg");
      this.backRightShin = root.getChild("backRightShin");
      this.backRightHoof = root.getChild("backRightHoof");
      this.frontLeftLeg = root.getChild("frontLeftLeg");
      this.frontLeftShin = root.getChild("frontLeftShin");
      this.frontLeftHoof = root.getChild("frontLeftHoof");
      this.frontRightLeg = root.getChild("frontRightLeg");
      this.frontRightShin = root.getChild("frontRightShin");
      this.frontRightHoof = root.getChild("frontRightHoof");
      this.muleLeftChest = root.getChild("muleLeftChest");
      this.muleRightChest = root.getChild("muleRightChest");
      this.horseSaddleBottom = root.getChild("horseSaddleBottom");
      this.horseSaddleFront = root.getChild("horseSaddleFront");
      this.horseSaddleBack = root.getChild("horseSaddleBack");
      this.horseLeftSaddleRope = root.getChild("horseLeftSaddleRope");
      this.horseLeftSaddleMetal = root.getChild("horseLeftSaddleMetal");
      this.horseRightSaddleRope = root.getChild("horseRightSaddleRope");
      this.horseRightSaddleMetal = root.getChild("horseRightSaddleMetal");
      this.horseLeftFaceMetal = root.getChild("horseLeftFaceMetal");
      this.horseRightFaceMetal = root.getChild("horseRightFaceMetal");
      this.horseLeftRein = root.getChild("horseLeftRein");
      this.horseRightRein = root.getChild("horseRightRein");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition head = root.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      head.addOrReplaceChild("upperMouth",
         CubeListBuilder.create().texOffs(24, 18).addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6),
         PartPose.ZERO);
      head.addOrReplaceChild("lowerMouth",
         CubeListBuilder.create().texOffs(24, 27).addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5),
         PartPose.ZERO);
      head.addOrReplaceChild("horn",
         CubeListBuilder.create().texOffs(25, 0).addBox(-0.5F, -8.0F, -0.5F, 1, 8, 1),
         PartPose.offset(0.0F, -10.0F, 4.0F));
      root.addOrReplaceChild("horseLeftEar",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.45F, -12.0F, 4.0F, 2, 3, 1),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("horseRightEar",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("muleLeftEar",
         CubeListBuilder.create().texOffs(0, 12).addBox(-2.0F, -16.0F, 4.0F, 2, 7, 1),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, (float) (Math.PI / 12)));
      root.addOrReplaceChild("muleRightEar",
         CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, -16.0F, 4.0F, 2, 7, 1),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, (float) (-Math.PI / 12)));
      root.addOrReplaceChild("neck",
         CubeListBuilder.create().texOffs(0, 12).addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("horseFaceRopes",
         CubeListBuilder.create().texOffs(80, 12).addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, new CubeDeformation(0.2F)),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("mane",
         CubeListBuilder.create().texOffs(58, 0).addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 34).addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24),
         PartPose.offset(0.0F, 11.0F, 9.0F));
      root.addOrReplaceChild("tailBase",
         CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3),
         PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, -1.134464F, 0.0F, 0.0F));
      root.addOrReplaceChild("tailMiddle",
         CubeListBuilder.create().texOffs(38, 7).addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7),
         PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, -1.134464F, 0.0F, 0.0F));
      root.addOrReplaceChild("tailTip",
         CubeListBuilder.create().texOffs(24, 3).addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7),
         PartPose.offsetAndRotation(0.0F, 3.0F, 14.0F, (float) (-Math.PI * 4.0 / 9.0), 0.0F, 0.0F));
      root.addOrReplaceChild("backLeftLeg",
         CubeListBuilder.create().texOffs(78, 29).addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5),
         PartPose.offset(4.0F, 9.0F, 11.0F));
      root.addOrReplaceChild("backLeftShin",
         CubeListBuilder.create().texOffs(78, 43).addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3),
         PartPose.offset(4.0F, 16.0F, 11.0F));
      root.addOrReplaceChild("backLeftHoof",
         CubeListBuilder.create().texOffs(78, 51).addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4),
         PartPose.offset(4.0F, 16.0F, 11.0F));
      root.addOrReplaceChild("backRightLeg",
         CubeListBuilder.create().texOffs(96, 29).addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5),
         PartPose.offset(-4.0F, 9.0F, 11.0F));
      root.addOrReplaceChild("backRightShin",
         CubeListBuilder.create().texOffs(96, 43).addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3),
         PartPose.offset(-4.0F, 16.0F, 11.0F));
      root.addOrReplaceChild("backRightHoof",
         CubeListBuilder.create().texOffs(96, 51).addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4),
         PartPose.offset(-4.0F, 16.0F, 11.0F));
      root.addOrReplaceChild("frontLeftLeg",
         CubeListBuilder.create().texOffs(44, 29).addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4),
         PartPose.offset(4.0F, 9.0F, -8.0F));
      root.addOrReplaceChild("frontLeftShin",
         CubeListBuilder.create().texOffs(44, 41).addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3),
         PartPose.offset(4.0F, 16.0F, -8.0F));
      root.addOrReplaceChild("frontLeftHoof",
         CubeListBuilder.create().texOffs(44, 51).addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4),
         PartPose.offset(4.0F, 16.0F, -8.0F));
      root.addOrReplaceChild("frontRightLeg",
         CubeListBuilder.create().texOffs(60, 29).addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4),
         PartPose.offset(-4.0F, 9.0F, -8.0F));
      root.addOrReplaceChild("frontRightShin",
         CubeListBuilder.create().texOffs(60, 41).addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3),
         PartPose.offset(-4.0F, 16.0F, -8.0F));
      root.addOrReplaceChild("frontRightHoof",
         CubeListBuilder.create().texOffs(60, 51).addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4),
         PartPose.offset(-4.0F, 16.0F, -8.0F));
      root.addOrReplaceChild("muleLeftChest",
         CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3),
         PartPose.offsetAndRotation(-7.5F, 3.0F, 10.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
      root.addOrReplaceChild("muleRightChest",
         CubeListBuilder.create().texOffs(0, 47).addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3),
         PartPose.offsetAndRotation(4.5F, 3.0F, 10.0F, 0.0F, (float) (Math.PI / 2), 0.0F));
      root.addOrReplaceChild("horseSaddleBottom",
         CubeListBuilder.create().texOffs(80, 0).addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8),
         PartPose.offset(0.0F, 2.0F, 2.0F));
      root.addOrReplaceChild("horseSaddleFront",
         CubeListBuilder.create().texOffs(106, 9).addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2),
         PartPose.offset(0.0F, 2.0F, 2.0F));
      root.addOrReplaceChild("horseSaddleBack",
         CubeListBuilder.create().texOffs(80, 9).addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2),
         PartPose.offset(0.0F, 2.0F, 2.0F));
      root.addOrReplaceChild("horseLeftSaddleMetal",
         CubeListBuilder.create().texOffs(74, 0).addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2),
         PartPose.offset(5.0F, 3.0F, 2.0F));
      root.addOrReplaceChild("horseLeftSaddleRope",
         CubeListBuilder.create().texOffs(70, 0).addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1),
         PartPose.offset(5.0F, 3.0F, 2.0F));
      root.addOrReplaceChild("horseRightSaddleMetal",
         CubeListBuilder.create().texOffs(74, 4).addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2),
         PartPose.offset(-5.0F, 3.0F, 2.0F));
      root.addOrReplaceChild("horseRightSaddleRope",
         CubeListBuilder.create().texOffs(80, 0).addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1),
         PartPose.offset(-5.0F, 3.0F, 2.0F));
      root.addOrReplaceChild("horseLeftFaceMetal",
         CubeListBuilder.create().texOffs(74, 13).addBox(1.5F, -8.0F, -4.0F, 1, 2, 2),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("horseRightFaceMetal",
         CubeListBuilder.create().texOffs(74, 13).addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2),
         PartPose.offsetAndRotation(0.0F, 4.0F, -10.0F, (float) (Math.PI / 6), 0.0F, 0.0F));
      root.addOrReplaceChild("horseLeftRein",
         CubeListBuilder.create().texOffs(44, 10).addBox(2.6F, -6.0F, -6.0F, 0, 3, 16),
         PartPose.offset(0.0F, 4.0F, -10.0F));
      root.addOrReplaceChild("horseRightRein",
         CubeListBuilder.create().texOffs(44, 5).addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16),
         PartPose.offset(0.0F, 4.0F, -10.0F));
      return LayerDefinition.create(mesh, 128, 128);
   }

   private float updateHorseRotation(float a, float b, float t) {
      float f = b - a;
      while (f < -180.0F) f += 360.0F;
      while (f >= 180.0F) f -= 360.0F;
      return a + t * f;
   }

   @Override
   public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
      super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTickTime);

   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      float partialTickTime = ageInTicks - (float) entity.tickCount;
      float f = this.updateHorseRotation(entity.yBodyRotO, entity.yBodyRot, partialTickTime);
      float f1 = this.updateHorseRotation(entity.yHeadRotO, entity.yHeadRot, partialTickTime);
      float f2 = Mth.lerp(partialTickTime, entity.xRotO, entity.getXRot());
      float f3 = f1 - f;
      float f4 = f2 * (float) (Math.PI / 180.0);
      f3 = Mth.clamp(f3, -20.0F, 20.0F);
      if (limbSwingAmount > 0.2F) {
         f4 += Mth.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;
      }

      float f5 = entity.getEatAnim(partialTickTime);
      float f6 = entity.getStandAnim(partialTickTime);
      float f7 = 1.0F - f6;
      float f8 = entity.getMouthAnim(partialTickTime);
      boolean flag = entity.tailCounter != 0;
      float f9 = (float) entity.tickCount + partialTickTime;
      float f10 = Mth.cos(limbSwing * 0.6662F + (float) Math.PI);
      float f11 = f10 * 0.8F * limbSwingAmount;

      this.head.y = 4.0F;
      this.head.z = -10.0F;
      this.tailBase.y = 3.0F;
      this.tailMiddle.z = 14.0F;
      this.muleRightChest.y = 3.0F;
      this.muleRightChest.z = 10.0F;
      this.body.xRot = 0.0F;
      this.head.xRot = (float) (Math.PI / 6) + f4;
      this.head.yRot = f3 * (float) (Math.PI / 180.0);
      this.head.xRot = f6 * ((float) (Math.PI / 12) + f4) + f5 * 2.1816616F + (1.0F - Math.max(f6, f5)) * this.head.xRot;
      this.head.yRot = f6 * f3 * (float) (Math.PI / 180.0) + (1.0F - Math.max(f6, f5)) * this.head.yRot;
      this.head.y = f6 * -6.0F + f5 * 11.0F + (1.0F - Math.max(f6, f5)) * this.head.y;
      this.head.z = f6 * -1.0F + f5 * -10.0F + (1.0F - Math.max(f6, f5)) * this.head.z;
      this.tailBase.y = f6 * 9.0F + f7 * this.tailBase.y;
      this.tailMiddle.z = f6 * 18.0F + f7 * this.tailMiddle.z;
      this.muleRightChest.y = f6 * 5.5F + f7 * this.muleRightChest.y;
      this.muleRightChest.z = f6 * 15.0F + f7 * this.muleRightChest.z;
      this.body.xRot = f6 * (float) (-Math.PI / 4) + f7 * this.body.xRot;

      ModelPart[] mirrors = { this.horseLeftEar, this.horseRightEar, this.muleLeftEar, this.muleRightEar, this.neck, this.mane };
      for (ModelPart m : mirrors) {
         m.y = this.head.y; m.z = this.head.z;
         m.xRot = this.head.xRot; m.yRot = this.head.yRot;
      }
      this.upperMouth.y = 0.02F; this.lowerMouth.y = 0.0F;
      this.upperMouth.z = 0.02F - f8; this.lowerMouth.z = f8;
      this.upperMouth.xRot = -0.09424778F * f8;
      this.lowerMouth.xRot = (float) (Math.PI / 20) * f8;
      this.upperMouth.yRot = 0.0F; this.lowerMouth.yRot = 0.0F;
      this.muleLeftChest.xRot = f11 / 5.0F;
      this.muleRightChest.xRot = -f11 / 5.0F;

      float f12 = (float) (Math.PI / 12) * f6;
      float f13 = Mth.cos(f9 * 0.6F + (float) Math.PI);
      this.frontLeftLeg.y = -2.0F * f6 + 9.0F * f7;
      this.frontLeftLeg.z = -2.0F * f6 + -8.0F * f7;
      this.frontRightLeg.y = this.frontLeftLeg.y;
      this.frontRightLeg.z = this.frontLeftLeg.z;
      this.backLeftShin.y = this.backLeftLeg.y + Mth.sin((float) (Math.PI / 2) + f12 + f7 * -f10 * 0.5F * limbSwingAmount) * 7.0F;
      this.backLeftShin.z = this.backLeftLeg.z + Mth.cos((float) (-Math.PI / 2) + f12 + f7 * -f10 * 0.5F * limbSwingAmount) * 7.0F;
      this.backRightShin.y = this.backRightLeg.y + Mth.sin((float) (Math.PI / 2) + f12 + f7 * f10 * 0.5F * limbSwingAmount) * 7.0F;
      this.backRightShin.z = this.backRightLeg.z + Mth.cos((float) (-Math.PI / 2) + f12 + f7 * f10 * 0.5F * limbSwingAmount) * 7.0F;
      float f14 = ((float) (-Math.PI / 3) + f13) * f6 + f11 * f7;
      float f15 = ((float) (-Math.PI / 3) - f13) * f6 + -f11 * f7;
      this.frontLeftShin.y = this.frontLeftLeg.y + Mth.sin((float) (Math.PI / 2) + f14) * 7.0F;
      this.frontLeftShin.z = this.frontLeftLeg.z + Mth.cos((float) (-Math.PI / 2) + f14) * 7.0F;
      this.frontRightShin.y = this.frontRightLeg.y + Mth.sin((float) (Math.PI / 2) + f15) * 7.0F;
      this.frontRightShin.z = this.frontRightLeg.z + Mth.cos((float) (-Math.PI / 2) + f15) * 7.0F;
      this.backLeftLeg.xRot = f12 + -f10 * 0.5F * limbSwingAmount * f7;
      this.backLeftShin.xRot = -0.08726646F * f6 + (-f10 * 0.5F * limbSwingAmount - Math.max(0.0F, f10 * 0.5F * limbSwingAmount)) * f7;
      this.backLeftHoof.xRot = this.backLeftShin.xRot;
      this.backRightLeg.xRot = f12 + f10 * 0.5F * limbSwingAmount * f7;
      this.backRightShin.xRot = -0.08726646F * f6 + (f10 * 0.5F * limbSwingAmount - Math.max(0.0F, -f10 * 0.5F * limbSwingAmount)) * f7;
      this.backRightHoof.xRot = this.backRightShin.xRot;
      this.frontLeftLeg.xRot = f14;
      this.frontLeftShin.xRot = (this.frontLeftLeg.xRot + (float) Math.PI * Math.max(0.0F, 0.2F + f13 * 0.2F)) * f6
            + (f11 + Math.max(0.0F, f10 * 0.5F * limbSwingAmount)) * f7;
      this.frontLeftHoof.xRot = this.frontLeftShin.xRot;
      this.frontRightLeg.xRot = f15;
      this.frontRightShin.xRot = (this.frontRightLeg.xRot + (float) Math.PI * Math.max(0.0F, 0.2F - f13 * 0.2F)) * f6
            + (-f11 + Math.max(0.0F, -f10 * 0.5F * limbSwingAmount)) * f7;
      this.frontRightHoof.xRot = this.frontRightShin.xRot;
      this.backLeftHoof.y = this.backLeftShin.y; this.backLeftHoof.z = this.backLeftShin.z;
      this.backRightHoof.y = this.backRightShin.y; this.backRightHoof.z = this.backRightShin.z;
      this.frontLeftHoof.y = this.frontLeftShin.y; this.frontLeftHoof.z = this.frontLeftShin.z;
      this.frontRightHoof.y = this.frontRightShin.y; this.frontRightHoof.z = this.frontRightShin.z;

      boolean saddled = entity.isSaddled();
      boolean ridden = entity.isVehicle();
      boolean isMule = entity instanceof AbstractChestedHorse;
      if (saddled) {
         this.horseSaddleBottom.y = f6 * 0.5F + f7 * 2.0F;
         this.horseSaddleBottom.z = f6 * 11.0F + f7 * 2.0F;
         this.horseSaddleFront.y = this.horseSaddleBottom.y;
         this.horseSaddleBack.y = this.horseSaddleBottom.y;
         this.horseLeftSaddleRope.y = this.horseSaddleBottom.y;
         this.horseRightSaddleRope.y = this.horseSaddleBottom.y;
         this.horseLeftSaddleMetal.y = this.horseSaddleBottom.y;
         this.horseRightSaddleMetal.y = this.horseSaddleBottom.y;
         this.muleLeftChest.y = this.muleRightChest.y;
         this.horseSaddleFront.z = this.horseSaddleBottom.z;
         this.horseSaddleBack.z = this.horseSaddleBottom.z;
         this.horseLeftSaddleRope.z = this.horseSaddleBottom.z;
         this.horseRightSaddleRope.z = this.horseSaddleBottom.z;
         this.horseLeftSaddleMetal.z = this.horseSaddleBottom.z;
         this.horseRightSaddleMetal.z = this.horseSaddleBottom.z;
         this.muleLeftChest.z = this.muleRightChest.z;
         this.horseSaddleBottom.xRot = this.body.xRot;
         this.horseSaddleFront.xRot = this.body.xRot;
         this.horseSaddleBack.xRot = this.body.xRot;
         this.horseLeftRein.y = this.head.y;
         this.horseRightRein.y = this.head.y;
         this.horseFaceRopes.y = this.head.y;
         this.horseLeftFaceMetal.y = this.head.y;
         this.horseRightFaceMetal.y = this.head.y;
         this.horseLeftRein.z = this.head.z;
         this.horseRightRein.z = this.head.z;
         this.horseFaceRopes.z = this.head.z;
         this.horseLeftFaceMetal.z = this.head.z;
         this.horseRightFaceMetal.z = this.head.z;
         this.horseLeftRein.xRot = f4;
         this.horseRightRein.xRot = f4;
         this.horseFaceRopes.xRot = this.head.xRot;
         this.horseLeftFaceMetal.xRot = this.head.xRot;
         this.horseRightFaceMetal.xRot = this.head.xRot;
         this.horseFaceRopes.yRot = this.head.yRot;
         this.horseLeftFaceMetal.yRot = this.head.yRot;
         this.horseLeftRein.yRot = this.head.yRot;
         this.horseRightFaceMetal.yRot = this.head.yRot;
         this.horseRightRein.yRot = this.head.yRot;
         if (isMule) {
            this.horseLeftSaddleRope.xRot = (float) (-Math.PI / 3);
            this.horseLeftSaddleMetal.xRot = (float) (-Math.PI / 3);
            this.horseRightSaddleRope.xRot = (float) (-Math.PI / 3);
            this.horseRightSaddleMetal.xRot = (float) (-Math.PI / 3);
            this.horseLeftSaddleRope.zRot = 0.0F; this.horseLeftSaddleMetal.zRot = 0.0F;
            this.horseRightSaddleRope.zRot = 0.0F; this.horseRightSaddleMetal.zRot = 0.0F;
         } else {
            this.horseLeftSaddleRope.xRot = f11 / 3.0F;
            this.horseLeftSaddleMetal.xRot = f11 / 3.0F;
            this.horseRightSaddleRope.xRot = f11 / 3.0F;
            this.horseRightSaddleMetal.xRot = f11 / 3.0F;
            this.horseLeftSaddleRope.zRot = f11 / 5.0F;
            this.horseLeftSaddleMetal.zRot = f11 / 5.0F;
            this.horseRightSaddleRope.zRot = -f11 / 5.0F;
            this.horseRightSaddleMetal.zRot = -f11 / 5.0F;
         }
      }

      f12 = (float) (-Math.PI * 5.0 / 12.0) + limbSwingAmount * 1.5F;
      if (f12 > 0.0F) f12 = 0.0F;
      if (flag) { this.tailBase.yRot = Mth.cos(f9 * 0.7F); f12 = 0.0F; }
      else this.tailBase.yRot = 0.0F;
      this.tailMiddle.yRot = this.tailBase.yRot;
      this.tailTip.yRot = this.tailBase.yRot;
      this.tailMiddle.y = this.tailBase.y; this.tailTip.y = this.tailBase.y;
      this.tailMiddle.z = this.tailBase.z; this.tailTip.z = this.tailBase.z;
      this.tailBase.xRot = f12; this.tailMiddle.xRot = f12;
      this.tailTip.xRot = (float) (-Math.PI / 12) + f12;
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {

      this.body.render(pose, buf, light, overlay, r, g, b, a);
      this.tailBase.render(pose, buf, light, overlay, r, g, b, a);
      this.tailMiddle.render(pose, buf, light, overlay, r, g, b, a);
      this.tailTip.render(pose, buf, light, overlay, r, g, b, a);
      this.neck.render(pose, buf, light, overlay, r, g, b, a);
      this.mane.render(pose, buf, light, overlay, r, g, b, a);
      this.head.render(pose, buf, light, overlay, r, g, b, a);
      this.horseLeftEar.render(pose, buf, light, overlay, r, g, b, a);
      this.horseRightEar.render(pose, buf, light, overlay, r, g, b, a);
      this.backLeftLeg.render(pose, buf, light, overlay, r, g, b, a);
      this.backLeftShin.render(pose, buf, light, overlay, r, g, b, a);
      this.backLeftHoof.render(pose, buf, light, overlay, r, g, b, a);
      this.backRightLeg.render(pose, buf, light, overlay, r, g, b, a);
      this.backRightShin.render(pose, buf, light, overlay, r, g, b, a);
      this.backRightHoof.render(pose, buf, light, overlay, r, g, b, a);
      this.frontLeftLeg.render(pose, buf, light, overlay, r, g, b, a);
      this.frontLeftShin.render(pose, buf, light, overlay, r, g, b, a);
      this.frontLeftHoof.render(pose, buf, light, overlay, r, g, b, a);
      this.frontRightLeg.render(pose, buf, light, overlay, r, g, b, a);
      this.frontRightShin.render(pose, buf, light, overlay, r, g, b, a);
      this.frontRightHoof.render(pose, buf, light, overlay, r, g, b, a);
   }
}
