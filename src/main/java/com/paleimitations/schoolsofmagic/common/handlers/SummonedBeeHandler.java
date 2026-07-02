package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.UUID;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SummonedBeeHandler {

   private static final String KEY = "som_bee_caster";

   @SubscribeEvent
   public static void onBeeHurt(LivingHurtEvent event) {
      if (!(event.getEntity() instanceof Bee victim)) {
         return;
      }
      if (event.getSource() == null) {
         return;
      }
      Entity attackerEntity = event.getSource().getEntity();
      if (!(attackerEntity instanceof Bee attacker)) {
         return;
      }
      if (!victim.getPersistentData().hasUUID(KEY) || !attacker.getPersistentData().hasUUID(KEY)) {
         return;
      }
      UUID victimCaster = victim.getPersistentData().getUUID(KEY);
      UUID attackerCaster = attacker.getPersistentData().getUUID(KEY);
      if (victimCaster.equals(attackerCaster)) {
         event.setCanceled(true);
      }
   }
}
