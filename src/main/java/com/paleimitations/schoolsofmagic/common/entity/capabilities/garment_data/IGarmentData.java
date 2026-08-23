package com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IGarmentData {
   int CROWN = 0;
   int CAPE = 1;
   int BELT = 2;
   int GRIMOIRE = 3;
   int SLOTS = 4;

   ItemStack getGarment(int slot);

   void setGarment(int slot, ItemStack stack);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag tag);
}
