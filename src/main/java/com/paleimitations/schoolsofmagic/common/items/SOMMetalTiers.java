package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class SOMMetalTiers {
   public static final MetalTier SILVER = new MetalTier(EnumMetal.SILVER, 250, 6.0F, 2.0F, 2, 12);
   public static final MetalTier COPPER = new MetalTier(EnumMetal.COPPER, 310, 6.0F, 2.0F, 2, 0);
   public static final MetalTier BRONZE = new MetalTier(EnumMetal.BRONZE, 97, 12.0F, 0.0F, 0, 22);
   public static final MetalTier BRASS  = new MetalTier(EnumMetal.BRASS, 166, 6.0F, 3.0F, 2, 14);
   public static final MetalTier STEEL  = new MetalTier(EnumMetal.STEEL, 250, 6.0F, 1.0F, 2, 14);

   public static class MetalTier implements Tier {
      private final EnumMetal metal;
      private final int uses;
      private final float speed;
      private final float damage;
      private final int level;
      private final int enchantment;

      MetalTier(EnumMetal metal, int uses, float speed, float damage, int level, int enchantment) {
         this.metal = metal;
         this.uses = uses;
         this.speed = speed;
         this.damage = damage;
         this.level = level;
         this.enchantment = enchantment;
      }

      public EnumMetal getMetalType() { return this.metal; }

      public ItemStack getIngot() {
         ItemStack ingot = new ItemStack(ItemRegistry.ingot.get());
         ingot.setDamageValue(this.metal.getIndex());
         return ingot;
      }

      @Override public int getUses() { return this.uses; }
      @Override public float getSpeed() { return this.speed; }
      @Override public float getAttackDamageBonus() { return this.damage; }
      @Override public int getLevel() { return this.level; }
      @Override public int getEnchantmentValue() { return this.enchantment; }
      @Override public Ingredient getRepairIngredient() { return Ingredient.of(this.getIngot()); }
   }

   public static boolean mendsWith(Tier tier, ItemStack repair) {
      return tier instanceof MetalTier metal
         && repair.is(ItemRegistry.ingot.get())
         && repair.getDamageValue() == metal.getMetalType().getIndex();
   }

   public static boolean isModIngot(ItemStack stack) {
      return stack.is(ItemRegistry.ingot.get());
   }
}
