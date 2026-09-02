package com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data;

import com.paleimitations.schoolsofmagic.common.items.ItemHerbPouch;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class GarmentSlots {
   public static boolean shows(Player player, int slot) {
      IGarmentData data = CapabilityGarmentData.get(player);
      return data == null || !data.isHidden(slot);
   }

   public static boolean isPlain(ItemStack stack) {
      return stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment;
   }

   private static boolean isKind(ItemStack stack, String kind) {
      if (stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment plain) {
         return kind.equals(plain.kind());
      }
      if (stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment set) {
         return kind.equals(set.kind());
      }
      return false;
   }

   public static boolean isRing(ItemStack stack) {
      return stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing
         || isKind(stack, "ring");
   }

   public static boolean isCrown(ItemStack stack) {
      return stack.is(ItemRegistry.apprentice_crown.get()) || isKind(stack, "crown");
   }

   public static boolean isNecklace(ItemStack stack) {
      return stack.is(ItemRegistry.apprentice_necklace.get()) || isKind(stack, "necklace");
   }

   // a crown or a necklace carries its metal the same two ways a ring does, forged plain into the
   // damage or set with a stone into the tag
   public static com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType metalOf(ItemStack stack) {
      if (stack.isEmpty()) return null;
      if (stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment) {
         int at = stack.getDamageValue();
         if (at < 0 || at >= com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment.METALS.length) return null;
         return com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.valueOf(
            com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment.METALS[at].toUpperCase(java.util.Locale.ROOT));
      }
      if (stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment) {
         return com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment.getMetal(stack);
      }
      return com.paleimitations.schoolsofmagic.common.items.RingItemHelper.getMetal(stack);
   }

   public static com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType gemOf(ItemStack stack) {
      if (stack.isEmpty()) return null;
      if (stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment) {
         return com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment.getGem(stack);
      }
      return com.paleimitations.schoolsofmagic.common.items.RingItemHelper.getGem(stack);
   }

   // carried in the charm slot instead
   public static ItemStack wornCrown(Player player) {
      ItemStack head = getWorn(player, IGarmentData.CROWN);
      if (isCrown(head)) return head;
      return findCharmPouch(player, GarmentSlots::isCrown);
   }

   public static ItemStack wornNecklace(Player player) {
      com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData data =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData.get(player);
      ItemStack neck = data == null ? ItemStack.EMPTY : data.getTalisman();
      if (isNecklace(neck)) return neck;
      return findCharmPouch(player, GarmentSlots::isNecklace);
   }

   public static boolean accepts(int slot, ItemStack stack) {
      if (stack.isEmpty()) return false;
      return switch (slot) {
         case IGarmentData.CROWN -> isCrown(stack);
         case IGarmentData.CAPE -> false;
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
