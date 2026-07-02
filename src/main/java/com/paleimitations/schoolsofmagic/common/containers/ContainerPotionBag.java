package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.containers.slots.SlotPotion;
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

public class ContainerPotionBag extends AbstractContainerMenu {
   private final IItemHandler handler;
   private final Player player;

   public ContainerPotionBag(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, playerInventory.player);
   }

   public ContainerPotionBag(int id, Inventory playerInventory, Player player) {
      super(MenuTypeRegistry.POTION_BAG.get(), id);
      this.player = player;
      this.handler = player.getMainHandItem().getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      int xPosHand = 44;
      int yPosHand = 36;
      int index = 0;
      for (int y = 0; y < 2; ++y) {
         for (int x = 0; x < 5; ++x) {
            this.addSlot(new SlotPotion(this.handler, index, xPosHand + x * 18, yPosHand + y * 18));
            ++index;
         }
      }
      int xPos = 8;
      int yPos = 129;
      for (int y = 0; y < 3; ++y) {
         for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
         }
      }
      for (int x = 0; x < 9; ++x) {
         this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
      }
   }

   @Override
   public boolean stillValid(Player p) {
      return true;
   }

   @Override
   public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player playerIn) {
      if (slotId >= 0 && this.getSlot(slotId) != null && this.getSlot(slotId).getItem() == playerIn.getMainHandItem()) {
         return;
      }
      super.clicked(slotId, dragType, clickTypeIn, playerIn);
   }

   @Override
   public ItemStack quickMoveStack(Player playerIn, int fromSlot) {
      ItemStack previous = ItemStack.EMPTY;
      Slot slot = this.slots.get(fromSlot);
      if (slot != null && slot.hasItem() && slot.getItem() != playerIn.getMainHandItem()) {
         ItemStack current = slot.getItem();
         previous = current.copy();
         int handlerSlots = this.handler.getSlots();
         boolean failed = fromSlot < handlerSlots
            ? !this.moveItemStackTo(current, handlerSlots, handlerSlots + 36, true)
            : !this.moveItemStackTo(current, 0, handlerSlots, false);
         if (failed) {
            return ItemStack.EMPTY;
         }
         if (current.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
         if (current.getCount() == previous.getCount()) {
            return ItemStack.EMPTY;
         }
         slot.onTake(playerIn, current);
      }
      return previous;
   }
}
