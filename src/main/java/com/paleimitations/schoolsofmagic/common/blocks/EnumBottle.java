package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumBottle implements StringRepresentable {
   FIREBERRY(0), ROSE(1), SUNFLOWER(2), MANDRAKE(3), WORMWOOD(4), STORMTHISTLE(5),
   LOTUS(6), POPPY(7), SNOWBELL(8), JIMSONWEED(9), NIGHTBERRY(10), GRAVEROOT(11), BRAMBLEBERRY(12);

   private final int index;

   EnumBottle(int index) { this.index = index; }

   @Override
   public String getSerializedName() { return this.name().toLowerCase(); }

   public int getIndex() { return this.index; }

   public static EnumBottle getFromIndex(int index) {
      for (EnumBottle t : values()) if (t.index == index) return t;
      return POPPY;
   }
}
