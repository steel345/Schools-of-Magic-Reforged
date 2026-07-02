package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementString extends PageElement {
   public final String[] text;
   public final int fontColor;
   public final int height;
   public final int width;
   public final boolean centered;

   public final net.minecraft.world.item.ItemStack stackLabel;

   public PageElementString(String text, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(x, y);
      this.text = new String[]{text};
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
      this.stackLabel = net.minecraft.world.item.ItemStack.EMPTY;
   }

   public PageElementString(net.minecraft.world.item.ItemStack stack, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(x, y);
      this.text = new String[]{""};
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
      this.stackLabel = stack;
   }

   public PageElementString(String[] text, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(x, y);
      this.text = text;
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
      this.stackLabel = net.minecraft.world.item.ItemStack.EMPTY;
   }

   public PageElementString(String[] text, int x, int y, int target, int width, int height, int fontColor, boolean centered) {
      super(x, y, target);
      this.text = text;
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
      this.stackLabel = net.minecraft.world.item.ItemStack.EMPTY;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      String t = (this.stackLabel != null && !this.stackLabel.isEmpty())
         ? this.stackLabel.getHoverName().getString()
         : I18n.get(this.text[0]);
      if ((this.stackLabel == null || this.stackLabel.isEmpty()) && this.text.length > 1) {
         for (int i = 1; i < this.text.length; ++i) {
            if (this.text[i] != null) t = t + " " + I18n.get(this.text[i]);
         }
      }
      int textWidth = font.width(t);
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
      gg.drawString(font, t, Math.round(drawX / scaler), Math.round(drawY / scaler), this.fontColor, false);
      gg.pose().popPose();
   }
}
