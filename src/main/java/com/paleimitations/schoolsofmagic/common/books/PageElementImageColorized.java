package com.paleimitations.schoolsofmagic.common.books;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementImageColorized extends PageElementImage {
   public final float red;
   public final float green;
   public final float blue;
   public final float alpha;

   public PageElementImageColorized(ResourceLocation imageLocation, int x, int y, int xUV, int yUV, int width, int height, float scale, int color) {
      super(imageLocation, x, y, xUV, yUV, width, height, scale, false);
      if ((color & 0xFC000000) == 0) color |= 0xFF000000;
      this.red = (float)(color >> 16 & 0xFF) / 255.0F;
      this.green = (float)(color >> 8 & 0xFF) / 255.0F;
      this.blue = (float)(color & 0xFF) / 255.0F;
      this.alpha = (float)(color >> 24 & 0xFF) / 255.0F;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      RenderSystem.setShaderColor(this.red, this.green, this.blue, this.alpha);
      super.drawElement(gg, mouseX, mouseY, xIn, yIn, isGUI, target);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
