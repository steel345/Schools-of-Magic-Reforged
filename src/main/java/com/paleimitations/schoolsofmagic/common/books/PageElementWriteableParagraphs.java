package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementWriteableParagraphs extends PageElementParagraphs {
   public String text;

   public PageElementWriteableParagraphs(String text, float scale, int fontColor) {
      super("", scale, fontColor, 9,
         new ParagraphBox(23, 65, 0, 99, 125), new ParagraphBox(134, 50, 0, 99, 140),
         new ParagraphBox(23, 50, 1, 99, 140), new ParagraphBox(134, 50, 1, 99, 140),
         new ParagraphBox(23, 50, 2, 99, 140), new ParagraphBox(134, 50, 2, 99, 140),
         new ParagraphBox(23, 50, 3, 99, 140), new ParagraphBox(134, 50, 3, 99, 140),
         new ParagraphBox(23, 50, 4, 99, 140), new ParagraphBox(134, 50, 4, 99, 140),
         new ParagraphBox(23, 50, 5, 99, 140), new ParagraphBox(134, 50, 5, 99, 140),
         new ParagraphBox(23, 50, 6, 99, 140), new ParagraphBox(134, 50, 6, 99, 140),
         new ParagraphBox(23, 50, 7, 99, 140), new ParagraphBox(134, 50, 7, 99, 140),
         new ParagraphBox(23, 50, 8, 99, 140), new ParagraphBox(134, 50, 8, 99, 140),
         new ParagraphBox(23, 50, 9, 99, 140), new ParagraphBox(134, 50, 9, 99, 140));
      this.text = text;
   }

   @Override
   public boolean isTarget(int i) {
      return i <= this.subpage;
   }

   @Override
   public boolean hasSubpage(int subpage) {
      Font font = Minecraft.getInstance().font;
      String s = this.text;
      int boxId = 0;
      int linenumber = 0;
      boolean flag = true;
      while (flag) {
         String overflow = null;
         if (this.boxes.size() > boxId && this.boxes.get(boxId) != null) {
            ParagraphBox box = this.boxes.get(boxId);
            for (String s1 : listFormattedStringToWidth(s, Math.round(box.width / this.scale))) {
               if ((linenumber + 1) * font.lineHeight <= Math.round(box.height / this.scale)) {
                  if (subpage == box.target) return true;
               } else {
                  overflow = overflow == null ? s1 : overflow + s1;
               }
               ++linenumber;
            }
         } else {
            flag = false;
         }
         if (overflow == null) {
            flag = false;
            continue;
         }
         ++boxId;
         linenumber = 0;
         s = overflow;
      }
      return false;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int subpage) {
      Font font = Minecraft.getInstance().font;
      gg.pose().pushPose();
      gg.pose().scale(this.scale, this.scale, this.scale);
      int boxId = 0;
      int linenumber = 0;
      String s = this.text;
      boolean flag = true;
      while (flag) {
         String overflow = null;
         if (this.boxes.size() > boxId && this.boxes.get(boxId) != null) {
            ParagraphBox box = this.boxes.get(boxId);
            int xI = Math.round((box.x + xIn) / this.scale);
            int yI = Math.round((box.y + yIn) / this.scale);
            for (String s1 : listFormattedStringToWidth(s, Math.round(box.width / this.scale))) {
               if ((linenumber + 1) * font.lineHeight <= Math.round(box.height / this.scale)) {
                  if (subpage == box.target) {
                     gg.drawString(font, s1, xI, yI + linenumber * font.lineHeight, 0, false);
                  }
               } else {
                  overflow = overflow == null ? s1 : overflow + s1;
               }
               ++linenumber;
            }
         } else {
            flag = false;
         }
         if (overflow == null) {
            flag = false;
            continue;
         }
         ++boxId;
         linenumber = 0;
         s = overflow;
      }
      gg.pose().popPose();
   }
}
