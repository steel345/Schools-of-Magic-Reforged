package com.paleimitations.schoolsofmagic.common.blocks;

public enum EnumCauldronFluid {
   WATER,
   LAVA,
   POWDER_SNOW;

   public static EnumCauldronFluid fromName(String name) {
      for (EnumCauldronFluid f : values()) {
         if (f.name().equals(name)) {
            return f;
         }
      }
      return WATER;
   }
}
