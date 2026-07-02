package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.BookFrameHandler;
import com.paleimitations.schoolsofmagic.common.containers.ContainerBookFrame;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.Book;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementPageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GuiBookFrame extends AbstractContainerScreen<ContainerBookFrame> {

   private static final ResourceLocation MENU_OPTIONS = new ResourceLocation("som", "textures/gui/books/menu_options.png");
   private static final ResourceLocation PAGE_DEFAULT = new ResourceLocation("som", "textures/gui/books/paper_default.png");
   private static final ResourceLocation SLOTS = new ResourceLocation("som", "textures/gui/book_gui_slots.png");

   public GuiBookFrame(ContainerBookFrame menu, Inventory inv, Component title) {
      super(menu, inv, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private ItemStack book() {
      return Minecraft.getInstance().player.getMainHandItem();
   }

   private IBook bookCap() {
      return CapabilityBook.getCapability(book());
   }

   @Override
   protected void init() {
      super.init();
      this.titleLabelX = 100000;
      this.inventoryLabelX = 100000;
      int ox = this.leftPos;
      int top = this.topPos + 16;
      this.addRenderableWidget(new MenuButton(ox + 70, top, 70, 19, this::onPrev));
      this.addRenderableWidget(new MenuButton(ox + 93, top, 93, 14, this::onBackChapter));
      this.addRenderableWidget(new MenuButton(ox + 112, top, 112, 14, this::onIndex));
      this.addRenderableWidget(new MenuButton(ox + 130, top, 130, 14, this::onClose));
      this.addRenderableWidget(new MenuButton(ox + 149, top, 149, 14, this::onNextChapter));
      this.addRenderableWidget(new MenuButton(ox + 167, top, 167, 19, this::onNext));
   }

   private int lastSyncedPage = -1;
   private int lastSyncedSub = -1;

   @Override
   protected void containerTick() {
      super.containerTick();
      IBook book = bookCap();
      if (book == null) return;
      if (book.getPage() != this.lastSyncedPage || book.getSubPage() != this.lastSyncedSub) {
         this.lastSyncedPage = book.getPage();
         this.lastSyncedSub = book.getSubPage();
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetBookPage(this.lastSyncedPage, this.lastSyncedSub));
      }
   }

   private void playTurn() {
      net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
         net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(net.minecraft.sounds.SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }

   private void onNext() {
      IBook book = bookCap();
      if (book == null || book.getCurrentPage() == null) return;
      boolean advanced = false;
      for (int i = book.getSubPage() + 1; i < book.getCurrentPage().getSubPages(); i++) {
         if (!book.getCurrentPage().isSubPageBlank(i)) { advanced = true; book.setSubPage(i); break; }
      }
      if (!advanced && book.getBookPages().size() > book.getPage() + 1) {
         book.setPage(book.getPage() + 1);
         book.setSubPage(0);
      }
      playTurn();
   }

   private void onPrev() {
      IBook book = bookCap();
      if (book == null) return;
      boolean moved = false;
      if (book.getSubPage() > 0) {
         for (int i = book.getSubPage() - 1; i >= 0; i--) {
            if (!book.getCurrentPage().isSubPageBlank(i)) { moved = true; book.setSubPage(i); break; }
         }
      }
      if (!moved && book.getPage() > 0) {
         book.setPage(book.getPage() - 1);
         int j = 0;
         for (int i = 0; i < book.getCurrentPage().getSubPages(); i++) {
            if (!book.getCurrentPage().isSubPageBlank(i) && i > j) j = i;
         }
         book.setSubPage(j);
      }
      playTurn();
   }

   private void onBackChapter() {
      IBook book = bookCap();
      if (book == null) return;
      for (int i = book.getPage() - 1; i >= 0; i--) {
         if (book.getBookPages().get(i) instanceof com.paleimitations.schoolsofmagic.common.books.BookPageChapter) {
            book.setPage(i); book.setSubPage(0); playTurn(); return;
         }
      }
   }

   private void onNextChapter() {
      IBook book = bookCap();
      if (book == null) return;
      for (int i = book.getPage() + 1; i < book.getBookPages().size(); i++) {
         if (book.getBookPages().get(i) instanceof com.paleimitations.schoolsofmagic.common.books.BookPageChapter) {
            book.setPage(i); book.setSubPage(0); playTurn(); return;
         }
      }
   }

   private void onIndex() {
      IBook book = bookCap();
      if (book == null) return;
      for (int i = 0; i < book.getBookPages().size(); i++) {
         if (book.getBookPages().get(i) instanceof com.paleimitations.schoolsofmagic.common.books.BookPageTableContent) {
            book.setPage(i); book.setSubPage(0); playTurn(); return;
         }
      }
      book.setPage(0); book.setSubPage(0); playTurn();
   }

   private class MenuButton extends net.minecraft.client.gui.components.AbstractButton {
      private final int uBase;
      private final int w;
      private final Runnable onPress;

      MenuButton(int posX, int posY, int uBase, int width, Runnable onPress) {
         super(posX, posY, width, 14, net.minecraft.network.chat.Component.empty());
         this.uBase = uBase;
         this.w = width;
         this.onPress = onPress;
      }

      @Override
      public void onPress() {
         this.onPress.run();
      }

      @Override
      protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput out) {
         this.defaultButtonNarrationText(out);
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.w && mouseY < this.getY() + 14;
         int v = hovered ? 41 : 55;
         gg.blit(MENU_OPTIONS, this.getX(), this.getY(), this.uBase, v, this.w, 14);
      }
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partial, int mx, int my) {
      int x = this.leftPos;
      int y = this.topPos;
      ItemStack bookStack = book();
      IBook book = bookCap();
      gg.blit(SLOTS, x, y, 0, 0, 256, 256);
      gg.blit(MENU_OPTIONS, x, y, 0, 0, 256, 256);
      gg.blit(Book.coverFor(bookStack, book), x, y, 0, 0, 256, 256);
      gg.blit(Book.linkLocationFor(bookStack, book), x, y, 0, 0, 256, 256);
      gg.blit(PAGE_DEFAULT, x, y, 0, 0, 256, 256);
      if (book != null && book.getBookPages() != null && !book.getBookPages().isEmpty() && book.getCurrentPage() != null) {
         book.getCurrentPage().drawPage(gg, mx - x, my - y, x, y, true, book.getSubPage());
         for (BookElementSticker sticker : book.getStickers()) {
            if (sticker != null) {
               sticker.drawElement(gg, mx - x, my - y, x, y, true, book.getSubPage(), book.getPage());
            }
         }
      }
   }

   @Override
   public void render(GuiGraphics gg, int mx, int my, float partial) {
      this.renderBackground(gg);
      super.render(gg, mx, my, partial);
      this.renderTooltip(gg, mx, my);
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      IBook book = bookCap();
      if (book != null && book.getCurrentPage() != null) {
         for (PageElement element : book.getCurrentPage().elements) {
            if (element instanceof PageElementPageButton b) {
               b.click((float) (mouseX - this.leftPos), (float) (mouseY - this.topPos), book.getSubPage(), book, null);
            }
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void removed() {
      super.removed();
      ItemStack held = book();
      IBook b = bookCap();
      if (held.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook && b != null) {
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetBookPage(b.getPage(), b.getSubPage()));
      }
      if (held.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && b != null && b.getCurrentPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell bps
            && bps.getSpell() != null) {
         com.paleimitations.schoolsofmagic.common.spells.Spell sp = bps.getSpell();
         com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.setSelectedSpell(held, sp);
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetGrimoireSpell(
               sp.getResourceLocation().toString(), sp.serializeNBT()));
      }
   }
}
