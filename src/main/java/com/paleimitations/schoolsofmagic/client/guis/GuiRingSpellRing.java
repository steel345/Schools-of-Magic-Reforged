package com.paleimitations.schoolsofmagic.client.guis;

import com.mojang.math.Axis;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class GuiRingSpellRing {

   private static int animationTick;
   private static int prevSlot;
   private static int animateSlot;

   public static int ringSlots(IManaData mana) {
      net.minecraft.world.item.ItemStack ring = net.minecraft.world.item.ItemStack.EMPTY;
      net.minecraft.client.player.LocalPlayer p = net.minecraft.client.Minecraft.getInstance().player;
      if (p != null) {
         IRingData rd = CapabilityRingData.get(p);
         if (rd != null) ring = rd.getRing();
      }
      if (ring.hasTag() && ring.getTag().contains("ring_metal")) {
         int level = mana.getLevel();
         if (level < 5) return 3;
         else if (level < 10) return 4;
         else if (level < 15) return 5;
         else if (level < 20) return 6;
         else if (level < 25) return 7;
         else if (level < 30) return 8;
         else return level < 35 ? 9 : 10;
      }
      return Math.max(1, Math.min(4, mana.getLevel()));
   }

   private static ResourceLocation ringBg(IRingData ring) {
      String m = "gold";
      net.minecraft.world.item.ItemStack rs = ring.getRing();
      if (rs.hasTag()) {
         String mm = rs.getTag().getString("ring_metal");
         switch (mm) {
            case "gold": case "silver": case "void": case "copper":
            case "bronze": case "brass": case "iron": case "steel": m = mm; break;
            default: m = "gold";
         }
      }
      return new ResourceLocation("som", "textures/gui/hud_elements_" + m + ".png");
   }

   private static IRingData activeRing(LocalPlayer player) {
      if (player == null || !ClientProxy.OPEN_SPELL_RING.isDown()) return null;
      if (!com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler.isRingActive(player)) return null;
      if (player.getMainHandItem().getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).isPresent()) return null;
      IRingData ring = CapabilityRingData.get(player);
      if (ring == null || ring.getRing().isEmpty()) return null;
      return ring;
   }

   @SubscribeEvent
   public static void hideCrosshair(RenderGuiOverlayEvent.Pre event) {
      if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()
            && activeRing(Minecraft.getInstance().player) != null) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onRender(RenderGuiOverlayEvent.Post event) {
      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || player.isSpectator() || mc.options.hideGui) return;
      IRingData ring = activeRing(player);
      if (ring == null) return;
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) return;

      GuiGraphics gg = event.getGuiGraphics();
      int xPos = gg.guiWidth() / 2 - 57;
      int yPos = gg.guiHeight() / 2 - 57;
      ResourceLocation bg = ringBg(ring);

      gg.blit(bg, xPos, yPos, 0, 115, 114, 114);

      int slots = ringSlots(mana);
      int sel = mana.getCurrentSpellSlot() % slots;
      Spell selSpell = mana.getSpell(sel);
      if (selSpell != null) {
         Component s = selSpell instanceof SpellCustom cs && cs.hasName()
            ? Component.literal(cs.getCustomName())
            : Component.translatable("spell." + selSpell.getName() + ".name");
         gg.drawString(mc.font, s, gg.guiWidth() / 2 - mc.font.width(s) / 2, yPos - 26, 0xFFFFFF, false);
      }

      if (animationTick > 0) animationTick--;
      if (prevSlot != sel) {
         if (animationTick < 36) animationTick += 18;
         animateSlot = prevSlot;
      }
      if (animationTick == 0) animateSlot = sel;
      prevSlot = sel;

      float difference = (float) (animateSlot - sel);
      if (Math.abs(difference) >= (float) (slots - 1) && difference < 0.0F) {
         difference = (float) (animateSlot - sel + slots);
      }
      float angle = animateSlot == sel
         ? (float) sel / (float) slots * 360.0F
         : ((float) sel + difference * ((float) animationTick + mc.getPartialTick()) / 18.0F) / (float) slots * 360.0F;
      if (difference > 0.0F && Math.abs(difference) >= (float) (slots - 1)) {
         difference = (float) (animateSlot - sel - slots);
         angle = ((float) sel + difference * ((float) animationTick + mc.getPartialTick()) / 18.0F) / (float) slots * 360.0F;
      }

      for (int i = 0; i < slots; i++) {
         float radius = 54.0F;
         Spell sp = mana.getSpell(i);
         gg.pose().pushPose();
         gg.pose().translate(gg.guiWidth() / 2.0F, gg.guiHeight() / 2.0F, 0.0F);
         gg.pose().mulPose(Axis.ZP.rotationDegrees(-angle + (float) i / (float) slots * 360.0F));
         gg.blit(bg, -19, -54 - 19, 38, 0, 38, 38);
         if (sp != null) drawIcon(gg, 0.0F, -radius, sp);
         gg.pose().popPose();
      }

      gg.pose().pushPose();
      gg.pose().translate(gg.guiWidth() / 2.0F, gg.guiHeight() / 2.0F, 0.0F);
      gg.pose().scale(3.0F, 3.0F, 1.0F);
      gg.pose().translate(-8.0F, -8.0F, 0.0F);
      gg.renderItem(ring.getRing(), 0, 0);
      gg.flush();
      gg.pose().popPose();

      float cd = player.getCooldowns().getCooldownPercent(
         com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.apprentice_ring.get(), mc.getFrameTime());
      if (cd > 0.0F) {
         int cx = gg.guiWidth() / 2;
         int cy = gg.guiHeight() / 2;
         int top = cy - 24 + net.minecraft.util.Mth.floor(48.0F * (1.0F - cd));
         gg.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), cx - 24, top, cx + 24, cy + 24, Integer.MAX_VALUE);
      }
   }

   private static final ResourceLocation EMPTY_CIRCLE = new ResourceLocation("som", "textures/gui/spells/empty_circle.png");

   private static void drawIcon(GuiGraphics gg, float xPos, float yPos, Spell spell) {
      int x = Math.round(xPos - 16);
      int y = Math.round(yPos - 16);
      if (spell instanceof SpellCustom custom && custom.getEffect() != null) {
         gg.blit(EMPTY_CIRCLE, x, y, 0, 0, 32, 32, 32, 32);
         TextureAtlasSprite sprite = Minecraft.getInstance().getMobEffectTextures().get(custom.getEffect());
         gg.blit(x + 4, y + 4, 0, 24, 24, sprite);
         return;
      }
      gg.blit(spell.getSpellIcon(), x, y, 0, 0, 32, 32, 32, 32);
   }
}
