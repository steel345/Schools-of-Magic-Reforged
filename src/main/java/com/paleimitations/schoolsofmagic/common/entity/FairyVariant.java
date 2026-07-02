package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.util.RandomSource;

public enum FairyVariant {
   WHITE("white", "Moonlit Fairy", 0xC2CDE6, 12),
   ORANGE("orange", "Ember Fairy", 0xF9801D, 9),
   MAGENTA("magenta", "Bloom Fairy", 0xC74EBD, 12),
   YELLOW("yellow", "Sunbeam Fairy", 0xFED83D, 9),
   LIME("lime", "Sprout Fairy", 0x80C71F, 12),
   PINK("pink", "Heart Fairy", 0xF38BAA, 9),
   GRAY("gray", "Dust Fairy", 0x474F52, 9),
   LIGHT_GRAY("light_gray", "Mist Fairy", 0x9D9D97, 6),
   CYAN("cyan", "Tide Fairy", 0x169C9C, 9),
   PURPLE("purple", "Arcane Fairy", 0x8932B8, 6),
   BLUE("blue", "Breeze Fairy", 0x3C44AA, 12),
   BROWN("brown", "Burrow Fairy", 0x835432, 9),
   GREEN("green", "Moss Fairy", 0x5E7C16, 9),
   RED("red", "Spite Fairy", 0xB02E26, 3),
   BLACK("black", "Shadow Fairy", 0x1D1D21, 6);

   public final String id;
   public final String title;
   public final int color;
   public final int spawnWeight;

   FairyVariant(String id, String title, int color, int spawnWeight) {
      this.id = id;
      this.title = title;
      this.color = color;
      this.spawnWeight = spawnWeight;
   }

   public int eggPrimary() {
      return this.color;
   }

   public int eggSecondary() {
      int r = (int) (((this.color >> 16) & 0xFF) * 0.55F);
      int g = (int) (((this.color >> 8) & 0xFF) * 0.55F);
      int b = (int) ((this.color & 0xFF) * 0.55F);
      return (r << 16) | (g << 8) | b;
   }

   private static final FairyVariant[] VALUES = values();

   public static FairyVariant byId(int i) {
      return VALUES[Math.floorMod(i, VALUES.length)];
   }

   public static FairyVariant weightedRandom(RandomSource rand) {
      int total = 0;
      for (FairyVariant v : VALUES) total += v.spawnWeight;
      int roll = rand.nextInt(total);
      for (FairyVariant v : VALUES) {
         roll -= v.spawnWeight;
         if (roll < 0) return v;
      }
      return BLUE;
   }
}
