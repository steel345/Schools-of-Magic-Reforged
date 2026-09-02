package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelWornNecklace;
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

public class LayerWornNecklace<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation BAND = new ResourceLocation("som", "textures/entity/curio/necklace.png");
   private static final ResourceLocation GEM = new ResourceLocation("som", "textures/entity/curio/necklace_gem_overlay.png");

   private final ModelWornNecklace model;

   public LayerWornNecklace(RenderLayerParent<T, M> parent, EntityModelSet models) {
      super(parent);
      this.model = new ModelWornNecklace(models.bakeLayer(ModelWornNecklace.LAYER));
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int light,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!(entity instanceof Player player) || player.isInvisible()) return;

      if (!com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.shows(player, com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData.SHOW_TALISMAN)) return;
      ItemStack necklace = GarmentSlots.wornNecklace(player);
      if (necklace.isEmpty()) return;

      if (!player.getInventory().getArmor(2).isEmpty()) return;

      int metal = LayerWornRing.metalTint(GarmentSlots.metalOf(necklace));

      pose.pushPose();
      this.getParentModel().body.translateAndRotate(pose);

      VertexConsumer band = buf.getBuffer(RenderType.entityCutoutNoCull(BAND));
      this.model.render(pose, band, light, OverlayTexture.NO_OVERLAY,
         (metal >> 16 & 0xFF) / 255.0F, (metal >> 8 & 0xFF) / 255.0F, (metal & 0xFF) / 255.0F, 1.0F);

      int gem = LayerWornRing.stoneTint(necklace);
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
