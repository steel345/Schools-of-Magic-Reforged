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

public class BakedPotatoModel extends EntityModel<Entity> {
   private final ModelPart bb_main;
   private final ModelPart cube_r1;
   private final ModelPart cube_r2;

   public BakedPotatoModel() {
      ModelPart part = createBodyLayer().bakeRoot();
      this.bb_main = part.getChild("bb_main");
      this.cube_r1 = this.bb_main.getChild("cube_r1");
      this.cube_r2 = this.bb_main.getChild("cube_r2");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      PartDefinition bbmain = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
      bbmain.addOrReplaceChild(
         "cube_r1",
         CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -2.0F, 6.0F, 3.0F, 2.0F),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F)
      );
      bbmain.addOrReplaceChild(
         "cube_r2",
         CubeListBuilder.create().texOffs(0, 5).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 3.0F, 2.0F),
         PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F)
      );
      return LayerDefinition.create(meshdefinition, 16, 16);
   }

   public void setupAnim(Entity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
   }

   public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
      this.bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
   }
}
