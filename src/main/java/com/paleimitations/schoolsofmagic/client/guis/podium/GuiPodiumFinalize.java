package com.paleimitations.schoolsofmagic.client.guis.podium;

import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageWriteable;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumFinal;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketInsertSticker;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiPodiumFinalize extends AbstractContainerScreen<ContainerPodiumFinal> {
   public static final ResourceLocation ICONS = new ResourceLocation("som", "textures/gui/podium/icons.png");
   public static final ResourceLocation ICON_BAR = new ResourceLocation("som", "textures/gui/podium/icon_bar.png");
   public static final ResourceLocation FINAL = new ResourceLocation("som", "textures/gui/podium/personalize.png");
   public static final ResourceLocation OAK_FINAL = new ResourceLocation("som", "textures/gui/podium/oak_final.png");
   public static final ResourceLocation SPRUCE_FINAL = new ResourceLocation("som", "textures/gui/podium/spruce_final.png");
   public static final ResourceLocation BIRCH_FINAL = new ResourceLocation("som", "textures/gui/podium/birch_final.png");
   public static final ResourceLocation ACACIA_FINAL = new ResourceLocation("som", "textures/gui/podium/acacia_final.png");
   public static final ResourceLocation JUNGLE_FINAL = new ResourceLocation("som", "textures/gui/podium/jungle_final.png");
   public static final ResourceLocation DARK_OAK_FINAL = new ResourceLocation("som", "textures/gui/podium/dark_oak_final.png");
   public static final ResourceLocation ASH_FINAL = new ResourceLocation("som", "textures/gui/podium/ash_final.png");
   public static final ResourceLocation ELDER_FINAL = new ResourceLocation("som", "textures/gui/podium/elder_final.png");
   public static final ResourceLocation PINE_FINAL = new ResourceLocation("som", "textures/gui/podium/pine_final.png");
   public static final ResourceLocation WILLOW_FINAL = new ResourceLocation("som", "textures/gui/podium/willow_final.png");
   public static final ResourceLocation YEW_FINAL = new ResourceLocation("som", "textures/gui/podium/yew_final.png");
   public static final ResourceLocation VERDE_FINAL = new ResourceLocation("som", "textures/gui/podium/verde_final.png");

   private EnumPersonalizeState state = EnumPersonalizeState.STICKER;
   private EnumWriteState writeState = EnumWriteState.NONE;
   private float stickerX = 100.0F;
   private float stickerY = 67.0F;
   private float stickerR = 0.0F;

   public GuiPodiumFinalize(ContainerPodiumFinal menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private TileEntityPodium getPodium() { return this.menu.getPodium(); }

   private ResourceLocation getTexture() {
      TileEntityPodium p = getPodium();
      if (p == null) return OAK_FINAL;
      return switch (p.getWood()) {
         case OAK -> OAK_FINAL; case SPRUCE -> SPRUCE_FINAL; case BIRCH -> BIRCH_FINAL;
         case ACACIA -> ACACIA_FINAL; case JUNGLE -> JUNGLE_FINAL; case DARK_OAK -> DARK_OAK_FINAL;
         case ASH -> ASH_FINAL; case ELDER -> ELDER_FINAL; case PINE -> PINE_FINAL;
         case WILLOW -> WILLOW_FINAL; case YEW -> YEW_FINAL; case VERDE -> VERDE_FINAL;
      };
   }

   @Override
   protected void init() {
      super.init();
      TileEntityPodium podium = getPodium();
      if (podium == null) return;

      this.addRenderableWidget(new PodiumSwitchButton(podium, 3, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 3, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 3, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 3, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 3, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 3, 5, this.leftPos + 154, this.topPos + 130));

      this.addRenderableWidget(new TurnPageButton(podium, true,  this.leftPos + 168, this.topPos + 36));
      this.addRenderableWidget(new TurnPageButton(podium, false, this.leftPos + 200, this.topPos + 36));

      this.addRenderableWidget(new SwitchStateButton(EnumPersonalizeState.STICKER, this.leftPos + 147, this.topPos + 12));
      this.addRenderableWidget(new SwitchStateButton(EnumPersonalizeState.INSERT,  this.leftPos + 176, this.topPos + 12));
      this.addRenderableWidget(new SwitchStateButton(EnumPersonalizeState.WRITE,   this.leftPos + 205, this.topPos + 12));

      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.UP,       this.leftPos + 182, this.topPos + 61));
      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.DOWN,     this.leftPos + 182, this.topPos + 97));
      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.LEFT,     this.leftPos + 164, this.topPos + 78));
      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.RIGHT,    this.leftPos + 200, this.topPos + 78));
      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.CW,       this.leftPos + 167, this.topPos + 92));
      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.CCW,      this.leftPos + 197, this.topPos + 92));
      this.addRenderableWidget(new MoveStickerButton(MoveStickerButton.Kind.CONFIRM,  this.leftPos + 146, this.topPos + 78));

      this.addRenderableWidget(new InsertButton(InsertButton.Kind.TABLE_OF_CONTENTS, this.leftPos + 146, this.topPos + 61));
      this.addRenderableWidget(new InsertButton(InsertButton.Kind.CHAPTER,          this.leftPos + 146, this.topPos + 79));
      this.addRenderableWidget(new InsertButton(InsertButton.Kind.PAGE,             this.leftPos + 146, this.topPos + 97));

      this.addRenderableWidget(new EditButton(EditButton.Kind.TITLE, this.leftPos + 146, this.topPos + 61));
      this.addRenderableWidget(new EditButton(EditButton.Kind.PAGE,  this.leftPos + 146, this.topPos + 79));
      this.addRenderableWidget(new EditButton(EditButton.Kind.BOOK,  this.leftPos + 146, this.topPos + 97));

      this.bookNameBox = new EditBox(this.font, this.leftPos + 60, this.topPos + 70, 110, 12, Component.empty());
      this.bookNameBox.setMaxLength(48);
      this.bookNameBox.setBordered(true);
      this.bookNameBox.setVisible(false);
      ItemStack held = podium.handler.getStackInSlot(0);
      if (held.hasCustomHoverName()) {
         this.bookNameBox.setValue(held.getHoverName().getString());
      }

      this.addWidget(this.bookNameBox);
   }

   private EditBox bookNameBox;

   private void commitWrittenPage() {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      BookPageWriteable page = currentWriteablePage();
      if (page == null) return;
      PacketHandler.INSTANCE.sendToServer(
         new com.paleimitations.schoolsofmagic.common.network.PacketWritePage(
            podium.getBlockPos(), podium.getPage(), page.getName()));
      writeState = EnumWriteState.NONE;
   }

   @Override
   public void removed() {
      if (state == EnumPersonalizeState.WRITE
            && (writeState == EnumWriteState.PAGE || writeState == EnumWriteState.TITLE)) {
         commitWrittenPage();
      }
      super.removed();
   }

   private void commitBookName() {
      TileEntityPodium podium = getPodium();
      if (podium == null || this.bookNameBox == null) return;
      PacketHandler.INSTANCE.sendToServer(
         new com.paleimitations.schoolsofmagic.common.network.PacketRenameBook(
            podium.getBlockPos(), this.bookNameBox.getValue()));
      writeState = EnumWriteState.NONE;
   }

   private boolean draggingSticker = false;

   @Override
   public boolean mouseClicked(double mx, double my, int button) {
      if (button == 0 && clickedQuill(mx, my)) {
         return true;
      }

      if (button == 0 && editingText() && placeCaretFromClick(mx, my)) {
         this.editAnchor = this.caret;
         this.draggingCaret = true;
         return true;
      }

      if (button == 0) {
         TileEntityPodium podium = getPodium();
         if (podium != null) {
            PodiumGuiHelper.clickGuiSubject(
               (float) (mx - this.leftPos) - 17.886177F,
               (float) (my - this.topPos) - 10.642276F,
               podium.handler.getStackInSlot(0), podium, false);
         }
      }
      if (state == EnumPersonalizeState.STICKER && button == 0) {
         double sx = this.leftPos + stickerX;
         double sy = this.topPos + stickerY;
         if (mx >= sx - 8 && mx <= sx + 8 && my >= sy - 8 && my <= sy + 8) {
            draggingSticker = true;
            return true;
         }
      }
      return super.mouseClicked(mx, my, button);
   }

   @Override
   public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
      if (draggingCaret && button == 0 && editingText()) {
         placeCaretFromClick(mx, my);
         return true;
      }
      if (draggingSticker && button == 0) {
         stickerX = (float) Math.max(0, Math.min(200, mx - this.leftPos));
         stickerY = (float) Math.max(0, Math.min(135, my - this.topPos));
         return true;
      }
      return super.mouseDragged(mx, my, button, dx, dy);
   }

   @Override
   public boolean mouseReleased(double mx, double my, int button) {
      if (draggingCaret && button == 0) {
         draggingCaret = false;
         return true;
      }
      if (draggingSticker && button == 0) {
         draggingSticker = false;
         return true;
      }
      return super.mouseReleased(mx, my, button);
   }

   @Override
   public boolean mouseScrolled(double mx, double my, double delta) {
      if (state == EnumPersonalizeState.STICKER) {
         stickerR = (stickerR + (float)(delta * 15.0)) % 360.0F;
         return true;
      }
      return super.mouseScrolled(mx, my, delta);
   }

   private IBook currentBook() {
      TileEntityPodium p = getPodium();
      if (p == null) return null;
      return p.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
   }

   private String convertedCurrentPage() {
      IBook book = currentBook();
      BookPage page = book == null ? null : book.getCurrentPage();
      if (page == null) {
         return new BookPageWriteable("", "").getName();
      }
      String title = "";
      StringBuilder body = new StringBuilder();
      for (PageElement element : page.elements) {
         if (element instanceof PageElementTitle t
               && t.text != null && t.text.length > 0 && t.text[0] != null) {
            if (title.isEmpty()) title = t.text[0];
         } else if (element instanceof com.paleimitations.schoolsofmagic.common.books.PageElementStandardText s) {
            if (title.isEmpty()) {
               title = net.minecraft.client.resources.language.I18n.get(s.textLocation);
            }
         } else if (element instanceof com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs p) {
            p.loadText();
            for (String line : p.text) {
               if (line == null || line.isEmpty()) continue;
               if (body.length() > 0) body.append("\n\n");
               body.append(line);
            }
         }
      }

      title = title.replace("<title>", "").replace("<paragraph>", "");
      String text = body.toString().replace("<title>", "").replace("<paragraph>", "");
      return new BookPageWriteable(title, text).getName();
   }

   private com.paleimitations.schoolsofmagic.common.books.BookTextOverride editBuffer;
   private int editPage = -1;
   private int caret = -1;
   private boolean previewCaretShown;
   private boolean draggingCaret;

   private int editAnchor = -1;

   private boolean hasSelection() {
      return this.editAnchor >= 0 && this.caret >= 0 && this.editAnchor != this.caret;
   }

   private int selFrom() { return Math.min(this.caret, this.editAnchor); }

   private int selTo() { return Math.max(this.caret, this.editAnchor); }

   private int[] toParaSrc(int index) {
      boolean title = writeState == EnumWriteState.TITLE;
      if (title) return new int[]{-1, index};
      String cur = editedText(false);
      int at = Math.max(0, Math.min(this.caret < 0 ? cur.length() : this.caret, cur.length()));
      String shown = this.previewCaretShown
         ? cur.substring(0, at) + "_" + cur.substring(at) : cur;
      int shownIndex = this.previewCaretShown && index >= at ? index + 1 : index;
      String[] paras = shown.split("<~>", -1);
      int off = 0;
      for (int i = 0; i < paras.length; i++) {
         if (shownIndex <= off + paras[i].length()) return new int[]{i, shownIndex - off};
         off += paras[i].length() + 3;
      }
      return new int[]{paras.length - 1, paras[paras.length - 1].length()};
   }

   private void drawEditSelection(GuiGraphics gg) {
      if (!editingText() || !hasSelection()) return;
      int[] from = toParaSrc(selFrom());
      int[] to = toParaSrc(selTo());
      for (com.paleimitations.schoolsofmagic.client.BookRichText.Hit h
            : com.paleimitations.schoolsofmagic.client.BookRichText.captured()) {
         boolean after = h.para > from[0] || (h.para == from[0] && h.src >= from[1]);
         boolean before = h.para < to[0] || (h.para == to[0] && h.src < to[1]);
         if (after && before) {
            gg.fill(Math.round(h.x0) - this.leftPos, Math.round(h.y0) - this.topPos,
               Math.round(h.x1) - this.leftPos, Math.round(h.y1) - this.topPos, 0x804A90D9);
         }
      }
   }

   private void deleteEditSelection(boolean title) {
      if (!hasSelection()) return;
      String cur = editedText(title);
      int from = Math.max(0, Math.min(selFrom(), cur.length()));
      int to = Math.max(0, Math.min(selTo(), cur.length()));
      setEditedText(title, cur.substring(0, from) + cur.substring(to));
      this.caret = from;
      this.editAnchor = from;
   }

   private boolean editingText() {
      return state == EnumPersonalizeState.WRITE && this.editBuffer != null
         && (writeState == EnumWriteState.TITLE || writeState == EnumWriteState.PAGE);
   }

   private boolean placeCaretFromClick(double mx, double my) {
      if (!editingText()) return false;
      int[] hit = com.paleimitations.schoolsofmagic.client.BookRichText.indexAt(mx, my);
      if (hit == null) return false;
      boolean title = writeState == EnumWriteState.TITLE;

      if (title != (hit[0] < 0)) return false;

      String cur = editedText(title);
      int at = Math.max(0, Math.min(this.caret < 0 ? cur.length() : this.caret, cur.length()));
      String shown = this.previewCaretShown
         ? cur.substring(0, at) + "_" + cur.substring(at) : cur;

      int idx;
      if (title) {
         idx = hit[1];
      } else {
         String[] paras = shown.split("<~>", -1);
         if (hit[0] >= paras.length) return false;
         int off = 0;
         for (int i = 0; i < hit[0]; i++) off += paras[i].length() + 3;
         idx = off + hit[1];
      }

      if (this.previewCaretShown && idx > at) idx--;
      this.caret = Math.max(0, Math.min(idx, cur.length()));
      return true;
   }

   private com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout currentLayout() {
      IBook book = currentBook();
      return book == null ? null : book.getPageLayouts().get(book.getPage());
   }

   private com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element layoutPiece(
         com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind kind) {
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout layout = currentLayout();
      if (layout == null) return null;
      for (com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element e : layout.elements) {
         if (e.kind == kind) return e;
      }
      return null;
   }

   private String writeableTitle() {
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element laid =
         layoutPiece(com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind.TITLE);
      if (laid != null) return laid.value == null ? "" : laid.value;
      BookPageWriteable page = currentWriteablePage();
      if (page == null) return null;
      for (PageElement e : page.elements) {
         if (e instanceof PageElementTitle t && t.text != null && t.text.length > 0) {
            return t.text[0] == null ? "" : t.text[0];
         }
      }
      return "";
   }

   private String writeableBody() {
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element laid =
         layoutPiece(com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind.TEXT);
      if (laid != null) return laid.value == null ? "" : laid.value;
      BookPageWriteable page = currentWriteablePage();
      if (page == null) return null;
      for (PageElement e : page.elements) {
         if (e instanceof com.paleimitations.schoolsofmagic.common.books.PageElementWriteableParagraphs p) {
            return p.text == null ? "" : p.text;
         }
      }
      return "";
   }

   private String shippedTitle() {
      String own = writeableTitle();
      if (own != null) return own;
      IBook book = currentBook();
      BookPage page = book == null ? null : book.getCurrentPage();
      if (page == null) return "";
      for (PageElement e : page.elements) {
         if (e instanceof PageElementTitle t && t.text != null && t.text.length > 0 && t.text[0] != null) {
            return t.text[0];
         }
         if (e instanceof com.paleimitations.schoolsofmagic.common.books.PageElementStandardText s) {
            return net.minecraft.client.resources.language.I18n.get(s.textLocation);
         }
      }
      return "";
   }

   private String shippedBody() {
      String own = writeableBody();
      if (own != null) return own;
      IBook book = currentBook();
      BookPage page = book == null ? null : book.getCurrentPage();
      if (page == null) return "";
      for (PageElement e : page.elements) {
         if (e instanceof com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs p) {
            p.loadText();
            return String.join("<~>", p.text);
         }
      }
      return "";
   }

   private com.paleimitations.schoolsofmagic.common.books.BookTextOverride livePreview() {
      if (this.editBuffer == null || state != EnumPersonalizeState.WRITE
            || (writeState != EnumWriteState.TITLE && writeState != EnumWriteState.PAGE)) {
         com.paleimitations.schoolsofmagic.client.BookLayoutRenderer.clearEditPreview();
         return null;
      }
      boolean title = writeState == EnumWriteState.TITLE;
      String cur = editedText(title);
      int at = Math.max(0, Math.min(this.caret < 0 ? cur.length() : this.caret, cur.length()));

      this.previewCaretShown = (System.currentTimeMillis() / 500L) % 2L == 0L;
      String shown = this.previewCaretShown
         ? cur.substring(0, at) + "_" + cur.substring(at) : cur;
      com.paleimitations.schoolsofmagic.common.books.BookTextOverride preview =
         new com.paleimitations.schoolsofmagic.common.books.BookTextOverride(
            title ? shown : this.editBuffer.title,
            title ? this.editBuffer.body : shown,
            this.editBuffer.originalTitle, this.editBuffer.originalBody);

      com.paleimitations.schoolsofmagic.client.BookLayoutRenderer.setEditPreview(
         layoutPiece(com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind.TITLE),
         preview.title,
         layoutPiece(com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind.TEXT),
         preview.body);
      return preview;
   }

   private String editedText(boolean title) {
      if (this.editBuffer == null) return "";
      return title ? this.editBuffer.title : this.editBuffer.body;
   }

   private void setEditedText(boolean title, String value) {
      if (this.editBuffer == null) return;
      if (title) this.editBuffer.title = value; else this.editBuffer.body = value;
   }

   private void typeInto(char c, boolean title) {
      deleteEditSelection(title);
      String cur = editedText(title);
      int at = Math.max(0, Math.min(this.caret < 0 ? cur.length() : this.caret, cur.length()));
      setEditedText(title, cur.substring(0, at) + c + cur.substring(at));
      this.caret = at + 1;
      this.editAnchor = this.caret;
   }

   private void backspaceEdit(boolean title) {
      if (hasSelection()) {
         deleteEditSelection(title);
         return;
      }
      String cur = editedText(title);
      int at = Math.max(0, Math.min(this.caret < 0 ? cur.length() : this.caret, cur.length()));
      if (at == 0 || cur.isEmpty()) return;
      setEditedText(title, cur.substring(0, at - 1) + cur.substring(at));
      this.caret = at - 1;
      this.editAnchor = this.caret;
   }

   private void moveCaret(int by) {
      String cur = editedText(writeState == EnumWriteState.TITLE);
      int at = this.caret < 0 ? cur.length() : this.caret;
      this.caret = Math.max(0, Math.min(at + by, cur.length()));

      if (!net.minecraft.client.gui.screens.Screen.hasShiftDown()) this.editAnchor = this.caret;
   }

   private void beginEdit() {
      IBook book = currentBook();
      if (book == null) return;
      this.editPage = book.getPage();
      com.paleimitations.schoolsofmagic.common.books.BookTextOverride existing =
         book.getTextOverrides().get(this.editPage);
      String shippedT = shippedTitle();
      String shippedB = shippedBody();
      this.editBuffer = existing != null
         ? new com.paleimitations.schoolsofmagic.common.books.BookTextOverride(
              existing.title, existing.body, shippedT, shippedB)
         : new com.paleimitations.schoolsofmagic.common.books.BookTextOverride(
              shippedT, shippedB, shippedT, shippedB);
      this.caret = writeState == EnumWriteState.TITLE
         ? this.editBuffer.title.length() : this.editBuffer.body.length();
      this.editAnchor = this.caret;
   }

   private void sendEdit(boolean clear) {
      TileEntityPodium podium = getPodium();
      if (podium == null || this.editBuffer == null || this.editPage < 0) return;
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout layout = currentLayout();
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element laidTitle =
         layoutPiece(com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind.TITLE);
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Element laidText =
         layoutPiece(com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout.Kind.TEXT);
      BookPageWriteable writeable = currentWriteablePage();

      if (layout != null && (laidTitle != null || laidText != null)) {
         if (!clear) {
            if (laidTitle != null) laidTitle.value = this.editBuffer.title;
            if (laidText != null) laidText.value = this.editBuffer.body;
         }
         PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetPageLayout(
               podium.getBlockPos(), this.editPage, layout.save()));
      } else if (writeable != null) {
         if (!clear) applyToWriteable(writeable);
         commitWrittenPage();
      } else {
         PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetPageOverride(
               podium.getBlockPos(), this.editPage, clear,
               this.editBuffer.title, this.editBuffer.body,
               this.editBuffer.originalTitle, this.editBuffer.originalBody));
      }
      this.editBuffer = null;
      this.editPage = -1;
      this.caret = -1;
      this.editAnchor = -1;
      com.paleimitations.schoolsofmagic.client.BookLayoutRenderer.clearEditPreview();
   }

   private void applyToWriteable(BookPageWriteable page) {
      for (PageElement e : page.elements) {
         if (e instanceof PageElementTitle t && t.text != null && t.text.length > 0) {
            t.text[0] = this.editBuffer.title;
         } else if (e instanceof com.paleimitations.schoolsofmagic.common.books.PageElementWriteableParagraphs p) {
            p.text = this.editBuffer.body;
         }
      }
   }

   private boolean clickedQuill(double mx, double my) {
      IBook book = currentBook();
      BookPage page = book == null ? null : book.getCurrentPage();
      if (page == null) return false;
      TileEntityPodium podium = getPodium();
      if (podium == null) return false;
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout layout =
         book.getPageLayouts().get(book.getPage());
      if (layout != null && layout.finished) return false;
      for (PageElement e : page.elements) {
         if (e instanceof com.paleimitations.schoolsofmagic.common.books.PageElementQuill quill
               && com.paleimitations.schoolsofmagic.common.books.PageElementQuill.isOverScreen(mx, my)) {
            quill.press();
            this.minecraft.setScreen(new com.paleimitations.schoolsofmagic.client.guis.editor.GuiBookEditor(
               this, podium.getBlockPos(), book.getPage(), layout));
            return true;
         }
      }
      return false;
   }

   private BookPageWriteable currentWriteablePage() {
      IBook book = currentBook();
      if (book == null) return null;
      BookPage page = book.getCurrentPage();
      return page instanceof BookPageWriteable w ? w : null;
   }

   private PageElementTitle currentTitleElement(boolean bookRoot) {
      IBook book = currentBook();
      if (book == null) return null;
      BookPage page;
      if (bookRoot) {
         if (book.getBookPages() == null || book.getBookPages().isEmpty()) return null;
         page = book.getBookPages().get(0);
      } else {
         page = book.getCurrentPage();
      }
      if (page == null || page.elements == null) return null;
      for (PageElement el : page.elements) {
         if (el instanceof PageElementTitle t) return t;
      }
      return null;
   }

   private void appendTitleChar(PageElementTitle t, char c) {
      if (t == null || t.text == null || t.text.length == 0) return;
      String cur = t.text[0] == null ? "" : t.text[0];
      if (cur.length() < 32 && net.minecraft.SharedConstants.isAllowedChatCharacter(c)) {
         t.text[0] = cur + c;
      }
   }

   private void backspaceTitle(PageElementTitle t) {
      if (t == null || t.text == null || t.text.length == 0) return;
      String cur = t.text[0];
      if (cur == null || cur.isEmpty()) return;
      t.text[0] = cur.substring(0, cur.length() - 1);
   }

   @Override
   public boolean charTyped(char c, int modifiers) {
      if (state == EnumPersonalizeState.WRITE && writeState != EnumWriteState.NONE) {
         switch (writeState) {
            case PAGE:
               typeInto(c, false); return true;
            case TITLE:
               typeInto(c, true); return true;
            case BOOK:
               if (this.bookNameBox != null && this.bookNameBox.isVisible()) {
                  return this.bookNameBox.charTyped(c, modifiers);
               }
               return true;
            default:
         }
      }
      return super.charTyped(c, modifiers);
   }

   @Override
   public boolean keyPressed(int key, int scan, int mods) {
      if (this.bookNameBox != null && this.bookNameBox.isVisible()) {
         if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            commitBookName();
            return true;
         }
         if (key == GLFW.GLFW_KEY_ESCAPE) {
            writeState = EnumWriteState.NONE;
            return true;
         }
         if (this.bookNameBox.keyPressed(key, scan, mods)) {
            return true;
         }

         return true;
      }
      if (state == EnumPersonalizeState.WRITE && writeState != EnumWriteState.NONE) {
         int legacy = switch (key) {
            case GLFW.GLFW_KEY_BACKSPACE -> 14;
            case GLFW.GLFW_KEY_ENTER     -> 28;
            case GLFW.GLFW_KEY_KP_ENTER  -> 156;
            default -> -1;
         };

         boolean ctrlV = (key == GLFW.GLFW_KEY_V) && ((mods & GLFW.GLFW_MOD_CONTROL) != 0);
         switch (writeState) {
            case PAGE: {
               if (key == GLFW.GLFW_KEY_ESCAPE) { writeState = EnumWriteState.NONE; sendEdit(false); return true; }
               if (key == GLFW.GLFW_KEY_LEFT)  { moveCaret(-1); return true; }
               if (key == GLFW.GLFW_KEY_RIGHT) { moveCaret(1); return true; }
               if (key == GLFW.GLFW_KEY_HOME)  { this.caret = 0; return true; }
               if (key == GLFW.GLFW_KEY_END)   { this.caret = editedText(false).length(); return true; }
               if (key == GLFW.GLFW_KEY_BACKSPACE) { backspaceEdit(false); return true; }
               if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) { typeInto('\n', false); return true; }
               if (ctrlV) {
                  for (char ch : net.minecraft.client.Minecraft.getInstance()
                        .keyboardHandler.getClipboard().toCharArray()) {
                     typeInto(ch, false);
                  }
                  return true;
               }

               return true;
            }
            case TITLE:
               if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
                  writeState = EnumWriteState.NONE;
                  sendEdit(false);
                  return true;
               }
               if (key == GLFW.GLFW_KEY_LEFT)  { moveCaret(-1); return true; }
               if (key == GLFW.GLFW_KEY_RIGHT) { moveCaret(1); return true; }
               if (key == GLFW.GLFW_KEY_HOME)  { this.caret = 0; return true; }
               if (key == GLFW.GLFW_KEY_END)   { this.caret = editedText(true).length(); return true; }
               if (key == GLFW.GLFW_KEY_BACKSPACE) { backspaceEdit(true); return true; }
               return true;
            case BOOK:
               if (key == GLFW.GLFW_KEY_BACKSPACE) { backspaceTitle(currentTitleElement(true)); return true; }
               break;
            default:
         }
      }
      return super.keyPressed(key, scan, mods);
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      if (this.bookNameBox != null) {
         boolean naming = state == EnumPersonalizeState.WRITE && writeState == EnumWriteState.BOOK;
         if (naming != this.bookNameBox.isVisible()) {
            this.bookNameBox.setVisible(naming);
            this.bookNameBox.setFocused(naming);
            if (naming) {
               this.setFocused(this.bookNameBox);
            }
         }
      }

      com.paleimitations.schoolsofmagic.common.books.BookTextOverride.setPreview(livePreview());
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      com.paleimitations.schoolsofmagic.common.books.BookTextOverride.setPreview(null);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   private static final String CARET = "_";
   private PageElementTitle caretTitle;
   private com.paleimitations.schoolsofmagic.common.books.PageElementWriteableParagraphs caretBody;

   private boolean showCaret() {
      if (state != EnumPersonalizeState.WRITE) return false;
      if (writeState != EnumWriteState.TITLE && writeState != EnumWriteState.PAGE) return false;
      return (System.currentTimeMillis() / 500L) % 2L == 0L;
   }

   private void insertCaret() {
      if (writeState == EnumWriteState.TITLE) {
         PageElementTitle title = currentTitleElement(false);
         if (title == null) return;
         if (title.text == null || title.text.length == 0 || title.text[0] == null) return;
         this.caretTitle = title;
         title.text[0] = title.text[0] + CARET;
         return;
      }
      BookPageWriteable page = currentWriteablePage();
      if (page == null) return;
      for (PageElement element : page.elements) {
         if (element instanceof com.paleimitations.schoolsofmagic.common.books.PageElementWriteableParagraphs body) {
            this.caretBody = body;
            body.text = body.text + CARET;
            return;
         }
      }
   }

   private void removeCaret() {
      if (this.caretTitle != null) {
         String cur = this.caretTitle.text[0];
         if (cur != null && cur.endsWith(CARET)) {
            this.caretTitle.text[0] = cur.substring(0, cur.length() - 1);
         }
         this.caretTitle = null;
      }
      if (this.caretBody != null) {
         if (this.caretBody.text.endsWith(CARET)) {
            this.caretBody.text = this.caretBody.text.substring(0, this.caretBody.text.length() - 1);
         }
         this.caretBody = null;
      }
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(getTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

      gg.blit(FINAL, this.leftPos + 140, this.topPos + 12, 0, 0, 96, 103);
      if (this.state == EnumPersonalizeState.STICKER) {
         gg.blit(FINAL, this.leftPos + 162, this.topPos + 59, 0, 107, 52, 52);
      }
      if (this.state == EnumPersonalizeState.INSERT || this.state == EnumPersonalizeState.WRITE) {
         gg.blit(FINAL, this.leftPos + 160, this.topPos + 59, 166, 0, 71, 51);
      }
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;

      gg.pose().pushPose();
      gg.pose().translate(17.886177F, 10.642276F, 0.0F);

      boolean capture = editingText();
      if (capture) com.paleimitations.schoolsofmagic.client.BookRichText.beginCapture();
      try {
         PodiumGuiHelper.renderGuiSubject(gg, mouseX - this.leftPos - 17.886177F, mouseY - this.topPos - 10.642276F,
            this, podium.handler.getStackInSlot(0), 0.0F, podium, false);
      } finally {
         if (capture) com.paleimitations.schoolsofmagic.client.BookRichText.endCapture();
      }
      gg.pose().popPose();

      if (capture) drawEditSelection(gg);

      if (this.state == EnumPersonalizeState.STICKER) {
         ItemStack consumable = podium.handler.getStackInSlot(1);
         IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (consumable.getItem() == ItemRegistry.sticker.get() && consumable.hasTag()
               && consumable.getTag().contains("sticker")) {
            BookElementSticker.EnumSticker esticker =
               BookElementSticker.EnumSticker.getSticker(consumable.getTag().getString("sticker"));
            if (esticker != null) {
               if (book != null && book.getCurrentPage() != null) {
                  float scale = 0.50406504F;
                  gg.pose().pushPose();
                  gg.pose().translate(17.886177F, 10.642276F, 0.0F);
                  gg.pose().scale(scale, scale, scale);
                  gg.pose().translate(-20.0F, -23.0F, 0.0F);
                  gg.pose().pushPose();
                  gg.pose().translate(this.stickerX + 28.0F, this.stickerY + 59.0F, 0.0F);
                  gg.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees(this.stickerR));
                  gg.blit(esticker.location, -12, -12, esticker.index % 10 * 24, esticker.index / 10 * 24, 24, 24);
                  gg.pose().popPose();
                  gg.pose().popPose();
               }

               gg.blit(esticker.location, 176, 73, esticker.index % 10 * 24, esticker.index / 10 * 24, 24, 24);
            }
         }
      }

      if (this.state == EnumPersonalizeState.INSERT) {
         drawFitCentered(gg, "gui.insert_table_content.name", 195.0F, 67.0F);
         drawFitCentered(gg, "gui.insert_chapter.name",       195.0F, 85.0F);
         drawFitCentered(gg, "gui.insert_page.name",          195.0F, 103.0F);
      } else if (this.state == EnumPersonalizeState.WRITE) {
         drawFitCentered(gg, "gui.edit_title.name", 195.0F, 67.0F);
         drawFitCentered(gg, "gui.edit_page.name",  195.0F, 85.0F);
         drawFitCentered(gg, "gui.edit_book.name",  195.0F, 103.0F);
      }

      if (this.bookNameBox != null && this.bookNameBox.isVisible()) {
         gg.pose().pushPose();
         gg.pose().translate(-this.leftPos, -this.topPos, 300.0F);
         this.bookNameBox.render(gg, mouseX, mouseY, 0.0F);
         gg.pose().popPose();
      }
   }

   private void drawFitCentered(GuiGraphics gg, String key, float cx, float cy) {
      String s = net.minecraft.client.resources.language.I18n.get(key);
      int tw = this.font.width(s);
      int th = this.font.lineHeight;
      float scaler = Math.min(67.0F / (float) tw, 11.0F / (float) th);
      float dx = cx - (float) tw * scaler / 2.0F;
      float dy = cy - (float) th * scaler / 2.0F;
      gg.pose().pushPose();
      gg.pose().scale(scaler, scaler, scaler);
      gg.drawString(this.font, s, Math.round(dx / scaler), Math.round(dy / scaler), 0, false);
      gg.pose().popPose();
   }

   public enum EnumPersonalizeState { STICKER, WRITE, INSERT }
   public enum EnumWriteState { NONE, PAGE, TITLE, BOOK }

   @OnlyIn(Dist.CLIENT)
   private class SwitchStateButton extends AbstractButton {
      private final EnumPersonalizeState target;
      SwitchStateButton(EnumPersonalizeState target, int x, int y) {
         super(x, y, 23, 19, Component.empty());
         this.target = target;
      }
      @Override public void onPress() { state = target; }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean active = (state == target);
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         gg.blit(FINAL, getX(), getY(), active ? 143 : (hov ? 120 : 97), 0, 23, 19);
         int iconU = (target == EnumPersonalizeState.WRITE) ? 143
                   : (target == EnumPersonalizeState.INSERT ? 120 : 97);
         gg.blit(FINAL, getX(), getY(), iconU, 19, 23, 19);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private class MoveStickerButton extends AbstractButton {
      enum Kind { UP, DOWN, LEFT, RIGHT, CW, CCW, CONFIRM }
      private final Kind kind;
      MoveStickerButton(Kind kind, int x, int y) {
         super(x, y, 12, 12, Component.empty());
         this.kind = kind;
      }
      private boolean isStickerMode() { return state == EnumPersonalizeState.STICKER; }

      private boolean isArrow() { return kind != Kind.CONFIRM; }
      private int holdFrames = -1;

      @Override public void render(GuiGraphics gg, int mx, int my, float pt) {
         this.visible = isStickerMode();

         if (holdFrames >= 0) {
            long win = net.minecraft.client.Minecraft.getInstance().getWindow().getWindow();
            boolean down = GLFW.glfwGetMouseButton(win, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
            if (!down || !isStickerMode()) {
               holdFrames = -1;
            } else {
               holdFrames++;
               if (holdFrames > 5) {
                  int speed = Math.min(1 + (holdFrames - 5) / 3, 8);
                  for (int i = 0; i < speed; i++) doStep();
               }
            }
         }
         super.render(gg, mx, my, pt);
      }

      @Override public boolean mouseClicked(double mx, double my, int button) {
         boolean handled = super.mouseClicked(mx, my, button);
         if (handled && button == 0 && isArrow() && isStickerMode()) holdFrames = 0;
         return handled;
      }

      private void doStep() {
         switch (kind) {
            case UP    -> { if (stickerY > 0)   stickerY--; }
            case DOWN  -> { if (stickerY < 135) stickerY++; }
            case LEFT  -> { if (stickerX > 0)   stickerX--; }
            case RIGHT -> { if (stickerX < 200) stickerX++; }
            case CW    -> stickerR = (stickerR + 1.0F) % 360.0F;
            case CCW   -> stickerR = (stickerR - 1.0F + 360.0F) % 360.0F;
            default -> {}
         }
      }

      @Override public void onPress() {
         if (state != EnumPersonalizeState.STICKER) return;
         TileEntityPodium podium = getPodium();
         if (podium == null) return;
         switch (kind) {
            case UP, DOWN, LEFT, RIGHT -> doStep();
            case CW    -> stickerR = (stickerR + 5.0F) % 360.0F;
            case CCW   -> stickerR = (stickerR - 5.0F + 360.0F) % 360.0F;
            case CONFIRM -> confirmSticker(podium);
         }
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         int v = switch (kind) {
            case UP -> 42; case LEFT -> 54; case DOWN -> 66; case RIGHT -> 78;
            case CW -> 90; case CCW -> 102; case CONFIRM -> 114;
         };
         TileEntityPodium p = getPodium();
         boolean hasSticker = p != null
            && p.handler.getStackInSlot(1).getItem() == ItemRegistry.sticker.get();
         boolean atBoundary = (kind == Kind.UP && stickerY == 0.0F)
            || (kind == Kind.DOWN && stickerY == 135.0F)
            || (kind == Kind.LEFT && stickerX == 0.0F)
            || (kind == Kind.RIGHT && stickerX == 200.0F);
         boolean usable = hasSticker && !atBoundary;
         int u = usable ? (hov ? 109 : 97) : 121;
         gg.blit(FINAL, getX(), getY(), u, v, 12, 12);
      }
   }

   private void confirmSticker(TileEntityPodium podium) {
      ItemStack consumable = podium.handler.getStackInSlot(1);
      if (consumable.getItem() != ItemRegistry.sticker.get() || !consumable.hasTag()) return;
      if (!consumable.getTag().contains("sticker")) return;
      String stickerKey = consumable.getTag().getString("sticker");
      BookElementSticker.EnumSticker stickerEnum = BookElementSticker.EnumSticker.getSticker(stickerKey);
      if (stickerEnum == null) return;
      IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book == null) return;
      PacketHandler.INSTANCE.sendToServer(new PacketInsertSticker(
         book.getPage(), book.getSubPage(),
         stickerX + 28.0F, stickerY + 59.0F, stickerR,
         stickerKey, podium.getBlockPos()));

      book.getStickers().add(new BookElementSticker(
         stickerEnum, stickerR % 360.0F,
         stickerX + 28.0F, stickerY + 59.0F,
         book.getPage(), book.getSubPage()));
      stickerX = 100.0F; stickerY = 67.0F; stickerR = 0.0F;
   }

   private boolean hasPaper() {
      TileEntityPodium p = getPodium();
      return p != null && p.handler.getStackInSlot(1).is(net.minecraft.world.item.Items.PAPER);
   }

   private boolean hasInk() {
      TileEntityPodium p = getPodium();
      if (p == null) return false;
      ItemStack stack = p.handler.getStackInSlot(1);
      return stack.is(net.minecraft.world.item.Items.INK_SAC)
         || stack.is(net.minecraft.world.item.Items.BLACK_DYE);
   }

   @OnlyIn(Dist.CLIENT)
   static class TurnPageButton extends AbstractButton {
      private final boolean isBack;
      private final TileEntityPodium podium;
      TurnPageButton(TileEntityPodium podium, boolean isBack, int posX, int posY) {
         super(posX, posY, 29, 19, Component.empty());
         this.podium = podium; this.isBack = isBack;
      }
      @Override public void onPress() { this.podium.turnPage(!isBack); }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         gg.blit(GuiPodiumRead.ICONS, getX(), getY(), hov ? 29 : 0, isBack ? 23 : 42, 29, 19);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private class InsertButton extends AbstractButton {
      enum Kind { TABLE_OF_CONTENTS, CHAPTER, PAGE }
      private final Kind kind;
      InsertButton(Kind kind, int x, int y) {
         super(x, y, 12, 12, Component.empty());
         this.kind = kind;
      }
      private boolean isInsertMode() { return state == EnumPersonalizeState.INSERT; }
      @Override public void render(GuiGraphics gg, int mx, int my, float pt) {
         this.visible = isInsertMode();
         super.render(gg, mx, my, pt);
      }
      @Override public void onPress() {
         if (!isInsertMode() || !hasPaper()) return;
         TileEntityPodium podium = getPodium();
         if (podium == null) return;

         String pageKey = switch (kind) {
            case TABLE_OF_CONTENTS -> "table_content";
            case CHAPTER -> "chapter";
            case PAGE -> "writeable";
         };

         PacketHandler.INSTANCE.sendToServer(new com.paleimitations.schoolsofmagic.common.network.PacketInsertPage(
            podium.getPage(), pageKey, podium.getBlockPos(), false, 1));
         net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
            net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
               net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         boolean usable = hasPaper();
         gg.blit(FINAL, getX(), getY(), usable ? (hov ? 109 : 97) : 121, 78, 12, 12);
      }
   }

   @OnlyIn(Dist.CLIENT)
   private class EditButton extends AbstractButton {
      enum Kind { TITLE, PAGE, BOOK }
      private final Kind kind;
      EditButton(Kind kind, int x, int y) {
         super(x, y, 12, 12, Component.empty());
         this.kind = kind;
      }
      private boolean isWriteMode() { return state == EnumPersonalizeState.WRITE; }
      @Override public void render(GuiGraphics gg, int mx, int my, float pt) {
         this.visible = isWriteMode();
         super.render(gg, mx, my, pt);
      }

      private boolean usable() {
         return hasInk();
      }

      @Override public boolean mouseClicked(double mx, double my, int button) {
         if (button == 1 && isWriteMode() && usable() && this.isMouseOver(mx, my)) {
            TileEntityPodium podium = getPodium();
            IBook book = currentBook();
            if (podium != null && book != null) {
               if (kind == Kind.BOOK) {
                  PacketHandler.INSTANCE.sendToServer(
                     new com.paleimitations.schoolsofmagic.common.network.PacketRenameBook(
                        podium.getBlockPos(), ""));
               } else {
                  PacketHandler.INSTANCE.sendToServer(
                     new com.paleimitations.schoolsofmagic.common.network.PacketSetPageOverride(
                        podium.getBlockPos(), book.getPage(), true, "", "", "", ""));
               }
               writeState = EnumWriteState.NONE;
               editBuffer = null;
               editPage = -1;
               caret = -1;
               net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
                  net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                     net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            return true;
         }
         return super.mouseClicked(mx, my, button);
      }

      @Override public void onPress() {
         if (!isWriteMode()) return;

         EnumWriteState target = switch (kind) {
            case PAGE -> EnumWriteState.PAGE;
            case TITLE -> EnumWriteState.TITLE;
            case BOOK -> EnumWriteState.BOOK;
         };

         boolean closing = (writeState == target);
         if (!closing && !usable()) return;
         writeState = closing ? EnumWriteState.NONE : target;
         if (target != EnumWriteState.BOOK) {
            if (closing) {
               sendEdit(false);
            } else {
               beginEdit();
            }
         }
         net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
            net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
               net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         boolean on = usable();

         boolean active = (kind == Kind.PAGE && writeState == EnumWriteState.PAGE)
            || (kind == Kind.TITLE && writeState == EnumWriteState.TITLE)
            || (kind == Kind.BOOK && writeState == EnumWriteState.BOOK);
         gg.blit(FINAL, getX(), getY(), on ? (hov || active ? 109 : 97) : 121, 78, 12, 12);
      }
   }
}
