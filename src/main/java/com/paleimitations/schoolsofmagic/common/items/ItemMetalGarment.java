package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// a crown, a necklace or a ring. which metal it was beaten out of is the damage value, the same
// way every other set of variants in the mod carries its kind
public class ItemMetalGarment extends Item implements IItemMetaHandler {
   public static final String[] METALS = {
      "iron", "gold", "copper", "brass", "bronze", "silver", "steel", "void"
   };

   private final String kind;

   public ItemMetalGarment(String kind, Properties properties) {
      super(properties.stacksTo(1));
      this.kind = kind;
   }

   public static ItemStack of(net.minecraft.world.item.Item item, String metal) {
      ItemStack stack = new ItemStack(item);
      stack.setDamageValue(indexOf(metal));
      return stack;
   }

   public static int indexOf(String metal) {
      for (int i = 0; i < METALS.length; i++) {
         if (METALS[i].equals(metal)) return i;
      }
      return 0;
   }

   public String kind() {
      return this.kind;
   }

   @Override
   public int getDamage() {
      return METALS.length;
   }

   @Override
   public String handleMeta(int meta) {
      return METALS[Math.floorMod(meta, METALS.length)];
   }

   @Override
   public boolean isDamageable(ItemStack stack) {
      return false;
   }

   @Override
   public boolean isBarVisible(ItemStack stack) {
      return false;
   }

   @Override
   public Component getName(ItemStack stack) {
      String metal = this.handleMeta(stack.getDamageValue());
      return Component.translatable("item.som." + metal + "_" + this.kind);
   }
}
