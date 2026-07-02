package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class PotionProjectileRepulsion extends MobEffect {
   public PotionProjectileRepulsion(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      new Random();
      Level world = entityLivingBaseIn.level();
      entityLivingBaseIn.removeEffect(PotionRegistry.unlucky_wind.get());
      if (!world.isClientSide) {
         for (Entity entityIn : world.getEntitiesOfClass(Entity.class, entityLivingBaseIn.getBoundingBox().inflate(3.0))) {
            if (entityIn instanceof Projectile && !entityIn.onGround()) {
               double curPosX = entityIn.getX();
               double curPosY = entityIn.getY();
               double curPosZ = entityIn.getZ();
               double endPosX = entityLivingBaseIn.getX();
               double endPosY = entityLivingBaseIn.getY();
               double endPosZ = entityLivingBaseIn.getZ();
               double x = -((-curPosX + endPosX) * 30.0) / (Math.abs(-curPosX + endPosX) + 0.1);
               double y = -((-curPosY + endPosY) * 30.0) / (Math.abs(-curPosY + endPosY) + 0.1);
               double z = -((-curPosZ + endPosZ) * 30.0) / (Math.abs(-curPosZ + endPosZ) + 0.1);
               entityIn.push(x, y, z);
            }
         }
      }
   }
}
