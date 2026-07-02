package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.EntityIntelligent;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityAIBreakBlock extends Goal {
   protected EntityIntelligent entity;
   protected BlockPos blockPos = BlockPos.ZERO;
   protected BlockState blockstate;
   private int breakingTime;
   private int previousBreakProgress = -1;

   public EntityAIBreakBlock(EntityIntelligent entityIn) {
      this.entity = entityIn;
      if (!(entityIn.getNavigation() instanceof GroundPathNavigation)) {
         throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
      }
      this.setFlags(EnumSet.noneOf(Goal.Flag.class));
   }

   @Override
   public boolean canUse() {
      if (this.entity.getTargetblocks().isEmpty()) {
         return false;
      }
      this.blockPos = this.entity.blockPosition();
      if (!this.entity.getTargetblocks().contains(this.entity.level().getBlockState(this.blockPos))) {
         BlockPos dest = BlockPos.ZERO;
         for (BlockPos posit : BlockPos.betweenClosed(this.entity.blockPosition().offset(2, 4, 2), this.entity.blockPosition().offset(-2, -1, -2))) {
            if (!this.entity.getTargetblocks().contains(this.entity.level().getBlockState(posit)) || !this.canBeSeen(this.entity, posit)) continue;
            if (dest == null) {
               dest = posit.immutable();
               continue;
            }
            if (!(Utils.getDistanceDouble((double)posit.getX() + 0.5, (double)posit.getY() + 0.5, (double)posit.getZ() + 0.5, this.entity.getX(), this.entity.getY() + (double)this.entity.getEyeHeight(), this.entity.getZ()) < Utils.getDistanceDouble((double)dest.getX() + 0.5, (double)dest.getY() + 0.5, (double)dest.getZ() + 0.5, this.entity.getX(), this.entity.getY() + (double)this.entity.getEyeHeight(), this.entity.getZ()))) continue;
            dest = posit.immutable();
         }
         this.blockstate = this.entity.level().getBlockState(dest);
         this.blockPos = dest;
      }
      return !this.entity.getTargetblocks().contains(this.entity.level().getBlockState(this.blockPos));
   }

   public boolean canBeSeen(Entity entity, BlockPos pos) {
      Level world = entity.level();
      Vec3 vec3d = new Vec3(entity.getX(), entity.getY() + (double)entity.getEyeHeight(), entity.getZ());
      Vec3 vec3d1 = new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
      BlockHitResult raytraceresult = world.clip(new ClipContext(vec3d, vec3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
      return raytraceresult.getBlockPos().equals(pos);
   }

   @Override
   public void start() {
      this.breakingTime = 0;
   }

   @Override
   public boolean canContinueToUse() {
      double d0 = Utils.getDistanceDouble((double)this.blockPos.getX() + 0.5, (double)this.blockPos.getY() + 0.5, (double)this.blockPos.getZ() + 0.5, this.entity.getX(), this.entity.getY(), this.entity.getZ());
      if (this.breakingTime <= 60 && d0 < 4.0) {
         return true;
      }
      return false;
   }

   @Override
   public void stop() {
      this.entity.level().destroyBlockProgress(this.entity.getId(), this.blockPos, -1);
   }

   @Override
   public void tick() {
      if (this.entity.getRandom().nextInt(20) == 0) {
      }
      ++this.breakingTime;
      int i = (int)((float)this.breakingTime / 60.0F * 10.0F);
      if (i != this.previousBreakProgress) {
         this.entity.level().destroyBlockProgress(this.entity.getId(), this.blockPos, i);
         this.previousBreakProgress = i;
      }
      if (this.breakingTime == 60) {
         this.entity.level().removeBlock(this.blockPos, false);
      }
   }

   private BlockState getBlock(BlockPos pos) {
      return this.entity.level().getBlockState(pos);
   }
}
