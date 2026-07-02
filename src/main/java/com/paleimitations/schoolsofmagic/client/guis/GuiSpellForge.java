package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.ContainerSpellForge;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiSpellForge extends AbstractContainerScreen<ContainerSpellForge> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/spell_forge.png");

   public GuiSpellForge(ContainerSpellForge menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 215;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   private boolean isActive() {
      com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge te = this.menu.getTile();
      return te != null && te.active;
   }

   private float progress() {
      com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge te = this.menu.getTile();
      return te != null ? te.getProgress() : 0.0F;
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
      if (isActive()) {
         float pr = progress();
         int barH = 82;
         int filled = (int) (barH * pr);
         if (filled > 0) {
            gg.blit(TEXTURE, this.leftPos + (241 - 100), this.topPos + 37 + (barH - filled), 241, 37 + (barH - filled), 6, filled);
         }
      }
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      if (isActive()) {
         gg.blit(TEXTURE, 176 - 115, 50, 176, 0, 55, 55);
      } else {
         gg.blit(TEXTURE, 176 - 148 + 7, 55 - 18, 176, 55, 26, 79);
      }
   }
}
