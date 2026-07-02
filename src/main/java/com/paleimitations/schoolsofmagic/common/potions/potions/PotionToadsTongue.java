package com.paleimitations.schoolsofmagic.common.potions.potions;

import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

public class PotionToadsTongue extends MobEffect {
   public PotionToadsTongue(MobEffectCategory category, int color) {
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
         for (ItemEntity entity : world.getEntitiesOfClass(ItemEntity.class, entityLivingBaseIn.getBoundingBox().inflate((double)(10 + 2 * amplifier)))) {
            double curPosX = entity.getX();
            double curPosY = entity.getY();
            double curPosZ = entity.getZ();
            double endPosX = entityLivingBaseIn.getX();
            double endPosY = entityLivingBaseIn.getY();
            double endPosZ = entityLivingBaseIn.getZ();
            double x = -(Math.abs(curPosX - endPosX) * (curPosX - endPosX)) / ((Math.abs(curPosX - endPosX) + 0.1) * 40.0);
            double y = -(Math.abs(curPosY - endPosY) * (curPosY - endPosY)) / ((Math.abs(curPosY - endPosY) + 0.1) * 30.0);
            double z = -(Math.abs(curPosZ - endPosZ) * (curPosZ - endPosZ)) / ((Math.abs(curPosZ - endPosZ) + 0.1) * 40.0);
            entity.push(x, y, z);
         }
      }
   }
}
