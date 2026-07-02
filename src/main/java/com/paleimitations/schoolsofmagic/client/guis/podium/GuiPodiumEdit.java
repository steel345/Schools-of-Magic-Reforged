package com.paleimitations.schoolsofmagic.client.guis.podium;

import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumEdit;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketInsertPage;
import com.paleimitations.schoolsofmagic.common.network.PacketInsertSpellPage;
import com.paleimitations.schoolsofmagic.common.network.PacketRemovePage;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiPodiumEdit extends AbstractContainerScreen<ContainerPodiumEdit> {
   public static final ResourceLocation ICONS = new ResourceLocation("som", "textures/gui/podium/icons.png");
   public static final ResourceLocation OAK_EDIT = new ResourceLocation("som", "textures/gui/podium/oak_edit.png");
   public static final ResourceLocation SPRUCE_EDIT = new ResourceLocation("som", "textures/gui/podium/spruce_edit.png");
   public static final ResourceLocation BIRCH_EDIT = new ResourceLocation("som", "textures/gui/podium/birch_edit.png");
   public static final ResourceLocation ACACIA_EDIT = new ResourceLocation("som", "textures/gui/podium/acacia_edit.png");
   public static final ResourceLocation JUNGLE_EDIT = new ResourceLocation("som", "textures/gui/podium/jungle_edit.png");
   public static final ResourceLocation DARK_OAK_EDIT = new ResourceLocation("som", "textures/gui/podium/dark_oak_edit.png");
   public static final ResourceLocation ASH_EDIT = new ResourceLocation("som", "textures/gui/podium/ash_edit.png");
   public static final ResourceLocation ELDER_EDIT = new ResourceLocation("som", "textures/gui/podium/elder_edit.png");
   public static final ResourceLocation PINE_EDIT = new ResourceLocation("som", "textures/gui/podium/pine_edit.png");
   public static final ResourceLocation WILLOW_EDIT = new ResourceLocation("som", "textures/gui/podium/willow_edit.png");
   public static final ResourceLocation YEW_EDIT = new ResourceLocation("som", "textures/gui/podium/yew_edit.png");
   public static final ResourceLocation VERDE_EDIT = new ResourceLocation("som", "textures/gui/podium/verde_edit.png");

   public GuiPodiumEdit(ContainerPodiumEdit menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private TileEntityPodium getPodium() { return this.menu.getPodium(); }

   private ResourceLocation getTexture() {
      TileEntityPodium p = getPodium();
      if (p == null) return OAK_EDIT;
      return switch (p.getWood()) {
         case OAK -> OAK_EDIT; case SPRUCE -> SPRUCE_EDIT; case BIRCH -> BIRCH_EDIT;
         case ACACIA -> ACACIA_EDIT; case JUNGLE -> JUNGLE_EDIT; case DARK_OAK -> DARK_OAK_EDIT;
         case ASH -> ASH_EDIT; case ELDER -> ELDER_EDIT; case PINE -> PINE_EDIT;
         case WILLOW -> WILLOW_EDIT; case YEW -> YEW_EDIT; case VERDE -> VERDE_EDIT;
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
   protected void init() {
      super.init();
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      this.addRenderableWidget(new PodiumSwitchButton(podium, 2, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 2, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 2, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 2, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 2, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 2, 5, this.leftPos + 154, this.topPos + 130));
      this.addRenderableWidget(new TurnPageButton(podium, false, this.leftPos + 154, this.topPos + 103));
      this.addRenderableWidget(new TurnPageButton(podium, true,  this.leftPos + 73,  this.topPos + 103));
      this.addRenderableWidget(new InsertButton(podium, this.leftPos + 119, this.topPos + 71));

      this.addRenderableWidget(new RemoveButton(podium, false, this.leftPos + 42,  this.topPos + 102));
      this.addRenderableWidget(new RemoveButton(podium, true,  this.leftPos + 193, this.topPos + 102));
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;

      IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      if (book != null) {
         gg.pose().pushPose();
         gg.pose().translate(6.886178F, 1.642276F, 0.0F);
         PodiumGuiHelper.renderGuiSubject(gg, 0.0F, 0.0F, this, book, 0.0F, book.getPage(), true);
         gg.pose().popPose();
         if (!book.getBookPages().isEmpty() && book.getBookPage(book.getPage() + 1) != null) {
            gg.pose().pushPose();
            gg.pose().translate(140.88618F, 1.642276F, 0.0F);
            PodiumGuiHelper.renderGuiSubject(gg, 0.0F, 0.0F, this, book, 0.0F, book.getPage() + 1, true);
            gg.pose().popPose();
         }
      } else {

         gg.pose().pushPose();
         gg.pose().translate(6.886178F, 1.642276F, 0.0F);
         PodiumGuiHelper.renderGuiSubject(gg, mouseX, mouseY, this, podium.handler.getStackInSlot(0), 0.0F, podium, true);
         gg.pose().popPose();
      }
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
   static class InsertButton extends AbstractButton {
      private final TileEntityPodium podium;
      InsertButton(TileEntityPodium podium, int posX, int posY) {

         super(posX, posY, 19, 29, Component.empty());
         this.podium = podium;
      }
      @Override public void onPress() {
         ItemStack inv = podium.handler.getStackInSlot(5);
         IPage page = inv.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
         if (page != null && page.getBookPage() != null) {
            if (page.getBookPage() instanceof BookPageSpell pageSpell) {
               PacketHandler.INSTANCE.sendToServer(new PacketInsertSpellPage(
                  podium.getPage(),
                  pageSpell.getSpell().getResourceLocation(),
                  pageSpell.getSpell().serializeNBT(),
                  podium.getBlockPos()));
            } else {
               PacketHandler.INSTANCE.sendToServer(new PacketInsertPage(
                  podium.getPage(),
                  page.getBookPage().getName(),
                  podium.getBlockPos()));
            }
         }
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {

         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         boolean usable = podium.handler.getStackInSlot(5).getItem() == ItemRegistry.grimoire_page.get();
         gg.blit(GuiPodiumRead.ICONS, getX(), getY(), usable ? (hov ? 19 : 0) : 38, 153, 19, 29);
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class RemoveButton extends AbstractButton {
      private final TileEntityPodium podium;
      private final boolean removeNext;
      RemoveButton(TileEntityPodium podium, boolean removeNext, int posX, int posY) {

         super(posX, posY, 22, 22, Component.empty());
         this.podium = podium; this.removeNext = removeNext;
      }
      @Override public void onPress() {
         PacketHandler.INSTANCE.sendToServer(new PacketRemovePage(removeNext ? podium.getPage() + 1 : podium.getPage(), podium.getBlockPos()));
      }
      @Override protected void updateWidgetNarration(NarrationElementOutput out) { defaultButtonNarrationText(out); }
      @Override public void renderWidget(GuiGraphics gg, int mx, int my, float pt) {

         boolean hov = mx >= getX() && my >= getY() && mx < getX() + width && my < getY() + height;
         IBook book = podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         boolean isCurrent = !removeNext;
         boolean usable = book != null && book.getCurrentPage() != null
            && (book.getPage() != book.getBookPages().size() - 1 || isCurrent);
         gg.blit(GuiPodiumRead.ICONS, getX(), getY(), usable ? (hov ? 22 : 0) : 44, 222, 22, 22);
      }
   }
}
