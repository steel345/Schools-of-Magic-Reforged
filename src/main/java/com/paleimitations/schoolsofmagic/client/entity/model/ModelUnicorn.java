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

public class ModelUnicorn<T extends EntityUnicorn> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "unicorn"), "main");

   private final ModelPart unicorn;
   private final ModelPart head;
   private final ModelPart neck;
   private final ModelPart mane;
   private final ModelPart frontLeftLeg;
   private final ModelPart frontRightLeg;
   private final ModelPart backLeftLeg;
   private final ModelPart backRightLeg;
   private final ModelPart tail;
   private final ModelPart horn;

   public ModelUnicorn(ModelPart root) {
      this.unicorn = root.getChild("unicorn");
      this.head = this.unicorn.getChild("head");
      this.neck = this.unicorn.getChild("neck");
      this.mane = this.neck.getChild("mane");
      this.frontLeftLeg = this.unicorn.getChild("front_left_leg");
      this.frontRightLeg = this.unicorn.getChild("front_right_leg");
      this.backLeftLeg = this.unicorn.getChild("back_left_leg");
      this.backRightLeg = this.unicorn.getChild("back_right_leg");
      this.tail = this.unicorn.getChild("tail");
      this.horn = this.head.getChild("horn");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition unicorn = partdefinition.addOrReplaceChild("unicorn", CubeListBuilder.create(), PartPose.offset(-3.0F, 20.5F, -4.9F));

      PartDefinition head = unicorn.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -18.5F, -4.1F));

      PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(19, 16).addBox(0.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -0.01F));

      PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(19, 16).addBox(-2.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -0.01F));

      PartDefinition horn = head.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(60, 48).addBox(-0.5F, -4.75F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
      .texOffs(42, 38).addBox(-1.0F, 0.25F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.25F, 1.0F, 0.48F, 0.0F, 0.0F));

      PartDefinition neck = unicorn.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 35).addBox(-2.05F, -6.0F, -2.0F, 4.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -18.5F, -4.1F));

      PartDefinition mane = neck.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(27, 16).addBox(-1.0F, -6.5F, -5.1F, 2.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(20, 4).addBox(1.0F, -2.5F, -1.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(10, -4).addBox(-1.0F, 0.5F, -1.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(20, 1).addBox(0.0F, -6.5F, -1.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 10.1F));

      PartDefinition beard = neck.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(41, 45).addBox(-1.5F, -7.0F, -27.0F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 20.0F));

      PartDefinition body = unicorn.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-5.0F, -8.0F, -17.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.05F)), PartPose.offset(3.0F, -9.5F, 10.9F));

      PartDefinition front_left_leg = unicorn.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.0F, -1.9F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.0F, -6.5F, -4.1F));

      PartDefinition front_left_hair = front_left_leg.addOrReplaceChild("front_left_hair", CubeListBuilder.create().texOffs(10, -4).addBox(2.0F, -1.5F, -2.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(20, 1).addBox(-2.0F, -1.5F, -2.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 6.5F, 4.1F));

      PartDefinition front_right_leg = unicorn.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.0F, -1.9F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -6.5F, -4.1F));

      PartDefinition front_right_hair = front_right_leg.addOrReplaceChild("front_right_hair", CubeListBuilder.create().texOffs(20, 4).addBox(2.0F, -1.5F, -2.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(20, 1).addBox(-2.0F, -1.5F, -2.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.5F, 4.1F));

      PartDefinition back_left_leg = unicorn.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0F, -1.0F, -1.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.0F, -6.5F, 12.9F));

      PartDefinition back_left_hair = back_left_leg.addOrReplaceChild("back_left_hair", CubeListBuilder.create().texOffs(20, 4).addBox(2.0F, -1.5F, -2.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(10, -4).addBox(-2.0F, -1.5F, -2.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 6.5F, 5.1F));

      PartDefinition back_right_leg = unicorn.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0F, -1.0F, -1.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -6.5F, 12.9F));

      PartDefinition back_right_hair = back_right_leg.addOrReplaceChild("back_right_hair", CubeListBuilder.create().texOffs(20, 4).addBox(2.0F, -1.5F, -2.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
      .texOffs(10, -4).addBox(-2.0F, -1.5F, -2.1F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.5F, 5.1F));

      PartDefinition tail = unicorn.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(2, 1).addBox(-2.0F, -8.5163F, -1.6711F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -10.9837F, 19.5711F, 0.6545F, 0.0F, 0.0F));

      PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(41, 1).addBox(-3.0F, -2.0F, -4.6711F, 6.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, -0.3054F, 0.0F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public void setupAnim(T unicorn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float swing = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
      float opposite = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

      this.frontLeftLeg.xRot = swing;
      this.frontRightLeg.xRot = opposite;
      this.backLeftLeg.xRot = opposite;
      this.backRightLeg.xRot = swing;

      float yaw = netHeadYaw * ((float) Math.PI / 180F);
      float pitch = headPitch * ((float) Math.PI / 180F);
      this.head.yRot = yaw;
      this.head.xRot = pitch;
      this.neck.yRot = yaw;
      this.neck.xRot = pitch;

      this.tail.yRot = Mth.cos(ageInTicks * 0.08F) * 0.15F;
      this.horn.visible = unicorn.hasHorn();
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.unicorn.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
