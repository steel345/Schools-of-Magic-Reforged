package com.paleimitations.schoolsofmagic.common.brewing;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.ItemStack;

/** Helper for reading/writing a {@link BrewResult} on a teacup item (dynamic-tea storage). */
public final class DynamicTea {

   public static final String TAG = "BrewResult";

   public static ItemStack create(BrewResult result) {
      ItemStack cup = new ItemStack(ItemRegistry.teacup.get());
      cup.getOrCreateTag().put(TAG, result.toNbt());
      return cup;
   }

   public static boolean isDynamic(ItemStack stack) {
      // Use getTag() (not hasTag()): hasTag() returns false once the stack is shrunk to empty,
      // which would drop the effect data when the tea is drunk.
      return stack != null && stack.getTag() != null && stack.getTag().contains(TAG);
   }

   public static BrewResult get(ItemStack stack) {
      return isDynamic(stack) ? BrewResult.fromNbt(stack.getTag().getCompound(TAG)) : null;
   }

   private DynamicTea() {}
}
