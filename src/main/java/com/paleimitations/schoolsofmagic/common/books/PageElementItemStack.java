package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementItemStack extends PageElement {
   public final ItemStack stack;

   public PageElementItemStack(ItemStack stack, int x, int y) {
      super(x, y);
      this.stack = stack;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawItemStack(gg, this.stack, this.x + xIn, this.y + yIn, isGUI);
   }
}
