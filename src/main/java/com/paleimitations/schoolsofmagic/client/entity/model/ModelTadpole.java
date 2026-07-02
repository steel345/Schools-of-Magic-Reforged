package com.paleimitations.schoolsofmagic.client.entity.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelTadpole<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "tadpole"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer tail;
   public final MowzieModelRenderer eye1;
   public final MowzieModelRenderer eye2;

   public ModelTadpole(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.tail = new MowzieModelRenderer(this.body.part.getChild("tail"));
      this.eye1 = new MowzieModelRenderer(this.body.part.getChild("eye1"));
      this.eye2 = new MowzieModelRenderer(this.body.part.getChild("eye2"));
      this.parts.add(this.body);
      this.parts.add(this.tail);
      this.parts.add(this.eye1);
      this.parts.add(this.eye2);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(3, 1).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2),
         PartPose.offset(0.0F, 24.0F, 0.0F));
      body.addOrReplaceChild("tail",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.5F, 0.0F, 0, 3, 6),
         PartPose.ZERO);
      body.addOrReplaceChild("eye1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(1.0F, -0.2F, -1.0F));
      body.addOrReplaceChild("eye2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(-1.0F, -0.2F, -1.0F));
      return LayerDefinition.create(mesh, 16, 16);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float f = (float) entity.tickCount;
      float f1 = 0.5F;
      float globalSpeed = 1.0F;
      float globalDegree = 0.625F;
      this.bob(this.body, 0.5F * globalSpeed, 1.0F * globalDegree, false, f, f1);
      this.swing(this.tail, 1.0F * globalSpeed, 0.25F, false, 0.0F, 0.0F, f, f1);
   }
}
