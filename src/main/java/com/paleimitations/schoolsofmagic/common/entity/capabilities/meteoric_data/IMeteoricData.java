package com.paleimitations.schoolsofmagic.common.entity.capabilities.meteoric_data;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public interface IMeteoricData {
   UUID getCreator();

   void setCreator(UUID var1);

   boolean isMeteor();

   void setMeteor(boolean var1);

   boolean doesExplode();

   void setDoesExplode(boolean var1);

   boolean isSmokey();

   void setSmokey(boolean var1);

   float getExplosionSize();

   void setExplosionSize(float var1);

   float getDamage();

   void setDamage(float var1);

   Vec3 getMotion();

   void setMotion(Vec3 var1);

   BlockPos getStartPos();

   void setStartPos(BlockPos var1);

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);

   boolean getFallBack();

   void setFallBack(boolean var1);
}
