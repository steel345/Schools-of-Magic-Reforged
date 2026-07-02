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

public class ModelOrchidFae<T extends Entity> extends EntityModel<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "orchid_fae"), "main");

   public final ModelPart body;
   public final ModelPart legL;
   public final ModelPart legR;
   public final ModelPart leafL;
   public final ModelPart leafR;
   public final ModelPart head;
   public final ModelPart petalCenter;
   public final ModelPart petalL;
   public final ModelPart petalR;
   public final ModelPart lip;
   public final ModelPart tongueR;
   public final ModelPart tongueL;
   public final ModelPart petalL_1;
   public final ModelPart petalL_2;

   private final float bodyBaseY;
   private final float leafLBaseX, leafLBaseZ;
   private final float leafRBaseX, leafRBaseZ;

   public ModelOrchidFae(ModelPart root) {
      this.body = root.getChild("body");
      this.legL = this.body.getChild("legL");
      this.legR = this.body.getChild("legR");
      this.leafL = this.body.getChild("leafL");
      this.leafR = this.body.getChild("leafR");
      this.head = this.body.getChild("head");
      this.petalCenter = this.head.getChild("petalCenter");
      this.petalL = this.head.getChild("petalL");
      this.petalR = this.head.getChild("petalR");
      this.lip = this.head.getChild("lip");
      this.tongueR = this.head.getChild("tongueR");
      this.tongueL = this.head.getChild("tongueL");
      this.petalL_1 = this.head.getChild("petalL_1");
      this.petalL_2 = this.head.getChild("petalL_2");

      this.bodyBaseY = this.body.y;
      this.leafLBaseX = this.leafL.xRot;
      this.leafLBaseZ = this.leafL.zRot;
      this.leafRBaseX = this.leafR.xRot;
      this.leafRBaseZ = this.leafR.zRot;
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(17, 0).addBox(-2.5F, 0.0F, -1.0F, 5, 9, 2),
         PartPose.offset(0.0F, 12.0F, 0.0F));
      body.addOrReplaceChild("legL",
         CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2),
         PartPose.offset(1.5F, 9.0F, 0.0F));
      body.addOrReplaceChild("legR",
         CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 2, 6, 2),
         PartPose.offset(-1.5F, 9.0F, 0.0F));
      body.addOrReplaceChild("leafR",
         CubeListBuilder.create().texOffs(10, 13).addBox(-1.0F, 0.0F, -3.0F, 1, 12, 6),
         PartPose.offsetAndRotation(-2.5F, 1.5F, 0.0F, 0.0F, -0.4098033F, (float) Math.toRadians(47.0)));
      body.addOrReplaceChild("leafL",
         CubeListBuilder.create().texOffs(10, 13).addBox(0.0F, 0.0F, -3.0F, 1, 12, 6),
         PartPose.offsetAndRotation(2.5F, 1.5F, 0.0F, 0.0F, 0.4098033F, (float) Math.toRadians(-47.0)));
      PartDefinition head = body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0F, -2.0F, 4, 6, 4),
         PartPose.ZERO);
      head.addOrReplaceChild("petalCenter",
         CubeListBuilder.create().texOffs(31, 0).addBox(-4.0F, -10.0F, 0.0F, 8, 10, 1),
         PartPose.offset(0.0F, -4.0F, 2.0F));
      head.addOrReplaceChild("petalL",
         CubeListBuilder.create().texOffs(25, 12).addBox(-4.0F, -8.0F, 0.0F, 8, 8, 1),
         PartPose.offsetAndRotation(1.3F, -2.0F, 1.0F, 2.959555F, -0.8196066F, (float) (-Math.PI / 3)));
      head.addOrReplaceChild("petalR",
         CubeListBuilder.create().texOffs(25, 12).addBox(-4.0F, -8.0F, 0.0F, 8, 8, 1),
         PartPose.offsetAndRotation(-1.3F, -2.0F, 1.0F, 2.959555F, 0.8196066F, (float) (Math.PI / 3)));
      head.addOrReplaceChild("petalL_1",
         CubeListBuilder.create().texOffs(25, 12).addBox(-4.0F, -8.0F, 0.0F, 8, 8, 1),
         PartPose.offsetAndRotation(2.0F, -4.0F, 1.4F, (float) Math.PI, -0.13665928F, -2.2310543F));
      head.addOrReplaceChild("petalL_2",
         CubeListBuilder.create().texOffs(25, 12).addBox(-4.0F, -8.0F, 0.0F, 8, 8, 1),
         PartPose.offsetAndRotation(-2.0F, -4.0F, 1.4F, (float) Math.PI, 0.13665928F, 2.2310543F));
      head.addOrReplaceChild("lip",
         CubeListBuilder.create().texOffs(25, 21).addBox(-2.5F, -2.0F, -6.0F, 5, 4, 6),
         PartPose.offsetAndRotation(0.0F, -3.5F, -0.6F, (float) Math.toRadians(20.5), 0.0F, 0.0F));
      head.addOrReplaceChild("tongueR",
         CubeListBuilder.create().texOffs(44, 6).addBox(-1.0F, -2.0F, -6.0F, 3, 4, 6),
         PartPose.offsetAndRotation(-2.0F, -4.2F, 0.0F, -0.091106184F, 0.27314404F, 0.31869712F));
      head.addOrReplaceChild("tongueL",
         CubeListBuilder.create().texOffs(44, 16).addBox(-2.0F, -2.0F, -6.0F, 3, 4, 6),
         PartPose.offsetAndRotation(2.0F, -4.2F, 0.0F, -0.091106184F, -0.27314404F, -0.31869712F));
      return LayerDefinition.create(mesh, 64, 32);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      float deg = (float) (Math.PI / 180.0);
      float swingClamp = Math.min(limbSwingAmount, 1.0F);

      this.legR.xRot = (float) Math.cos(limbSwing * 0.6662F) * 1.1F * swingClamp;
      this.legL.xRot = (float) Math.cos(limbSwing * 0.6662F + Math.PI) * 1.1F * swingClamp;
      this.legL.zRot = 0.0F;
      this.legR.zRot = 0.0F;

      float armSway = (float) Math.sin(ageInTicks * 0.08F) * 0.18F;
      float armLift = (float) Math.sin(ageInTicks * 0.06F) * 0.10F;
      this.leafL.zRot = this.leafLBaseZ - armSway;
      this.leafR.zRot = this.leafRBaseZ + armSway;
      this.leafL.xRot = this.leafLBaseX + armLift;
      this.leafR.xRot = this.leafRBaseX + armLift;

      this.head.xRot = headPitch * deg + (float) Math.sin(ageInTicks * 0.09F) * 0.07F;
      this.head.yRot = netHeadYaw * deg + (float) Math.sin(ageInTicks * 0.05F) * 0.14F;
      this.head.zRot = (float) Math.sin(ageInTicks * 0.07F) * 0.10F;

      this.body.y = this.bodyBaseY + (float) Math.sin(ageInTicks * 0.05F) * 0.5F;
      this.body.xRot = 0.0F;
      this.body.zRot = 0.0F;

      if (entity instanceof com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae fae && fae.isBursting()) {
         int bt = fae.getBurstTicks();
         int dur = com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae.BURST_DURATION;
         int rel = com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae.BURST_RELEASE_AT;
         if (bt > rel) {
            float c = (dur - bt) / (float) (dur - rel);
            this.body.xRot = 0.55F * c;
            this.body.y = this.bodyBaseY - 1.4F * c;
            this.head.xRot = 0.9F * c;
            this.leafL.zRot = this.leafLBaseZ + 0.7F * c;
            this.leafR.zRot = this.leafRBaseZ - 0.7F * c;
            this.leafL.xRot = this.leafLBaseX + 0.6F * c;
            this.leafR.xRot = this.leafRBaseX + 0.6F * c;
            this.legL.xRot = -1.2F * c;
            this.legR.xRot = -1.2F * c;
         } else {
            float r = bt / (float) rel;
            this.body.y = this.bodyBaseY + 2.6F * r;
            this.body.xRot = -0.35F * r;
            this.head.xRot = -0.5F * r;
            this.leafL.zRot = this.leafLBaseZ - 1.0F * r;
            this.leafR.zRot = this.leafRBaseZ + 1.0F * r;
            this.leafL.xRot = this.leafLBaseX - 0.5F * r;
            this.leafR.xRot = this.leafRBaseX - 0.5F * r;
            this.legL.xRot = 0.9F * r;
            this.legR.xRot = 0.9F * r;
            this.legL.zRot = -0.45F * r;
            this.legR.zRot = 0.45F * r;
         }
      } else if (entity instanceof com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae fae2 && fae2.getEmote() != 0) {
         int emote = fae2.getEmote();
         if (emote == com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae.EMOTE_HUG) {
            this.body.xRot = 0.35F;
            this.body.y = this.bodyBaseY;
            this.head.xRot = 0.45F;
            float squeeze = 0.85F + (float) Math.sin(ageInTicks * 0.5F) * 0.12F;
            this.leafL.zRot = this.leafLBaseZ + squeeze;
            this.leafR.zRot = this.leafRBaseZ - squeeze;
            this.leafL.xRot = this.leafLBaseX + 0.9F;
            this.leafR.xRot = this.leafRBaseX + 0.9F;
         } else if (emote == com.paleimitations.schoolsofmagic.common.entity.EntityFlowerFae.EMOTE_MAD) {
            float shake = (float) Math.sin(ageInTicks * 1.4F) * 0.12F;
            this.body.xRot = 0.7F + shake;
            this.body.zRot = shake;
            this.body.y = this.bodyBaseY;
            this.head.xRot = 0.9F + shake;
            this.leafL.zRot = this.leafLBaseZ + 1.1F;
            this.leafR.zRot = this.leafRBaseZ - 1.1F;
            this.leafL.xRot = this.leafLBaseX + 1.2F;
            this.leafR.xRot = this.leafRBaseX + 1.2F;
            this.legL.xRot = -1.4F;
            this.legR.xRot = -1.4F;
         } else {
            float wiggle = (float) Math.sin(ageInTicks * 0.8F);
            this.body.zRot = wiggle * 0.12F;
            this.head.xRot = -0.25F + Math.abs(wiggle) * 0.2F;
            this.head.yRot = wiggle * 0.3F;
            this.leafL.zRot = this.leafLBaseZ - 0.8F;
            this.leafR.zRot = this.leafRBaseZ + 0.8F;
            this.leafL.xRot = this.leafLBaseX - 0.6F + wiggle * 0.2F;
            this.leafR.xRot = this.leafRBaseX - 0.6F - wiggle * 0.2F;
         }
      } else {
         this.body.zRot = 0.0F;
      }
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {

      pose.pushPose();
      pose.translate(this.body.x / 16.0F, this.body.y / 16.0F, this.body.z / 16.0F);
      pose.scale(0.8F, 0.8F, 0.8F);
      pose.translate(-this.body.x / 16.0F, -this.body.y / 16.0F, -this.body.z / 16.0F);
      this.body.render(pose, buf, light, overlay, r, g, b, a);
      pose.popPose();
   }
}
