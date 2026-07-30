package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelDummyBase {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "target_dummy_base"), "main");

   private final ModelPart basePlate;

   public ModelDummyBase(ModelPart root) {
      this.basePlate = root.getChild("base_plate");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      mesh.getRoot().addOrReplaceChild("base_plate",
         CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, 23.0F, -6.0F, 12.0F, 1.0F, 12.0F),
         PartPose.ZERO);
      return LayerDefinition.create(mesh, 64, 64);
   }

   public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
      this.basePlate.render(poseStack, vertexConsumer, packedLight, packedOverlay);
   }
}
