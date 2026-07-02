package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.world.weather.CapabilityWeatherEffect;
import com.paleimitations.schoolsofmagic.common.world.weather.IWeatherStorage;
import com.paleimitations.schoolsofmagic.common.world.weather.WeatherClear;
import com.paleimitations.schoolsofmagic.common.world.weather.WeatherFog;
import com.paleimitations.schoolsofmagic.common.world.weather.WeatherSoulFog;
import com.paleimitations.schoolsofmagic.common.world.weather.WeatherWind;
import java.util.Random;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CommandWeather {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("magicweather")
            .requires(src -> src.hasPermission(2))
            .then(
               Commands.argument("id", IntegerArgumentType.integer())
                  .executes(ctx -> execute(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "id")))
            )
      );
   }

   private static int execute(CommandSourceStack source, int id) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      ServerLevel world = player.serverLevel();
      IWeatherStorage storage = world.getCapability(CapabilityWeatherEffect.CAPABILITY_WEATHER_STORAGE).orElse(null);
      if (storage == null) {
         return 0;
      }

      Random rand = new Random();
      Level level = world;
      switch (id) {
         case 0:
            storage.createGlobalWeatherEffect(
               new WeatherClear(level, 6000 + Math.round(rand.nextFloat() * 3600.0F), 6000 + Math.round(rand.nextFloat() * 3600.0F), true)
            );
            break;
         case 1:
            storage.createGlobalWeatherEffect(
               new WeatherWind(level, rand.nextDouble() * 360.0, rand.nextDouble() * 1.25 + 1.0, 200, 12000 + Math.round(rand.nextFloat() * 3600.0F), true)
            );
            break;
         case 2:
            storage.createGlobalWeatherEffect(
               new WeatherFog(level, rand.nextDouble() * 0.25 + 1.125, 200, 12000 + Math.round(rand.nextFloat() * 3600.0F), true)
            );
            break;
         case 3:
            storage.createGlobalWeatherEffect(
               new WeatherSoulFog(level, rand.nextDouble() * 0.25 + 1.125, 200, 12000 + Math.round(rand.nextFloat() * 3600.0F), true)
            );
            break;
         case 4:
            storage.createGlobalWeatherEffect(
               new WeatherWind(level, rand.nextDouble() * 360.0, rand.nextDouble() * 1.25 + 1.0, 200, 12000 + Math.round(rand.nextFloat() * 3600.0F), false)
            );
            break;
         case 5:
            storage.createGlobalWeatherEffect(
               new WeatherFog(level, rand.nextDouble() * 0.25 + 1.125, 200, 12000 + Math.round(rand.nextFloat() * 3600.0F), false)
            );
            break;
         case 6:
            storage.createGlobalWeatherEffect(
               new WeatherSoulFog(level, rand.nextDouble() * 0.25 + 1.125, 200, 12000 + Math.round(rand.nextFloat() * 3600.0F), false)
            );
      }

      return 1;
   }
}
