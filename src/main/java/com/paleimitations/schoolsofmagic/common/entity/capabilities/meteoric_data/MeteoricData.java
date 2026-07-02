package com.paleimitations.schoolsofmagic.common.entity.capabilities.meteoric_data;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

public class MeteoricData implements IMeteoricData, INBTSerializable<CompoundTag> {
   public boolean isMeteor;
   public boolean doesExplode;
   public boolean isSmokey;
   public boolean fallBack;
   public Vec3 motion = new Vec3(0.0, 0.0, 0.0);
   public float damage;
   public float explosionSize;
   public UUID creator;
   public BlockPos startPos = BlockPos.ZERO;

   @Override
   public boolean getFallBack() {
      return this.fallBack;
   }

   @Override
   public void setFallBack(boolean fallBack) {
      this.fallBack = fallBack;
   }

   @Override
   public BlockPos getStartPos() {
      return this.startPos;
   }

   @Override
   public void setStartPos(BlockPos startPos) {
      this.startPos = startPos;
   }

   @Override
   public UUID getCreator() {
      return this.creator;
   }

   @Override
   public void setCreator(UUID creator) {
      this.creator = creator;
   }

   @Override
   public boolean isSmokey() {
      return this.isSmokey;
   }

   @Override
   public void setSmokey(boolean smokey) {
      this.isSmokey = smokey;
   }

   @Override
   public void setDoesExplode(boolean doesExplode) {
      this.doesExplode = doesExplode;
   }

   @Override
   public boolean doesExplode() {
      return this.doesExplode;
   }

   @Override
   public boolean isMeteor() {
      return this.isMeteor;
   }

   @Override
   public void setMeteor(boolean meteor) {
      this.isMeteor = meteor;
   }

   @Override
   public Vec3 getMotion() {
      return this.motion;
   }

   @Override
   public void setMotion(Vec3 motion) {
      this.motion = motion;
   }

   @Override
   public float getDamage() {
      return this.damage;
   }

   @Override
   public void setDamage(float damage) {
      this.damage = damage;
   }

   @Override
   public float getExplosionSize() {
      return this.explosionSize;
   }

   @Override
   public void setExplosionSize(float explosionSize) {
      this.explosionSize = explosionSize;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("isMeteor", this.isMeteor);
      nbt.putBoolean("doesExplode", this.doesExplode);
      nbt.putBoolean("isSmokey", this.isSmokey);
      nbt.putDouble("motionX", this.motion.x);
      nbt.putDouble("motionY", this.motion.y);
      nbt.putDouble("motionZ", this.motion.z);
      nbt.putDouble("damage", this.damage);
      nbt.putDouble("explosionSize", this.explosionSize);
      if (this.creator != null) {
         nbt.putUUID("creator", this.creator);
      }
      nbt.putLong("startPos", this.startPos.asLong());
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.isMeteor = nbt.getBoolean("isMeteor");
      this.isSmokey = nbt.getBoolean("isSmokey");
      this.doesExplode = nbt.getBoolean("doesExplode");
      this.motion = new Vec3(nbt.getDouble("motionX"), nbt.getDouble("motionY"), nbt.getDouble("motionZ"));
      this.damage = nbt.getFloat("damage");
      this.explosionSize = nbt.getFloat("explosionSize");
      if (nbt.hasUUID("creator")) {
         this.creator = nbt.getUUID("creator");
      }
      this.startPos = BlockPos.of(nbt.getLong("startPos"));
   }
}
