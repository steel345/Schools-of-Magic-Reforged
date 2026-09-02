package com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class GarmentData implements IGarmentData {
   private final ItemStack[] garments = new ItemStack[SLOTS];

   public GarmentData() {
      for (int i = 0; i < SLOTS; i++) this.garments[i] = ItemStack.EMPTY;
   }

   private static boolean valid(int slot) {
      return slot >= 0 && slot < SLOTS;
   }

   @Override
   public ItemStack getGarment(int slot) {
      return valid(slot) ? this.garments[slot] : ItemStack.EMPTY;
   }

   @Override
   public void setGarment(int slot, ItemStack stack) {
      if (!valid(slot)) return;
      this.garments[slot] = stack == null ? ItemStack.EMPTY : stack;
   }

   private int hidden = 0;

   @Override public int getHidden() { return this.hidden; }
   @Override public void setHidden(int mask) { this.hidden = mask; }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putInt("hidden", this.hidden);
      for (int i = 0; i < SLOTS; i++) {
         if (!this.garments[i].isEmpty()) {
            tag.put("garment" + i, this.garments[i].save(new CompoundTag()));
         }
      }
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag tag) {
      this.hidden = tag.getInt("hidden");
      for (int i = 0; i < SLOTS; i++) {
         this.garments[i] = tag.contains("garment" + i)
            ? ItemStack.of(tag.getCompound("garment" + i)) : ItemStack.EMPTY;
      }
   }
}
