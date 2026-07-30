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

// The hip pouch worn in the charm slot (Blockbench export). Its part is placed with
// the same origin convention as a humanoid model, so rendering it inside the body
// part's transform lands it on the hip and moves it with the body.
public class ModelWornPouch {
   public static final ModelLayerLocation LAYER =
      new ModelLayerLocation(new ResourceLocation("som", "worn_pouch"), "main");

   private final ModelPart bb_main;

   public ModelWornPouch(ModelPart root) {
      this.bb_main = root.getChild("bb_main");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition part = mesh.getRoot();
      part.addOrReplaceChild("bb_main", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-6.0F, -12.0F, -3.0F, 2.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(0, 10).addBox(-7.0F, -11.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 23.0F, 0.0F));
      return LayerDefinition.create(mesh, 16, 16);
   }

   public void render(PoseStack pose, VertexConsumer vc, int light, int overlay,
                      float r, float g, float b, float a) {
      this.bb_main.render(pose, vc, light, overlay, r, g, b, a);
   }
}
