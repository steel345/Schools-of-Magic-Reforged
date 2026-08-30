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

// the hat, the robe, the sleeves and the boots. it was drawn as one piece hung off its own root, so
// every box here is that same box moved onto the limb that has to carry it
public class ModelWizardRobesLayer1 extends HumanoidModel<LivingEntity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "wizard_robes_layer_1"), "main");

   public ModelWizardRobesLayer1(ModelPart root) {
      super(root);
      this.rightArmPose = ArmPose.EMPTY;
      this.leftArmPose = ArmPose.EMPTY;
      this.hat.visible = false;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      CubeDeformation none = new CubeDeformation(0.0F);

      PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
      root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

      PartDefinition brim = head.addOrReplaceChild("brim", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-6.0F, -10.0F, -6.0F, 12.0F, 2.0F, 12.0F, none)
         .texOffs(0, 14).addBox(-5.0F, -13.0F, -5.0F, 10.0F, 3.0F, 10.0F, none)
         .texOffs(68, 71).addBox(1.0F, -13.0F, -6.0F, 0.0F, 1.0F, 1.0F, none),
         PartPose.ZERO);

      brim.addOrReplaceChild("cone_tip", CubeListBuilder.create()
         .texOffs(20, 47).addBox(-0.5F, -4.0F, -1.0F, 3.0F, 5.0F, 3.0F, none),
         PartPose.offsetAndRotation(-1.0F, -17.0F, 0.0F, -0.6981F, 0.0F, 0.0F));
      brim.addOrReplaceChild("cone", CubeListBuilder.create()
         .texOffs(40, 14).addBox(-2.0F, -4.0F, -3.0F, 6.0F, 5.0F, 6.0F, none),
         PartPose.offsetAndRotation(-1.0F, -13.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
      brim.addOrReplaceChild("buckle", CubeListBuilder.create()
         .texOffs(68, 68).addBox(-2.0F, -3.0F, 4.0F, 1.0F, 3.0F, 1.0F, none)
         .texOffs(69, 72).addBox(-1.0F, -1.0F, 4.0F, 1.0F, 1.0F, 1.0F, none)
         .texOffs(69, 72).addBox(-1.0F, -3.0F, 4.0F, 1.0F, 1.0F, 1.0F, none),
         PartPose.offset(1.0F, -10.0F, -10.0F));

      PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
      body.addOrReplaceChild("robe", CubeListBuilder.create()
         .texOffs(0, 27).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 14.0F, 6.0F, none),
         PartPose.ZERO);

      PartDefinition rightArm = root.addOrReplaceChild("right_arm",
         CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
      rightArm.addOrReplaceChild("sleeve_r", CubeListBuilder.create()
         .texOffs(54, 65).addBox(-4.0F, 7.5F, -3.0F, 1.0F, 2.0F, 6.0F, none)
         .texOffs(68, 7).addBox(-3.0F, 7.5F, 2.0F, 4.0F, 2.0F, 1.0F, none)
         .texOffs(20, 55).addBox(1.0F, 7.5F, -2.0F, 1.0F, 2.0F, 5.0F, none)
         .texOffs(48, 10).addBox(-3.0F, 7.5F, -3.0F, 5.0F, 2.0F, 1.0F, none)
         .texOffs(52, 25).addBox(-3.5F, -2.5F, -2.5F, 5.0F, 10.0F, 5.0F, none),
         PartPose.ZERO);

      PartDefinition leftArm = root.addOrReplaceChild("left_arm",
         CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
      leftArm.addOrReplaceChild("sleeve_l", CubeListBuilder.create()
         .texOffs(40, 65).addBox(-2.0F, 7.5F, -3.0F, 1.0F, 2.0F, 6.0F, none)
         .texOffs(68, 65).addBox(-1.0F, 7.5F, 2.0F, 4.0F, 2.0F, 1.0F, none)
         .texOffs(68, 0).addBox(3.0F, 7.5F, -2.0F, 1.0F, 2.0F, 5.0F, none)
         .texOffs(64, 20).addBox(-1.0F, 7.5F, -3.0F, 5.0F, 2.0F, 1.0F, none)
         .texOffs(52, 40).addBox(-1.5F, -2.5F, -2.5F, 5.0F, 10.0F, 5.0F, none),
         PartPose.ZERO);

      PartDefinition rightLeg = root.addOrReplaceChild("right_leg",
         CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
      rightLeg.addOrReplaceChild("boot_r", CubeListBuilder.create()
         .texOffs(0, 65).addBox(-2.6F, 7.3F, -2.5F, 5.0F, 5.0F, 5.0F, none),
         PartPose.ZERO);

      PartDefinition leftLeg = root.addOrReplaceChild("left_leg",
         CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
      leftLeg.addOrReplaceChild("boot_l", CubeListBuilder.create()
         .texOffs(20, 63).addBox(-2.4F, 7.3F, -2.5F, 5.0F, 5.0F, 5.0F, none),
         PartPose.ZERO);

      return LayerDefinition.create(mesh, 128, 128);
   }
}
