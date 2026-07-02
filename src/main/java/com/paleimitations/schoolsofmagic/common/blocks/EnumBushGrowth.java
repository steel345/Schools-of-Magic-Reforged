package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumBushGrowth implements StringRepresentable {
   DEAD(0), GROWN(1);

   private final int index;

   EnumBushGrowth(int index) { this.index = index; }

   @Override
   public String getSerializedName() { return this.name().toLowerCase(); }

   public int getIndex() { return this.index; }

   public static EnumBushGrowth getFromIndex(int index) {
      for (EnumBushGrowth t : values()) if (t.index == index) return t;
      return GROWN;
   }
}
