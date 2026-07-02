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

public class PageElementSizedText extends PageElement {
   public final String textLocation;
   public final int fontColor;
   public final int height;
   public final int width;
   public final boolean centered;

   public PageElementSizedText(String textLocation, int x, int y, int width, int height, int fontColor, boolean centered) {
      super(x, y);
      this.textLocation = textLocation;
      this.width = width;
      this.height = height;
      this.fontColor = fontColor;
      this.centered = centered;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      String fileName = this.textLocation + ".txt";
      ResourceLocation fileLoc = new ResourceLocation("som", "lang/book/" + mc.options.languageCode + "_0/" + fileName);
      ResourceLocation backupLoc = new ResourceLocation("som", "lang/book/en_us_0/" + fileName);
      Resource resource = mc.getResourceManager().getResource(fileLoc).orElse(
         mc.getResourceManager().getResource(backupLoc).orElse(null));
      if (resource == null) return;
      try (InputStream is = resource.open()) {
         int linenumber = 0;
         int lineAdjuster = 0;
         for (String line : IOUtils.readLines(is, StandardCharsets.UTF_8)) {
            int textWidth = font.width(line);
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
            gg.drawString(font, line, Math.round(drawX / scaler), Math.round(drawY / scaler) + lineAdjuster, this.fontColor, false);
            lineAdjuster += Math.round(linenumber * (font.lineHeight + 3) * scaler);
            ++linenumber;
            gg.pose().popPose();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
