package com.paleimitations.schoolsofmagic.common.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSmall extends SlotItemHandler {
   public SlotSmall(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
   }

   @Override
   public int getMaxStackSize(ItemStack stack) {
      return 1;
   }

   @Override
   public int getMaxStackSize() {
      return 1;
   }
}
