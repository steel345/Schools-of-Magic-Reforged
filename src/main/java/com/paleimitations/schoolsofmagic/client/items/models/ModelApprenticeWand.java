package com.paleimitations.schoolsofmagic.client.items.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ModelApprenticeWand {

   public static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/wand/apprentice.png");

   private final ModelPart handle;
   private final ModelPart diamond1;
   private final ModelPart diamond2;
   private final ModelPart diamond3;

   public ModelApprenticeWand() {
      ModelPart root = createLayer().bakeRoot();
      this.handle = root.getChild("handle");
      this.diamond1 = this.handle.getChild("diamond1");
      this.diamond2 = this.handle.getChild("diamond2");
      this.diamond3 = this.handle.getChild("diamond3");
   }

   protected static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition handle = root.addOrReplaceChild("handle",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -52.0F, -2.0F, 4, 56, 4),
         PartPose.offset(0.0F, 0.0F, 0.0F));

      PartDefinition gem = handle.addOrReplaceChild("gem",
         CubeListBuilder.create().texOffs(16, 0).addBox(-2.5F, 0.0F, -2.5F, 5, 6, 5),
         PartPose.offset(0.0F, 4.0F, 0.0F));
      gem.addOrReplaceChild("gemFacet1_1", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, 0.0F, 0.0F, 2, 3, 2),
         PartPose.offsetAndRotation(-2.5F, 0.0F, -2.5F, -0.3926991F, 0.07417649F, 0.3926991F));
      gem.addOrReplaceChild("gemFacet2_1", CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, 0.0F, 0.0F, 2, 3, 2),
         PartPose.offsetAndRotation(2.5F, 0.0F, -2.5F, -0.37716565F, -0.07417649F, -0.3926991F));
      gem.addOrReplaceChild("gemFacet3_1", CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, 0.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(2.5F, 0.0F, 2.5F, 0.3926991F, 0.07417649F, -0.3926991F));
      gem.addOrReplaceChild("gemFacet4_1", CubeListBuilder.create().texOffs(36, 0).addBox(0.0F, 0.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(-2.5F, 0.0F, 2.5F, 0.3926991F, -0.07417649F, 0.3926991F));

      handle.addOrReplaceChild("diamond1", CubeListBuilder.create().texOffs(16, 16).addBox(-2.5F, -2.0F, -2.5F, 4, 4, 4),
         PartPose.offsetAndRotation(2.3F, -17.5F, -0.7F, 0.0F, 0.5009095F, 0.0F));
      handle.addOrReplaceChild("diamond2", CubeListBuilder.create().texOffs(16, 16).addBox(-2.5F, -2.0F, -2.5F, 4, 4, 4),
         PartPose.offsetAndRotation(-1.4F, -17.5F, -0.1F, 0.0F, -0.5009095F, 0.0F));
      handle.addOrReplaceChild("diamond3", CubeListBuilder.create().texOffs(16, 16).addBox(-2.5F, -2.0F, -2.5F, 4, 4, 4),
         PartPose.offset(0.5F, -17.5F, 2.6F));

      PartDefinition wf1 = handle.addOrReplaceChild("wandFacet1_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(0.0F, -3.5F, -3.0F, 3, 4, 3),
         PartPose.offsetAndRotation(-3.0F, -14.0F, 2.0F, -0.27314404F, -0.3642502F, -0.4553564F));
      wf1.addOrReplaceChild("wandFacet1_2", CubeListBuilder.create().texOffs(54, 9).addBox(0.0F, -3.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(0.5F, -3.5F, -0.5F, 0.3926991F, -0.07417649F, 0.3926991F));
      PartDefinition wf3 = handle.addOrReplaceChild("wandFacet3_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(0.0F, -3.5F, 0.0F, 3, 4, 3),
         PartPose.offsetAndRotation(0.0F, -14.0F, -3.0F, 0.7853982F, -0.59184116F, -0.5462881F));
      wf3.addOrReplaceChild("wandFacet3_2", CubeListBuilder.create().texOffs(54, 9).addBox(0.0F, -3.0F, 0.0F, 2, 3, 2),
         PartPose.offsetAndRotation(0.5F, -3.5F, 0.5F, -0.3926991F, 0.07417649F, 0.3926991F));
      PartDefinition wf4 = handle.addOrReplaceChild("wandFacet4_1",
         CubeListBuilder.create().texOffs(41, 7).addBox(-3.0F, -3.5F, -3.0F, 3, 4, 3),
         PartPose.offsetAndRotation(3.0F, -14.0F, 2.0F, -0.27314404F, 0.3642502F, 0.4553564F));
      wf4.addOrReplaceChild("wandFacet4_2", CubeListBuilder.create().texOffs(54, 9).addBox(-2.0F, -3.0F, -2.0F, 2, 3, 2),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.3926991F, 0.07417649F, -0.3926991F));

      return LayerDefinition.create(mesh, 64, 64);
   }

   public static ResourceLocation getWandTexture() {
      return TEXTURE;
   }

   public void render(PoseStack pose, MultiBufferSource buf, int light, float scale, int rank) {
      VertexConsumer vc = buf.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      pose.pushPose();
      pose.scale(0.2F, 0.2F, 0.2F);
      this.handle.xRot = (float) Math.PI;
      this.diamond1.visible = rank >= 1;
      this.diamond2.visible = rank >= 2;
      this.diamond3.visible = rank >= 3;
      this.handle.render(pose, vc, light, OverlayTexture.NO_OVERLAY);
      pose.popPose();
   }
}
