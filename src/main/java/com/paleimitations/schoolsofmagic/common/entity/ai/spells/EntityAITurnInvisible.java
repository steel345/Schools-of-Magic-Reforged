package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityUnicorn;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class EntityAITurnInvisible extends Goal {
   private static final double NOTICE_RANGE = 16.0D;

   protected int spellWarmup;
   protected int spellCooldown;
   protected final EntityUnicorn unicorn;

   public EntityAITurnInvisible(EntityUnicorn unicorn) {
      this.unicorn = unicorn;
   }

   private Player threat() {
      Player player = this.unicorn.level().getNearestPlayer(this.unicorn, NOTICE_RANGE);
      if (player == null || player.isSpectator() || !player.isAlive()) return null;
      return player;
   }

   @Override
   public boolean canUse() {
      if (this.unicorn.isTamed() || this.unicorn.isVehicle()) return false;
      if (this.unicorn.tickCount < this.spellCooldown) return false;
      if (this.unicorn.hasEffect(MobEffects.INVISIBILITY)) return false;
      return this.threat() != null;
   }

   @Override
   public boolean canContinueToUse() {
      return this.spellWarmup > 0;
   }

   @Override
   public void start() {
      this.spellWarmup = 30;
      this.spellCooldown = this.unicorn.tickCount + 1200;
      this.unicorn.playSound(SoundEvents.ILLUSIONER_CAST_SPELL, 0.4F, 1.4F);
   }

   @Override
   public void tick() {
      --this.spellWarmup;
      if (this.spellWarmup == 0) {
         this.unicorn.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 600));
      }
   }
}
