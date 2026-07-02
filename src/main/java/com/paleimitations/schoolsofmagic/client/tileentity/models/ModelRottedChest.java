package com.paleimitations.schoolsofmagic.client.tileentity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelRottedChest {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "rotted_chest"), "main");

   public final ModelPart root;
   public final ModelPart latch;
   public final ModelPart outside;
   public final ModelPart inside;

   public ModelRottedChest(ModelPart root) {
      this.root = root;
      this.latch = root.getChild("latch");
      this.outside = root.getChild("outside");
      this.inside = root.getChild("inside");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("latch",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2, 4, 1),
         PartPose.offset(0.0F, 15.0F, -7.0F));
      root.addOrReplaceChild("outside",
         CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.0F, -7.0F, 14, 14, 14),
         PartPose.offset(0.0F, 17.0F, 0.0F));
      root.addOrReplaceChild("inside",
         CubeListBuilder.create().texOffs(0, 28).addBox(-7.0F, -7.0F, -7.0F, 14, 14, 14),
         PartPose.offset(0.0F, 17.0F, 0.0F));
      return LayerDefinition.create(mesh, 64, 64);
   }

   public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay) {
      this.latch.render(poseStack, buffer, packedLight, packedOverlay);
      this.outside.render(poseStack, buffer, packedLight, packedOverlay);

      poseStack.pushPose();
      poseStack.translate(this.inside.x / 16.0F, this.inside.y / 16.0F, this.inside.z / 16.0F);
      poseStack.scale(0.95F, 0.95F, 0.95F);
      poseStack.translate(-this.inside.x / 16.0F, -this.inside.y / 16.0F, -this.inside.z / 16.0F);
      this.inside.render(poseStack, buffer, packedLight, packedOverlay);
      poseStack.popPose();
   }
}
