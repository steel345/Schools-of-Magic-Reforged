package com.paleimitations.schoolsofmagic.common.books;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementChapterEntry extends PageElementPageButton {
   public final String[] text;
   public final String desc;

   public PageElementChapterEntry(String[] text, String desc, int pageNumber, int x, int y, int target, int width, int height) {
      super(pageNumber, x, y, target, width, height);
      this.text = text;
      this.desc = desc;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      int textHeight = 9;
      float scaler = (float)this.height / (float)textHeight;
      String end = "... " + this.pageNumber;
      int endWidth = font.width(end);
      String t = I18n.get(this.text[0]);
      if (this.text.length > 1) {
         for (int i = 1; i < this.text.length; ++i) {
            if (this.text[i] != null) t = t + " " + I18n.get(this.text[i]);
         }
      }
      String txt = truncateStringToWidth(t, Math.round((float)this.width / scaler) - endWidth) + end;
      int drawX = this.x + xIn;
      int drawY = this.y + yIn;
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      gg.drawString(font, txt, Math.round((float)drawX / scaler), Math.round((float)drawY / scaler), 0, false);
      if (mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height && this.isTarget(this.subpage)) {
         gg.drawString(font, txt, Math.round((float)drawX / scaler), Math.round((float)drawY / scaler) - 1, Color.WHITE.getRGB(), false);
      }
      gg.pose().popPose();
      if (!this.desc.isEmpty()) {
         float descScaler = 6.0F / (float)textHeight;
         String descEnd = "...";
         int descEndWidth = font.width(descEnd);
         String descTxt = truncateStringToWidth(I18n.get(this.desc), Math.round((float)this.width / descScaler) - descEndWidth) + descEnd;
         gg.pose().pushPose();
         gg.pose().scale(descScaler, descScaler, descScaler);
         gg.drawString(font, descTxt, Math.round((float)drawX / descScaler), Math.round((float)drawY / descScaler) + 10, 0, false);
         gg.pose().popPose();
      }
   }

   private static String truncateStringToWidth(String str, int wrapWidth) {
      Font font = Minecraft.getInstance().font;
      int j = 0;
      int k;
      for (k = 0; k < str.length(); ++k) {
         j += font.width(String.valueOf(str.charAt(k)));
         if (j > wrapWidth) break;
      }
      return str.substring(0, k);
   }
}
