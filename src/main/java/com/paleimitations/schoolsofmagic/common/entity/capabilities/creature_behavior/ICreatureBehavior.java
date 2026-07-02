package com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface ICreatureBehavior {

   CompoundTag serializeNBT();

   void deserializeNBT(CompoundTag var1);

   boolean isAngry();

   LivingEntity getAngerTarget(Level var1);

   UUID getAngerTargetUUID();

   void setAngerTarget(UUID var1);

   void setAngry(boolean var1);

   void setAngryAndUpdate(boolean var1, UUID var2);

   boolean isAfraid();

   LivingEntity getFearTarget(Level var1);

   UUID getFearTargetUUID();

   void setFearTarget(UUID var1);

   void setAfraid(boolean var1);

   void setAfraidAndUpdate(boolean var1, UUID var2);

   boolean isInfatuated();

   LivingEntity getInfatuationTarget(Level var1);

   UUID getInfatuationTargetUUID();

   void setInfatuationTarget(UUID var1);

   void setInfatuated(boolean var1);

   void setInfatuatedAndUpdate(boolean var1, UUID var2);

   boolean isLoyal();

   LivingEntity getLoyaltyTarget(Level var1);

   UUID getLoyaltyTargetUUID();

   void setLoyaltyTarget(UUID var1);

   void setLoyal(boolean var1);

   void setLoyalAndUpdate(boolean var1, UUID var2);

   boolean isSitting();

   void setSitting(boolean var1);

   boolean useTimer();

   void setUseTimer(boolean var1);

   int getTimer();

   void setTimer(int var1);

   boolean isPassive();

   LivingEntity getPassiveTarget(Level var1);

   UUID getPassiveTargetUUID();

   void setPassiveTarget(UUID var1);

   void setPassive(boolean var1);

   void setPassiveAndUpdate(boolean var1, UUID var2);

   boolean isAsleep();

   LivingEntity getSleepCause(Level var1);

   UUID getSleepCauseUUID();

   void setSleepCause(UUID var1);

   void setAsleep(boolean var1);

   void setAsleepAndUpdate(boolean var1, UUID var2);

   boolean isOnTargetTeam(Entity var1, UUID var2);

   boolean shouldUpdateClient();

   void setShouldUpdateClient(boolean var1);
}
