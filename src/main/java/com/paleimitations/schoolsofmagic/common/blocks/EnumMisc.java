package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumMisc implements StringRepresentable {
   SHAMROCK(12),
   HYDRANGEA_FLOWERS(13);

   private final int index;

   private EnumMisc(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumMisc getFromIndex(int index) {
      for (EnumMisc type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return SHAMROCK;
   }
}
