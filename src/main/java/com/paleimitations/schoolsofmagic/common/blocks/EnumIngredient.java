package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumIngredient implements StringRepresentable {
   BIRD_HEART(0),
   VILLAGER_HEART(1),
   BEAR_HEART(2),
   DRAGON_HEART(3),
   DRAGON_CLAW(4),
   BAT_WING(5),
   MONKEY_PAW(6),
   TOAD_TONGUE(7),
   TENTACLE(8),
   FISH_TAIL(9),
   PIG_TAIL(10);

   private final int index;

   private EnumIngredient(int index) {
      this.index = index;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.index;
   }

   public static EnumIngredient getFromIndex(int index) {
      for (EnumIngredient type : values()) {
         if (type.getIndex() == index) {
            return type;
         }
      }

      return BIRD_HEART;
   }
}
