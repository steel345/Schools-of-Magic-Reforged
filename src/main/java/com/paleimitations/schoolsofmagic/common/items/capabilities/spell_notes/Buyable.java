package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes;

import com.paleimitations.imitationcore.common.utils.FloatRange;
import com.paleimitations.schoolsofmagic.common.registries.BuyableRegistry;
import net.minecraft.world.item.ItemStack;

public class Buyable {
   public final ItemStack spellNoteOption;
   public final FloatRange magicianUnits;
   public final FloatRange spellUnits;
   public final FloatRange ritualUnits;
   public final FloatRange potionUnits;
   public final FloatRange magicValue;
   public final FloatRange[] schoolUnits;
   public final FloatRange[] elementUnits;
   public final boolean spark;
   public int tierValue;
   // what school of magic this belongs to, which is not the same as what it demands to buy
   public final boolean[] ofElement = new boolean[16];
   public final boolean[] ofSchool = new boolean[6];

   public Buyable belongsTo(boolean[] elements, boolean[] schools) {
      if (elements != null) System.arraycopy(elements, 0, this.ofElement, 0, Math.min(elements.length, 16));
      if (schools != null) System.arraycopy(schools, 0, this.ofSchool, 0, Math.min(schools.length, 6));
      return this;
   }

   public Buyable(
      ItemStack stack,
      FloatRange magicianUnits,
      FloatRange spellUnits,
      FloatRange ritualUnits,
      FloatRange potionUnits,
      FloatRange[] schoolUnits,
      FloatRange[] elementUnits,
      boolean spark,
      FloatRange magicValue,
      int tierValue
   ) {
      this.spellNoteOption = stack;
      this.magicianUnits = magicianUnits;
      this.spellUnits = spellUnits;
      this.ritualUnits = ritualUnits;
      this.potionUnits = potionUnits;
      this.schoolUnits = schoolUnits;
      this.elementUnits = elementUnits;
      this.spark = spark;
      this.magicValue = magicValue;
      this.tierValue = tierValue;
      BuyableRegistry.BUYABLES.add(this);
   }

   // floor, not a window. inRange rejected notes that had too much of an element
   private static boolean meets(FloatRange range, float have) {
      if (range.minimum == 0.0F && range.maximum == 0.0F) return true;
      return have >= range.minimum;
   }

   // a note made of nothing but gem dust has no magician or spell units at all, so these floors shut
   // it out of everything. a note worth plenty overall clears them
   private static boolean broad(FloatRange range, float have, float worth) {
      if (meets(range, have)) return true;
      return worth >= range.minimum * 3.0F;
   }

   public boolean isBuyable(SpellNotes notes, boolean desperate) {
      if (desperate && this.magicValue.inRange(notes.magicValue())) {
         return true;
      }
      float worth = notes.magicValue();
      if (!broad(this.magicianUnits, notes.magicianUnits, worth)
            || !broad(this.spellUnits, notes.spellUnits, worth)
            || !broad(this.ritualUnits, notes.ritualUnits, worth)
            || !broad(this.potionUnits, notes.potionUnits, worth)
            || (this.spark && notes.spark < 0)) {
         return false;
      }

      for (int i = 0; i < 6; i++) {
         if (!meets(this.schoolUnits[i], notes.schoolUnits[i])) return false;
      }
      for (int i = 0; i < 16; i++) {
         if (!meets(this.elementUnits[i], notes.elementUnits[i])) return false;
      }
      return true;
   }

   public ItemStack getItemStack() {
      return this.spellNoteOption;
   }

   public int getTierValue() {
      return this.tierValue;
   }
}
