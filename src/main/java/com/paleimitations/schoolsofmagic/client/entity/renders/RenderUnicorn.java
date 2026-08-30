package com.paleimitations.schoolsofmagic.client.entity.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelUnicorn;
import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;
import com.paleimitations.schoolsofmagic.common.entity.UnicornColor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class RenderUnicorn extends MobRenderer<EntityUnicorn, ModelUnicorn<EntityUnicorn>> {
   private static final float BABY_SCALE = 0.5F;

   public static final ResourceLocation SKIN = new ResourceLocation("som", "textures/entity/unicorn_skin.png");
   public static final ResourceLocation DEFAULT = new ResourceLocation("som", "textures/entity/unicorn_default.png");
   public static final ResourceLocation HAIR = new ResourceLocation("som", "textures/entity/unicorn_hair_tint.png");

   public RenderUnicorn(EntityRendererProvider.Context context) {
      super(context, new ModelUnicorn<>(context.bakeLayer(ModelUnicorn.LAYER_LOCATION)), 0.5F);
      this.addLayer(new HairLayer(this));
   }

   @Override
   protected void scale(EntityUnicorn unicorn, PoseStack pose, float partialTick) {
      float scale = unicorn.isBaby() ? BABY_SCALE : 1.0F;
      pose.scale(scale, scale, scale);
      this.shadowRadius = 0.5F * scale;
      super.scale(unicorn, pose, partialTick);
   }

   @Override
   public ResourceLocation getTextureLocation(EntityUnicorn entity) {
      return entity.getColor().isDefault() ? DEFAULT : SKIN;
   }

   static class HairLayer extends RenderLayer<EntityUnicorn, ModelUnicorn<EntityUnicorn>> {
      HairLayer(RenderUnicorn parent) {
         super(parent);
      }

      @Override
      public void render(PoseStack pose, MultiBufferSource buf, int light, EntityUnicorn unicorn,
                         float limbSwing, float limbSwingAmount, float partialTick,
                         float age, float netHeadYaw, float headPitch) {
         UnicornColor color = unicorn.getColor();
         if (color.isDefault() || unicorn.isInvisible()) return;

         int hair = color.getHair();
         float r = (hair >> 16 & 0xFF) / 255.0F;
         float g = (hair >> 8 & 0xFF) / 255.0F;
         float b = (hair & 0xFF) / 255.0F;
         this.getParentModel().renderToBuffer(pose, buf.getBuffer(RenderType.entityCutoutNoCull(HAIR)),
            light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
      }
   }
}
