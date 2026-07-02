package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres;

public interface IStandardOre extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumStandardOres.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumStandardOres.values()[meta].getSerializedName();
   }
}
