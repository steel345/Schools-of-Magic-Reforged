package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.EntityMagicBroom;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelMagicBroom extends EntityModel<EntityMagicBroom> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "magic_broom"), "main");
   private final ModelPart bone;

   public ModelMagicBroom(ModelPart root) {
      this.bone = root.getChild("bone");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      CubeDeformation flat = new CubeDeformation(0.03F);
      PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -2.0F, -1.0F, 27.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
         .texOffs(0, 20).addBox(-11.0F, -3.0F, -4.0F, 0.0F, 4.0F, 8.0F, flat)
         .texOffs(0, 4).addBox(-19.0F, 1.0F, -4.0F, 8.0F, 0.0F, 8.0F, flat)
         .texOffs(0, 12).addBox(-19.0F, -3.0F, -4.0F, 8.0F, 0.0F, 8.0F, flat), PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

      bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 32).addBox(1.0F, -3.0F, -4.0F, 0.0F, 4.0F, 8.0F, flat), PartPose.offsetAndRotation(-15.0F, 0.0F, 5.0F, 0.0F, 1.5708F, 0.0F));

      bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 20).addBox(1.0F, -3.0F, -4.0F, 0.0F, 4.0F, 8.0F, flat), PartPose.offsetAndRotation(-15.0F, 0.0F, -3.0F, 0.0F, 1.5708F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public void setupAnim(EntityMagicBroom entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
