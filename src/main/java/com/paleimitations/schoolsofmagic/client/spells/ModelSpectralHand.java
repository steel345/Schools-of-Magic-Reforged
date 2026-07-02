package com.paleimitations.schoolsofmagic.client.spells;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;

public class ModelSpectralHand {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "spectral_hand"), "main");
   public static final ResourceLocation TEXTURE =
      new ResourceLocation("som", "textures/entity/spectral_hand.png");

   public final ModelPart palm;
   public final ModelPart thumb_base;
   public final ModelPart pointer_base;
   public final ModelPart pinky_base;
   public final ModelPart middle_base;
   public final ModelPart ring_base;
   public final ModelPart thumb;
   public final ModelPart pointer_tip;
   public final ModelPart pinky_tip;
   public final ModelPart middle_tip;
   public final ModelPart ring_tip;

   public ModelSpectralHand(ModelPart root) {
      this.palm = root.getChild("palm");
      this.ring_base = this.palm.getChild("ring_base");
      this.middle_base = this.palm.getChild("middle_base");
      this.pinky_base = this.palm.getChild("pinky_base");
      this.pointer_base = this.palm.getChild("pointer_base");
      this.thumb_base = this.palm.getChild("thumb_base");
      this.ring_tip = this.ring_base.getChild("ring_tip");
      this.middle_tip = this.middle_base.getChild("middle_tip");
      this.pinky_tip = this.pinky_base.getChild("pinky_tip");
      this.pointer_tip = this.pointer_base.getChild("pointer_tip");
      this.thumb = this.thumb_base.getChild("thumb");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition palm = root.addOrReplaceChild("palm",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, 0.0F, 4, 4, 1),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.13665928F));

      PartDefinition ring_base = palm.addOrReplaceChild("ring_base",
         CubeListBuilder.create().texOffs(0, 9).addBox(-0.5F, -2.5F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(0.5F, -4.5F, 0.5F, 0.63739425F, 0.0F, 0.18203785F));
      ring_base.addOrReplaceChild("ring_tip",
         CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
         PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.4553564F, 0.0F, 0.0F));

      PartDefinition middle_base = palm.addOrReplaceChild("middle_base",
         CubeListBuilder.create().texOffs(0, 9).addBox(-0.5F, -2.5F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-0.5F, -4.5F, 0.5F, 0.4098033F, 0.0F, -0.091106184F));
      middle_base.addOrReplaceChild("middle_tip",
         CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
         PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.63739425F, 0.0F, 0.0F));

      PartDefinition pinky_base = palm.addOrReplaceChild("pinky_base",
         CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
         PartPose.offsetAndRotation(1.5F, -4.0F, 0.5F, 0.7285004F, 0.0F, 0.63739425F));
      pinky_base.addOrReplaceChild("pinky_tip",
         CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
         PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.4553564F, 0.0F, 0.0F));

      PartDefinition pointer_base = palm.addOrReplaceChild("pointer_base",
         CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
         PartPose.offsetAndRotation(-1.5F, -4.0F, 0.5F, 0.5462881F, 0.0F, -0.27314404F));
      pointer_base.addOrReplaceChild("pointer_tip",
         CubeListBuilder.create().texOffs(5, 6).addBox(-0.5F, -2.0F, -0.5F, 1, 2, 1),
         PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.5462881F, 0.0F, 0.0F));

      PartDefinition thumb_base = palm.addOrReplaceChild("thumb_base",
         CubeListBuilder.create().texOffs(10, 0).addBox(-1.4F, -1.5F, -0.2F, 2, 2, 1),
         PartPose.offsetAndRotation(-1.6F, -0.5F, 0.0F, 0.0F, -0.091106184F, -0.4098033F));
      thumb_base.addOrReplaceChild("thumb",
         CubeListBuilder.create().texOffs(5, 9).addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-0.9F, -1.0F, 0.2F, 0.31869712F, 0.0F, -0.045553092F));

      return LayerDefinition.create(mesh, 16, 16);
   }

   private static void setRot(ModelPart part, float x, float y, float z) {
      part.xRot = x;
      part.yRot = y;
      part.zRot = z;
   }

   public void setGrabAngles() {
      setRot(this.pointer_tip, 2.2310543F, 0.0F, 0.0F);
      setRot(this.pinky_base, 1.4570009F, 0.0F, 0.0F);
      setRot(this.thumb_base, 0.18203785F, -1.4570009F, -0.7285004F);
      setRot(this.middle_base, 1.4570009F, 0.0F, 0.0F);
      setRot(this.pinky_tip, 2.276433F, 0.0F, 0.0F);
      setRot(this.ring_base, 1.548107F, 0.0F, 0.0F);
      setRot(this.pointer_base, 1.2747885F, 0.0F, 0.0F);
      setRot(this.palm, 0.0F, 0.0F, 0.3642502F);
      setRot(this.thumb, 1.3203416F, 0.0F, -1.4570009F);
      setRot(this.ring_tip, 2.1399481F, 0.0F, 0.0F);
      setRot(this.middle_tip, 2.0943952F, 0.0F, 0.0F);
   }

   public void setUngrabAngles() {
      setRot(this.middle_base, 0.4098033F, 0.0F, -0.091106184F);
      setRot(this.ring_base, 0.63739425F, 0.0F, 0.18203785F);
      setRot(this.pinky_tip, 0.4553564F, 0.0F, 0.0F);
      setRot(this.thumb, 0.31869712F, 0.0F, -0.045553092F);
      setRot(this.palm, 0.0F, 0.0F, 0.13665928F);
      setRot(this.middle_tip, 0.63739425F, 0.0F, 0.0F);
      setRot(this.ring_tip, 0.4553564F, 0.0F, 0.0F);
      setRot(this.pinky_base, 0.7285004F, 0.0F, 0.63739425F);
      setRot(this.pointer_base, 0.5462881F, 0.0F, -0.27314404F);
      setRot(this.thumb_base, 0.0F, -0.091106184F, -0.4098033F);
      setRot(this.pointer_tip, 0.5462881F, 0.0F, 0.0F);
   }

   public void render(PoseStack poseStack, MultiBufferSource buffer, Entity entity, float scale, Vec3 position, float partial, ItemStack grabbedStack, boolean grabbed, int swingTick, int packedLight) {
      Minecraft mc = Minecraft.getInstance();

      net.minecraft.world.phys.Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
      double x = cam.x;
      double y = cam.y;
      double z = cam.z;

      poseStack.pushPose();
      poseStack.translate(position.x - x, position.y - y, position.z - z);
      poseStack.pushPose();
      poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partial, entity.yRotO, entity.getYRot()) + 180.0F));
      poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partial, entity.xRotO, entity.getXRot())));
      poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
      if (swingTick != 0) {
         poseStack.mulPose(Axis.XP.rotationDegrees((1.0F - Math.abs(1.0F - ((float)swingTick - partial) / 3.0F)) * 66.0F));
      }
      if (grabbedStack.getItem() instanceof SwordItem
         || grabbedStack.getItem() instanceof PickaxeItem
         || grabbedStack.getItem() instanceof ShovelItem
         || grabbedStack.getItem() instanceof AxeItem
         || grabbedStack.getItem() instanceof HoeItem) {
         poseStack.pushPose();
         poseStack.translate(0.2D, -0.15D, -0.1D);
         poseStack.scale(1.0F, 1.0F, 1.0F);
         poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
         mc.getItemRenderer().renderStatic(grabbedStack, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
            packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, mc.level, 0);
         poseStack.popPose();
         poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));
      } else {
         poseStack.pushPose();
         poseStack.translate(0.0D, 0.45D, -0.45D);
         poseStack.scale(2.0F, 2.0F, 2.0F);
         poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
         poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
         mc.getItemRenderer().renderStatic(grabbedStack, ItemDisplayContext.GROUND,
            packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, mc.level, 0);
         poseStack.popPose();
      }
      poseStack.pushPose();
      if (!grabbedStack.isEmpty() || grabbed) {
         this.setGrabAngles();
      } else {
         this.setUngrabAngles();
      }
      poseStack.scale(scale, scale, scale);
      VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
      this.palm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
      poseStack.popPose();
      poseStack.popPose();
      poseStack.popPose();
   }
}
