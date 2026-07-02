package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelCocoon<T extends Entity> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "cocoon"), "main");

   public final ModelPart main;
   public final ModelPart outer;

   public ModelCocoon(ModelPart root) {
      this.main = root.getChild("main");
      this.outer = root.getChild("outer");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      mesh.getRoot().addOrReplaceChild("outer",
         CubeListBuilder.create().texOffs(0, 32).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16),
         PartPose.offset(0.0F, 15.0F, 0.0F));
      mesh.getRoot().addOrReplaceChild("main",
         CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16),
         PartPose.offset(0.0F, 15.0F, 0.0F));
      return LayerDefinition.create(mesh, 64, 64);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {

      pose.pushPose();
      pose.translate(this.outer.x / 16.0F, this.outer.y / 16.0F, this.outer.z / 16.0F);
      pose.scale(1.08F, 1.08F, 1.08F);
      pose.translate(-this.outer.x / 16.0F, -this.outer.y / 16.0F, -this.outer.z / 16.0F);
      this.outer.render(pose, buf, light, overlay, r, g, b, a);
      pose.popPose();
      this.main.render(pose, buf, light, overlay, r, g, b, a);
   }
}
