package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.paleimitations.schoolsofmagic.client.entity.model.ModelWornRing;
import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.items.RingItemHelper;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
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

public class LayerWornRing<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation BAND = new ResourceLocation("som", "textures/entity/curio/ring.png");
   private static final ResourceLocation GEM = new ResourceLocation("som", "textures/entity/curio/ring_gem_overlay.png");

   private final ModelWornRing model;

   public LayerWornRing(RenderLayerParent<T, M> parent, EntityModelSet models) {
      super(parent);
      this.model = new ModelWornRing(models.bakeLayer(ModelWornRing.LAYER));
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int light,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!(entity instanceof Player player) || player.isInvisible()) return;

      if (!com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.shows(player, com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData.SHOW_RING)) return;
      ItemStack ring = wornRing(player);
      if (ring.isEmpty()) return;

      int metal = metalTint(com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.metalOf(ring));

      pose.pushPose();
      this.getParentModel().rightArm.translateAndRotate(pose);

      VertexConsumer band = buf.getBuffer(RenderType.entityCutoutNoCull(BAND));
      this.model.render(pose, band, light, OverlayTexture.NO_OVERLAY,
         (metal >> 16 & 0xFF) / 255.0F, (metal >> 8 & 0xFF) / 255.0F, (metal & 0xFF) / 255.0F, 1.0F);

      int gem = stoneTint(ring);
      if (gem >= 0) {
         float r = (gem >> 16 & 0xFF) / 255.0F;
         float g = (gem >> 8 & 0xFF) / 255.0F;
         float b = (gem & 0xFF) / 255.0F;

         VertexConsumer stone = buf.getBuffer(RenderType.entityCutoutNoCull(GEM));
         this.model.render(pose, stone, light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);

         if (isWorking(player)) {
            VertexConsumer glow = buf.getBuffer(RenderType.eyes(GEM));
            this.model.render(pose, glow, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
         }
      }
      pose.popPose();
   }

   private static ItemStack wornRing(Player player) {
      return RingItemHelper.getWornRing(player);
   }

   // only lights up for a ring cast, a wand in hand is doing the work itself
   public static boolean isWorking(Player player) {
      if (player.isUsingItem() && player.getUseItem().getItem() instanceof ItemBaseWand) return false;
      return RingCastHandler.isConcentrating(player) || RingCastHandler.isRingChanneling(player);
   }

   public static int stoneTint(ItemStack piece) {
      IWandData.EnumGemType set =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.gemOf(piece);
      if (set != null) return gemTint(set);
      if (piece.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing
            || piece.is(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.apprentice_crown.get())
            || piece.is(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.apprentice_necklace.get())) {
         return gemTint(null);
      }
      return -1;
   }

   public static int metalTint(IWandData.EnumHandleType metal) {
      if (metal == null) return 0xFFD93F;
      return switch (metal) {
         case COPPER -> 0xE28243;
         case BRONZE -> 0xF4AA3E;
         case BRASS -> 0xFFD25A;
         case GOLD -> 0xFFD93F;
         case SILVER -> 0xF6F6F6;
         case IRON -> 0xB1B1B1;
         case STEEL -> 0x454545;
         case VOID -> 0x312A4F;
      };
   }

   public static int gemTint(IWandData.EnumGemType gem) {
      if (gem == null) return 0x40E6D8;
      return switch (gem) {
         case RUBY -> 0xC55858;
         case SUNSTONE -> 0xF6A668;
         case CITRINE -> 0xC6AF55;
         case PERIDOT -> 0x52C770;
         case JADE -> 0x97C76A;
         case TURQUOISE -> 0x63D3CC;
         case AQUAMARINE -> 0x6EC4E7;
         case SAPPHIRE -> 0x9093FF;
         case AMETHYST -> 0xB486D6;
         case GARNET -> 0xB669A2;
         case ROSE_QUARTZ -> 0xB47B90;
         case MOONSTONE -> 0xBCBCBC;
         case PUTRIDITE -> 0x898989;
         case OPAL -> 0x484747;
         case ONYX -> 0x363636;
         case SMOKY_QUARTZ -> 0x987366;
         case DIAMOND -> 0x83B1F0;
         case EMERALD -> 0x41F384;
      };
   }

   public static int glowColor(Player player) {
      ItemStack piece = RingItemHelper.getWorn(player);
      if (piece.isEmpty() || !isWorking(player)) return -1;
      return stoneTint(piece);
   }
}
