package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ContainerPhoenix extends AbstractContainerMenu {

   public static final int COLUMNS = 5;
   private final EntityPhoenix entity;
   private final Container inv;

   public ContainerPhoenix(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, (EntityPhoenix) playerInventory.player.level().getEntity(buf.readInt()));
   }

   public ContainerPhoenix(int id, Inventory playerInventory, final EntityPhoenix entity) {
      super(MenuTypeRegistry.PHOENIX.get(), id);
      this.entity = entity;
      this.inv = entity.inventory;
      this.inv.startOpen(playerInventory.player);

      if (entity.isChested()) {
         for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < COLUMNS; ++col) {
               this.addSlot(new Slot(this.inv, 1 + col + row * COLUMNS, 80 + col * 18, 18 + row * 18));
            }
         }
      }

      for (int row = 0; row < 3; ++row) {
         for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
         }
      }
      for (int col = 0; col < 9; ++col) {
         this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
      }
   }

   public EntityPhoenix getEntity() {
      return this.entity;
   }

   @Override
   public boolean stillValid(Player player) {
      return this.inv.stillValid(player) && this.entity.isAlive() && this.entity.distanceTo(player) < 8.0F;
   }

   @Override
   public void removed(Player player) {
      super.removed(player);
      this.inv.stopOpen(player);
   }

   @Override
   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack result = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack stack = slot.getItem();
         result = stack.copy();
         int phoenixSlots = this.slots.size() - 36;
         if (index < phoenixSlots) {
            if (!this.moveItemStackTo(stack, phoenixSlots, this.slots.size(), true)) return ItemStack.EMPTY;
         } else if (phoenixSlots == 0 || !this.moveItemStackTo(stack, 0, phoenixSlots, false)) {
            return ItemStack.EMPTY;
         }
         if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
         else slot.setChanged();
      }
      return result;
   }
}
