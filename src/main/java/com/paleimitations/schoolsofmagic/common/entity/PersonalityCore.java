package com.paleimitations.schoolsofmagic.common.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.util.INBTSerializable;

public class PersonalityCore implements INBTSerializable<CompoundTag> {
   public float body;
   public float heart;
   public float mind;
   public float soul;

   public void shuffleAttributesFromJob(RandomSource rand, Job.EnumJob jobType) {
      this.body = jobType.body.minimum + rand.nextFloat() * (jobType.body.maximum - jobType.body.minimum);
      this.heart = jobType.heart.minimum + rand.nextFloat() * (jobType.heart.maximum - jobType.heart.minimum);
      this.mind = jobType.mind.minimum + rand.nextFloat() * (jobType.mind.maximum - jobType.mind.minimum);
      this.soul = 0.4F + ((float)(-Math.pow(rand.nextFloat(), 3.0)) + 1.0F) * 0.6F;
   }

   public void shuffleAttributes(RandomSource rand) {
      this.body = rand.nextFloat();
      this.heart = rand.nextFloat();
      this.mind = rand.nextFloat();
      this.soul = (float)(-Math.pow(rand.nextFloat(), 3.0)) + 1.0F;
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = new CompoundTag();
      nbt.putFloat("body", this.body);
      nbt.putFloat("heart", this.heart);
      nbt.putFloat("mind", this.mind);
      nbt.putFloat("soul", this.soul);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.body = nbt.getFloat("body");
      this.heart = nbt.getFloat("heart");
      this.mind = nbt.getFloat("mind");
      this.soul = nbt.getFloat("soul");
   }
}
