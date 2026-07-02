package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PotionGreasySkin extends MobEffect {
   public PotionGreasySkin(MobEffectCategory category, int color) {
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
      ItemStack copy = null;
      Level world = entityLivingBaseIn.level();
      if (rand.nextInt(6) == 3) {
         for (int i = 0; i < Lists.newArrayList(entityLivingBaseIn.getArmorSlots().iterator()).size(); i++) {
            if (Lists.newArrayList(entityLivingBaseIn.getArmorSlots().iterator()).get(i) != null && rand.nextInt(4) == 2) {
               stack = (ItemStack)Lists.newArrayList(entityLivingBaseIn.getArmorSlots().iterator()).get(i);
               copy = ((ItemStack)Lists.newArrayList(entityLivingBaseIn.getArmorSlots().iterator()).get(i)).copy();
            }
         }

         if (stack != null && !world.isClientSide) {
            ItemEntity itemEntity = new ItemEntity(
               world, entityLivingBaseIn.getX(), entityLivingBaseIn.getY(), entityLivingBaseIn.getZ(), copy
            );
            itemEntity.setPickUpDelay(30);
            world.addFreshEntity(itemEntity);
            stack.setCount(0);
         }
      }
   }
}
