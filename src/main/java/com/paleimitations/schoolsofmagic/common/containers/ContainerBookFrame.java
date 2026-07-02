package com.paleimitations.schoolsofmagic.common.containers;

import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBookFrame extends AbstractContainerMenu {

   public static final int FRAME_X = 64;
   public static final int FRAME_Y = 221;
   public static final int FRAME_SPACING = 22;
   public static final int[] SLOT_X = {68, 89, 110, 131, 152, 173};

   private final IItemHandler handler;

   public ContainerBookFrame(int id, Inventory playerInventory, FriendlyByteBuf buf) {
      this(id, playerInventory, playerInventory.player);
   }

   public ContainerBookFrame(int id, Inventory playerInventory, Player player) {
      super(MenuTypeRegistry.BOOK_FRAME.get(), id);
      this.handler = new BookFrameHandler(player.getMainHandItem());

      for (int i = 0; i < BookFrameHandler.SIZE; i++) {
         this.addSlot(new SlotItemHandler(this.handler, i, SLOT_X[i], FRAME_Y));
      }
   }

   @Override
   public boolean stillValid(Player p) {
      return p.getMainHandItem().getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemSpellbook;
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
      Slot slot = this.slots.get(fromSlot);
      if (slot != null && slot.hasItem()) {
         ItemStack stack = slot.remove(slot.getItem().getCount());
         if (!playerIn.getInventory().add(stack)) {
            playerIn.drop(stack, false);
         }
         slot.setChanged();
      }
      return ItemStack.EMPTY;
   }
}
