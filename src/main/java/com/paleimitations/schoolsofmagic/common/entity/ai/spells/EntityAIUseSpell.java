package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class EntityAIUseSpell extends Goal {
   protected int spellWarmup;
   protected int spellCooldown;
   protected final EntityMagician magician;

   public EntityAIUseSpell(EntityMagician magician) {
      this.magician = magician;
   }

   @Override
   public boolean canUse() {
      if (this.magician.getTarget() == null || Utils.getDistance(this.magician, this.magician.getTarget()) > 50.0) {
         return false;
      }
      if (this.magician.isCasting()) {
         return false;
      }
      return this.magician.tickCount >= this.spellCooldown;
   }

   @Override
   public boolean canContinueToUse() {
      return this.magician.getTarget() != null && this.spellWarmup > 0;
   }

   @Override
   public void start() {
      this.spellWarmup = this.getCastingTime();
      this.magician.setSpellTicks(this.getCastingTime());
      this.spellCooldown = this.magician.tickCount + this.getCastingInterval();
      SoundEvent soundevent = this.getSpellPrepareSound();
      if (soundevent != null) {
         this.magician.playSound(soundevent, 1.0F, 1.0F);
      }
      this.magician.setSpellType(this.getSpellType());
   }

   @Override
   public void tick() {
      --this.spellWarmup;
      if (this.spellWarmup == 0) {
         this.castSpell();

         SoundEvent attackSound = this.magician.getAttackSound();
         if (attackSound != null) {
            this.magician.playSound(attackSound, 1.0F, 1.0F);
         }
      }
   }

   protected abstract void castSpell();

   protected abstract int getCastingTime();

   protected abstract int getCastingInterval();

   @Nullable
   protected abstract SoundEvent getSpellPrepareSound();

   protected abstract EntityMagician.EnumSpellType getSpellType();
}
