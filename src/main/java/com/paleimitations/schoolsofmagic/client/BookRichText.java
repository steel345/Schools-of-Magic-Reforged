package com.paleimitations.schoolsofmagic.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// A small Patchouli-like rich-text layer for book paragraphs. Inline codes:
//   /b bold   /i italic   /z reset   /n newline
//   /s-NNN    set text size in percent (100 = default)
//   /#RRGGBB  custom colour
//   named colours (rainbow + extras): /red (/r), /orange, /yellow, /green,
//     /blue, /indigo, /violet, /purple, /pink, /cyan, /aqua, /white, /black,
//     /gray, /brown, /gold, /lime, /magenta  (aliases /bold /italic /reset ok)
//   /rainbow  colour each following character across the spectrum
// Anything after a lone '/' that is not a known code is left as literal text, so
// ordinary slashes in prose are untouched. Pages with no codes never reach this
// class; they keep the original renderer exactly.
@OnlyIn(Dist.CLIENT)
public class BookRichText {

   private static final int DEFAULT = 0;
   private static final Map<String, Integer> COLORS = new HashMap<>();
   static {
      COLORS.put("r", 0xC0392B);      COLORS.put("red", 0xC0392B);
      COLORS.put("orange", 0xE67E22); COLORS.put("yellow", 0xF1C40F);
      COLORS.put("green", 0x27AE60);  COLORS.put("lime", 0x2ECC40);
      COLORS.put("blue", 0x2E64FE);   COLORS.put("indigo", 0x4B0082);
      COLORS.put("violet", 0x8E44AD); COLORS.put("purple", 0x7D3C98);
      COLORS.put("pink", 0xE84393);   COLORS.put("magenta", 0xD81B9A);
      COLORS.put("cyan", 0x1ABC9C);   COLORS.put("aqua", 0x00B5CC);
      COLORS.put("white", 0xF5F5F5);  COLORS.put("black", 0x1A1A1A);
      COLORS.put("gray", 0x7F8C8D);   COLORS.put("grey", 0x7F8C8D);
      COLORS.put("brown", 0x795548);  COLORS.put("gold", 0xC49B2E);
      // One code per school of magic, using that school's own element colour (kept
      // in step with MagicElementRegistry) so mentions always read in-theme.
      COLORS.put("pyromancy", 0xA22626);    COLORS.put("heliomancy", 0xE96400);
      COLORS.put("aeromancy", 0xFABC28);    COLORS.put("geomancy", 0x65B41C);
      COLORS.put("animancy", 0x536729);     COLORS.put("electromancy", 0x1E8496);
      COLORS.put("hydromancy", 0x339BDA);   COLORS.put("cryomancy", 0x35379F);
      COLORS.put("hieromancy", 0x7327B1);
      // Chaotimancy reads hot pink; /Ct is its short code.
      COLORS.put("chaotics", 0xD70076);     COLORS.put("ct", 0xD70076);
      COLORS.put("auramancy", 0xE0769C);    COLORS.put("astromancy", 0xD8DEDE);
      COLORS.put("infernality", 0xA5A8AA);  COLORS.put("spectromancy", 0x707274);
      COLORS.put("umbramancy", 0x313335);   COLORS.put("necromancy", 0x704626);
   }

   private static final class Style {
      int color = DEFAULT;
      boolean bold, italic, rainbow;
      float size = 100f;
      Style copy() { Style s = new Style(); s.color = color; s.bold = bold; s.italic = italic; s.rainbow = rainbow; s.size = size; return s; }
   }

   private static final class G {
      final char c; final int color; final boolean bold, italic; final float size;
      G(char c, Style st, int rainbowColor) {
         this.c = c;
         this.color = st.rainbow && c != ' ' ? rainbowColor : st.color;
         this.bold = st.bold; this.italic = st.italic; this.size = st.size;
      }
   }

