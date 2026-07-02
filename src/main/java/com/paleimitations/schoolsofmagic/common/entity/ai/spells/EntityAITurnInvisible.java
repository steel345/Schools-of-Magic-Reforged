package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;

public class EntityAITurnInvisible extends Goal {
   protected int spellWarmup;
   protected int spellCooldown;
   protected final EntityUnicorn unicorn;

   public EntityAITurnInvisible(EntityUnicorn unicorn) {
      this.unicorn = unicorn;
   }

   @Override
   public boolean canUse() {
      if (this.unicorn.getTarget() == null || Utils.getDistance(this.unicorn, this.unicorn.getTarget()) > 25.0) {
         return false;
      }
      return this.unicorn.tickCount >= this.spellCooldown && !this.unicorn.hasEffect(MobEffects.INVISIBILITY);
   }

   @Override
   public boolean canContinueToUse() {
      return this.unicorn.getTarget() != null && this.spellWarmup > 0;
   }

   @Override
   public void start() {
      this.spellWarmup = 40;
      this.spellCooldown = this.unicorn.tickCount + 1200;
      this.unicorn.playSound(SoundEvents.ILLUSIONER_CAST_SPELL, 0.1F, 1.0F);
   }

   @Override
   public void tick() {
      --this.spellWarmup;
      if (this.spellWarmup == 0) {
         this.unicorn.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600));
      }
   }
}
