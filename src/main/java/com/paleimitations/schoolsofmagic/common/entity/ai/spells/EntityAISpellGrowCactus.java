package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.CapabilityBanishedBlocks;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.IBanishedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EntityAISpellGrowCactus extends EntityAIUseSpell {
   public EntityAISpellGrowCactus(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      if (!super.canUse()) {
         return false;
      }
      return this.magician.level().getBlockState(this.magician.getTarget().blockPosition().below()).getBlock() == Blocks.SAND
         && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (100 - 20 * this.magician.getLevel());
   }

   @Override
   protected void castSpell() {
      LivingEntity base = this.magician.getTarget();
      BlockPos pos = base.blockPosition();

      IBanishedBlocks banished =
            this.magician.level().getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      final int countdown = 1200;

      for (BlockPos posit : BlockPos.betweenClosed(pos.offset(1, 1, 0), pos.offset(-1, 1, 0))) {
         for (int i = 0; i < 3; i++) {
            BlockPos put = posit.below(2 - i);
            if (this.magician.level().getBlockState(put).canBeReplaced()
               && Blocks.CACTUS.defaultBlockState().canSurvive(this.magician.level(), put)
               && !put.equals(pos.below(1 - i))) {
               if (banished != null) {
                  BlockState prev = this.magician.level().getBlockState(put);
                  banished.addSet(put.immutable(), prev, countdown);
               }
               this.magician.level().setBlockAndUpdate(put, Blocks.CACTUS.defaultBlockState());
            }
         }
      }

      for (BlockPos posit : BlockPos.betweenClosed(pos.offset(0, 1, 1), pos.offset(0, 1, -1))) {
         for (int ix = 0; ix < 3; ix++) {
            BlockPos put = posit.below(2 - ix);
            if (this.magician.level().getBlockState(put).canBeReplaced()
               && Blocks.CACTUS.defaultBlockState().canSurvive(this.magician.level(), put)
               && !put.equals(pos.below(1 - ix))) {
               if (banished != null) {
                  BlockState prev = this.magician.level().getBlockState(put);
                  banished.addSet(put.immutable(), prev, countdown);
               }
               this.magician.level().setBlockAndUpdate(put, Blocks.CACTUS.defaultBlockState());
            }
         }
      }
   }

   @Override
   protected SoundEvent getSpellPrepareSound() {
      return SoundEvents.EVOKER_PREPARE_SUMMON;
   }

   @Override
   protected EntityMagician.EnumSpellType getSpellType() {
      return EntityMagician.EnumSpellType.GROW_CACTUS;
   }
}
