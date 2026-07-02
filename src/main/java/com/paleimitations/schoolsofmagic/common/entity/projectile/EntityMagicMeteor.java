package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.awt.Color;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EntityMagicMeteor extends AbstractSpellShot {
   private static final EntityDataAccessor<Integer> COLOR =
      SynchedEntityData.defineId(EntityMagicMeteor.class, EntityDataSerializers.INT);
   private MobEffectInstance effect;

   public EntityMagicMeteor(EntityType<? extends EntityMagicMeteor> type, Level level) {
      super(type, level);
   }

   public EntityMagicMeteor(Level level, LivingEntity thrower) {
      super(EntityRegistry.MAGIC_METEOR.get(), level, thrower);
   }

   public EntityMagicMeteor(Level level, double x, double y, double z) {
      super(EntityRegistry.MAGIC_METEOR.get(), level, x, y, z);
   }

   @Override
   protected void defineSynchedData() {
      this.getEntityData().define(COLOR, 0xFFFFFF);
   }

   public void setColor(int color) { this.getEntityData().set(COLOR, color); }

   @Override
   public int getColor() { return this.getEntityData().get(COLOR); }

   @Override
   public Color getColorColor() { return new Color(this.getColor()); }

   public void setEffect(MobEffectInstance effect) { this.effect = effect; }

   @Override
   protected float getGravityVelocity() { return 0.06F; }

   @Override
   public void tick() {
      super.tick();
      if (this.level().isClientSide) {
         java.awt.Color col = new java.awt.Color(this.getColor() & 0xFFFFFF);
         java.util.Random rand = new java.util.Random();
         for (int i = 0; i < 2; i++) {
            com.paleimitations.schoolsofmagic.client.effects.EffectHelper.createSmallPuffParticle(this.level(),
               this.getX() + (rand.nextDouble() - 0.5D) * 0.2D,
               this.getY() + (rand.nextDouble() - 0.5D) * 0.2D,
               this.getZ() + (rand.nextDouble() - 0.5D) * 0.2D, 0.16F, col);
         }
      }
   }

   @Override
   public void performSpell(HitResult result) {
      if (!this.level().isClientSide) {
         this.level().explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
         if (this.effect != null) {
            AreaEffectCloud cloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            cloud.setOwner(this.getOwner() instanceof LivingEntity le ? le : null);
            cloud.setRadius(2.5F);
            cloud.setDuration(100);
            cloud.setRadiusOnUse(-0.5F);
            cloud.setWaitTime(5);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
            cloud.setFixedColor(this.getColor());
            cloud.addEffect(new MobEffectInstance(this.effect));
            this.level().addFreshEntity(cloud);
         }
      }
   }

   @Override
   public void readAdditionalSaveData(CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      this.setColor(compound.getInt("Color"));
      this.effect = compound.contains("Effect") ? MobEffectInstance.load(compound.getCompound("Effect")) : null;
   }

   @Override
   public void addAdditionalSaveData(CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("Color", this.getColor());
      if (this.effect != null) compound.put("Effect", this.effect.save(new CompoundTag()));
   }
}
