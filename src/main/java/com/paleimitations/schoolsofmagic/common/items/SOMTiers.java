package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public final class SOMTiers {

   public static final Tier OBSIDIAN = new Tier() {
      public int getUses() { return 2000; }
      public float getSpeed() { return 8.0F; }
      public float getAttackDamageBonus() { return 4.0F; }
      public int getLevel() { return 3; }
      public int getEnchantmentValue() { return 22; }
      public Ingredient getRepairIngredient() { return Ingredient.of(ItemRegistry.item_obsidian_shard.get()); }
   };

   public static final Tier LIGHT = new Tier() {
      public int getUses() { return 10; }
      public float getSpeed() { return 8.0F; }
      public float getAttackDamageBonus() { return 3.0F; }
      public int getLevel() { return 3; }
      public int getEnchantmentValue() { return 10; }
      public Ingredient getRepairIngredient() { return Ingredient.EMPTY; }
   };

   public static final Tier BONE_KNIFE = new Tier() {
      public int getUses() { return 240; }
      public float getSpeed() { return 6.0F; }
      public float getAttackDamageBonus() { return 0.0F; }
      public int getLevel() { return 2; }
      public int getEnchantmentValue() { return 14; }
      public Ingredient getRepairIngredient() { return Ingredient.of(Items.BONE); }
   };

   public static final Tier ATHAME = new Tier() {
      public int getUses() { return 430; }
      public float getSpeed() { return 6.0F; }
      public float getAttackDamageBonus() { return 2.0F; }
      public int getLevel() { return 2; }
      public int getEnchantmentValue() { return 14; }
      public Ingredient getRepairIngredient() { return Ingredient.of(ItemRegistry.item_obsidian_shard.get()); }
   };

   private SOMTiers() {}
}
