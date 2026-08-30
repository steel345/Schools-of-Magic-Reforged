package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.CapabilityRiftStorage;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.IRiftStorage;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.RiftStorage;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerRift extends AbstractContainerMenu {
   private final RiftView view;
   private final Player player;
   private final net.minecraft.world.inventory.DataSlot maxScroll = net.minecraft.world.inventory.DataSlot.standalone();

   public ContainerRift(int id, Inventory inventory, FriendlyByteBuf buf) {
      this(id, inventory);
   }

   public ContainerRift(int id, Inventory inventory) {
      super(MenuTypeRegistry.RIFT.get(), id);
      this.player = inventory.player;

      IRiftStorage storage = CapabilityRiftStorage.get(this.player);
      this.view = new RiftView(storage == null ? new RiftStorage() : storage);

      for (int row = 0; row < RiftView.HEIGHT; row++) {
         for (int col = 0; col < RiftView.WIDTH; col++) {
            this.addSlot(new Slot(this.view, col + row * RiftView.WIDTH, 9 + col * 18, 18 + row * 18) {
               @Override
               public boolean mayPlace(ItemStack stack) {
                  // while a search is running the tail slots stand for nothing. without this they
                  // read as empty and quietly eat whatever is put in them
                  return ContainerRift.this.view.mapped(this.getSlotIndex());
               }
            });
         }
      }
      for (int row = 0; row < 3; row++) {
         for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inventory, col + row * 9 + 9, 17 + col * 18, 133 + row * 18));
         }
      }
      for (int col = 0; col < 9; col++) {
         this.addSlot(new Slot(inventory, col, 17 + col * 18, 191));
      }

      this.addDataSlot(this.maxScroll);
      this.maxScroll.set(this.view.maxScroll());
   }

   public int maxScroll() {
      return this.maxScroll.get();
   }

   public RiftView view() {
      return this.view;
   }

   public void search(String query, int scroll) {
      this.view.setQuery(query);
      this.view.setScroll(scroll);
      this.maxScroll.set(this.view.maxScroll());
      this.broadcastChanges();
   }

   public void scrollTo(int rows) {
      this.view.setScroll(rows);
      this.maxScroll.set(this.view.maxScroll());
      this.broadcastChanges();
   }

   @Override
   public void broadcastChanges() {
      this.maxScroll.set(this.view.maxScroll());
      super.broadcastChanges();
   }

   @Override
   public ItemStack quickMoveStack(Player player, int index) {
      Slot slot = this.slots.get(index);
      if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

      ItemStack stack = slot.getItem();
      ItemStack copy = stack.copy();
      int riftEnd = RiftView.VIEW;
      int before = stack.getCount();

      if (index < riftEnd) {
         this.moveItemStackTo(stack, riftEnd, this.slots.size(), true);
      } else if (!this.moveItemStackTo(stack, 0, riftEnd, false)) {
         // the pile only grows on the server, the client is shown where things landed
         if (!player.level().isClientSide && this.view.storage().add(stack.copy())) stack.setCount(0);
      }

      // if nothing actually left the slot we have to say so here. vanilla keeps calling this
      // until the slot stops holding what it held, and it will sit there forever otherwise
      if (stack.getCount() == before) return ItemStack.EMPTY;

      if (stack.isEmpty()) {
         slot.set(ItemStack.EMPTY);
      } else {
         slot.setChanged();
      }
      return copy;
   }

   @Override
   public void removed(Player player) {
      super.removed(player);
      // nothing to write back, the slots were the storage all along. drop the empty tail so the
      // save does not grow every time it is opened
      this.view.storage().trim();
   }

   @Override
   public boolean stillValid(Player player) {
      return player.isAlive();
   }
}
