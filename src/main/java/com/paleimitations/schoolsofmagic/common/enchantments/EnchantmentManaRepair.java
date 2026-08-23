package com.paleimitations.schoolsofmagic.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentManaRepair extends Enchantment {
   public EnchantmentManaRepair() {
      super(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values());
   }

   @Override
   public int getMinCost(int level) {
      return 25;
   }

   @Override
   public int getMaxCost(int level) {
      return 75;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }

   @Override
   public boolean canEnchant(ItemStack stack) {
      return stack.isDamageableItem() && super.canEnchant(stack);
   }
}
