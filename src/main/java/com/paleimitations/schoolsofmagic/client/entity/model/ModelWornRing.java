package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelWornRing {
   public static final ModelLayerLocation LAYER =
      new ModelLayerLocation(new ResourceLocation("som", "worn_ring"), "main");

   private final ModelPart ring;

   public ModelWornRing(ModelPart root) {
      this.ring = root.getChild("ring");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition ring = partdefinition.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.99F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.01F))
      .texOffs(32, 32).addBox(-2.0F, -1.99F, -0.5F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 12.0F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   public void render(PoseStack pose, VertexConsumer vc, int light, int overlay,
                      float r, float g, float b, float a) {
      this.ring.render(pose, vc, light, overlay, r, g, b, a);
   }
}
