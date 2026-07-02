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
   }

   private boolean draggingSticker = false;

   @Override
   public boolean mouseClicked(double mx, double my, int button) {
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
      if (draggingSticker && button == 0) {
         stickerX = (float) Math.max(0, Math.min(200, mx - this.leftPos));
         stickerY = (float) Math.max(0, Math.min(135, my - this.topPos));
         return true;
      }
      return super.mouseDragged(mx, my, button, dx, dy);
   }

   @Override
   public boolean mouseReleased(double mx, double my, int button) {
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
            case PAGE: {
               BookPageWriteable w = currentWriteablePage();
               if (w != null) { w.editText(c, -1); return true; }
               break;
            }
            case TITLE:
               appendTitleChar(currentTitleElement(false), c); return true;
            case BOOK:
               appendTitleChar(currentTitleElement(true),  c); return true;
            default:
         }
      }
      return super.charTyped(c, modifiers);
   }

   @Override
   public boolean keyPressed(int key, int scan, int mods) {
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
               BookPageWriteable w = currentWriteablePage();
               if (w == null) break;
               if (ctrlV) { w.editText((char)0, 0); return true; }
               if (legacy >= 0) { w.editText('\0', legacy); return true; }
               break;
            }
            case TITLE:
               if (key == GLFW.GLFW_KEY_BACKSPACE) { backspaceTitle(currentTitleElement(false)); return true; }
               break;
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
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
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
      PodiumGuiHelper.renderGuiSubject(gg, mouseX - this.leftPos - 8.886178F, mouseY - this.topPos - 10.642276F,
         this, podium.handler.getStackInSlot(0), 0.0F, podium, false);
      gg.pose().popPose();

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
      private boolean isArrow() { return kind == Kind.UP || kind == Kind.DOWN || kind == Kind.LEFT || kind == Kind.RIGHT; }
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
         if (!isInsertMode()) return;
         TileEntityPodium podium = getPodium();
         if (podium == null) return;

         String pageKey = switch (kind) {
            case TABLE_OF_CONTENTS -> "table_content";
            case CHAPTER -> "chapter";
            case PAGE -> "";
         };
         if (pageKey.isEmpty()) return;
         PacketHandler.INSTANCE.sendToServer(new com.paleimitations.schoolsofmagic.common.network.PacketInsertPage(
            podium.getPage(), pageKey, podium.getBlockPos()));
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         gg.blit(FINAL, getX(), getY(), hov ? 109 : 97, 78, 12, 12);
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
      @Override public void onPress() {
         if (!isWriteMode()) return;

         EnumWriteState target = switch (kind) {
            case PAGE -> EnumWriteState.PAGE;
            case TITLE -> EnumWriteState.TITLE;
            case BOOK -> EnumWriteState.BOOK;
         };
         writeState = (writeState == target) ? EnumWriteState.NONE : target;
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {
         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;

         gg.blit(FINAL, getX(), getY(), hov ? 109 : 97, 78, 12, 12);
      }
   }
}
