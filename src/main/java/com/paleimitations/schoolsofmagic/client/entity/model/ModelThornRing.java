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

public class ModelThornRing<T extends Entity> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "thorn_ring"), "main");

   public final ModelPart center;

   public ModelThornRing(ModelPart root) {
      this.center = root.getChild("center");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition center = mesh.getRoot().addOrReplaceChild("center",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(0.0F, 24.0F, 0.0F));

      addVine(center, "vine",    0.0F, -12.0F,  0.0F, 0.59184116F,  0.0F, (float) Math.PI);
      addVine(center, "vine_1",  0.0F,  12.0F,  0.0F, 0.63739425F,  0.0F, (float) -Math.PI);
      addVine(center, "vine_2", -12.0F, 0.0F,   0.0F, -0.7740535F,  0.0F, (float) (Math.PI / 2));
      addVine(center, "vine_3",  12.0F, 0.0F,   0.0F, -0.63739425F, 0.0F, (float) (-Math.PI / 2));
      addVine(center, "vine_4", -8.5F, -8.5F,   0.0F, -0.7740535F,  0.0F, (float) (Math.PI * 3.0 / 4.0));
      addVine(center, "vine_5",  8.5F, -8.5F,   0.0F, -0.59184116F, 0.0F, (float) (-Math.PI * 3.0 / 4.0));
      addVine(center, "vine_6", -8.5F,  8.5F,   0.0F,  0.8651597F,  0.0F, (float) (-Math.PI * 3.0 / 4.0));
      addVine(center, "vine_7",  8.5F,  8.5F,   0.0F, -0.5462881F,  0.0F, (float) (Math.PI * 3.0 / 4.0));
      addVine(center, "vine_8", -11.1F, 4.6F,   0.0F, 0.0F, 0.0F, (float) (-Math.PI * 5.0 / 8.0));
      addVine(center, "vine_9",  11.1F, 4.6F,   0.0F, 0.0F, 0.0F, (float) (Math.PI * 5.0 / 8.0));
      addVine(center, "vine_10",-11.1F,-4.6F,   0.0F, 0.0F, 0.0F, (float) (Math.PI * 5.0 / 8.0));
      addVine(center, "vine_11", 11.1F,-4.6F,   0.0F, 0.0F, 0.0F, (float) (-Math.PI * 5.0 / 8.0));
      addVine(center, "vine_12",-4.6F, 11.1F,   0.0F, 0.0F, 0.0F, (float) (Math.PI / 8));
      addVine(center, "vine_13",-4.6F,-11.1F,   0.0F, 0.0F, 0.0F, (float) (-Math.PI / 8));
      addVine(center, "vine_14", 4.6F, 11.1F,   0.0F, 0.0F, 0.0F, (float) (-Math.PI / 8));
      addVine(center, "vine_15", 4.6F,-11.1F,   0.0F, 0.0F, 0.0F, (float) (Math.PI / 8));

      return LayerDefinition.create(mesh, 32, 32);
   }

   private static void addVine(PartDefinition parent, String name, float x, float y, float z, float rx, float ry, float rz) {
      PartDefinition vine = parent.addOrReplaceChild(name,
         CubeListBuilder.create().texOffs(0, 4).addBox(-3.0F, -1.0F, -1.0F, 6, 2, 2),
         PartPose.offsetAndRotation(x, y, z, rx, ry, rz));
      vine.addOrReplaceChild("thorn1",
         CubeListBuilder.create().texOffs(13, 6).addBox(-3.0F, 0.0F, -3.0F, 6, 0, 6),
         PartPose.rotation((float) (Math.PI / 4), 0.0F, 0.0F));
      vine.addOrReplaceChild("thorn2",
         CubeListBuilder.create().texOffs(13, 0).addBox(-3.0F, 0.0F, -3.0F, 6, 0, 6),
         PartPose.rotation((float) (-Math.PI / 4), 0.0F, 0.0F));
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {
      this.center.render(pose, buf, light, overlay, r, g, b, a);
   }
}
