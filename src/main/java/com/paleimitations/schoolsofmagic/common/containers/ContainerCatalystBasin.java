package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.containers.slots.SlotSmall;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityCatalystBasin;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class ContainerCatalystBasin extends AbstractContainerMenu {
   private final TileEntityCatalystBasin te;
   private final IItemHandler handler;

   public ContainerCatalystBasin(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, resolve(playerInventory, buf.readBlockPos()));
   }

   public ContainerCatalystBasin(int id, Inventory playerInventory, TileEntityCatalystBasin te) {
      super(MenuTypeRegistry.CATALYST_BASIN.get(), id);
      this.te = te;
      this.handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      this.addSlot(new SlotSmall(this.handler, 0, 8, 8));
      this.addSlot(new SlotSmall(this.handler, 1, 8, 27));
      this.addSlot(new SlotSmall(this.handler, 2, 152, 8));
      this.addSlot(new SlotSmall(this.handler, 3, 152, 27));
      int xPos = 8;
      int yPos = 133;
      for (int y = 0; y < 3; ++y) {
         for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
         }
      }
      for (int x = 0; x < 9; ++x) {
         this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
      }
   }

   private static TileEntityCatalystBasin resolve(Inventory inv, BlockPos pos) {
      return (TileEntityCatalystBasin) inv.player.level().getBlockEntity(pos);
   }

   public TileEntityCatalystBasin getTile() {
      return this.te;
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
