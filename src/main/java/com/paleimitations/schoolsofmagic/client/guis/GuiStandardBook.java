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
   private static final ResourceLocation BOOK_KNOWLADGE = new ResourceLocation("som", "textures/gui/books/book_knowladge.png");
   private static final ResourceLocation PAGE_TOC = new ResourceLocation("som", "textures/gui/books/paper_default_table_of_contents.png");

   private final Player player;
   private final ItemStack bookStack;
   private final net.minecraft.core.BlockPos lecternPos;
   private BookSearchField search;
   private boolean typing = false;
   private int searchLeft;
   private static final int SEARCH_TOP = 54;
   private static final float SEARCH_SCALE = 0.55F;
   private static final int SEARCH_CLIP = 82;
   private java.util.List<com.paleimitations.schoolsofmagic.client.KnowledgeSearch.Hit> results = new java.util.ArrayList<>();
   private int resultPage = 0;
   private static final int PER_PAGE = 12;
   private static final int RESULT_TOP = 74;
   private static final float RESULT_SCALE = 0.56F;
   private static final int RESULT_WIDTH = 150;
   private static final int RESULT_LEFT_PAD = 30;

   private BookSearchField search() {
      if (this.search == null) this.search = new BookSearchField(this.font);
      return this.search;
   }

   private void runSearch() {
      if (isKnowledgeBook()) {
         net.minecraft.core.BlockPos origin = this.lecternPos != null ? this.lecternPos : this.player.blockPosition();
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
            new com.paleimitations.schoolsofmagic.common.network.PacketKnowledgeRequest(origin));
      } else if (isTocPage()) {
         String q = search().getValue().trim();
         this.results = com.paleimitations.schoolsofmagic.client.KnowledgeSearch.searchBook(getBook(), search().getValue());
         // A plain number also surfaces that page itself as a result.
         if (q.matches("\\d+")) {
            int n = Integer.parseInt(q);
            IBook b = getBook();
            int[] ps = com.paleimitations.schoolsofmagic.client.guis.podium.PodiumGuiHelper.spreadToPageSub(b, (n - 1) / 2);
            if (ps != null && !com.paleimitations.schoolsofmagic.client.KnowledgeSearch.isPageHidden(b.getBookPages().get(ps[0]))) {
               boolean already = false;
               for (com.paleimitations.schoolsofmagic.client.KnowledgeSearch.Hit hh : this.results) {
                  if (hh.pageIndex == ps[0]) { already = true; break; }
               }
               if (!already) {
                  String title = com.paleimitations.schoolsofmagic.client.KnowledgeSearch.pageTitle(b.getBookPages().get(ps[0]));
                  this.results.add(0, new com.paleimitations.schoolsofmagic.client.KnowledgeSearch.Hit(
                     net.minecraft.world.item.ItemStack.EMPTY, title.isEmpty() ? "Page " + n : title, "Page " + n, null, -1, ps[0]));
               }
            }
         }
         this.resultPage = 0;
      }
   }

   public void acceptCandidates(java.util.List<com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather.Found> found) {
      this.results = com.paleimitations.schoolsofmagic.client.KnowledgeSearch.matchAll(found, search().getValue());
      this.resultPage = 0;
   }

   private int resultPages() {
      return com.paleimitations.schoolsofmagic.client.KnowledgeResultsView.pageCount(this.results.size(), PER_PAGE);
   }
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

      this.searchLeft = offsetLeft + 42;

      if (this.lecternPos != null) {
         int cx = offsetLeft + 128;
         this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
            Component.translatable("lectern.take_book"),
            b -> {
               com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketTakeLecternBook(this.lecternPos));
               this.onClose();
            }).bounds(cx - 80, 222, 78, 20).build());
         this.addRenderableWidget(net.minecraft.client.gui.components.Button.builder(
            net.minecraft.network.chat.CommonComponents.GUI_DONE,
            b -> this.onClose()).bounds(cx + 2, 222, 78, 20).build());
      }
   }

   @Override public boolean isPauseScreen() { return false; }

   private boolean isKnowledgeBook() {
      return heldBook().getItem() == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.book_of_knowledge.get();
   }

   private boolean isTocPage() {
      IBook b = getBook();
      return b != null && b.getCurrentPage() instanceof BookPageTableContent;
   }

   private void drawPageNum(GuiGraphics gg, int cx, String s) {
      gg.pose().pushPose();
      gg.pose().translate(cx, 198, 0.0F);
      gg.pose().scale(0.8F, 0.8F, 1.0F);
      gg.drawString(this.font, s, -this.font.width(s) / 2, 0, 0x3A2E1E, false);
      gg.pose().popPose();
   }

   // Jump the book to the spread that holds a given page number (each half of a
   // spread is one page).
   private boolean jumpToPageNumber(int number) {
      if (number < 1) return false;
      IBook b = getBook();
      int[] ps = com.paleimitations.schoolsofmagic.client.guis.podium.PodiumGuiHelper.spreadToPageSub(b, (number - 1) / 2);
      if (ps == null) return false;
      b.setPage(ps[0]);
      b.setSubPage(ps[1]);
      return true;
   }

   // The Book of Knowledge searches nearby shelves; a table-of-contents page searches
   // the current book's own pages. The search bar and results share this geometry.
   private boolean searchUiActive() { return isKnowledgeBook() || isTocPage(); }
   private int offX() { return (this.width - 256) / 2; }
   private float barX() { return isKnowledgeBook() ? offX() + 42 : offX() + 44; }
   private float barY() { return isKnowledgeBook() ? 53 : 188; }
   private float barScale() { return 0.55F; }
   private int barClip() { return isKnowledgeBook() ? 82 : 78; }
   private float resX() { return isKnowledgeBook() ? offX() + 30 : offX() + 24; }
   private float resY() { return isKnowledgeBook() ? 74 : 65; }
   private float resScale() { return isKnowledgeBook() ? 0.56F : 0.7F; }
   private int resWidth() { return 99; }
   private float resColGap() { return Math.round(108 / resScale()); }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      // While typing in the search bar, keep keystrokes inside the field (so letters
      // like the inventory key do not close the book) and let Enter exit writing mode.
      if (this.typing) {
         if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.typing = false;
            runSearch();
            return true;
         }
         if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.typing = false;
            return true;
         }
         search().keyPressed(keyCode);
         return true;
      }
      if (keyCode == GLFW.GLFW_KEY_ESCAPE
            || Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {

         Minecraft.getInstance().setScreen(null);
         return true;
      }
      // Hold space to flip forward quickly (shift+space flips back); key-repeat drives
      // the rapid page turning while the key is held.
      if (keyCode == GLFW.GLFW_KEY_SPACE) {
         if (Screen.hasShiftDown()) onPrev(); else onNext();
         playTurn();
         return true;
      }
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean charTyped(char c, int modifiers) {
      if (this.typing) {
         search().charTyped(c);
         return true;
      }
      return super.charTyped(c, modifiers);
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
      if (isKnowledgeBook()) {
         // Same layering as a normal book (menu bar + button paper on top) but with
         // the Book of Knowledge texture in place of the cover/links/paper trio.
         gg.blit(MENU_OPTIONS, offsetLeft, offsetTop, 0, 0, 256, 256);
         gg.blit(BOOK_KNOWLADGE, offsetLeft, offsetTop, 0, 0, 256, 256);
      } else {
         gg.blit(MENU_OPTIONS, offsetLeft, offsetTop, 0, 0, 256, 256);
         gg.blit(com.paleimitations.schoolsofmagic.common.items.capabilities.book.Book.coverFor(heldBook, book), offsetLeft, offsetTop, 0, 0, 256, 256);
         gg.blit(com.paleimitations.schoolsofmagic.common.items.capabilities.book.Book.linkLocationFor(heldBook, book), offsetLeft, offsetTop, 0, 0, 256, 256);
         gg.blit(isTocPage() ? PAGE_TOC : PAGE_DEFAULT, offsetLeft, offsetTop, 0, 0, 256, 256);
      }
      com.paleimitations.schoolsofmagic.client.GrimoireScramble.SCRAMBLE =
         heldBook.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook
            && com.paleimitations.schoolsofmagic.common.items.BookDecorations.hasSwirl(heldBook)
            && !com.paleimitations.schoolsofmagic.common.items.ItemSpellbook.isOwner(heldBook, this.player);
      // On a table-of-contents page, live search results replace the entries.
      boolean tocSearching = isTocPage() && !this.results.isEmpty();
      if (!tocSearching && !book.getBookPages().isEmpty() && book.getCurrentPage() != null) {
         book.getCurrentPage().drawPage(gg, mouseX - offsetLeft, mouseY - offsetTop, offsetLeft, offsetTop, true, book.getSubPage());
      }
      com.paleimitations.schoolsofmagic.client.GrimoireScramble.SCRAMBLE = false;
      for (BookElementSticker sticker : book.getStickers()) {
         if (sticker != null) {
            sticker.drawElement(gg, mouseX - offsetLeft, mouseY - offsetTop, offsetLeft, offsetTop, true, book.getSubPage(), book.getPage());
         }
      }
      // Each half of the spread is a page: left number, then right number.
      if (!isKnowledgeBook()) {
         int si = com.paleimitations.schoolsofmagic.client.guis.podium.PodiumGuiHelper.spreadIndex(book);
         if (si >= 0) {
            drawPageNum(gg, offsetLeft + 72, String.valueOf(2 * si + 1));
            drawPageNum(gg, offsetLeft + 184, String.valueOf(2 * si + 2));
         }
      }
      if (searchUiActive()) {
         gg.pose().pushPose();
         gg.pose().translate(barX(), barY(), 0.0F);
         gg.pose().scale(barScale(), barScale(), 1.0F);
         search().render(gg, Math.round(barClip() / barScale()), this.typing);
         gg.pose().popPose();

         if (!this.results.isEmpty()) {
            float rx = resX();
            float ry = resY();
            float rs = resScale();
            gg.pose().pushPose();
            gg.pose().translate(rx, ry, 0.0F);
            gg.pose().scale(rs, rs, 1.0F);
            com.paleimitations.schoolsofmagic.client.KnowledgeResultsView.render(
               gg, this.font, this.results, this.resultPage, PER_PAGE, resWidth(), resColGap(),
               (mouseX - rx) / rs, (mouseY - ry) / rs);
            gg.pose().popPose();
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

      if (searchUiActive()
            && mouseX >= barX() - 2 && mouseX <= barX() + barClip() * barScale() + 4
            && mouseY >= barY() - 3 && mouseY <= barY() + barScale() * 10 + 2) {
         this.typing = true;
         float fx = (float) (mouseX - barX()) / barScale();
         search().clickAt(fx, Math.round(barClip() / barScale()));
         return true;
      }
      this.typing = false;

      if (searchUiActive() && !this.results.isEmpty()) {
         float rx = resX();
         float ry = resY();
         float rs = resScale();
         int hit = com.paleimitations.schoolsofmagic.client.KnowledgeResultsView.hitTest(
            this.font, this.results.size(), this.resultPage, PER_PAGE, resWidth(), resColGap(),
            (float) (mouseX - rx) / rs, (float) (mouseY - ry) / rs);
         if (hit >= 0) {
            net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
               net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                  net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
            com.paleimitations.schoolsofmagic.client.KnowledgeSearch.Hit h = this.results.get(hit);
            if (h.pageIndex >= 0) {
               // Table-of-contents result: jump to the matching page.
               IBook b = getBook();
               if (b != null) { b.setPage(h.pageIndex); b.setSubPage(0); }
               this.results = new java.util.ArrayList<>();
               this.resultPage = 0;
            } else if (this.lecternPos != null && h.shelf != null
                  && com.paleimitations.schoolsofmagic.client.KnowledgeSearch.isWorkstationRenderable(h.source)) {
               com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketKnowledgeFetch(
                     h.shelf, h.slot, this.lecternPos));
            }
            return true;
         }
      }

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
      if (!this.results.isEmpty()) {
         if (this.resultPage < resultPages() - 1) this.resultPage++;
         return;
      }
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
      if (!this.results.isEmpty()) {
         if (this.resultPage > 0) this.resultPage--;
         return;
      }
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
