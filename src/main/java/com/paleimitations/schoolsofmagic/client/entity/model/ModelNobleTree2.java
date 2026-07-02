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

public class ModelNobleTree2<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "noble_tree2"), "main");

   public final MowzieModelRenderer base;
   public final MowzieModelRenderer horn;
   public final MowzieModelRenderer nose;
   public final MowzieModelRenderer brow;
   public final MowzieModelRenderer cheeks;
   public final MowzieModelRenderer lips;
   public final MowzieModelRenderer eyeR;
   public final MowzieModelRenderer eyeL;
   public final MowzieModelRenderer lips_1;
   public final MowzieModelRenderer horn_1;
   public final MowzieModelRenderer horn_2;
   public final MowzieModelRenderer horn_3;
   public final MowzieModelRenderer eyelids;
   public final MowzieModelRenderer horn_4;
   public final MowzieModelRenderer horn_5;

   public ModelNobleTree2(ModelPart root) {
      super(root);
      this.base = new MowzieModelRenderer(root.getChild("base"));
      this.eyeR = new MowzieModelRenderer(this.base.part.getChild("eyeR"));
      this.eyeL = new MowzieModelRenderer(this.base.part.getChild("eyeL"));
      this.lips = new MowzieModelRenderer(this.base.part.getChild("lips"));
      this.lips_1 = new MowzieModelRenderer(this.base.part.getChild("lips_1"));
      this.cheeks = new MowzieModelRenderer(this.base.part.getChild("cheeks"));
      this.nose = new MowzieModelRenderer(this.base.part.getChild("nose"));
      this.horn = new MowzieModelRenderer(this.base.part.getChild("horn"));
      this.horn_2 = new MowzieModelRenderer(this.horn.part.getChild("horn_2"));
      this.horn_3 = new MowzieModelRenderer(this.horn.part.getChild("horn_3"));
      this.brow = new MowzieModelRenderer(this.base.part.getChild("brow"));
      this.eyelids = new MowzieModelRenderer(this.brow.part.getChild("eyelids"));
      this.horn_1 = new MowzieModelRenderer(this.base.part.getChild("horn_1"));
      this.horn_4 = new MowzieModelRenderer(this.horn_1.part.getChild("horn_4"));
      this.horn_5 = new MowzieModelRenderer(this.horn_4.part.getChild("horn_5"));
      this.parts.add(this.base); this.parts.add(this.horn); this.parts.add(this.nose); this.parts.add(this.brow);
      this.parts.add(this.cheeks); this.parts.add(this.lips); this.parts.add(this.eyeR); this.parts.add(this.eyeL);
      this.parts.add(this.lips_1); this.parts.add(this.horn_1); this.parts.add(this.horn_2); this.parts.add(this.horn_3);
      this.parts.add(this.eyelids); this.parts.add(this.horn_4); this.parts.add(this.horn_5);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition base = mesh.getRoot().addOrReplaceChild("base",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 0.0F, -3.0F, 10, 14, 8),
         PartPose.offset(0.0F, 9.0F, 9.0F));
      base.addOrReplaceChild("eyeR",
         CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 0.0F, 3, 1, 2),
         PartPose.offset(-3.0F, 6.0F, -3.5F));
      base.addOrReplaceChild("eyeL",
         CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, 0.0F, 0.0F, 3, 1, 2),
         PartPose.offset(2.0F, 6.0F, -3.5F));
      base.addOrReplaceChild("lips",
         CubeListBuilder.create().texOffs(79, 90).addBox(-2.0F, -1.0F, -1.0F, 4, 2, 3),
         PartPose.offsetAndRotation(0.0F, 12.0F, -2.0F, 0.5009095F, 0.0F, 0.0F));
      base.addOrReplaceChild("lips_1",
         CubeListBuilder.create().texOffs(79, 90).addBox(-2.0F, -1.0F, -1.0F, 4, 2, 3),
         PartPose.offsetAndRotation(0.0F, 12.4F, -2.0F, -0.31869712F, 0.0F, 0.0F));
      base.addOrReplaceChild("cheeks",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, 0.0F, -1.0F, 11, 4, 4),
         PartPose.offsetAndRotation(0.0F, 6.8F, -2.9F, 0.18203785F, 0.0F, 0.0F));
      base.addOrReplaceChild("nose",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2, 5, 6),
         PartPose.offsetAndRotation(0.0F, 4.9F, -3.9F, -0.3642502F, 0.0F, 0.0F));
      PartDefinition horn = base.addOrReplaceChild("horn",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, 0.0F, 6, 20, 3),
         PartPose.offset(0.0F, -3.0F, -2.4F));
      horn.addOrReplaceChild("horn_2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 2, 20, 2),
         PartPose.offset(1.1F, -5.1F, 0.6F));
      horn.addOrReplaceChild("horn_3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, 0.0F, 1, 20, 2),
         PartPose.offset(0.0F, -2.5F, 1.0F));
      PartDefinition brow = base.addOrReplaceChild("brow",
         CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, 0.0F, -1.0F, 11, 2, 4),
         PartPose.offsetAndRotation(0.0F, 3.4F, -2.6F, -0.18203785F, 0.0F, 0.0F));
      brow.addOrReplaceChild("eyelids",
         CubeListBuilder.create().texOffs(75, 90).addBox(-4.5F, 0.0F, -1.0F, 9, 2, 4),
         PartPose.offsetAndRotation(0.0F, 1.1F, 0.6F, -0.3642502F, 0.0F, 0.0F));
      PartDefinition horn_1 = base.addOrReplaceChild("horn_1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2, 13, 2),
         PartPose.offsetAndRotation(4.9F, 3.5F, -2.7F, 0.68294734F, -0.4553564F, 0.0F));
      PartDefinition horn_4 = horn_1.addOrReplaceChild("horn_4",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -7.0F, -0.5F, 1, 7, 1),
         PartPose.offsetAndRotation(0.0F, -7.7F, -0.5F, -0.4098033F, 0.0F, -0.63739425F));
      horn_4.addOrReplaceChild("horn_5",
         CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0034906585F, 0.22759093F, (float) (Math.PI / 3)));
      return LayerDefinition.create(mesh, 100, 100);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float f1 = 0.5F;
      this.walk(this.horn_1, 0.125F, 0.125F, false, 0.0F, 0.25F, (float) entity.tickCount, f1);
      this.flap(this.horn_1, 0.125F, 0.125F, false, 0.0F, 0.0F, (float) entity.tickCount, f1);
   }
}
