package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;

public interface IMetalType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return 16;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMetal.getFromIndex(meta).getSerializedName();
   }
}
