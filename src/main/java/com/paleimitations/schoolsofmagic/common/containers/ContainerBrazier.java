package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBrazier extends AbstractContainerMenu {

   // Input slots ordered bottom-to-top so items fill from the lowest slot upward. Shifted +1,+1.
   private static final int[][] BRAZIER_SLOTS = {
      {81, 71}, {60, 60}, {39, 53}, {100, 41}, {37, 32},
      {79, 31}, {121, 31}, {58, 25}, {115, 10}
   };
   private static final int OUTPUT_X = 79;
   private static final int OUTPUT_Y = 113;

   private final TileEntityRitualCenter te;
   private final IItemHandler handler;
   private final int inputCount;

   public ContainerBrazier(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, resolve(playerInventory, buf.readBlockPos()));
   }

   public ContainerBrazier(int id, Inventory playerInventory, TileEntityRitualCenter te) {
      super(MenuTypeRegistry.BRAZIER.get(), id);
      this.te = te;
      this.handler = te.handler;
      this.inputCount = Math.min(BRAZIER_SLOTS.length, this.handler.getSlots());

      for (int i = 0; i < this.inputCount; i++) {
         this.addSlot(new SlotItemHandler(this.handler, i, BRAZIER_SLOTS[i][0], BRAZIER_SLOTS[i][1]));
      }
      this.addSlot(new SlotItemHandler(te.output, 0, OUTPUT_X, OUTPUT_Y) {
         @Override
         public boolean mayPlace(ItemStack stack) {
            return false;
         }
      });

      for (int row = 0; row < 3; row++) {
         for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 159 + row * 18));
         }
      }
      for (int col = 0; col < 9; col++) {
         this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 217));
      }
   }

   private static TileEntityRitualCenter resolve(Inventory inv, BlockPos pos) {
      return (TileEntityRitualCenter) inv.player.level().getBlockEntity(pos);
   }

   public TileEntityRitualCenter getTile() {
      return this.te;
   }

   public boolean isLit() {
      if (this.te == null || this.te.getLevel() == null) return false;
      BlockState s = this.te.getLevel().getBlockState(this.te.getBlockPos());
      return s.hasProperty(BlockBrazier.FLAME) && s.getValue(BlockBrazier.FLAME) > 0;
   }

   @Override
   public boolean stillValid(Player player) {
      if (this.te == null || this.te.isRemoved()) return false;
      return player.distanceToSqr(this.te.getBlockPos().getX() + 0.5D,
         this.te.getBlockPos().getY() + 0.5D, this.te.getBlockPos().getZ() + 0.5D) <= 64.0D;
   }

   @Override
   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack result = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack stack = slot.getItem();
         result = stack.copy();
         int brazierSlots = this.inputCount + 1;
         if (index < brazierSlots) {
            if (!this.moveItemStackTo(stack, brazierSlots, this.slots.size(), true)) return ItemStack.EMPTY;
         } else if (!this.moveItemStackTo(stack, 0, this.inputCount, false)) {
            return ItemStack.EMPTY;
         }
         if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
         else slot.setChanged();
         if (stack.getCount() == result.getCount()) return ItemStack.EMPTY;
         slot.onTake(player, stack);
      }
      return result;
   }
}
