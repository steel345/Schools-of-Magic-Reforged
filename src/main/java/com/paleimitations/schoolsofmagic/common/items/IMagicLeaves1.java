package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;

public interface IMagicLeaves1 extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return 4;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMagicWood.values()[meta].getSerializedName();
   }
}
