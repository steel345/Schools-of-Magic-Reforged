package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.ManaData;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementSpellInfo extends PageElement {
   public static final ResourceLocation SPELL_ICONS = new ResourceLocation("som", "textures/gui/books/magic_icons.png");

   public static final ResourceLocation SPELL_ICONS2 = new ResourceLocation("som", "textures/gui/books/magic_icons.png");

   public static final ResourceLocation ELEMENT_SCHOOL_ICONS = new ResourceLocation("som", "textures/gui/crystal_ball_icons.png");

   public static final ResourceLocation SPELL_CHARGE_ICONS = new ResourceLocation("som", "textures/gui/spell_charge_icons.png");
   public final Spell spell;
   private final Map<Spell.EnumSpellModifier, Object> map = Maps.newHashMap();

   public PageElementSpellInfo(Spell spell) {
      super(0, 0);
      this.spell = spell;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Color color = new Color(BookPageSpell.getColorFromElementList(this.spell.getElements()));
      float cr = color.getRed() / 255.0F;
      float cg = color.getGreen() / 255.0F;
      float cb = color.getBlue() / 255.0F;

      gg.blit(this.spell.getSpellIcon(), 24 + xIn, 48 + yIn, 24, 24, 0.0F, 0.0F, 32, 32, 32, 32);

      String spellName = I18n.get("spell." + this.spell.getName() + ".name");
      int nameWidth = spellName.trim().contains(" ") ? 68 : 54;
      drawStandardText(gg, spellName, nameWidth, 14, 83 + xIn, 60.0F + yIn, Color.WHITE.getRGB(), true, true, isGUI);

      gg.setColor(cr, cg, cb, 1.0F);
      this.drawTexturedModalRect(gg, SPELL_ICONS, 96 + xIn, 75 + yIn, 96, 120, 21, 110);

      float f = this.spell.getCost() / (float) ManaData.getMaxMana(this.spell.getMinimumMagicianLevel() + 1);
      this.drawTexturedModalRect(gg, SPELL_ICONS, 104 + xIn, 96 + yIn + Math.round(75.0F * (1.0F - f)),
         117, 120 + Math.round(75.0F * (1.0F - f)), 5, Math.round(75.0F * f));
      gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);

      drawStandardText(gg, String.valueOf(this.spell.getMinimumMagicianLevel() + 1), 11, 11,
         107 + xIn, 86 + yIn, Color.WHITE.getRGB(), true, false);
      drawStandardText(gg, String.valueOf(this.spell.getCost() * (this.spell.isPerSecond() ? 20.0F : 1.0F)), 9, 6,
         106.5F + xIn, 177.5F + yIn, Color.WHITE.getRGB(), true, false);

      int i = this.drawMaterialComponentTab(gg, 22 + xIn, 78 + yIn, isGUI, cr, cg, cb);
      i = this.drawSchoolComponent(gg, 22 + xIn, i, isGUI, cr, cg, cb);
      i = this.drawElementComponent(gg, 22 + xIn, i, isGUI, cr, cg, cb);
      i = this.drawModifierComponent(gg, 22 + xIn, i, isGUI, 0, cr, cg, cb);
      if (this.getModifierMap().entrySet().size() > 1 && i < 170) i = this.drawModifierComponent(gg, 22 + xIn, i, isGUI, 1, cr, cg, cb);
      if (this.getModifierMap().entrySet().size() > 2 && i < 170) i = this.drawModifierComponent(gg, 22 + xIn, i, isGUI, 2, cr, cg, cb);
      if (this.getModifierMap().entrySet().size() > 3 && i < 170) i = this.drawModifierComponent(gg, 22 + xIn, i, isGUI, 3, cr, cg, cb);
      if (this.getModifierMap().entrySet().size() > 4 && i < 170) i = this.drawModifierComponent(gg, 22 + xIn, i, isGUI, 4, cr, cg, cb);
      if (i < 174) this.drawChargeLevels(gg, 22 + xIn, i + 1, yIn + 188);
   }

   public int drawChargeLevels(GuiGraphics gg, int x, int y, int bottom) {
      int minSpell = this.spell.getMinimumSpellChargeLevel();
      int maxSpell = this.spell.getMaximumSpellChargeLevel();
      int count = maxSpell + 1 - minSpell;
      if (count <= 0) {
         return y;
      }
      int boxWidth = 73;
      int iconSize = 16;
      int gap = 1;
      int perRow = Math.max(1, (boxWidth + gap) / (iconSize + gap));
      float scale = (float) iconSize / 32.0F;
      gg.pose().pushPose();
      gg.pose().scale(scale, scale, 1.0F);
      for (int k = 0; k < count; k++) {
         int icon = minSpell + k;
         int px = x + (k % perRow) * (iconSize + gap);
         int py = y + (k / perRow) * (iconSize + gap);
         if (py + iconSize > bottom) {
            continue;
         }
         gg.blit(SPELL_CHARGE_ICONS, Math.round(px / scale), Math.round(py / scale),
            icon % 3 * 32, icon / 3 * 32, 32, 32);
      }
      gg.pose().popPose();
      int rows = (count + perRow - 1) / perRow;
      return y + rows * (iconSize + gap);
   }

   public static void drawStandardText(GuiGraphics gg, String s, int width, int height, float x, float y, int color, boolean centered, boolean dropShadow) {
      drawStandardText(gg, s, width, height, x, y, color, centered, dropShadow, true);
   }

   public static void drawStandardText(GuiGraphics gg, String s, int width, int height, float x, float y, int color, boolean centered, boolean dropShadow, boolean gui) {
      Font font = Minecraft.getInstance().font;
      int textWidth = font.width(s);
      int textHeight = font.lineHeight;
      float scaler = Math.min((float) width / textWidth, (float) height / textHeight);
      float drawX = x;
      float drawY = y;
      if (centered) {
         drawX = x - textWidth * scaler / 2.0F;
         drawY = y - textHeight * scaler / 2.0F;
      }
      int tx = Math.round(drawX / scaler);
      int ty = Math.round(drawY / scaler);
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      if (dropShadow) {
         float zStep = gui ? 0.1F : -0.1F;
         int shadow = 0xFF000000 | ((color >> 16 & 0xFF) / 4 << 16) | ((color >> 8 & 0xFF) / 4 << 8) | ((color & 0xFF) / 4);
         gg.pose().translate(0.0F, 0.0F, zStep);
         gg.drawString(font, s, tx + 1, ty + 1, shadow, false);
         gg.pose().translate(0.0F, 0.0F, zStep);
      }
      gg.drawString(font, s, tx, ty, color, false);
      gg.pose().popPose();
   }

   public int drawMaterialComponentTab(GuiGraphics gg, int x, int y, boolean gui, float cr, float cg, float cb) {
      if (!this.spell.getMaterialComponents().isEmpty()) {
         int j = (int) (System.currentTimeMillis() / 3000L) % this.spell.getMaterialComponents().size();
         ItemStack stack = this.spell.getMaterialComponents().get(j);
         gg.setColor(cr, cg, cb, 1.0F);
         this.drawTexturedModalRect(gg, SPELL_ICONS, x, y, 0, 165, 73, 18);
         gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawItemStack(gg, stack, x + 1, y + 1, gui);
         drawStandardText(gg, stack.getHoverName().getString(), 51, 8, x + 45.5F, y + 10.0F, Color.WHITE.getRGB(), true, gui);
         return y + 18;
      }
      return y;
   }

   public int drawSchoolComponent(GuiGraphics gg, int x, int y, boolean gui, float cr, float cg, float cb) {
      if (!this.spell.getSchools().isEmpty()) {
         int j = (int) (System.currentTimeMillis() / 3000L) % this.spell.getSchools().size();
         MagicSchool school = this.spell.getSchools().get(j);
         gg.setColor(cr, cg, cb, 1.0F);
         this.drawTexturedModalRect(gg, SPELL_ICONS, x, y, 0, 120, 73, 18);
         gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
         gg.pose().pushPose();
         gg.pose().translate(x + 2.0F, y + 2.0F, 0.0F);
         gg.pose().scale(0.46666667F, 0.46666667F, 1.0F);
         this.drawTexturedModalRect(gg, ELEMENT_SCHOOL_ICONS, 0, 0, school.getId() * 30, 60, 30, 30);
         gg.pose().popPose();
         boolean flag = this.spell.getMinimumSchoolLevels()[school.getId()] > 0;
         if (flag) {
            drawStandardText(gg, I18n.get("school." + school.getName() + ".name"), 53, 6, x + 18, y + 2, Color.WHITE.getRGB(), false, gui);
            drawStandardText(gg, I18n.get("page.level.element") + ": " + (this.spell.getMinimumSchoolLevels()[school.getId()] + 1), 53, 6, x + 18, y + 9, Color.WHITE.getRGB(), false, gui);
         } else {
            drawStandardText(gg, I18n.get("school." + school.getName() + ".name"), 53, 8, x + 44, y + 10, Color.WHITE.getRGB(), true, gui);
         }
         return y + 18;
      }
      return y;
   }

   public int drawElementComponent(GuiGraphics gg, int x, int y, boolean gui, float cr, float cg, float cb) {
      if (!this.spell.getElements().isEmpty()) {
         int j = (int) (System.currentTimeMillis() / 3000L) % this.spell.getElements().size();
         MagicElement element = this.spell.getElements().get(j);
         gg.setColor(cr, cg, cb, 1.0F);
         this.drawTexturedModalRect(gg, SPELL_ICONS, x, y, 0, 120, 73, 18);
         gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
         gg.pose().pushPose();
         gg.pose().translate(x + 2.0F, y + 2.0F, 0.0F);
         gg.pose().scale(0.46666667F, 0.46666667F, 1.0F);
         this.drawTexturedModalRect(gg, ELEMENT_SCHOOL_ICONS, 0, 0, element.getId() % 8 * 30, element.getId() / 8 * 30, 30, 30);
         gg.pose().popPose();
         boolean flag = this.spell.getMinimumElementLevels()[element.getId()] > 0;
         if (flag) {
            drawStandardText(gg, I18n.get("element." + element.getName() + ".name"), 53, 6, x + 18, y + 2, Color.WHITE.getRGB(), false, gui);
            drawStandardText(gg, I18n.get("page.level.element") + ": " + (this.spell.getMinimumElementLevels()[element.getId()] + 1), 53, 6, x + 18, y + 9, Color.WHITE.getRGB(), false, gui);
         } else {
            drawStandardText(gg, I18n.get("element." + element.getName() + ".name"), 53, 8, x + 44, y + 10, Color.WHITE.getRGB(), true, gui);
         }
         return y + 18;
      }
      return y;
   }

   @SuppressWarnings("unchecked")
   public int drawModifierComponent(GuiGraphics gg, int x, int y, boolean gui, int offset, float cr, float cg, float cb) {
      if (!this.getModifierMap().isEmpty()) {
         int j = (int) (System.currentTimeMillis() / 3000L + offset) % this.getModifierMap().entrySet().size();
         Map.Entry<Spell.EnumSpellModifier, Object> entry =
            (Map.Entry<Spell.EnumSpellModifier, Object>) this.getModifierMap().entrySet().toArray()[j];
         gg.setColor(cr, cg, cb, 1.0F);
         this.drawTexturedModalRect(gg, SPELL_ICONS, x, y, 0, 120, 73, 18);
         gg.setColor(1.0F, 1.0F, 1.0F, 1.0F);
         gg.pose().pushPose();
         gg.pose().translate(x + 2.0F, y + 2.0F, 0.0F);
         gg.pose().scale(0.46666667F, 0.46666667F, 1.0F);
         this.drawTexturedModalRect(gg, SPELL_ICONS, 0, 0,
            122 + entry.getKey().id % 4 * 30, 90 + entry.getKey().id / 4 * 30, 30, 30);
         gg.pose().popPose();
         drawStandardText(gg, I18n.get("modifier." + entry.getKey().getSerializedName().toLowerCase() + ".name"),
            53, 8, x + 44, y + 10, Color.WHITE.getRGB(), true, gui);
         return y + 18;
      }
      return y;
   }

   public Map<Spell.EnumSpellModifier, Object> getModifierMap() {
      if (this.map.isEmpty() && !this.spell.getModifiers().isEmpty()) {
         for (Spell.EnumSpellModifier mod : this.spell.getModifiers().keySet()) {
            boolean flag = false;
            for (Spell.EnumSpellModifier mod1 : this.spell.getModifiers().keySet()) {
               if (mod.id == mod1.id && mod1.level > mod.level) {
                  flag = true;
                  break;
               }
            }
            if (!flag) this.map.put(mod, this.spell.getModifiers().get(mod));
         }
      }
      return this.map;
   }
}
