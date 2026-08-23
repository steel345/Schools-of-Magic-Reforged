package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SunscreenEvents {
   @SubscribeEvent
   public static void onTick(LivingEvent.LivingTickEvent event) {
      LivingEntity entity = event.getEntity();
      if (entity.level().isClientSide) {
         return;
      }
      if (entity.hasEffect(PotionRegistry.sunscreen.get())) {
         if (entity.getRemainingFireTicks() > 0) {
            entity.clearFire();
            entity.setRemainingFireTicks(0);
         }
         if (entity instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
         }
         if (entity instanceof Hoglin hoglin) {
            hoglin.setImmuneToZombification(true);
         }
      }
   }

   @SubscribeEvent
   public static void onConversion(LivingConversionEvent.Pre event) {
      if (event.getEntity().hasEffect(PotionRegistry.sunscreen.get())) {
         event.setCanceled(true);
         event.setConversionTimer(Integer.MAX_VALUE);
      }
   }
}
