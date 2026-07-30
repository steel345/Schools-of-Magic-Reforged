package com.paleimitations.schoolsofmagic.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class LecternPageCache {

   private static final Map<BlockPos, ItemStack> CACHE = new HashMap<>();

   private LecternPageCache() {}

   public static void set(BlockPos pos, ItemStack stack) {
      if (stack == null || stack.isEmpty()) {
         CACHE.remove(pos.immutable());
      } else {
         CACHE.put(pos.immutable(), stack);
      }
   }

   public static ItemStack get(BlockPos pos) {
      return CACHE.getOrDefault(pos, ItemStack.EMPTY);
   }

   public static void clear() {
      CACHE.clear();
   }
}
