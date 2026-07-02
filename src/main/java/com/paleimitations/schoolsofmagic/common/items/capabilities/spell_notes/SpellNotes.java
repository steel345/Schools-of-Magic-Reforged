package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public class SpellNotes implements ISpellNotes, INBTSerializable<CompoundTag> {
   public float magicianUnits = 0.0F;
   public float spellUnits = 0.0F;
   public float ritualUnits = 0.0F;
   public float potionUnits = 0.0F;
   public float[] schoolUnits = new float[6];
   public float[] elementUnits = new float[16];
   public int spark = -1;
   public float luck = 0.0F;
   private List<ItemStack> options = Lists.newArrayList();

   public SpellNotes() {
   }

   public float magicValue() {
      float total = this.magicianUnits;
      total += this.spellUnits;
      total += this.ritualUnits;
      total += this.potionUnits;

      for (int i = 0; i < 6; i++) {
         total += this.schoolUnits[i];
      }

      for (int i = 0; i < 16; i++) {
         total += this.elementUnits[i];
      }

      if (this.spark >= 0) {
         total += 50.0F;
      }

      return total;
   }

   @Override
   public List<ItemStack> getOptions() {
      return this.options;
   }

   @Override
   public SpellNotes getSpellNotes() {
      return this;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putFloat("magicianUnits", this.magicianUnits);
      nbt.putFloat("spellUnits", this.spellUnits);
      nbt.putFloat("ritualUnits", this.ritualUnits);
      nbt.putFloat("potionUnits", this.potionUnits);

      for (int i = 0; i < 6; i++) {
         nbt.putFloat("schoolUnit_" + i, this.schoolUnits[i]);
      }

      for (int i = 0; i < 16; i++) {
         nbt.putFloat("elementUnit_" + i, this.elementUnits[i]);
      }

      nbt.putInt("spark", this.spark);
      nbt.putInt("optionSize", this.options.size());
      int m = 0;

      for (ItemStack stack : this.options) {
         nbt.put("option" + m, stack.serializeNBT());
         m++;
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.magicianUnits = nbt.getFloat("magicianUnits");
      this.spellUnits = nbt.getFloat("spellUnits");
      this.ritualUnits = nbt.getFloat("ritualUnits");
      this.potionUnits = nbt.getFloat("potionUnits");

      for (int i = 0; i < 6; i++) {
         this.schoolUnits[i] = nbt.getFloat("schoolUnit_" + i);
      }

      for (int i = 0; i < 16; i++) {
         this.elementUnits[i] = nbt.getFloat("elementUnit_" + i);
      }

      this.spark = nbt.getInt("spark");
      List<ItemStack> list = Lists.newArrayList();

      for (int i = 0; i < nbt.getInt("optionSize"); i++) {
         list.add(ItemStack.of(nbt.getCompound("option" + i)));
      }

      this.options = list;
   }
}
