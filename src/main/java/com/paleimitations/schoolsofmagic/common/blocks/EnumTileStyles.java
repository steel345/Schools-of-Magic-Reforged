package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumTileStyles implements StringRepresentable {
   FACADE,
   FULL,
   BIG,
   MEDIUM,
   SMALL,
   FISH_SCALE,
   HERRINGBONE,
   HORIZONTAL,
   DIAMOND,
   MIXED,
   BUBBLE,
   DAMAGED;

   private EnumTileStyles() {
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.ordinal();
   }

   public static EnumTileStyles getFromIndex(int index) {
      for (EnumTileStyles type : values()) {
         if (type.ordinal() == index) {
            return type;
         }
      }

      return FULL;
   }
}
