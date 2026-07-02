package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementStandardText extends PageElement {
   public final String textLocation;
   public final int fontColor;
   public final int height;
   public final int width;
   public final boolean centered;
   public final boolean bold;

   public PageElementStandardText(String textLocation, int x, int y, int width, int height, int fontColor, boolean centered) {
      this(textLocation, x, y, width, height, fontColor, centered, false);
   }

   public PageElementStandardText(String textLocation, int x, int y, int width, int height, int fontColor, boolean centered, boolean bold) {
      super(x, y);
      this.textLocation = textLocation;
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
      this.bold = bold;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      String s = I18n.get(this.textLocation);
      if (this.bold) s = "§l" + s;
      int textWidth = font.width(s);
      int textHeight = font.lineHeight;
      float scaler = Math.min((float) this.width / textWidth, (float) this.height / textHeight);
      int drawX = this.x + xIn;
      int drawY = this.y + yIn;
      if (this.centered) {
         drawX = Math.round((this.x + xIn) - textWidth * scaler / 2.0F);
         drawY = Math.round((this.y + yIn) - textHeight * scaler / 2.0F);
      }
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      gg.drawString(font, com.paleimitations.schoolsofmagic.client.GrimoireScramble.apply(s), Math.round(drawX / scaler), Math.round(drawY / scaler), this.fontColor, false);
      gg.pose().popPose();
   }
}
