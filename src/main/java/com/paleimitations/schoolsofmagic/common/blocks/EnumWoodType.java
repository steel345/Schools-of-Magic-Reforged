package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumWoodType implements StringRepresentable {
   OAK(0),
   SPRUCE(1),
   BIRCH(2),
   JUNGLE(3),
   ACACIA(4),
   DARK_OAK(5),
   ASH(6),
   ELDER(7),
   PINE(8),
   WILLOW(9),
   YEW(10),
   VERDE(11);

   private final int index;

   private EnumWoodType(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumWoodType getFromIndex(int index) {
      for (EnumWoodType type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return BIRCH;
   }
}
