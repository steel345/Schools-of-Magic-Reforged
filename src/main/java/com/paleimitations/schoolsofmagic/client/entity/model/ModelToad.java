package com.paleimitations.schoolsofmagic.client.entity.model;

import com.paleimitations.schoolsofmagic.common.entity.EntityToad;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModelToad extends MowzieModelBase<EntityToad> {

   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "toad"), "main");

   public final MowzieModelRenderer body;
   public final MowzieModelRenderer head;
   public final MowzieModelRenderer foreleg1;
   public final MowzieModelRenderer foreleg2;
   public final MowzieModelRenderer backthigh1;
   public final MowzieModelRenderer backthigh2;
   public final MowzieModelRenderer jaw;
   public final MowzieModelRenderer eye1;
   public final MowzieModelRenderer eye2;
   public final MowzieModelRenderer backleg1;
   public final MowzieModelRenderer backleg2;

   public ModelToad(ModelPart root) {
      super(root);
      this.body = new MowzieModelRenderer(root.getChild("body"));
      this.head = new MowzieModelRenderer(this.body.part.getChild("head"));
      this.eye1 = new MowzieModelRenderer(this.head.part.getChild("eye1"));
      this.eye2 = new MowzieModelRenderer(this.head.part.getChild("eye2"));
      this.jaw = new MowzieModelRenderer(this.head.part.getChild("jaw"));
      this.foreleg1 = new MowzieModelRenderer(this.body.part.getChild("foreleg1"));
      this.foreleg2 = new MowzieModelRenderer(this.body.part.getChild("foreleg2"));
      this.backthigh1 = new MowzieModelRenderer(this.body.part.getChild("backthigh1"));
      this.backthigh2 = new MowzieModelRenderer(this.body.part.getChild("backthigh2"));
      this.backleg1 = new MowzieModelRenderer(this.backthigh1.part.getChild("backleg1"));
      this.backleg2 = new MowzieModelRenderer(this.backthigh2.part.getChild("backleg2"));
      this.parts.add(this.body); this.parts.add(this.head); this.parts.add(this.foreleg1);
      this.parts.add(this.foreleg2); this.parts.add(this.backthigh1); this.parts.add(this.backthigh2);
      this.parts.add(this.jaw); this.parts.add(this.eye1); this.parts.add(this.eye2);
      this.parts.add(this.backleg1); this.parts.add(this.backleg2);
      this.setInitPose();
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition body = mesh.getRoot().addOrReplaceChild("body",
         CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -4.0F, 4, 2, 4),
         PartPose.offsetAndRotation(0.0F, 23.8F, 0.0F, (float) (-Math.PI / 9), 0.0F, 0.0F));
      PartDefinition head = body.addOrReplaceChild("head",
         CubeListBuilder.create().texOffs(0, 6).addBox(-2.0F, 0.0F, -2.0F, 4, 1, 2),
         PartPose.offsetAndRotation(0.0F, -2.0F, -4.0F, 0.27925268F, 0.0F, 0.0F));
      head.addOrReplaceChild("eye1",
         CubeListBuilder.create().texOffs(12, 0).addBox(-0.8F, -0.3F, -0.3F, 1, 1, 1),
         PartPose.offset(2.0F, 0.0F, -2.0F));
      head.addOrReplaceChild("eye2",
         CubeListBuilder.create().texOffs(12, 0).addBox(-0.2F, -0.3F, -0.3F, 1, 1, 1),
         PartPose.offset(-2.0F, 0.0F, -2.0F));
      head.addOrReplaceChild("jaw",
         CubeListBuilder.create().texOffs(0, 9).addBox(-2.0F, 0.0F, -2.0F, 4, 1, 2),
         PartPose.offset(0.0F, 1.0F, 0.0F));
      body.addOrReplaceChild("foreleg1",
         CubeListBuilder.create().texOffs(0, 12).addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1),
         PartPose.offset(-1.5F, 0.0F, -4.0F));
      body.addOrReplaceChild("foreleg2",
         CubeListBuilder.create().texOffs(0, 12).addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1),
         PartPose.offset(1.5F, 0.0F, -4.0F));
      PartDefinition backthigh1 = body.addOrReplaceChild("backthigh1",
         CubeListBuilder.create().texOffs(4, 12).addBox(-1.0F, -3.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.87266463F, 0.0F, 0.0F));
      backthigh1.addOrReplaceChild("backleg1",
         CubeListBuilder.create().texOffs(8, 12).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(-0.5F, -3.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
      PartDefinition backthigh2 = body.addOrReplaceChild("backthigh2",
         CubeListBuilder.create().texOffs(4, 12).addBox(0.0F, -3.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.87266463F, 0.0F, 0.0F));
      backthigh2.addOrReplaceChild("backleg2",
         CubeListBuilder.create().texOffs(8, 12).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1),
         PartPose.offsetAndRotation(0.5F, -3.0F, 0.0F, (float) (-Math.PI * 2.0 / 9.0), 0.0F, 0.0F));
      return LayerDefinition.create(mesh, 16, 16);
   }

   @Override
   public void prepareMobModel(EntityToad entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
      super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTickTime);
      float f9 = (float) entity.tickCount + partialTickTime;
      boolean flag = entity.mouthCounter != 0;
      if (flag) {
         this.jaw.part.yRot = Mth.cos(f9 * 0.7F);
      } else {
         this.jaw.part.yRot = 0.0F;
      }
   }

   @Override
   public void setupAnim(EntityToad entity, float limbSwing, float limbSwingAmount,
                         float ageInTicks, float netHeadYaw, float headPitch) {
      this.setToInitPose();
      float f9 = (float) entity.tickCount;
      boolean flag = entity.mouthCounter != 0;
      if (flag) {
         this.walk(this.jaw, 0.5F, 0.25F, false, 2.0F, 0.125F, f9, 1.0F);
      } else {
         this.jaw.part.yRot = 0.0F;
      }

      float globalSpeed = 1.0F;
      float globalDegree = 0.625F;
      float localOffset = 2.0F;
      float globalOffset = -2.0F;
      this.walk(this.body, 0.5F * globalSpeed, 0.5F * globalDegree, false, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.backthigh1, 0.5F * globalSpeed, 4.0F * globalDegree, false, localOffset + globalOffset, 2.0F * globalDegree, limbSwing, limbSwingAmount);
      this.walk(this.backthigh2, 0.5F * globalSpeed, 4.0F * globalDegree, false, localOffset + globalOffset, 2.0F * globalDegree, limbSwing, limbSwingAmount);
      this.walk(this.backleg1, 0.5F * globalSpeed, 1.5F * globalDegree, true, localOffset + globalOffset, -0.75F * globalDegree, limbSwing, limbSwingAmount);
      this.walk(this.backleg2, 0.5F * globalSpeed, 1.5F * globalDegree, true, localOffset + globalOffset, -0.75F * globalDegree, limbSwing, limbSwingAmount);
      this.swing(this.backthigh1, 0.5F * globalSpeed, 0.75F * globalDegree, true, localOffset + globalOffset, 0.375F * globalDegree, limbSwing, limbSwingAmount);
      this.swing(this.backthigh2, 0.5F * globalSpeed, 0.75F * globalDegree, false, localOffset + globalOffset, -0.375F * globalDegree, limbSwing, limbSwingAmount);
      this.walk(this.foreleg1, 0.5F * globalSpeed, 0.75F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
      this.walk(this.foreleg2, 0.5F * globalSpeed, 0.75F * globalDegree, true, globalOffset, 0.0F, limbSwing, limbSwingAmount);
   }
}
