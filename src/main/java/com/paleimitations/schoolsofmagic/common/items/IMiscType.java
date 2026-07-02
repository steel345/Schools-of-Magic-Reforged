package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;

public interface IMiscType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      int max = 0;
      for (EnumMisc m : EnumMisc.values()) {
         max = Math.max(max, m.getIndex());
      }
      return max + 1;
   }

   @Override
   default String handleMeta(int meta) {
      return EnumMisc.getFromIndex(meta).getSerializedName();
   }

   @Override
   default boolean hasMeta(int meta) {
      for (EnumMisc m : EnumMisc.values()) {
         if (m.getIndex() == meta) {
            return true;
         }
      }
      return false;
   }
}
