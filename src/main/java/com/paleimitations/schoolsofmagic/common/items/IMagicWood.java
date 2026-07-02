package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;

public interface IMagicWood extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumMagicWood.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMagicWood.values()[meta].getSerializedName();
   }
}
