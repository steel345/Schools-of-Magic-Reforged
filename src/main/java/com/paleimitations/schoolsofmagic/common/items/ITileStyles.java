package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumTileStyles;

public interface ITileStyles extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumTileStyles.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumTileStyles.values()[meta].getSerializedName();
   }
}
