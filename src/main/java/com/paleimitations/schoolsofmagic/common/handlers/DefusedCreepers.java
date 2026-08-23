package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class DefusedCreepers {
   public static final int CALM_TICKS = 600;

   private static final class Calm {
      long until;
      SwellGoal goal;
      int priority;
   }

   private static final Map<UUID, Calm> CALM = new HashMap<>();

   public static void calm(Creeper creeper) {
      if (creeper.level().isClientSide) return;
      creeper.setSwellDir(-1);
      creeper.setTarget(null);
      creeper.setLastHurtByMob(null);

      Calm calm = new Calm();
      calm.until = creeper.level().getGameTime() + CALM_TICKS;
      for (WrappedGoal wrapped : creeper.goalSelector.getAvailableGoals()) {
         if (wrapped.getGoal() instanceof SwellGoal swell) {
            calm.goal = swell;
            calm.priority = wrapped.getPriority();
            break;
         }
      }
      if (calm.goal != null) {
         creeper.goalSelector.removeGoal(calm.goal);
      }
      CALM.put(creeper.getUUID(), calm);
   }

   @SubscribeEvent
   public static void onLivingTick(LivingEvent.LivingTickEvent event) {
      if (!(event.getEntity() instanceof Creeper creeper) || creeper.level().isClientSide) return;
      Calm calm = CALM.get(creeper.getUUID());
      if (calm == null) return;

      if (creeper.level().getGameTime() > calm.until) {
         CALM.remove(creeper.getUUID());
         if (calm.goal != null) {
            creeper.goalSelector.addGoal(calm.priority, calm.goal);
         }
         return;
      }
      creeper.setSwellDir(-1);
      creeper.setTarget(null);
   }
}
