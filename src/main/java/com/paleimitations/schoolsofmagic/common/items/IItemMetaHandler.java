package com.paleimitations.schoolsofmagic.common.items;

public interface IItemMetaHandler {
   int getDamage();

   String handleMeta(int var1);

   default boolean hasMeta(int meta) {
      return true;
   }
}
