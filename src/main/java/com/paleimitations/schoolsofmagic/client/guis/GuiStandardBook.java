package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.books.BookElementSticker;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementPageButton;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lwjgl.glfw.GLFW;

public class GuiStandardBook extends Screen {
   private static final ResourceLocation MENU_OPTIONS = new ResourceLocation("som", "textures/gui/books/menu_options.png");
   public static final ResourceLocation PAGE_DEFAULT = new ResourceLocation("som", "textures/gui/books/paper_default.png");

   private final Player player;
   private final ItemStack bookStack;
   private final net.minecraft.core.BlockPos lecternPos;
   private MenuButton buttonNextPage;
   private MenuButton buttonPreviousPage;
   private MenuButton nextChapter;
   private MenuButton backChapter;
   private MenuButton indexReturn;
   private MenuButton closeButton;

   public GuiStandardBook(Player playerIn) {
      this(playerIn, ItemStack.EMPTY, null);
   }

   public GuiStandardBook(Player playerIn, ItemStack stackIn) {
      this(playerIn, stackIn, null);
   }

   public GuiStandardBook(Player playerIn, ItemStack stackIn, net.minecraft.core.BlockPos lecternPos) {
      super(Component.empty());
      this.player = playerIn;
      this.bookStack = stackIn == null ? ItemStack.EMPTY : stackIn;
      this.lecternPos = lecternPos;
   }

   private ItemStack heldBook() {
      if (!this.bookStack.isEmpty()) return this.bookStack;
      ItemStack main = this.player.getMainHandItem();
      return main.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent() ? main : this.player.getOffhandItem();
   }

   private IBook getBook() {
      ItemStack held = heldBook();
      com.paleimitations.schoolsofmagic.common.items.ItemBookBase.ensureCosmetics(held);
      return held.getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
   }

