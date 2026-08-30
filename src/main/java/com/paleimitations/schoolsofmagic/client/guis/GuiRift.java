package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.ContainerRift;
import com.paleimitations.schoolsofmagic.common.containers.RiftView;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketRiftView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class GuiRift extends AbstractContainerScreen<ContainerRift> {
   private static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/rift.png");

   // the panel is drawn from partway into the sheet, the scroller sits above it in the corner
   private static final int PANEL_U = 33;
   private static final int PANEL_V = 24;
   private static final int SCROLLER_U = 0;
   private static final int SCROLLER_IDLE_V = 0;
   private static final int SCROLLER_ACTIVE_V = 15;
   private static final int SCROLLER_W = 12;
   private static final int SCROLLER_H = 15;

   private static final int TRACK_X = 175;
   private static final int TRACK_Y = 18;
   private static final int TRACK_H = RiftView.HEIGHT * 18 - 2;

   private EditBox search;
   private int scroll;
   private float offset;
   private boolean dragging;
   private int blink;

   public GuiRift(ContainerRift menu, Inventory inventory, Component title) {
      super(menu, inventory, title);
      this.imageWidth = 195;
      this.imageHeight = 215;

   }

   @Override
   protected void init() {
      super.init();
      this.search = new EditBox(this.font, this.leftPos + 83, this.topPos + 6, 84, 10, Component.empty());
      this.search.setMaxLength(48);
      this.search.setBordered(false);
      this.search.setTextColor(0xE6D9FF);
      this.search.setResponder(text -> {
         this.scroll = 0;
         PacketHandler.INSTANCE.sendToServer(new PacketRiftView(text, 0));
      });
      this.addRenderableWidget(this.search);
      this.setInitialFocus(this.search);
   }

   private boolean canScroll() {
      return this.menu.maxScroll() > 0;
   }

   private void sendView() {
      PacketHandler.INSTANCE.sendToServer(new PacketRiftView(this.search.getValue(), this.scroll));
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
      int max = this.menu.maxScroll();
      if (max <= 0) return super.mouseScrolled(mouseX, mouseY, delta);
      this.setOffset(this.offset - (float) delta / (float) max);
      return true;
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.canScroll() && this.overTrack(mouseX, mouseY)) {
         this.dragging = true;
         this.dragTo(mouseY);
         return true;
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
      if (this.dragging) {
         this.dragTo(mouseY);
         return true;
      }
      return super.mouseDragged(mouseX, mouseY, button, dx, dy);
   }

   @Override
   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.dragging = false;
      return super.mouseReleased(mouseX, mouseY, button);
   }

   private boolean overTrack(double mouseX, double mouseY) {
      int x = this.leftPos + TRACK_X;
      int y = this.topPos + TRACK_Y;
      return mouseX >= x && mouseX < x + SCROLLER_W && mouseY >= y && mouseY < y + TRACK_H;
   }

   private void dragTo(double mouseY) {
      this.setOffset(((float) mouseY - (this.topPos + TRACK_Y) - SCROLLER_H / 2.0F) / (TRACK_H - SCROLLER_H));
   }

   // the knob rides a free 0-1 offset the way vanilla does it, and the row it lands on falls out
   // of that. the bar itself never snaps, only what is on the shelves behind it
   private void setOffset(float wanted) {
      int max = this.menu.maxScroll();
      this.offset = Mth.clamp(wanted, 0.0F, 1.0F);
      int row = Mth.clamp((int) ((double) (this.offset * max) + 0.5D), 0, max);
      if (row != this.scroll) {
         this.scroll = row;
         this.sendView();
      }
   }

   // nothing ticks a widget for you, and the cursor blink counts ticks
   @Override
   protected void containerTick() {
      super.containerTick();
      // the box blinks off its own tick count, half as often as vanilla so it is calmer
      if (++this.blink % 2 == 0) this.search.tick();
   }

   @Override
   public boolean keyPressed(int key, int scanCode, int modifiers) {
      if (this.search.isFocused() && key != 256) {
         return this.search.keyPressed(key, scanCode, modifiers) || this.search.canConsumeInput();
      }
      return super.keyPressed(key, scanCode, modifiers);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(TEXTURE, this.leftPos, this.topPos, PANEL_U, PANEL_V, this.imageWidth, this.imageHeight);

      boolean scrollable = this.canScroll();
      if (!scrollable) this.offset = 0.0F;
      int knob = (int) (this.offset * (TRACK_H - SCROLLER_H));

      gg.blit(TEXTURE, this.leftPos + TRACK_X, this.topPos + TRACK_Y + knob,
         SCROLLER_U, scrollable ? SCROLLER_ACTIVE_V : SCROLLER_IDLE_V, SCROLLER_W, SCROLLER_H);
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   public boolean isPauseScreen() {
      return false;
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
   }
}
