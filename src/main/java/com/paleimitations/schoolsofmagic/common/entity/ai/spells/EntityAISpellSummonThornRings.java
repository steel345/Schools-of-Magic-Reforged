package com.paleimitations.schoolsofmagic.common.entity.ai.spells;

import com.paleimitations.schoolsofmagic.common.entity.EntityMagician;
import com.paleimitations.schoolsofmagic.common.entity.projectile.EntityThornRing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityAISpellSummonThornRings extends EntityAIUseSpell {
   public EntityAISpellSummonThornRings(EntityMagician magician) {
      super(magician);
   }

   @Override
   public boolean canUse() {
      return super.canUse() && this.magician.getRandom().nextInt(20) == 0;
   }

   @Override
   protected int getCastingTime() {
      return 20;
   }

   @Override
   protected int getCastingInterval() {
      return 100 + (25 - 5 * this.magician.getLevel());
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
            this.spawnThorns(
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
            this.spawnThorns(
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
            this.spawnThorns(
               this.magician.getX() + (double)Mth.cos(f) * d2,
               this.magician.getZ() + (double)Mth.sin(f) * d2,
               d0,
               d1,
               f - 45.0F + 90.0F * (float)(l % 2),
               j
            );
         }
      }
   }

   private void spawnThorns(double x, double z, double yMin, double yMax, float rotation, int delay) {
      BlockPos blockpos = BlockPos.containing(x, yMax, z);
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
      } while (blockpos.getY() >= Mth.floor(yMin) - 1);

      if (flag) {
         EntityThornRing entitythorn = new EntityThornRing(
            this.magician.level(), x, (double)blockpos.getY() + d0, z, rotation, delay, this.magician
         );
         this.magician.level().addFreshEntity(entitythorn);
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
