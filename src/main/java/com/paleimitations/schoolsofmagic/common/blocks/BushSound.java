package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public final class BushSound {
   private BushSound() {}

   public static void rustle(Level world, Entity entity) {
      if (entity == null || world == null) return;

      if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4D
            && world.getRandom().nextInt(4) == 0) {
         entity.playSound(SoundEvents.GRASS_STEP, 0.45F,
               0.85F + world.getRandom().nextFloat() * 0.2F);
      }
   }
}
