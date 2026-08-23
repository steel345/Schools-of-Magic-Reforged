package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MetalArmor extends ArmorItem {
   public MetalArmor(ArmorMaterial material, ArmorItem.Type type, Item.Properties props) {
      super(material, type, props);
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      if (!repair.is(ItemRegistry.ingot.get())) {
         return super.isValidRepairItem(toRepair, repair);
      }
      return this.getMaterial() instanceof MetalArmorMaterial metal
         && repair.getDamageValue() == metal.getMetalType().getIndex();
   }
}
