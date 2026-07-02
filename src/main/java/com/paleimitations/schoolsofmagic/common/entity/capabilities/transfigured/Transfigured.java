package com.paleimitations.schoolsofmagic.common.entity.capabilities.transfigured;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

public class Transfigured implements INBTSerializable<CompoundTag>, ITransfigured {
   protected int entityId;
   protected ResourceLocation entityKey;
   protected CompoundTag entityData = new CompoundTag();

   public Transfigured() {}

   public Transfigured(EntityType<?> type, CompoundTag entityData) {
      this.setEntityType(type);
      this.entityData = entityData;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("entityId", this.entityId);
      if (this.entityKey != null) {
         nbt.putString("entityKey", this.entityKey.toString());
      }
      if (!this.entityData.isEmpty()) {
         nbt.put("entityData", this.entityData);
      }
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.entityId = nbt.getInt("entityId");
      if (nbt.contains("entityKey")) {
         this.entityKey = ResourceLocation.tryParse(nbt.getString("entityKey"));
      } else {
         this.entityKey = null;
      }
      if (nbt.contains("entityData")) {
         this.entityData = nbt.getCompound("entityData");
      }
   }

   @Override public int getEntityId() { return this.entityId; }
   @Override public ResourceLocation getEntityKey() { return this.entityKey; }
   @Override public CompoundTag getEntityData() { return this.entityData; }

   @Override
   public void setEntityId(int id) {
      this.entityId = id;

      this.entityKey = null;
   }

   @Override
   public void setEntityType(EntityType<?> type) {
      this.entityKey = ForgeRegistries.ENTITY_TYPES.getKey(type);

   }

   @Override public void setEntityData(CompoundTag nbt) { this.entityData = nbt; }
}
