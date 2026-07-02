package com.paleimitations.schoolsofmagic.client.entity.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelMagicCircle<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "magic_circle"), "main");

   public final MowzieModelRenderer magicCircle;

   public ModelMagicCircle(ModelPart root) {
      super(root);
      this.magicCircle = new MowzieModelRenderer(root.getChild("magicCircle"));
      this.parts.add(this.magicCircle);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      mesh.getRoot().addOrReplaceChild("magicCircle",
         CubeListBuilder.create().texOffs(-48, 0).addBox(-24.0F, 0.0F, -24.0F, 48, 0, 48),
         PartPose.offset(0.0F, 16.0F, 0.0F));
      return LayerDefinition.create(mesh, 96, 48);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {

   }
}
