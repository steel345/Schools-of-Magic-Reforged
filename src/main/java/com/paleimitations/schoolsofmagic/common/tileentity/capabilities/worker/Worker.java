package com.paleimitations.schoolsofmagic.common.tileentity.capabilities.worker;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class Worker implements IWork, INBTSerializable<CompoundTag> {
   private int cooldown = 0;
   private int maxCooldown;
   private boolean reversed;
   private Runnable doWork;
   private Runnable workDone;

   public Worker(int maxCooldown, boolean reversed, Runnable doWork, Runnable workDone) {
      this.reversed = reversed;
      this.maxCooldown = maxCooldown;
      this.doWork = doWork;
      this.workDone = workDone;
   }

   public Worker setMaxCooldown(int maxCooldown) {
      this.maxCooldown = maxCooldown;
      return this;
   }

   public int getMaxCooldown() {
      return this.maxCooldown;
   }

   public int getCooldown() {
      return this.cooldown;
   }

   public void setCooldown(int cooldown) {
      if (this.cooldown < this.maxCooldown) {
         this.cooldown = cooldown;
      }
   }

   @Override
   public int getWorkDone() {
      return this.cooldown;
   }

   @Override
   public int getMaxWork() {
      return this.maxCooldown;
   }

   @Override
   public void doWork() {
      if (this.reversed) {
         this.cooldown--;
      } else {
         this.cooldown++;
      }

      this.doWork.run();
      if (this.cooldown == this.maxCooldown) {
         this.workDone();
      }

      if (this.maxCooldown != 0) {
         this.cooldown = this.cooldown % this.maxCooldown;
      }

      if (this.cooldown < 0) {
         this.cooldown = 0;
      }
   }

   @Override
   public void workDone() {
      this.workDone.run();
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putInt("cooldown", this.cooldown);
      nbt.putInt("maxCooldown", this.maxCooldown);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.cooldown = nbt.getInt("cooldown");

      int saved = nbt.getInt("maxCooldown");
      if (saved > 0) {
         this.maxCooldown = saved;
      }
   }
}
