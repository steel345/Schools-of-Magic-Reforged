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

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(getTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
   }

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
      gg.pose().popPose();
   }

   @Override
   protected void init() {
      super.init();
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 0, 5, this.leftPos + 154, this.topPos + 130));
      this.addRenderableWidget(new TurnPageButton(podium, false, this.leftPos + 189, this.topPos + 46));
      this.addRenderableWidget(new TurnPageButton(podium, true, this.leftPos + 38, this.topPos + 46));
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
   static class TurnPageButton extends AbstractButton {
      private final boolean isBack;
      private final TileEntityPodium podium;

      public TurnPageButton(TileEntityPodium podium, boolean isBack, int posX, int posY) {
         super(posX, posY, 29, 19, Component.empty());
         this.podium = podium;
         this.isBack = isBack;
      }

      @Override public void onPress() { this.podium.turnPage(!isBack); }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }

      @Override
      public boolean mouseClicked(double mx, double my, int button) {
         boolean over = this.active && this.visible
            && mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         if (over && button == 1 && Screen.hasShiftDown()) {
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
         int u = ((this.podium.page != 0 || this.podium.subpage != 0 || !this.isBack)
               && (this.podium.getPage() != this.podium.getNumOfPages() - 1
                   || this.podium.getSubPage() != this.podium.getNumOfSubPages() - 1
                   || this.isBack))
            ? (hovered ? 29 : 0) : 58;
         gg.blit(ICONS, this.getX(), this.getY(), u, this.isBack ? 23 : 42, 29, 19);
      }
   }
}
