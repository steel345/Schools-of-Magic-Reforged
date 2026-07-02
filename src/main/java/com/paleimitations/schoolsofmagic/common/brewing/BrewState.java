package com.paleimitations.schoolsofmagic.common.brewing;

public enum BrewState {
   EMPTY,
   WATER_ADDED,
   MODIFIER_INFUSING,
   MODIFIER_INFUSED,
   HERBS_ADDED,
   BREWING,
   COMPLETE;

   public static BrewState byName(String name) {
      if (name == null) return EMPTY;
      try {
         return BrewState.valueOf(name);
      } catch (IllegalArgumentException e) {
         return EMPTY;
      }
   }
}
