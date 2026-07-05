package com.paleimitations.schoolsofmagic.common.brewing;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.ItemStack;

public final class DynamicTea {

   public static final String TAG = "BrewResult";

   public static ItemStack create(BrewResult result) {
      ItemStack cup = new ItemStack(ItemRegistry.teacup.get());
      cup.getOrCreateTag().put(TAG, result.toNbt());
      return cup;
   }

   public static boolean isDynamic(ItemStack stack) {
      return stack != null && stack.getTag() != null && stack.getTag().contains(TAG);
   }

   public static BrewResult get(ItemStack stack) {
      return isDynamic(stack) ? BrewResult.fromNbt(stack.getTag().getCompound(TAG)) : null;
   }

   private DynamicTea() {}
}
