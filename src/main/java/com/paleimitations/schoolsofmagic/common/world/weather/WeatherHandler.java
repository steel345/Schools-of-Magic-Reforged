package com.paleimitations.schoolsofmagic.common.world.weather;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Bus.FORGE)
public class WeatherHandler implements IWeatherStorage {
   public static IWeatherEffect globalWeatherEffect;
   public static int globalClearTick;
   public static List<IWeatherEffect> localWeatherEffects = Lists.newArrayList();
   private Random rand = new Random();

   public WeatherHandler() {
   }

   @Override
   public void createGlobalWeatherEffect(IWeatherEffect weatherEffect) {
      System.out.println("Creating new weather event: " + weatherEffect.getName());
      globalWeatherEffect = weatherEffect;
      globalClearTick = 0;
   }

   @Override
   public void setClearTick(int globalClearTick) {
      WeatherHandler.globalClearTick = globalClearTick;
   }

   @Override
   public void addLocalWeatherEffect(IWeatherEffect effect) {
      localWeatherEffects.add(effect);
   }

   @Override
   public int getClearTick() {
      return globalClearTick;
   }

   @Override
   public void setLocalWeatherEffects(List<IWeatherEffect> localWeatherEffects) {
      WeatherHandler.localWeatherEffects = localWeatherEffects;
   }

   @Override
   public List<IWeatherEffect> getLocalWeatherEffects() {
      return localWeatherEffects;
   }

   @Override
   public IWeatherEffect getGlobalWeatherEffect() {
      return globalWeatherEffect;
   }

   @Override
   public void setGlobalWeatherEffect(IWeatherEffect effect) {
      globalWeatherEffect = effect;
   }

   @Override
   public int getGlobalDuration() {
      return globalWeatherEffect == null ? 0 : globalWeatherEffect.getDuration();
   }

   @SubscribeEvent
   public static void onWorldWeatherEvent(TickEvent.LevelTickEvent event) {
      Level world = event.level;
      if (!world.isClientSide) {
         if (globalWeatherEffect != null) {
            globalWeatherEffect.tick();
            if (globalWeatherEffect.getDuration() == 0) {
               globalWeatherEffect = null;
            }
         } else {
            globalClearTick++;
         }

         for (IWeatherEffect effect : localWeatherEffects) {
            effect.tick();
         }

         Random rand = new Random();
         if (globalClearTick > 6000 && rand.nextInt(1000) == 0) {
            if (rand.nextInt(50) == 0) {
               createGlobal(
                  new WeatherSoulFog(
                     world,
                     rand.nextDouble() * 0.25 + 1.125,
                     6000 + Math.round(rand.nextFloat() * 3600.0F),
                     12000 + Math.round(rand.nextFloat() * 3600.0F),
                     true
                  )
               );
            } else if (rand.nextInt(10) == 0) {
               createGlobal(
                  new WeatherFog(
                     world,
                     rand.nextDouble() * 0.25 + 1.125,
                     6000 + Math.round(rand.nextFloat() * 3600.0F),
                     12000 + Math.round(rand.nextFloat() * 3600.0F),
                     true
                  )
               );
            } else if (rand.nextInt(10) == 0) {
               createGlobal(
                  new WeatherWind(
                     world,
                     rand.nextDouble() * 360.0,
                     rand.nextDouble() * 1.25 + 1.0,
                     12000 + Math.round(rand.nextFloat() * 3600.0F),
                     6000 + Math.round(rand.nextFloat() * 3600.0F),
                     true
                  )
               );
            } else {
               createGlobal(
                  new WeatherClear(world, 12000 + Math.round(rand.nextFloat() * 3600.0F), 6000 + Math.round(rand.nextFloat() * 3600.0F), true)
               );
            }
         }

         List<IWeatherEffect> effects = Lists.newArrayList();

         for (IWeatherEffect effect : localWeatherEffects) {
            if (effect.getDuration() > 0) {
               effects.add(effect);
            }
         }

         localWeatherEffects = effects;
      }
   }

   private static void createGlobal(IWeatherEffect weatherEffect) {
      System.out.println("Creating new weather event: " + weatherEffect.getName());
      globalWeatherEffect = weatherEffect;
      globalClearTick = 0;
   }

   @SubscribeEvent
   public static void onEntityWeatherEvent(LivingEvent.LivingTickEvent event) {

      IWeatherEffect g = globalWeatherEffect;
      if (g != null) {
         g.inStormTick(event);
      }

      for (IWeatherEffect effect : localWeatherEffects) {
         effect.inStormTick(event);
      }
   }

   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void onClientWeatherEvent(ClientTickEvent event) {
      IWeatherEffect g = globalWeatherEffect;
      if (g != null && !Minecraft.getInstance().isPaused()) {
         g.clientTick(event);
      }

      for (IWeatherEffect effect : localWeatherEffects) {
         effect.clientTick(event);
      }
   }

   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void onFogWeatherEvent(ViewportEvent.RenderFog event) {

      IWeatherEffect g = globalWeatherEffect;
      if (g != null) {
         g.fogRender(event);
         g.fogDensity(event);
      }

      for (IWeatherEffect effect : localWeatherEffects) {
         effect.fogRender(event);
         effect.fogDensity(event);
      }
   }

   @OnlyIn(Dist.CLIENT)
   @SubscribeEvent
   public static void onFogColorWeatherEvent(ViewportEvent.ComputeFogColor event) {
      IWeatherEffect g = globalWeatherEffect;
      if (g != null) {
         g.fogColor(event);
      }

      for (IWeatherEffect effect : localWeatherEffects) {
         effect.fogColor(event);
      }
   }
}
