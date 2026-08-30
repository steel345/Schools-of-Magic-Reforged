package com.paleimitations.schoolsofmagic.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

// the texture is packed as separate tiles per face, which box uv cannot follow - it lays one
// cross out and you end up with the stem on top of the cap. each face is its own flat plane
// instead, so every one points at the tile the artist drew for it
public class ModelShroom<T extends Entity> extends EntityModel<T> {
   public static final ModelLayerLocation LAYER_LOCATION =
      new ModelLayerLocation(new ResourceLocation("som", "shroom"), "main");

   private final ModelPart shroom;

   public ModelShroom(ModelPart root) {
      this.shroom = root.getChild("shroom");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition mesh = new MeshDefinition();
      PartDefinition root = mesh.getRoot();
      PartDefinition shroom = root.addOrReplaceChild("shroom", CubeListBuilder.create(),
         PartPose.offset(0.0F, 24.0F, 0.0F));

      // cap, 6 wide by 5 tall by 5 deep, top of it 11 up from the foot
      face(shroom, "cap_north", 12, 0, 6, 5, 0.0F, -8.5F, -2.5F, 0.0F);
      face(shroom, "cap_south", 12, 5, 6, 5, 0.0F, -8.5F, 2.5F, Mth.PI);
      face(shroom, "cap_west", 6, 5, 5, 5, -3.0F, -8.5F, 0.0F, Mth.HALF_PI);
      face(shroom, "cap_east", 0, 5, 5, 5, 3.0F, -8.5F, 0.0F, -Mth.HALF_PI);
      flat(shroom, "cap_up", 0, 0, 6, 5, 0.0F, -11.0F, 0.0F, false);
      flat(shroom, "cap_down", 6, 0, 6, 5, 0.0F, -6.0F, 0.0F, true);

      // stem, 3 by 6 by 3 under it
      face(shroom, "stem_north", 0, 10, 3, 6, 0.0F, -3.0F, -1.5F, 0.0F);
      face(shroom, "stem_south", 3, 10, 3, 6, 0.0F, -3.0F, 1.5F, Mth.PI);
      face(shroom, "stem_west", 9, 10, 3, 6, -1.5F, -3.0F, 0.0F, Mth.HALF_PI);
      face(shroom, "stem_east", 6, 10, 3, 6, 1.5F, -3.0F, 0.0F, -Mth.HALF_PI);
      flat(shroom, "stem_down", 12, 10, 3, 3, 0.0F, 0.0F, 0.0F, true);

      return LayerDefinition.create(mesh, 32, 16);
   }

   private static final net.minecraft.client.model.geom.builders.CubeDeformation THIN =
      new net.minecraft.client.model.geom.builders.CubeDeformation(0.01F);

   // an upright plane. its front and back read the tile at texOffs one to one
   private static void face(PartDefinition parent, String name, int u, int v, int w, int h,
                            float x, float y, float z, float yRot) {
      parent.addOrReplaceChild(name,
         CubeListBuilder.create().texOffs(u, v).addBox(-w / 2.0F, -h / 2.0F, 0.0F, w, h, 0.0F, THIN),
         PartPose.offsetAndRotation(x, y, z, 0.0F, yRot, 0.0F));
   }

   private static void flat(PartDefinition parent, String name, int u, int v, int w, int d,
                            float x, float y, float z, boolean down) {
      parent.addOrReplaceChild(name,
         CubeListBuilder.create().texOffs(u, v).addBox(-w / 2.0F, -d / 2.0F, 0.0F, w, d, 0.0F, THIN),
         PartPose.offsetAndRotation(x, y, z, down ? Mth.HALF_PI : -Mth.HALF_PI, 0.0F, 0.0F));
   }

   @Override
   public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                         float netHeadYaw, float headPitch) {
   }

   @Override
   public void renderToBuffer(PoseStack pose, VertexConsumer buf, int light, int overlay,
                              float r, float g, float b, float a) {
      this.shroom.render(pose, buf, light, overlay, r, g, b, a);
   }
}
