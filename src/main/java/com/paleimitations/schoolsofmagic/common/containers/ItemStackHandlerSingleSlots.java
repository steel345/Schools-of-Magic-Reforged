package com.paleimitations.schoolsofmagic.common.containers;

import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerSingleSlots extends ItemStackHandler {
   public ItemStackHandlerSingleSlots(int size) {
      super(size);
   }

   @Override
   public int getSlotLimit(int slot) {
      return 1;
   }
}
