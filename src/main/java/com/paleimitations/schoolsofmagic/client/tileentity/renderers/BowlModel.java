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

public class BowlModel extends EntityModel<Entity> {
   private final ModelPart bb_main;
   private final ModelPart east_r1;
   private final ModelPart south_r1;

   public BowlModel() {
      ModelPart part = createBodyLayer().bakeRoot();
      this.bb_main = part.getChild("bb_main");
      this.east_r1 = this.bb_main.getChild("east_r1");
      this.south_r1 = this.bb_main.getChild("south_r1");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition bbmain = partdefinition.addOrReplaceChild(
         "bb_main",
         CubeListBuilder.create()
            .texOffs(16, 26)
            .addBox(-2.0F, -0.5F, -2.0F, 4.0F, 1.0F, 4.0F)
            .texOffs(0, 0)
            .addBox(-4.0F, -1.5F, -4.0F, 8.0F, 1.0F, 8.0F)
            .texOffs(11, 18)
            .addBox(-4.0F, -5.5F, -5.0F, 8.0F, 4.0F, 1.0F)
            .texOffs(0, 17)
            .addBox(-5.0F, -5.5F, -4.0F, 1.0F, 4.0F, 8.0F)
            .texOffs(0, 9)
            .addBox(-4.0F, -5.0F, -4.0F, 8.0F, 0.0F, 8.0F),
         PartPose.offset(0.0F, 24.0F, 0.0F)
      );
      bbmain.addOrReplaceChild(
         "east_r1",
         CubeListBuilder.create().texOffs(0, 17).addBox(-0.5F, -2.0F, -4.0F, 1.0F, 4.0F, 8.0F),
         PartPose.offsetAndRotation(4.5F, -3.5F, 0.0F, 0.0F, 3.1416F, 0.0F)
      );
      bbmain.addOrReplaceChild(
         "south_r1",
         CubeListBuilder.create().texOffs(11, 18).addBox(-4.0F, -2.0F, -0.5F, 8.0F, 4.0F, 1.0F),
         PartPose.offsetAndRotation(0.0F, -3.5F, 4.5F, 0.0F, 3.1416F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 32, 32);
   }

   public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
   }

   public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
   }
}
