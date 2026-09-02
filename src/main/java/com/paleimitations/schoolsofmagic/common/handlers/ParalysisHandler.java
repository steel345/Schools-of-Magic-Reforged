package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class ParalysisHandler {
   public static boolean held(LivingEntity living) {
      return living != null && living.hasEffect(PotionRegistry.paralysis.get());
   }

   @SubscribeEvent
   public static void onTick(LivingEvent.LivingTickEvent event) {
      if (!(event.getEntity() instanceof Mob mob) || mob.level().isClientSide) return;
      if (!held(mob)) return;

      mob.getNavigation().stop();
      mob.setTarget(null);
      mob.setJumping(false);
      mob.xxa = 0.0F;
      mob.yya = 0.0F;
      mob.zza = 0.0F;
      mob.setSpeed(0.0F);
      mob.setDeltaMovement(0.0D, Math.min(0.0D, mob.getDeltaMovement().y), 0.0D);
   }

   @SubscribeEvent
   public static void onAttack(LivingAttackEvent event) {
      if (event.getSource().getEntity() instanceof LivingEntity attacker && held(attacker)) {
         event.setCanceled(true);
      }
   }
}
