package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelWisp;
import com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderAcolyteWisp extends MobRenderer<EntityAcolyteWisp, ModelWisp<EntityAcolyteWisp>> {

   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/entity/wisp.png");

   public RenderAcolyteWisp(EntityRendererProvider.Context context) {
      super(context, new ModelWisp<>(context.bakeLayer(ModelWisp.LAYER_LOCATION)), 0.3F);
      this.shadowRadius = 0.2F;
   }

   @Override
   public ResourceLocation getTextureLocation(EntityAcolyteWisp entity) {
      return TEXTURE;
   }

   @Override
   protected int getBlockLightLevel(EntityAcolyteWisp entity, net.minecraft.core.BlockPos pos) {
      return 15;
   }

   @Override
   protected void scale(EntityAcolyteWisp entity, com.mojang.blaze3d.vertex.PoseStack pose, float partialTick) {
      pose.translate(0.0F, 0.7F, 0.0F);
      pose.scale(0.6F, 0.6F, 0.6F);
   }
}
