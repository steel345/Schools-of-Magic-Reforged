package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelHuman<T extends LivingEntity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "human"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer head, armR, armL, legR, legL;
   public final MowzieModelRenderer body_outer, head_outer;
   public final MowzieModelRenderer armR_outer, armL_outer, legR_outer, legL_outer;
   public final MowzieModelRenderer cloak, hood, sleeveL, sleeveR;

   public ArmPose leftArmPose = ArmPose.EMPTY;
   public ArmPose rightArmPose = ArmPose.EMPTY;

   public float swingProgress = 0.0F;

   public ModelHuman(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.head = new MowzieModelRenderer(this.body.part.getChild("head"));
      this.armR = new MowzieModelRenderer(this.body.part.getChild("armR"));
      this.armL = new MowzieModelRenderer(this.body.part.getChild("armL"));
      this.legR = new MowzieModelRenderer(this.body.part.getChild("legR"));
      this.legL = new MowzieModelRenderer(this.body.part.getChild("legL"));
      this.body_outer = new MowzieModelRenderer(root.getChild("body_outer"));
      this.head_outer = new MowzieModelRenderer(root.getChild("head_outer"));
      this.armR_outer = new MowzieModelRenderer(root.getChild("armR_outer"));
      this.armL_outer = new MowzieModelRenderer(root.getChild("armL_outer"));
      this.legR_outer = new MowzieModelRenderer(root.getChild("legR_outer"));
      this.legL_outer = new MowzieModelRenderer(root.getChild("legL_outer"));
      this.cloak = new MowzieModelRenderer(root.getChild("cloak"));
      this.hood = new MowzieModelRenderer(root.getChild("hood"));
      this.sleeveL = new MowzieModelRenderer(root.getChild("sleeveL"));
      this.sleeveR = new MowzieModelRenderer(root.getChild("sleeveR"));
      this.parts.add(this.body); this.parts.add(this.head); this.parts.add(this.armR); this.parts.add(this.armL);
      this.parts.add(this.legR); this.parts.add(this.legL);
      this.parts.add(this.body_outer); this.parts.add(this.head_outer);
      this.parts.add(this.armR_outer); this.parts.add(this.armL_outer);
      this.parts.add(this.legR_outer); this.parts.add(this.legL_outer);
      this.parts.add(this.cloak); this.parts.add(this.hood); this.parts.add(this.sleeveL); this.parts.add(this.sleeveR);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      CubeDeformation inflate25 = new CubeDeformation(0.25F);
      CubeDeformation inflate50 = new CubeDeformation(0.5F);

      PartDefinition body = root.addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4),
         PartPose.ZERO);
      body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8),
         PartPose.ZERO);
      body.addOrReplaceChild("armR",
         CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4),
         PartPose.offset(5.0F, 2.0F, 0.0F));
      body.addOrReplaceChild("armL",
         CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4),
         PartPose.offset(-5.0F, 2.0F, 0.0F));
      body.addOrReplaceChild("legR",
         CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4),
         PartPose.offset(1.9F, 12.0F, 0.0F));
      body.addOrReplaceChild("legL",
         CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4),
         PartPose.offset(-1.9F, 12.0F, 0.0F));

      root.addOrReplaceChild("body_outer",
         CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, inflate25),
         PartPose.ZERO);
      root.addOrReplaceChild("head_outer",
         CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, inflate25),
         PartPose.ZERO);
      root.addOrReplaceChild("armR_outer",
         CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, inflate25),
         PartPose.offset(5.0F, 2.0F, 0.0F));
      root.addOrReplaceChild("armL_outer",
         CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, inflate25),
         PartPose.offset(-5.0F, 2.0F, 0.0F));
      root.addOrReplaceChild("legR_outer",
         CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, inflate25),
         PartPose.offset(1.9F, 12.0F, 0.0F));
      root.addOrReplaceChild("legL_outer",
         CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, inflate25),
         PartPose.offset(-1.9F, 12.0F, 0.0F));
      root.addOrReplaceChild("cloak",
         CubeListBuilder.create().texOffs(0, 64).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, inflate50),
         PartPose.ZERO);
      root.addOrReplaceChild("hood",
         CubeListBuilder.create().texOffs(30, 64).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, inflate50),
         PartPose.ZERO);
      root.addOrReplaceChild("sleeveL",
         CubeListBuilder.create().texOffs(46, 80).addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, inflate50),
         PartPose.offset(-5.0F, 2.0F, 0.0F));
      root.addOrReplaceChild("sleeveR",
         CubeListBuilder.create().texOffs(29, 80).addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, inflate50),
         PartPose.offset(5.0F, 2.0F, 0.0F));

      return LayerDefinition.create(mesh, 64, 96);
   }

   public void matchRotations(MowzieModelRenderer original, MowzieModelRenderer copy) {
      copy.setRotationAngles(original.part.xRot, original.part.yRot, original.part.zRot);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      this.faceTarget(this.head, 2.0F, netHeadYaw, headPitch);
      this.bob(this.body, 0.75F, 0.5F, false, limbSwing, limbSwingAmount);
      this.flap(this.armL, 0.125F, 0.125F, false, 0.0F, 0.125F, (float) entity.tickCount, 0.5F);
      this.flap(this.armR, 0.125F, 0.125F, true, 0.0F, -0.125F, (float) entity.tickCount, 0.5F);
      this.walk(this.armL, 0.125F, 0.125F, false, 0.0F, 0.125F, (float) entity.tickCount, 0.5F);
      this.walk(this.armR, 0.125F, 0.125F, true, 0.0F, -0.125F, (float) entity.tickCount, 0.5F);
      this.walk(this.armL, 1.125F, 1.0F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.armR, 1.125F, 1.0F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.legL, 1.125F, 1.0F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.legR, 1.125F, 1.0F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);

      switch (this.leftArmPose) {
         case EMPTY -> this.armL.part.yRot = 0.0F;
         case BLOCK -> {
            this.armL.part.xRot = this.armL.part.xRot * 0.5F - 0.9424779F;
            this.armL.part.yRot = (float) (Math.PI / 6);
         }
         case ITEM -> {
            this.armL.part.xRot = this.armL.part.xRot * 0.5F - (float) (Math.PI / 10);
            this.armL.part.yRot = 0.0F;
         }
         default -> {}
      }
      switch (this.rightArmPose) {
         case EMPTY -> this.armR.part.yRot = 0.0F;
         case BLOCK -> {
            this.armR.part.xRot = this.armR.part.xRot * 0.5F - 0.9424779F;
            this.armR.part.yRot = (float) (-Math.PI / 6);
         }
         case ITEM -> {
            this.armR.part.xRot = this.armR.part.xRot * 0.5F - (float) (Math.PI / 10);
            this.armR.part.yRot = 0.0F;
         }
         default -> {}
      }

      if (this.swingProgress > 0.0F) {
         HumanoidArm side = this.getMainHand(entity);
         MowzieModelRenderer arm = this.getArmForSide(side);
         float swingP = this.swingProgress;
         this.body.part.yRot = Mth.sin(Mth.sqrt(limbSwingAmount) * (float) (Math.PI * 2)) * 0.2F;
         if (side == HumanoidArm.LEFT) this.body.part.yRot *= -1.0F;

         this.armR.part.z = Mth.sin(this.body.part.yRot) * 5.0F;
         this.armR.part.x = -Mth.cos(this.body.part.yRot) * 5.0F;
         this.armR.part.z = -Mth.sin(this.body.part.yRot) * 5.0F;
         this.armR.part.x = Mth.cos(this.body.part.yRot) * 5.0F;
         this.armR.part.yRot += this.body.part.yRot;
         this.armR.part.yRot += this.body.part.yRot;
         this.armR.part.xRot += this.body.part.yRot;
         swingP = 1.0F - this.swingProgress;
         swingP *= swingP;
         swingP *= swingP;
         swingP = 1.0F - swingP;
         float swingP2 = Mth.sin(swingP * (float) Math.PI);
         float swingP3 = Mth.sin(this.swingProgress * (float) Math.PI) * -(this.head.part.xRot - 0.7F) * 0.75F;
         arm.part.xRot = (float) ((double) arm.part.xRot - ((double) swingP2 * 1.2 + (double) swingP3));
         arm.part.yRot += this.body.part.yRot * 2.0F;
         arm.part.zRot += Mth.sin(this.swingProgress * (float) Math.PI) * -0.4F;
      }

      if (entity.isShiftKeyDown()) {
         this.body.part.xRot = 0.5F;
         this.armR.part.xRot += 0.4F;
         this.armL.part.xRot += 0.4F;
         this.legR.part.z = 4.0F;
         this.legL.part.z = 4.0F;
         this.legR.part.y = 9.0F;
         this.legL.part.y = 9.0F;
         this.head.part.y = 1.0F;
      } else {
         this.body.part.xRot = 0.0F;
         this.legR.part.z = 0.1F;
         this.legL.part.z = 0.1F;
         this.legR.part.y = 12.0F;
         this.legL.part.y = 12.0F;
         this.head.part.y = 0.0F;
      }

      this.armR.part.zRot += Mth.cos((float) entity.tickCount * 0.09F) * 0.05F + 0.05F;
      this.armL.part.zRot -= Mth.cos((float) entity.tickCount * 0.09F) * 0.05F + 0.05F;
      this.armR.part.xRot += Mth.sin((float) entity.tickCount * 0.067F) * 0.05F;
      this.armL.part.xRot -= Mth.sin((float) entity.tickCount * 0.067F) * 0.05F;
      if (this.rightArmPose == ArmPose.BOW_AND_ARROW) {
         this.armR.part.yRot = -0.1F + this.head.part.yRot;
         this.armL.part.yRot = 0.1F + this.head.part.yRot + 0.4F;
         this.armR.part.xRot = (float) (-Math.PI / 2) + this.head.part.xRot;
         this.armL.part.xRot = (float) (-Math.PI / 2) + this.head.part.xRot;
      } else if (this.leftArmPose == ArmPose.BOW_AND_ARROW) {
         this.armR.part.yRot = -0.1F + this.head.part.yRot - 0.4F;
         this.armL.part.yRot = 0.1F + this.head.part.yRot;
         this.armR.part.xRot = (float) (-Math.PI / 2) + this.head.part.xRot;
         this.armL.part.xRot = (float) (-Math.PI / 2) + this.head.part.xRot;
      }

      this.matchRotations(this.body, this.body_outer);
      this.matchRotations(this.body, this.cloak);
      this.matchRotations(this.armL, this.armL_outer);
      this.matchRotations(this.armL, this.sleeveL);
      this.matchRotations(this.armR, this.armR_outer);
      this.matchRotations(this.armR, this.sleeveR);
      this.matchRotations(this.head, this.head_outer);
      this.matchRotations(this.head, this.hood);
      this.matchRotations(this.legL, this.legL_outer);
      this.matchRotations(this.legR, this.legR_outer);
   }

   protected MowzieModelRenderer getArmForSide(HumanoidArm side) {
      return side == HumanoidArm.LEFT ? this.armL : this.armR;
   }

   protected HumanoidArm getMainHand(LivingEntity entity) {
      HumanoidArm mainArm = entity.getMainArm();
      return entity.swingingArm == InteractionHand.MAIN_HAND ? mainArm : mainArm.getOpposite();
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {
      this.armR_outer.part.render(pose, buf, light, overlay, r, g, b, a);
      this.armL_outer.part.render(pose, buf, light, overlay, r, g, b, a);
      this.body_outer.part.render(pose, buf, light, overlay, r, g, b, a);
      this.hood.part.render(pose, buf, light, overlay, r, g, b, a);
      this.legL_outer.part.render(pose, buf, light, overlay, r, g, b, a);
      this.sleeveL.part.render(pose, buf, light, overlay, r, g, b, a);
      this.sleeveR.part.render(pose, buf, light, overlay, r, g, b, a);
      this.cloak.part.render(pose, buf, light, overlay, r, g, b, a);
      this.body.part.render(pose, buf, light, overlay, r, g, b, a);
      this.head_outer.part.render(pose, buf, light, overlay, r, g, b, a);
      this.legR_outer.part.render(pose, buf, light, overlay, r, g, b, a);
   }

   @OnlyIn(Dist.CLIENT)
   public static enum ArmPose {
      EMPTY, ITEM, BLOCK, BOW_AND_ARROW;
   }
}
