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
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelWebProjectile<T extends Entity> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "web_projectile"), "main");

   public final ModelPart inner;
   public final ModelPart outer;
   public final ModelPart branch1;
   public final ModelPart branch2;
   public final ModelPart branch3;

   public ModelWebProjectile(ModelPart root) {
      this.inner = root.getChild("inner");
      this.outer = root.getChild("outer");
      this.branch1 = root.getChild("branch1");
      this.branch2 = root.getChild("branch2");
      this.branch3 = root.getChild("branch3");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      root.addOrReplaceChild("branch1",
         CubeListBuilder.create().texOffs(-16, 16).addBox(-8.0F, 0.0F, -8.0F, 16, 0, 16),
         PartPose.ZERO);
      root.addOrReplaceChild("outer",
         CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8),
         PartPose.ZERO);
      root.addOrReplaceChild("branch2",
         CubeListBuilder.create().texOffs(0, 16).addBox(-8.0F, -8.0F, 0.0F, 16, 16, 0),
         PartPose.ZERO);
      root.addOrReplaceChild("branch3",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -8.0F, -8.0F, 0, 16, 16),
         PartPose.ZERO);
      root.addOrReplaceChild("inner",
         CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8),
         PartPose.ZERO);
      return LayerDefinition.create(mesh, 64, 32);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {

   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {

      this.branch1.render(pose, buf, light, overlay, r, g, b, a);
      pose.pushPose();
      pose.translate(this.outer.x / 16.0F, this.outer.y / 16.0F, this.outer.z / 16.0F);
      pose.scale(1.2F, 1.2F, 1.2F);
      pose.translate(-this.outer.x / 16.0F, -this.outer.y / 16.0F, -this.outer.z / 16.0F);
      this.outer.render(pose, buf, light, overlay, r, g, b, a);
      pose.popPose();
      this.branch2.render(pose, buf, light, overlay, r, g, b, a);
      this.branch3.render(pose, buf, light, overlay, r, g, b, a);
      this.inner.render(pose, buf, light, overlay, r, g, b, a);
   }
}
