package com.paleimitations.schoolsofmagic.common.books;

public class PageElementTitle extends PageElementString {
   public PageElementTitle(String[] text, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(text, x, y, width, height, fontColor, centered);
   }

   public PageElementTitle(String text, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(text, x, y, width, height, fontColor, centered);
   }

   @Override
   @net.minecraftforge.api.distmarker.OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
   public void drawElement(net.minecraft.client.gui.GuiGraphics gg, float mouseX, float mouseY,
         int xIn, int yIn, boolean isGUI, int target) {
      String original = this.text.length > 0 && this.text[0] != null ? this.text[0] : "";
      String shown = BookTextOverride.titleOr(original);
      if (shown.equals(original) || this.text.length == 0) {
         super.drawElement(gg, mouseX, mouseY, xIn, yIn, isGUI, target);
         return;
      }
      this.text[0] = shown;
      try {
         super.drawElement(gg, mouseX, mouseY, xIn, yIn, isGUI, target);
      } finally {
         this.text[0] = original;
      }
   }
}
