package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumMagicWood implements StringRepresentable, IMagicWood {
   ASH(0),
   ELDER(1),
   PINE(2),
   WILLOW(3),
   YEW(4),
   VERDE(5);

   private final int index;

   private EnumMagicWood(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumMagicWood getFromIndex(int index) {
      for (EnumMagicWood type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return ASH;
   }
}
