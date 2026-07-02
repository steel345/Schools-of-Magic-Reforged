package com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public interface ISummoned {
   int getDespawnCountdown();

   void setDespawnCountdown(LivingEntity var1, int var2);

   boolean isSummoned();

   void setSummoned(LivingEntity var1, boolean var2);

   void update(LivingEntity var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
