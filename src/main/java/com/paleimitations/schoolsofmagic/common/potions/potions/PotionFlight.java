package com.paleimitations.schoolsofmagic.common.potions.potions;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PotionFlight extends MobEffect {
   public PotionFlight(MobEffectCategory category, int color) {
      super(category, color);
   }

   public boolean isInstantenous() {
      return false;
   }

   public List<ItemStack> getCurativeItems() {
      return Lists.newArrayList();
   }

   public boolean isDurationEffectTick(int duration, int amplifier) {
      return true;
   }

   public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
      super.applyEffectTick(entityLivingBaseIn, amplifier);
      if (entityLivingBaseIn instanceof Player) {
         Player player = (Player)entityLivingBaseIn;
         if (!player.getAbilities().mayfly) {
            player.getAbilities().mayfly = true;
         }
      }
   }
}
