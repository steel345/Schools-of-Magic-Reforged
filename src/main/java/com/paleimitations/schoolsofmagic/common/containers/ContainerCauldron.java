package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.containers.slots.SlotSmall;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCauldron;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class ContainerCauldron extends AbstractContainerMenu {
   private final TileEntityCauldron tb;
   private final IItemHandler handler;

   public ContainerCauldron(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, resolve(playerInventory, buf.readBlockPos()));
   }

   public ContainerCauldron(int id, Inventory playerInventory, TileEntityCauldron tb) {
      super(MenuTypeRegistry.CAULDRON.get(), id);
      this.tb = tb;
      this.handler = tb.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      this.addSlot(new SlotSmall(this.handler, 0, 77, 76));
      this.addSlot(new SlotSmall(this.handler, 1, 54, 61));
      this.addSlot(new SlotSmall(this.handler, 2, 28, 47));
      this.addSlot(new SlotSmall(this.handler, 3, 43, 23));
      this.addSlot(new SlotSmall(this.handler, 4, 71, 29));
      this.addSlot(new SlotSmall(this.handler, 5, 97, 44));
      this.addSlot(new SlotSmall(this.handler, 6, 124, 50));
      this.addSlot(new SlotSmall(this.handler, 7, 135, 26));
      this.addSlot(new SlotSmall(this.handler, 8, 110, 15));
      int xPos = 8;
      int yPos = 159;
      for (int y = 0; y < 3; ++y) {
         for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
         }
      }
      for (int x = 0; x < 9; ++x) {
         this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
      }
   }

   private static TileEntityCauldron resolve(Inventory inv, BlockPos pos) {
      return (TileEntityCauldron) inv.player.level().getBlockEntity(pos);
   }

   public TileEntityCauldron getTile() {
      return this.tb;
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
