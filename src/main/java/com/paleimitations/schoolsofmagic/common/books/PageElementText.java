package com.paleimitations.schoolsofmagic.common.books;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;

public class PageElementText extends PageElement {
   public final String textLocation;
   public final int fontColor;
   public final float scale;

   public PageElementText(String textLocation, int x, int y, float scale, int fontColor) {
      super(x, y);
      this.textLocation = textLocation;
      this.scale = scale;
      this.fontColor = fontColor;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      ResourceLocation fileLoc = new ResourceLocation("som", "lang/book/" + mc.options.languageCode + "_0/" + this.textLocation + ".txt");
      ResourceLocation backupLoc = new ResourceLocation("som", "lang/book/en_us_0/" + this.textLocation + ".txt");
      Resource resource = mc.getResourceManager().getResource(fileLoc).orElse(
         mc.getResourceManager().getResource(backupLoc).orElse(null));
      if (resource == null) return;
      try (InputStream is = resource.open()) {
         int linenumber = 0;
         for (String line : IOUtils.readLines(is, StandardCharsets.UTF_8)) {
            gg.pose().pushPose();
            gg.pose().scale(this.scale, this.scale, this.scale);
            gg.drawString(font, line,
               Math.round((this.x + xIn) / this.scale),
               Math.round((this.y + yIn) / this.scale) + linenumber * font.lineHeight,
               this.fontColor, false);
            ++linenumber;
            gg.pose().popPose();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
