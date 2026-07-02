package com.paleimitations.schoolsofmagic.common.containers.podium;

import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPodiumFinal extends AbstractContainerMenu {
   protected final IItemHandler handler;
   protected final TileEntityPodium podium;

   public ContainerPodiumFinal(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, resolve(playerInventory, buf.readBlockPos()));
   }

   public ContainerPodiumFinal(int id, Inventory playerInventory, TileEntityPodium podium) {
      super(MenuTypeRegistry.PODIUM_FINAL.get(), id);
      this.podium = podium;
      this.handler = podium.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      int xPos = 48;
      int yPos = 170;
      this.addSlot(new SlotItemHandler(this.handler, 1, 147, 38));
      for (int y = 0; y < 3; ++y) {
         for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
         }
      }
      for (int x = 0; x < 9; ++x) {
         this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
      }
   }

   private static TileEntityPodium resolve(Inventory inv, BlockPos pos) {
      return (TileEntityPodium) inv.player.level().getBlockEntity(pos);
   }

   public TileEntityPodium getPodium() {
      return this.podium;
   }

   @Override
   public boolean stillValid(Player playerIn) {
      return !playerIn.isSpectator();
   }

   @Override
   public ItemStack quickMoveStack(Player playerIn, int fromSlot) {
      ItemStack previous = ItemStack.EMPTY;
      Slot slot = this.slots.get(fromSlot);
      if (slot != null && slot.hasItem()) {
         ItemStack current = slot.getItem();
         previous = current.copy();

         int teSlots = 1;
         boolean failed = fromSlot < teSlots
            ? !this.moveItemStackTo(current, teSlots, this.slots.size(), true)
            : !this.moveItemStackTo(current, 0, teSlots, false);
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
