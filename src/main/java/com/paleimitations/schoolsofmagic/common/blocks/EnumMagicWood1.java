package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumMagicWood1 implements StringRepresentable, IMagicWood {
   ASH(0),
   ELDER(1),
   PINE(2);

   private final int index;

   private EnumMagicWood1(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumMagicWood1 getFromIndex(int index) {
      for (EnumMagicWood1 type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return ASH;
   }
}
