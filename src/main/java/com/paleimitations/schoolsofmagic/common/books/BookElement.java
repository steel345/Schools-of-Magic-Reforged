package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BookElement {
   public int page;
   public int subpage;
   public float x;
   public float y;

   public BookElement(float x, float y, int page, int subpage) {
      this.page = page;
      this.subpage = subpage;
      this.x = x;
      this.y = y;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean shouldDraw(float mouseX, float mouseY, int x, int y, boolean isGUI, int subpage, int page) {
      return subpage == this.subpage && page == this.page;
   }

   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int x, int y, boolean isGUI, int subpage, int page) {
   }

   @OnlyIn(Dist.CLIENT)
   public void drawTexturedModalRect(GuiGraphics gg, ResourceLocation texture, int x, int y, int textureX, int textureY, int width, int height) {
      gg.blit(texture, x, y, textureX, textureY, width, height);
   }
}
