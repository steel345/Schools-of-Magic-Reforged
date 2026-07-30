package com.paleimitations.schoolsofmagic.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

// Client state for a lectern whose Book of Knowledge is being fetched: first the
// book flips shut (closeStart), then it floats out (float book set).
@OnlyIn(Dist.CLIENT)
public class LecternKnowledgeCache {

   public static class Entry {
      public ItemStack book = ItemStack.EMPTY;
      public long closeStart = -1L;
   }

   private static final Map<BlockPos, Entry> MAP = new HashMap<>();

   private static Entry entry(BlockPos pos) {
      return MAP.computeIfAbsent(pos.immutable(), p -> new Entry());
   }

   // The book starts flipping shut on the lectern surface.
   public static void startClose(BlockPos pos, long now) {
      entry(pos).closeStart = now;
   }

   // The book has floated out (empty clears everything).
   public static void set(BlockPos pos, ItemStack stack) {
      if (stack == null || stack.isEmpty()) {
         MAP.remove(pos);
      } else {
         entry(pos).book = stack;
      }
   }

   public static Entry get(BlockPos pos) {
      return MAP.get(pos);
   }
}
