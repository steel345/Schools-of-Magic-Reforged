package com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior;

import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

public class CreatureBehavior implements ICreatureBehavior, INBTSerializable<CompoundTag> {

   private boolean isAngry;
   private boolean isAfraid;
   private boolean isInfatuated;
   private boolean isLoyal;
   private boolean isPassive;
   private boolean isAsleep;
   private boolean shouldUpdateClient;
   private boolean isSitting;
   private boolean useTimer = false;
   private UUID angerTarget;
   private UUID fearTarget;
   private UUID infatuationTarget;
   private UUID loyaltyTarget;
   private UUID passiveTarget;
   private UUID sleepCause;
   private int timer;

   public CreatureBehavior() {
   }

   @Override
   public boolean isAngry() {
      return this.isAngry;
   }

   @Nullable
   @Override
   public LivingEntity getAngerTarget(Level worldIn) {
      return this.getTarget(worldIn, this.angerTarget);
   }

   @Override
   public UUID getAngerTargetUUID() {
      return this.angerTarget;
   }

   @Override
   public void setAngerTarget(UUID angerTarget) {
      this.angerTarget = angerTarget;
   }

   @Override
   public void setAngry(boolean angry) {
      this.isAngry = angry;
   }

   @Override
   public void setAngryAndUpdate(boolean angry, UUID target) {
      this.isAngry = angry;
      this.angerTarget = target;
      this.setShouldUpdateClient(true);
   }

   @Override
   public boolean isAfraid() {
      return this.isAfraid;
   }

   @Nullable
   @Override
   public LivingEntity getFearTarget(Level worldIn) {
      return this.getTarget(worldIn, this.fearTarget);
   }

   @Override
   public UUID getFearTargetUUID() {
      return this.fearTarget;
   }

   @Override
   public void setFearTarget(UUID fearTarget) {
      this.fearTarget = fearTarget;
   }

   @Override
   public void setAfraid(boolean afraid) {
      this.isAfraid = afraid;
   }

   @Override
   public void setAfraidAndUpdate(boolean afraid, UUID target) {
      this.isAfraid = afraid;
      this.fearTarget = target;
      this.setShouldUpdateClient(true);
   }

   @Override
   public boolean isInfatuated() {
      return this.isInfatuated;
   }

   @Nullable
   @Override
   public LivingEntity getInfatuationTarget(Level worldIn) {
      return this.getTarget(worldIn, this.infatuationTarget);
   }

   @Override
   public UUID getInfatuationTargetUUID() {
      return this.infatuationTarget;
   }

   @Override
   public void setInfatuationTarget(UUID infatuationTarget) {
      this.infatuationTarget = infatuationTarget;
   }

   @Override
   public void setInfatuated(boolean infatuated) {
      this.isInfatuated = infatuated;
   }

   @Override
   public void setInfatuatedAndUpdate(boolean infatuated, UUID target) {
      this.isInfatuated = infatuated;
      this.infatuationTarget = target;
      this.setShouldUpdateClient(true);
   }

   @Override
   public boolean isLoyal() {
      return this.isLoyal;
   }

   @Nullable
   @Override
   public LivingEntity getLoyaltyTarget(Level worldIn) {
      return this.getTarget(worldIn, this.loyaltyTarget);
   }

   @Override
   public UUID getLoyaltyTargetUUID() {
      return this.loyaltyTarget;
   }

   @Override
   public void setLoyaltyTarget(UUID loyaltyTarget) {
      this.loyaltyTarget = loyaltyTarget;
   }

   @Override
   public void setLoyal(boolean loyal) {
      this.isLoyal = loyal;
   }

   @Override
   public void setLoyalAndUpdate(boolean loyal, UUID target) {
      this.isLoyal = loyal;
      this.loyaltyTarget = target;
      this.setShouldUpdateClient(true);
   }

   @Override
   public int getTimer() {
      return this.timer;
   }

   @Override
   public void setTimer(int timer) {
      this.timer = timer;
   }

   @Override
   public boolean useTimer() {
      return this.useTimer;
   }

   @Override
   public void setUseTimer(boolean useTimer) {
      this.useTimer = useTimer;
   }

   @Override
   public boolean isSitting() {
      return this.isSitting;
   }

   @Override
   public void setSitting(boolean isSitting) {
      this.isSitting = isSitting;
   }

   @Override
   public boolean isPassive() {
      return this.isPassive;
   }

   @Nullable
   @Override
   public LivingEntity getPassiveTarget(Level worldIn) {
      return this.getTarget(worldIn, this.passiveTarget);
   }

   @Override
   public UUID getPassiveTargetUUID() {
      return this.passiveTarget;
   }

   @Override
   public void setPassiveTarget(UUID passiveTarget) {
      this.passiveTarget = passiveTarget;
   }

   @Override
   public void setPassive(boolean passive) {
      this.isPassive = passive;
   }

   @Override
   public void setPassiveAndUpdate(boolean passive, UUID target) {
      this.passiveTarget = target;
      this.isPassive = passive;
      this.setShouldUpdateClient(true);
   }

