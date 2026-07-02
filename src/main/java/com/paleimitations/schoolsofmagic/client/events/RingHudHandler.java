package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class RingHudHandler {

   private static final ResourceLocation SPELLSLOT =
      new ResourceLocation("som", "textures/gui/ring/hotbar_spellslot.png");
   private static final ResourceLocation SPELLSLOT_NORMAL =
      new ResourceLocation("som", "textures/gui/ring/hotbar_spellslot_normal.png");
   private static final ResourceLocation SELECTION =
      new ResourceLocation("som", "textures/gui/ring/hotbar_selection_spell.png");

   private static boolean lastUse = false;
   private static boolean channeling = false;
   private static int chargeTicks = 0;
   private static int animTicks = 0;
   private static boolean concentrationFired = false;
   private static int holdTicks = 0;
   private static int chargeFrames = 0;

   public static boolean isChanneling() {
      return channeling;
   }

   public static int getChargeFrames() {
      return chargeFrames;
   }

   @SubscribeEvent
   public static void onClientTick(net.minecraftforge.event.TickEvent.ClientTickEvent event) {
      if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || mc.level == null || mc.screen != null
            || !com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(player)) {
         lastUse = false;
         channeling = false;
         return;
      }
      boolean use = mc.options.keyUse.isDown();
      boolean shift = player.isShiftKeyDown();
      com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData mana =
         player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
      com.paleimitations.schoolsofmagic.common.spells.Spell spell = mana == null ? null : mana.getCurrentSpell();
      boolean channeled = spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc && sc.isChanneled();
      boolean concentration = spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom scc && scc.isConcentration();
      net.minecraft.world.item.Item ringItem =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player).getRing().getItem();
      boolean onCd = player.getCooldowns().isOnCooldown(ringItem);
      boolean chargeUp = spell != null
         && !(spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom)
         && spell.getUseLength() > 1
         && spell.isChargeUp();
      boolean builtinHold = spell != null
         && !(spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom)
         && spell.getUseLength() > 1
         && !spell.isChargeUp();
      if (!use) { concentrationFired = false; }
      boolean concActive = (concentration || chargeUp) && !shift && use && !onCd && !concentrationFired;
      boolean crouchHold = builtinHold && spell != null && spell.allowsCrouchHold();
      boolean holdActive = builtinHold && (!shift || crouchHold) && use && !onCd;
      channeling = concActive || holdActive || (channeled && !shift && use && !onCd);
      if (channeling) chargeFrames++; else chargeFrames = 0;

      if (shift && use && !lastUse && !crouchHold) {
         if (com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.tryBind(player)) {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingBind());
         }
      }

      if (concActive) {
         chargeTicks++;
         if (concentration) {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingConcentrate());
         } else {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingHold(chargeTicks, false));
         }
         if (chargeTicks >= 40) {
            if (concentration) {
               com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketRingConcentration());
            } else {
               net.minecraft.world.item.ItemStack ringStack =
                  com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player).getRing();
               spell.finishHoldEffect(ringStack, player.level(), player);
               com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketRingHold(0, true));
            }
            concentrationFired = true;
            channeling = false;
         }
      } else {
         chargeTicks = 0;
      }

      if (holdActive) {
         net.minecraft.world.item.ItemStack ringStack =
            com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player).getRing();
         holdTicks++;
         int count = Math.max(0, spell.getUseLength() - holdTicks);
         spell.rightHoldEffect(ringStack, player, count);
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketRingHold(count, false));
      } else {
         if (holdTicks > 0 && builtinHold && spell != null) {
            net.minecraft.world.item.ItemStack ringStack =
               com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player).getRing();
            spell.finishHoldEffect(ringStack, player.level(), player);
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingHold(0, true));
         }
         holdTicks = 0;
      }

      if (channeled && !shift && use && !onCd) {
         if (!lastUse) {
            spell.rightClickEffect(player.level(), player, net.minecraft.world.InteractionHand.MAIN_HAND);
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingCast());
         } else {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingChannel());
         }
      }
      lastUse = use;
   }

   @SubscribeEvent
   public static void onKey(net.minecraftforge.client.event.InputEvent.Key event) {
      if (event.getAction() != org.lwjgl.glfw.GLFW.GLFW_PRESS) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.player == null || mc.screen != null || !mc.options.keyShift.isDown()) return;
      IRingData ring = CapabilityRingData.get(mc.player);
      if (ring == null || ring.getRing().isEmpty()
            || !(ring.getRing().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing)) return;
      for (int i = 0; i < 9; i++) {
         if (mc.options.keyHotbarSlots[i].matches(event.getKey(), event.getScanCode())) {
            while (mc.options.keyHotbarSlots[i].consumeClick()) {}
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketRingSetSlot(i));
            return;
         }
      }
   }

   @SubscribeEvent
   public static void onHotbar(RenderGuiOverlayEvent.Post event) {
      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || player.isSpectator() || mc.options.hideGui) return;
      IRingData ring = CapabilityRingData.get(player);
      if (ring == null || ring.getRing().isEmpty()) return;

      int mask = ring.getSpellSlots();
      if (mask == 0) return;

      GuiGraphics gg = event.getGuiGraphics();
      int width = gg.guiWidth();
      int height = gg.guiHeight();
      int sel = player.getInventory().selected;
      float f = player.getCooldowns().getCooldownPercent(
         com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.apprentice_ring.get(), mc.getFrameTime());

      for (int i = 0; i < 9; i++) {
         if ((mask & (1 << i)) == 0) continue;
         int x = width / 2 - 91 + i * 20;
         int y = height - 22;
         gg.blit(i == 0 ? SPELLSLOT : SPELLSLOT_NORMAL, x, y, 0, 0, 22, 22, 22, 22);
         if (f > 0.0F) {
            int ix = x + 3;
            int iy = height - 19;
            int top = iy + net.minecraft.util.Mth.floor(16.0F * (1.0F - f));
            gg.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), ix, top, ix + 16, iy + 16, Integer.MAX_VALUE);
         }
         net.minecraft.world.item.ItemStack slotStack = player.getInventory().getItem(i);
         if (!slotStack.isEmpty()) {
            gg.renderItemDecorations(mc.font, slotStack, x + 3, height - 19);
         }
      }

      if (sel >= 0 && sel <= 8) {
         int x = width / 2 - 91 + sel * 20;
         if ((mask & (1 << sel)) != 0) {
            gg.blit(SELECTION, x - 1, height - 23, 0, 0, 24, 23, 24, 23);
         } else {
            gg.blit(new net.minecraft.resources.ResourceLocation("textures/gui/widgets.png"),
               x - 1, height - 23, 0, 22, 24, 22, 256, 256);
         }
      }
   }
}
