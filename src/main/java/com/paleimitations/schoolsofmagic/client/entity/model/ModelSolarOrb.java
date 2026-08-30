package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
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
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public class ModelSolarOrb extends HierarchicalModel<Entity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "solar_orb"), "main");

   private final ModelPart root;
   private final ModelPart orb;

   public ModelSolarOrb(ModelPart root) {
      this.root = root;
      this.orb = root.getChild("solar_orb");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition solar_orb = partdefinition.addOrReplaceChild("solar_orb", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

      PartDefinition core = solar_orb.addOrReplaceChild("core", CubeListBuilder.create().texOffs(36, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.01F))
      .texOffs(0, 18).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.01F))
      .texOffs(24, 18).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.01F))
      .texOffs(0, 30).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare_ring = solar_orb.addOrReplaceChild("flare_ring", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare1 = flare_ring.addOrReplaceChild("flare1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare1_s = flare1.addOrReplaceChild("flare1_s", CubeListBuilder.create().texOffs(16, 30).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare2 = flare_ring.addOrReplaceChild("flare2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5585F, 1.0472F));

      PartDefinition flare2_s = flare2.addOrReplaceChild("flare2_s", CubeListBuilder.create().texOffs(24, 30).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare3 = flare_ring.addOrReplaceChild("flare3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.117F, 2.0595F));

      PartDefinition flare3_s = flare3.addOrReplaceChild("flare3_s", CubeListBuilder.create().texOffs(16, 30).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare4 = flare_ring.addOrReplaceChild("flare4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 3.1416F));

      PartDefinition flare4_s = flare4.addOrReplaceChild("flare4_s", CubeListBuilder.create().texOffs(24, 30).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare5 = flare_ring.addOrReplaceChild("flare5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.5585F, 4.2237F));

      PartDefinition flare5_s = flare5.addOrReplaceChild("flare5_s", CubeListBuilder.create().texOffs(16, 30).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition flare6 = flare_ring.addOrReplaceChild("flare6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.117F, 5.236F));

      PartDefinition flare6_s = flare6.addOrReplaceChild("flare6_s", CubeListBuilder.create().texOffs(24, 30).addBox(-2.0F, -12.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition corona = solar_orb.addOrReplaceChild("corona", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition corona_a_b = corona.addOrReplaceChild("corona_a_b", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, 0.0F, 18.0F, 18.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition corona_b_b = corona.addOrReplaceChild("corona_b_b", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -9.0F, 0.0F, 18.0F, 18.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.03F, 0.0F, -1.5708F, 0.0F));

      PartDefinition lashes = solar_orb.addOrReplaceChild("lashes", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

      PartDefinition lash1 = lashes.addOrReplaceChild("lash1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash1_s = lash1.addOrReplaceChild("lash1_s", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash2 = lashes.addOrReplaceChild("lash2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.7854F));

      PartDefinition lash2_s = lash2.addOrReplaceChild("lash2_s", CubeListBuilder.create().texOffs(8, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash3 = lashes.addOrReplaceChild("lash3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.3963F, 1.5708F));

      PartDefinition lash3_s = lash3.addOrReplaceChild("lash3_s", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash4 = lashes.addOrReplaceChild("lash4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

      PartDefinition lash4_s = lash4.addOrReplaceChild("lash4_s", CubeListBuilder.create().texOffs(8, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash5 = lashes.addOrReplaceChild("lash5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 3.1416F));

      PartDefinition lash5_s = lash5.addOrReplaceChild("lash5_s", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash6 = lashes.addOrReplaceChild("lash6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.7453F, 3.927F));

      PartDefinition lash6_s = lash6.addOrReplaceChild("lash6_s", CubeListBuilder.create().texOffs(8, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash7 = lashes.addOrReplaceChild("lash7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3491F, 4.7124F));

      PartDefinition lash7_s = lash7.addOrReplaceChild("lash7_s", CubeListBuilder.create().texOffs(0, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition lash8 = lashes.addOrReplaceChild("lash8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 5.4978F));

      PartDefinition lash8_s = lash8.addOrReplaceChild("lash8_s", CubeListBuilder.create().texOffs(8, 42).addBox(-2.0F, -20.0F, 0.0F, 4.0F, 16.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public ModelPart root() {
      return this.root;
   }

   public void play(AnimationDefinition definition, float seconds) {
      this.root.getAllParts().forEach(ModelPart::resetPose);
      KeyframeAnimations.animate(this, definition, (long) (seconds * 1000.0F), 1.0F, new Vector3f());
   }

   @Override
   public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.orb.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
