package com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class TalismanData implements ITalismanData {
   private ItemStack talisman = ItemStack.EMPTY;

   @Override
   public ItemStack getTalisman() {
      return this.talisman;
   }

   @Override
   public void setTalisman(ItemStack stack) {
      this.talisman = stack == null ? ItemStack.EMPTY : stack;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (!this.talisman.isEmpty()) {
         tag.put("talisman", this.talisman.save(new CompoundTag()));
      }
      return tag;
   }

   @Override
   public void deserializeNBT(CompoundTag tag) {
      this.talisman = tag.contains("talisman") ? ItemStack.of(tag.getCompound("talisman")) : ItemStack.EMPTY;
   }
}
