package com.paleimitations.schoolsofmagic.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class KnowledgeResultsView {
   public static int pageCount(int total, int perPage) {
      if (total <= 0) return 1;
      return (total + perPage - 1) / perPage;
   }

   private static int hitHeight(Font font) {
      return (font.lineHeight + 1) * 2 + 2;
   }

   private static float[] slotXY(Font font, int slot, int perPage, float colGap) {
      int rows = Math.max(1, perPage / 2);
      int col = slot / rows;
      int row = slot % rows;
      return new float[]{col * colGap, row * hitHeight(font)};
   }

   public static int hitTest(Font font, int total, int page, int perPage, int width, float colGap,
                             float mxLocal, float myLocal) {
      int block = hitHeight(font);
      int start = page * perPage;
      int end = Math.min(total, start + perPage);
      for (int i = start; i < end; i++) {
         float[] xy = slotXY(font, i - start, perPage, colGap);
         if (mxLocal >= xy[0] - 2 && mxLocal <= xy[0] + width + 2
               && myLocal >= xy[1] - 1 && myLocal < xy[1] + block - 2) {
            return i;
         }
      }
      return -1;
   }

   public static int render(GuiGraphics gg, Font font, List<KnowledgeSearch.Hit> hits,
                            int page, int perPage, int width, float colGap, float mxLocal, float myLocal) {
      int hovered = -1;
      int block = hitHeight(font);
      int rh = font.lineHeight + 1;
      int start = page * perPage;
      int end = Math.min(hits.size(), start + perPage);
      for (int i = start; i < end; i++) {
         KnowledgeSearch.Hit h = hits.get(i);
         float[] xy = slotXY(font, i - start, perPage, colGap);
         int x = Math.round(xy[0]);
         int y = Math.round(xy[1]);
         boolean hover = mxLocal >= x - 2 && mxLocal <= x + width + 2 && myLocal >= y - 1 && myLocal < y + block - 2;
         if (hover) {
            hovered = i;
            gg.fill(x - 2, y - 1, x + width + 2, y + block - 2, 0x40FFFFFF);
         }
         int titleColor = hover ? 0x000000 : 0x3A2E1E;
         int snipColor = hover ? 0x222222 : 0x5A4A38;
         gg.drawString(font, trim(font, h.title, width), x, y, titleColor, false);
         gg.drawString(font, trim(font, h.snippet, width - 6), x + 6, y + rh, snipColor, false);
      }
      return hovered;
   }

   private static String trim(Font font, String s, int width) {
      if (s == null) return "";
      if (font.width(s) <= width) return s;
      String ell = "...";
      int w = width - font.width(ell);
      StringBuilder b = new StringBuilder();
      for (int i = 0; i < s.length(); i++) {
         if (font.width(b.toString() + s.charAt(i)) > w) break;
         b.append(s.charAt(i));
      }
      return b + ell;
   }
}
