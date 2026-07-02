package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;

public interface IWoodType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumWoodType.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumWoodType.values()[meta].getSerializedName();
   }
}
