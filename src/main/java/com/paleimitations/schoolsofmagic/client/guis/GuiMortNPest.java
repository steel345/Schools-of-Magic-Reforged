package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.ContainerMortNPest;
import com.paleimitations.schoolsofmagic.common.network.PacketGetMortNPest;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityMortNPest;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiMortNPest extends AbstractContainerScreen<ContainerMortNPest> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/mortnpest.png");
   private final ProgressBar progressBar;

   public GuiMortNPest(ContainerMortNPest menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 191;
      this.progressBar = new ProgressBar(TEXTURE, ProgressBar.ProgressBarDirection.LEFT_TO_RIGHT, 52, 8, 62, 10, 200, 0);
   }

   @Override
   protected void init() {
      super.init();
      TileEntityMortNPest tb = this.menu.getTile();
      if (tb != null) {
         this.addRenderableWidget(new PestleButton(82 + this.leftPos, 22 + this.topPos, tb));
      }
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
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
      TileEntityMortNPest tb = this.menu.getTile();
      if (tb != null) {
         this.progressBar.setMin(tb.getCrush()).setMax(tb.getMaxCrush());
         this.progressBar.draw(gg);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static class PestleButton extends AbstractButton {
      private final TileEntityMortNPest mortnpest;

      public PestleButton(int posX, int posY, TileEntityMortNPest mortnpest) {
         super(posX, posY, 12, 35, Component.empty());
         this.mortnpest = mortnpest;
      }

      @Override
      public void onPress() {
         if (this.mortnpest.canPress()) {
            this.mortnpest.setCanPress(false);
            PacketHandler.INSTANCE.sendToServer(new PacketGetMortNPest(this.mortnpest.getBlockPos()));
         }
      }

      @Override
      public void renderWidget(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
         if (!this.visible) return;
         boolean isMouseOver = mouseX >= this.getX() && mouseY >= this.getY()
            && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
         int textureX = 164;
         if (!this.mortnpest.canPress()) textureX += this.width;
         else if (isMouseOver) textureX += this.width * 2;
         gg.blit(TEXTURE, this.getX(), this.getY(), textureX, 0, this.width, this.height);
      }

      @Override
      protected void updateWidgetNarration(NarrationElementOutput out) {
         this.defaultButtonNarrationText(out);
      }
   }
}
