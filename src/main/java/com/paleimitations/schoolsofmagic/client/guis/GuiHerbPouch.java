package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.common.containers.ContainerHerbPouch;
import com.paleimitations.schoolsofmagic.common.containers.slots.SlotHerb;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiHerbPouch extends AbstractContainerScreen<ContainerHerbPouch> {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/container/herbalist_pouch.png");

   private int lastMouseX;
   private int lastMouseY;

   public GuiHerbPouch(ContainerHerbPouch menu, Inventory playerInventory, Component title) {
      super(menu, playerInventory, title);
      this.imageWidth = 176;
      this.imageHeight = 192;
   }

   @Override
   public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
      this.lastMouseX = mouseX;
      this.lastMouseY = mouseY;
      this.renderBackground(gg);
      super.render(gg, mouseX, mouseY, partialTicks);
      this.renderTooltip(gg, mouseX, mouseY);
   }

   // Suppress the vanilla 16x16 hover highlight on pouch slots; we draw a 2px-inset
   // one in renderSlot instead.
   @Override
   public int getSlotColor(int index) {
      if (index >= 0 && index < this.menu.slots.size() && this.menu.slots.get(index) instanceof SlotHerb) {
         return 0;
      }
      return super.getSlotColor(index);
   }

   @Override
   protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
      gg.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
   }

   @Override
   protected void renderLabels(GuiGraphics gg, int mouseX, int mouseY) {
   }

   // Render pouch-slot contents 2px smaller on every side (12x12 instead of 16x16),
   // with a matching 2px-inset hover highlight drawn at exact pixel coords.
   @Override
   public void renderSlot(GuiGraphics gg, Slot slot) {
      if (slot instanceof SlotHerb) {
         float s = 12.0F / 16.0F;
         float cx = slot.x + 8.0F;
         float cy = slot.y + 8.0F;
         gg.pose().pushPose();
         gg.pose().translate(cx, cy, 0.0F);
         gg.pose().scale(s, s, 1.0F);
         gg.pose().translate(-cx, -cy, 0.0F);
         super.renderSlot(gg, slot);
         gg.pose().popPose();

         int mx = this.lastMouseX - this.leftPos;
         int my = this.lastMouseY - this.topPos;
         if (mx >= slot.x && mx < slot.x + 16 && my >= slot.y && my < slot.y + 16) {
            gg.pose().pushPose();
            gg.pose().translate(0.0F, 0.0F, 240.0F);
            gg.fill(slot.x + 2, slot.y + 2, slot.x + 14, slot.y + 14, 0x80FFFFFF);
            gg.pose().popPose();
         }
      } else {
         super.renderSlot(gg, slot);
      }
   }
}
