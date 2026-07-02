package com.paleimitations.schoolsofmagic.client.tileentity.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelSpellForge {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "spell_forge"), "main");

   public final ModelPart root;
   public final ModelPart lower_bulb;
   public final ModelPart brazier_connector;
   public final ModelPart door;
   private static final float DOOR_OPEN_YROT = 0.8727F;

   public ModelSpellForge(ModelPart root) {
      this.root = root;
      this.lower_bulb = root.getChild("lower_bulb");
      this.brazier_connector = root.getChild("brazier_connector");
      this.door = this.lower_bulb.getChild("bone4").getChild("door_r1");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();

      PartDefinition lower_bulb = root.addOrReplaceChild("lower_bulb",
         CubeListBuilder.create()
            .texOffs(53, 47).addBox(6.0F, -7.0F, -8.0F, 2, 4, 2, new CubeDeformation(0.0120F))
            .texOffs(53, 47).addBox(6.0F, -7.0F, 6.0F, 2, 4, 2, new CubeDeformation(0.0126F))
            .texOffs(52, 47).addBox(-8.0F, -7.0F, -8.0F, 2, 4, 2, new CubeDeformation(0.0132F))
            .texOffs(53, 47).addBox(7.0F, -3.0F, 7.0F, 1, 2, 1, new CubeDeformation(0.0138F))
            .texOffs(53, 47).addBox(-8.0F, -7.0F, 6.0F, 2, 4, 2, new CubeDeformation(0.0144F))
            .texOffs(53, 47).addBox(7.0F, -3.0F, -8.0F, 1, 2, 1, new CubeDeformation(0.0150F))
            .texOffs(53, 47).addBox(-8.0F, -3.0F, -8.0F, 1, 2, 1, new CubeDeformation(0.0156F))
            .texOffs(53, 47).addBox(-8.0F, -3.0F, 7.0F, 1, 2, 1, new CubeDeformation(0.0162F))
            .texOffs(35, 36).addBox(-8.0F, -9.0F, -8.0F, 16, 2, 16, new CubeDeformation(0.0168F)),
         PartPose.offsetAndRotation(0.0F, 13.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

      PartDefinition bone4 = lower_bulb.addOrReplaceChild("bone4",
         CubeListBuilder.create()
            .texOffs(55, 40).addBox(-5.0F, -4.0F, 0.0F, 1, 8, 1, new CubeDeformation(0.0174F))
            .texOffs(60, 40).addBox(4.0F, -4.0F, 0.0F, 1, 8, 1, new CubeDeformation(0.0180F))
            .texOffs(55, 48).addBox(-5.0F, 4.0F, 0.0F, 10, 1, 1, new CubeDeformation(0.0186F))
            .texOffs(58, 41).addBox(-5.0F, -5.0F, 0.0F, 10, 1, 1, new CubeDeformation(0.0192F)),
         PartPose.offset(0.0F, -1.0F, -7.5F));

      bone4.addOrReplaceChild("door_r1",
         CubeListBuilder.create()
            .texOffs(32, 36).addBox(0.0F, -4.0F, -0.5F, 8, 8, 1, new CubeDeformation(0.0198F)),
         PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -3.1416F, 0.8727F, 0.0F));

      PartDefinition bone = lower_bulb.addOrReplaceChild("bone",
         CubeListBuilder.create()
            .texOffs(-14, 0).addBox(-7.0F, 5.75F, -8.75F, 14, 0, 14, new CubeDeformation(0.0204F))
            .texOffs(0, 0).addBox(-7.0F, -8.25F, -8.75F, 0, 14, 14, new CubeDeformation(0.0210F))
            .texOffs(28, 0).addBox(-7.0F, -8.25F, -8.75F, 14, 14, 0, new CubeDeformation(0.0216F)),
         PartPose.offsetAndRotation(0.0F, -2.25F, 1.75F, 0.0F, 0.0F, -3.1416F));

      bone.addOrReplaceChild("shape60_r1",
         CubeListBuilder.create()
            .texOffs(0, 0).addBox(-7.0F, -6.0F, 0.5F, 14, 14, 0, new CubeDeformation(0.0222F)),
         PartPose.offsetAndRotation(0.0F, -8.75F, -2.75F, 1.5708F, 0.0F, 3.1416F));

      bone.addOrReplaceChild("shape59_r1",
         CubeListBuilder.create()
            .texOffs(56, 0).addBox(-7.0F, -6.5F, 0.0F, 14, 14, 0, new CubeDeformation(0.0228F)),
         PartPose.offsetAndRotation(0.0F, -1.75F, 5.25F, 0.0F, 3.1416F, 0.0F));

      bone.addOrReplaceChild("shape59_1_r1",
         CubeListBuilder.create()
            .texOffs(28, 0).addBox(0.0F, -6.5F, -7.0F, 0, 14, 14, new CubeDeformation(0.0234F)),
         PartPose.offsetAndRotation(7.0F, -1.75F, -1.75F, 0.0F, 3.1416F, 0.0F));

      PartDefinition bone5 = bone.addOrReplaceChild("bone5",
         CubeListBuilder.create()
            .texOffs(-14, 0).addBox(-7.0F, 6.25F, -8.75F, 14, 0, 14, new CubeDeformation(0.0240F))
            .texOffs(-14, 0).addBox(-7.0F, -7.75F, -8.75F, 14, 0, 14, new CubeDeformation(0.0246F))
            .texOffs(0, -14).addBox(-7.0F, -7.75F, -8.75F, 0, 14, 14, new CubeDeformation(0.0252F))
            .texOffs(0, 0).addBox(-7.0F, -7.75F, -8.75F, 14, 14, 0, new CubeDeformation(0.0258F)),
         PartPose.offsetAndRotation(0.0F, -26.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

      bone5.addOrReplaceChild("shape59_r2",
         CubeListBuilder.create()
            .texOffs(0, 0).addBox(-7.0F, -6.0F, 0.0F, 14, 14, 0, new CubeDeformation(0.0264F)),
         PartPose.offsetAndRotation(0.0F, -1.75F, 5.25F, 0.0F, 3.1416F, 0.0F));

      bone5.addOrReplaceChild("shape59_1_r2",
         CubeListBuilder.create()
            .texOffs(0, -14).addBox(0.0F, -6.0F, -7.0F, 0, 14, 14, new CubeDeformation(0.0270F)),
         PartPose.offsetAndRotation(7.0F, -1.75F, -1.75F, 0.0F, 3.1416F, 0.0F));

      lower_bulb.addOrReplaceChild("bone7",
         CubeListBuilder.create()
            .texOffs(0, 36).addBox(-7.0F, -10.0F, 0.0F, 7, 10, 0, new CubeDeformation(0.0276F))
            .texOffs(59, 18).addBox(-7.0F, -10.0F, 7.0F, 7, 10, 0, new CubeDeformation(0.0282F))
            .texOffs(87, 11).addBox(0.0F, -10.0F, 0.0F, 0, 10, 7, new CubeDeformation(0.0288F))
            .texOffs(73, 11).addBox(-7.0F, -10.0F, 0.0F, 0, 10, 7, new CubeDeformation(0.0294F)),
         PartPose.offset(3.5F, 16.0F, -3.5F));

      PartDefinition bone2 = lower_bulb.addOrReplaceChild("bone2",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

      PartDefinition bone3 = bone2.addOrReplaceChild("bone3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-25.0F, 6.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0300F))
            .texOffs(7, 55).addBox(-13.0F, 6.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0306F))
            .texOffs(7, 55).addBox(-13.0F, -3.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0312F))
            .texOffs(7, 55).addBox(-25.0F, -3.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0318F)),
         PartPose.offset(17.0F, -3.0F, -1.0F));

      bone3.addOrReplaceChild("bot_facet_t9_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 1, 1, 1, new CubeDeformation(0.0324F))
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 1, 1, 1, new CubeDeformation(0.0330F)),
         PartPose.offsetAndRotation(-25.0F, 6.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      bone3.addOrReplaceChild("bot_facet_t7_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0336F)),
         PartPose.offsetAndRotation(-25.0F, 10.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      bone3.addOrReplaceChild("bot_facet_t6_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0342F)),
         PartPose.offsetAndRotation(-25.0F, 14.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      bone3.addOrReplaceChild("bot_facet_t5_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0348F)),
         PartPose.offsetAndRotation(-25.0F, 18.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone6 = bone3.addOrReplaceChild("bone6",
         CubeListBuilder.create(),
         PartPose.offset(-9.5F, 14.0F, 1.0F));

      bone6.addOrReplaceChild("bot_facet_t11_r1",
         CubeListBuilder.create()
            .texOffs(9, 55).addBox(0.5F, -0.51F, 0.0F, 2, 1, 1, new CubeDeformation(0.0354F)),
         PartPose.offsetAndRotation(-11.5F, -14.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t11_r2",
         CubeListBuilder.create()
            .texOffs(9, 55).addBox(0.5F, -0.51F, 0.0F, 2, 1, 1, new CubeDeformation(0.0360F))
            .texOffs(9, 55).addBox(0.5F, 0.49F, 0.0F, 2, 0, 1, new CubeDeformation(0.0366F)),
         PartPose.offsetAndRotation(-3.5F, -14.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t10_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.51F, 0.0F, 4, 1, 1, new CubeDeformation(0.0372F)),
         PartPose.offsetAndRotation(-11.5F, -12.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t9_r2",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.51F, 0.0F, 4, 1, 1, new CubeDeformation(0.0378F)),
         PartPose.offsetAndRotation(-3.5F, -12.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t9_r3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.49F, 0.0F, 4, 1, 1, new CubeDeformation(0.0384F)),
         PartPose.offsetAndRotation(-11.5F, -8.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t8_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.51F, 0.0F, 4, 1, 1, new CubeDeformation(0.0390F)),
         PartPose.offsetAndRotation(-3.5F, -8.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t8_r2",
         CubeListBuilder.create()
            .texOffs(10, 55).addBox(1.5F, -0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0396F)),
         PartPose.offsetAndRotation(-0.5F, -4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t7_r2",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0402F)),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t6_r2",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0408F)),
         PartPose.offsetAndRotation(-0.5F, 0.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone6.addOrReplaceChild("bot_facet_t5_r2",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0414F)),
         PartPose.offsetAndRotation(-0.5F, 4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone12 = bone3.addOrReplaceChild("bone12",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(-16.5F, -18.0F, 1.0F, 0.0F, 0.0F, -1.5708F));

      bone12.addOrReplaceChild("bot_facet_t11_r3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0420F)),
         PartPose.offsetAndRotation(-1.5F, -6.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone12.addOrReplaceChild("bot_facet_t10_r2",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0426F)),
         PartPose.offsetAndRotation(-1.5F, -2.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone12.addOrReplaceChild("bot_facet_t9_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0432F)),
         PartPose.offsetAndRotation(-1.5F, 1.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone12.addOrReplaceChild("bot_facet_t8_r3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0438F)),
         PartPose.offsetAndRotation(-1.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone3.addOrReplaceChild("bone14",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(-17.5F, -18.0F, 1.0F, -1.5708F, 0.0F, -1.5708F));

      bone3.addOrReplaceChild("bone13",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(-16.5F, 6.0F, 1.0F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone10 = bone3.addOrReplaceChild("bone10",
         CubeListBuilder.create(),
         PartPose.offset(-9.5F, -10.0F, 1.0F));

      bone10.addOrReplaceChild("bot_facet_t10_r3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 1, 1, 1, new CubeDeformation(0.0444F)),
         PartPose.offsetAndRotation(-0.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone10.addOrReplaceChild("bot_facet_t9_r5",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 1, 1, 1, new CubeDeformation(0.0450F)),
         PartPose.offsetAndRotation(-0.5F, -7.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone10.addOrReplaceChild("bot_facet_t8_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0456F)),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone10.addOrReplaceChild("bot_facet_t7_r3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0462F)),
         PartPose.offsetAndRotation(-0.5F, 0.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone10.addOrReplaceChild("bot_facet_t6_r3",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0468F)),
         PartPose.offsetAndRotation(-0.5F, 4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone11 = bone3.addOrReplaceChild("bone11",
         CubeListBuilder.create(),
         PartPose.offset(-24.5F, -10.0F, 1.0F));

      bone11.addOrReplaceChild("bot_facet_t11_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0474F)),
         PartPose.offsetAndRotation(-0.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone11.addOrReplaceChild("bot_facet_t10_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0480F)),
         PartPose.offsetAndRotation(-0.5F, -7.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone11.addOrReplaceChild("bot_facet_t9_r6",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0486F)),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone11.addOrReplaceChild("bot_facet_t8_r5",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0492F)),
         PartPose.offsetAndRotation(-0.5F, 0.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone11.addOrReplaceChild("bot_facet_t7_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0498F)),
         PartPose.offsetAndRotation(-0.5F, 4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone15 = bone3.addOrReplaceChild("bone15",
         CubeListBuilder.create(),
         PartPose.offset(-17.5F, 14.0F, -7.0F));

      bone15.addOrReplaceChild("bot_facet_t12_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.51F, 1, 1, 1, new CubeDeformation(0.0504F)),
         PartPose.offsetAndRotation(-0.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone3.addOrReplaceChild("bone9",
         CubeListBuilder.create(),
         PartPose.offset(-16.5F, -10.0F, -7.0F));

      PartDefinition bone8 = lower_bulb.addOrReplaceChild("bone8",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(0.0F, 10.0F, 0.0F, 0.0F, 1.5708F, -3.1416F));

      PartDefinition bone16 = bone8.addOrReplaceChild("bone16",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-25.0F, 6.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0510F))
            .texOffs(7, 55).addBox(-13.0F, 6.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0516F))
            .texOffs(7, 55).addBox(-13.0F, -3.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0522F))
            .texOffs(7, 55).addBox(-25.0F, -3.01F, 0.5F, 4, 1, 1, new CubeDeformation(0.0528F)),
         PartPose.offset(17.0F, -3.0F, -1.0F));

      bone16.addOrReplaceChild("bot_facet_t10_r5",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0534F)),
         PartPose.offsetAndRotation(-25.0F, 6.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      bone16.addOrReplaceChild("bot_facet_t8_r6",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0540F)),
         PartPose.offsetAndRotation(-25.0F, 10.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      bone16.addOrReplaceChild("bot_facet_t7_r5",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0546F)),
         PartPose.offsetAndRotation(-25.0F, 14.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      bone16.addOrReplaceChild("bot_facet_t6_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0552F)),
         PartPose.offsetAndRotation(-25.0F, 18.5F, 0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone17 = bone16.addOrReplaceChild("bone17",
         CubeListBuilder.create(),
         PartPose.offset(-9.5F, 14.0F, 1.0F));

      bone17.addOrReplaceChild("bot_facet_t12_r2",
         CubeListBuilder.create()
            .texOffs(9, 55).addBox(0.5F, -0.51F, 0.0F, 2, 1, 1, new CubeDeformation(0.0558F)),
         PartPose.offsetAndRotation(-11.5F, -14.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t12_r3",
         CubeListBuilder.create()
            .texOffs(9, 55).addBox(0.5F, -0.51F, 0.0F, 2, 1, 1, new CubeDeformation(0.0564F))
            .texOffs(9, 55).addBox(0.5F, 0.49F, 0.0F, 2, 0, 1, new CubeDeformation(0.0570F)),
         PartPose.offsetAndRotation(-3.5F, -14.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t11_r5",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.51F, 0.0F, 4, 1, 1, new CubeDeformation(0.0576F)),
         PartPose.offsetAndRotation(-11.5F, -12.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t10_r6",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.51F, 0.0F, 4, 1, 1, new CubeDeformation(0.0582F)),
         PartPose.offsetAndRotation(-3.5F, -12.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t10_r7",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.49F, 0.0F, 4, 1, 1, new CubeDeformation(0.0588F)),
         PartPose.offsetAndRotation(-11.5F, -8.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t9_r7",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.51F, 0.0F, 4, 1, 1, new CubeDeformation(0.0594F)),
         PartPose.offsetAndRotation(-3.5F, -8.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t9_r8",
         CubeListBuilder.create()
            .texOffs(10, 55).addBox(1.5F, -0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0600F)),
         PartPose.offsetAndRotation(-0.5F, -4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone17.addOrReplaceChild("bot_facet_t8_r7",
         CubeListBuilder.create()
            .texOffs(9, 55).addBox(0.5F, 0.0F, 0.0F, 2, 1, 1, new CubeDeformation(0.0606F)),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone18 = bone16.addOrReplaceChild("bone18",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(-16.5F, -18.0F, 1.0F, 0.0F, 0.0F, -1.5708F));

      bone18.addOrReplaceChild("bot_facet_t12_r4",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0612F)),
         PartPose.offsetAndRotation(-1.5F, -6.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone18.addOrReplaceChild("bot_facet_t11_r6",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0618F)),
         PartPose.offsetAndRotation(-1.5F, -2.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone18.addOrReplaceChild("bot_facet_t10_r8",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0624F)),
         PartPose.offsetAndRotation(-1.5F, 1.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone18.addOrReplaceChild("bot_facet_t9_r9",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-2.0F, 0.5F, 0.0F, 4, 1, 1, new CubeDeformation(0.0630F)),
         PartPose.offsetAndRotation(-1.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone16.addOrReplaceChild("bone19",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(-17.5F, -18.0F, 1.0F, -1.5708F, 0.0F, -1.5708F));

      bone16.addOrReplaceChild("bone20",
         CubeListBuilder.create(),
         PartPose.offsetAndRotation(-16.5F, 6.0F, 1.0F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone21 = bone16.addOrReplaceChild("bone21",
         CubeListBuilder.create(),
         PartPose.offset(-9.5F, -10.0F, 1.0F));

      bone21.addOrReplaceChild("bot_facet_t11_r7",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0636F)),
         PartPose.offsetAndRotation(-0.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone21.addOrReplaceChild("bot_facet_t10_r9",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0642F)),
         PartPose.offsetAndRotation(-0.5F, -7.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone21.addOrReplaceChild("bot_facet_t9_r10",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0648F)),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone21.addOrReplaceChild("bot_facet_t8_r8",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0654F)),
         PartPose.offsetAndRotation(-0.5F, 0.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone21.addOrReplaceChild("bot_facet_t7_r6",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, -0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0660F)),
         PartPose.offsetAndRotation(-0.5F, 4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone22 = bone16.addOrReplaceChild("bone22",
         CubeListBuilder.create(),
         PartPose.offset(-24.5F, -10.0F, 1.0F));

      bone22.addOrReplaceChild("bot_facet_t12_r5",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0666F)),
         PartPose.offsetAndRotation(-0.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone22.addOrReplaceChild("bot_facet_t11_r8",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.01F, 0.0F, 1, 1, 1, new CubeDeformation(0.0672F)),
         PartPose.offsetAndRotation(-0.5F, -7.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone22.addOrReplaceChild("bot_facet_t10_r10",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0678F)),
         PartPose.offsetAndRotation(-0.5F, -3.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone22.addOrReplaceChild("bot_facet_t9_r11",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.0F, 4, 1, 1, new CubeDeformation(0.0684F)),
         PartPose.offsetAndRotation(-0.5F, 0.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone22.addOrReplaceChild("bot_facet_t8_r9",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.01F, 0.0F, 4, 1, 1, new CubeDeformation(0.0690F)),
         PartPose.offsetAndRotation(-0.5F, 4.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      PartDefinition bone23 = bone16.addOrReplaceChild("bone23",
         CubeListBuilder.create(),
         PartPose.offset(-17.5F, 14.0F, -7.0F));

      bone23.addOrReplaceChild("bot_facet_t13_r1",
         CubeListBuilder.create()
            .texOffs(7, 55).addBox(-1.5F, 0.0F, 0.51F, 1, 1, 1, new CubeDeformation(0.0696F)),
         PartPose.offsetAndRotation(-0.5F, 5.5F, -0.5F, 0.0F, 0.0F, -1.5708F));

      bone16.addOrReplaceChild("bone24",
         CubeListBuilder.create(),
         PartPose.offset(-16.5F, -10.0F, -7.0F));

      root.addOrReplaceChild("brazier_connector",
         CubeListBuilder.create()
            .texOffs(36, 55).addBox(-7.5F, 21.0F, -7.5F, 15, 2, 15, new CubeDeformation(0.0702F)),
         PartPose.offset(0.0F, 1.0F, 0.0F));

      return LayerDefinition.create(mesh, 128, 80);
   }

   public void renderAll(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int tick, TileEntitySpellForge te, boolean connected, boolean active) {
      this.door.yRot = active ? (float) Math.PI : DOOR_OPEN_YROT;
      this.lower_bulb.render(poseStack, buffer, packedLight, packedOverlay);
      if (connected) {
         this.brazier_connector.render(poseStack, buffer, packedLight, packedOverlay);
      }
   }
}
