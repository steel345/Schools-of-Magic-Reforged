package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class PotionDislocation extends MobEffect {
   public PotionDislocation(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      int j = 20 >> amplifier;
      return j > 0 ? duration % j == 0 : true;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      Random rand = new Random();
      Level world = entityLivingBaseIn.level();
      int i = 10 + 10 * amplifier;
      double newPosX = entityLivingBaseIn.getX() - (double)rand.nextInt(i) + (double)rand.nextInt(i);
      double newPosZ = entityLivingBaseIn.getZ() - (double)rand.nextInt(i) + (double)rand.nextInt(i);
      double newPosY = (double)world.getHeight(Heightmap.Types.MOTION_BLOCKING, (int)newPosX, (int)newPosZ);
      entityLivingBaseIn.moveTo(newPosX, newPosY, newPosZ);
      world.playLocalSound(
         entityLivingBaseIn.getX(),
         entityLivingBaseIn.getY(),
         entityLivingBaseIn.getZ(),
         SoundEvents.ENDERMAN_TELEPORT,
         SoundSource.HOSTILE,
         1.0F,
         rand.nextFloat() * 0.4F + 2.8F,
         false
      );
      world.playLocalSound(newPosX, newPosY, newPosZ, SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0F, rand.nextFloat() * 0.4F + 2.8F, false);
   }
}
