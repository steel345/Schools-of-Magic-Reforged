package com.paleimitations.schoolsofmagic.client.guis.podium;

import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumRead;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketTurnPage;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiPodiumRead extends AbstractContainerScreen<ContainerPodiumRead> {
   public static final ResourceLocation TABLE_OF_CONTENTS = new ResourceLocation("som", "textures/gui/books/paper_default_table_of_contents.png");
   public static final ResourceLocation MENU_OPTIONS = new ResourceLocation("som", "textures/gui/books/menu_options.png");
   public static final ResourceLocation ICONS = new ResourceLocation("som", "textures/gui/podium/icons.png");
   public static final ResourceLocation ICON_BAR = new ResourceLocation("som", "textures/gui/podium/icon_bar.png");
   public static final ResourceLocation ASH_READ = new ResourceLocation("som", "textures/gui/podium/ash_read.png");
   public static final ResourceLocation ELDER_READ = new ResourceLocation("som", "textures/gui/podium/elder_read.png");
   public static final ResourceLocation PINE_READ = new ResourceLocation("som", "textures/gui/podium/pine_read.png");
   public static final ResourceLocation WILLOW_READ = new ResourceLocation("som", "textures/gui/podium/willow_read.png");
   public static final ResourceLocation YEW_READ = new ResourceLocation("som", "textures/gui/podium/yew_read.png");
   public static final ResourceLocation VERDE_READ = new ResourceLocation("som", "textures/gui/podium/verde_read.png");
   public static final ResourceLocation OAK_READ = new ResourceLocation("som", "textures/gui/podium/oak_read.png");
   public static final ResourceLocation SPRUCE_READ = new ResourceLocation("som", "textures/gui/podium/spruce_read.png");
   public static final ResourceLocation BIRCH_READ = new ResourceLocation("som", "textures/gui/podium/birch_read.png");
   public static final ResourceLocation ACACIA_READ = new ResourceLocation("som", "textures/gui/podium/acacia_read.png");
   public static final ResourceLocation DARK_OAK_READ = new ResourceLocation("som", "textures/gui/podium/dark_oak_read.png");
   public static final ResourceLocation JUNGLE_READ = new ResourceLocation("som", "textures/gui/podium/jungle_read.png");

   private final java.util.List<MenuButton> menuButtons = new java.util.ArrayList<>();
   private com.paleimitations.schoolsofmagic.client.guis.BookSearchField search;
   private boolean typing = false;
   private java.util.List<com.paleimitations.schoolsofmagic.client.KnowledgeSearch.Hit> results = new java.util.ArrayList<>();
   private int resultPage = 0;
   private static final int PER_PAGE = 12;

   private void runSearch() {
      if (isKnowledgeBook()) {
         TileEntityPodium podium = getPodium();
         if (podium != null) {
            PacketHandler.INSTANCE.sendToServer(
               new com.paleimitations.schoolsofmagic.common.network.PacketKnowledgeRequest(podium.getBlockPos()));
         }
      } else if (isTocPage()) {
         IBook b = getPodiumBook();
         String q = search().getValue().trim();
         this.results = com.paleimitations.schoolsofmagic.client.KnowledgeSearch.searchBook(b, search().getValue());
         if (q.matches("\\d+")) {
            int n = Integer.parseInt(q);
            int[] ps = PodiumGuiHelper.spreadToPageSub(b, (n - 1) / 2);
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

   private IBook getPodiumBook() {
      TileEntityPodium p = getPodium();
      if (p == null) return null;
      return p.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
   }

   private boolean isTocPage() {
      IBook b = getPodiumBook();
      return b != null && b.getCurrentPage() instanceof com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
   }

   private boolean searchUiActive() { return isKnowledgeBook() || isTocPage(); }

   public void acceptCandidates(java.util.List<com.paleimitations.schoolsofmagic.common.handlers.KnowledgeGather.Found> found) {
      this.results = com.paleimitations.schoolsofmagic.client.KnowledgeSearch.matchAll(found, search().getValue());
      this.resultPage = 0;
   }

   private int resultPages() {
      return com.paleimitations.schoolsofmagic.client.KnowledgeResultsView.pageCount(this.results.size(), PER_PAGE);
   }

   private com.paleimitations.schoolsofmagic.client.guis.BookSearchField search() {
      if (this.search == null) {
         this.search = new com.paleimitations.schoolsofmagic.client.guis.BookSearchField(this.font);
      }
      return this.search;
   }

   public GuiPodiumRead(ContainerPodiumRead menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private TileEntityPodium getPodium() { return this.menu.getPodium(); }

   private ResourceLocation getTexture() {
      TileEntityPodium p = getPodium();
      if (p == null) return OAK_READ;
      return switch (p.getWood()) {
         case OAK -> OAK_READ;
         case SPRUCE -> SPRUCE_READ;
         case BIRCH -> BIRCH_READ;
         case ACACIA -> ACACIA_READ;
         case JUNGLE -> JUNGLE_READ;
         case DARK_OAK -> DARK_OAK_READ;
         case ASH -> ASH_READ;
         case ELDER -> ELDER_READ;
         case PINE -> PINE_READ;
         case WILLOW -> WILLOW_READ;
         case YEW -> YEW_READ;
         case VERDE -> VERDE_READ;
      };
   }

   private boolean overBar(double mouseX, double mouseY) {
      float bx = this.leftPos + BOOK_TX + BOOK_SCALE * (-20.0F + barTexX());
      float by = this.topPos + BOOK_TY + BOOK_SCALE * (-23.0F + barTexY());
      float bw = BOOK_SCALE * texClip();
      float bh = BOOK_SCALE * 12.0F;
      return mouseX >= bx - 2 && mouseX <= bx + bw + 2 && mouseY >= by - 2 && mouseY <= by + bh;
   }

   @Override
   protected void containerTick() {
      super.containerTick();
      search().tick();
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(getTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

      boolean hasBook = getPodiumBook() != null;
      for (MenuButton button : this.menuButtons) {
         button.visible = hasBook;
         button.active = hasBook;
      }
      if (!hasBook) return;

      com.mojang.blaze3d.systems.RenderSystem.enableBlend();
      com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
      gg.pose().pushPose();
      gg.pose().translate(menuLeft(), this.topPos + MENU_TOP, 0.0F);
      gg.pose().scale(MENU_SCALE, MENU_SCALE, 1.0F);
      gg.blit(MENU_OPTIONS, 0, 0, MENU_STRIP_U, MENU_STRIP_V, MENU_STRIP_W, MENU_STRIP_H);
      gg.pose().popPose();
   }

   private static final float BOOK_TX = 73.88618F;
   private static final float BOOK_TY = 1.642276F;
   private static final float BOOK_SCALE = 0.50406504F;

   private static final float TEX_TEXT_SCALE = 0.75F;
   private static final float TEX_RESULT_SCALE = 0.55F;

   private float barTexX() { return isKnowledgeBook() ? 42.0F : 44.0F; }
   private float barTexY() { return isKnowledgeBook() ? 53.0F : 188.0F; }
   private int texClip()   { return 43; }
   private static final float TEX_RESULT_X = 24.0F;
   private static final float TEX_RESULT_Y = 64.0F;
   private static final int RESULT_WIDTH = 180;
   private static final float RESULT_COLGAP = 200.0F;

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      gg.pose().pushPose();
      gg.pose().translate(73.88618F, 1.642276F, 0.0F);
      PodiumGuiHelper.renderGuiSubject(gg,
         (mouseX - this.leftPos) - 73.88618F,
         (mouseY - this.topPos) - 1.642276F,
         this, podium.handler.getStackInSlot(0), 0.0F, podium, false);
      if (searchUiActive()) {
         gg.pose().pushPose();
         gg.pose().scale(BOOK_SCALE, BOOK_SCALE, 1.0F);
         gg.pose().translate(-20.0F, -23.0F, 0.0F);

         if (isTocPage()) {
            if (!this.results.isEmpty()) {
               gg.blit(TABLE_OF_CONTENTS, 0, 0, 0, 0, 256, 256);
            } else {
               gg.blit(TABLE_OF_CONTENTS, 34, 183, 34, 183, 108, 22);
            }
         }

         gg.pose().pushPose();
         gg.pose().translate(barTexX(), barTexY(), 0.0F);
         gg.pose().scale(TEX_TEXT_SCALE, TEX_TEXT_SCALE, 1.0F);
         search().render(gg, Math.round(texClip() / TEX_TEXT_SCALE), this.typing);
         gg.pose().popPose();

         if (!this.results.isEmpty()) {
            gg.pose().pushPose();
            gg.pose().translate(TEX_RESULT_X, TEX_RESULT_Y, 0.0F);
            gg.pose().scale(TEX_RESULT_SCALE, TEX_RESULT_SCALE, 1.0F);
            float originX = this.leftPos + BOOK_TX + BOOK_SCALE * (-20.0F + TEX_RESULT_X);
            float originY = this.topPos + BOOK_TY + BOOK_SCALE * (-23.0F + TEX_RESULT_Y);
            float sf = BOOK_SCALE * TEX_RESULT_SCALE;
            com.paleimitations.schoolsofmagic.client.KnowledgeResultsView.render(
               gg, this.font, this.results, this.resultPage, PER_PAGE, RESULT_WIDTH, RESULT_COLGAP,
               (mouseX - originX) / sf, (mouseY - originY) / sf);
            gg.pose().popPose();
         }
         gg.pose().popPose();
      }
      gg.pose().popPose();
   }

   private boolean isKnowledgeBook() {
      TileEntityPodium p = getPodium();
      return p != null && p.handler.getStackInSlot(0).getItem()
         == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.book_of_knowledge.get();
   }

   @Override
   protected void init() {
      super.init();
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      boolean knowledge = isKnowledgeBook();
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 5, this.leftPos + 154, this.topPos + 130));
      this.addRenderableWidget(new TurnPageButton(podium, false, this.leftPos + 189, this.topPos + 46));
      this.addRenderableWidget(new TurnPageButton(podium, true, this.leftPos + 38, this.topPos + 46));

      this.menuButtons.clear();
      this.addMenuButton(70, 19, this::onPrev);
      this.addMenuButton(93, 14, this::onBackChapter);
      this.addMenuButton(112, 14, this::onIndex);
      this.addMenuButton(130, 14, this::onClose);
      this.addMenuButton(149, 14, this::onNextChapter);
      this.addMenuButton(167, 19, this::onNext);
   }

   private boolean clickedQuill(double mx, double my) {
      TileEntityPodium podium = getPodium();
      IBook book = getPodiumBook();
      if (podium == null || book == null || book.getCurrentPage() == null) return false;
      com.paleimitations.schoolsofmagic.common.books.editor.BookPageLayout layout =
         book.getPageLayouts().get(book.getPage());
      if (layout != null && layout.finished) return false;
      for (com.paleimitations.schoolsofmagic.common.books.PageElement e : book.getCurrentPage().elements) {
         if (e instanceof com.paleimitations.schoolsofmagic.common.books.PageElementQuill quill
               && com.paleimitations.schoolsofmagic.common.books.PageElementQuill.isOverScreen(mx, my)) {
            quill.press();
            this.minecraft.setScreen(
               new com.paleimitations.schoolsofmagic.client.guis.editor.GuiBookEditor(
                  this, podium.getBlockPos(), book.getPage(), layout));
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.typing) {
         if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER
               || keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER) {
            this.typing = false;
            runSearch();
            return true;
         }
         if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
            this.typing = false;
            return true;
         }
         search().keyPressed(keyCode);
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
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (button == 0 && clickedQuill(mouseX, mouseY)) {
         return true;
      }
      if (searchUiActive() && overBar(mouseX, mouseY)) {
         this.typing = true;

         float originX = this.leftPos + BOOK_TX + BOOK_SCALE * (-20.0F + barTexX());
         float sf = BOOK_SCALE * TEX_TEXT_SCALE;
         float fx = (float) (mouseX - originX) / sf;
         search().clickAt(fx, Math.round(texClip() / TEX_TEXT_SCALE));
         return true;
      }
      this.typing = false;

      if (searchUiActive() && !this.results.isEmpty()) {
         float originX = this.leftPos + BOOK_TX + BOOK_SCALE * (-20.0F + TEX_RESULT_X);
         float originY = this.topPos + BOOK_TY + BOOK_SCALE * (-23.0F + TEX_RESULT_Y);
         float sf = BOOK_SCALE * TEX_RESULT_SCALE;
         int hit = com.paleimitations.schoolsofmagic.client.KnowledgeResultsView.hitTest(
            this.font, this.results.size(), this.resultPage, PER_PAGE, RESULT_WIDTH, RESULT_COLGAP,
            (float) (mouseX - originX) / sf, (float) (mouseY - originY) / sf);
         if (hit >= 0) {
            net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
               net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
                  net.minecraft.sounds.SoundEvents.UI_BUTTON_CLICK, 1.0F));
            com.paleimitations.schoolsofmagic.client.KnowledgeSearch.Hit h = this.results.get(hit);
            if (h.pageIndex >= 0) {
               IBook b = getPodiumBook();
               if (b != null) { b.setPage(h.pageIndex); b.setSubPage(0); }
               PacketHandler.INSTANCE.sendToServer(new PacketTurnPage(h.pageIndex, 0, getPodium().getBlockPos()));
               this.results = new java.util.ArrayList<>();
               this.resultPage = 0;
            } else if (h.shelf != null
                  && com.paleimitations.schoolsofmagic.client.KnowledgeSearch.isWorkstationRenderable(h.source)) {
               PacketHandler.INSTANCE.sendToServer(
                  new com.paleimitations.schoolsofmagic.common.network.PacketKnowledgeFetch(
                     h.shelf, h.slot, getPodium().getBlockPos()));
            }
            return true;
         }
      }

      TileEntityPodium podium = getPodium();
      if (podium != null) {
         PodiumGuiHelper.clickGuiSubject(
            (float) (mouseX - this.leftPos) - 73.88618F,
            (float) (mouseY - this.topPos) - 1.642276F,
            podium.handler.getStackInSlot(0), podium, false);
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @OnlyIn(Dist.CLIENT)
   class TurnPageButton extends AbstractButton {
      private final boolean isBack;
      private final TileEntityPodium podium;

      public TurnPageButton(TileEntityPodium podium, boolean isBack, int posX, int posY) {
         super(posX, posY, 29, 19, Component.empty());
         this.podium = podium;
         this.isBack = isBack;
      }

      private boolean searching() { return !GuiPodiumRead.this.results.isEmpty(); }

      private boolean canTurn() {
         if (searching()) {
            return this.isBack ? GuiPodiumRead.this.resultPage > 0
               : GuiPodiumRead.this.resultPage < GuiPodiumRead.this.resultPages() - 1;
         }
         return (this.podium.page != 0 || this.podium.subpage != 0 || !this.isBack)
            && (this.podium.getPage() != this.podium.getNumOfPages() - 1
                || this.podium.getSubPage() != this.podium.getNumOfSubPages() - 1
                || this.isBack);
      }

      @Override
      public void onPress() {
         if (searching()) {
            if (this.isBack) {
               if (GuiPodiumRead.this.resultPage > 0) GuiPodiumRead.this.resultPage--;
            } else {
               if (GuiPodiumRead.this.resultPage < GuiPodiumRead.this.resultPages() - 1) GuiPodiumRead.this.resultPage++;
            }
         } else {
            this.podium.turnPage(!isBack);
         }
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }

      @Override
      public boolean mouseClicked(double mx, double my, int button) {
         boolean over = this.active && this.visible
            && mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         if (over && button == 1 && Screen.hasShiftDown() && !searching()) {
            IBook book = this.podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
            if (book != null) { book.setPage(0); book.setSubPage(0); }
            PacketHandler.INSTANCE.sendToServer(new PacketTurnPage(0, 0, this.podium.getBlockPos()));
            return true;
         }
         return super.mouseClicked(mx, my, button);
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         int u = canTurn() ? (hovered ? 29 : 0) : 58;
         gg.blit(ICONS, this.getX(), this.getY(), u, this.isBack ? 23 : 42, 29, 19);
      }
   }

   // the podium draws its book at half size, so the bar has to shrink with it
   private static final float MENU_SCALE = BOOK_SCALE;
   private static final int MENU_TOP = -5;

   private static final int MENU_STRIP_U = 58;
   private static final int MENU_STRIP_V = 8;
   private static final int MENU_STRIP_W = 140;
   private static final int MENU_STRIP_H = 33;
   private static final int MENU_BUTTON_DROP = 8;

   private float menuLeft() {
      return this.leftPos + 128 - (128 - MENU_STRIP_U) * MENU_SCALE;
   }

   private void addMenuButton(int uBase, int width, Runnable onPress) {
      int x = Math.round(menuLeft() + (uBase - MENU_STRIP_U) * MENU_SCALE);
      int y = Math.round(this.topPos + MENU_TOP + MENU_BUTTON_DROP * MENU_SCALE);
      MenuButton button = new MenuButton(x, y, uBase, width, onPress);
      this.menuButtons.add(button);
      this.addRenderableWidget(button);
   }

   private void playTurn() {
      net.minecraft.client.Minecraft.getInstance().getSoundManager().play(
         net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(
            net.minecraft.sounds.SoundEvents.BOOK_PAGE_TURN, 1.0F));
   }

   private void jumpTo(int page) {
      TileEntityPodium podium = getPodium();
      IBook book = getPodiumBook();
      if (podium == null || book == null) return;
      book.setPage(page);
      book.setSubPage(0);
      podium.page = page;
      podium.subpage = 0;
      PacketHandler.INSTANCE.sendToServer(new PacketTurnPage(page, 0, podium.getBlockPos()));
      playTurn();
   }

   private void onNext() {
      if (!this.results.isEmpty()) {
         if (this.resultPage < resultPages() - 1) this.resultPage++;
         return;
      }
      TileEntityPodium podium = getPodium();
      if (podium != null) podium.turnPage(true);
   }

   private void onPrev() {
      if (!this.results.isEmpty()) {
         if (this.resultPage > 0) this.resultPage--;
         return;
      }
      TileEntityPodium podium = getPodium();
      if (podium != null) podium.turnPage(false);
   }

   private void onNextChapter() {
      IBook book = getPodiumBook();
      if (book == null) return;
      java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> pages = book.getBookPages();
      for (int i = book.getPage() + 1; i < pages.size(); i++) {
         if (pages.get(i) instanceof com.paleimitations.schoolsofmagic.common.books.BookPageChapter) {
            jumpTo(i);
            return;
         }
      }
   }

   private void onBackChapter() {
      IBook book = getPodiumBook();
      if (book == null) return;
      java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> pages = book.getBookPages();
      for (int i = book.getPage() - 1; i >= 0; i--) {
         if (pages.get(i) instanceof com.paleimitations.schoolsofmagic.common.books.BookPageChapter) {
            jumpTo(i);
            return;
         }
      }
   }

   private void onIndex() {
      IBook book = getPodiumBook();
      if (book == null) return;
      java.util.List<com.paleimitations.schoolsofmagic.common.books.BookPage> pages = book.getBookPages();
      for (int i = 0; i < pages.size(); i++) {
         if (pages.get(i) instanceof com.paleimitations.schoolsofmagic.common.books.BookPageTableContent) {
            jumpTo(i);
            return;
         }
      }
      jumpTo(0);
   }

   @OnlyIn(Dist.CLIENT)
   class MenuButton extends AbstractButton {
      private final int uBase;
      private final int w;
      private final Runnable onPress;

      public MenuButton(int posX, int posY, int uBase, int width, Runnable onPress) {
         super(posX, posY, Math.round(width * MENU_SCALE), Math.round(14 * MENU_SCALE), Component.empty());
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
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         gg.pose().pushPose();
         gg.pose().translate(this.getX(), this.getY(), 0.0F);
         gg.pose().scale(MENU_SCALE, MENU_SCALE, 1.0F);
         gg.blit(MENU_OPTIONS, 0, 0, this.uBase, hovered ? 41 : 55, this.w, 14);
         gg.pose().popPose();
      }
   }
}
