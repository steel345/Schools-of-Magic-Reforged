package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood2;

public interface IMagicWood2 extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return EnumMagicWood2.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMagicWood2.values()[meta].getSerializedName();
   }
}
