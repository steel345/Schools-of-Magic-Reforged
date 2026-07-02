package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumCauldronType implements StringRepresentable {
   NORMAL(0, 1),
   GOLD(1, 5),
   LION(2, 10);

   private final int index;
   private final int cauldronStrength;

   EnumCauldronType(int index, int cauldronStrength) {
      this.cauldronStrength = cauldronStrength;
      this.index = index;
   }

   public int getCauldronStrength() {
      return this.cauldronStrength;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumCauldronType getFromIndex(int index) {
      for (EnumCauldronType type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }
      return NORMAL;
   }
}
