package com.paleimitations.schoolsofmagic.common.entity.capabilities.creature_behavior;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIAngryAttack;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIAngryFindTarget;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIAngryRevenge;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIAngryTargetAll;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIFearAvoidAll;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIFearAvoidTarget;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAIInfatuationFollow;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAILoyaltyFollowOwner;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAILoyaltyOwnerHurt;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAILoyaltyOwnerTarget;
import com.paleimitations.schoolsofmagic.common.entity.ai.EntityAISleep;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateCreatureBehavior;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class CapabilityCreatureBehavior {

   public static final Capability<ICreatureBehavior> CAP = CapabilityManager.get(new CapabilityToken<ICreatureBehavior>(){});
   public static final ResourceLocation ID = new ResourceLocation("som", "creature_behavior");

   @Nullable
   public static ICreatureBehavior getHumanFeature(LivingEntity entity) {
      return entity.getCapability(CAP).orElse(null);
   }

   @SubscribeEvent
   public static void attach(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof PathfinderMob) {
         event.addCapability(ID, new Provider());
      }
   }

   @SubscribeEvent
   public static void updateCreature(LivingEvent.LivingTickEvent event) {
      LivingEntity living = event.getEntity();
      if (living instanceof PathfinderMob creature) {
         ICreatureBehavior cap = creature.getCapability(CAP).orElse(null);
         if (cap != null && cap.shouldUpdateClient() && !creature.level().isClientSide) {
            PacketUpdateCreatureBehavior message = new PacketUpdateCreatureBehavior(living.getId(), cap.serializeNBT());

            PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> living), message);
            cap.setShouldUpdateClient(false);
         }
      }
   }

   @SubscribeEvent
   public static void createCreature(EntityJoinLevelEvent event) {
      if (event.getEntity() instanceof PathfinderMob creature) {
         creature.goalSelector.addGoal(1, new EntityAISleep(creature));
         boolean tag = false;

         for (WrappedGoal entry : creature.goalSelector.getAvailableGoals()) {
            if (entry.getGoal() instanceof EntityAIAngryAttack) {
               tag = true;
               return;
            }
         }

         if (!tag) {
            if (creature.getAttribute(Attributes.MOVEMENT_SPEED) != null
               && (creature.getNavigation() instanceof GroundPathNavigation || creature.getNavigation() instanceof FlyingPathNavigation)) {
               double d = creature.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 4.5 < 1.0
                  ? 1.0
                  : (
                     creature.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 4.5 > 2.0
                        ? 2.0
                        : creature.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 4.5
                  );
               creature.goalSelector.addGoal(1, new EntityAIAngryAttack(creature, d * 1.25, true));
               creature.goalSelector.addGoal(1, new EntityAIFearAvoidTarget(creature, LivingEntity.class, 2.0F, d, d * 1.25));
               creature.goalSelector.addGoal(1, new EntityAIFearAvoidAll(creature, LivingEntity.class, 2.0F, d, d * 1.25));
               creature.goalSelector.addGoal(1, new EntityAIInfatuationFollow(creature, d, 2.0F, 15.0F));
               creature.goalSelector.addGoal(1, new EntityAILoyaltyFollowOwner(creature, d, 10.0F, 2.0F));
            }

            creature.targetSelector.addGoal(1, new EntityAIAngryRevenge(creature, false));
            creature.targetSelector.addGoal(1, new EntityAIAngryFindTarget(creature));
            creature.targetSelector.addGoal(1, new EntityAIAngryTargetAll(creature));
            creature.targetSelector.addGoal(1, new EntityAILoyaltyOwnerHurt(creature));
            creature.targetSelector.addGoal(2, new EntityAILoyaltyOwnerTarget(creature));
         }
      }
   }

   @SubscribeEvent
   public static void updateCreatureBehavior(LivingEvent.LivingTickEvent event) {
      if (event.getEntity() instanceof PathfinderMob creature) {
         ICreatureBehavior behavior = creature.getCapability(CAP).orElse(null);
         if (behavior == null) return;
         if (!event.getEntity().level().isClientSide) {
            if (!creature.hasEffect(PotionRegistry.sleep.get()) && !creature.hasEffect(PotionRegistry.paralysis.get())) {
               if (creature.getType().getCategory() == MobCategory.CREATURE
                  && !(creature instanceof Wolf)
                  && !(creature instanceof Ocelot)
                  && creature.getMobType() != MobType.ARTHROPOD
                  && creature.getMobType() != MobType.UNDEAD
                  && creature.getMobType() != MobType.ILLAGER) {
                  if (!creature.level().isDay()
                     && creature.level().dimension().location().equals(net.minecraft.world.level.Level.OVERWORLD.location())
                     && !behavior.isAsleep()
                     && creature.getRandom().nextInt(100) == 0
                     && creature.getKillCredit() == null
                     && creature.getTarget() == null
                     && creature.getLastHurtByMob() == null) {
                     behavior.setAsleep(true);
                     behavior.setShouldUpdateClient(true);
                  }

                  if (creature.level().isDay() && behavior.isAsleep() && creature.getRandom().nextInt(100) == 0
                     || creature.getKillCredit() != null
                     || creature.getTarget() != null
                     || creature.getLastHurtByMob() != null) {
                     behavior.setAsleep(false);
                     behavior.setShouldUpdateClient(true);
                  }
               } else if (creature.getRandom().nextInt(100) == 0 || creature.getTarget() != null || creature.getLastHurtByMob() != null) {
                  behavior.setAsleep(false);
                  behavior.setShouldUpdateClient(true);
               }
            } else if (!behavior.isAsleep()) {
               behavior.setAsleep(true);
               behavior.setShouldUpdateClient(true);
            }

            if (creature.hasEffect(PotionRegistry.bewilderment.get())) {
               if (!behavior.isPassive()) {
                  behavior.setPassive(true);
                  behavior.setShouldUpdateClient(true);
               }
            } else if (behavior.isPassive()) {
               behavior.setPassive(false);
               behavior.setShouldUpdateClient(true);
            }

            if (creature.hasEffect(PotionRegistry.fear.get())) {
               if (!behavior.isAfraid()) {
                  behavior.setAfraid(true);
                  behavior.setShouldUpdateClient(true);
               }
            } else if (behavior.isAfraid()) {
               behavior.setAfraid(false);
               behavior.setShouldUpdateClient(true);
            }

            if (creature.hasEffect(PotionRegistry.infatuation.get())) {
               if (!behavior.isInfatuated()) {
                  behavior.setInfatuated(true);
                  behavior.setShouldUpdateClient(true);
               }
            } else if (behavior.isInfatuated()) {
               behavior.setInfatuated(false);
               behavior.setShouldUpdateClient(true);
            }

            if (behavior.isPassive()) {
               if (behavior.getPassiveTarget(creature.level()) != null) {
                  if (creature.getTarget() == behavior.getPassiveTarget(creature.level())) {
                     creature.setTarget(null);
                  }
               } else if (creature.getTarget() != null) {
                  creature.setTarget(null);
               }
            }

            if (behavior.isLoyal() && behavior.getLoyaltyTarget(event.getEntity().level()) != null) {
               if (behavior.useTimer()) {
                  behavior.setTimer(behavior.getTimer() - 1);
                  if (behavior.getTimer() == 0) {
                     behavior.setLoyal(false);
                     behavior.setLoyaltyTarget(null);
                  }

                  behavior.setShouldUpdateClient(true);
               }

               if (creature.getLastHurtByMob() == behavior.getLoyaltyTarget(creature.level())) {
                  creature.setLastHurtByMob(null);
               }

               if (creature.getTarget() == behavior.getLoyaltyTarget(creature.level())) {
                  creature.setTarget(null);
               }
            }

            if ((!creature.onGround() || creature.isOnFire()) && behavior.isAsleep()) {
               behavior.setAsleep(false);
               behavior.setShouldUpdateClient(true);
            }
         }
      }
   }

   @SubscribeEvent
   public static void preventFriendlyFire(LivingAttackEvent event) {
      Entity attacker = event.getSource().getEntity();
      if (!(attacker instanceof PathfinderMob mob)) {
         return;
      }
      ICreatureBehavior behavior = mob.getCapability(CAP).orElse(null);
      if (behavior == null || !behavior.isLoyal()) {
         return;
      }
      java.util.UUID owner = behavior.getLoyaltyTargetUUID();
      if (owner != null && behavior.isOnTargetTeam(event.getEntity(), owner)) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void removeEvent(MobEffectEvent.Remove event) {
      LivingEntity creature = event.getEntity();
      ICreatureBehavior behavior = creature.getCapability(CAP).orElse(null);
      if (behavior == null) return;
      if (event.getEffect().equals(PotionRegistry.fear.get())) {
         if (behavior.isAfraid()) {
            behavior.setAfraid(false);
            behavior.setFearTarget(null);
            behavior.setShouldUpdateClient(true);
         }
      } else if (event.getEffect().equals(PotionRegistry.infatuation.get()) && behavior.isInfatuated()) {
         behavior.setInfatuated(false);
         behavior.setInfatuationTarget(null);
         behavior.setShouldUpdateClient(true);
      }
   }

   private static class Provider implements ICapabilitySerializable<CompoundTag> {
      private final CreatureBehavior instance = new CreatureBehavior();
      private final LazyOptional<ICreatureBehavior> opt = LazyOptional.of(() -> this.instance);

      @NotNull
      @Override
      public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
         return cap == CAP ? this.opt.cast() : LazyOptional.empty();
      }

      @Override
      public CompoundTag serializeNBT() {
         CompoundTag tag = new CompoundTag();
         this.instance.writeNBT(tag);
         return tag;
      }

      @Override
      public void deserializeNBT(CompoundTag tag) {
         this.instance.readNBT(tag);
      }
   }
}
