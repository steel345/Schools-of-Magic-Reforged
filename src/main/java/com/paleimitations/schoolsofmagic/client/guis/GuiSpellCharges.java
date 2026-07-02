package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.ManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.handlers.RingCastHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand;
import com.paleimitations.schoolsofmagic.common.items.ItemBaseWand;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GuiSpellCharges {

   private static final ResourceLocation SPELL_CHARGE_ICONS = new ResourceLocation("som", "textures/gui/spell_charge_icons.png");

   public static boolean isHudOpen() {
      LocalPlayer player = Minecraft.getInstance().player;
      return player != null && ClientProxy.OPEN_SPELL_RING.isDown() && wandOrRingActive(player);
   }

   private static ResourceLocation hud(String metal) {
      return new ResourceLocation("som", "textures/gui/hud_elements_" + metal + ".png");
   }

   private static boolean validMetal(String m) {
      switch (m) {
         case "gold":
         case "silver":
         case "void":
         case "copper":
         case "bronze":
         case "brass":
         case "iron":
         case "steel":
            return true;
         default:
            return false;
      }
   }

   public static String getMetal(LocalPlayer player) {
      ItemStack main = player.getMainHandItem();
      if (main.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(main)
            && com.paleimitations.schoolsofmagic.common.items.BookDecorations.hasJewel(main)) {
         String jm = com.paleimitations.schoolsofmagic.common.items.BookDecorations.emblemMetal(main);
         if ("netherite".equals(jm)) {
            jm = "steel";
         } else if ("tenebrium".equals(jm)) {
            jm = "void";
         }
         if (jm != null && validMetal(jm)) {
            return jm;
         }
      }
      if (main.getItem() instanceof ItemApprenticeWand) {
         return "gold";
      }
      IWandData wand = main.getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);
      if (wand != null && wand.getHandleType() != null) {
         String m = wand.getHandleType().name().toLowerCase();
         if (validMetal(m)) {
            return m;
         }
      }
      IRingData ring = CapabilityRingData.get(player);
      if (ring != null && !ring.getRing().isEmpty() && ring.getRing().hasTag()) {
         String m = ring.getRing().getTag().getString("ring_metal");
         if (validMetal(m)) {
            return m;
         }
      }
      return "gold";
   }

   public static boolean wandOrRingActive(LocalPlayer player) {
      return player.getMainHandItem().getItem() instanceof ItemBaseWand
            || RingCastHandler.isRingActive(player)
            || (player.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
                && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(player.getMainHandItem()));
   }

   @SubscribeEvent
   public void onRender(RenderGuiOverlayEvent.Post event) {
      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) {
         return;
      }
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || player.isSpectator() || mc.options.hideGui) {
         return;
      }
      if (!wandOrRingActive(player)) {
         return;
      }
      IManaData data = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data == null) {
         return;
      }

      GuiGraphics gg = event.getGuiGraphics();
      ResourceLocation hud = hud(getMetal(player));
      int level = data.getLevel();
      int maxSpellCharge = Math.max(1, data.getLargestChargeLevel() + 1);

      com.mojang.blaze3d.systems.RenderSystem.enableBlend();
      com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
      if (ClientProxy.OPEN_SPELL_RING.isDown()) {
         this.drawColumn(gg, data, hud, level, maxSpellCharge);
      } else {
         this.drawCompact(gg, data, hud, level);
      }
   }

   private static Spell activeSpell(IManaData data) {
      net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
      if (player != null) {
         net.minecraft.world.item.ItemStack main = player.getMainHandItem();
         if (main.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
               && com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isCastingMode(main)) {
            return com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.castingInstance(player, main);
         }
      }
      return data.getCurrentSpell();
   }

   private void drawColumn(GuiGraphics gg, IManaData data, ResourceLocation hud, int level, int maxSpellCharge) {
      int screenH = gg.guiHeight();
      Spell current = activeSpell(data);
      float chargeScale = Math.min((float) screenH * 0.8F / (float) maxSpellCharge / 38.0F, 1.0F);
      gg.pose().pushPose();
      gg.pose().translate(0.0F, screenH / 2.0F, 0.0F);
      gg.pose().scale(chargeScale, chargeScale, 1.0F);
      gg.pose().translate(0.0F, -(38.0F * (float) maxSpellCharge) / 2.0F, 0.0F);
      for (int i = 0; i < maxSpellCharge; i++) {
         boolean hasSpell = current != null;
         boolean isSelected = hasSpell && current.currentSpellChargeLevel == i;
         boolean usable = hasSpell && i >= current.getMinimumSpellChargeLevel() && i <= current.getMaximumSpellChargeLevel();
         float countdown = (float) data.getCountdowns()[i];
         float maxCountdown = (float) ManaData.MAX_COUNTDOWNS[i];
         float cooldown = (maxCountdown - countdown) / maxCountdown;
         gg.blit(hud, 0, 38 * i, isSelected ? 0 : 38, 0, 38, 38);
         int a = data.getMaxCharges(i, level);
         int b = data.getCharges()[i];
         for (int j = 1; j <= a; j++) {
            gg.blit(hud, 16 * j + 19, 38 * i + 9, 76, 0, 20, 20);
            if (j <= b) {
               if (usable) {
                  gg.blit(SPELL_CHARGE_ICONS, 16 * j + 21, 38 * i + 12, 14 * i, 192 + (isSelected ? 0 : 14), 14, 14);
               } else {
                  gg.blit(SPELL_CHARGE_ICONS, 16 * j + 21, 38 * i + 12, 0, 220, 14, 14);
               }
            }
         }
         gg.blit(SPELL_CHARGE_ICONS, 3, 38 * i + 3, i % 3 * 32 + 96, i / 3 * 32, 32, 32);
         gg.blit(SPELL_CHARGE_ICONS, 3, 38 * i + 3 + Math.round(32.0F * countdown / maxCountdown),
            i % 3 * 32, i / 3 * 32 + (usable ? 0 : 96) + Math.round(32.0F * countdown / maxCountdown),
            32, Math.round(32.0F * cooldown));
      }
      gg.pose().popPose();
   }

   private void drawCompact(GuiGraphics gg, IManaData data, ResourceLocation hud, int level) {
      Spell spell = activeSpell(data);
      if (spell == null) {
         return;
      }
      int screenH = gg.guiHeight();
      int i = spell.currentSpellChargeLevel;
      boolean usable = i >= spell.getMinimumSpellChargeLevel();
      float countdown = (float) data.getCountdowns()[i];
      float maxCountdown = (float) ManaData.MAX_COUNTDOWNS[i];
      float cooldown = (maxCountdown - countdown) / maxCountdown;

      gg.blit(hud, 0, screenH - 38, 0, 77, 38, 38);
      this.drawSpellIcon32(gg, spell, 3, screenH - 35);

      gg.pose().pushPose();
      float scale = 0.65F;
      gg.pose().translate(38.0F * (1.0F - scale) / 2.0F, (float) screenH - 38.0F * (1.0F + scale), 0.0F);
      gg.pose().scale(scale, scale, 1.0F);
      gg.blit(hud, 0, 0, 38, 0, 38, 38);
      gg.blit(SPELL_CHARGE_ICONS, 3, 3, i % 3 * 32 + 96, i / 3 * 32, 32, 32);
      gg.blit(SPELL_CHARGE_ICONS, 3, 3 + Math.round(32.0F * countdown / maxCountdown),
         i % 3 * 32, i / 3 * 32 + (usable ? 0 : 96) + Math.round(32.0F * countdown / maxCountdown),
         32, Math.round(32.0F * cooldown));
      int a = data.getMaxCharges(i, level);
      int b = data.getCharges()[i];
      for (int j = 1; j <= a; j++) {
         gg.blit(hud, 9, -1 - 16 * j, 136, 0, 20, 20);
         if (j <= b) {
            if (usable) {
               gg.blit(SPELL_CHARGE_ICONS, 12, 3 - 16 * j, 14 * i, 192, 14, 14);
            } else {
               gg.blit(SPELL_CHARGE_ICONS, 12, 3 - 16 * j, 0, 220, 14, 14);
            }
         }
      }
      gg.pose().popPose();

      boolean canCast = data.hasChargeLevel(i) || spell.getRemainingUses() > 0;
      net.minecraft.world.effect.MobEffect durEffect = spell.getDurationEffect();
      if (spell.usesTimedBar()) {
         if (canCast || spell.getTimedBarRatio() < 1.0F) {
            this.drawDurationBar(gg, hud, screenH, i, spell.getTimedBarRatio());
         }
      } else if (durEffect != null && spell.getMaxDuration() > 0) {
         int cur = Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(durEffect)
            ? Minecraft.getInstance().player.getEffect(durEffect).getDuration() : 0;
         if (cur > 0 || canCast) {
            float ratio = Math.min(1.0F, (float) cur / (float) spell.getMaxDuration());
            this.drawDurationBar(gg, hud, screenH, i, ratio);
         }
      } else if (canCast && (spell.usesUsesBar() || spell.getUsesPerCharge(i) >= 100)) {
         int max = Math.max(1, spell.getMaxUses());
         float ratio = Math.min(1.0F, (float) spell.getRemainingUses() / (float) max);
         this.drawDurationBar(gg, hud, screenH, i, ratio);
      } else if (canCast && spell.getUsesPerCharge(i) > 1) {
         net.minecraft.client.gui.Font font = Minecraft.getInstance().font;
         int rawUses = spell.getRemainingUses() > 0 ? spell.getRemainingUses() : spell.getUsesPerCharge(i);
         String remUses = String.valueOf(smoothUses(spell, rawUses));
         gg.blit(hud, 40, screenH - 29, 167, 21, 21, 20);
         float scaleUses = Math.min(11.0F / (float) font.width(remUses), 10.0F / 9.0F);
         gg.pose().pushPose();
         gg.pose().translate(51.0F - (float) font.width(remUses) * scaleUses / 2.0F, (float) screenH - 18.0F - 9.0F * scaleUses / 2.0F, 0.0F);
         gg.pose().scale(scaleUses, scaleUses, 1.0F);
         gg.drawString(font, remUses, 0, 1, 0x000000, false);
         gg.drawString(font, remUses, 0, 0, 0xFFFFFF, false);
         gg.pose().popPose();
      }
   }

   private static String usesSpell = "";
   private static int usesDisplayed = -1;
   private static int usesPending = -1;
   private static long usesPendingSince = 0L;

   private int smoothUses(Spell spell, int raw) {
      String name = spell.getName();
      long now = System.currentTimeMillis();
      if (!name.equals(usesSpell)) {
         usesSpell = name;
         usesDisplayed = raw;
         usesPending = -1;
         return raw;
      }
      if (raw <= usesDisplayed) {
         usesDisplayed = raw;
         usesPending = -1;
      } else if (usesPending != raw) {
         usesPending = raw;
         usesPendingSince = now;
      } else if (now - usesPendingSince > 200L) {
         usesDisplayed = raw;
         usesPending = -1;
      }
      return usesDisplayed;
   }

   private void drawDurationBar(GuiGraphics gg, ResourceLocation hud, int screenH, int chargeLevel, float ratio) {
      int width = Math.round(ratio * 64.0F);
      gg.pose().pushPose();
      gg.pose().translate(40.0F, (float) (screenH - 13), 0.0F);
      gg.pose().scale(0.65F, 0.65F, 1.0F);
      gg.blit(hud, 0, 0, 101, 49, 72, 12);
      if (width > 0) {
         gg.blit(SPELL_CHARGE_ICONS, 4, 4, 101, 151 + 4 * chargeLevel, width, 4);
      }
      gg.pose().popPose();
   }

   private static final ResourceLocation EMPTY_CIRCLE = new ResourceLocation("som", "textures/gui/spells/empty_circle.png");

   private void drawSpellIcon32(GuiGraphics gg, Spell spell, int x, int y) {
      if (spell instanceof SpellCustom custom && custom.getEffect() != null) {
         gg.blit(EMPTY_CIRCLE, x, y, 0, 0, 32, 32, 32, 32);
         TextureAtlasSprite sprite = Minecraft.getInstance().getMobEffectTextures().get(custom.getEffect());
         gg.blit(x + 4, y + 4, 0, 24, 24, sprite);
         return;
      }
      gg.blit(spell.getSpellIcon(), x, y, 0, 0, 32, 32, 32, 32);
   }
}
