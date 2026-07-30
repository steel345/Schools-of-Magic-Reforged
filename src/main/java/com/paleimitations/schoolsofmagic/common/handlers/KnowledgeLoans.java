package com.paleimitations.schoolsofmagic.common.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

// Server-side registry of active knowledge "loans": a book borrowed from a shelf
// and now held in the reading workstation, with the workstation's own book set
// aside (floating) to swap back. Keyed by the workstation position.
public class KnowledgeLoans {

   public static class Loan {
      public final BlockPos shelf;
      public final int slot;
      public final ItemStack knowledge;

      public Loan(BlockPos shelf, int slot, ItemStack knowledge) {
         this.shelf = shelf;
         this.slot = slot;
         this.knowledge = knowledge;
      }
   }

   private static final Map<BlockPos, Loan> BY_STATION = new HashMap<>();

   public static void add(BlockPos station, BlockPos shelf, int slot, ItemStack knowledge) {
      BY_STATION.put(station.immutable(), new Loan(shelf.immutable(), slot, knowledge.copy()));
   }

   public static Loan get(BlockPos station) {
      return BY_STATION.get(station);
   }

   public static void remove(BlockPos station) {
      BY_STATION.remove(station);
   }
}
