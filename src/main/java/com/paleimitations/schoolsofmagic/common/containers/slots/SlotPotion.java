package com.paleimitations.schoolsofmagic.common.containers.slots;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotPotion extends SlotItemHandler {
   public SlotPotion(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
      super(itemHandler, index, xPosition, yPosition);
   }

   @Override
   public boolean mayPlace(ItemStack stack) {
      return super.mayPlace(stack)
         && (
            stack.getItem() instanceof SplashPotionItem
               || stack.getItem() instanceof LingeringPotionItem
               || stack.getItem() == ItemRegistry.potion_lingering.get()
               || stack.getItem() == ItemRegistry.potion_throwable.get()
         );
   }
}
