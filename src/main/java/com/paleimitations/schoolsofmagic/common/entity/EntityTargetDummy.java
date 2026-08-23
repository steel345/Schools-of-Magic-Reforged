package com.paleimitations.schoolsofmagic.common.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityTargetDummy extends PathfinderMob {
   private static final int DPS_WINDOW = 100;

   private static class Hit {
      final long tick;
      final float damage;
      Hit(long tick, float damage) { this.tick = tick; this.damage = damage; }
   }

   private final List<Hit> hits = new ArrayList<>();

   public int hitTicks = 0;
   public float hitYaw = 0.0F;
   public float wobbleStrength = 1.0F;
   private long lastReactTick = -1L;

   public EntityTargetDummy(EntityType<? extends PathfinderMob> type, Level level) {
      super(type, level);
   }

   public static AttributeSupplier.Builder createAttributes() {
      return PathfinderMob.createMobAttributes()
         .add(Attributes.MAX_HEALTH, 4.0D)
         .add(Attributes.MOVEMENT_SPEED, 0.0D)
         .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
   }

   @Override
   protected void registerGoals() {
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   protected void doPush(Entity entity) {
   }

   @Override
   protected void pushEntities() {
   }

   @Override
   public void knockback(double strength, double x, double z) {
      if (!this.level().isClientSide && strength > 0.0D) {
         react(yawFromDelta(-x, -z), 0.0F, 0.0F, (float) (strength * 2.5D));
      }
   }

   @Override
   public boolean addEffect(net.minecraft.world.effect.MobEffectInstance instance, Entity source) {
      boolean applied = super.addEffect(instance, source);
      if (applied && !this.level().isClientSide && source != null) {
         react(yawFrom(source), 0.0F, 0.0F);
      }
      return applied;
   }

   private float yawFrom(Entity e) {
      if (e == null) {
         return this.getYRot();
      }
      return yawFromDelta(this.getX() - e.getX(), this.getZ() - e.getZ());
   }

   private static float yawFromDelta(double dx, double dz) {
      return (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
   }

   public void react(float yaw, float damage, float dps) {
      react(yaw, damage, dps, 1.0F);
   }

   public void react(float yaw, float damage, float dps, float wobble) {
      if (this.level().isClientSide || this.isRemoved()) {
         return;
      }
      long now = this.level().getGameTime();
      if (now != this.lastReactTick) {
         this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_STAND_HIT, SoundSource.NEUTRAL, 0.8F, 1.0F);
         this.lastReactTick = now;
      }
      com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
         net.minecraftforge.network.PacketDistributor.NEAR.with(() ->
            new net.minecraftforge.network.PacketDistributor.TargetPoint(
               this.getX(), this.getY(), this.getZ(), 48.0D, this.level().dimension())),
         new com.paleimitations.schoolsofmagic.common.network.PacketDummyDamage(this.getId(), damage, dps, yaw, wobble));
   }

   @Override
   public boolean removeWhenFarAway(double dist) {
      return false;
   }

   @Override
   public boolean canBeLeashed(net.minecraft.world.entity.player.Player player) {
      return false;
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource source) {
      return SoundEvents.ARMOR_STAND_HIT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ARMOR_STAND_BREAK;
   }

   @Override
   protected SoundEvent getAmbientSound() {
      return null;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.hitTicks > 0) {
         this.hitTicks--;
      }
   }

   public float getWobble(float ageInTicks) {
      if (this.hitTicks <= 0) {
         return 0.0F;
      }
      float t = this.hitTicks / 10.0F;
      return Mth.sin(t * (float) Math.PI) * 0.35F * t * Mth.clamp(this.wobbleStrength, 0.4F, 3.0F);
    }

   private AABB baseBox() {
      return new AABB(this.getX() - 0.45D, this.getY(), this.getZ() - 0.45D,
                      this.getX() + 0.45D, this.getY() + 0.2D, this.getZ() + 0.45D);
   }

   private boolean aimedAtBase(DamageSource source) {
      Entity direct = source.getDirectEntity();
      if (direct != null && !(direct instanceof LivingEntity)) {
         return direct.getY() <= this.getY() + 0.2D;
      }
      if (source.getEntity() instanceof LivingEntity attacker) {
         Vec3 eye = attacker.getEyePosition(1.0F);
         Vec3 end = eye.add(attacker.getViewVector(1.0F).scale(6.0D));
         return this.baseBox().clip(eye, end).isPresent();
      }
      return false;
   }

   private float computeDps() {
      long now = this.level().getGameTime();
      this.hits.removeIf(h -> now - h.tick > DPS_WINDOW);
      float sum = 0.0F;
      for (Hit h : this.hits) {
         sum += h.damage;
      }
      return sum / (DPS_WINDOW / 20.0F);
   }

   @Override
   public boolean hurt(DamageSource source, float amount) {
      if (this.level().isClientSide || this.isRemoved()) {
         return false;
      }
      if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) {
         return super.hurt(source, amount);
      }

      if (aimedAtBase(source)) {
         this.breakDummy();
         return true;
      }

      float dps = 0.0F;
      if (amount > 0.0F) {
         this.hits.add(new Hit(this.level().getGameTime(), amount));
         dps = computeDps();
      }

      react(yawFrom(source.getEntity()), Math.max(0.0F, amount), dps);
      return true;
   }

   public void breakDummy() {
      if (!this.level().isClientSide) {
         this.level().playSound(null, this.blockPosition(), SoundEvents.ARMOR_STAND_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
         this.spawnAtLocation(new ItemStack(Items.ARMOR_STAND));
         this.spawnAtLocation(new ItemStack(Items.HAY_BLOCK));
      }
      this.discard();
   }

   @Override
   public void addAdditionalSaveData(CompoundTag tag) {
      super.addAdditionalSaveData(tag);
   }

   @Override
   public void readAdditionalSaveData(CompoundTag tag) {
      super.readAdditionalSaveData(tag);
   }
}
