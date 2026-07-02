package com.paleimitations.schoolsofmagic.common.entity.capabilities.effect_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class EffectVariables implements INBTSerializable<CompoundTag>, IEffectVariables {

   private float sneezeOffset = 0.0F;
   private float timeToSneeze = 0.0F;
   private float returnFromSneeze = 0.0F;

   public EffectVariables() {
   }

   @Override
   public void startSneeze(float sneezeOffset, float timeToSneeze, float returnFromSneeze) {
      this.sneezeOffset = sneezeOffset;
      this.timeToSneeze = timeToSneeze;
      this.returnFromSneeze = returnFromSneeze;
   }

   @Override
   public float getSneezeOffset() {
      return this.sneezeOffset;
   }

   @Override
   public void setSneezeOffset(float sneezeOffset) {
      this.sneezeOffset = sneezeOffset;
   }

   @Override
   public float getTimeToSneeze() {
      return this.timeToSneeze;
   }

   @Override
   public void setTimeToSneeze(float timeToSneeze) {
      this.timeToSneeze = timeToSneeze;
   }

   @Override
   public float getReturnFromSneeze() {
      return this.returnFromSneeze;
   }

   @Override
   public void setReturnFromSneeze(float returnFromSneeze) {
      this.returnFromSneeze = returnFromSneeze;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      writeNBT(nbt);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      readNBT(nbt);
   }

   public void writeNBT(CompoundTag nbt) {
      nbt.putFloat("sneezeOffset", this.sneezeOffset);
      nbt.putFloat("timeToSneeze", this.timeToSneeze);
      nbt.putFloat("returnFromSneeze", this.returnFromSneeze);
   }

   public void readNBT(CompoundTag nbt) {
      this.sneezeOffset = nbt.getFloat("sneezeOffset");
      this.timeToSneeze = nbt.getFloat("timeToSneeze");
      this.returnFromSneeze = nbt.getFloat("returnFromSneeze");
   }
}
