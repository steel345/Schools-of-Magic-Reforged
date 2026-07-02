package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumGemState implements StringRepresentable {
   ROUGH(0), POLISHED(1);

   private final int index;
   EnumGemState(int index) { this.index = index; }

   @Override
   public String getSerializedName() { return this.name().toLowerCase(); }

   public int getIndex() { return this.index; }

   public static EnumGemState getFromIndex(int index) {
      for (EnumGemState t : values()) if (t.index == index) return t;
      return ROUGH;
   }
}
