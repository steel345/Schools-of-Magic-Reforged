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

public class ModelJumpingCactus<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "jumping_cactus"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer spine1;
   public final MowzieModelRenderer spine2;
   public final MowzieModelRenderer spine3;
   public final MowzieModelRenderer spine4;
   public final MowzieModelRenderer spine5;
   public final MowzieModelRenderer spine6;

   public ModelJumpingCactus(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.spine1 = new MowzieModelRenderer(this.body.part.getChild("spine1"));
      this.spine2 = new MowzieModelRenderer(this.body.part.getChild("spine2"));
      this.spine3 = new MowzieModelRenderer(this.body.part.getChild("spine3"));
      this.spine4 = new MowzieModelRenderer(this.body.part.getChild("spine4"));
      this.spine5 = new MowzieModelRenderer(this.body.part.getChild("spine5"));
      this.spine6 = new MowzieModelRenderer(this.body.part.getChild("spine6"));
      this.parts.add(this.body);
      this.parts.add(this.spine1);
      this.parts.add(this.spine2);
      this.parts.add(this.spine3);
      this.parts.add(this.spine4);
      this.parts.add(this.spine5);
      this.parts.add(this.spine6);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2, 6, 2),
         PartPose.ZERO);
      body.addOrReplaceChild("spine1",
         CubeListBuilder.create().texOffs(0, 4).addBox(0.0F, -2.0F, -2.0F, 0, 6, 4),
         PartPose.ZERO);
      body.addOrReplaceChild("spine2",
         CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -2.0F, -2.0F, 0, 6, 4),
         PartPose.offsetAndRotation(0, 0, 0, 0, (float) (Math.PI / 2), 0));
      body.addOrReplaceChild("spine3",
         CubeListBuilder.create().texOffs(8, 4).addBox(1.0F, -2.0F, -2.0F, 0, 6, 4),
         PartPose.ZERO);
      body.addOrReplaceChild("spine4",
         CubeListBuilder.create().texOffs(8, 4).addBox(-1.0F, -2.0F, -2.0F, 0, 6, 4),
         PartPose.ZERO);
      body.addOrReplaceChild("spine5",
         CubeListBuilder.create().texOffs(8, 4).addBox(0.0F, -2.0F, -2.0F, 0, 6, 4),
         PartPose.offsetAndRotation(0, 0, 0, 0, (float) (Math.PI / 2), 0));
      body.addOrReplaceChild("spine6",
         CubeListBuilder.create().texOffs(0, 4).addBox(1.0F, -2.0F, -2.0F, 0, 6, 4),
         PartPose.offsetAndRotation(0, 0, 0, 0, (float) (Math.PI / 2), 0));
      return LayerDefinition.create(mesh, 16, 16);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {

   }
}
