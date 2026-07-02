package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class SOMSword extends SwordItem {
   public SOMSword(Tier tier, Item.Properties props) {
      super(tier, 3, -2.4F, props);
   }

   public SOMSword(Tier tier, float attackSpeedModifier, Item.Properties props) {
      super(tier, 3, attackSpeedModifier, props);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return this == ItemRegistry.sword_light.get() ? true : super.isFoil(stack);
   }
}
