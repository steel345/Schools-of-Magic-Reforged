package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumFaeStone implements StringRepresentable {
   NORMAL, COBBLE, MOSSY_COBBLE, BRICKS, MOSSY_BRICKS, CRACKED_BRICKS, CHISELED_BRICKS;

   @Override
   public String getSerializedName() { return this.name().toLowerCase(); }

   public int getIndex() { return this.ordinal(); }

   public static EnumFaeStone getFromIndex(int index) {
      for (EnumFaeStone t : values()) if (t.ordinal() == index) return t;
      return NORMAL;
   }
}
