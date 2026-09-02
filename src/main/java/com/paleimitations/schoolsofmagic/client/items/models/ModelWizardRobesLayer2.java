package com.paleimitations.schoolsofmagic.client.items.models;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ModelWizardRobesLayer2 extends HumanoidModel<LivingEntity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "wizard_robes_layer_2"), "main");

   public ModelWizardRobesLayer2(ModelPart root) {
      super(root);
      this.rightArmPose = ArmPose.EMPTY;
      this.leftArmPose = ArmPose.EMPTY;
      this.hat.visible = false;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      CubeDeformation none = new CubeDeformation(0.0F);

      root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
      root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
      PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
      body.addOrReplaceChild("brooch", CubeListBuilder.create()
         .texOffs(68, 68).addBox(-1.1667F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, none)
         .texOffs(69, 72).addBox(-0.1667F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, none)
         .texOffs(69, 72).addBox(-0.1667F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, none),
         PartPose.offset(0.1667F, 12.5F, -3.5F));
      root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
      root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

      PartDefinition rightLeg = root.addOrReplaceChild("right_leg",
         CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
      rightLeg.addOrReplaceChild("leg_r", CubeListBuilder.create()
         .texOffs(32, 27).addBox(-2.5F, -0.25F, -2.4F, 4.8F, 12.5F, 4.8F, none),
         PartPose.ZERO);

      PartDefinition leftLeg = root.addOrReplaceChild("left_leg",
         CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
      leftLeg.addOrReplaceChild("leg_l", CubeListBuilder.create()
         .texOffs(32, 45).addBox(-2.3F, -0.25F, -2.4F, 4.8F, 12.5F, 4.8F, none),
         PartPose.ZERO);

      return LayerDefinition.create(mesh, 128, 128);
   }
}
