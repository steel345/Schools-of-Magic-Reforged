package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public enum MetalArmorMaterial implements ArmorMaterial {
   SILVER("silver", new int[]{2, 5, 6, 2}, 15, 0, 12),
   COPPER("copper", new int[]{2, 5, 6, 2}, 15, 60, 0),
   BRONZE("bronze", new int[]{1, 3, 5, 2}, 7, 65, 25),
   BRASS("brass", new int[]{3, 6, 7, 3}, 10, 0, 9),
   STEEL("steel", new int[]{1, 4, 5, 1}, 15, 0, 9);

   private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};

   private final String metal;
   private final int[] protection;
   private final int durabilityMultiplier;
   private final int durabilityBonus;
   private final int enchantment;

   MetalArmorMaterial(String metal, int[] protection, int durabilityMultiplier, int durabilityBonus, int enchantment) {
      this.metal = metal;
      this.protection = protection;
      this.durabilityMultiplier = durabilityMultiplier;
      this.durabilityBonus = durabilityBonus;
      this.enchantment = enchantment;
   }

   @Override
   public int getDurabilityForType(ArmorItem.Type type) {
      return BASE_DURABILITY[type.getSlot().getIndex()] * this.durabilityMultiplier + this.durabilityBonus;
   }

   @Override
   public int getDefenseForType(ArmorItem.Type type) {
      return this.protection[type.getSlot().getIndex()];
   }

   @Override
   public int getEnchantmentValue() {
      return this.enchantment;
   }

   @Override
   public SoundEvent getEquipSound() {
      return SoundEvents.ARMOR_EQUIP_IRON;
   }

   @Override
   public Ingredient getRepairIngredient() {
      return Ingredient.EMPTY;
   }

   @Override
   public String getName() {
      return "som:metal_" + this.metal;
   }

   @Override
   public float getToughness() {
      return 0.0F;
   }

   @Override
   public float getKnockbackResistance() {
      return 0.0F;
   }
}
