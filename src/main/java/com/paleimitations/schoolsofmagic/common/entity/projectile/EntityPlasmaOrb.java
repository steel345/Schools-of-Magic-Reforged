package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.awt.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityPlasmaOrb extends AbstractSpellShot {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityPlasmaOrb.class, EntityDataSerializers.INT);
   private static final EntityDataAccessor<Boolean> ORBITING =
      SynchedEntityData.defineId(EntityPlasmaOrb.class, EntityDataSerializers.BOOLEAN);
   private MobEffectInstance effect;
   private int orbitIndex = 0;
   private int orbitTotal = 3;
   private int orbitLife = 0;

   public EntityPlasmaOrb(EntityType<? extends EntityPlasmaOrb> type, Level level) {
      super(type, level);
   }

   public EntityPlasmaOrb(Level level, LivingEntity thrower) {
      super(EntityRegistry.PLASMA_ORB.get(), level, thrower);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
      this.getEntityData().define(ORBITING, true);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }
   @Override public int getColor() { return this.getEntityData().get(COLOR); }
   @Override public Color getColorColor() { return new Color(this.getColor()); }
   public void setOrbiting(boolean b) { this.getEntityData().set(ORBITING, b); }
   public boolean isOrbiting() { return this.getEntityData().get(ORBITING); }
   public void setEffect(MobEffectInstance effect) { this.effect = effect; }
   public void setOrbitSlot(int index, int total) { this.orbitIndex = index; this.orbitTotal = Math.max(1, total); }

   @Override
   protected float getGravityVelocity() { return 0.0F; }

   public void launch(Vec3 dir, float speed, boolean homing) {
      this.setOrbiting(false);
      this.setDeltaMovement(dir.normalize().scale(speed));
      this.setHoming(homing);
   }

   private void spawnParticles() {
      int c = this.getColor();
      float pr = ((c >> 16) & 0xFF) / 255.0F;
      float pg = ((c >> 8) & 0xFF) / 255.0F;
      float pb = (c & 0xFF) / 255.0F;
      com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleOptions core =
         new com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleOptions(pr, pg, pb, 0.9F);
      for (int i = 0; i < 4; i++) {
         double ax = (this.random.nextDouble() - 0.5D) * 0.22D;
         double ay = (this.random.nextDouble() - 0.5D) * 0.22D;
         double az = (this.random.nextDouble() - 0.5D) * 0.22D;
         this.level().addParticle(core, this.getX() + ax, this.getY() + ay, this.getZ() + az,
            ax * 0.05D, ay * 0.05D, az * 0.05D);
      }
      if (!this.isOrbiting()) {
         com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleOptions trail =
            new com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleOptions(pr, pg, pb, 0.6F);
         net.minecraft.world.phys.Vec3 m = this.getDeltaMovement();
         for (int i = 0; i < 3; i++) {
            double f = this.random.nextDouble();
            this.level().addParticle(trail,
               this.getX() - m.x * f, this.getY() - m.y * f, this.getZ() - m.z * f,
               0.0D, 0.0D, 0.0D);
         }
      }
   }

   @Override
   public void tick() {
      if (this.level().isClientSide) { spawnParticles(); }
      if (this.isOrbiting()) {
         if (!this.level().isClientSide) {
            Entity owner = this.getOwner();
            if (!(owner instanceof LivingEntity caster) || ++this.orbitLife > 600) { this.discard(); return; }
            com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData md =
               caster.getCapability(com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
            if (md == null || !(md.getCurrentSpell() instanceof com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom sc)
                  || sc.getShape() != com.paleimitations.schoolsofmagic.common.spells.EnumSpellShape.PLASMA) {
               this.discard();
               return;
            }
            double base = (double) this.orbitIndex / this.orbitTotal * Math.PI * 2.0D;
            double ang = base + this.tickCount * 0.09D;
            double r = 1.35D;
            this.setPos(caster.getX() + Math.cos(ang) * r, caster.getY() + 1.1D, caster.getZ() + Math.sin(ang) * r);
            this.setDeltaMovement(Vec3.ZERO);
         }
         super.baseTick();
         return;
      }
      super.tick();
   }

   @Override
   public void performSpell(HitResult result) {
      if (result instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity le && le != this.getOwner()) {
         if (this.effect != null) le.addEffect(new MobEffectInstance(this.effect));
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setColor(compound.getInt("Color"));
      this.setOrbiting(compound.getBoolean("Orbiting"));
      this.orbitIndex = compound.getInt("OrbitIndex");
      this.orbitTotal = Math.max(1, compound.getInt("OrbitTotal"));
      this.effect = compound.contains("Effect") ? MobEffectInstance.load(compound.getCompound("Effect")) : null;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("Color", this.getColor());
      compound.putBoolean("Orbiting", this.isOrbiting());
      compound.putInt("OrbitIndex", this.orbitIndex);
      compound.putInt("OrbitTotal", this.orbitTotal);
      if (this.effect != null) compound.put("Effect", this.effect.save(new CompoundTag()));
   }
}
