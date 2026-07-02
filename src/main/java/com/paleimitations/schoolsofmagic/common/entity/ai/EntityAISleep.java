package com.paleimitations.schoolsofmagic.common.entity.ai;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.particles.SOMParticleType;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.CapabilityCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior.ICreatureBehavior;
import java.util.EnumSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class EntityAISleep extends Goal {
   private final PathfinderMob creature;

   public EntityAISleep(PathfinderMob sleepingEntity) {
      this.creature = sleepingEntity;
      this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
   }

   @Override
   public boolean canUse() {
      ICreatureBehavior behavior = this.creature.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      return behavior != null && behavior.isAsleep();
   }

   @Override
   public boolean canContinueToUse() {
      ICreatureBehavior behavior = this.creature.getCapability(CapabilityCreatureBehavior.CAP).orElse(null);
      return behavior != null && behavior.isAsleep();
   }

   @Override
   public void tick() {
      RandomSource rand = this.creature.getRandom();
      if (rand.nextInt(20) == 0 && this.creature.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
         double px = this.creature.getX() - rand.nextDouble() * (double) this.creature.getBbWidth() / 2.0 + rand.nextDouble() * (double) this.creature.getBbWidth() / 2.0;
         double py = this.creature.getY() + (double) this.creature.getEyeHeight() - rand.nextDouble() * (double) this.creature.getBbHeight() / 8.0 + rand.nextDouble() * (double) this.creature.getBbHeight() / 8.0;
         double pz = this.creature.getZ() - rand.nextDouble() * (double) this.creature.getBbWidth() / 2.0 + rand.nextDouble() * (double) this.creature.getBbWidth() / 2.0;
         serverLevel.sendParticles(com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.SNORE.get(), px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
      }
      this.creature.getNavigation().stop();
      double x = this.creature.getX() + Math.cos(Math.toRadians(this.creature.getYRot()));
      double y = this.creature.getY() - 2.0;
      double z = this.creature.getZ() + Math.sin(Math.toRadians(this.creature.getYRot()));
      this.creature.getLookControl().setLookAt(x, y, z, 30.0F, 30.0F);
   }

   @Override
   public boolean isInterruptable() {
      return false;
   }
}
