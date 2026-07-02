package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityAISpellSummonFangs extends EntityAIUseSpell {
   public EntityAISpellSummonFangs(EntityMagician magician) {
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
      return 400;
   }

   @Override
   protected void castSpell() {
      LivingEntity entitylivingbase = this.magician.getTarget();
      double d0 = Math.min(entitylivingbase.getY(), this.magician.getY());
      double d1 = Math.max(entitylivingbase.getY(), this.magician.getY()) + 1.0;
      float f = (float)Mth.atan2(entitylivingbase.getZ() - this.magician.getZ(), entitylivingbase.getX() - this.magician.getX());
      if (this.magician.distanceToSqr(entitylivingbase) < 9.0) {
         for (int i = 0; i < 5; i++) {
            float f1 = f + (float)i * (float) Math.PI * 0.4F;
            this.spawnFangs(
               this.magician.getX() + (double)Mth.cos(f1) * 1.5,
               this.magician.getZ() + (double)Mth.sin(f1) * 1.5,
               d0,
               d1,
               f1,
               0
            );
         }

         for (int k = 0; k < 8; k++) {
            float f2 = f + (float)k * (float) Math.PI * 2.0F / 8.0F + (float) (Math.PI * 2.0 / 5.0);
            this.spawnFangs(
               this.magician.getX() + (double)Mth.cos(f2) * 2.5,
               this.magician.getZ() + (double)Mth.sin(f2) * 2.5,
               d0,
               d1,
               f2,
               3
            );
         }
      } else {
         for (int l = 0; l < 16; l++) {
            double d2 = 1.25 * (double)(l + 1);
            int j = 1 * l;
            this.spawnFangs(
               this.magician.getX() + (double)Mth.cos(f) * d2,
               this.magician.getZ() + (double)Mth.sin(f) * d2,
               d0,
               d1,
               f,
               j
            );
         }
      }
   }

   private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
      BlockPos blockpos = BlockPos.containing(p_190876_1_, p_190876_7_, p_190876_3_);
      boolean flag = false;
      double d0 = 0.0;

      do {
         BlockState blockstate;
         if (!this.magician.level().getBlockState(blockpos).isFaceSturdy(this.magician.level(), blockpos, Direction.UP)
            && (blockstate = this.magician.level().getBlockState(blockpos.below())).isFaceSturdy(this.magician.level(), blockpos.below(), Direction.UP)) {
            if (!this.magician.level().isEmptyBlock(blockpos)) {
               VoxelShape voxelshape = this.magician.level().getBlockState(blockpos).getCollisionShape(this.magician.level(), blockpos);
               if (!voxelshape.isEmpty()) {
                  d0 = voxelshape.max(Direction.Axis.Y);
               }
            }

            flag = true;
            break;
         }

         blockpos = blockpos.below();
      } while (blockpos.getY() >= Mth.floor(p_190876_5_) - 1);

      if (flag) {
         EvokerFangs entityevokerfangs = new EvokerFangs(
            this.magician.level(), p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, this.magician
         );
         this.magician.level().addFreshEntity(entityevokerfangs);
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
