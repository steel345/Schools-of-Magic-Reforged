package com.paleimitations.schoolsofmagic.common.entity.projectile;

import com.paleimitations.schoolsofmagic.common.entity.EntityWebbedCocoon;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;

public class EntityWebProjectile extends ThrowableProjectile {
   public int rotation = 1;
   public Entity shootingEntity;
   private double damage = 1.0;

   public EntityWebProjectile(EntityType<? extends EntityWebProjectile> type, Level worldIn) {
      super(type, worldIn);
   }

   public EntityWebProjectile(Level worldIn) {
      super(EntityRegistry.WEB_PROJECTILE.get(), worldIn);
   }

   public EntityWebProjectile(Level worldIn, LivingEntity throwerIn) {
      super(EntityRegistry.WEB_PROJECTILE.get(), throwerIn, worldIn);
      this.shootingEntity = throwerIn;
   }

   public EntityWebProjectile(Level worldIn, double x, double y, double z) {
      super(EntityRegistry.WEB_PROJECTILE.get(), x, y, z, worldIn);
   }

   public int getRotation() {
      return this.rotation;
   }

   public void setRotation(int rotation) {
      this.rotation = rotation;
   }

   @Override
   protected void defineSynchedData() {
   }

   @Override
   protected void onHitEntity(EntityHitResult result) {
      Entity entity = result.getEntity();
      DamageSource damagesource = this.shootingEntity == null ? this.level().damageSources().thrown(this, this) : this.level().damageSources().thrown(this, this.shootingEntity);
      if (entity != null && entity != this.shootingEntity) {
         if (!(entity instanceof Spider) && !(entity instanceof EntityWebbedCocoon) && entity.hurt(damagesource, 0.0F) && !(entity.getVehicle() instanceof EntityWebbedCocoon)) {
            if (entity instanceof LivingEntity) {
               LivingEntity entitylivingbase = (LivingEntity)entity;
               if (this.shootingEntity instanceof LivingEntity) {
                  EnchantmentHelper.doPostHurtEffects(entitylivingbase, this.shootingEntity);
                  EnchantmentHelper.doPostDamageEffects((LivingEntity)this.shootingEntity, entitylivingbase);
               }
               if (!this.level().isClientSide) {
                  EntityWebbedCocoon cocoon = new EntityWebbedCocoon(this.level(), entitylivingbase);
                  cocoon.copyPosition(entitylivingbase);
                  cocoon.setCocoonWidth(entitylivingbase.getBbWidth() * 1.45F);
                  cocoon.setCocoonHeight(entitylivingbase.getBbHeight() * 0.75F);
                  this.level().addFreshEntity(cocoon);
               }
            }
            this.playSound(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
         } else {
            Vec3 mv = this.getDeltaMovement();
            this.setDeltaMovement(mv.scale(-0.1));
            if (!this.level().isClientSide && mv.lengthSqr() < (double)0.001F) {
               this.discard();
            }
         }
      }
   }

   @Override
   public void playerTouch(Player entityIn) {
      if (!this.level().isClientSide && entityIn != this.shootingEntity) {
         EntityWebbedCocoon cocoon = new EntityWebbedCocoon(this.level(), entityIn);
         cocoon.copyPosition(entityIn);
         cocoon.setCocoonWidth(entityIn.getBbWidth() * 1.45F);
         cocoon.setCocoonHeight(entityIn.getBbHeight() * 0.75F);
         this.level().addFreshEntity(cocoon);
      }
      this.discard();
   }

   @Override
   protected float getGravity() {
      return 0.03F;
   }
}
