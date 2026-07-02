package com.paleimitations.schoolsofmagic.client.guis;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import com.paleimitations.schoolsofmagic.common.containers.ContainerTeapot;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiTeapot extends AbstractContainerScreen<ContainerTeapot> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/teapot.png");

   public GuiTeapot(ContainerTeapot menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 188;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

      if (this.menu.getTile() != null && this.menu.getTile().waterLevel > 0) {
         Color color = this.menu.getTile().getColor();
         RenderSystem.setShaderColor(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
         gg.blit(TEXTURE, this.leftPos + 75, this.topPos + 21, 196, (3 - this.menu.getTile().waterLevel) * 52, 34, 52);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
      gg.blit(TEXTURE, this.leftPos + 82, this.topPos + 16, 175, 0, 20, 62);

      com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTeapot te = this.menu.getTile();
      if (te != null && te.getLevel() != null
            && com.paleimitations.schoolsofmagic.common.tileentity.TileEntityTeapot
               .isHeatSourceBelow(te.getLevel(), te.getBlockPos())) {

         gg.blit(TEXTURE, this.leftPos + 34, this.topPos + 61, 179, 65, 7, 10);
      }
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {}
}
