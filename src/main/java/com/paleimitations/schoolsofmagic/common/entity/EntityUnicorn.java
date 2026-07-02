package com.paleimitations.schoolsofmagic.common.entity;

import com.paleimitations.schoolsofmagic.common.entity.ai.spells.EntityAITurnInvisible;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;

import javax.annotation.Nullable;

public class EntityUnicorn extends AbstractHorse {
   private static final EntityDataAccessor<Boolean> HORN = SynchedEntityData.defineId(EntityUnicorn.class, EntityDataSerializers.BOOLEAN);
   private int regrow = 0;

   public EntityUnicorn(EntityType<? extends AbstractHorse> type, Level level) {
      super(type, level);
   }

   @Override
   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(HORN, true);
   }

   @Override
   protected void registerGoals() {
      super.registerGoals();
      this.goalSelector.addGoal(1, new EntityAITurnInvisible(this));
      this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 8.0F, 2.2, 2.2));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 300, true, false, null));
   }

   public boolean hasHorn() {
      return this.entityData.get(HORN);
   }

   public void setHorn(boolean horn) {
      this.entityData.set(HORN, horn);
   }

   @Override
   public void aiStep() {
      super.aiStep();
      if (!this.hasHorn() && this.tickCount > this.regrow) {
         this.setHorn(true);
      }
   }

   @Override
   public InteractionResult mobInteract(Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.getItem() instanceof ShearsItem && this.hasHorn()) {
         stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
         player.getInventory().add(new ItemStack(ItemRegistry.horn_unicorn.get()));
         this.setHorn(false);
         this.regrow = this.tickCount + 2400 + this.getRandom().nextInt(2400);
         this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
         if (!this.level().isClientSide) {
            com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData data =
               player.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData.CAP).orElse(null);
            if (data != null) {
               for (com.paleimitations.schoolsofmagic.common.quests.Quest q : data.getQuests()) {
                  for (com.paleimitations.schoolsofmagic.common.quests.Task t : q.tasks) {
                     if (t.getName() != null && t.getName().equalsIgnoreCase("harvest_unicorn_horn")) {
                        t.checkEvent(player, this);
                     }
                  }
               }
            }
         }
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Nullable
   @Override
   public AgeableMob getBreedOffspring(net.minecraft.server.level.ServerLevel level, AgeableMob otherParent) {
      return new EntityUnicorn(com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.UNICORN.get(), this.level());
   }

   @Override
   public CompoundTag serializeNBT() {
      CompoundTag nbt = super.serializeNBT();
      nbt.putBoolean("horn", this.hasHorn());
      nbt.putInt("regrow", this.regrow);
      return nbt;
   }

   @Override
   public void deserializeNBT(CompoundTag nbt) {
      this.setHorn(nbt.getBoolean("horn"));
      this.regrow = nbt.getInt("regrow");
      super.deserializeNBT(nbt);
   }

   @Override
   protected SoundEvent getAmbientSound() {
      super.getAmbientSound();
      return SoundEvents.HORSE_AMBIENT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      super.getDeathSound();
      return SoundEvents.HORSE_DEATH;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      super.getHurtSound(damageSourceIn);
      return SoundEvents.HORSE_HURT;
   }

   @Override
   protected SoundEvent getAngrySound() {
      super.getAngrySound();
      return SoundEvents.HORSE_ANGRY;
   }
}
