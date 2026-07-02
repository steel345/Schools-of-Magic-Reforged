package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumMagicType implements StringRepresentable {
   PYROMANCY(0),
   HELIOMANCY(1),
   AEROMANCY(2),
   GEOMANCY(3),
   ANIMANCY(4),
   ELECTROMANCY(5),
   HYDROMANCY(6),
   CRYOMANCY(7),
   HIEROMANCY(8),
   CHAOTICS(9),
   AURAMANCY(10),
   ASTROMANCY(11),
   INFERNALITY(12),
   SPECTROMANCY(13),
   UMBRAMANCY(14),
   NECROMANCY(15);

   private final int index;

   private EnumMagicType(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumMagicType getFromIndex(int index) {
      for (EnumMagicType type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return PYROMANCY;
   }
}
