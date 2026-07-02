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

public class ModelNobleTree4<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "noble_tree4"), "main");

   public final MowzieModelRenderer base;
   public final MowzieModelRenderer brow;
   public final MowzieModelRenderer eyeR;
   public final MowzieModelRenderer eyeL;
   public final MowzieModelRenderer nose;
   public final MowzieModelRenderer mouthTop;
   public final MowzieModelRenderer mouthBottom;
   public final MowzieModelRenderer eyelids;
   public final MowzieModelRenderer eyelids_1;
   public final MowzieModelRenderer horn2;
   public final MowzieModelRenderer leavesR;

   public ModelNobleTree4(ModelPart root) {
      super(root);
      this.base = new MowzieModelRenderer(root.getChild("base"));
      this.brow = new MowzieModelRenderer(this.base.part.getChild("brow"));
      this.eyelids = new MowzieModelRenderer(this.brow.part.getChild("eyelids"));
      this.eyelids_1 = new MowzieModelRenderer(this.brow.part.getChild("eyelids_1"));
      this.eyeR = new MowzieModelRenderer(this.base.part.getChild("eyeR"));
      this.eyeL = new MowzieModelRenderer(this.base.part.getChild("eyeL"));
      this.nose = new MowzieModelRenderer(this.base.part.getChild("nose"));
      this.horn2 = new MowzieModelRenderer(this.nose.part.getChild("horn2"));
      this.mouthTop = new MowzieModelRenderer(this.base.part.getChild("mouthTop"));
      this.leavesR = new MowzieModelRenderer(this.mouthTop.part.getChild("leavesR"));
      this.mouthBottom = new MowzieModelRenderer(this.base.part.getChild("mouthBottom"));
      this.parts.add(this.base); this.parts.add(this.brow); this.parts.add(this.eyeR); this.parts.add(this.eyeL);
      this.parts.add(this.nose); this.parts.add(this.mouthTop); this.parts.add(this.mouthBottom);
      this.parts.add(this.eyelids); this.parts.add(this.eyelids_1); this.parts.add(this.horn2); this.parts.add(this.leavesR);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition base = mesh.getRoot().addOrReplaceChild("base",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -3.0F, 10, 14, 8),
         PartPose.offsetAndRotation(0.0F, 9.0F, 9.0F, -0.18203785F, 0.0F, 0.0F));
      PartDefinition brow = base.addOrReplaceChild("brow",
         CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.0F, -1.0F, 12, 9, 4),
         PartPose.offsetAndRotation(0.0F, 3.4F, -2.5F, -0.18203785F, 0.0F, 0.0F));
      brow.addOrReplaceChild("eyelids",
         CubeListBuilder.create().texOffs(75, 90).addBox(-4.5F, 0.0F, -1.0F, 9, 2, 4),
         PartPose.offsetAndRotation(0.0F, 1.1F, 0.6F, -0.5462881F, 0.0F, 0.0F));
      brow.addOrReplaceChild("eyelids_1",
         CubeListBuilder.create().texOffs(75, 90).addBox(-4.5F, 0.0F, -1.0F, 9, 2, 4),
         PartPose.offsetAndRotation(0.0F, 3.1F, -0.2F, 0.5462881F, 0.0F, 0.0F));
      base.addOrReplaceChild("eyeR",
         CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 0.0F, 3, 1, 2),
         PartPose.offset(-3.0F, 6.0F, -3.5F));
      base.addOrReplaceChild("eyeL",
         CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 0.0F, 3, 1, 2),
         PartPose.offset(2.0F, 6.0F, -3.5F));
      PartDefinition nose = base.addOrReplaceChild("nose",
         CubeListBuilder.create().texOffs(20, 80).addBox(-2.0F, -2.0F, -9.0F, 4, 4, 9),
         PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.22759093F, 0.091106184F, 0.0F));
      nose.addOrReplaceChild("horn2",
         CubeListBuilder.create().texOffs(32, 56).addBox(-1.5F, -5.0F, -1.5F, 3, 6, 3),
         PartPose.offsetAndRotation(0.8F, -0.6F, -6.1F, 0.7285004F, -0.045553092F, 0.63739425F));
      PartDefinition mouthTop = base.addOrReplaceChild("mouthTop",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, 0.0F, 5, 5, 3),
         PartPose.offsetAndRotation(0.0F, 7.0F, -3.5F, -0.22759093F, 0.0F, 0.0F));
      mouthTop.addOrReplaceChild("leavesR",
         CubeListBuilder.create().texOffs(60, 54).addBox(-4.0F, 0.0F, 0.0F, 8, 4, 0),
         PartPose.offsetAndRotation(0.0F, 2.1F, 0.0F, -0.27314404F, 0.045553092F, 0.0F));
      base.addOrReplaceChild("mouthBottom",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, -1.0F, 5, 2, 3),
         PartPose.offsetAndRotation(0.0F, 12.4F, -3.5F, 0.22759093F, 0.0F, 0.0F));
      return LayerDefinition.create(mesh, 100, 100);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float f1 = 0.5F;
      this.walk(this.horn2, 0.125F, 0.0625F, false, 0.25F, 0.25F, (float) entity.tickCount, f1);
      this.flap(this.horn2, 0.125F, 0.0625F, false, 0.25F, 0.0F, (float) entity.tickCount, f1);
   }
}
