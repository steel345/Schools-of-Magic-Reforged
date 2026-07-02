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

public class ModelNobleTree3<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "noble_tree3"), "main");

   public final MowzieModelRenderer base;
   public final MowzieModelRenderer brow, eyeR, eyeL, mouthTop, mouthBottom;
   public final MowzieModelRenderer leavesR, leavesR_1, leavesR_2, leavesR_3, leavesR_4, leavesR_5;
   public final MowzieModelRenderer horn1, horn1_1, eyelids, nose, eyelids_1, nose2, nose2_1;
   public final MowzieModelRenderer antR1, antR2, antR3, antR4, antR1_1, antL2, antL3, antL4;

   public ModelNobleTree3(ModelPart root) {
      super(root);
      this.base = new MowzieModelRenderer(root.getChild("base"));
      this.brow = new MowzieModelRenderer(this.base.part.getChild("brow"));
      this.eyelids = new MowzieModelRenderer(this.brow.part.getChild("eyelids"));
      this.eyelids_1 = new MowzieModelRenderer(this.brow.part.getChild("eyelids_1"));
      this.nose = new MowzieModelRenderer(this.brow.part.getChild("nose"));
      this.nose2 = new MowzieModelRenderer(this.nose.part.getChild("nose2"));
      this.nose2_1 = new MowzieModelRenderer(this.nose2.part.getChild("nose2_1"));
      this.eyeR = new MowzieModelRenderer(this.base.part.getChild("eyeR"));
      this.eyeL = new MowzieModelRenderer(this.base.part.getChild("eyeL"));
      this.mouthTop = new MowzieModelRenderer(this.base.part.getChild("mouthTop"));
      this.mouthBottom = new MowzieModelRenderer(this.base.part.getChild("mouthBottom"));
      this.leavesR = new MowzieModelRenderer(this.base.part.getChild("leavesR"));
      this.leavesR_1 = new MowzieModelRenderer(this.base.part.getChild("leavesR_1"));
      this.horn1 = new MowzieModelRenderer(this.base.part.getChild("horn1"));
      this.antR1 = new MowzieModelRenderer(this.horn1.part.getChild("antR1"));
      this.antR2 = new MowzieModelRenderer(this.antR1.part.getChild("antR2"));
      this.antR3 = new MowzieModelRenderer(this.antR2.part.getChild("antR3"));
      this.antR4 = new MowzieModelRenderer(this.antR2.part.getChild("antR4"));
      this.leavesR_2 = new MowzieModelRenderer(this.antR2.part.getChild("leavesR_2"));
      this.leavesR_3 = new MowzieModelRenderer(this.antR2.part.getChild("leavesR_3"));
      this.horn1_1 = new MowzieModelRenderer(this.base.part.getChild("horn1_1"));
      this.antR1_1 = new MowzieModelRenderer(this.horn1_1.part.getChild("antR1_1"));
      this.antL2 = new MowzieModelRenderer(this.antR1_1.part.getChild("antL2"));
      this.antL3 = new MowzieModelRenderer(this.antL2.part.getChild("antL3"));
      this.antL4 = new MowzieModelRenderer(this.antL2.part.getChild("antL4"));
      this.leavesR_4 = new MowzieModelRenderer(this.antL2.part.getChild("leavesR_4"));
      this.leavesR_5 = new MowzieModelRenderer(this.antL2.part.getChild("leavesR_5"));
      this.parts.add(this.base); this.parts.add(this.brow); this.parts.add(this.eyeR); this.parts.add(this.eyeL);
      this.parts.add(this.mouthTop); this.parts.add(this.mouthBottom);
      this.parts.add(this.leavesR); this.parts.add(this.leavesR_1); this.parts.add(this.leavesR_2);
      this.parts.add(this.leavesR_3); this.parts.add(this.leavesR_4); this.parts.add(this.leavesR_5);
      this.parts.add(this.horn1); this.parts.add(this.horn1_1); this.parts.add(this.eyelids); this.parts.add(this.nose);
      this.parts.add(this.eyelids_1); this.parts.add(this.nose2); this.parts.add(this.nose2_1);
      this.parts.add(this.antR1); this.parts.add(this.antR2); this.parts.add(this.antR3); this.parts.add(this.antR4);
      this.parts.add(this.antR1_1); this.parts.add(this.antL2); this.parts.add(this.antL3); this.parts.add(this.antL4);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition base = mesh.getRoot().addOrReplaceChild("base",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -3.0F, 10, 14, 8),
         PartPose.offsetAndRotation(0.0F, 9.0F, 9.0F, 0.0F, 0.0F, -0.091106184F));
      PartDefinition brow = base.addOrReplaceChild("brow",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7.0F, -1.0F, 7, 9, 4),
         PartPose.offsetAndRotation(0.0F, 3.4F, -2.5F, -0.091106184F, 0.0F, 0.0F));
      brow.addOrReplaceChild("eyelids",
         CubeListBuilder.create().texOffs(75, 90).addBox(-4.5F, 0.0F, -1.0F, 9, 2, 4),
         PartPose.offsetAndRotation(0.0F, 1.1F, 0.6F, -0.5462881F, 0.0F, 0.0F));
      brow.addOrReplaceChild("eyelids_1",
         CubeListBuilder.create().texOffs(75, 90).addBox(-4.5F, 0.0F, -1.0F, 9, 2, 4),
         PartPose.offsetAndRotation(0.0F, 3.1F, -0.2F, 0.5462881F, 0.0F, 0.0F));
      PartDefinition nose = brow.addOrReplaceChild("nose",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2, 11, 2),
         PartPose.offsetAndRotation(0.0F, 1.6F, -0.8F, -0.31869712F, 0.0F, 0.0F));
      PartDefinition nose2 = nose.addOrReplaceChild("nose2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 4, 1),
         PartPose.offsetAndRotation(0.0F, 8.0F, -0.4F, -0.59184116F, 0.0F, 0.0F));
      nose2.addOrReplaceChild("nose2_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 1, 4, 1),
         PartPose.offsetAndRotation(0.0F, 3.0F, -0.4F, -0.59184116F, 0.0F, 0.0F));
      base.addOrReplaceChild("eyeR",
         CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 0.0F, 3, 1, 2),
         PartPose.offset(-3.0F, 6.0F, -3.5F));
      base.addOrReplaceChild("eyeL",
         CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 0.0F, 3, 1, 2),
         PartPose.offset(2.0F, 6.0F, -3.5F));
      base.addOrReplaceChild("mouthTop",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5, 5, 3),
         PartPose.offsetAndRotation(0.0F, 7.0F, -3.5F, -0.22759093F, 0.0F, 0.0F));
      base.addOrReplaceChild("mouthBottom",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, -1.0F, 5, 5, 3),
         PartPose.offsetAndRotation(0.0F, 12.4F, -3.5F, 0.22759093F, 0.0F, 0.0F));
      base.addOrReplaceChild("leavesR",
         CubeListBuilder.create().texOffs(58, 54).addBox(-5.0F, 0.0F, 0.0F, 10, 6, 0),
         PartPose.offsetAndRotation(-2.2F, 11.1F, -2.0F, -0.4098033F, 0.31869712F, (float) (Math.PI / 3)));
      base.addOrReplaceChild("leavesR_1",
         CubeListBuilder.create().texOffs(58, 54).addBox(-5.0F, 0.0F, 0.0F, 10, 6, 0),
         PartPose.offsetAndRotation(2.2F, 11.1F, -2.0F, -0.4098033F, -0.31869712F, (float) (-Math.PI / 3)));
      PartDefinition horn1 = base.addOrReplaceChild("horn1",
         CubeListBuilder.create().texOffs(32, 56).addBox(-1.5F, -6.0F, -1.5F, 3, 7, 3),
         PartPose.offsetAndRotation(4.0F, 1.0F, -1.2F, 0.59184116F, 0.0F, 0.8196066F));
      PartDefinition antR1 = horn1.addOrReplaceChild("antR1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2, 4, 2),
         PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, -0.18203785F, 0.0F, 0.0F));
      PartDefinition antR2 = antR1.addOrReplaceChild("antR2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -6.0F, -0.5F, 1, 7, 1),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -0.22759093F, 0.0F, 0.4098033F));
      antR2.addOrReplaceChild("antR3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -0.5F, 4, 1, 1),
         PartPose.offsetAndRotation(0.5F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
      antR2.addOrReplaceChild("antR4",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -1.0F, 3, 1, 1),
         PartPose.offsetAndRotation(-0.5F, -3.0F, 0.5F, 0.0F, (float) (-Math.PI / 12), (float) (Math.PI / 9)));
      antR2.addOrReplaceChild("leavesR_2",
         CubeListBuilder.create().texOffs(58, 52).addBox(-5.0F, -11.0F, 0.0F, 10, 11, 0),
         PartPose.offset(-1.0F, 0.0F, 0.0F));
      antR2.addOrReplaceChild("leavesR_3",
         CubeListBuilder.create().texOffs(58, 53).addBox(-5.0F, -11.0F, 0.0F, 10, 11, 0),
         PartPose.offsetAndRotation(1.1F, 0.0F, 0.0F, 0.0F, 1.9577358F, -0.3642502F));
      PartDefinition horn1_1 = base.addOrReplaceChild("horn1_1",
         CubeListBuilder.create().texOffs(32, 56).addBox(-1.5F, -6.0F, -1.5F, 3, 7, 3),
         PartPose.offsetAndRotation(-4.0F, 1.0F, -1.2F, 0.59184116F, 0.0F, -0.8196066F));
      PartDefinition antR1_1 = horn1_1.addOrReplaceChild("antR1_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -1.0F, 2, 4, 2),
         PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, -0.18203785F, 0.0F, 0.0F));
      PartDefinition antL2 = antR1_1.addOrReplaceChild("antL2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -0.5F, 1, 6, 1),
         PartPose.offsetAndRotation(0.5F, -3.0F, 0.0F, -0.22759093F, 0.0F, -0.4098033F));
      antL2.addOrReplaceChild("antL3",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 4, 1, 1),
         PartPose.offsetAndRotation(-1.0F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 4)));
      antL2.addOrReplaceChild("antL4",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -1.0F, 3, 1, 1),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, (float) (Math.PI / 12), (float) (-Math.PI / 9)));
      antL2.addOrReplaceChild("leavesR_4",
         CubeListBuilder.create().texOffs(58, 52).addBox(-5.0F, -11.0F, 0.0F, 10, 11, 0),
         PartPose.offset(1.0F, 0.0F, 0.0F));
      antL2.addOrReplaceChild("leavesR_5",
         CubeListBuilder.create().texOffs(58, 53).addBox(-5.0F, -11.0F, 0.0F, 10, 11, 0),
         PartPose.offsetAndRotation(-1.1F, 0.0F, 0.0F, 0.0F, 1.4570009F, 0.3642502F));
      return LayerDefinition.create(mesh, 100, 100);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float f1 = 0.5F;
      float tick = (float) entity.tickCount;
      this.walk(this.horn1, 0.125F, 0.125F, false, 0.0F, 0.25F, tick, f1);
      this.walk(this.horn1_1, 0.125F, 0.125F, true, 0.0F, -0.25F, tick, f1);
      this.flap(this.horn1, 0.125F, 0.125F, false, 0.0F, 0.0F, tick, f1);
      this.flap(this.horn1_1, 0.125F, 0.125F, false, 0.0F, 0.0F, tick, f1);
      this.walk(this.nose, 0.125F, 0.0625F, false, 0.25F, 0.25F, tick, f1);
      this.flap(this.nose, 0.125F, 0.0625F, false, 0.25F, 0.0F, tick, f1);
   }
}
