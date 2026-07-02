package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.common.entity.EntityToad;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.CapabilityQuestData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.quests.IQuestData;
import com.paleimitations.schoolsofmagic.common.quests.Quest;
import com.paleimitations.schoolsofmagic.common.quests.Task;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityToadAIMate extends Goal {
   private final EntityToad animal;
   private final Class<? extends EntityToad> mateClass;
   Level world;
   private EntityToad targetMate;
   int spawnBabyDelay;
   double moveSpeed;

   public EntityToadAIMate(EntityToad animal, double speedIn) {
      this.animal = animal;
      this.world = animal.level();
      this.mateClass = EntityToad.class;
      this.moveSpeed = speedIn;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      if (!this.animal.isInLove()) {
         return false;
      }
      this.targetMate = (EntityToad)this.getNearbyMate();
      return this.targetMate != null;
   }

   @Override
   public boolean canContinueToUse() {
      return this.targetMate.isAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
   }

   @Override
   public void stop() {
      this.targetMate = null;
      this.spawnBabyDelay = 0;
   }

   @Override
   public void tick() {
      this.animal.getLookControl().setLookAt(this.targetMate, 10.0F, (float)this.animal.getMaxHeadXRot());
      this.animal.getNavigation().moveTo(this.targetMate, this.moveSpeed);
      ++this.spawnBabyDelay;
      if (this.spawnBabyDelay >= 60 && this.animal.distanceToSqr(this.targetMate) < 9.0) {
         this.pregnant();
      }
   }

   private Animal getNearbyMate() {
      List<? extends EntityToad> list = this.world.getEntitiesOfClass(this.mateClass, this.animal.getBoundingBox().inflate(8.0));
      double d0 = Double.MAX_VALUE;
      Animal entityanimal = null;
      for (Animal entityanimal1 : list) {
         if (!this.animal.canMate(entityanimal1) || !(this.animal.distanceToSqr(entityanimal1) < d0)) continue;
         entityanimal = entityanimal1;
         d0 = this.animal.distanceToSqr(entityanimal1);
      }
      return entityanimal;
   }

   private void pregnant() {
      ServerPlayer entityplayermp = this.animal.getLoveCause();
      if (entityplayermp == null && this.targetMate.getLoveCause() != null) {
         entityplayermp = this.targetMate.getLoveCause();
      }
      if (entityplayermp != null) {
         entityplayermp.awardStat(Stats.ANIMALS_BRED);
         CriteriaTriggers.BRED_ANIMALS.trigger(entityplayermp, this.animal, this.targetMate, null);
         IQuestData data = entityplayermp.getCapability(CapabilityQuestData.CAP).orElse(null);
         if (data != null) {
            for (Quest q : data.getQuests()) {
               for (Task t : q.tasks) {
                  if (t.getName() == null || !t.getName().equalsIgnoreCase("breed_toads")) continue;
                  t.checkEvent((Player)entityplayermp, null);
               }
            }
         }
      }
      this.animal.setAge(6000);
      this.targetMate.setAge(6000);
      this.animal.resetLove();
      this.targetMate.resetLove();
      RandomSource random = this.animal.getRandom();
      this.animal.setPregnant(true);
      for (int i = 0; i < 7; ++i) {
         double d0 = random.nextGaussian() * 0.02;
         double d1 = random.nextGaussian() * 0.02;
         double d2 = random.nextGaussian() * 0.02;
         double d3 = random.nextDouble() * (double)this.animal.getBbWidth() * 2.0 - (double)this.animal.getBbWidth();
         double d4 = 0.5 + random.nextDouble() * (double)this.animal.getBbHeight();
         double d5 = random.nextDouble() * (double)this.animal.getBbWidth() * 2.0 - (double)this.animal.getBbWidth();
         this.world.addParticle(ParticleTypes.HEART, this.animal.getX() + d3, this.animal.getY() + d4, this.animal.getZ() + d5, d0, d1, d2);
      }
      if (this.world.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_DOMOBLOOT)) {
         this.world.addFreshEntity(new ExperienceOrb(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
      }
   }
}
