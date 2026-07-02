package com.paleimitations.schoolsofmagic.client.items.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ModelObsidianArmorLayer1 extends HumanoidModel<LivingEntity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "obsidian_armor_layer_1"), "main");

   public ModelObsidianArmorLayer1(ModelPart root) {
      super(root);
      this.rightArmPose = ArmPose.EMPTY;
      this.leftArmPose = ArmPose.EMPTY;
      this.hat.visible = false;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition head = root.addOrReplaceChild("head",
         CubeListBuilder.create(),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition helm = head.addOrReplaceChild("helm",
         CubeListBuilder.create().texOffs(32, 0).addBox(-5.0F, -9.0F, -5.0F, 10, 10, 10),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      helm.addOrReplaceChild("spike_r_1",
         CubeListBuilder.create().texOffs(113, 70).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 1),
         PartPose.offset(3.0F, -10.0F, -5.0F));
      helm.addOrReplaceChild("spike_center",
         CubeListBuilder.create().texOffs(105, 70).addBox(-1.0F, -5.0F, -1.0F, 2, 9, 1),
         PartPose.offset(0.0F, -6.0F, -5.0F));
      helm.addOrReplaceChild("spike_l",
         CubeListBuilder.create().texOffs(113, 70).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 1),
         PartPose.offset(-3.0F, -10.0F, -5.0F));

      root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create(),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition bodyArmor = body.addOrReplaceChild("body_armor",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -1.0F, -3.0F, 10, 14, 6),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      bodyArmor.addOrReplaceChild("gem",
         CubeListBuilder.create().texOffs(113, 70).addBox(-1.0F, 0.0F, -1.0F, 2, 3, 1),
         PartPose.offset(0.0F, 4.0F, -2.5F));

      PartDefinition rightArm = root.addOrReplaceChild("right_arm",
         CubeListBuilder.create(),
         PartPose.offset(-5.0F, 2.0F, 0.0F));
      PartDefinition armorR = rightArm.addOrReplaceChild("arm_r",
         CubeListBuilder.create().texOffs(24, 26).addBox(-4.0F, -2.0F, -3.0F, 6, 14, 6),
         PartPose.offset(0.0F, -1.0F, 0.0F));
      armorR.addOrReplaceChild("spike_r",
         CubeListBuilder.create().texOffs(113, 70).addBox(-1.0F, -4.0F, -1.0F, 2, 4, 1),
         PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 1.5707964F, 1.5707964F, 0.31869712F));

      PartDefinition leftArm = root.addOrReplaceChild("left_arm",
         CubeListBuilder.create(),
         PartPose.offset(5.0F, 2.0F, 0.0F));
      PartDefinition armorL = leftArm.addOrReplaceChild("arm_l",
         CubeListBuilder.create().texOffs(52, 26).addBox(-2.0F, -2.0F, -3.0F, 6, 14, 6),
         PartPose.offset(0.0F, -1.0F, 0.0F));
      armorL.addOrReplaceChild("spike_l_1",
         CubeListBuilder.create().texOffs(113, 76).addBox(-1.0F, 0.0F, -1.0F, 2, 4, 1),
         PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 1.5707964F, 1.5707964F, -0.31869712F));

      PartDefinition rightLeg = root.addOrReplaceChild("right_leg",
         CubeListBuilder.create(),
         PartPose.offset(-1.9F, 12.0F, 0.0F));
      rightLeg.addOrReplaceChild("boot_r",
         CubeListBuilder.create().texOffs(104, 50).addBox(-3.0F, 0.0F, -3.0F, 6, 12, 6),
         PartPose.offset(0.0F, 0.5F, 0.0F));

      PartDefinition leftLeg = root.addOrReplaceChild("left_leg",
         CubeListBuilder.create(),
         PartPose.offset(1.9F, 12.0F, 0.0F));
      leftLeg.addOrReplaceChild("boot_l",
         CubeListBuilder.create().texOffs(104, 50).addBox(-3.0F, 0.0F, -3.0F, 6, 12, 6),
         PartPose.offset(0.0F, 0.5F, 0.0F));

      return LayerDefinition.create(mesh, 128, 128);
   }
}
