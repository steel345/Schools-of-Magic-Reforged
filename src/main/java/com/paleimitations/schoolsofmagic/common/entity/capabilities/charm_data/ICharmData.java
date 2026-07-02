package com.paleimitations.schoolsofmagic.common.entity.capabilities.charm_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface ICharmData {
   ItemStack getCharm();

   void setCharm(ItemStack stack);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag tag);
}
