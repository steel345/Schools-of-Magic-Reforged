package com.paleimitations.schoolsofmagic.common.containers.slots;

import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

// Only vegetation (the som:vegetation tag) may go in a herb pouch slot.
public class SlotHerb extends SlotItemHandler {
   public SlotHerb(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
   }

   @Override
   public boolean mayPlace(ItemStack stack) {
      return super.mayPlace(stack) && stack.is(ItemHerbPouch.VEGETATION);
   }
}
