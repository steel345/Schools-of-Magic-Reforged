package com.paleimitations.schoolsofmagic.common.tileentity.capabilities.entitystorage;

import net.minecraft.nbt.CompoundTag;

public interface IEntityStorage {
   int getEntityId();

   CompoundTag getEntityData();

   void setEntityId(int var1);

   void setEntityData(CompoundTag var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);
}
