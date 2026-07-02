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

   public boolean isBuyable(SpellNotes notes, boolean desperate) {
      if (desperate && this.magicValue.inRange(notes.magicValue())) {
         return true;
      } else if (this.magicianUnits.inRange(notes.magicianUnits)
         && this.spellUnits.inRange(notes.spellUnits)
         && this.ritualUnits.inRange(notes.ritualUnits)
         && this.potionUnits.inRange(notes.potionUnits)
         && (!this.spark || notes.spark >= 0)) {
         for (int i = 0; i < 6; i++) {
            if (!this.schoolUnits[i].inRange(notes.schoolUnits[i])) {
               return false;
            }
         }

         for (int ix = 0; ix < 16; ix++) {
            if (!this.elementUnits[ix].inRange(notes.elementUnits[ix])) {
               return false;
            }
         }

         if (this.spark) {
            for (int ixx = 0; ixx < 16; ixx++) {
               if (this.elementUnits[ixx].maximum > 0.0F && ixx == notes.spark) {
                  return true;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public ItemStack getItemStack() {
      return this.spellNoteOption;
   }

   public int getTierValue() {
      return this.tierValue;
   }
}
