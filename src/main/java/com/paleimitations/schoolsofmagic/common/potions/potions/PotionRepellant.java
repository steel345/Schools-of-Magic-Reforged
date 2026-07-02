package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;

public class PotionRepellant extends MobEffect {
   public PotionRepellant(MobEffectCategory category, int color) {
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

      for (Entity e : ((ServerLevel)world).getAllEntities()) {
         if (e instanceof Mob
            && ((Mob)e).getMobType() == MobType.ARTHROPOD
            && e != entityLivingBaseIn
            && e.distanceTo(entityLivingBaseIn) < 5.0F) {
            double x = entityLivingBaseIn.getX() - e.getX();
            double z = entityLivingBaseIn.getZ() - e.getZ();
            ((Mob)e).knockback(0.5F + 0.5F * (float)amplifier, x, z);
         }
      }

      if (entityLivingBaseIn.getMobType() == MobType.ARTHROPOD && entityLivingBaseIn.getEffect(this).getDuration() % 20 == 0) {
         entityLivingBaseIn.hurt(world.damageSources().cactus(), 1.0F);
      }
   }
}
