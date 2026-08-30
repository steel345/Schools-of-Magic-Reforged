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

public class ModelWornNecklace {
   public static final ModelLayerLocation LAYER =
      new ModelLayerLocation(new ResourceLocation("som", "worn_necklace"), "main");

   private final ModelPart necklace;

   public ModelWornNecklace(ModelPart root) {
      this.necklace = root.getChild("necklace");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      PartDefinition necklace = partdefinition.addOrReplaceChild("necklace", CubeListBuilder.create()
         .texOffs(0, 0).addBox(-4.0F, -2.3F, -5.5F, 9.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)),
         PartPose.offset(0.0F, 2.0F, 3.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   public void render(PoseStack pose, VertexConsumer vc, int light, int overlay,
                      float r, float g, float b, float a) {
      this.necklace.render(pose, vc, light, overlay, r, g, b, a);
   }
}
