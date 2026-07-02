package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.containers.slots.SlotSmall;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySpellForge;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class ContainerSpellForge extends AbstractContainerMenu {
   private final TileEntitySpellForge te;
   private final IItemHandler handler;

   public ContainerSpellForge(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, resolve(playerInventory, buf.readBlockPos()));
   }

   public ContainerSpellForge(int id, Inventory playerInventory, TileEntitySpellForge te) {
      super(MenuTypeRegistry.SPELL_FORGE.get(), id);
      this.te = te;
      this.handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
      int[][] slotPos = {{62,52},{80,52},{98,52},{62,70},{80,70},{98,70},{62,88},{80,88},{98,88}};
      for (int i = 0; i < 9; i++) {
         this.addSlot(new SlotSmall(this.handler, i, slotPos[i][0], slotPos[i][1]) {
            @Override
            public boolean isActive() {
               return ContainerSpellForge.this.te == null || !ContainerSpellForge.this.te.active;
            }
         });
      }
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

   private static TileEntitySpellForge resolve(Inventory inv, BlockPos pos) {
      return (TileEntitySpellForge) inv.player.level().getBlockEntity(pos);
   }

   public TileEntitySpellForge getTile() {
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
