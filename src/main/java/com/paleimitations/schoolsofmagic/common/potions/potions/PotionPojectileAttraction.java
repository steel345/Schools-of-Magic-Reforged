package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class PotionPojectileAttraction extends MobEffect {
   public PotionPojectileAttraction(MobEffectCategory category, int color) {
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
      entityLivingBaseIn.removeEffect(PotionRegistry.lucky_wind.get());
      if (!world.isClientSide) {
         for (Entity entityIn : ((ServerLevel)world).getAllEntities()) {
            if (entityIn instanceof Projectile && entityIn.distanceTo(entityLivingBaseIn) < 30.0F && !entityIn.onGround()) {
               double curPosX = entityIn.getX();
               double curPosY = entityIn.getY();
               double curPosZ = entityIn.getZ();
               double endPosX = entityLivingBaseIn.getX();
               double endPosY = entityLivingBaseIn.getY() - (double)entityLivingBaseIn.getEyeHeight();
               double endPosZ = entityLivingBaseIn.getZ();
               double x = -(curPosX - endPosX) / (Math.abs(curPosX - endPosX) + 0.1);
               double y = -(curPosY - endPosY) / (Math.abs(curPosY - endPosY) + 0.1);
               double z = -(curPosZ - endPosZ) / (Math.abs(curPosZ - endPosZ) + 0.1);
               entityIn.push(x, y, z);
            }
         }
      }
   }
}
