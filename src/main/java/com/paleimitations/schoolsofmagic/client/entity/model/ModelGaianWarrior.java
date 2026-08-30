package com.paleimitations.schoolsofmagic.client.entity.model;

import com.paleimitations.schoolsofmagic.common.entity.EntityGaianWarrior;
import net.minecraft.client.model.HierarchicalModel;
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

public class ModelGaianWarrior extends HierarchicalModel<EntityGaianWarrior> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "gaian_warrior"), "main");

   private final ModelPart root;
   private final ModelPart warrior;
   private final ModelPart head;

   public ModelGaianWarrior(ModelPart root) {
      this.root = root;
      this.warrior = root.getChild("warrior");
      this.head = this.warrior.getChild("body").getChild("head");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition warrior = partdefinition.addOrReplaceChild("warrior", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition body = warrior.addOrReplaceChild("body", CubeListBuilder.create().texOffs(48, 30).addBox(-6.0F, 0.0F, -4.0F, 12.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
      .texOffs(44, 0).addBox(-8.0F, -14.0F, -5.0F, 16.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

      body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(96, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 0.0F));

      body.addOrReplaceChild("arm_right", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.0F, -3.0F, 5.0F, 24.0F, 6.0F, new CubeDeformation(0.0F))
      .texOffs(88, 30).addBox(-1.5F, -5.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(10.5F, -14.0F, 0.0F));

      body.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(22, 0).addBox(-2.5F, -1.0F, -3.0F, 5.0F, 24.0F, 6.0F, new CubeDeformation(0.0F))
      .texOffs(104, 30).addBox(-2.5F, -5.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.5F, -14.0F, 0.0F));

      warrior.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(0, 30).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 12.0F, 0.0F));

      warrior.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(24, 30).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 12.0F, 0.0F));

      return LayerDefinition.create(meshdefinition, 128, 128);
   }

   @Override
   public void setupAnim(EntityGaianWarrior entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.root().getAllParts().forEach(ModelPart::resetPose);

      this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
      this.head.xRot = headPitch * ((float) Math.PI / 180F);

      this.animate(entity.spawnState, GaianWarriorAnimation.spawn, ageInTicks);
      this.animate(entity.despawnState, GaianWarriorAnimation.despawn, ageInTicks);
      this.animate(entity.deathState, GaianWarriorAnimation.death, ageInTicks);
      this.animate(entity.attackState, GaianWarriorAnimation.attack, ageInTicks);

      if (limbSwingAmount > 0.02F) {
         this.animateWalk(GaianWarriorAnimation.walk, limbSwing, limbSwingAmount, 2.0F, 2.5F);
      } else {
         this.animate(entity.idleState, GaianWarriorAnimation.idle, ageInTicks);
      }
   }

   @Override
   public ModelPart root() {
      return this.root;
   }

   public ModelPart warrior() {
      return this.warrior;
   }

   public static float wrap(float degrees) {
      return Mth.wrapDegrees(degrees);
   }
}
