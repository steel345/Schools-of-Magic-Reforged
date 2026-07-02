package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;

public interface ICauldronType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumCauldronType.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumCauldronType.values()[meta].getSerializedName();
   }
}
