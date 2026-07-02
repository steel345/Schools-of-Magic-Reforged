package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.common.entity.EntityDryad;
import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionSpined extends MobEffect {
   public PotionSpined(MobEffectCategory category, int color) {
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
      if (!world.isClientSide) {
         for (Entity e : ((ServerLevel)world).getAllEntities()) {
            if (e instanceof LivingEntity
               && e.distanceTo(entityLivingBaseIn) < 1.5F
               && e != entityLivingBaseIn
               && ((LivingEntity)e).getEffect(PotionRegistry.spined.get()) == null
               && !(e instanceof EntityDryad)
               && !(e instanceof EntityNobleTree)) {
               e.hurt(world.damageSources().cactus(), 0.5F + 0.5F * (float)amplifier);
            }
         }
      }
   }
}
