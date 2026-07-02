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

public class EntityAIFindAndBreakBlock extends Goal {
   private final EntityIntelligent entity;
   private final double movementSpeed;
   protected int runDelay;
   private int timeoutCounter;
   private int maxStayTicks;
   protected BlockPos blockpos = BlockPos.ZERO;
   private boolean hasArrived;
   private final int searchLength;
   protected BlockState blockstate;
   private int breakingTime;
   private float breakingPoint;
   private int previousBreakProgress = -1;
   private int boredomTimer;

   public EntityAIFindAndBreakBlock(EntityIntelligent creature, double speedIn, int length) {
      this.entity = creature;
      this.movementSpeed = speedIn;
      this.searchLength = length;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
      if (!(creature.getNavigation() instanceof GroundPathNavigation)) {
         throw new IllegalArgumentException("Unsupported mob");
      }
   }

   protected boolean shouldMoveTo(Level worldIn, BlockPos pos) {
      return this.entity.getTargetblocks().contains(worldIn.getBlockState(pos));
   }

   @Override
   public boolean canUse() {
      if (this.entity.getTargetblocks().isEmpty()) {
         return false;
      }
      if (!this.searchForTarget()) {
         return false;
      }
      if (this.runDelay > 0) {
         --this.runDelay;
         return false;
      }
      this.runDelay = 5 + this.entity.getRandom().nextInt(10);
      return true;
   }

   @Override
   public boolean canContinueToUse() {
      if (!this.entity.getTargetblocks().contains(this.blockstate)) {
         return false;
      }
      if ((float)this.breakingTime > this.breakingPoint) {
         return false;
      }
      if (this.boredomTimer == 0) {
         return false;
      }
      return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 100 && this.shouldMoveTo(this.entity.level(), this.blockpos);
   }

   @Override
   public void start() {
      double d0 = Utils.getDistanceDouble((double)this.blockpos.getX() + 0.5, (double)this.blockpos.getY() + 0.5, (double)this.blockpos.getZ() + 0.5, this.entity.getX(), this.entity.getY() + (double)this.entity.getBbHeight() / 2.0, this.entity.getZ());
      this.breakingPoint = this.blockstate.getDestroySpeed(this.entity.level(), this.blockpos) * 5.0F * 20.0F;
      this.timeoutCounter = 0;
      this.breakingTime = 0;
      this.maxStayTicks = (int)((float)this.entity.getRandom().nextInt((int)((float)this.entity.getRandom().nextInt((int)(this.breakingPoint / 2.0F)) + this.breakingPoint / 2.0F)) + this.breakingPoint);
      this.boredomTimer = (int)(d0 / this.movementSpeed * 20.0 + (double)this.maxStayTicks);
   }

   @Override
   public void tick() {
      this.entity.getLookControl().setLookAt((double)this.blockpos.getX() + 0.5, (double)(this.blockpos.getY() + 1), (double)this.blockpos.getZ() + 0.5, 10.0F, (float)this.entity.getMaxHeadXRot());
      this.hasArrived = this.canBeSeen(this.entity, this.blockpos);
      --this.boredomTimer;
      if (!this.hasArrived) {
         ++this.timeoutCounter;
         if (this.timeoutCounter % 10 == 0) {
            this.entity.getNavigation().moveTo((double)this.blockpos.getX() + 0.5, (double)this.blockpos.getY() + 0.5, (double)this.blockpos.getZ() + 0.5, this.movementSpeed);
         }
      } else {
         --this.timeoutCounter;
         if (this.entity.getRandom().nextInt(20) == 0) {
         }
         ++this.breakingTime;
         int i = (int)((float)this.breakingTime / this.breakingPoint * 10.0F);
         if (i != this.previousBreakProgress) {
            this.entity.level().destroyBlockProgress(this.entity.getId(), this.blockpos, i);
            this.previousBreakProgress = i;
         }
         if ((float)this.breakingTime == this.breakingPoint) {
            this.entity.level().destroyBlock(this.blockpos, true);
         }
      }
   }

   protected boolean getIsAboveDestination() {
      return this.hasArrived;
   }

   private boolean searchForTarget() {
      int i = this.searchLength;
      int j = (int)(this.entity.getBbHeight() * 2.0F);
      BlockPos blockpos = this.entity.blockPosition();
      BlockPos dest = BlockPos.ZERO;
      for (BlockPos posit : BlockPos.betweenClosed(blockpos.offset(-i, -j, -i), blockpos.offset(i, j, i))) {
         if (!this.shouldMoveTo(this.entity.level(), posit) || !(Utils.getDistanceDouble((double)posit.getX() + 0.5, (double)posit.getY() + 0.5, (double)posit.getZ() + 0.5, this.entity.getX(), this.entity.getY() + (double)this.entity.getBbHeight() / 2.0, this.entity.getZ()) < Utils.getDistanceDouble((double)dest.getX() + 0.5, (double)dest.getY() + 0.5, (double)dest.getZ() + 0.5, this.entity.getX(), this.entity.getY() + (double)this.entity.getBbHeight() / 2.0, this.entity.getZ()))) continue;
         dest = posit.immutable();
      }
      if (this.shouldMoveTo(this.entity.level(), dest)) {
         this.blockpos = dest;
         this.blockstate = this.entity.level().getBlockState(dest);
         return true;
      }
      return false;
   }

   public boolean canBeSeen(Entity entity, BlockPos pos) {
      Level world = entity.level();
      Vec3 vec3d = new Vec3(entity.getX(), entity.getY() + (double)entity.getEyeHeight(), entity.getZ());
      Vec3 vec3d1 = new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
      BlockHitResult raytraceresult = world.clip(new ClipContext(vec3d, vec3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
      double d0 = Utils.getDistanceDouble((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, entity.getX(), entity.getY() + (double)this.entity.getBbHeight() / 2.0, entity.getZ());
      return raytraceresult.getBlockPos().equals(pos) && d0 < (double)(this.entity.getBbHeight() * 2.0F);
   }
}
