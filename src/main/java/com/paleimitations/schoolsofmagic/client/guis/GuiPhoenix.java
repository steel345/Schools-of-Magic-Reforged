package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.ContainerPhoenix;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GuiPhoenix extends AbstractContainerScreen<ContainerPhoenix> {

   private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/horse.png");
   private final EntityPhoenix entity;
   private float xMouse;
   private float yMouse;

   public GuiPhoenix(ContainerPhoenix menu, Inventory inventory, Component title) {
      super(menu, inventory, title);
      this.entity = menu.getEntity();
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTick, int mouseX, int mouseY) {
      int x = this.leftPos;
      int y = this.topPos;
      gg.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
      if (this.entity.isChested()) {
         gg.blit(TEXTURE, x + 79, y + 17, 0, this.imageHeight, ContainerPhoenix.COLUMNS * 18, 54);
      }
      com.paleimitations.schoolsofmagic.client.entity.renders.RenderPhoenix.RENDERING_GUI = true;
      InventoryScreen.renderEntityInInventoryFollowsMouse(gg, x + 51, y + 60, 17,
         (float) (x + 51) - this.xMouse, (float) (y + 75 - 50) - this.yMouse, this.entity);
      com.paleimitations.schoolsofmagic.client.entity.renders.RenderPhoenix.RENDERING_GUI = false;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTick) {
      this.xMouse = (float) mouseX;
      this.yMouse = (float) mouseY;
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTick);
      this.renderTooltip(gg, mouseX, mouseY);
   }
}
