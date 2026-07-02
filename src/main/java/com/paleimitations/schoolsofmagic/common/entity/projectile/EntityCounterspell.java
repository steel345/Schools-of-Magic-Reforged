package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCounterspell extends AbstractSpellShot {
   private boolean countered = false;
   private boolean maxLevel = false;

   public void setMaxLevel(boolean maxLevel) {
      this.maxLevel = maxLevel;
   }

   @Override
   public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putBoolean("maxLevel", this.maxLevel);
   }

   @Override
   public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.maxLevel = compound.getBoolean("maxLevel");
   }

   public EntityCounterspell(EntityType<? extends EntityCounterspell> type, Level worldIn) {
      super(type, worldIn);
      this.setNoGravity(true);
   }

   public EntityCounterspell(EntityType<? extends EntityCounterspell> type, Level worldIn, LivingEntity throwerIn) {
      super(type, worldIn, throwerIn);
      this.setNoGravity(true);
   }

   public EntityCounterspell(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.COUNTERSPELL.get(), worldIn, throwerIn);
      this.setNoGravity(true);
   }

   @Override
   public int getColor() {
      return 0xFFD700;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.level() instanceof ServerLevel sl && !this.countered && this.tickCount > 2) {
         List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(1.5D));
         for (Entity target : list) {
            if (target == this || target == this.getThrower()) continue;
            if (target instanceof AbstractSpellShot || target instanceof EntityMagicBeam || target instanceof LivingEntity) {
               this.counter(target);
               break;
            }
         }
         if (!this.countered) {
            sl.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 2, 0.05D, 0.05D, 0.05D, 0.01D);
         }
      }
      if (!this.countered && this.tickCount > 60) {
         this.discard();
      }
   }

   @Override
   public void performSpell(HitResult result) {
      if (result instanceof EntityHitResult ehr && ehr.getEntity() != null) {
         this.counter(ehr.getEntity());
      }
   }

   private void counter(Entity target) {
      if (target == this || target == this.getThrower()) return;
      if (target instanceof AbstractSpellShot shot && shot.getThrower() != this.getThrower()) {
         LivingEntity orig = shot.getThrower();
         if (orig != null) {
            Vec3 dir = orig.getEyePosition().subtract(shot.position()).normalize();
            double sp = Math.max(0.6D, shot.getDeltaMovement().length());
            shot.setDeltaMovement(dir.scale(sp));
            shot.thrower = this.getThrower();
            shot.setOwner(this.getThrower());
            shot.ignoreEntity = this.getThrower();
         } else {
            shot.discard();
         }
         this.disintegrate();
         this.countered = true;
         this.discard();
         return;
      } else if (target instanceof EntityMagicBeam beam) {
         this.cooldown(beam.getCaster());
         beam.discard();
         this.disintegrate();
         this.countered = true;
         this.discard();
         return;
      } else if (target instanceof LivingEntity le) {
         for (EntityMagicBeam b : this.level().getEntitiesOfClass(EntityMagicBeam.class, le.getBoundingBox().inflate(24.0D))) {
            if (b.getCaster() == le) b.discard();
         }
         for (AbstractSpellShot s : this.level().getEntitiesOfClass(AbstractSpellShot.class, le.getBoundingBox().inflate(24.0D))) {
            if (s != this && s.getThrower() == le) {
               Vec3 d = le.getEyePosition().subtract(s.position()).normalize();
               double sp = Math.max(0.6D, s.getDeltaMovement().length());
               s.setDeltaMovement(d.scale(sp));
               s.setOwner(this.getThrower());
               s.thrower = this.getThrower();
               s.ignoreEntity = this.getThrower();
            }
         }
         if (this.maxLevel && le instanceof Player tp) {
            tp.hurt(this.damageSources().indirectMagic(this, this.getThrower()), 1.0F);
            this.drainCharge(tp);
         }
         this.cooldown(le);
         this.disintegrate();
         this.countered = true;
         this.discard();
         return;
      }
   }

   private void drainCharge(Player target) {
      com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData md =
         target.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
      if (md == null) return;
      int tier = md.getCurrentSpell() != null ? md.getCurrentSpell().currentSpellChargeLevel : md.getLargestChargeLevel();
      if (tier < 0) return;
      md.useCharge(tier);
      if (target instanceof net.minecraft.server.level.ServerPlayer sp) {
         com.paleimitations.schoolsofmagic.common.network.PacketUpdateManaData msg =
            new com.paleimitations.schoolsofmagic.common.network.PacketUpdateManaData(sp.getId(), md.serializeNBT());
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY.with(() -> sp), msg);
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> sp), msg);
      }
   }

   private void cooldown(LivingEntity caster) {
      if (caster instanceof Player p) {
         int t = 30;
         if (!p.getMainHandItem().isEmpty()) p.getCooldowns().addCooldown(p.getMainHandItem().getItem(), t);
         if (!p.getOffhandItem().isEmpty()) p.getCooldowns().addCooldown(p.getOffhandItem().getItem(), t);
         try {
            ItemStack ring = com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(p).getRing();
            if (ring != null && !ring.isEmpty()) p.getCooldowns().addCooldown(ring.getItem(), t);
         } catch (Throwable ignored) {
         }
      }
   }

   private void disintegrate() {
      if (this.level() instanceof ServerLevel sl) {
         sl.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 0.6F, 1.6F);
         sl.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY(), this.getZ(), 12, 0.2D, 0.2D, 0.2D, 0.05D);
         sl.sendParticles(ParticleTypes.WITCH, this.getX(), this.getY(), this.getZ(), 8, 0.2D, 0.2D, 0.2D, 0.02D);
      }
   }
}
