package com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data;

import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// What each garment slot will take, and where a worn pouch or book is looked up.
public class GarmentSlots {

   public static boolean accepts(int slot, ItemStack stack) {
      if (stack.isEmpty()) return false;
      return switch (slot) {
         // Nothing is made for the head or shoulders yet.
         case IGarmentData.CROWN, IGarmentData.CAPE -> false;
         case IGarmentData.BELT -> stack.getItem() instanceof ItemHerbPouch
            || stack.getItem() == ItemRegistry.potion_bag.get();
         case IGarmentData.GRIMOIRE -> stack.getCapability(CapabilityBook.BOOK_CAPABILITY).isPresent();
         default -> false;
      };
   }

   public static ItemStack getWorn(Player player, int slot) {
      IGarmentData data = CapabilityGarmentData.get(player);
      return data == null ? ItemStack.EMPTY : data.getGarment(slot);
   }

   // A pouch or bag counts as worn whether it hangs from the belt or the charm slot,
   // so both look and behave the same.
   public static ItemStack findWornPouch(Player player, java.util.function.Predicate<ItemStack> test) {
      ItemStack belt = getWorn(player, IGarmentData.BELT);
      if (test.test(belt)) return belt;
      return findCharmPouch(player, test);
   }

   // Only what sits in the charm slot, for the charm key.
   public static ItemStack findCharmPouch(Player player, java.util.function.Predicate<ItemStack> test) {
      com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData charm =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData.get(player);
      ItemStack worn = charm == null ? ItemStack.EMPTY : charm.getCharm();
      return test.test(worn) ? worn : ItemStack.EMPTY;
   }

   // Only what hangs from the belt, for the belt key.
   public static ItemStack findBeltPouch(Player player, java.util.function.Predicate<ItemStack> test) {
      ItemStack belt = getWorn(player, IGarmentData.BELT);
      return test.test(belt) ? belt : ItemStack.EMPTY;
   }
}
