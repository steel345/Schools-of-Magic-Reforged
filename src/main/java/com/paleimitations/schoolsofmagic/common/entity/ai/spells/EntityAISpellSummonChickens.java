package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIAngryAttack;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Chicken;

public class EntityAISpellSummonChickens extends EntityAIUseSpell {
   public EntityAISpellSummonChickens(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.magician.getLevel() == 0 && this.magician.getRandom().nextInt(40) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 60 + (100 - 20 * this.magician.getLevel());
   }

   @Override
   protected int getCastingInterval() {
      return 200 + (300 - 60 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      for (int i = 0; i < 3; i++) {
         BlockPos blockpos = this.magician.blockPosition()
            .offset(-2 + this.magician.getRandom().nextInt(5), 1, -2 + this.magician.getRandom().nextInt(5));

         while (!this.magician.level().isEmptyBlock(blockpos)) {
            blockpos = this.magician.blockPosition()
               .offset(-2 + this.magician.getRandom().nextInt(5), 1, -2 + this.magician.getRandom().nextInt(5));
         }

         Chicken entity = EntityType.CHICKEN.create(this.magician.level());
         entity.setPos((double)blockpos.getX() + 0.5, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5);
         if (!this.magician.level().isClientSide) {
            this.magician.level().addFreshEntity(entity);
         }

         if (entity.getAttribute(Attributes.ATTACK_DAMAGE) != null) {
            entity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2.0);
         }
         entity.goalSelector.addGoal(1, new EntityAIAngryAttack(entity, 1.0, true));
         entity.setTarget(this.magician.getTarget());
         ISummoned summoned = entity.getCapability(CapabilitySummoned.CAP).orElse(null);
         if (summoned != null) {
            summoned.setSummoned(entity, true);
            summoned.setDespawnCountdown(entity, 200 + this.magician.getRandom().nextInt(200));
         }
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.SUMMON_MINIONS;
   }
}
