package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;

public interface IMagicType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumMagicType.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMagicType.values()[meta].getSerializedName();
   }
}
