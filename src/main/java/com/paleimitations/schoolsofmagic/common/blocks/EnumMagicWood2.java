package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumMagicWood2 implements StringRepresentable, IMagicWood {
   WILLOW(0),
   YEW(1),
   VERDE(2);

   private final int index;

   private EnumMagicWood2(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumMagicWood2 getFromIndex(int index) {
      for (EnumMagicWood2 type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return WILLOW;
   }
}
