package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood1;

public interface IMagicWood1 extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumMagicWood1.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMagicWood1.values()[meta].getSerializedName();
   }
}
