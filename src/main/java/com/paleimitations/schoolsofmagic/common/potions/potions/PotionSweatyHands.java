package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PotionSweatyHands extends MobEffect {
   public PotionSweatyHands(MobEffectCategory category, int color) {
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
      ItemStack stack = null;
      Level world = entityLivingBaseIn.level();
      if (rand.nextInt(6) == 3) {
         stack = entityLivingBaseIn.getMainHandItem().copy();
         if (stack != null && !world.isClientSide) {
            ItemEntity itemEntity = new ItemEntity(
               world, entityLivingBaseIn.getX(), entityLivingBaseIn.getY(), entityLivingBaseIn.getZ(), stack
            );
            itemEntity.setPickUpDelay(30);
            world.addFreshEntity(itemEntity);
            entityLivingBaseIn.getMainHandItem().setCount(0);
         }
      }
   }
}
