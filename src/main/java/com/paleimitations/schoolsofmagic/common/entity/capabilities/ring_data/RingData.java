package com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class RingData implements IRingData {
   private ItemStack ring = ItemStack.EMPTY;
   private int spellSlots = defaultMask();

   private static int defaultMask() {
      try {
         return 1 << (com.paleimitations.schoolsofmagic.common.compat.SOMConfig.default_ring_spell_slot.get() - 1);
      } catch (Exception e) {
         return 1;
      }
   }

   @Override
   public ItemStack getRing() {
      return this.ring;
   }

   @Override
   public void setRing(ItemStack stack) {
      this.ring = stack == null ? ItemStack.EMPTY : stack;
   }

   @Override
   public int getSpellSlots() {
      return this.spellSlots;
   }

   @Override
   public void setSpellSlots(int mask) {
      this.spellSlots = mask & 0x1FF;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (!this.ring.isEmpty()) {
         tag.put("ring", this.ring.save(new CompoundTag()));
      }
      tag.putInt("spellSlots", this.spellSlots);
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag tag) {
      this.ring = tag.contains("ring") ? ItemStack.of(tag.getCompound("ring")) : ItemStack.EMPTY;
      this.spellSlots = tag.contains("spellSlots") ? (tag.getInt("spellSlots") & 0x1FF) : 1;
   }
}
