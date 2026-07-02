package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumGemState;

public interface IGemState extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumGemState.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumGemState.values()[meta].getSerializedName();
   }
}
