package com.paleimitations.schoolsofmagic.common.enchantments;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

public class EnchantmentMagicProtection extends Enchantment {
   public EnchantmentMagicProtection() {
      super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{
         EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
      });
   }

   @Override
   public int getMinCost(int level) {
      return 5 + (level - 1) * 8;
   }

   @Override
   public int getMaxCost(int level) {
      return this.getMinCost(level) + 8;
   }

   @Override
   public int getMaxLevel() {
      return 4;
   }

   @Override
   public int getDamageProtection(int level, DamageSource source) {
      if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return 0;
      }
      return source.is(DamageTypeTags.WITCH_RESISTANT_TO) ? level * 5 : 0;
   }

   @Override
   protected boolean checkCompatibility(Enchantment other) {
      if (other instanceof ProtectionEnchantment || other instanceof EnchantmentMagicProtection) {
         return false;
      }
      return super.checkCompatibility(other);
   }
}
