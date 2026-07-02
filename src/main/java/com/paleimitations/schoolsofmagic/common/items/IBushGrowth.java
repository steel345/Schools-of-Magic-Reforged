package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumBushGrowth;

public interface IBushGrowth extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumBushGrowth.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumBushGrowth.values()[meta].getSerializedName();
   }
}
