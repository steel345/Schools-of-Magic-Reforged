package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class EntityAISpellGrowFlowersPassive extends EntityAIUseSpell {
   public EntityAISpellGrowFlowersPassive(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (this.magician.isCasting() || this.magician.getTarget() != null) {
         return false;
      }
      return this.magician.tickCount >= this.spellCooldown && this.magician.getRandom().nextInt(50) == 0;
   }

   @Override
   public boolean canContinueToUse() {
      return this.spellWarmup > 0;
   }

   @Override
   protected int getCastingTime() {
      return 40 + (100 - 20 * this.magician.getLevel());
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (300 - 60 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {

   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.GROW_FLOWERS;
   }
}
