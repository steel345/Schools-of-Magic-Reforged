package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumMetal implements StringRepresentable {
   SILVER(0),
   COPPER(2),
   BRONZE(4),
   BRASS(6),
   STEEL(8),
   TENEBRIUM(9);

   private final int id;

   private EnumMetal(int id) {
      this.id = id;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.id;
   }

   public static EnumMetal getFromIndex(int index) {
      for (EnumMetal type : values()) {
         if (type.id == index) {
            return type;
         }
      }

      return SILVER;
   }
}
