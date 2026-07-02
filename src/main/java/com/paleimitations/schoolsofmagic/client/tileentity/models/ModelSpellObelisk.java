package com.paleimitations.schoolsofmagic.client.tileentity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellObelisk;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelSpellObelisk {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "spell_obelisk"), "main");

   public final ModelPart root;
   public final ModelPart shape1;
   public final ModelPart shape2;
   public final ModelPart shape3;
   public final ModelPart shape4;
   public final ModelPart shape4_1;
   public final ModelPart shape4_2;
   public final ModelPart shape4_3;
   public final ModelPart shape8;
   public final ModelPart shape9;

   public ModelSpellObelisk(ModelPart root) {
      this.root = root;
      this.shape1 = root.getChild("shape1");
      this.shape2 = root.getChild("shape2");
      this.shape3 = root.getChild("shape3");
      this.shape4 = root.getChild("shape4");
      this.shape4_1 = root.getChild("shape4_1");
      this.shape4_2 = root.getChild("shape4_2");
      this.shape4_3 = root.getChild("shape4_3");
      this.shape8 = root.getChild("shape8");
      this.shape9 = root.getChild("shape9");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("shape1",
         CubeListBuilder.create().texOffs(0, 18).addBox(-7.0F, 0.0F, -6.0F, 14, 10, 12),
         PartPose.offset(0.0F, 11.0F, 0.0F));
      root.addOrReplaceChild("shape4",
         CubeListBuilder.create().texOffs(28, 57).addBox(0.0F, 0.0F, -5.0F, 3, 5, 10),
         PartPose.offset(4.0F, 1.0F, 0.0F));
      root.addOrReplaceChild("shape2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, 0.0F, -7.0F, 16, 3, 14),
         PartPose.offset(0.0F, 21.0F, 0.0F));
      root.addOrReplaceChild("shape8",
         CubeListBuilder.create().texOffs(57, 45).addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8),
         PartPose.offset(0.0F, -4.0F, 0.0F));
      root.addOrReplaceChild("shape3",
         CubeListBuilder.create().texOffs(0, 41).addBox(-7.0F, 0.0F, -5.0F, 14, 5, 10),
         PartPose.offset(0.0F, 6.0F, 0.0F));
      root.addOrReplaceChild("shape4_2",
         CubeListBuilder.create().texOffs(55, 57).addBox(-3.0F, 0.0F, -4.0F, 3, 5, 8),
         PartPose.offset(-4.0F, -4.0F, 0.0F));
      root.addOrReplaceChild("shape4_1",
         CubeListBuilder.create().texOffs(0, 57).addBox(-3.0F, 0.0F, -5.0F, 3, 5, 10),
         PartPose.offset(-4.0F, 1.0F, 0.0F));
      root.addOrReplaceChild("shape9",
         CubeListBuilder.create().texOffs(55, 31).addBox(-6.0F, 0.0F, -4.0F, 12, 4, 8),
         PartPose.offset(0.0F, -8.0F, 0.0F));
      root.addOrReplaceChild("shape4_3",
         CubeListBuilder.create().texOffs(79, 57).addBox(0.0F, 0.0F, -4.0F, 3, 5, 8),
         PartPose.offset(4.0F, -4.0F, 0.0F));
      return LayerDefinition.create(mesh, 128, 80);
   }

   public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int tick, TileEntitySpellObelisk te) {
      this.shape1.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape4.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape2.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape8.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape3.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape4_2.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape4_1.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape9.render(poseStack, buffer, packedLight, packedOverlay);
      this.shape4_3.render(poseStack, buffer, packedLight, packedOverlay);
   }
}
