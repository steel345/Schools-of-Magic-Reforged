package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.entity.Entity;

public class ModelMagicMissile<T extends Entity> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "magic_missile"), "main");

   private final ModelPart missile;
   private final ModelPart star;
   private final ModelPart trail;
   private final ModelPart motes;

   public ModelMagicMissile(ModelPart root) {
      this.missile = root.getChild("missile");
      this.star = this.missile.getChild("star");
      this.trail = this.missile.getChild("trail");
      this.motes = this.missile.getChild("motes");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition missile = partdefinition.addOrReplaceChild("missile", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

      PartDefinition star = missile.addOrReplaceChild("star", CubeListBuilder.create().texOffs(32, 0).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
      .texOffs(0, 32).addBox(-8.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F))
      .texOffs(0, 0).addBox(0.0F, -8.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition trail = missile.addOrReplaceChild("trail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

      PartDefinition bone = trail.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(20, 48).addBox(-3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.0F));

      PartDefinition spark_3_r1 = bone.addOrReplaceChild("spark_3_r1", CubeListBuilder.create().texOffs(20, 48).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 1.5708F, 0.0F));

      PartDefinition bone2 = trail.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(20, 48).addBox(-3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -4.0F));

      PartDefinition spark_4_r1 = bone2.addOrReplaceChild("spark_4_r1", CubeListBuilder.create().texOffs(20, 48).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 6.0F, 0.0F, 1.5708F, 0.0F));

      PartDefinition motes = missile.addOrReplaceChild("motes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 12.0F));

      PartDefinition mote_c_r1 = motes.addOrReplaceChild("mote_c_r1", CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -3.0F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2443F, 0.0F, 0.0F));

      PartDefinition mote_b_r1 = motes.addOrReplaceChild("mote_b_r1", CubeListBuilder.create().texOffs(40, 48).addBox(2.0F, 1.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3142F));

      PartDefinition mote_a_r1 = motes.addOrReplaceChild("mote_a_r1", CubeListBuilder.create().texOffs(32, 48).addBox(-4.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      this.star.zRot = ageInTicks * 0.5F;
      this.trail.zRot = -ageInTicks * 0.25F;
      this.motes.zRot = ageInTicks * 0.15F;
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.missile.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
