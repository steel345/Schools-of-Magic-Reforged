package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

public class PotionUndead extends MobEffect {
   public PotionUndead(MobEffectCategory category, int color) {
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
      Random rand = new Random();
      Level world = entityLivingBaseIn.level();
      if (!world.isClientSide) {
         for (Entity e : ((ServerLevel)world).getAllEntities()) {
            if (e.getType().getCategory() == MobCategory.MONSTER
               && e instanceof Mob
               && ((Mob)e).isInvertedHealAndHarm()
               && ((Mob)e).getTarget() == entityLivingBaseIn) {
               ((Mob)e).setTarget(null);
               Entity entityIn = null;

               for (Entity e2 : ((ServerLevel)world).getAllEntities()) {
                  if (e2 != entityLivingBaseIn) {
                     entityIn = e2;
                  }
               }

               ((Mob)e).lookAt(entityIn, 0.0F, 0.0F);
            }
         }

         if (world.canSeeSky(entityLivingBaseIn.blockPosition()) && world.isDay() && !entityLivingBaseIn.isInWaterOrRain()) {
            entityLivingBaseIn.setSecondsOnFire(2);
         }

         if (rand.nextInt(250) == 0) {
            if (rand.nextInt(2) == 1) {
               world.playLocalSound(
                  entityLivingBaseIn.getX(),
                  entityLivingBaseIn.getY(),
                  entityLivingBaseIn.getZ(),
                  SoundEvents.ZOMBIE_AMBIENT,
                  SoundSource.HOSTILE,
                  1.0F,
                  rand.nextFloat() * 0.4F + 2.8F,
                  false
               );
            } else {
               world.playLocalSound(
                  entityLivingBaseIn.getX(),
                  entityLivingBaseIn.getY(),
                  entityLivingBaseIn.getZ(),
                  SoundEvents.SKELETON_AMBIENT,
                  SoundSource.HOSTILE,
                  1.0F,
                  rand.nextFloat() * 0.4F + 2.8F,
                  false
               );
            }
         }
      }
   }
}
