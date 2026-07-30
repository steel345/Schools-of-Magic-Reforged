package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.containers.slots.SlotHerb;
import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class ContainerHerbPouch extends AbstractContainerMenu {
   private final IItemHandler handler;

   public ContainerHerbPouch(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, playerInventory.player);
   }

   public ContainerHerbPouch(int id, Inventory playerInventory, Player player) {
      super(MenuTypeRegistry.HERB_POUCH.get(), id);
      // The pouch may be held in the main hand or worn in the charm slot.
      ItemStack main = player.getMainHandItem();
      ItemStack source = main.getItem() instanceof ItemHerbPouch ? main : charmPouch(player);
      this.handler = source.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);

      // Pouch storage: 4x5 grid, positioned to line up with the brown cells drawn
      // in the herbalist_pouch.png texture.
      int[] cols = {52, 71, 91, 110};
      int[] rows = {5, 23, 41, 59, 77};
      int index = 0;
      if (this.handler != null) {
         for (int row : rows) {
            for (int col : cols) {
               this.addSlot(new SlotHerb(this.handler, index, col, row));
               ++index;
            }
         }
      }
      // Player inventory + hotbar (standard vanilla layout at x8).
      int invX = 8;
      int invY = 109;
      for (int y = 0; y < 3; ++y) {
         for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x + y * 9 + 9, invX + x * 18, invY + y * 18));
         }
      }
      for (int x = 0; x < 9; ++x) {
         this.addSlot(new Slot(playerInventory, x, invX + x * 18, invY + 58));
      }
   }

   // The pouch may hang from the belt or sit in the charm slot.
   private static ItemStack charmPouch(Player player) {
      return com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .findWornPouch(player, s -> s.getItem() instanceof ItemHerbPouch);
   }

   @Override
   public boolean stillValid(Player p) {
      return true;
   }

   @Override
   public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player playerIn) {
      // Never let the open pouch item itself be moved out of the hand.
      if (slotId >= 0 && this.getSlot(slotId) != null && this.getSlot(slotId).getItem() == playerIn.getMainHandItem()) {
         return;
      }
      super.clicked(slotId, dragType, clickTypeIn, playerIn);
   }

   @Override
   public ItemStack quickMoveStack(Player playerIn, int fromSlot) {
      if (this.handler == null) return ItemStack.EMPTY;
      ItemStack previous = ItemStack.EMPTY;
      Slot slot = this.slots.get(fromSlot);
      if (slot != null && slot.hasItem() && slot.getItem() != playerIn.getMainHandItem()) {
         ItemStack current = slot.getItem();
         previous = current.copy();
         int handlerSlots = this.handler.getSlots();
         boolean intoPouch = fromSlot >= handlerSlots;
         // Only vegetation may shift into the pouch.
         if (intoPouch && !current.is(ItemHerbPouch.VEGETATION)) return ItemStack.EMPTY;
         boolean failed = intoPouch
            ? !this.moveItemStackTo(current, 0, handlerSlots, false)
            : !this.moveItemStackTo(current, handlerSlots, handlerSlots + 36, true);
         if (failed) return ItemStack.EMPTY;
         if (current.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
         if (current.getCount() == previous.getCount()) return ItemStack.EMPTY;
         slot.onTake(playerIn, current);
      }
      return previous;
   }
}
