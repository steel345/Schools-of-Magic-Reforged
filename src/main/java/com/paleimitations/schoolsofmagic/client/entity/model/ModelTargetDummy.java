package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.EntityTargetDummy;
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

public class ModelTargetDummy extends EntityModel<EntityTargetDummy> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "target_dummy"), "main");

   private final ModelPart bone;
   private final ModelPart head;
   private final ModelPart right_leg;
   private final ModelPart left_leg;
   private final ModelPart right_arm;
   private final ModelPart left_arm;
   private final ModelPart body;
   private final ModelPart headwear;

   public ModelTargetDummy(ModelPart root) {
      this.bone = root.getChild("bone");
      this.head = this.bone.getChild("head");
      this.right_leg = this.bone.getChild("right_leg");
      this.left_leg = this.bone.getChild("left_leg");
      this.right_arm = this.bone.getChild("right_arm");
      this.left_arm = this.bone.getChild("left_arm");
      this.body = this.bone.getChild("body");
      this.headwear = this.bone.getChild("headwear");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 26.0F, 0.0F));

      bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
         .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -26.0F, 0.0F));

      bone.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16)
         .addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, -14.0F, 0.0F));

      bone.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror()
         .addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, -14.0F, 0.0F));

      bone.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16)
         .addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, -24.0F, 0.0F));

      bone.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror()
         .addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, -24.0F, 0.0F));

      bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16)
         .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -26.0F, 0.0F));

      bone.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(0, 0)
         .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -26.0F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public void setupAnim(EntityTargetDummy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.bone.xRot = 0.0F;
      this.bone.zRot = 0.0F;
      float wobble = entity.getWobble(ageInTicks);
      if (wobble != 0.0F) {
         float rel = (entity.hitYaw - entity.yBodyRot) * ((float) Math.PI / 180.0F);
         this.bone.xRot = Mth.cos(rel) * wobble;
         this.bone.zRot = -Mth.sin(rel) * wobble;
      }
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
                              float red, float green, float blue, float alpha) {
      this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
