package com.paleimitations.schoolsofmagic.client.tileentity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
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

public class ModelTome {

   private final ModelPart joint;
   private final ModelPart binding;
   private final ModelPart pagesL;
   private final ModelPart pagesR;
   private final ModelPart page;

   public ModelTome() {
      this.joint = createLayer().bakeRoot().getChild("joint");
      this.binding = this.joint.getChild("binding");
      this.pagesL = this.binding.getChild("pagesL");
      this.pagesR = this.binding.getChild("pagesR");
      this.page = this.pagesL.getChild("page");
   }

   protected static LayerDefinition createLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition joint = root.addOrReplaceChild("joint",
         CubeListBuilder.create(),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition binding = joint.addOrReplaceChild("binding",
         CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -43.0F, 0.0F, 12, 86, 8),
         PartPose.offset(0.0F, 20.0F, 0.0F));
      PartDefinition pagesL = binding.addOrReplaceChild("pagesL",
         CubeListBuilder.create().texOffs(55, 100).addBox(0.0F, -40.5F, -4.0F, 55, 81, 8),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      pagesL.addOrReplaceChild("backCover",
         CubeListBuilder.create().texOffs(188, 100).addBox(0.0F, -43.0F, 0.0F, 62, 85, 2),
         PartPose.offset(0.0F, 0.1F, 4.0F));
      pagesL.addOrReplaceChild("page",
         CubeListBuilder.create().texOffs(0, 200).addBox(0.0F, -40.5F, 0.0F, 55, 81, 0),
         PartPose.offset(0.0F, 0.0F, -4.0F));
      PartDefinition pagesR = binding.addOrReplaceChild("pagesR",
         CubeListBuilder.create().texOffs(55, 0).addBox(-55.0F, -40.5F, -4.0F, 55, 81, 8),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      pagesR.addOrReplaceChild("frontCover",
         CubeListBuilder.create().texOffs(188, 0).addBox(-62.0F, -43.0F, 0.0F, 62, 85, 2),
         PartPose.offset(0.0F, 0.1F, 4.0F));
      return LayerDefinition.create(mesh, 320, 320);
   }

   public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay,
                      ResourceLocation tex, float animationTick, float partial, TileEntityPodium te, int bookState) {

      TileEntityPodium.EnumState st = TileEntityPodium.EnumState.values()[bookState];
      int len = st.getAnimationLength();
      float progress = len > 0 ? Math.min(1.0F, animationTick / (float) len) : 0.0F;
      final float H = (float) Math.toRadians(90.0);

      this.binding.xRot = 0.0F; this.binding.zRot = 0.0F;
      this.pagesL.yRot = 0.0F;  this.pagesR.yRot = 0.0F;
      this.page.yRot = 0.0F;    this.page.visible = false;
      switch (st) {
         case CLOSED -> {
            this.pagesL.yRot = H; this.pagesR.yRot = -H; this.binding.zRot = H; this.binding.xRot = -H;
         }
         case OPEN_BOOK -> {
            float f = 1.0F - progress;
            this.pagesL.yRot = H * f; this.pagesR.yRot = -H * f; this.binding.zRot = H * f; this.binding.xRot = -H * f;
         }
         case CLOSE_BOOK -> {
            this.pagesL.yRot = H * progress; this.pagesR.yRot = -H * progress; this.binding.zRot = H * progress; this.binding.xRot = -H * progress;
         }
         case TURN_PAGE_BACK -> {
            this.page.visible = true; this.page.yRot = (float) Math.toRadians(180.0 - 180.0 * progress);
         }
         case TURN_PAGE_FORWARD -> {
            this.page.visible = true; this.page.yRot = (float) Math.toRadians(180.0 * progress);
         }
         default -> {  }
      }

      pose.pushPose();
      pose.scale(0.16F, 0.16F, 0.16F);
      VertexConsumer vc = buffer.getBuffer(RenderType.entitySolid(tex));
      this.joint.render(pose, vc, packedLight, packedOverlay == 0 ? OverlayTexture.NO_OVERLAY : packedOverlay);
      pose.popPose();
   }
}
