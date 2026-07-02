package com.paleimitations.schoolsofmagic.client.tileentity.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class ChickenModel extends EntityModel<Entity> {
   private final ModelPart bb_main;

   public ChickenModel() {
      ModelPart part = createBodyLayer().bakeRoot();
      this.bb_main = part.getChild("bb_main");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition bbmain = partdefinition.addOrReplaceChild(
         "bb_main",
         CubeListBuilder.create()
            .texOffs(0, 0)
            .addBox(-4.0F, -5.0F, -2.5F, 8.0F, 5.0F, 6.0F)
            .texOffs(8, 11)
            .addBox(0.0F, -3.0F, -3.5F, 3.0F, 3.0F, 1.0F)
            .texOffs(0, 3)
            .addBox(3.0F, -3.0F, -3.0F, 2.0F, 3.0F, 0.0F)
            .texOffs(0, 11)
            .addBox(0.0F, -3.0F, 3.5F, 3.0F, 3.0F, 1.0F)
            .texOffs(0, 0)
            .addBox(3.0F, -3.0F, 4.0F, 2.0F, 3.0F, 0.0F),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 32, 32);
   }

   public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
   }

   public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
   }
}
