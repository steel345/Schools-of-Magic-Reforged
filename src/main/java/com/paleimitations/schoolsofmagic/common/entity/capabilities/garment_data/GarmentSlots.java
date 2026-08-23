package com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data;

import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GarmentSlots {
   public static boolean accepts(int slot, ItemStack stack) {
      if (stack.isEmpty()) return false;
      return switch (slot) {
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

   public static ItemStack findWornPouch(Player player, java.util.function.Predicate<ItemStack> test) {
      ItemStack belt = getWorn(player, IGarmentData.BELT);
      if (test.test(belt)) return belt;
      return findCharmPouch(player, test);
   }

   public static ItemStack findCharmPouch(Player player, java.util.function.Predicate<ItemStack> test) {
      com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.ICharmData charm =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data.CapabilityCharmData.get(player);
      ItemStack worn = charm == null ? ItemStack.EMPTY : charm.getCharm();
      return test.test(worn) ? worn : ItemStack.EMPTY;
   }

   public static ItemStack findBeltPouch(Player player, java.util.function.Predicate<ItemStack> test) {
      ItemStack belt = getWorn(player, IGarmentData.BELT);
      return test.test(belt) ? belt : ItemStack.EMPTY;
   }
}
