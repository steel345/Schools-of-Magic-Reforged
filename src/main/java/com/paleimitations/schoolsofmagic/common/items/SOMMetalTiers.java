package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class SOMMetalTiers {
   public static final Tier SILVER = make(250, 6.0F, 2.0F, 2, 12);
   public static final Tier COPPER = make(310, 6.0F, 2.0F, 2, 0);
   public static final Tier BRONZE = make(97, 12.0F, 0.0F, 0, 22);
   public static final Tier BRASS = make(166, 6.0F, 3.0F, 2, 14);
   public static final Tier STEEL = make(250, 6.0F, 1.0F, 2, 14);

   private static Tier make(int uses, float speed, float dmg, int level, int ench) {
      return new Tier() {
         @Override
         public int getUses() {
            return uses;
         }

         @Override
         public float getSpeed() {
            return speed;
         }

         @Override
         public float getAttackDamageBonus() {
            return dmg;
         }

         @Override
         public int getLevel() {
            return level;
         }

         @Override
         public int getEnchantmentValue() {
            return ench;
         }

         @Override
         public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
         }
      };
   }
}
