package com.paleimitations.schoolsofmagic.common.tileentity.capabilities.entitystorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class EntityStorage implements IEntityStorage, INBTSerializable<CompoundTag> {
   protected int entityId;
   protected CompoundTag entityData = new CompoundTag();

   public EntityStorage() {
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("entityId", this.entityId);
      if (!this.entityData.isEmpty()) {
         nbt.put("entityData", this.entityData);
      }

      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.entityId = nbt.getInt("entityId");
      if (nbt.contains("entityData")) {
         this.entityData = nbt.getCompound("entityData");
      }
   }

   @Override
   public int getEntityId() {
      return this.entityId;
   }

   @Override
   public CompoundTag getEntityData() {
      return this.entityData;
   }

   @Override
   public void setEntityId(int id) {
      this.entityId = id;
   }

   @Override
   public void setEntityData(CompoundTag nbt) {
      this.entityData = nbt;
   }
}
