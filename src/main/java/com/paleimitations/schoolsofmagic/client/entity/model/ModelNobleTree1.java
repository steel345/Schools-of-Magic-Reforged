package com.paleimitations.schoolsofmagic.client.entity.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ModelNobleTree1<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "noble_tree1"), "main");

   public final MowzieModelRenderer base;
   public final MowzieModelRenderer shoulderR, shoulderL;
   public final MowzieModelRenderer noseBase, horn, mouth;
   public final MowzieModelRenderer armR1, armR2, fingerR1, fingerR2, leavesR;
   public final MowzieModelRenderer armL1, fingerL1, fingerL2, leavesL1, leavesL2;
   public final MowzieModelRenderer nostrils, eyelids_upper, blinker, eyeR, eyeL, eyelids_lower;
   public final MowzieModelRenderer horn2, horn2_1;

   public ModelNobleTree1(ModelPart root) {
      super(root);
      this.base = new MowzieModelRenderer(root.getChild("base"));
      this.noseBase = new MowzieModelRenderer(this.base.part.getChild("noseBase"));
      this.eyelids_upper = new MowzieModelRenderer(this.noseBase.part.getChild("eyelids_upper"));
      this.eyeR = new MowzieModelRenderer(this.eyelids_upper.part.getChild("eyeR"));
      this.eyeL = new MowzieModelRenderer(this.eyelids_upper.part.getChild("eyeL"));
      this.blinker = new MowzieModelRenderer(this.noseBase.part.getChild("blinker"));
      this.eyelids_lower = new MowzieModelRenderer(this.blinker.part.getChild("eyelids_lower"));
      this.nostrils = new MowzieModelRenderer(this.noseBase.part.getChild("nostrils"));
      this.shoulderL = new MowzieModelRenderer(this.base.part.getChild("shoulderL"));
      this.armL1 = new MowzieModelRenderer(this.shoulderL.part.getChild("armL1"));
      this.leavesL1 = new MowzieModelRenderer(this.armL1.part.getChild("leavesL1"));
      this.leavesL2 = new MowzieModelRenderer(this.armL1.part.getChild("leavesL2"));
      this.fingerL1 = new MowzieModelRenderer(this.armL1.part.getChild("fingerL1"));
      this.fingerL2 = new MowzieModelRenderer(this.armL1.part.getChild("fingerL2"));
      this.shoulderR = new MowzieModelRenderer(this.base.part.getChild("shoulderR"));
      this.armR1 = new MowzieModelRenderer(this.shoulderR.part.getChild("armR1"));
      this.armR2 = new MowzieModelRenderer(this.armR1.part.getChild("armR2"));
      this.fingerR1 = new MowzieModelRenderer(this.armR2.part.getChild("fingerR1"));
      this.fingerR2 = new MowzieModelRenderer(this.armR2.part.getChild("fingerR2"));
      this.leavesR = new MowzieModelRenderer(this.armR2.part.getChild("leavesR"));
      this.mouth = new MowzieModelRenderer(this.base.part.getChild("mouth"));
      this.horn = new MowzieModelRenderer(this.base.part.getChild("horn"));
      this.horn2 = new MowzieModelRenderer(this.horn.part.getChild("horn2"));
      this.horn2_1 = new MowzieModelRenderer(this.horn.part.getChild("horn2_1"));
      this.parts.add(this.base); this.parts.add(this.shoulderR); this.parts.add(this.shoulderL);
      this.parts.add(this.noseBase); this.parts.add(this.horn); this.parts.add(this.mouth);
      this.parts.add(this.armR1); this.parts.add(this.armR2); this.parts.add(this.fingerR1); this.parts.add(this.fingerR2);
      this.parts.add(this.leavesR); this.parts.add(this.armL1); this.parts.add(this.fingerL1); this.parts.add(this.fingerL2);
      this.parts.add(this.leavesL1); this.parts.add(this.leavesL2); this.parts.add(this.nostrils);
      this.parts.add(this.eyelids_upper); this.parts.add(this.blinker); this.parts.add(this.eyeR); this.parts.add(this.eyeL);
      this.parts.add(this.eyelids_lower); this.parts.add(this.horn2); this.parts.add(this.horn2_1);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition base = mesh.getRoot().addOrReplaceChild("base",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10, 10, 10),
         PartPose.offsetAndRotation(-0.6F, 16.0F, 12.0F, 0.0F, 0.0F, 0.13665928F));
      PartDefinition noseBase = base.addOrReplaceChild("noseBase",
         CubeListBuilder.create().texOffs(75, 90).addBox(-1.0F, -3.5F, -2.0F, 2, 5, 2),
         PartPose.offset(0.0F, -1.0F, -5.0F));
      PartDefinition eyelids_upper = noseBase.addOrReplaceChild("eyelids_upper",
         CubeListBuilder.create().texOffs(74, 90).addBox(-3.5F, -3.0F, 0.0F, 7, 3, 2),
         PartPose.offsetAndRotation(0.0F, -1.6F, -1.0F, -0.22759093F, 0.0F, 0.045553092F));
      eyelids_upper.addOrReplaceChild("eyeR",
         CubeListBuilder.create().texOffs(1, 49).addBox(0.0F, 0.0F, 0.0F, 2, 1, 2),
         PartPose.offsetAndRotation(1.3F, -0.2F, 0.3F, 0.22759093F, 0.0F, 0.0F));
      eyelids_upper.addOrReplaceChild("eyeL",
         CubeListBuilder.create().texOffs(0, 49).addBox(-2.0F, 0.0F, 0.0F, 2, 1, 2),
         PartPose.offsetAndRotation(-1.2F, -0.2F, 0.3F, 0.22759093F, 0.0F, 0.0F));
      PartDefinition blinker = noseBase.addOrReplaceChild("blinker",
         CubeListBuilder.create().texOffs(50, 50).addBox(0.0F, 0.0F, 0.0F, 1, 1, 1),
         PartPose.offset(0.0F, 0.8F, 0.0F));
      blinker.addOrReplaceChild("eyelids_lower",
         CubeListBuilder.create().texOffs(75, 90).addBox(-3.5F, -1.0F, 0.0F, 7, 1, 2),
         PartPose.offsetAndRotation(0.0F, -0.6F, -0.9F, 0.091106184F, 0.0F, 0.045553092F));
      noseBase.addOrReplaceChild("nostrils",
         CubeListBuilder.create().texOffs(75, 90).addBox(-0.5F, -0.5F, -0.5F, 3, 1, 1),
         PartPose.offset(-1.0F, 0.8F, -0.5F));
      PartDefinition shoulderL = base.addOrReplaceChild("shoulderL",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -10.0F, -1.0F, 3, 3, 3),
         PartPose.offsetAndRotation(-5.0F, 4.1F, -5.0F, 0.0F, 0.0F, -0.091106184F));
      PartDefinition armL1 = shoulderL.addOrReplaceChild("armL1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 3, 10, 3),
         PartPose.offsetAndRotation(-0.3F, -9.4F, 0.0F, 0.91053826F, 0.0F, -0.63739425F));
      armL1.addOrReplaceChild("leavesL1",
         CubeListBuilder.create().texOffs(50, 49).addBox(-3.0F, -16.0F, -1.0F, 18, 16, 0),
         PartPose.offsetAndRotation(0.5F, -7.0F, -0.7F, -0.18203785F, -2.048842F, 0.8651597F));
      armL1.addOrReplaceChild("leavesL2",
         CubeListBuilder.create().texOffs(53, 50).addBox(0.0F, -19.0F, -1.0F, 15, 14, 0),
         PartPose.offsetAndRotation(-2.0F, -7.0F, -0.7F, -0.4098033F, -0.68294734F, 0.18203785F));
      armL1.addOrReplaceChild("fingerL1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -10.0F, -1.0F, 2, 10, 2),
         PartPose.offsetAndRotation(0.5F, -7.0F, 0.2F, -0.68294734F, 0.31869712F, 0.091106184F));
      armL1.addOrReplaceChild("fingerL2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -1.0F, 1, 7, 1),
         PartPose.offsetAndRotation(0.5F, -7.0F, 0.2F, -0.18203785F, -2.048842F, 0.8651597F));
      PartDefinition shoulderR = base.addOrReplaceChild("shoulderR",
         CubeListBuilder.create().texOffs(50, 0).addBox(3.0F, -1.2F, -1.0F, 3, 3, 3),
         PartPose.offsetAndRotation(0.0F, 3.3F, -5.0F, -0.5009095F, 0.0F, 0.0F));
      PartDefinition armR1 = shoulderR.addOrReplaceChild("armR1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -5.0F, 2, 2, 6),
         PartPose.offsetAndRotation(4.7F, 0.2F, 0.5F, 0.3642502F, -0.63739425F, 0.0F));
      PartDefinition armR2 = armR1.addOrReplaceChild("armR2",
         CubeListBuilder.create().texOffs(50, 0).addBox(-0.5F, -1.0F, -7.0F, 1, 1, 7),
         PartPose.offsetAndRotation(0.5F, 0.5F, -5.0F, -0.8196066F, 0.0F, 0.0F));
      armR2.addOrReplaceChild("fingerR1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -3.0F, 1, 1, 3),
         PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, -0.3642502F, 0.31869712F, 0.0F));
      armR2.addOrReplaceChild("fingerR2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -5.0F, 1, 1, 5),
         PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, -0.59184116F, 0.95609134F, 0.0F));
      armR2.addOrReplaceChild("leavesR",
         CubeListBuilder.create().texOffs(46, 50).addBox(-3.4F, -1.0F, -8.0F, 10, 0, 12),
         PartPose.offsetAndRotation(0.0F, 0.0F, -7.0F, 0.3642502F, 1.548107F, 0.91053826F));
      base.addOrReplaceChild("mouth",
         CubeListBuilder.create().texOffs(0, 73).addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4),
         PartPose.offsetAndRotation(0.0F, 0.5F, -3.7F, -0.22759093F, 0.0F, 0.0F));
      PartDefinition horn = base.addOrReplaceChild("horn",
         CubeListBuilder.create().texOffs(0, 49).addBox(-3.5F, -11.0F, -5.0F, 8, 11, 8),
         PartPose.offsetAndRotation(0.0F, -5.2F, -2.4F, 0.4098033F, 0.091106184F, 0.22759093F));
      horn.addOrReplaceChild("horn2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -9.0F, -1.5F, 3, 9, 3),
         PartPose.offset(-1.8F, 0.0F, -1.3F));
      horn.addOrReplaceChild("horn2_1",
         CubeListBuilder.create().texOffs(32, 56).addBox(-3.0F, -9.0F, -1.5F, 3, 9, 3),
         PartPose.offsetAndRotation(4.9F, 0.0F, -0.4F, 0.0F, 0.0F, 0.4098033F));
      return LayerDefinition.create(mesh, 100, 100);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float f1 = 0.5F;
      float tick = (float) entity.tickCount;
      this.walk(this.armL1, 0.125F, 0.125F, false, 0.0F, 0.25F, tick, f1);
      this.walk(this.armR1, 0.125F, 0.125F, true, 0.0F, -0.25F, tick, f1);
      this.flap(this.armL1, 0.125F, 0.125F, false, 0.0F, 0.0F, tick, f1);
      this.flap(this.armR1, 0.125F, 0.125F, false, 0.0F, 0.0F, tick, f1);
      this.walk(this.fingerL1, 0.125F, 0.0625F, false, 0.25F, 0.25F, tick, f1);
      this.walk(this.armR2, 0.125F, 0.0625F, true, 0.25F, -0.25F, tick, f1);
      this.flap(this.fingerL1, 0.125F, 0.0625F, false, 0.25F, 0.0F, tick, f1);
      this.flap(this.armR2, 0.125F, 0.0625F, false, 0.25F, 0.0F, tick, f1);
   }
}
