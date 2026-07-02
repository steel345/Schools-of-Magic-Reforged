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

public class ModelPetSpider<T extends Entity> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "pet_spider"), "main");

   public final ModelPart body;

   public ModelPetSpider(ModelPart root) {
      this.body = root.getChild("body");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 45).addBox(-6.5F, -4.0F, -13.0F, 13, 6, 15),
         PartPose.offsetAndRotation(0.0F, 16.0F, 3.0F, 0.2268928F, 0.0F, 0.0F));
      body.addOrReplaceChild("abdomen",
         CubeListBuilder.create().texOffs(0, 66).addBox(-7.5F, -6.0F, 0.0F, 15, 12, 18),
         PartPose.offsetAndRotation(0.0F, -2.0F, 3.0F, (float) (Math.PI / 9), 0.0F, 0.0F));
      body.addOrReplaceChild("connector",
         CubeListBuilder.create().texOffs(0, 25).addBox(-4.5F, -3.0F, 0.0F, 9, 7, 13),
         PartPose.offset(0.0F, -2.5F, -8.0F));
      body.addOrReplaceChild("maw1",
         CubeListBuilder.create().texOffs(34, 11).addBox(-2.5F, -1.5F, -3.0F, 5, 6, 3),
         PartPose.offset(2.8F, -1.5F, -12.0F));
      body.addOrReplaceChild("maw2",
         CubeListBuilder.create().texOffs(34, 11).addBox(-2.5F, -1.5F, -3.0F, 5, 6, 3),
         PartPose.offset(-2.8F, -1.5F, -12.0F));
      PartDefinition head = body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 10).addBox(-5.5F, -3.0F, -8.0F, 11, 4, 11),
         PartPose.offsetAndRotation(0.0F, -1.0F, -4.5F, -0.5009095F, 0.0F, 0.0F));
      head.addOrReplaceChild("eye1",
         CubeListBuilder.create().texOffs(36, 4).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offset(2.0F, -0.1F, -7.5F));
      head.addOrReplaceChild("eye2",
         CubeListBuilder.create().texOffs(36, 4).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offset(-2.0F, -0.1F, -7.5F));
      head.addOrReplaceChild("eye3",
         CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2),
         PartPose.offset(-5.0F, 0.1F, -7.5F));
      head.addOrReplaceChild("eye4",
         CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2),
         PartPose.offset(5.0F, 0.1F, -7.5F));
      head.addOrReplaceChild("eye5",
         CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(4.5F, -2.0F, -7.8F));
      head.addOrReplaceChild("eye6",
         CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(-4.5F, -2.0F, -7.8F));
      head.addOrReplaceChild("eye7",
         CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(-3.0F, -2.7F, -7.7F));
      head.addOrReplaceChild("eye8",
         CubeListBuilder.create().texOffs(0, 13).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(3.0F, -2.7F, -7.7F));

      addLegL(body, "leg1L", 6.8F, -0.5F, -10.0F, 0.4098033F, -0.5462881F, 0.27314404F, -0.95609134F, 0.7285004F);
      addLegR(body, "leg1R", -6.8F, -0.5F, -10.0F, -0.4098033F, -0.5462881F, -0.27314404F, 0.95609134F, -0.7285004F);
      addLegL(body, "leg2L", 7.8F, -1.5F, -6.6F, 0.13962634F, -0.31869712F, 0.045553092F, -0.91053826F, 0.91053826F);
      addLegR(body, "leg2R", -7.8F, -1.5F, -6.6F, -0.13962634F, -0.31869712F, -0.045553092F, 0.91053826F, -0.91053826F);
      addLegL(body, "leg3L", 7.8F, -1.0F, -3.0F, -0.13962634F, -0.18203785F, -0.18203785F, -0.7740535F, 0.87266463F);
      addLegR(body, "leg3R", -7.8F, -1.0F, -3.0F, 0.13962634F, -0.18203785F, 0.18203785F, 0.7740535F, -0.87266463F);
      addLegL(body, "leg4L", 7.0F, -0.5F, 0.4F, -0.4098033F, 0.0F, -0.3642502F, -0.5009095F, 0.7285004F);
      addLegR(body, "leg4R", -7.0F, -0.5F, 0.4F, 0.4098033F, 0.0F, 0.3642502F, 0.5009095F, -0.7285004F);

      return LayerDefinition.create(mesh, 80, 96);
   }

   private static void addLegL(PartDefinition parent, String prefix,
                               float ox, float oy, float oz,
                               float yRot1, float xRot2, float yRot2, float zRot2, float zRot4) {
      PartDefinition s1 = parent.addOrReplaceChild(prefix + "1",
         CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4),
         PartPose.offsetAndRotation(ox, oy, oz, 0.0F, yRot1, 0.0F));
      PartDefinition s2 = s1.addOrReplaceChild(prefix + "2",
         CubeListBuilder.create().texOffs(45, 24).addBox(0.0F, -2.0F, -2.0F, 12, 4, 4),
         PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, xRot2, yRot2, zRot2));
      PartDefinition s3 = s2.addOrReplaceChild(prefix + "3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offsetAndRotation(12.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.31869712F));
      PartDefinition s4 = s3.addOrReplaceChild(prefix + "4",
         CubeListBuilder.create().texOffs(45, 40).addBox(0.0F, -2.0F, -2.0F, 12, 4, 4),
         PartPose.offsetAndRotation(0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
      PartDefinition s5 = s4.addOrReplaceChild(prefix + "5",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offsetAndRotation(12.5F, 0.5F, 0.0F, 0.0F, 0.0F, 0.31869712F));
      boolean longerTip = prefix.startsWith("leg2") || prefix.startsWith("leg3");
      if (longerTip) {
         s5.addOrReplaceChild(prefix + "6",
            CubeListBuilder.create().texOffs(44, 48).addBox(0.0F, -2.0F, -2.0F, 14, 4, 4),
            PartPose.offsetAndRotation(0.5F, -0.2F, 0.1F, 0.0F, 0.0F,
               prefix.equals("leg2L") ? 0.6632251F : (float) (Math.PI * 2.0 / 9.0)));
      } else {
         s5.addOrReplaceChild(prefix + "6",
            CubeListBuilder.create().texOffs(45, 40).addBox(0.0F, -2.0F, -2.0F, 12, 4, 4),
            PartPose.offsetAndRotation(0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
      }
   }

   private static void addLegR(PartDefinition parent, String prefix,
                               float ox, float oy, float oz,
                               float yRot1, float xRot2, float yRot2, float zRot2, float zRot4) {
      PartDefinition s1 = parent.addOrReplaceChild(prefix + "1",
         CubeListBuilder.create().texOffs(50, 0).addBox(-2.0F, -2.0F, -2.0F, 4, 4, 4),
         PartPose.offsetAndRotation(ox, oy, oz, 0.0F, yRot1, 0.0F));
      PartDefinition s2 = s1.addOrReplaceChild(prefix + "2",
         CubeListBuilder.create().texOffs(45, 32).addBox(-12.0F, -2.0F, -2.0F, 12, 4, 4),
         PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, xRot2, yRot2, zRot2));
      PartDefinition s3 = s2.addOrReplaceChild(prefix + "3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offsetAndRotation(-12.5F, 0.5F, 0.0F, 0.0F, 0.0F, -0.31869712F));
      PartDefinition s4 = s3.addOrReplaceChild(prefix + "4",
         CubeListBuilder.create().texOffs(45, 40).addBox(-12.0F, -2.0F, -2.0F, 12, 4, 4),
         PartPose.offsetAndRotation(-0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
      PartDefinition s5 = s4.addOrReplaceChild(prefix + "5",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
         PartPose.offsetAndRotation(-12.5F, 0.5F, 0.0F, 0.0F, 0.0F, -0.31869712F));
      boolean longerTip = prefix.startsWith("leg2") || prefix.startsWith("leg3");
      if (longerTip) {
         s5.addOrReplaceChild(prefix + "6",
            CubeListBuilder.create().texOffs(44, 48).addBox(-14.0F, -2.0F, -2.0F, 14, 4, 4),
            PartPose.offsetAndRotation(-0.5F, -0.2F, 0.1F, 0.0F, 0.0F,
               prefix.equals("leg2R") ? -0.6632251F : (float) (-Math.PI * 2.0 / 9.0)));
      } else {
         s5.addOrReplaceChild(prefix + "6",
            CubeListBuilder.create().texOffs(45, 40).addBox(-12.0F, -2.0F, -2.0F, 12, 4, 4),
            PartPose.offsetAndRotation(-0.5F, -0.2F, 0.1F, 0.0F, 0.0F, zRot4));
      }
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {
      this.body.render(pose, buf, light, overlay, r, g, b, a);
   }
}
