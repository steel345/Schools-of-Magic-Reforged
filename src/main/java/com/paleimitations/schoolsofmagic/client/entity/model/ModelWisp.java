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

public class ModelWisp<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "wisp"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer wingR;
   public final MowzieModelRenderer wingL;

   public ModelWisp(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.wingL = new MowzieModelRenderer(this.body.part.getChild("wingL"));
      this.wingR = new MowzieModelRenderer(this.body.part.getChild("wingR"));
      this.parts.add(this.body);
      this.parts.add(this.wingL);
      this.parts.add(this.wingR);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4),
         PartPose.ZERO);
      body.addOrReplaceChild("wingL",
         CubeListBuilder.create().texOffs(8, 8).addBox(-4.0F, -5.0F, 0.0F, 4, 8, 0),
         PartPose.offset(-1.0F, 0.0F, 0.0F));
      body.addOrReplaceChild("wingR",
         CubeListBuilder.create().texOffs(0, 8).addBox(0.0F, -5.0F, 0.0F, 4, 8, 0),
         PartPose.offset(1.0F, 0.0F, 0.0F));
      return LayerDefinition.create(mesh, 16, 16);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float tick = ageInTicks;
      float flap = net.minecraft.util.Mth.cos(tick * 2.1F) * (float) Math.PI * 0.18F;
      this.wingR.part.yRot += flap;
      this.wingL.part.yRot += -flap;
   }
}
