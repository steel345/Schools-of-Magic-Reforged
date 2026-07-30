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

   // The bag being shown in the ring: the one held in hand, or the one worn in the
   // charm slot (held open with the charm key). Returns EMPTY when no ring applies.
   public static ItemStack activeBag(Player player) {
      if (player.getMainHandItem().getItem() == ItemRegistry.potion_bag.get()
         && ClientProxy.OPEN_SPELL_RING.isDown()) {
         return player.getMainHandItem();
      }
      // Each key opens the bag in its own slot: the charm key never reaches the belt
      // and the belt key never reaches the charm slot.
      java.util.function.Predicate<ItemStack> isBag = s -> s.getItem() == ItemRegistry.potion_bag.get();
      if (ClientProxy.CHARM_ACTIVATE.isDown()) {
         ItemStack worn = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
            .findCharmPouch(player, isBag);
         if (!worn.isEmpty()) return worn;
      }
      if (ClientProxy.BELT_ACTIVATE.isDown()) {
         ItemStack worn = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
            .findBeltPouch(player, isBag);
         if (!worn.isEmpty()) return worn;
      }
      return ItemStack.EMPTY;
   }

   @SubscribeEvent
   public void renderPotionRing(RenderGuiOverlayEvent.Post event) {

      if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;
      Minecraft mc = Minecraft.getInstance();
      LocalPlayer player = mc.player;
      if (player == null || mc.screen != null) return;
      ItemStack bag = activeBag(player);
      if (bag.isEmpty()) return;
      IItemHandler handler = bag.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      if (handler == null) return;

      GuiGraphics gg = event.getGuiGraphics();
      int xPos = gg.guiWidth() / 2 - 69;
      int yPos = gg.guiHeight() / 2 - 69;
      gg.blit(TEXTURE, xPos, yPos, 0, 0, 138, 138);

      int selected = bag.getDamageValue();
      Component label = selected >= 0 && selected < handler.getSlots() && !handler.getStackInSlot(selected).isEmpty()
         ? handler.getStackInSlot(selected).getHoverName()
         : Component.empty();
      gg.drawString(mc.font, label, gg.guiWidth() / 2 - mc.font.width(label) / 2, yPos - 8, 0xFFFFFF, false);

      this.drawSelector(xPos, yPos, selected, gg);
      this.drawSpellIcons(handler, xPos, yPos, gg);
   }

   public void drawSelector(int xPos, int yPos, int slot, GuiGraphics gg) {
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

   public void drawSpellIcons(IItemHandler handler, int xPos, int yPos, GuiGraphics gg) {
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
