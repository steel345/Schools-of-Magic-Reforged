package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementItemStackScaled extends PageElement {
   public final ItemStack stack;
   public final float scale;

   public PageElementItemStackScaled(ItemStack stack, int x, int y, float scale) {
      super(x, y);
      this.stack = stack;
      this.scale = scale;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      gg.pose().pushPose();
      gg.pose().translate((double) (this.x + xIn), (double) (this.y + yIn), 0.0D);
      gg.pose().scale(this.scale, this.scale, 1.0F);
      this.drawItemStack(gg, this.stack, 0, 0, isGUI);
      gg.pose().popPose();
   }
}