   private static final class Line { final int start, next; final float maxSize; final List<G> glyphs;
      Line(List<G> g, int start, int next, float maxSize) { this.glyphs = g; this.start = start; this.next = next; this.maxSize = maxSize; } }

   // The visible text with all "/" codes removed (e.g. "/bBroom/z" -> "Broom").
   // Used so search matches words that sit right after a formatting code.
   public static String stripCodes(String s) {
      if (s == null) return "";
      StringBuilder sb = new StringBuilder();
      int i = 0, n = s.length();
      while (i < n) {
         char c = s.charAt(i);
         if (c == '/' && i < n - 1) {
            int adv = readCode(s, i, null);
            if (adv > i) { i = adv; continue; }
         }
         if (c == '\\' && i + 1 < n && s.charAt(i + 1) == '/') { sb.append('/'); i += 2; continue; }
         sb.append(c);
         i++;
      }
      return sb.toString();
   }

   // Fast pre-check so callers only switch to this path when a code really exists.
   public static boolean hasCodes(String s) {
      if (s == null) return false;
      int i = -1;
      while ((i = s.indexOf('/', i + 1)) != -1 && i < s.length() - 1) {
         if (readCode(s, i, null) > i) return true;
      }
      return false;
   }

   // Applies a code found at index i (the '/') to st (may be null to just probe).
   // Returns the index just past the code, or i if it was not a valid code.
   private static int readCode(String s, int i, Style st) {
      int n = s.length();
      char c1 = s.charAt(i + 1);
      if (c1 == '#') {
         if (i + 7 < n && isHex(s, i + 2, 6)) {
            if (st != null) { st.color = Integer.parseInt(s.substring(i + 2, i + 8), 16); st.rainbow = false; }
            return i + 8;
         }
         return i;
      }
      if ((c1 == 's' || c1 == 'S') && i + 2 < n && s.charAt(i + 2) == '-') {
         int j = i + 3;
         while (j < n && Character.isDigit(s.charAt(j))) j++;
         if (j > i + 3) {
            if (st != null) st.size = Math.max(30, Math.min(300, Integer.parseInt(s.substring(i + 3, j))));
            return j;
         }
         return i;
      }
      // Match the LONGEST known keyword that starts right after the slash, so a code
      // glued to the following word still parses (e.g. "/rred" = red + "red",
      // "/rainbowshimmering" = rainbow + "shimmering").
      String lower = s.toLowerCase();
      for (String kw : KEYWORDS) {
         if (lower.startsWith(kw, i + 1)) {
            if (st != null) applyKeyword(st, kw);
            return i + 1 + kw.length();
         }
      }
      return i;
   }

   private static void applyKeyword(Style st, String kw) {
      switch (kw) {
         case "b": case "bold":   st.bold = true; return;
         case "i": case "italic": st.italic = true; return;
         case "z": case "reset":  st.color = DEFAULT; st.bold = false; st.italic = false; st.rainbow = false; st.size = 100f; return;
         case "rainbow":          st.rainbow = true; return;
         default:
            Integer col = COLORS.get(kw);
            if (col != null) { st.color = col; st.rainbow = false; }
      }
   }

   // All keywords, longest first, so startsWith finds the longest match.
   private static final java.util.List<String> KEYWORDS;
   static {
      java.util.List<String> ks = new ArrayList<>(COLORS.keySet());
      ks.add("bold"); ks.add("italic"); ks.add("reset"); ks.add("rainbow");
      ks.add("b"); ks.add("i"); ks.add("z");
      ks.sort((a, c) -> c.length() - a.length());
      KEYWORDS = ks;
   }

