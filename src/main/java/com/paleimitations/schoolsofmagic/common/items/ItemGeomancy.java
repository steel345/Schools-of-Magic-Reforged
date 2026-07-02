package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGeomancy extends Item {
   public ItemGeomancy(Item.Properties props) {
      super(props);
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isFoil(ItemStack stack) {
      return this == ItemRegistry.shard_netherstar.get() ? true : super.isFoil(stack);
   }
}
