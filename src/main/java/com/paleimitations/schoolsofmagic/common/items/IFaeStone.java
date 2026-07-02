package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone;

public interface IFaeStone extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumFaeStone.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumFaeStone.values()[meta].getSerializedName();
   }
}
