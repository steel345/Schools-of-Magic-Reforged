package com.paleimitations.schoolsofmagic.client.guis;

import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

@OnlyIn(Dist.CLIENT)
public class GuiPotionRing {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/potion_ring.png");

   public GuiPotionRing() {}

   @SubscribeEvent
   public void renderPotionRing(RenderGuiOverlayEvent.Post event) {

      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null) return;
      if (!ClientProxy.OPEN_SPELL_RING.isDown()) return;
      if (player.getMainHandItem().getItem() != ItemRegistry.potion_bag.get()) return;
      IItemHandler handler = player.getMainHandItem().getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (handler == null) return;

      GuiGraphics gg = event.getGuiGraphics();
      int xPos = gg.guiWidth() / 2 - 69;
      int yPos = gg.guiHeight() / 2 - 69;
      gg.blit(TEXTURE, xPos, yPos, 0, 0, 138, 138);

      int selected = player.getMainHandItem().getDamageValue();
      Component label = !handler.getStackInSlot(selected).isEmpty()
         ? handler.getStackInSlot(selected).getHoverName()
         : Component.empty();
      gg.drawString(mc.font, label, gg.guiWidth() / 2 - mc.font.width(label) / 2, yPos - 8, 0xFFFFFF, false);

      this.drawSelector(player, xPos, yPos, handler, selected, gg);
      this.drawSpellIcons(player, xPos, yPos, gg);
   }

   public void drawSelector(Player player, int xPos, int yPos, IItemHandler handler, int slot, GuiGraphics gg) {
      int x;
      int y;
      switch (slot + 1) {
         case 1 -> { x = 57; y = 2; }
         case 2 -> { x = 91; y = 11; }
         case 3 -> { x = 110; y = 41; }
         case 4 -> { x = 110; y = 73; }
         case 5 -> { x = 91; y = 103; }
         case 6 -> { x = 57; y = 112; }
         case 7 -> { x = 23; y = 103; }
         case 8 -> { x = 5; y = 73; }
         case 9 -> { x = 5; y = 41; }
         case 10 -> { x = 23; y = 11; }
         default -> { x = 64; y = 64; }
      }
      if (x != 64 && y != 64) {
         gg.blit(TEXTURE, xPos + x, yPos + y, 142, 0, 24, 24);
      }
   }

   public void drawSpellIcons(Player player, int xPos, int yPos, GuiGraphics gg) {
      if (player.getMainHandItem().getItem() != ItemRegistry.potion_bag.get()) return;
      IItemHandler handler = player.getMainHandItem().getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (handler == null) return;
      drawItemStack(gg, xPos + 61, yPos + 6, handler.getStackInSlot(0));
      drawItemStack(gg, xPos + 95, yPos + 15, handler.getStackInSlot(1));
      drawItemStack(gg, xPos + 114, yPos + 45, handler.getStackInSlot(2));
      drawItemStack(gg, xPos + 114, yPos + 77, handler.getStackInSlot(3));
      drawItemStack(gg, xPos + 95, yPos + 107, handler.getStackInSlot(4));
      drawItemStack(gg, xPos + 61, yPos + 116, handler.getStackInSlot(5));
      drawItemStack(gg, xPos + 27, yPos + 107, handler.getStackInSlot(6));
      drawItemStack(gg, xPos + 9, yPos + 77, handler.getStackInSlot(7));
      drawItemStack(gg, xPos + 9, yPos + 45, handler.getStackInSlot(8));
      drawItemStack(gg, xPos + 27, yPos + 15, handler.getStackInSlot(9));
   }

   private void drawItemStack(GuiGraphics gg, int x, int y, ItemStack stack) {
      if (!stack.isEmpty()) {
         gg.renderItem(stack, x, y);
      }
   }
}
