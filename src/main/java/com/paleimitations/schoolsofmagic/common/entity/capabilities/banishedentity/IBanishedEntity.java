package com.paleimitations.schoolsofmagic.common.entity.capabilities.banishedentity;

import java.util.Map;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IBanishedEntity {

   Map<CompoundTag, Vec3i> getBanishedEntity();

   void setBanishedEntity(Map<CompoundTag, Vec3i> var1);

   void clear();

   @Deprecated void banishEntity(int var1, CompoundTag var2, int var3);

   default void banishEntity(EntityType<?> type, CompoundTag entityData, int timer) {

      banishEntity(0, entityData, timer);
   }

   void removeEntity(CompoundTag var1);

   void setTimer(int var1, CompoundTag var2, int var3);
}
