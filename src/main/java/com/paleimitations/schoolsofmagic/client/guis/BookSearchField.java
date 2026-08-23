package com.paleimitations.schoolsofmagic.client.guis;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class BookSearchField {
   private final Font font;
   public int maxLength = 120;
   private String value = "";
   private int caret = 0;
   private int displayStart = 0;

   public BookSearchField(Font font) {
      this.font = font;
   }

   public String getValue() { return this.value; }

   public boolean charTyped(char c) {
      if (c >= ' ' && c != 127 && this.value.length() < this.maxLength) {
         this.value = this.value.substring(0, this.caret) + c + this.value.substring(this.caret);
         this.caret++;
         return true;
      }
      return false;
   }

   public boolean keyPressed(int key) {
      switch (key) {
         case GLFW.GLFW_KEY_BACKSPACE:
            if (this.caret > 0) {
               this.value = this.value.substring(0, this.caret - 1) + this.value.substring(this.caret);
               this.caret--;
            }
            return true;
         case GLFW.GLFW_KEY_DELETE:
            if (this.caret < this.value.length()) {
               this.value = this.value.substring(0, this.caret) + this.value.substring(this.caret + 1);
            }
            return true;
         case GLFW.GLFW_KEY_LEFT:
            if (this.caret > 0) this.caret--;
            return true;
         case GLFW.GLFW_KEY_RIGHT:
            if (this.caret < this.value.length()) this.caret++;
            return true;
         case GLFW.GLFW_KEY_HOME:
            this.caret = 0;
            return true;
         case GLFW.GLFW_KEY_END:
            this.caret = this.value.length();
            return true;
         default:
            return false;
      }
   }

   public void clickAt(float clickFontX, int clipWidth) {
      updateScroll(clipWidth);
      int idx = this.displayStart;
      float acc = 0;
      while (idx < this.value.length()) {
         float cw = this.font.width(this.value.substring(idx, idx + 1));
         if (acc + cw / 2.0F >= clickFontX) break;
         acc += cw;
         idx++;
      }
      this.caret = idx;
   }

   private void updateScroll(int clipWidth) {
      if (this.displayStart > this.value.length()) this.displayStart = this.value.length();
      if (this.caret < this.displayStart) this.displayStart = this.caret;
      while (this.displayStart < this.caret
            && this.font.width(this.value.substring(this.displayStart, this.caret)) > clipWidth) {
         this.displayStart++;
      }
   }

   public void render(GuiGraphics gg, int clipWidth, boolean showCursor) {
      updateScroll(clipWidth);
      int end = this.displayStart;
      while (end < this.value.length()
            && this.font.width(this.value.substring(this.displayStart, end + 1)) <= clipWidth) {
         end++;
      }
      String visible = this.value.substring(this.displayStart, end);
      gg.drawString(this.font, visible, 1, 1, 0x000000, false);
      gg.drawString(this.font, visible, 0, 0, 0xFFFFFF, false);
      if (showCursor) {
         int c = Math.min(this.caret, end);
         int cx = this.font.width(this.value.substring(this.displayStart, c));
         gg.fill(cx, 0, cx + 1, this.font.lineHeight - 1, 0xFFFFFFFF);
      }
   }
}
