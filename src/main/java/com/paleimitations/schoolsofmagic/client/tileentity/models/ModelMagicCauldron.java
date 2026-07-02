package com.paleimitations.schoolsofmagic.client.tileentity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.MowzieModelBase;
import com.paleimitations.schoolsofmagic.client.entity.model.MowzieModelRenderer;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelMagicCauldron extends MowzieModelBase<Entity> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "magic_cauldron"), "main");

   public final MowzieModelRenderer bottom;
   public final MowzieModelRenderer stir;
   public final MowzieModelRenderer lion_w;
   public final MowzieModelRenderer lion_e;
   public final MowzieModelRenderer side_s;
   public final MowzieModelRenderer side_n;
   public final MowzieModelRenderer side_e;
   public final MowzieModelRenderer side_w;
   public final MowzieModelRenderer leg_1;
   public final MowzieModelRenderer leg_2;
   public final MowzieModelRenderer leg_3;
   public final MowzieModelRenderer leg_4;
   public final MowzieModelRenderer big_toe_1, toe_r_1, toe_l_1, toenail_c_1, toenail_r_1, toenail_l_1;
   public final MowzieModelRenderer big_toe_2, toe_r_2, toe_l_2, toenail_c_2, toenail_r_2, toenail_l_2;
   public final MowzieModelRenderer big_toe_3, toe_r_3, toe_l_3, toenail_c_3, toenail_r_3, toenail_l_3;
   public final MowzieModelRenderer big_toe_4, toe_r_4, toe_l_4, toenail_c_4, toenail_r_4, toenail_l_4;
   public final MowzieModelRenderer spoon;
   public final MowzieModelRenderer snout_w, mane_w, handle_base_w, handle_side1_w, handle_side2_w, handle_bottom_w;
   public final MowzieModelRenderer snout_e, mane_e, handle_base_e, handle_side1_e, handle_side2_e, handle_bottom_e;
   public final MowzieModelRenderer lid, lid2, handle_t1, handle_t2, handle_t3;

   public ModelMagicCauldron(ModelPart root) {
      super(root);
      this.bottom = new MowzieModelRenderer(root.getChild("bottom"));
      this.stir = new MowzieModelRenderer(root.getChild("stir"));
      this.lid = new MowzieModelRenderer(root.getChild("lid"));
      this.lion_w = new MowzieModelRenderer(root.getChild("lion_w"));
      this.lion_e = new MowzieModelRenderer(root.getChild("lion_e"));

      this.lid2 = new MowzieModelRenderer(this.lid.part.getChild("lid2"));
      this.handle_t1 = new MowzieModelRenderer(this.lid2.part.getChild("handle_t1"));
      this.handle_t2 = new MowzieModelRenderer(this.lid2.part.getChild("handle_t2"));
      this.handle_t3 = new MowzieModelRenderer(this.lid2.part.getChild("handle_t3"));

      this.side_s = new MowzieModelRenderer(this.bottom.part.getChild("side_s"));
      this.side_n = new MowzieModelRenderer(this.bottom.part.getChild("side_n"));
      this.side_e = new MowzieModelRenderer(this.bottom.part.getChild("side_e"));
      this.side_w = new MowzieModelRenderer(this.bottom.part.getChild("side_w"));

      this.leg_1 = new MowzieModelRenderer(this.bottom.part.getChild("leg_1"));
      this.leg_2 = new MowzieModelRenderer(this.bottom.part.getChild("leg_2"));
      this.leg_3 = new MowzieModelRenderer(this.bottom.part.getChild("leg_3"));
      this.leg_4 = new MowzieModelRenderer(this.bottom.part.getChild("leg_4"));

      this.big_toe_1 = new MowzieModelRenderer(this.leg_1.part.getChild("big_toe_1"));
      this.toe_r_1 = new MowzieModelRenderer(this.leg_1.part.getChild("toe_r_1"));
      this.toe_l_1 = new MowzieModelRenderer(this.leg_1.part.getChild("toe_l_1"));
      this.toenail_c_1 = new MowzieModelRenderer(this.big_toe_1.part.getChild("toenail_c_1"));
      this.toenail_r_1 = new MowzieModelRenderer(this.toe_r_1.part.getChild("toenail_r_1"));
      this.toenail_l_1 = new MowzieModelRenderer(this.toe_l_1.part.getChild("toenail_l_1"));

      this.big_toe_2 = new MowzieModelRenderer(this.leg_2.part.getChild("big_toe_2"));
      this.toe_r_2 = new MowzieModelRenderer(this.leg_2.part.getChild("toe_r_2"));
      this.toe_l_2 = new MowzieModelRenderer(this.leg_2.part.getChild("toe_l_2"));
      this.toenail_c_2 = new MowzieModelRenderer(this.big_toe_2.part.getChild("toenail_c_2"));
      this.toenail_r_2 = new MowzieModelRenderer(this.toe_r_2.part.getChild("toenail_r_2"));
      this.toenail_l_2 = new MowzieModelRenderer(this.toe_l_2.part.getChild("toenail_l_2"));

      this.big_toe_3 = new MowzieModelRenderer(this.leg_3.part.getChild("big_toe_3"));
      this.toe_r_3 = new MowzieModelRenderer(this.leg_3.part.getChild("toe_r_3"));
      this.toe_l_3 = new MowzieModelRenderer(this.leg_3.part.getChild("toe_l_3"));
      this.toenail_c_3 = new MowzieModelRenderer(this.big_toe_3.part.getChild("toenail_c_3"));
      this.toenail_r_3 = new MowzieModelRenderer(this.toe_r_3.part.getChild("toenail_r_3"));
      this.toenail_l_3 = new MowzieModelRenderer(this.toe_l_3.part.getChild("toenail_l_3"));

      this.big_toe_4 = new MowzieModelRenderer(this.leg_4.part.getChild("big_toe_4"));
      this.toe_r_4 = new MowzieModelRenderer(this.leg_4.part.getChild("toe_r_4"));
      this.toe_l_4 = new MowzieModelRenderer(this.leg_4.part.getChild("toe_l_4"));
      this.toenail_c_4 = new MowzieModelRenderer(this.big_toe_4.part.getChild("toenail_c_4"));
      this.toenail_r_4 = new MowzieModelRenderer(this.toe_r_4.part.getChild("toenail_r_4"));
      this.toenail_l_4 = new MowzieModelRenderer(this.toe_l_4.part.getChild("toenail_l_4"));

      this.spoon = new MowzieModelRenderer(this.stir.part.getChild("spoon"));

      this.snout_w = new MowzieModelRenderer(this.lion_w.part.getChild("snout_w"));
      this.mane_w = new MowzieModelRenderer(this.lion_w.part.getChild("mane_w"));
      this.handle_base_w = new MowzieModelRenderer(this.snout_w.part.getChild("handle_base_w"));
      this.handle_side1_w = new MowzieModelRenderer(this.handle_base_w.part.getChild("handle_side1_w"));
      this.handle_side2_w = new MowzieModelRenderer(this.handle_base_w.part.getChild("handle_side2_w"));
      this.handle_bottom_w = new MowzieModelRenderer(this.handle_base_w.part.getChild("handle_bottom_w"));

      this.snout_e = new MowzieModelRenderer(this.lion_e.part.getChild("snout_e"));
      this.mane_e = new MowzieModelRenderer(this.lion_e.part.getChild("mane_e"));
      this.handle_base_e = new MowzieModelRenderer(this.snout_e.part.getChild("handle_base_e"));
      this.handle_side1_e = new MowzieModelRenderer(this.handle_base_e.part.getChild("handle_side1_e"));
      this.handle_side2_e = new MowzieModelRenderer(this.handle_base_e.part.getChild("handle_side2_e"));
      this.handle_bottom_e = new MowzieModelRenderer(this.handle_base_e.part.getChild("handle_bottom_e"));

      MowzieModelRenderer[] all = {
         lid, lid2, handle_t1, handle_t2, handle_t3, bottom, stir, lion_w, lion_e,
         side_s, side_n, side_e, side_w,
         leg_1, leg_2, leg_3, leg_4,
         big_toe_1, toe_r_1, toe_l_1, toenail_c_1, toenail_r_1, toenail_l_1,
         big_toe_2, toe_r_2, toe_l_2, toenail_c_2, toenail_r_2, toenail_l_2,
         big_toe_3, toe_r_3, toe_l_3, toenail_c_3, toenail_r_3, toenail_l_3,
         big_toe_4, toe_r_4, toe_l_4, toenail_c_4, toenail_r_4, toenail_l_4,
         spoon,
         snout_w, mane_w, handle_base_w, handle_side1_w, handle_side2_w, handle_bottom_w,
         snout_e, mane_e, handle_base_e, handle_side1_e, handle_side2_e, handle_bottom_e
      };
      for (MowzieModelRenderer r : all) {
         this.parts.add(r);
         r.setInitValuesToCurrentPose();
      }
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition lid = root.addOrReplaceChild("lid",
         CubeListBuilder.create().texOffs(24, 53).addBox(-5.0F, 0.0F, -5.0F, 10, 1, 10),
         PartPose.offset(0.0F, 11.5F, 0.0F));
      PartDefinition lid2 = lid.addOrReplaceChild("lid2",
         CubeListBuilder.create().texOffs(0, 52).addBox(-4.0F, -0.5F, -4.0F, 8, 1, 8),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      lid2.addOrReplaceChild("handle_t1",
         CubeListBuilder.create().texOffs(43, 17).addBox(-0.5F, -1.0F, -0.5F, 4, 1, 1),
         PartPose.offset(-1.5F, -1.0F, 0.0F));
      lid2.addOrReplaceChild("handle_t2",
         CubeListBuilder.create().texOffs(46, 15).addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1),
         PartPose.offset(-1.5F, 0.0F, 0.0F));
      lid2.addOrReplaceChild("handle_t3",
         CubeListBuilder.create().texOffs(46, 15).addBox(-0.5F, -1.0F, -0.5F, 1, 1, 1),
         PartPose.offset(1.5F, 0.0F, 0.0F));

      PartDefinition stir = root.addOrReplaceChild("stir",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(0.0F, 18.0F, 0.0F));
      stir.addOrReplaceChild("spoon",
         CubeListBuilder.create().texOffs(5, 0).addBox(-0.5F, -10.0F, -0.5F, 1, 12, 1),
         PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.4098033F));

      PartDefinition lion_w = root.addOrReplaceChild("lion_w",
         CubeListBuilder.create().texOffs(38, 4).addBox(0.0F, 0.0F, -2.5F, 2, 3, 5),
         PartPose.offsetAndRotation(-5.2F, 14.8F, 0.0F, 0.0F, (float)Math.PI, 0.4098033F));
      lion_w.addOrReplaceChild("mane_w",
         CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.3F, -4.5F, 1, 8, 9),
         PartPose.offsetAndRotation(1.5F, -1.5F, 0.0F, 0.0F, 0.0F, 0.4098033F));
      PartDefinition snout_w = lion_w.addOrReplaceChild("snout_w",
         CubeListBuilder.create().texOffs(31, 2).addBox(0.0F, 3.0F, -2.0F, 2, 2, 4),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition handle_base_w = snout_w.addOrReplaceChild("handle_base_w",
         CubeListBuilder.create().texOffs(11, 9).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5),
         PartPose.offsetAndRotation(0.7F, 4.3F, 0.0F, 0.0F, 0.0F, 0.3642502F));
      handle_base_w.addOrReplaceChild("handle_side1_w",
         CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1),
         PartPose.offset(0.0F, -0.5F, 3.0F));
      handle_base_w.addOrReplaceChild("handle_side2_w",
         CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1),
         PartPose.offset(0.0F, -0.5F, -3.0F));
      handle_base_w.addOrReplaceChild("handle_bottom_w",
         CubeListBuilder.create().texOffs(11, 9).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5),
         PartPose.offset(0.0F, 3.0F, 0.0F));

      PartDefinition lion_e = root.addOrReplaceChild("lion_e",
         CubeListBuilder.create().texOffs(38, 4).addBox(0.0F, 0.0F, -2.5F, 2, 3, 5),
         PartPose.offsetAndRotation(5.2F, 14.8F, 0.0F, 0.0F, 0.0F, -0.4098033F));
      lion_e.addOrReplaceChild("mane_e",
         CubeListBuilder.create().texOffs(0, 32).addBox(0.0F, 0.3F, -4.5F, 1, 8, 9),
         PartPose.offsetAndRotation(1.5F, -1.5F, 0.0F, 0.0F, 0.0F, 0.4098033F));
      PartDefinition snout_e = lion_e.addOrReplaceChild("snout_e",
         CubeListBuilder.create().texOffs(31, 2).addBox(0.0F, 3.0F, -2.0F, 2, 2, 4),
         PartPose.offset(0.0F, 0.0F, 0.0F));
      PartDefinition handle_base_e = snout_e.addOrReplaceChild("handle_base_e",
         CubeListBuilder.create().texOffs(11, 9).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5),
         PartPose.offsetAndRotation(0.7F, 4.3F, 0.0F, 0.0F, 0.0F, 0.3642502F));
      handle_base_e.addOrReplaceChild("handle_side1_e",
         CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1),
         PartPose.offset(0.0F, -0.5F, 3.0F));
      handle_base_e.addOrReplaceChild("handle_side2_e",
         CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1),
         PartPose.offset(0.0F, -0.5F, -3.0F));
      handle_base_e.addOrReplaceChild("handle_bottom_e",
         CubeListBuilder.create().texOffs(11, 9).addBox(-0.5F, -0.5F, -2.5F, 1, 1, 5),
         PartPose.offset(0.0F, 3.0F, 0.0F));

      PartDefinition bottom = root.addOrReplaceChild("bottom",
         CubeListBuilder.create().texOffs(24, 21).addBox(-5.0F, -0.5F, -5.0F, 10, 1, 10),
         PartPose.offset(0.0F, 21.0F, 0.0F));

      bottom.addOrReplaceChild("side_s",
         CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, -8.0F, 0.0F, 10, 8, 1),
         PartPose.offset(0.0F, -0.5F, 5.0F));
      bottom.addOrReplaceChild("side_n",
         CubeListBuilder.create().texOffs(0, 23).addBox(-5.0F, -8.0F, -1.0F, 10, 8, 1),
         PartPose.offset(0.0F, -0.5F, -5.0F));
      bottom.addOrReplaceChild("side_e",
         CubeListBuilder.create().texOffs(42, 33).addBox(-1.0F, -8.0F, -5.0F, 1, 8, 10),
         PartPose.offset(-5.0F, -0.5F, 0.0F));
      bottom.addOrReplaceChild("side_w",
         CubeListBuilder.create().texOffs(42, 2).addBox(0.0F, -8.0F, -5.0F, 1, 8, 10),
         PartPose.offset(5.0F, -0.5F, 0.0F));

      addLeg(bottom, "leg_1", 3.5F, 0.0F, 3.5F, 0.0F, 0.0F, 0.0F, "1");
      addLeg(bottom, "leg_2", 3.5F, 0.0F, -3.5F, 0.0F, 1.5707964F, 0.0F, "2");
      addLeg(bottom, "leg_3", -3.5F, 0.0F, 3.5F, 0.0F, -1.5707964F, 0.0F, "3");
      addLeg(bottom, "leg_4", -3.5F, 0.0F, -3.5F, 0.0F, (float)Math.PI, 0.0F, "4");

      return LayerDefinition.create(mesh, 64, 64);
   }

   private static void addLeg(PartDefinition parent, String name, float px, float py, float pz, float rx, float ry, float rz, String suffix) {
      PartDefinition leg = parent.addOrReplaceChild(name,
         CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 3, 3, 3),
         PartPose.offsetAndRotation(px, py, pz, rx, ry, rz));
      PartDefinition toe_r = leg.addOrReplaceChild("toe_r_" + suffix,
         CubeListBuilder.create().texOffs(20, 16).addBox(-1.0F, 0.0F, 0.0F, 2, 1, 1),
         PartPose.offset(-1.0F, 2.0F, 1.0F));
      toe_r.addOrReplaceChild("toenail_r_" + suffix,
         CubeListBuilder.create().texOffs(26, 16).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(0.0F, 0.4F, 1.3F));
      PartDefinition toe_l = leg.addOrReplaceChild("toe_l_" + suffix,
         CubeListBuilder.create().texOffs(20, 18).addBox(0.0F, 0.0F, -1.0F, 1, 1, 2),
         PartPose.offset(1.0F, 2.0F, -1.0F));
      toe_l.addOrReplaceChild("toenail_l_" + suffix,
         CubeListBuilder.create().texOffs(26, 16).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(1.3F, 0.4F, 0.0F));
      PartDefinition big_toe = leg.addOrReplaceChild("big_toe_" + suffix,
         CubeListBuilder.create().texOffs(12, 16).addBox(-1.0F, -0.1F, -1.0F, 2, 2, 2),
         PartPose.offsetAndRotation(1.0F, 1.0F, 1.0F, 0.0F, 0.7853982F, 0.0F));
      big_toe.addOrReplaceChild("toenail_c_" + suffix,
         CubeListBuilder.create().texOffs(26, 16).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
         PartPose.offset(0.0F, 1.3F, 1.2F));
   }

   public void setAngles() {
      for (MowzieModelRenderer r : this.parts) {
         r.setCurrentPoseToInitValues();
      }
   }

   public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float tick, float partialTicks, TileEntityCauldron te) {
      this.setAngles();

      poseStack.pushPose();
      poseStack.translate(this.lion_w.part.x / 16.0F, this.lion_w.part.y / 16.0F, this.lion_w.part.z / 16.0F);
      poseStack.scale(0.8F, 0.8F, 0.8F);
      poseStack.translate(-this.lion_w.part.x / 16.0F, -this.lion_w.part.y / 16.0F, -this.lion_w.part.z / 16.0F);
      this.lion_w.part.render(poseStack, buffer, packedLight, packedOverlay);
      poseStack.popPose();

      poseStack.pushPose();
      poseStack.translate(this.lion_e.part.x / 16.0F, this.lion_e.part.y / 16.0F, this.lion_e.part.z / 16.0F);
      poseStack.scale(0.8F, 0.8F, 0.8F);
      poseStack.translate(-this.lion_e.part.x / 16.0F, -this.lion_e.part.y / 16.0F, -this.lion_e.part.z / 16.0F);
      this.lion_e.part.render(poseStack, buffer, packedLight, packedOverlay);
      poseStack.popPose();

      this.bottom.part.render(poseStack, buffer, packedLight, packedOverlay);

      if (te.getCounter() < te.getStirTickMax() && te.getPhase() == TileEntityCauldron.EnumPotionPhase.STIRRING) {
         this.stir.part.yRot = (tick + partialTicks) * 0.3F % 360.0F;
         this.spoon.part.zRot = (float)(0.20490165054798126D + Math.sin((tick + partialTicks) * 0.2F) * 0.4098033010959625D / 2.0D);
      }
      if (te.isLidded()) {
         this.lid.part.render(poseStack, buffer, packedLight, packedOverlay);
      } else {
         this.stir.part.render(poseStack, buffer, packedLight, packedOverlay);
      }
   }

   @Override
   public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

   }
}
