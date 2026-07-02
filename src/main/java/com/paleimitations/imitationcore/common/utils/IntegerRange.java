package com.paleimitations.imitationcore.common.utils;

public class IntegerRange {
   public int minimum;
   public int maximum;

   public IntegerRange(int min, int max) {
      this.minimum = Math.min(min, max);
      this.maximum = Math.max(min, max);
   }

   public boolean inRangeEndInclusive(int number, boolean endInclusive) {
      return number > this.minimum && number < this.maximum || endInclusive && number >= this.minimum && number <= this.maximum;
   }

   public boolean inRange(int number) {
      return number >= this.minimum && number <= this.maximum;
   }
}
