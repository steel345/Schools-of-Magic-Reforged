package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;

public interface IMagicLeaves2 extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return 2;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMagicWood.values()[meta + 4].getSerializedName();
   }
}
