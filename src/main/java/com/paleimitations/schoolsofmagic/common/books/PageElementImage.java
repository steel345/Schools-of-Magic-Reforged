package com.paleimitations.schoolsofmagic.common.books;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementImage extends PageElement {
   public final ResourceLocation imageLocation;
   public final int xUV;
   public final int yUV;
   public final int width;
   public final int height;
   public final float scale;
   public final boolean blend;

   public PageElementImage(ResourceLocation imageLocation, int x, int y, int xUV, int yUV, int width, int height, float scale, boolean blend) {
      super(x, y);
      this.imageLocation = imageLocation;
      this.xUV = xUV;
      this.yUV = yUV;
      this.width = width;
      this.height = height;
      this.scale = scale;
      this.blend = blend;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      gg.pose().pushPose();
      gg.pose().scale(this.scale, this.scale, this.scale);
      this.drawTexturedModalRect(gg, this.imageLocation,
         Math.round((float)(this.x + xIn) / this.scale),
         Math.round((float)(this.y + yIn) / this.scale),
         this.xUV, this.yUV, this.width, this.height);
      gg.pose().popPose();
   }
}
