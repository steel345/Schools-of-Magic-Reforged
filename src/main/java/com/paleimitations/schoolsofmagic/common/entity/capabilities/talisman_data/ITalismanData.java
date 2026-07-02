package com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ITalismanData {
   ItemStack getTalisman();

   void setTalisman(ItemStack stack);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag tag);
}
