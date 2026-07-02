package com.paleimitations.schoolsofmagic.client.guis.podium;

import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.containers.podium.ContainerPodiumSpell;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketModifySpell;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuiPodiumSpell extends AbstractContainerScreen<ContainerPodiumSpell> {
   public static final ResourceLocation ICONS = new ResourceLocation("som", "textures/gui/podium/icons.png");
   public static final ResourceLocation PAGE = new ResourceLocation("som", "textures/gui/books/paper.png");
   public static final ResourceLocation ASH_SPELL = new ResourceLocation("som", "textures/gui/podium/ash_spell.png");
   public static final ResourceLocation ELDER_SPELL = new ResourceLocation("som", "textures/gui/podium/elder_spell.png");
   public static final ResourceLocation PINE_SPELL = new ResourceLocation("som", "textures/gui/podium/pine_spell.png");
   public static final ResourceLocation WILLOW_SPELL = new ResourceLocation("som", "textures/gui/podium/willow_spell.png");
   public static final ResourceLocation YEW_SPELL = new ResourceLocation("som", "textures/gui/podium/yew_spell.png");
   public static final ResourceLocation VERDE_SPELL = new ResourceLocation("som", "textures/gui/podium/verde_spell.png");
   public static final ResourceLocation OAK_SPELL = new ResourceLocation("som", "textures/gui/podium/oak_spell.png");
   public static final ResourceLocation SPRUCE_SPELL = new ResourceLocation("som", "textures/gui/podium/spruce_spell.png");
   public static final ResourceLocation BIRCH_SPELL = new ResourceLocation("som", "textures/gui/podium/birch_spell.png");
   public static final ResourceLocation ACACIA_SPELL = new ResourceLocation("som", "textures/gui/podium/acacia_spell.png");
   public static final ResourceLocation DARK_OAK_SPELL = new ResourceLocation("som", "textures/gui/podium/dark_oak_spell.png");
   public static final ResourceLocation JUNGLE_SPELL = new ResourceLocation("som", "textures/gui/podium/jungle_spell.png");

   public GuiPodiumSpell(ContainerPodiumSpell menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 256;
      this.imageHeight = 256;
   }

   private TileEntityPodium getPodium() { return this.menu.getPodium(); }

   private ResourceLocation getTexture() {
      TileEntityPodium p = getPodium();
      if (p == null) return OAK_SPELL;
      return switch (p.getWood()) {
         case OAK -> OAK_SPELL;
         case SPRUCE -> SPRUCE_SPELL;
         case BIRCH -> BIRCH_SPELL;
         case ACACIA -> ACACIA_SPELL;
         case JUNGLE -> JUNGLE_SPELL;
         case DARK_OAK -> DARK_OAK_SPELL;
         case ASH -> ASH_SPELL;
         case ELDER -> ELDER_SPELL;
         case PINE -> PINE_SPELL;
         case WILLOW -> WILLOW_SPELL;
         case YEW -> YEW_SPELL;
         case VERDE -> VERDE_SPELL;
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
      this.addRenderableWidget(new PodiumSwitchButton(podium, 5, 0, this.leftPos + 79, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 5, 1, this.leftPos + 104, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 5, 2, this.leftPos + 129, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 5, 3, this.leftPos + 179, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 5, 4, this.leftPos + 54, this.topPos + 130));
      this.addRenderableWidget(new PodiumSwitchButton(podium, 5, 5, this.leftPos + 154, this.topPos + 130));
      this.addRenderableWidget(new SelectButton(podium, this.leftPos + 121, this.topPos + 37));
   }

   @Override
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      TileEntityPodium podium = getPodium();
      if (podium != null) {
         PodiumGuiHelper.clickGuiSubject(
            (float) (mouseX - this.leftPos) - 6.886178F,
            (float) (mouseY - this.topPos) - 1.642276F,
            podium.handler.getStackInSlot(0), podium, true);
      }
      return super.mouseClicked(mouseX, mouseY, button);
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityPodium podium = getPodium();
      if (podium == null) return;
      gg.pose().pushPose();
      gg.pose().translate(6.886178F, 1.642276F, 0.0F);
      PodiumGuiHelper.renderGuiSubject(gg, mouseX, mouseY, this, podium.handler.getStackInSlot(0), 0.0F, podium, true);
      gg.pose().popPose();
      if (podium.handler.getStackInSlot(7).getItem() == ItemRegistry.spell_modifier_scroll.get()) {
         gg.pose().pushPose();
         gg.pose().translate(140.88618F, 1.642276F, 0.0F);
         PodiumGuiHelper.renderGuiSubject(gg, 0.0F, 0.0F, this, podium.handler.getStackInSlot(7), 0.0F, podium, true);
         gg.pose().popPose();
      }
   }

   @OnlyIn(Dist.CLIENT)
   static class SelectButton extends AbstractButton {
      private final TileEntityPodium podium;

      public SelectButton(TileEntityPodium podium, int posX, int posY) {
         super(posX, posY, 14, 31, Component.empty());
         this.podium = podium;
      }

      @Override
      public void onPress() {
         if (canUse()) PacketHandler.INSTANCE.sendToServer(new PacketModifySpell(this.podium.getBlockPos()));
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
               && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         gg.blit(GuiPodiumRead.ICONS, this.getX(), this.getY(),
            !this.canUse() ? 85 : (hovered ? 71 : 57), 151, 14, 31);
      }

      @Override
      protected void updateWidgetNarration(NarrationElementOutput out) {
         this.defaultButtonNarrationText(out);
      }

      private boolean canUse() {
         Spell spell = null;
         IBook book = this.podium.handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
         if (book != null && book.getCurrentPage() instanceof BookPageSpell bps) spell = bps.getSpell();
         IPage page = this.podium.handler.getStackInSlot(0).getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
         if (page != null && page.getBookPage() instanceof BookPageSpell bps) spell = bps.getSpell();
         return this.podium.handler.getStackInSlot(7).getItem() == ItemRegistry.spell_modifier_scroll.get() && spell != null;
      }
   }
}
