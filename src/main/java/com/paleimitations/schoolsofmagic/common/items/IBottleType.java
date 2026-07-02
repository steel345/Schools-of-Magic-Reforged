package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;

public interface IBottleType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumBottle.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumBottle.values()[meta].getSerializedName();
   }
}
