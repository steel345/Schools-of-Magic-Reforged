package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Vex;

public class EntityAISpellSummonVex extends EntityAIUseSpell {
   public EntityAISpellSummonVex(EntityMagician magician) {
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
      return 600;
   }

   @Override
   protected void castSpell() {
      for (int i = 0; i < 3; i++) {
         BlockPos blockpos = this.magician.blockPosition()
            .offset(-2 + this.magician.getRandom().nextInt(5), 1, -2 + this.magician.getRandom().nextInt(5));
         Vex entityvex = EntityType.VEX.create(this.magician.level());
         entityvex.moveTo(blockpos, 0.0F, 0.0F);
         if (this.magician.level() instanceof ServerLevel) {
            entityvex.finalizeSpawn((ServerLevel)this.magician.level(), this.magician.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, null, null);
         }
         entityvex.setOwner(this.magician);
         entityvex.setBoundOrigin(blockpos);
         entityvex.setLimitedLife(20 * (10 + this.magician.getRandom().nextInt(10)));
         this.magician.level().addFreshEntity(entityvex);
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
