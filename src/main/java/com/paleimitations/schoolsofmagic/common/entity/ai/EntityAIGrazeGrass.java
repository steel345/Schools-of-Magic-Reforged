package com.paleimitations.schoolsofmagic.common.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;

public class EntityAIGrazeGrass extends Goal {
   private static final int GRAZE_TICKS = 50;

   private final PathfinderMob mob;
   private final Level level;
   private BlockPos target;
   private int timer;

   public EntityAIGrazeGrass(PathfinderMob mob) {
      this.mob = mob;
      this.level = mob.level();
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
   }

   private BlockPos findGrass() {
      BlockPos under = this.mob.blockPosition();
      if (this.level.getBlockState(under).is(Blocks.GRASS) || this.level.getBlockState(under).is(Blocks.TALL_GRASS)) {
         return under;
      }
      BlockPos below = under.below();
      return this.level.getBlockState(below).is(Blocks.GRASS_BLOCK) ? below : null;
   }

   @Override
   public boolean canUse() {
      if (this.mob.getRandom().nextInt(this.mob.isBaby() ? 50 : 600) != 0) return false;
      this.target = this.findGrass();
      return this.target != null;
   }

   @Override
   public boolean canContinueToUse() {
      return this.timer > 0 && this.target != null;
   }

   @Override
   public void start() {
      this.timer = GRAZE_TICKS;
      this.mob.getNavigation().stop();
   }

   @Override
   public void stop() {
      this.timer = 0;
      this.target = null;
   }

   @Override
   public void tick() {
      if (this.target == null) return;
      this.mob.getLookControl().setLookAt(
         this.target.getX() + 0.5D, this.target.getY() + 0.6D, this.target.getZ() + 0.5D, 30.0F, 30.0F);

      if (--this.timer > 0) return;

      if (this.level.getBlockState(this.target).is(Blocks.GRASS_BLOCK)) {
         if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            this.level.levelEvent(2001, this.target, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
            this.level.setBlock(this.target, Blocks.DIRT.defaultBlockState(), 2);
         }
      } else if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
         this.level.levelEvent(2001, this.target, Block.getId(this.level.getBlockState(this.target)));
         this.level.destroyBlock(this.target, false);
      }

      this.mob.gameEvent(GameEvent.EAT);
      this.mob.heal(1.0F);
   }

   public int grazeTicksLeft() {
      return this.timer;
   }
}