   private static boolean isHex(String s, int from, int len) {
      for (int k = from; k < from + len; k++) {
         char c = s.charAt(k);
         if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) return false;
      }
      return true;
   }

   private static List<G> parse(String s) {
      List<G> out = new ArrayList<>();
      Style st = new Style();
      int rainbowIndex = 0;
      int i = 0, n = s.length();
      while (i < n) {
         char c = s.charAt(i);
         if (c == '/' && i < n - 1) {
            int adv = readCode(s, i, st);
            if (adv > i) { i = adv; continue; }
         }
         if (c == '§' && i + 1 < n && applyVanilla(st, Character.toLowerCase(s.charAt(i + 1)))) { i += 2; continue; }
         if (c == '\\' && i + 1 < n && s.charAt(i + 1) == '/') { out.add(new G('/', st, 0)); i += 2; continue; }
         if (c == '\n') { out.add(new G('\n', st, 0)); i++; continue; }
         int rc = 0;
         if (st.rainbow && c != ' ') { rc = rainbow(rainbowIndex); rainbowIndex++; }
         out.add(new G(c, st, rc));
         i++;
      }
      return out;
   }

   // Vanilla § formatting so codes can be mixed with the "/" codes and still measure
   // correctly. Returns true if the char was a recognised format code (then consumed).
   private static boolean applyVanilla(Style st, char f) {
      switch (f) {
         case 'l': st.bold = true; return true;
         case 'o': st.italic = true; return true;
         case 'r': st.color = DEFAULT; st.bold = false; st.italic = false; st.rainbow = false; st.size = 100f; return true;
         case 'k': case 'm': case 'n': return true; // obfuscate/strike/underline: consume, not modelled
         default:
            int col = vanillaColor(f);
            if (col >= 0) { st.color = col; st.rainbow = false; return true; }
            return false;
      }
   }

   private static int vanillaColor(char f) {
      switch (f) {
         case '0': return 0x000000; case '1': return 0x0000AA; case '2': return 0x00AA00; case '3': return 0x00AAAA;
         case '4': return 0xAA0000; case '5': return 0xAA00AA; case '6': return 0xFFAA00; case '7': return 0xAAAAAA;
         case '8': return 0x555555; case '9': return 0x5555FF; case 'a': return 0x55FF55; case 'b': return 0x55FFFF;
         case 'c': return 0xFF5555; case 'd': return 0xFF55FF; case 'e': return 0xFFFF55; case 'f': return 0xFFFFFF;
         default: return -1;
      }
   }

   private static int rainbow(int idx) {
      return Color.HSBtoRGB((idx * 0.055f) % 1.0f, 0.85f, 0.85f) & 0xFFFFFF;
   }

   private static float advance(Font font, G g) {
      float w = font.width(String.valueOf(g.c)) * g.size / 100f;
      if (g.bold) w += g.size / 100f;
      return w;
   }

   private static List<Line> wrap(Font font, List<G> glyphs, int maxWidth) {
      List<Line> lines = new ArrayList<>();
      int n = glyphs.size(), i = 0;
      while (i < n) {
         int lastSpace = -1, j = i;
         float width = 0f;
         boolean hardBreak = false;
         for (; j < n; j++) {
            G g = glyphs.get(j);
            if (g.c == '\n') { hardBreak = true; break; }
            if (g.c == ' ') lastSpace = j;
            float w = advance(font, g);
            if (width + w > maxWidth && j > i) break;
            width += w;
         }
         int end, next;
         if (hardBreak) { end = j; next = j + 1; }
         else if (j >= n) { end = n; next = n; }
         else if (lastSpace >= i) { end = lastSpace; next = lastSpace + 1; }
         else { end = j; next = j; }
         List<G> sub = new ArrayList<>(glyphs.subList(i, end));
         float maxSize = 100f;
         for (G g : sub) if (g.size > maxSize) maxSize = g.size;
         lines.add(new Line(sub, i, next, maxSize));
         i = next;
      }
      return lines;
   }

   // Lays every paragraph through the boxes. When draw is true it renders onto
   // subpage; otherwise it just reports whether subpage gets any content.
   private static boolean flow(GuiGraphics gg, Font font, List<String> paragraphs, List<ParagraphBoxRef> boxes,
                               float scale, int xIn, int yIn, int subpage, boolean draw, int defaultColor) {
      boolean found = false;
      int boxId = 0;
      float yCursor = 0f;
      for (String para : paragraphs) {
         List<G> remaining = parse(para);
         boolean flag = true;
         while (flag) {
            if (boxId >= boxes.size() || boxes.get(boxId) == null) { flag = false; break; }
            ParagraphBoxRef box = boxes.get(boxId);
            int boxW = Math.round(box.width / scale);
            int boxH = Math.round(box.height / scale);
            int xI = Math.round((box.x + xIn) / scale);
            int yI = Math.round((box.y + yIn) / scale);
            List<Line> lines = wrap(font, remaining, boxW);
            int placed = 0;
            for (Line ln : lines) {
               int lh = Math.round(font.lineHeight * ln.maxSize / 100f);
               boolean fits = yCursor + lh <= boxH;
               if (!fits && !(placed == 0 && yCursor == 0f)) break;
               if (box.target == subpage) {
                  found = true;
                  if (!draw) return true;
                  drawLine(gg, font, ln, xI, yI + Math.round(yCursor), defaultColor);
               }
               yCursor += lh;
               placed++;
            }
            if (placed >= lines.size()) {
               flag = false;
            } else {
               int overflowStart = lines.get(placed).start;
               remaining = new ArrayList<>(remaining.subList(overflowStart, remaining.size()));
               boxId++;
               yCursor = 0f;
               if (remaining.isEmpty()) flag = false;
            }
         }
      }
      return found;
   }

   private static void drawLine(GuiGraphics gg, Font font, Line ln, int xI, int yBase, int defaultColor) {
      float x = 0f;
      int i = 0, n = ln.glyphs.size();
      while (i < n) {
         G first = ln.glyphs.get(i);
         int j = i;
         StringBuilder sb = new StringBuilder();
         while (j < n) {
            G g = ln.glyphs.get(j);
            if (g.color != first.color || g.bold != first.bold || g.italic != first.italic || g.size != first.size) break;
            sb.append(g.c);
            j++;
         }
         float runWidth = 0f;
         for (int k = i; k < j; k++) runWidth += advance(font, ln.glyphs.get(k));
         String run = sb.toString();
         String styled = (first.bold ? "§l" : "") + (first.italic ? "§o" : "") + run;
         styled = GrimoireScramble.apply(styled);
         int color = first.color == DEFAULT ? defaultColor : first.color;
         gg.pose().pushPose();
         gg.pose().translate(xI + x, yBase, 0.0F);
         gg.pose().scale(first.size / 100f, first.size / 100f, 1.0F);
         gg.drawString(font, styled, 0, 0, color, false);
         gg.pose().popPose();
         x += runWidth;
         i = j;
      }
   }

   // Public entry points used by PageElementParagraphs.
   public static void render(GuiGraphics gg, List<String> paragraphs, List<ParagraphBoxRef> boxes,
                             float scale, int xIn, int yIn, int subpage, int defaultColor) {
      Font font = net.minecraft.client.Minecraft.getInstance().font;
      gg.pose().pushPose();
      gg.pose().scale(scale, scale, scale);
      flow(gg, font, paragraphs, boxes, scale, xIn, yIn, subpage, true, defaultColor);
      gg.pose().popPose();
   }

   public static boolean hasSubpage(List<String> paragraphs, List<ParagraphBoxRef> boxes, float scale, int subpage) {
      Font font = net.minecraft.client.Minecraft.getInstance().font;
      return flow(null, font, paragraphs, boxes, scale, 0, 0, subpage, false, 0);
   }

   // A lightweight view of a ParagraphBox so this client class stays decoupled.
   public static final class ParagraphBoxRef {
      public final int x, y, target, width, height;
      public ParagraphBoxRef(int x, int y, int target, int width, int height) {
         this.x = x; this.y = y; this.target = target; this.width = width; this.height = height;
      }
   }
}
