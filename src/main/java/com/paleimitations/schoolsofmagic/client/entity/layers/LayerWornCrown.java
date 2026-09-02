package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelWornCrown;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LayerWornCrown<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation BAND = new ResourceLocation("som", "textures/entity/curio/crown.png");
   private static final ResourceLocation GEM = new ResourceLocation("som", "textures/entity/curio/crown_gem_overlay.png");

   private final ModelWornCrown model;

   public LayerWornCrown(RenderLayerParent<T, M> parent, EntityModelSet models) {
      super(parent);
      this.model = new ModelWornCrown(models.bakeLayer(ModelWornCrown.LAYER));
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int light,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!(entity instanceof Player player) || player.isInvisible()) return;

      if (!com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.shows(player, com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData.CROWN)) return;
      ItemStack crown = GarmentSlots.wornCrown(player);
      if (crown.isEmpty()) return;

      if (!player.getInventory().getArmor(3).isEmpty()) return;

      int metal = LayerWornRing.metalTint(GarmentSlots.metalOf(crown));

      pose.pushPose();
      this.getParentModel().head.translateAndRotate(pose);

      VertexConsumer band = buf.getBuffer(RenderType.entityCutoutNoCull(BAND));
      this.model.render(pose, band, light, OverlayTexture.NO_OVERLAY,
         (metal >> 16 & 0xFF) / 255.0F, (metal >> 8 & 0xFF) / 255.0F, (metal & 0xFF) / 255.0F, 1.0F);

      int gem = LayerWornRing.stoneTint(crown);
      if (gem >= 0) {
         float r = (gem >> 16 & 0xFF) / 255.0F;
         float g = (gem >> 8 & 0xFF) / 255.0F;
         float b = (gem & 0xFF) / 255.0F;

         VertexConsumer stone = buf.getBuffer(RenderType.entityCutoutNoCull(GEM));
         this.model.render(pose, stone, light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);

         if (LayerWornRing.isWorking(player)) {
            VertexConsumer glow = buf.getBuffer(RenderType.eyes(GEM));
            this.model.render(pose, glow, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
         }
      }
      pose.popPose();
   }
}
