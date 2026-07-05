package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.entity.EntityBroom;
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
import net.minecraft.util.Mth;

public class ModelBroom extends EntityModel<EntityBroom> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "broom"), "main");
   private final ModelPart bone;

   public ModelBroom(ModelPart root) {
      this.bone = root.getChild("bone");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();

      CubeDeformation flat = new CubeDeformation(0.03F);
      PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 0).addBox(-11.0F, -2.0F, -1.0F, 27.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
         .texOffs(0, 20).addBox(-11.0F, -3.0F, -4.0F, 0.0F, 4.0F, 8.0F, flat)
         .texOffs(0, 4).addBox(-19.0F, 1.0F, -4.0F, 8.0F, 0.0F, 8.0F, flat)
         .texOffs(0, 12).addBox(-19.0F, -3.0F, -4.0F, 8.0F, 0.0F, 8.0F, flat), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

      bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 32).addBox(1.0F, -3.0F, -4.0F, 0.0F, 4.0F, 8.0F, flat), PartPose.offsetAndRotation(-15.0F, 0.0F, 5.0F, 0.0F, 1.5708F, 0.0F));

      bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 20).addBox(1.0F, -3.0F, -4.0F, 0.0F, 4.0F, 8.0F, flat), PartPose.offsetAndRotation(-15.0F, 0.0F, -3.0F, 0.0F, 1.5708F, 0.0F));

      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   @Override
   public void setupAnim(EntityBroom entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
      float elapsed = ageInTicks - entity.getSweepStartAge();
      float active = (elapsed >= 0.0F && elapsed <= EntityBroom.SWEEP_DURATION) ? 1.0F : 0.0F;
      float phase = active > 0.0F ? elapsed / (float) EntityBroom.SWEEP_DURATION : 0.0F;

      float sweepSwing = Mth.sin(phase * (float) Math.PI * 2.0F);
      float sweepLean = Mth.sin(phase * (float) Math.PI);

      float move = Math.min(1.0F, limbSwingAmount * 2.5F);
      float walkRock = Mth.sin(limbSwing * 0.7F) * 0.35F * move;
      float idle = Mth.cos(ageInTicks * 0.08F) * 0.2F * (1.0F - active) * (1.0F - move);

      this.bone.yRot = sweepSwing * 0.55F * active;
      this.bone.zRot = -1.5708F - sweepLean * 0.5F * active + walkRock * (1.0F - active);
      this.bone.y = 4.0F + idle;
   }

   @Override
   public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
   }
}
