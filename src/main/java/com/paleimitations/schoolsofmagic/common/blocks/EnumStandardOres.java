package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum EnumStandardOres implements StringRepresentable {
   COAL("oreCoal"),
   DIAMOND("oreDiamond"),
   EMERALD("oreEmerald"),
   GOLD("oreGold"),
   IRON("oreIron"),
   LAPIS("oreLapis"),
   SILVER("oreSilver"),
   COPPER("oreCopper");

   private final String oreDictionary;

   private EnumStandardOres(String oreDictionary) {
      this.oreDictionary = oreDictionary;
   }

   public static EnumStandardOres getByOreDictionary(String oreDictionaryIn) {
      for (EnumStandardOres value : values()) {
         if (value.getOreDictionary().equals(oreDictionaryIn)) {
            return value;
         }
      }

      return COAL;
   }

   public String getOreDictionary() {
      return this.oreDictionary;
   }

   @Override
   public String getSerializedName() {
      return this.name().toLowerCase();
   }

   public int getIndex() {
      return this.ordinal();
   }

   public static EnumStandardOres getFromIndex(int index) {
      for (EnumStandardOres value : values()) {
         if (value.ordinal() == index) {
            return value;
         }
      }

      return COAL;
   }
}
