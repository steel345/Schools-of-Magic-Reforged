package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.item.DyeColor;

public interface IColorType extends IItemMetaHandler {
   @Override
   default int getDamage() {
      return DyeColor.values().length;
   }

   @Override
   default String handleMeta(int meta) {
      return DyeColor.values()[meta].getSerializedName();
   }
}