   @Override
   protected void init() {
      super.init();
      int offsetLeft = (this.width - 256) / 2;
      int top = 16;

      this.buttonPreviousPage = new MenuButton(offsetLeft + 70, top, 70, 19, this::onPrev);
      this.backChapter        = new MenuButton(offsetLeft + 93, top, 93, 14, this::onBackChapter);
      this.indexReturn        = new MenuButton(offsetLeft + 112, top, 112, 14, this::onIndex);
      this.closeButton        = new MenuButton(offsetLeft + 130, top, 130, 14, this::onCloseBook);
      this.nextChapter        = new MenuButton(offsetLeft + 149, top, 149, 14, this::onNextChapter);
      this.buttonNextPage     = new MenuButton(offsetLeft + 167, top, 167, 19, this::onNext);

      this.addRenderableWidget(this.buttonPreviousPage);
      this.addRenderableWidget(this.backChapter);
      this.addRenderableWidget(this.indexReturn);
      this.addRenderableWidget(this.closeButton);
      this.addRenderableWidget(this.nextChapter);
      this.addRenderableWidget(this.buttonNextPage);

      if (this.lecternPos != null) {
         int cx = offsetLeft + 128;
         this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
            Component.translatable("lectern.take_book"),
            b -> {
               com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketTakeLecternBook(this.lecternPos));
               this.onClose();
            }).bounds(cx - 80, 210, 78, 20).build());
         this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
            net.minecraft.network.chat.CommonComponents.GUI_DONE,
            b -> this.onClose()).bounds(cx + 2, 210, 78, 20).build());
      }
   }

   @Override public boolean isPauseScreen() { return false; }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == GLFW.GLFW_KEY_ESCAPE
            || Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {

         Minecraft.getInstance().setScreen(null);
         return true;
      }
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public void handleDelayedNarration() {
      if (this.minecraft == null) return;
      super.handleDelayedNarration();
   }

   private int lastSyncedPage = -1;
   private int lastSyncedSub = -1;

   @Override
   public void tick() {
      this.buttonNextPage.visible = true;
      this.buttonPreviousPage.visible = true;
      this.backChapter.visible = true;
      this.nextChapter.visible = true;
      this.indexReturn.visible = true;
      this.syncPage();
   }

   private void syncPage() {
      IBook book = getBook();
      if (book == null) return;
      if (!(heldBook().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook)) return;
      if (book.getPage() != this.lastSyncedPage || book.getSubPage() != this.lastSyncedSub) {
         this.lastSyncedPage = book.getPage();
         this.lastSyncedSub = book.getSubPage();
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetBookPage(this.lastSyncedPage, this.lastSyncedSub));
      }
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      IBook book = getBook();
      if (book == null) return;
      ItemStack heldBook = heldBook();
      int offsetLeft = (this.width - 256) / 2;
      int offsetTop = 0;
      com.mojang.blaze3d.systems.RenderSystem.enableBlend();
      com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
      com.mojang.blaze3d.systems.RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      gg.blit(MENU_OPTIONS, offsetLeft, offsetTop, 0, 0, 256, 256);
      gg.blit(com.paleimitations.schoolsofmagic.common.items.capabilities.book.Book.coverFor(heldBook, book), offsetLeft, offsetTop, 0, 0, 256, 256);
      gg.blit(com.paleimitations.schoolsofmagic.common.items.capabilities.book.Book.linkLocationFor(heldBook, book), offsetLeft, offsetTop, 0, 0, 256, 256);
      gg.blit(PAGE_DEFAULT, offsetLeft, offsetTop, 0, 0, 256, 256);
      com.paleimitations.schoolsofmagic.client.GrimoireScramble.SCRAMBLE =
         heldBook.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.BookDecorations.hasSwirl(heldBook)
            && !com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isOwner(heldBook, this.player);
      if (!book.getBookPages().isEmpty() && book.getCurrentPage() != null) {
         book.getCurrentPage().drawPage(gg, mouseX - offsetLeft, mouseY - offsetTop, offsetLeft, offsetTop, true, book.getSubPage());
      }
      com.paleimitations.schoolsofmagic.client.GrimoireScramble.SCRAMBLE = false;
      for (BookElementSticker sticker : book.getStickers()) {
         if (sticker != null) {
            sticker.drawElement(gg, mouseX - offsetLeft, mouseY - offsetTop, offsetLeft, offsetTop, true, book.getSubPage(), book.getPage());
         }
      }
      super.render(gg, mouseX, mouseY, partialTicks);
   }

   @Override
   public void removed() {
      super.removed();
      ItemStack held = heldBook();
      IBook book = getBook();
      if (held.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook && book != null) {
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetBookPage(book.getPage(), book.getSubPage()));
      }
      if (held.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && book != null && book.getCurrentPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageSpell bps
            && bps.getSpell() != null) {
         com.paleimitations.schoolsofmagic.common.spells.Spell sp = bps.getSpell();
         com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.setSelectedSpell(held, sp);
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketSetGrimoireSpell(
               sp.getResourceLocation().toString(), sp.serializeNBT()));
      }
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      int offsetLeft = (this.width - 256) / 2;
      IBook book = getBook();

      if (book != null && book.getCurrentPage() != null) {
         for (PageElement element : book.getCurrentPage().elements) {
            if (element instanceof PageElementPageButton b) {
               b.click((float) (mouseX - offsetLeft), (float) mouseY, book.getSubPage(), book, null);
            }
         }
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   private void playTurn() {
      net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
         SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }

   private void onNext() {
      IBook book = getBook(); if (book == null || book.getCurrentPage() == null) return;
      boolean advanced = false;
      for (int i = book.getSubPage() + 1; i < book.getCurrentPage().getSubPages(); i++) {
         if (!book.getCurrentPage().isSubPageBlank(i)) { advanced = true; book.setSubPage(i); break; }
      }
      if (!advanced && book.getBookPages().size() > book.getPage() + 1) {
         book.setPage(book.getPage() + 1);
         book.setSubPage(0);
      }
   }
   private void onPrev() {
      IBook book = getBook(); if (book == null) return;
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
   }

   private void onBackChapter() {
      IBook book = getBook(); if (book == null) return;
      List<BookPage> pages = book.getBookPages();
      for (int i = book.getPage() - 1; i >= 0; i--) {
         if (pages.get(i) instanceof BookPageChapter) {
            book.setPage(i); book.setSubPage(0); playTurn(); return;
         }
      }
   }
   private void onNextChapter() {
      IBook book = getBook(); if (book == null) return;
      List<BookPage> pages = book.getBookPages();
      for (int i = book.getPage() + 1; i < pages.size(); i++) {
         if (pages.get(i) instanceof BookPageChapter) {
            book.setPage(i); book.setSubPage(0); playTurn(); return;
         }
      }
   }
   private void onIndex() {
      IBook book = getBook(); if (book == null) return;
      List<BookPage> pages = book.getBookPages();
      for (int i = 0; i < pages.size(); i++) {
         if (pages.get(i) instanceof BookPageTableContent) {
            book.setPage(i); book.setSubPage(0); playTurn(); return;
         }
      }
      book.setPage(0); book.setSubPage(0); playTurn();
   }
   private void onCloseBook() {
      this.onClose();
   }

   @OnlyIn(Dist.CLIENT)
   class MenuButton extends AbstractButton {
      private final int uBase;
      private final int w;
      private final Runnable onPress;

      public MenuButton(int posX, int posY, int uBase, int width, Runnable onPress) {
         super(posX, posY, width, 14, Component.empty());
         this.uBase = uBase;
         this.w = width;
         this.onPress = onPress;
      }

      @Override public void onPress() { onPress.run(); }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.w && mouseY < this.getY() + 14;
         int v = hovered ? 41 : 55;
         gg.blit(MENU_OPTIONS, this.getX(), this.getY(), this.uBase, v, this.w, 14);
      }
   }
}
