package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;

public interface IPlantType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumPlantType.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumPlantType.values()[meta].getSerializedName();
   }
}
