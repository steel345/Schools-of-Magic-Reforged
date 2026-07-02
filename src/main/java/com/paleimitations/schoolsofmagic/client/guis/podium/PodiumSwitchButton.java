package com.paleimitations.schoolsofmagic.client.guis.podium;

import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSwitchPodiumGui;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class PodiumSwitchButton extends AbstractButton {
   private final int podiumGui;
   private final int gui;
   private final TileEntityPodium podium;

   public PodiumSwitchButton(TileEntityPodium podium, int gui, int podiumGui, int posX, int posY) {
      super(posX, posY, 23, 23, Component.empty());
      this.gui = gui;
      this.podium = podium;
      this.podiumGui = podiumGui;
   }

   @Override
   public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      boolean hovered = mouseX >= this.getX() && mouseY >= this.getY()
            && mouseX < this.getX() + this.width
            && mouseY < this.getY() + this.height;
      gg.blit(GuiPodiumRead.ICONS, this.getX(), this.getY(), !this.canUse() ? 58 : (hovered ? 29 : 0), 0, 23, 23);
      gg.blit(GuiPodiumRead.ICONS, this.getX(), this.getY(),
         23 * (this.podiumGui / 4), 61 + 23 * (this.podiumGui % 4), 23, 23);
   }

   @Override
   public void onPress() {
      if (this.canUse()) {
         PacketHandler.INSTANCE.sendToServer(
            new PacketSwitchPodiumGui(Minecraft.getInstance().player, this.podium.getBlockPos(), this.podiumGui));
      }
   }

   @Override
   protected void updateWidgetNarration(NarrationElementOutput out) {
      this.defaultButtonNarrationText(out);
   }

   private boolean canUse() {
      IBook book = null;
      IItemHandler handler = this.podium.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (handler != null) {
         book = handler.getStackInSlot(0).getCapability(CapabilityBook.BOOK_CAPABILITY).orElse(null);
      }
      return this.gui != this.podiumGui && (this.podiumGui != 2 && this.podiumGui != 3 || book != null);
   }
}
