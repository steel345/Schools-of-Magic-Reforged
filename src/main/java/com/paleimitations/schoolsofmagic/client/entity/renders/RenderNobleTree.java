package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree1;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree2;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree3;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelNobleTree4;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderNobleTree extends MobRenderer<EntityNobleTree, EntityModel<EntityNobleTree>> {
   private static final ResourceLocation[] TEX = new ResourceLocation[] {
      new ResourceLocation("som", "textures/entity/tree_ash.png"),
      new ResourceLocation("som", "textures/entity/tree_elder.png"),
      new ResourceLocation("som", "textures/entity/tree_pine.png"),
      new ResourceLocation("som", "textures/entity/tree_willow.png"),
      new ResourceLocation("som", "textures/entity/tree_yew.png"),
      new ResourceLocation("som", "textures/entity/tree_verde.png"),
      new ResourceLocation("som", "textures/entity/tree_oak.png"),
      new ResourceLocation("som", "textures/entity/tree_birch.png"),
      new ResourceLocation("som", "textures/entity/tree_spruce.png"),
      new ResourceLocation("som", "textures/entity/tree_doak.png"),
      new ResourceLocation("som", "textures/entity/tree_jungle.png"),
      new ResourceLocation("som", "textures/entity/tree_acacia.png"),
   };

   private final ModelNobleTree1<EntityNobleTree> tree1;
   private final ModelNobleTree2<EntityNobleTree> tree2;
   private final ModelNobleTree3<EntityNobleTree> tree3;
   private final ModelNobleTree4<EntityNobleTree> tree4;
   private final FaceDispatchModel dispatchModel;

   public RenderNobleTree(EntityRendererProvider.Context context) {

      this(context,
         new ModelNobleTree1<>(context.bakeLayer(ModelNobleTree1.LAYER_LOCATION)),
         new ModelNobleTree2<>(context.bakeLayer(ModelNobleTree2.LAYER_LOCATION)),
         new ModelNobleTree3<>(context.bakeLayer(ModelNobleTree3.LAYER_LOCATION)),
         new ModelNobleTree4<>(context.bakeLayer(ModelNobleTree4.LAYER_LOCATION)));
   }

   private RenderNobleTree(EntityRendererProvider.Context context,
                           ModelNobleTree1<EntityNobleTree> t1, ModelNobleTree2<EntityNobleTree> t2,
                           ModelNobleTree3<EntityNobleTree> t3, ModelNobleTree4<EntityNobleTree> t4) {
      super(context, new FaceDispatchModel(t1, t2, t3, t4), 0.3F);
      this.tree1 = t1; this.tree2 = t2; this.tree3 = t3; this.tree4 = t4;
      this.dispatchModel = (FaceDispatchModel) this.model;
   }

   @Override
   public void render(EntityNobleTree entity, float entityYaw, float partialTicks,
                      PoseStack pose, MultiBufferSource buf, int packedLight) {
      this.dispatchModel.activeFace = entity.getFaceType();
      super.render(entity, entityYaw, partialTicks, pose, buf, packedLight);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityNobleTree entity) {
      int s = entity.getTreeType();
      if (s >= 0 && s < TEX.length) return TEX[s];
      return TEX[0];
   }

   @SuppressWarnings("unchecked")
   private static class FaceDispatchModel extends EntityModel<EntityNobleTree> {
      final EntityModel<EntityNobleTree>[] inner;
      int activeFace = 0;

      FaceDispatchModel(EntityModel<EntityNobleTree>... inner) {
         this.inner = inner;
      }

      private EntityModel<EntityNobleTree> active() {
         int i = Math.max(0, Math.min(activeFace, inner.length - 1));
         return inner[i];
      }

      @Override
      public void setupAnim(EntityNobleTree entity, float limbSwing, float limbSwingAmount,
                            float ageInTicks, float netHeadYaw, float headPitch) {
         active().setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
      }

      @Override
      public void renderToBuffer(PoseStack pose, com.mojang.blaze3d.vertex.VertexConsumer buf,
                                 int light, int overlay, float r, float g, float b, float a) {
         active().renderToBuffer(pose, buf, light, overlay, r, g, b, a);
      }
   }
}
