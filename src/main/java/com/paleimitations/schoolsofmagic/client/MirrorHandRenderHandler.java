package com.paleimitations.schoolsofmagic.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.ItemMagicMirror;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT,
   bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MirrorHandRenderHandler {
   @SubscribeEvent
   public static void onRenderHand(RenderHandEvent event) {
      ItemStack stack = event.getItemStack();
      if (!(stack.getItem() instanceof ItemMagicMirror)) {
         return;
      }
      if (Minecraft.getInstance().player == null) {
         return;
      }
      net.minecraft.world.entity.player.Player p = Minecraft.getInstance().player;
      if (!p.isUsingItem() || p.getUseItem() != stack) {
         return;
      }
      int used = stack.getItem().getUseDuration(stack) - p.getUseItemRemainingTicks();
      float ease = (used + event.getPartialTick()) / 8.0F;
      if (ease <= 0.0F) {
         return;
      }
      if (ease > 1.0F) {
         ease = 1.0F;
      }
      ease = ease * ease * (3.0F - 2.0F * ease);
      boolean left = event.getHand() == InteractionHand.OFF_HAND;
      PoseStack pose = event.getPoseStack();
      pose.translate((left ? 0.32D : -0.32D) * ease, 0.42D * ease, 0.30D * ease);
      if (!left && !MirrorPortalRenderer.isPortalPass() && MirrorScryRenderHandler.isScrying()
            && MirrorPortalRenderer.renderScryingMirror(event, p, stack)) {
         event.setCanceled(true);
      }
   }
}
