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

public class ModelDryad<T extends Entity> extends MowzieModelBase<T> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "dryad"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer armL;
   public final MowzieModelRenderer armR;
   public final MowzieModelRenderer legL;
   public final MowzieModelRenderer legR;
   public final MowzieModelRenderer head;
   public final MowzieModelRenderer cape;
   public final MowzieModelRenderer capeL;
   public final MowzieModelRenderer staff;
   public final MowzieModelRenderer capeR;
   public final MowzieModelRenderer antL1;
   public final MowzieModelRenderer antR1;
   public final MowzieModelRenderer hair;
   public final MowzieModelRenderer antL2;
   public final MowzieModelRenderer antL3;
   public final MowzieModelRenderer antL4;
   public final MowzieModelRenderer antL5;
   public final MowzieModelRenderer antR2;
   public final MowzieModelRenderer antR3;
   public final MowzieModelRenderer antR4;
   public final MowzieModelRenderer antR5;

   public ModelDryad(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.head = new MowzieModelRenderer(this.body.part.getChild("head"));
      this.hair = new MowzieModelRenderer(this.head.part.getChild("hair"));
      this.armL = new MowzieModelRenderer(this.body.part.getChild("armL"));
      this.staff = new MowzieModelRenderer(this.armL.part.getChild("staff"));
      this.capeL = new MowzieModelRenderer(this.armL.part.getChild("capeL"));
      this.armR = new MowzieModelRenderer(this.body.part.getChild("armR"));
      this.capeR = new MowzieModelRenderer(this.armR.part.getChild("capeR"));
      this.cape = new MowzieModelRenderer(this.body.part.getChild("cape"));
      this.legL = new MowzieModelRenderer(this.body.part.getChild("legL"));
      this.legR = new MowzieModelRenderer(this.body.part.getChild("legR"));
      this.antL1 = new MowzieModelRenderer(this.head.part.getChild("antL1"));
      this.antL2 = new MowzieModelRenderer(this.antL1.part.getChild("antL2"));
      this.antL3 = new MowzieModelRenderer(this.antL2.part.getChild("antL3"));
      this.antL4 = new MowzieModelRenderer(this.antL2.part.getChild("antL4"));
      this.antL5 = new MowzieModelRenderer(this.antL2.part.getChild("antL5"));
      this.antR1 = new MowzieModelRenderer(this.head.part.getChild("antR1"));
      this.antR2 = new MowzieModelRenderer(this.antR1.part.getChild("antR2"));
      this.antR3 = new MowzieModelRenderer(this.antR2.part.getChild("antR3"));
      this.antR4 = new MowzieModelRenderer(this.antR2.part.getChild("antR4"));
      this.antR5 = new MowzieModelRenderer(this.antR2.part.getChild("antR5"));
      this.parts.add(this.body); this.parts.add(this.head); this.parts.add(this.hair);
      this.parts.add(this.armL); this.parts.add(this.armR); this.parts.add(this.legL); this.parts.add(this.legR);
      this.parts.add(this.antL1); this.parts.add(this.antL2); this.parts.add(this.antL3); this.parts.add(this.antL4); this.parts.add(this.antL5);
      this.parts.add(this.antR1); this.parts.add(this.antR2); this.parts.add(this.antR3); this.parts.add(this.antR4); this.parts.add(this.antR5);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(16, 18).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4),
         PartPose.offset(0.0F, -4.0F, 0.0F));
      PartDefinition head = body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(6, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8),
         PartPose.ZERO);
      head.addOrReplaceChild("hair",
         CubeListBuilder.create().mirror().texOffs(0, 35).addBox(-4.5F, -8.5F, -4.5F, 9, 18, 9),
         PartPose.ZERO);
      PartDefinition antL1 = head.addOrReplaceChild("antL1",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-4.0F, -7.0F, -3.0F, (float) (Math.PI / 18), 0.0F, (float) (-Math.PI / 9)));
      PartDefinition antL2 = antL1.addOrReplaceChild("antL2",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -6.0F, -0.5F, 1, 6, 1),
         PartPose.offsetAndRotation(1.0F, -3.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 4)));
      antL2.addOrReplaceChild("antL3",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -0.5F, 4, 1, 1),
         PartPose.offsetAndRotation(-1.0F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 4)));
      antL2.addOrReplaceChild("antL4",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -1.0F, 3, 1, 1),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, (float) (Math.PI / 12), (float) (-Math.PI / 9)));
      antL2.addOrReplaceChild("antL5",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, -0.5F, 1, 1, 1),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 18)));
      PartDefinition antR1 = head.addOrReplaceChild("antR1",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(3.0F, -7.0F, -3.0F, (float) (Math.PI / 18), 0.0F, (float) (Math.PI / 9)));
      PartDefinition antR2 = antR1.addOrReplaceChild("antR2",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, -0.5F, 1, 6, 1),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
      antR2.addOrReplaceChild("antR3",
         CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -0.5F, 4, 1, 1),
         PartPose.offsetAndRotation(1.0F, -6.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 4)));
      antR2.addOrReplaceChild("antR4",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -1.0F, -1.0F, 3, 1, 1),
         PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, (float) (-Math.PI / 12), (float) (Math.PI / 9)));
      antR2.addOrReplaceChild("antR5",
         CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -0.5F, 1, 1, 1),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 18)));
      PartDefinition armL = body.addOrReplaceChild("armL",
         CubeListBuilder.create().texOffs(40, 0).addBox(-3.0F, -2.0F, -2.0F, 4, 14, 4),
         PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.08726646F));
      armL.addOrReplaceChild("staff",
         CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -12.0F, 0.0F, 1, 37, 1),
         PartPose.offsetAndRotation(-1.0F, 11.0F, 0.0F, (float) (Math.PI / 2), 0.0F, 0.0F));
      armL.addOrReplaceChild("capeL",
         CubeListBuilder.create().texOffs(96, 22).addBox(-4.0F, 0.0F, -4.0F, 6, 19, 9),
         PartPose.offset(0.0F, -2.5F, 0.0F));
      PartDefinition armR = body.addOrReplaceChild("armR",
         CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -2.0F, -2.0F, 4, 14, 4),
         PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.08726646F));
      armR.addOrReplaceChild("capeR",
         CubeListBuilder.create().texOffs(65, 22).addBox(-2.0F, 0.0F, -4.0F, 6, 19, 9),
         PartPose.offset(0.0F, -2.5F, 0.0F));
      body.addOrReplaceChild("cape",
         CubeListBuilder.create().texOffs(37, 36).addBox(-5.0F, 0.0F, 0.0F, 10, 20, 3),
         PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.091106184F, 0.0F, 0.0F));
      body.addOrReplaceChild("legL",
         CubeListBuilder.create().texOffs(72, 0).addBox(-2.0F, 0.0F, -2.0F, 4, 16, 4),
         PartPose.offset(-1.9F, 12.0F, 0.0F));
      body.addOrReplaceChild("legR",
         CubeListBuilder.create().texOffs(88, 0).addBox(-2.0F, 0.0F, -2.0F, 4, 16, 4),
         PartPose.offset(1.9F, 12.0F, 0.0F));
      return LayerDefinition.create(mesh, 128, 64);
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float globalSpeed = 1.0F;
      float globalDegree = 1.0F;
      this.faceTarget(this.head, 1.0F, netHeadYaw, headPitch);
      this.bob(this.body, 0.75F * globalSpeed, 0.5F * globalDegree, false, limbSwing, limbSwingAmount);
      this.flap(this.armL, 0.125F * globalSpeed, 0.125F * globalDegree, false, 0.0F, 0.0F, ageInTicks, 0.5F);
      this.flap(this.armR, 0.125F * globalSpeed, 0.125F * globalDegree, true, 0.0F, 0.0F, ageInTicks, 0.5F);
      this.walk(this.armL, 1.125F * globalSpeed, 1.0F * globalDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.armR, 1.125F * globalSpeed, 1.0F * globalDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.legL, 1.125F * globalSpeed, 1.0F * globalDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.legR, 1.125F * globalSpeed, 1.0F * globalDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
   }
}
