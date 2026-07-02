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

public class ModelObsidianArmorLayer2 extends HumanoidModel<LivingEntity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "obsidian_armor_layer_2"), "main");

   public ModelObsidianArmorLayer2(ModelPart root) {
      super(root);
      this.rightArmPose = ArmPose.EMPTY;
      this.leftArmPose = ArmPose.EMPTY;
      this.head.visible = false;
      this.hat.visible = false;
      this.rightArm.visible = false;
      this.leftArm.visible = false;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
      root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create(),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      body.addOrReplaceChild("body_leggings",
         CubeListBuilder.create().texOffs(0, 52).addBox(-4.5F, 0.0F, -2.5F, 9, 11, 5),
         PartPose.offset(0.0F, 1.0F, 0.0F));

      root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
      root.addOrReplaceChild("left_arm",  CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

      PartDefinition rightLeg = root.addOrReplaceChild("right_leg",
         CubeListBuilder.create(),
         PartPose.offset(-1.9F, 12.0F, 0.0F));
      rightLeg.addOrReplaceChild("legging_r",
         CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, 0.0F, -2.5F, 5, 13, 5),
         PartPose.offset(0.0F, -1.0F, 0.0F));

      PartDefinition leftLeg = root.addOrReplaceChild("left_leg",
         CubeListBuilder.create(),
         PartPose.offset(1.9F, 12.0F, 0.0F));
      leftLeg.addOrReplaceChild("legging_l",
         CubeListBuilder.create().texOffs(0, 32).addBox(-2.5F, 0.0F, -2.5F, 5, 13, 5),
         PartPose.offset(0.0F, -1.0F, 0.0F));

      return LayerDefinition.create(mesh, 128, 128);
   }
}
