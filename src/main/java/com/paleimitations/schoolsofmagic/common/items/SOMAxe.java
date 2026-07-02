package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class SOMAxe extends AxeItem {
   public SOMAxe(Tier tier, float attackDamage, float attackSpeed, Item.Properties props) {
      super(tier, attackDamage, attackSpeed, props);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return this == ItemRegistry.axe_light.get() ? true : super.isFoil(stack);
   }
}