   @Override
   public boolean isAsleep() {
      return this.isAsleep;
   }

   @Nullable
   @Override
   public LivingEntity getSleepCause(Level worldIn) {
      return this.getTarget(worldIn, this.sleepCause);
   }

   @Override
   public UUID getSleepCauseUUID() {
      return this.sleepCause;
   }

   @Override
   public void setSleepCause(UUID sleepCause) {
      this.sleepCause = sleepCause;
   }

   @Override
   public void setAsleep(boolean asleep) {
      this.isAsleep = asleep;
   }

   @Override
   public void setAsleepAndUpdate(boolean asleep, UUID target) {
      if (target != null) {
         this.sleepCause = target;
      }

      this.isAsleep = asleep;
      this.setShouldUpdateClient(true);
   }

   @Override
   public void setShouldUpdateClient(boolean update) {
      this.shouldUpdateClient = update;
   }

   @Override
   public boolean shouldUpdateClient() {
      return this.shouldUpdateClient;
   }

   @Nullable
   public LivingEntity getTarget(Level world, UUID id) {
      try {
         if (id != null) {
            if (world.getPlayerByUUID(id) != null) {
               return world.getPlayerByUUID(id);
            }

            if (Utils.getEntity(world, id) != null && Utils.getEntity(world, id) instanceof LivingEntity) {
               return (LivingEntity) Utils.getEntity(world, id);
            }
         }

         return null;
      } catch (IllegalArgumentException var4) {
         return null;
      }
   }

   @Override
   public boolean isOnTargetTeam(Entity entityIn, UUID id) {

      if (entityIn == null || id == null) {
         return false;
      }

      if (id.equals(entityIn.getUUID())) {
         return true;
      }

      if (entityIn instanceof net.minecraft.world.entity.OwnableEntity ownable && id.equals(ownable.getOwnerUUID())) {
         return true;
      }

      ICreatureBehavior other = entityIn.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      if (other != null && other.isLoyal() && id.equals(other.getLoyaltyTargetUUID())) {
         return true;
      }

      LivingEntity owner = this.getTarget(entityIn.level(), id);
      if (entityIn == owner) {
         return true;
      }
      return owner != null && owner.isAlliedTo(entityIn);
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
      if (this.angerTarget != null) {
         nbt.putString("angerTarget", this.angerTarget.toString());
      }

      if (this.fearTarget != null) {
         nbt.putString("fearTarget", this.fearTarget.toString());
      }

      if (this.infatuationTarget != null) {
         nbt.putString("infatuationTarget", this.infatuationTarget.toString());
      }

      if (this.loyaltyTarget != null) {
         nbt.putString("loyaltyTarget", this.loyaltyTarget.toString());
      }

      if (this.passiveTarget != null) {
         nbt.putString("passiveTarget", this.passiveTarget.toString());
      }

      if (this.sleepCause != null) {
         nbt.putString("sleepCause", this.sleepCause.toString());
      }

      nbt.putBoolean("isAfraid", this.isAfraid);
      nbt.putBoolean("isAngry", this.isAngry);
      nbt.putBoolean("isAsleep", this.isAsleep);
      nbt.putBoolean("isInfatuated", this.isInfatuated);
      nbt.putBoolean("isLoyal", this.isLoyal);
      nbt.putBoolean("isSitting", this.isSitting);
      nbt.putBoolean("useTimer", this.useTimer);
      nbt.putInt("timer", this.timer);
      nbt.putBoolean("isPassive", this.isPassive);
      nbt.putBoolean("shouldUpdateClient", this.shouldUpdateClient);
   }

   public void readNBT(CompoundTag nbt) {
      if (nbt.contains("angerTarget")) {
         this.angerTarget = UUID.fromString(nbt.getString("angerTarget"));
      }

      if (nbt.contains("fearTarget")) {
         this.fearTarget = UUID.fromString(nbt.getString("fearTarget"));
      }

      if (nbt.contains("infatuationTarget")) {
         this.infatuationTarget = UUID.fromString(nbt.getString("infatuationTarget"));
      }

      if (nbt.contains("loyaltyTarget")) {
         this.loyaltyTarget = UUID.fromString(nbt.getString("loyaltyTarget"));
      }

      if (nbt.contains("passiveTarget")) {
         this.passiveTarget = UUID.fromString(nbt.getString("passiveTarget"));
      }

      if (nbt.contains("sleepCause")) {
         this.sleepCause = UUID.fromString(nbt.getString("sleepCause"));
      }

      this.isAfraid = nbt.getBoolean("isAfraid");
      this.isAngry = nbt.getBoolean("isAngry");
      this.isAsleep = nbt.getBoolean("isAsleep");
      this.isInfatuated = nbt.getBoolean("isInfatuated");
      this.isLoyal = nbt.getBoolean("isLoyal");
      this.isSitting = nbt.getBoolean("isSitting");
      this.useTimer = nbt.getBoolean("useTimer");
      this.timer = nbt.getInt("timer");
      this.isPassive = nbt.getBoolean("isPassive");
      this.shouldUpdateClient = nbt.getBoolean("shouldUpdateClient");
   }
}
