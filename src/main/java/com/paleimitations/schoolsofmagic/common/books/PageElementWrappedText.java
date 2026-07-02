package com.paleimitations.schoolsofmagic.common.books;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;

public class PageElementWrappedText extends PageElement {
   public final String textLocation;
   public final int fontColor;
   public final int wrapWidth;
   public final float scale;

   public PageElementWrappedText(String textLocation, int x, int y, int wrapWidth, float scale, int fontColor) {
      super(x, y);
      this.textLocation = textLocation;
      this.wrapWidth = wrapWidth;
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
         StringBuilder line = new StringBuilder();
         for (String add : IOUtils.readLines(is, StandardCharsets.UTF_8)) line.append(add);
         String[] paragraphs = line.toString().split("<~>");
         int linenumber = 0;
         for (String s : paragraphs) {
            gg.pose().pushPose();
            gg.pose().scale(this.scale, this.scale, this.scale);
            int xI = Math.round((this.x + xIn) / this.scale);
            int yI = Math.round((this.y + yIn) / this.scale);
            int wrapW = Math.round(this.wrapWidth / this.scale);
            Component comp = Component.literal(s);
            gg.drawWordWrap(font, comp, xI, yI + linenumber * font.lineHeight, wrapW, this.fontColor);
            linenumber += font.split(comp, wrapW).size();
            gg.pose().popPose();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
