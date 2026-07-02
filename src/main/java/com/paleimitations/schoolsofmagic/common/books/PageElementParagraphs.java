package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;

public class PageElementParagraphs extends PageElement {
   public final String textLocation;
   public final int fontColor;
   public final float scale;
   public final List<ParagraphBox> boxes;
   public List<String> text = Lists.newArrayList();

   public PageElementParagraphs(String textLocation, float scale, int fontColor, int target, ParagraphBox... boxes) {
      super(0, 0, target);
      this.textLocation = textLocation;
      this.scale = scale;
      this.fontColor = fontColor;
      this.boxes = Lists.newArrayList(boxes);
   }

   @OnlyIn(Dist.CLIENT)
   public void loadText() {
      this.text.clear();
      Minecraft mc = Minecraft.getInstance();
      String lang = mc.options.languageCode;
      ResourceLocation fileLoc = new ResourceLocation("som", "lang/book/" + lang + "_0/" + this.textLocation + ".txt");
      ResourceLocation backupLoc = new ResourceLocation("som", "lang/book/en_us_0/" + this.textLocation + ".txt");
      Resource resource = mc.getResourceManager().getResource(fileLoc).orElse(
         mc.getResourceManager().getResource(backupLoc).orElse(null));
      if (resource == null) return;
      try (InputStream is = resource.open()) {
         StringBuilder line = new StringBuilder();
         for (String add : IOUtils.readLines(is, StandardCharsets.UTF_8)) line.append(add);
         this.text = Lists.newArrayList(line.toString().split("<~>"));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public boolean isTarget(int i) {
      return i <= this.subpage;
   }

   @Override
   public boolean hasSubpage(int subpage) {
      Font font = Minecraft.getInstance().font;
      int boxId = 0;
      int linenumber = 0;
      for (String s : this.text) {
         boolean flag = true;
         while (flag) {
            String overflow = null;
            if (this.boxes.size() > boxId && this.boxes.get(boxId) != null) {
               ParagraphBox box = this.boxes.get(boxId);
               for (String s1 : listFormattedStringToWidth(s, Math.round(box.width / this.scale))) {
                  if ((linenumber + 1) * font.lineHeight <= Math.round(box.height / this.scale)) {
                     if (subpage == box.target) return true;
                  } else {
                     overflow = overflow == null ? s1 : overflow + s1;
                  }
                  ++linenumber;
               }
            } else {
               flag = false;
            }
            if (overflow == null) {
               flag = false;
               continue;
            }
            ++boxId;
            linenumber = 0;
            s = overflow;
         }
      }
      return false;
   }

   @Override
   @OnlyIn(Dist.CLIENT)
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int subpage) {
      Font font = Minecraft.getInstance().font;
      gg.pose().pushPose();
      gg.pose().scale(this.scale, this.scale, this.scale);
      int boxId = 0;
      int linenumber = 0;
      for (String s : this.text) {
         boolean flag = true;
         while (flag) {
            String overflow = null;
            if (this.boxes.size() > boxId && this.boxes.get(boxId) != null) {
               ParagraphBox box = this.boxes.get(boxId);
               int xI = Math.round((box.x + xIn) / this.scale);
               int yI = Math.round((box.y + yIn) / this.scale);
               for (String s1 : listFormattedStringToWidth(s, Math.round(box.width / this.scale))) {
                  if ((linenumber + 1) * font.lineHeight <= Math.round(box.height / this.scale)) {
                     if (subpage == box.target) {
                        gg.drawString(font, com.paleimitations.schoolsofmagic.client.GrimoireScramble.apply(s1), xI, yI + linenumber * font.lineHeight, 0, false);
                     }
                  } else {
                     overflow = overflow == null ? s1 : overflow + s1;
                  }
                  ++linenumber;
               }
            } else {
               flag = false;
            }
            if (overflow == null) {
               flag = false;
               continue;
            }
            ++boxId;
            linenumber = 0;
            s = overflow;
         }
      }
      gg.pose().popPose();
   }

   public static List<String> listFormattedStringToWidth(String str, int wrapWidth) {
      return Arrays.asList(wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
   }

   private static String wrapFormattedStringToWidth(String str, int wrapWidth) {
      int i = sizeStringToWidth(str, wrapWidth);
      if (str.length() <= i) return str;
      String s = str.substring(0, i);
      String s1 = getFormatFromString(s) + str.substring(i);
      return s + "\n" + wrapFormattedStringToWidth(s1, wrapWidth);
   }

   private static int sizeStringToWidth(String str, int wrapWidth) {
      Font font = Minecraft.getInstance().font;
      int i = str.length();
      int j = 0;
      int l = -1;
      boolean flag = false;
      int k;
      for (k = 0; k < i; ++k) {
         char c0 = str.charAt(k);
         switch (c0) {
            case '\n':
               --k;
               break;
            case ' ':
               l = k;
            default:
               j += font.width(String.valueOf(c0));
               if (flag) ++j;
               break;
            case '§':
               if (k >= i - 1) break;
               char c1 = str.charAt(++k);
               if (c1 == 'l' || c1 == 'L') {
                  flag = true;
               } else if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                  flag = false;
               }
         }
         if (c0 == '\n') {
            l = ++k;
            break;
         }
         if (j > wrapWidth) break;
      }
      return k != i && l != -1 && l < k ? l : k;
   }

   private static boolean isFormatColor(char c) {
      return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
   }

   private static String getFormatFromString(String s) {
      StringBuilder sb = new StringBuilder();
      int i = -1;
      int len = s.length();
      while ((i = s.indexOf('§', i + 1)) != -1) {
         if (i < len - 1) {
            char c = s.charAt(i + 1);
            if (isFormatColor(c)) {
               sb.setLength(0);
               sb.append('§').append(c);
            } else if (c == 'r' || c == 'R') {
               sb.setLength(0);
            } else {
               sb.append('§').append(c);
            }
         }
      }
      return sb.toString();
   }
}
