package com.paleimitations.schoolsofmagic.client.guis;

import java.awt.Color;

import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GuiSpellRing {
   private int animationTick;
   private int prevSlot;
   private int animateSlot;
   private boolean apprenticeWand;

   private static final float WAND_BRIGHTNESS = 1.5F;

   public GuiSpellRing() {}

   @SubscribeEvent
   public void hideCrosshair(RenderGuiOverlayEvent.Pre event) {
      if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()
            && ClientProxy.OPEN_SPELL_RING.isDown()
            && Minecraft.getInstance().player != null
            && Minecraft.getInstance().player.getMainHandItem().getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).isPresent()) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public void renderSpellRing(RenderGuiOverlayEvent.Post event) {

      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || player.isSpectator() || mc.options.hideGui) return;
      if (!ClientProxy.OPEN_SPELL_RING.isDown()) return;
      IManaData manaData = player.getCapability(CapabilityManaData.CAP).orElse(null);
      IWandData wandData = player.getMainHandItem().getCapability(CapabilityWandData.WAND_DATA_CAPABILITY).orElse(null);
      if (manaData == null || wandData == null) return;
      this.apprenticeWand = player.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand;

      GuiGraphics gg = event.getGuiGraphics();
      int xPos = gg.guiWidth() / 2 - 57;
      int yPos = gg.guiHeight() / 2 - 57;

      gg.blit(getTexture(wandData), xPos, yPos, 0, 115, 114, 114);

      if (manaData.getCurrentSpell() != null) {
         Component s = manaData.getCurrentSpell() instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom cs && cs.hasName()
            ? Component.literal(cs.getCustomName())
            : Component.translatable("spell." + manaData.getCurrentSpell().getName() + ".name");
         gg.drawString(mc.font, s, gg.guiWidth() / 2 - mc.font.width(s) / 2, yPos - 26, 0xFFFFFF, false);
      }

      if (this.animationTick > 0) this.animationTick--;
      if (this.prevSlot != manaData.getCurrentSpellSlot()) {
         if (this.animationTick < 36) this.animationTick += 18;
         this.animateSlot = this.prevSlot;
      }
      if (this.animationTick == 0) this.animateSlot = manaData.getCurrentSpellSlot();
      this.prevSlot = manaData.getCurrentSpellSlot();
      int spells = getSlotNumber(manaData, wandData);
      float difference = (float) (this.animateSlot - manaData.getCurrentSpellSlot());
      if (Math.abs(difference) >= (float) (spells - 1) && difference < 0.0F) {
         difference = (float) (this.animateSlot - manaData.getCurrentSpellSlot() + spells);
      }
      float angle = this.animateSlot == manaData.getCurrentSpellSlot()
         ? (float) manaData.getCurrentSpellSlot() / (float) spells * 360.0F
         : ((float) manaData.getCurrentSpellSlot() + difference * ((float) this.animationTick + mc.getPartialTick()) / 18.0F) / (float) spells * 360.0F;
      if (difference > 0.0F && Math.abs(difference) >= (float) (spells - 1)) {
         difference = (float) (this.animateSlot - manaData.getCurrentSpellSlot() - spells);
         angle = ((float) manaData.getCurrentSpellSlot() + difference * ((float) this.animationTick + mc.getPartialTick()) / 18.0F) / (float) spells * 360.0F;
      }

      for (int i = 0; i < spells; i++) {
         float radius = 54.0F;
         Spell spell = manaData.getSpell(i);
         gg.pose().pushPose();
         gg.pose().translate(gg.guiWidth() / 2.0F, gg.guiHeight() / 2.0F, 0.0F);
         gg.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-angle + (float) i / (float) spells * 360.0F));
         this.drawSpellSlot(gg, wandData);
         if (spell != null) this.drawSpellIcon(gg, 0.0F, -radius, spell);
         gg.pose().popPose();
      }

      gg.pose().pushPose();
      gg.pose().translate(gg.guiWidth() / 2.0F, gg.guiHeight() / 2.0F, 0.0F);
      gg.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-45.0F));
      gg.pose().scale(3.0F, 3.0F, 1.0F);
      gg.pose().translate(-8.0F, -8.0F, 0.0F);

      com.mojang.blaze3d.systems.RenderSystem.setShaderColor(WAND_BRIGHTNESS, WAND_BRIGHTNESS, WAND_BRIGHTNESS, 1.0F);
      this.drawItemStack(gg, player.getMainHandItem());
      gg.flush();
      com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      gg.pose().popPose();

      net.minecraft.client.player.LocalPlayer lp = Minecraft.getInstance().player;
      if (lp != null) {
         float f = lp.getCooldowns().getCooldownPercent(player.getMainHandItem().getItem(), Minecraft.getInstance().getFrameTime());
         if (f > 0.0F) {
            int cx = (int) (gg.guiWidth() / 2.0F);
            int cy = (int) (gg.guiHeight() / 2.0F);
            int left = cx - 24;
            int boxTop = cy - 24 + 2;
            int top = net.minecraft.util.Mth.floor(48.0F * (1.0F - f));
            gg.fill(net.minecraft.client.renderer.RenderType.guiOverlay(), left, boxTop + top, left + 48, boxTop + 48, Integer.MAX_VALUE);
         }
      }
   }

   private void drawItemStack(GuiGraphics gg, ItemStack stack) {
      gg.renderItem(stack, 0, 0);
   }

   private static final ResourceLocation EMPTY_CIRCLE = new ResourceLocation("som", "textures/gui/spells/empty_circle.png");

   public void drawSpellIcon(GuiGraphics gg, float xPos, float yPos, Spell spell) {
      int x = Math.round(xPos - 16);
      int y = Math.round(yPos - 16);
      if (spell instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom custom && custom.getEffect() != null) {
         gg.blit(EMPTY_CIRCLE, x, y, 0, 0, 32, 32, 32, 32);
         net.minecraft.client.renderer.texture.TextureAtlasSprite sprite =
            Minecraft.getInstance().getMobEffectTextures().get(custom.getEffect());
         gg.blit(x + 4, y + 4, 0, 24, 24, sprite);
         return;
      }
      gg.blit(spell.getSpellIcon(), x, y, 0, 0, 32, 32, 32, 32);
   }

   public void drawSpellSlot(GuiGraphics gg, IWandData wand) {

      gg.blit(getTexture(wand), -19, -54 - 19, 38, 0, 38, 38);
   }

   private static String metalName(IWandData wand, boolean apprentice) {
      if (apprentice) {
         return "gold";
      }
      if (wand != null && wand.getHandleType() != null) {
         String m = wand.getHandleType().name().toLowerCase();
         switch (m) {
            case "gold": case "silver": case "void": case "copper":
            case "bronze": case "brass": case "iron": case "steel": return m;
            default: return "gold";
         }
      }
      return "gold";
   }

   public ResourceLocation getTexture(IWandData wand) {
      return new ResourceLocation("som", "textures/gui/hud_elements_" + metalName(wand, this.apprenticeWand) + ".png");
   }

   public static int getSlotNumber(IManaData manaData, IWandData wandData) {
      if (wandData.hasLimitedSlots()) return wandData.getLimitedSlots();
      int level = manaData.getLevel();
      if (level < 5) return 3;
      else if (level < 10) return 4;
      else if (level < 15) return 5;
      else if (level < 20) return 6;
      else if (level < 25) return 7;
      else if (level < 30) return 8;
      else return level < 35 ? 9 : 10;
   }
}
