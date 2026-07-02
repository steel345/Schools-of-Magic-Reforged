package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class PotionStench extends MobEffect {
   public PotionStench(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      int j = 30 >> amplifier;
      return j > 0 ? duration % j == 0 : true;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      Random rand = new Random();
      Level world = entityLivingBaseIn.level();
      if (!(world instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
         return;
      }

      for (int i = 0; i <= 6; i++) {
         double alfa = rand.nextDouble() * 2.0 * Math.PI;
         double beta = rand.nextDouble() * 2.0 * Math.PI;
         double gamma = rand.nextDouble() * 2.0 * Math.PI;
         double distance = 1.5 * Math.pow(rand.nextDouble(), 2.4);
         double x = entityLivingBaseIn.getX() + distance * Math.cos(alfa);
         double z = entityLivingBaseIn.getZ() + distance * Math.cos(beta);
         double y = entityLivingBaseIn.getY() + (double)entityLivingBaseIn.getEyeHeight() + distance * Math.cos(gamma);
         serverLevel.sendParticles(com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.BUG.get(),
            x, y, z, 0, 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 0.5 - rand.nextDouble(), 1.0);
      }
   }
}
