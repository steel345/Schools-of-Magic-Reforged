package com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IRingData {
   ItemStack getRing();

   void setRing(ItemStack stack);

   int getSpellSlots();

   void setSpellSlots(int mask);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag tag);
}
