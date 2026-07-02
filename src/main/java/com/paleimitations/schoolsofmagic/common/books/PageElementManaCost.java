package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementManaCost extends PageElement {
   public final Spell spell;
   public final int fontColor;
   public final int height;
   public final int width;
   public final boolean centered;

   public PageElementManaCost(Spell spell, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(x, y);
      this.spell = spell;
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      String s = String.valueOf(this.spell.getCost() * (this.spell.isPerSecond() ? 20 : 1));
      int textWidth = font.width(s);
      int textHeight = 9;
      float scaler = Math.min((float)this.width / textWidth, (float)this.height / textHeight);
      int drawX = this.x + xIn;
      int drawY = this.y + yIn;
      if (this.centered) {
         drawX = Math.round((this.x + xIn) - textWidth * scaler / 2.0F);
         drawY = Math.round((this.y + yIn) - textHeight * scaler / 2.0F);
      }
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      gg.drawString(font, s, Math.round((float)drawX / scaler), Math.round((float)drawY / scaler), this.fontColor, false);
      gg.pose().popPose();
   }
}
