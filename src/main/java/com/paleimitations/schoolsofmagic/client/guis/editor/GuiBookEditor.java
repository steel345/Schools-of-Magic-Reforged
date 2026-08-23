package com.paleimitations.schoolsofmagic.client.guis.editor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.paleimitations.schoolsofmagic.client.BookLayoutRenderer;
import com.paleimitations.schoolsofmagic.client.BookRichText;
import com.paleimitations.schoolsofmagic.client.BookTemplates;
import com.paleimitations.schoolsofmagic.client.guis.podium.PodiumGuiHelper;
import com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout;
import com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element;
import com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSetPageLayout;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class GuiBookEditor extends Screen {
   private static final int PAGE_SIZE = 256;
   private static final int COL_W = 70;
   private static final int PICKER_COLS = 6;
   private static final int PICKER_ROWS = 11;
   private static final int CELL = 18;
   private static final int PANEL_W = PICKER_COLS * CELL;

   private static final int PANEL_BG = 0xC01A120B;
   private static final int PANEL_EDGE = 0xFF6B5A42;
   private static final int LABEL = 0xFFE8DCC0;
   private static final int SELECT_EDGE = 0xFFF1C40F;
   private static final int HOVER_EDGE = 0x80F5F5F5;
   private static final int SELECTION = 0x804A90D9;

   private static final int RAINBOW = -2;

   private enum PanelMode { ITEMS, COLORS, TEMPLATES }

   private final Screen parent;
   private final BlockPos podiumPos;
   private final int page;
   private final BookPageLayout layout;

   private int bookX;
   private int bookY;
   private int colX;
   private int panelX;
   private int gridY;

   private Element selected;
   private boolean dragging;
   private int grabX;
   private int grabY;
   private int ticks;

   private int caret;
   private int anchor;
   private boolean selectingText;

   private ItemStack dragStack = ItemStack.EMPTY;
   private boolean dragAsItem;
   private String dragTemplate;

   private PanelMode mode = PanelMode.ITEMS;
   private EditBox search;
   private EditBox hexBox;
   private final List<ItemStack> allItems = new ArrayList<>();
   private final List<ItemStack> shown = new ArrayList<>();
   private int scroll;

   private float hue = 0.0F;

   private final java.util.ArrayDeque<net.minecraft.nbt.CompoundTag> history = new java.util.ArrayDeque<>();
   private boolean typingRun;

   private void pushUndo() {
      this.typingRun = false;
      if (this.history.size() >= 64) this.history.removeLast();
      this.history.push(this.layout.save());
   }

   private void pushUndoForTyping() {
      if (this.typingRun) return;
      if (this.history.size() >= 64) this.history.removeLast();
      this.history.push(this.layout.save());
      this.typingRun = true;
   }

   private void undoLast() {
      net.minecraft.nbt.CompoundTag tag = this.history.poll();
      if (tag == null) return;
      BookPageLayout restored = BookPageLayout.load(tag);

      this.layout.elements.clear();
      this.layout.elements.addAll(restored.elements);
      this.layout.finished = restored.finished;

      select(null);
      this.typingRun = false;
   }

   public GuiBookEditor(Screen parent, BlockPos podiumPos, int page, BookPageLayout existing) {
      super(Component.translatable("gui.som.book_editor"));
      this.parent = parent;
      this.podiumPos = podiumPos;
      this.page = page;
      this.layout = existing == null ? new BookPageLayout() : existing.copy();
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   protected void init() {
      int contentW = COL_W + 6 + PAGE_SIZE + 6 + PANEL_W;
      int contentX = (this.width - contentW) / 2;
      this.colX = contentX;
      this.bookX = contentX + COL_W + 6;
      this.bookY = Math.max(2, (this.height - PAGE_SIZE) / 2);
      this.panelX = this.bookX + PAGE_SIZE + 6;
      this.gridY = this.bookY + 40;

      int y = this.bookY + 10;
      addButton("+ Text", this.colX, y, COL_W, () -> add(Kind.TEXT));
      addButton("+ Title", this.colX, y + 22, COL_W, () -> add(Kind.TITLE));
      addButton("+ Slot", this.colX, y + 44, COL_W, () -> add(Kind.SLOT));
      addButton("Bold", this.colX, y + 66, 33, () -> wrapCode("/b"));
      addButton("Italic", this.colX + 37, y + 66, 33, () -> wrapCode("/i"));
      addButton("Bigger", this.colX, y + 88, 33, () -> resize(0.1F));
      addButton("Smaller", this.colX + 37, y + 88, 33, () -> resize(-0.1F));
      addButton("Delete", this.colX, y + 110, COL_W, this::deleteSelected);
      addButton("Finish", this.colX, y + 138, COL_W, this::finish);
      addButton("Done", this.colX, y + 166, COL_W, this::done);
      addButton("Cancel", this.colX, y + 188, COL_W, this::onClose);

      addButton("Items", this.panelX, this.bookY, 34, () -> setMode(PanelMode.ITEMS));
      addButton("Color", this.panelX + 37, this.bookY, 34, () -> setMode(PanelMode.COLORS));
      addButton("Temp.", this.panelX + 74, this.bookY, 34, () -> setMode(PanelMode.TEMPLATES));

      this.search = new EditBox(this.font, this.panelX, this.bookY + 22, PANEL_W, 14,
         Component.translatable("gui.som.book_editor.search"));
      this.search.setMaxLength(48);
      this.search.setResponder(s -> {
         this.scroll = 0;
         filter(s);
      });
      addRenderableWidget(this.search);

      this.hexBox = new EditBox(this.font, this.panelX, this.bookY + 22, PANEL_W, 14,
         Component.translatable("gui.som.book_editor.hex"));
      this.hexBox.setMaxLength(7);
      this.hexBox.setResponder(this::applyHex);
      addRenderableWidget(this.hexBox);

      if (this.allItems.isEmpty()) collectCreativeItems();
      filter("");
      setMode(this.mode);
   }

   private void collectCreativeItems() {
      Set<String> seen = new HashSet<>();
      try {
         for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
            if (tab.getType() != CreativeModeTab.Type.CATEGORY) continue;
            for (ItemStack stack : tab.getDisplayItems()) {
               if (stack.isEmpty() || !seen.add(idOf(stack))) continue;
               this.allItems.add(stack);
            }
         }
      } catch (Exception ignored) {
      }

      if (this.allItems.isEmpty()) {
         for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item == Items.AIR) continue;
            this.allItems.add(new ItemStack(item));
         }
      }
   }

   private Button addButton(String text, int x, int y, int w, Runnable action) {
      return addRenderableWidget(Button.builder(Component.literal(text), b -> action.run())
         .bounds(x, y, w, 20).build());
   }

   private void setMode(PanelMode next) {
      this.mode = next;
      this.scroll = 0;
      if (this.search != null) this.search.visible = next == PanelMode.ITEMS;
      if (this.hexBox != null) this.hexBox.visible = next == PanelMode.COLORS;
   }

   private void filter(String query) {
      this.shown.clear();
      String q = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();
      for (ItemStack stack : this.allItems) {
         if (q.isEmpty() || stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains(q)) {
            this.shown.add(stack);
         }
      }
   }

   private int pageX(double mouseX) {
      return (int) Math.round(mouseX - this.bookX);
   }

   private int pageY(double mouseY) {
      return (int) Math.round(mouseY - this.bookY);
   }

   private boolean overPage(double mouseX, double mouseY) {
      return mouseX >= this.bookX && mouseX < this.bookX + PAGE_SIZE
         && mouseY >= this.bookY && mouseY < this.bookY + PAGE_SIZE;
   }

   private static boolean isText(Element e) {
      return e != null && (e.kind == Kind.TEXT || e.kind == Kind.TITLE);
   }

   private int[] bounds(Element e) {
      int w = Math.max(4, e.w);
      int h = Math.max(4, e.h);
      if (e.kind == Kind.TITLE) {
         return new int[]{e.x - w / 2, e.y - h / 2, w, h};
      }

      if (e.kind == Kind.TEXT) {
         float sc = textScale(e);
         int lines = wrapRanges(e.value == null ? "" : e.value, Math.max(8, Math.round(w / sc))).size();
         int used = Math.round(lines * this.font.lineHeight * sc) + 2;
         return new int[]{e.x, e.y, w, Math.max(8, Math.min(h, used))};
      }
      if (e.kind == Kind.TEMPLATE) {
         BookTemplates.Template t = BookTemplates.byName(e.value);
         if (t != null) {
            return new int[]{e.x + Math.round(t.hitX * e.scale), e.y + Math.round(t.hitY * e.scale),
               Math.round(t.hitW * e.scale), Math.round(t.hitH * e.scale)};
         }
      }

      if (e.kind == Kind.ITEM || e.kind == Kind.SLOT) {
         return new int[]{e.x, e.y, Math.round(w * e.scale), Math.round(h * e.scale)};
      }

      if (e.kind == Kind.ENTITY) {
         return new int[]{e.x - w / 2, e.y - h, w, h};
      }
      return new int[]{e.x, e.y, w, h};
   }

   private Element elementAt(int px, int py) {
      for (int i = this.layout.elements.size() - 1; i >= 0; i--) {
         Element e = this.layout.elements.get(i);
         int[] b = bounds(e);
         if (px >= b[0] && px < b[0] + b[2] && py >= b[1] && py < b[1] + b[3]) return e;
      }
      return null;
   }

   private void add(Kind kind) {
      pushUndo();
      Element e = Element.atHome(kind);
      this.layout.elements.add(e);
      select(e);
   }

   private void select(Element e) {
      this.selected = e;
      this.caret = e != null && e.value != null ? e.value.length() : 0;
      this.anchor = this.caret;

      setFocused(null);
   }

   private void deleteSelected() {
      if (this.selected == null) return;
      pushUndo();
      this.layout.elements.remove(this.selected);
      select(null);
   }

   private void wrapCode(String code) {
      if (!isText(this.selected)) return;
      pushUndo();
      String v = this.selected.value == null ? "" : this.selected.value;
      int from = Math.min(this.caret, this.anchor);
      int to = Math.max(this.caret, this.anchor);
      if (from == to) {
         from = 0;
         to = v.length();
      }
      String mid = v.substring(from, to);
      if (mid.startsWith(code) && mid.endsWith("/z")) {
         mid = mid.substring(code.length(), mid.length() - 2);
      } else {
         mid = code + mid + "/z";
      }
      this.selected.value = v.substring(0, from) + mid + v.substring(to);
      this.caret = from + mid.length();
      this.anchor = this.caret;
   }

   private void resize(float by) {
      if (this.selected == null) return;
      pushUndo();

      if (this.selected.kind == Kind.ENTITY) {
         int w = Math.max(4, this.selected.w);
         float ratio = this.selected.h <= 0 ? 1.0F : (float) this.selected.h / w;
         int nw = Math.max(8, Math.min(140, Math.round(w * (1.0F + by))));
         if (nw == w) nw = by > 0 ? w + 1 : w - 1;
         this.selected.w = Math.max(8, Math.min(140, nw));
         this.selected.h = Math.max(8, Math.round(this.selected.w * ratio));
         return;
      }
      this.selected.scale = Math.max(0.3F, Math.min(3.0F, this.selected.scale + by));
   }

   private void applyHex(String text) {
      if (this.selected == null || text == null) return;
      String s = text.startsWith("#") ? text.substring(1) : text;
      if (s.length() != 6) return;
      try {
         this.selected.color = Integer.parseInt(s, 16);
      } catch (NumberFormatException ignored) {
      }
   }

   private void save(boolean finished) {
      this.layout.finished = finished || this.layout.finished;
      PacketHandler.INSTANCE.sendToServer(
         new PacketSetPageLayout(this.podiumPos, this.page, this.layout.save()));
      IBook book = book();
      if (book != null) book.setPageLayout(this.page, this.layout);
      onClose();
   }

   private void done() {
      save(false);
   }

   private void finish() {
      save(true);
   }

   private TileEntityPodium podium() {
      if (this.minecraft == null || this.minecraft.level == null) return null;
      return this.minecraft.level.getBlockEntity(this.podiumPos) instanceof TileEntityPodium p ? p : null;
   }

   private IBook book() {
      TileEntityPodium podium = podium();
      if (podium == null) return null;
      return podium.handler.getStackInSlot(0)
         .getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
   }

   @Override
   public void onClose() {
      if (this.minecraft != null) this.minecraft.setScreen(this.parent);
   }

   @Override
   public void tick() {
      super.tick();
      this.ticks++;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partial) {
      renderBackground(gg);

      IBook book = book();
      if (book != null) {
         gg.blit(book.getCover(), this.bookX, this.bookY, 0, 0, PAGE_SIZE, PAGE_SIZE);
         gg.blit(book.getLinkLocation(), this.bookX, this.bookY, 0, 0, PAGE_SIZE, PAGE_SIZE);
      }
      gg.blit(PodiumGuiHelper.PAGE_DEFAULT, this.bookX, this.bookY, 0, 0, PAGE_SIZE, PAGE_SIZE);

      for (Element e : this.layout.elements) {
         if (e == this.selected && isText(e)) {
            drawRaw(gg, e);
         } else {
            BookLayoutRenderer.drawElement(gg, e, this.bookX, this.bookY, true);
         }
      }

      Element hovered = overPage(mouseX, mouseY) ? elementAt(pageX(mouseX), pageY(mouseY)) : null;
      if (hovered != null && hovered != this.selected) outline(gg, hovered, HOVER_EDGE);
      if (this.selected != null) outline(gg, this.selected, SELECT_EDGE);

      panel(gg, this.colX - 4, this.bookY + 4, COL_W + 8, 226);
      panel(gg, this.panelX - 4, this.bookY - 4, PANEL_W + 8, PICKER_ROWS * CELL + 52);

      super.render(gg, mouseX, mouseY, partial);

      switch (this.mode) {
         case ITEMS -> drawItemPanel(gg, mouseX, mouseY);
         case COLORS -> drawColorPanel(gg, mouseX, mouseY);
         case TEMPLATES -> drawTemplatePanel(gg, mouseX, mouseY);
      }

      if (!this.dragStack.isEmpty()) {
         gg.renderItem(this.dragStack, mouseX - 8, mouseY - 8);
      }

      String hint = this.selected == null
         ? "Click a piece to move it, right click to remove"
         : "Typing writes into the selected " + this.selected.kind.name().toLowerCase(Locale.ROOT);
      gg.drawString(this.font, hint, this.bookX, this.bookY + PAGE_SIZE + 4, LABEL, false);
   }

   private void panel(GuiGraphics gg, int x, int y, int w, int h) {
      gg.fill(x, y, x + w, y + h, PANEL_BG);
      gg.renderOutline(x, y, w, h, PANEL_EDGE);
   }

   private void outline(GuiGraphics gg, Element e, int color) {
      int[] b = bounds(e);
      gg.renderOutline(this.bookX + b[0], this.bookY + b[1], b[2], b[3], color);
   }

   private float textScale(Element e) {
      return 0.75F * Math.max(0.1F, e.scale);
   }

   private List<int[]> wrapRanges(String s, int wrapW) {
      List<int[]> lines = new ArrayList<>();
      int n = s.length();
      int i = 0;
      while (true) {
         int lastSpace = -1;
         int j = i;
         float w = 0.0F;
         boolean hard = false;
         for (; j < n; j++) {
            char c = s.charAt(j);
            if (c == '\n') { hard = true; break; }
            if (c == ' ') lastSpace = j;
            float cw = this.font.width(String.valueOf(c));
            if (w + cw > wrapW && j > i) break;
            w += cw;
         }
         int end;
         int next;
         if (hard) { end = j; next = j + 1; }
         else if (j >= n) { end = n; next = n + 1; }
         else if (lastSpace >= i) { end = lastSpace; next = lastSpace + 1; }
         else { end = j; next = Math.max(j, i + 1); }
         lines.add(new int[]{i, end});
         if (next > n) break;
         i = next;
      }
      return lines;
   }

   private int indexAt(Element e, double mx, double my) {
      String v = e.value == null ? "" : e.value;
      float sc = textScale(e);
      int[] b = bounds(e);
      int wrapW = Math.max(8, Math.round(b[2] / sc));
      List<int[]> lines = wrapRanges(v, wrapW);
      double localX = (mx - (this.bookX + b[0])) / sc;
      double localY = (my - (this.bookY + b[1])) / sc;
      int row = (int) Math.floor(localY / this.font.lineHeight);
      row = Math.max(0, Math.min(lines.size() - 1, row));
      int[] line = lines.get(row);
      float w = 0.0F;
      for (int k = line[0]; k < line[1]; k++) {
         float cw = this.font.width(String.valueOf(v.charAt(k)));
         if (localX < w + cw / 2.0F) return k;
         w += cw;
      }
      return line[1];
   }

   private void drawRaw(GuiGraphics gg, Element e) {
      String v = e.value == null ? "" : e.value;
      float sc = textScale(e);
      int[] b = bounds(e);
      int wrapW = Math.max(8, Math.round(b[2] / sc));
      List<int[]> lines = wrapRanges(v, wrapW);
      int from = Math.min(this.caret, this.anchor);
      int to = Math.max(this.caret, this.anchor);

      gg.pose().pushPose();
      gg.pose().scale(sc, sc, 1.0F);
      int lx = Math.round((this.bookX + b[0]) / sc);
      int ly = Math.round((this.bookY + b[1]) / sc);

      for (int row = 0; row < lines.size(); row++) {
         int[] line = lines.get(row);
         int ty = ly + row * this.font.lineHeight;
         if (from != to) {
            int selFrom = Math.max(from, line[0]);
            int selTo = Math.min(to, line[1]);
            if (selFrom < selTo) {
               int x0 = lx + this.font.width(v.substring(line[0], selFrom));
               int x1 = lx + this.font.width(v.substring(line[0], selTo));
               gg.fill(x0, ty - 1, x1, ty + this.font.lineHeight - 1, SELECTION);
            }
         }
         gg.drawString(this.font, v.substring(line[0], line[1]), lx, ty, e.color, false);
         if (this.ticks / 6 % 2 == 0 && this.caret >= line[0]
               && (this.caret < line[1] || (row == lines.size() - 1 && this.caret <= line[1]))) {
            int cx = lx + this.font.width(v.substring(line[0], this.caret));
            gg.fill(cx, ty - 1, cx + 1, ty + this.font.lineHeight - 1, 0xFF000000 | e.color);
         }
      }
      gg.pose().popPose();
   }

   private void drawItemPanel(GuiGraphics gg, int mouseX, int mouseY) {
      ItemStack tooltip = ItemStack.EMPTY;
      for (int i = 0; i < PICKER_COLS * PICKER_ROWS; i++) {
         int idx = this.scroll * PICKER_COLS + i;
         if (idx >= this.shown.size()) break;
         int cx = this.panelX + (i % PICKER_COLS) * CELL;
         int cy = this.gridY + (i / PICKER_COLS) * CELL;
         boolean over = mouseX >= cx && mouseX < cx + CELL && mouseY >= cy && mouseY < cy + CELL;
         gg.fill(cx, cy, cx + CELL, cy + CELL, over ? 0x60FFFFFF : 0x30000000);
         gg.renderItem(this.shown.get(idx), cx + 1, cy + 1);
         if (over) tooltip = this.shown.get(idx);
      }
      if (!tooltip.isEmpty() && this.dragStack.isEmpty()) {
         gg.renderTooltip(this.font, tooltip, mouseX, mouseY);
      }
   }

   private void drawTemplatePanel(GuiGraphics gg, int mouseX, int mouseY) {
      for (int i = 0; i < BookTemplates.ALL.size(); i++) {
         BookTemplates.Template t = BookTemplates.ALL.get(i);
         int ty = this.gridY + i * 20;
         boolean over = mouseX >= this.panelX && mouseX < this.panelX + PANEL_W
            && mouseY >= ty && mouseY < ty + 18;
         gg.fill(this.panelX, ty, this.panelX + PANEL_W, ty + 18, over ? 0x60FFFFFF : 0x30000000);
         gg.drawString(this.font, t.label, this.panelX + 3, ty + 5, LABEL, false);
      }
   }

   private void drawColorPanel(GuiGraphics gg, int mouseX, int mouseY) {
      int sx = this.panelX;
      int sy = this.gridY;
      int sqW = PANEL_W - 16;
      int sqH = 60;

      for (int i = 0; i < sqW; i += 2) {
         int top = Color.HSBtoRGB(this.hue, (float) i / sqW, 1.0F);
         gg.fillGradient(sx + i, sy, sx + i + 2, sy + sqH, 0xFF000000 | top, 0xFF000000);
      }
      gg.renderOutline(sx, sy, sqW, sqH, PANEL_EDGE);

      int hx = sx + sqW + 3;
      for (int i = 0; i < sqH; i += 2) {
         gg.fill(hx, sy + i, hx + 11, sy + i + 2, 0xFF000000 | Color.HSBtoRGB((float) i / sqH, 1.0F, 1.0F));
      }
      gg.renderOutline(hx, sy, 11, sqH, PANEL_EDGE);

      List<java.util.Map.Entry<String, Integer>> palette = BookRichText.palette();
      int py = sy + sqH + 6;
      for (int i = 0; i < palette.size(); i++) {
         int cx = this.panelX + (i % PICKER_COLS) * CELL;
         int cy = py + (i / PICKER_COLS) * CELL;
         boolean over = mouseX >= cx && mouseX < cx + CELL && mouseY >= cy && mouseY < cy + CELL;
         gg.fill(cx + 1, cy + 1, cx + CELL - 1, cy + CELL - 1, 0xFF000000 | palette.get(i).getValue());
         if (over) {
            gg.renderOutline(cx, cy, CELL, CELL, 0xFFFFFFFF);
            gg.renderTooltip(this.font, Component.literal(palette.get(i).getKey()), mouseX, mouseY);
         }
      }

      int ri = palette.size();
      int rx = this.panelX + (ri % PICKER_COLS) * CELL;
      int ry = py + (ri / PICKER_COLS) * CELL;
      for (int i = 0; i < CELL - 2; i++) {
         gg.fill(rx + 1 + i, ry + 1, rx + 2 + i, ry + CELL - 1,
            0xFF000000 | Color.HSBtoRGB((float) i / (CELL - 2), 0.85F, 0.85F));
      }
      if (mouseX >= rx && mouseX < rx + CELL && mouseY >= ry && mouseY < ry + CELL) {
         gg.renderOutline(rx, ry, CELL, CELL, 0xFFFFFFFF);
         gg.renderTooltip(this.font, Component.literal("rainbow"), mouseX, mouseY);
      }
   }

   private int colorPanelHit(double mx, double my) {
      int sx = this.panelX;
      int sy = this.gridY;
      int sqW = PANEL_W - 16;
      int sqH = 60;
      if (mx >= sx && mx < sx + sqW && my >= sy && my < sy + sqH) {
         float sat = (float) (mx - sx) / sqW;
         float bri = 1.0F - (float) (my - sy) / sqH;
         return Color.HSBtoRGB(this.hue, sat, bri) & 0xFFFFFF;
      }
      int hx = sx + sqW + 3;
      if (mx >= hx && mx < hx + 11 && my >= sy && my < sy + sqH) {
         this.hue = (float) (my - sy) / sqH;
         return -1;
      }
      List<java.util.Map.Entry<String, Integer>> palette = BookRichText.palette();
      int py = sy + sqH + 6;
      for (int i = 0; i < palette.size(); i++) {
         int cx = this.panelX + (i % PICKER_COLS) * CELL;
         int cy = py + (i / PICKER_COLS) * CELL;
         if (mx >= cx && mx < cx + CELL && my >= cy && my < cy + CELL) {
            return palette.get(i).getValue();
         }
      }
      int ri = palette.size();
      int rx = this.panelX + (ri % PICKER_COLS) * CELL;
      int ry = py + (ri / PICKER_COLS) * CELL;
      if (mx >= rx && mx < rx + CELL && my >= ry && my < ry + CELL) return RAINBOW;
      return -1;
   }

   private int pickerIndexAt(double mouseX, double mouseY) {
      if (this.mode != PanelMode.ITEMS) return -1;
      if (mouseX < this.panelX || mouseX >= this.panelX + PANEL_W) return -1;
      if (mouseY < this.gridY || mouseY >= this.gridY + PICKER_ROWS * CELL) return -1;
      int col = (int) ((mouseX - this.panelX) / CELL);
      int row = (int) ((mouseY - this.gridY) / CELL);
      int idx = (this.scroll + row) * PICKER_COLS + col;
      return idx < this.shown.size() ? idx : -1;
   }

   private String templateAt(double mouseX, double mouseY) {
      if (this.mode != PanelMode.TEMPLATES) return null;
      if (mouseX < this.panelX || mouseX >= this.panelX + PANEL_W) return null;
      for (int i = 0; i < BookTemplates.ALL.size(); i++) {
         int ty = this.gridY + i * 20;
         if (mouseY >= ty && mouseY < ty + 18) return BookTemplates.ALL.get(i).name;
      }
      return null;
   }

   @Override
   public boolean mouseClicked(double mx, double my, int button) {
      if (this.mode == PanelMode.COLORS && this.selected != null && button == 0) {
         int picked = colorPanelHit(mx, my);
         if (picked == RAINBOW) {
            wrapCode("/rainbow");
            return true;
         }
         if (picked >= 0) {
            pushUndo();
            this.selected.color = picked;
            if (this.hexBox != null) this.hexBox.setValue(String.format("#%06X", picked));
            return true;
         }
      }
      if (button == 0 || button == 1) {
         int idx = pickerIndexAt(mx, my);
         if (idx >= 0) {
            this.dragStack = this.shown.get(idx);

            this.dragAsItem = button == 1;
            this.dragTemplate = null;
            return true;
         }
         String template = templateAt(mx, my);
         if (template != null) {
            addTemplate(template, -1, -1);
            this.dragTemplate = null;
            this.dragStack = ItemStack.EMPTY;
            return true;
         }
      }
      if (overPage(mx, my)) {
         Element e = elementAt(pageX(mx), pageY(my));
         if (e != null && button == 1) {
            pushUndo();
            this.layout.elements.remove(e);
            if (this.selected == e) select(null);
            return true;
         }
         if (button == 0) {
            if (isText(this.selected) && e == this.selected) {
               this.caret = indexAt(this.selected, mx, my);
               this.anchor = this.caret;
               this.selectingText = true;
               return true;
            }
            select(e);
            if (e != null) {
               pushUndo();
               int[] b = bounds(e);
               this.dragging = true;
               this.grabX = pageX(mx) - b[0];
               this.grabY = pageY(my) - b[1];
            }
            return true;
         }
      }
      return super.mouseClicked(mx, my, button);
   }

   @Override
   public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
      if (this.selectingText && isText(this.selected) && button == 0) {
         this.caret = indexAt(this.selected, mx, my);
         return true;
      }
      if (this.dragging && this.selected != null && button == 0) {
         int[] b = bounds(this.selected);
         int nx = Math.max(4, Math.min(PAGE_SIZE - 4, pageX(mx) - this.grabX));
         int ny = Math.max(4, Math.min(PAGE_SIZE - 4, pageY(my) - this.grabY));
         this.selected.x += nx - b[0];
         this.selected.y += ny - b[1];
         return true;
      }
      return super.mouseDragged(mx, my, button, dx, dy);
   }

   @Override
   public boolean mouseReleased(double mx, double my, int button) {
      this.selectingText = false;

      if (this.dragging && this.selected != null && button == 0 && !overPage(mx, my)) {
         this.layout.elements.remove(this.selected);
         select(null);
         this.dragging = false;
         return true;
      }
      if (this.dragTemplate != null) {
         String name = this.dragTemplate;
         this.dragTemplate = null;
         if (overPage(mx, my)) addTemplate(name, pageX(mx), pageY(my));
         return true;
      }
      if (!this.dragStack.isEmpty()) {
         ItemStack dropped = this.dragStack;
         boolean asItem = this.dragAsItem;
         this.dragStack = ItemStack.EMPTY;
         if (overPage(mx, my)) dropOnPage(dropped, asItem, mx, my);
         return true;
      }
      this.dragging = false;
      return super.mouseReleased(mx, my, button);
   }

   private void dropOnPage(ItemStack dropped, boolean asItem, double mx, double my) {
      pushUndo();
      String id = idOf(dropped);
      Element onto = elementAt(pageX(mx), pageY(my));

      if (onto != null && onto.kind == Kind.TEMPLATE) {
         BookTemplates.Template t = BookTemplates.byName(onto.value);
         if (t != null) {
            for (int i = 0; i < t.slots.length; i++) {
               int sx = onto.x + t.slots[i][0];
               int sy = onto.y + t.slots[i][1];
               if (pageX(mx) >= sx && pageX(mx) < sx + 16 && pageY(my) >= sy && pageY(my) < sy + 16) {
                  while (onto.slots.size() <= i) onto.slots.add("");
                  onto.slots.set(i, stackOnto(onto.slots.get(i), id));
                  select(onto);
                  return;
               }
            }
         }
      }
      if (onto != null && (onto.kind == Kind.SLOT || onto.kind == Kind.ITEM)) {
         onto.value = stackOnto(onto.value, id);
         select(onto);
         return;
      }

      if (!asItem && dropped.getItem() instanceof SpawnEggItem egg) {
         net.minecraft.world.entity.EntityType<?> type = egg.getType(null);
         net.minecraft.resources.ResourceLocation rl =
            type == null ? null : ForgeRegistries.ENTITY_TYPES.getKey(type);
         if (rl != null) {
            Element e = Element.of(Kind.ENTITY, pageX(mx), pageY(my) + 8);
            e.value = rl.toString();
            e.w = 24;
            e.h = 32;
            this.layout.elements.add(e);
            select(e);
            return;
         }
      }
      Element e = Element.of(Kind.ITEM, pageX(mx) - 8, pageY(my) - 8);
      e.value = id;
      this.layout.elements.add(e);
      select(e);
   }

   private void addTemplate(String name, int px, int py) {
      BookTemplates.Template t = BookTemplates.byName(name);
      if (t == null) return;
      pushUndo();
      Element e = Element.of(Kind.TEMPLATE, px < 0 ? t.homeX : px, py < 0 ? t.homeY : py);
      e.value = name;
      e.w = t.width;
      e.h = t.height;
      for (int i = 0; i < t.slots.length; i++) e.slots.add("");
      if (t.fixedSlot >= 0 && t.fixedSlot < e.slots.size()) {
         e.slots.set(t.fixedSlot, t.fixedItem);
      }
      this.layout.elements.add(e);
      select(e);
   }

   private static String stackOnto(String existing, String dropped) {
      if (existing != null && !existing.isEmpty()
            && BookLayoutRenderer.basePart(existing).equals(BookLayoutRenderer.basePart(dropped))) {
         return BookLayoutRenderer.withCount(dropped, BookLayoutRenderer.countPart(existing) + 1);
      }
      return dropped;
   }

   private static String idOf(ItemStack stack) {
      net.minecraft.resources.ResourceLocation rl = ForgeRegistries.ITEMS.getKey(stack.getItem());
      if (rl == null) return "";
      int damage = stack.getDamageValue();
      return damage > 0 ? rl + "@" + damage : rl.toString();
   }

   @Override
   public boolean mouseScrolled(double mx, double my, double delta) {
      if (this.mode == PanelMode.ITEMS && mx >= this.panelX && mx < this.panelX + PANEL_W) {
         int rows = (this.shown.size() + PICKER_COLS - 1) / PICKER_COLS;
         int max = Math.max(0, rows - PICKER_ROWS);
         this.scroll = Math.max(0, Math.min(max, this.scroll - (int) Math.signum(delta)));
         return true;
      }
      return super.mouseScrolled(mx, my, delta);
   }

   private boolean boxFocused() {
      return (this.search != null && this.search.visible && this.search.isFocused())
         || (this.hexBox != null && this.hexBox.visible && this.hexBox.isFocused());
   }

   @Override
   public boolean charTyped(char c, int mods) {
      if (boxFocused()) return super.charTyped(c, mods);
      if (isText(this.selected) && c != '\r' && c != '\n') {
         insert(String.valueOf(c));
         return true;
      }
      return super.charTyped(c, mods);
   }

   @Override
   public boolean keyPressed(int key, int scan, int mods) {
      if (boxFocused()) return super.keyPressed(key, scan, mods);

      if (key == org.lwjgl.glfw.GLFW.GLFW_KEY_Z
            && net.minecraft.client.gui.screens.Screen.hasControlDown()) {
         undoLast();
         return true;
      }
      if (key == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE && this.selected != null) {
         select(null);
         return true;
      }
      if (!isText(this.selected)) return super.keyPressed(key, scan, mods);

      if (key == org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE) return true;

      String v = this.selected.value == null ? "" : this.selected.value;
      boolean shift = (mods & org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT) != 0;
      switch (key) {
         case org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE -> {
            if (this.caret != this.anchor) {
               deleteSelection();
            } else if (this.caret > 0) {
               pushUndoForTyping();
               this.selected.value = v.substring(0, this.caret - 1) + v.substring(this.caret);
               this.caret--;
               this.anchor = this.caret;
            }
            return true;
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE -> {
            if (this.caret != this.anchor) {
               deleteSelection();
            } else if (this.caret < v.length()) {
               pushUndoForTyping();
               this.selected.value = v.substring(0, this.caret) + v.substring(this.caret + 1);
            }
            return true;
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT -> {
            if (this.caret > 0) this.caret--;
            if (!shift) this.anchor = this.caret;
            return true;
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT -> {
            if (this.caret < v.length()) this.caret++;
            if (!shift) this.anchor = this.caret;
            return true;
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_HOME -> {
            this.caret = 0;
            if (!shift) this.anchor = this.caret;
            return true;
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_END -> {
            this.caret = v.length();
            if (!shift) this.anchor = this.caret;
            return true;
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_A -> {
            if (net.minecraft.client.gui.screens.Screen.hasControlDown()) {
               this.anchor = 0;
               this.caret = v.length();
               return true;
            }
            return super.keyPressed(key, scan, mods);
         }
         case org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER, org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER -> {
            if (this.selected.kind == Kind.TEXT) insert("\n");
            return true;
         }
         default -> {
            return super.keyPressed(key, scan, mods);
         }
      }
   }

   private void deleteSelection() {
      pushUndoForTyping();
      String v = this.selected.value == null ? "" : this.selected.value;
      int from = Math.min(this.caret, this.anchor);
      int to = Math.max(this.caret, this.anchor);
      this.selected.value = v.substring(0, from) + v.substring(to);
      this.caret = from;
      this.anchor = from;
   }

   private void insert(String s) {
      pushUndoForTyping();
      if (this.caret != this.anchor) deleteSelection();
      String v = this.selected.value == null ? "" : this.selected.value;
      int at = Math.max(0, Math.min(this.caret, v.length()));
      this.selected.value = v.substring(0, at) + s + v.substring(at);
      this.caret = at + s.length();
      this.anchor = this.caret;
   }
}
