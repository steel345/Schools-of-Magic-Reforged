package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.CapabilitySummoned;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.summoned.ISummoned;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Husk;

public class EntityAISpellSummonHusks extends EntityAIUseSpell {
   public EntityAISpellSummonHusks(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 60;
   }

   @Override
   protected int getCastingInterval() {
      return 500;
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

         Husk entity = EntityType.HUSK.create(this.magician.level());
         entity.setPos((double)blockpos.getX() + 0.5, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5);
         entity.setTarget(this.magician.getTarget());
         if (!this.magician.level().isClientSide) {
            this.magician.level().addFreshEntity(entity);
         }

         ICreatureBehavior behavior = entity.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
         if (behavior != null) {
            behavior.setLoyalAndUpdate(true, this.magician.getUUID());
         }
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
