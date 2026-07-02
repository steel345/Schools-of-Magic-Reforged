package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.EntityIntelligent;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityAIMoveToTargetBlock extends Goal {
   private final EntityIntelligent creature;
   private final double movementSpeed;
   protected int runDelay;
   private int timeoutCounter;
   private int maxStayTicks;
   protected BlockPos destinationBlock = BlockPos.ZERO;
   private boolean isAboveDestination;
   private final int searchLength;

   public EntityAIMoveToTargetBlock(EntityIntelligent creature, double speedIn, int length) {
      this.creature = creature;
      this.movementSpeed = speedIn;
      this.searchLength = length;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
   }

   protected boolean shouldMoveTo(Level worldIn, BlockPos pos) {
      if (this.creature.getTargetblocks().isEmpty()) {
         return false;
      }
      return this.creature.getTargetblocks().contains(worldIn.getBlockState(pos));
   }

   @Override
   public boolean canUse() {
      if (this.runDelay > 0) {
         --this.runDelay;
         return false;
      }
      this.runDelay = 5 + this.creature.getRandom().nextInt(10);
      return this.searchForDestination();
   }

   @Override
   public boolean canContinueToUse() {
      return this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 100 && this.shouldMoveTo(this.creature.level(), this.destinationBlock);
   }

   @Override
   public void start() {
      this.creature.getNavigation().moveTo((double)this.destinationBlock.getX() + 0.5, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
      this.timeoutCounter = 0;
      this.maxStayTicks = this.creature.getRandom().nextInt(this.creature.getRandom().nextInt(30) + 30) + 60;
   }

   @Override
   public void tick() {
      if (this.creature.distanceToSqr((double)this.destinationBlock.getX() + 0.5, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5) > 1.0) {
         this.isAboveDestination = false;
         ++this.timeoutCounter;
         if (this.timeoutCounter % 10 == 0) {
            this.creature.getNavigation().moveTo((double)this.destinationBlock.getX() + 0.5, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5, this.movementSpeed);
         }
      } else {
         this.isAboveDestination = true;
         --this.timeoutCounter;
      }
   }

   protected boolean getIsAboveDestination() {
      return this.isAboveDestination;
   }

   private boolean searchForDestination() {
      int i = this.searchLength;
      BlockPos blockpos = this.creature.blockPosition();
      BlockPos dest = BlockPos.ZERO;
      for (BlockPos posit : BlockPos.betweenClosed(blockpos.offset(-i, -2, -i), blockpos.offset(i, 2, i))) {
         if (!this.shouldMoveTo(this.creature.level(), posit) || !this.canBeSeen(this.creature, posit)) continue;
         if (dest == null) {
            dest = posit.immutable();
            continue;
         }
         if (!(Utils.getDistanceDouble((double)posit.getX() + 0.5, (double)posit.getY() + 0.5, (double)posit.getZ() + 0.5, this.creature.getX(), this.creature.getY() + (double)this.creature.getEyeHeight(), this.creature.getZ()) < Utils.getDistanceDouble((double)dest.getX() + 0.5, (double)dest.getY() + 0.5, (double)dest.getZ() + 0.5, this.creature.getX(), this.creature.getY() + (double)this.creature.getEyeHeight(), this.creature.getZ()))) continue;
         dest = posit.immutable();
      }
      if (this.shouldMoveTo(this.creature.level(), dest)) {
         this.destinationBlock = dest;
         return true;
      }
      return false;
   }

   public boolean canBeSeen(Entity entity, BlockPos pos) {
      Level world = entity.level();
      Vec3 vec3d = new Vec3(entity.getX(), entity.getY() + (double)entity.getEyeHeight(), entity.getZ());
      Vec3 vec3d1 = new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
      BlockHitResult raytraceresult = world.clip(new ClipContext(vec3d, vec3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
      return raytraceresult.getBlockPos().equals(pos);
   }
}
