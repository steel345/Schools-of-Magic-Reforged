package com.paleimitations.schoolsofmagic.common.containers.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotWater extends SlotItemHandler {
   public SlotWater(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
   }

   @Override
   public boolean mayPlace(ItemStack stack) {
      return super.mayPlace(stack) && stack.getItem() == Items.WATER_BUCKET;
   }
}
