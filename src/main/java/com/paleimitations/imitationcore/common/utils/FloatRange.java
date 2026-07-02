package com.paleimitations.imitationcore.common.utils;

public class FloatRange {
   public static final FloatRange EMPTY = new FloatRange(0.0F, 0.0F);
   public float minimum;
   public float maximum;

   public FloatRange(float min, float max) {
      this.minimum = Math.min(min, max);
      this.maximum = Math.max(min, max);
   }

   public boolean inRangeEndInclusive(float number, boolean endInclusive) {
      return this.minimum == 0.0F && this.maximum == 0.0F
         || number > this.minimum && number < this.maximum
         || endInclusive && number >= this.minimum && number <= this.maximum;
   }

   public boolean inRange(float number) {
      return this.minimum == 0.0F && this.maximum == 0.0F || number >= this.minimum && number <= this.maximum;
   }
}
