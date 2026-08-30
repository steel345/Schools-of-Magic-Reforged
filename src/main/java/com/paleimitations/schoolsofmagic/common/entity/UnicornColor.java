package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.util.RandomSource;

public enum UnicornColor {
   DEFAULT(0xFFFFFF),
   GOLD(0xFFD24A),
   PASTEL_PINK(0xFFC0DC),
   PASTEL_BLUE(0xB6D8FF);

   private final int hair;

   UnicornColor(int hair) {
      this.hair = hair;
   }

   public int getHair() {
      return this.hair;
   }

   public boolean isDefault() {
      return this == DEFAULT;
   }

   public static UnicornColor byId(int id) {
      UnicornColor[] all = values();
      return id >= 0 && id < all.length ? all[id] : DEFAULT;
   }

   public static UnicornColor random(RandomSource random) {
      return byId(random.nextInt(values().length));
   }
}
