package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class PotionFlamability extends MobEffect {
   public PotionFlamability(MobEffectCategory category, int color) {
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

      for (BlockPos pos : Lists.newArrayList(
         BlockPos.betweenClosed(
            entityLivingBaseIn.blockPosition()
               .offset(
                  (int)((double)entityLivingBaseIn.getBbWidth() + 2.0),
                  (int)((double)entityLivingBaseIn.getBbHeight() + 2.0),
                  (int)((double)entityLivingBaseIn.getBbWidth() + 2.0)
               ),
            entityLivingBaseIn.blockPosition()
               .offset(
                  (int)((double)(-entityLivingBaseIn.getBbWidth()) - 2.0),
                  (int)((double)(-entityLivingBaseIn.getBbHeight()) - 2.0),
                  (int)((double)(-entityLivingBaseIn.getBbWidth()) - 2.0)
               )
         )
      )) {
         if (world.getBlockState(pos).getBlock() == Blocks.FIRE
            || world.getBlockState(pos).getBlock() == Blocks.LAVA
            || world.getBlockState(pos).getBlock() == Blocks.MAGMA_BLOCK
            || world.getBlockState(pos).getBlock() == Blocks.TORCH
            || world.getBlockState(pos).getBlock() == Blocks.LAVA) {
            entityLivingBaseIn.setSecondsOnFire(5);
         }
      }
   }
}
