package com.paleimitations.schoolsofmagic.client.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.paleimitations.schoolsofmagic.client.entity.model.ModelWornPouch;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData;
import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LayerWornPouch<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation BASE = new ResourceLocation("som", "textures/entity/curio/pouch.png");
   private static final ResourceLocation OVERLAY_HERB = new ResourceLocation("som", "textures/entity/curio/pouch_overlay.png");
   private static final ResourceLocation OVERLAY_SILVER = new ResourceLocation("som", "textures/entity/curio/pouch_overlay_silver.png");

   private static final int POTION_BROWN = 0xA06540;

   private final ModelWornPouch model;

   public LayerWornPouch(RenderLayerParent<T, M> parent, EntityModelSet models) {
      super(parent);
      this.model = new ModelWornPouch(models.bakeLayer(ModelWornPouch.LAYER));
   }

   @Override
   public void render(PoseStack pose, MultiBufferSource buf, int light,
                      T entity, float limbSwing, float limbSwingAmount,
                      float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
      if (!(entity instanceof Player player) || player.isInvisible()) return;

      ItemStack charm = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .findWornPouch(player, s -> s.getItem() instanceof ItemHerbPouch
            || s.getItem() == ItemRegistry.potion_bag.get());
      if (charm.isEmpty()) return;

      int tint;
      ResourceLocation overlay;
      if (charm.getItem() instanceof ItemHerbPouch pouch) {
         tint = pouch.getColor(charm);
         overlay = OVERLAY_HERB;
      } else if (charm.getItem() == ItemRegistry.potion_bag.get()) {
         tint = POTION_BROWN;
         overlay = OVERLAY_SILVER;
      } else {
         return;
      }

      float lighten = 0.3F;
      float r = (tint >> 16 & 0xFF) / 255.0F;
      float g = (tint >> 8 & 0xFF) / 255.0F;
      float b = (tint & 0xFF) / 255.0F;
      r = r + (1.0F - r) * lighten;
      g = g + (1.0F - g) * lighten;
      b = b + (1.0F - b) * lighten;

      pose.pushPose();
      this.getParentModel().body.translateAndRotate(pose);
      VertexConsumer baseVc = buf.getBuffer(RenderType.entityCutoutNoCull(BASE));
      this.model.render(pose, baseVc, light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
      VertexConsumer overVc = buf.getBuffer(RenderType.entityCutoutNoCull(overlay));
      this.model.render(pose, overVc, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      pose.popPose();
   }
}
