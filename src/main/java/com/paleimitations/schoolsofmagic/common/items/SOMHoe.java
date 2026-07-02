package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class SOMHoe extends HoeItem {
   public SOMHoe(Tier tier, int attackDamage, float attackSpeed, Item.Properties props) {
      super(tier, attackDamage, attackSpeed, props);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return this == ItemRegistry.hoe_light.get() ? true : super.isFoil(stack);
   }
}
