package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;

public interface IIngredientType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumIngredient.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumIngredient.values()[meta].getSerializedName();
   }
}
