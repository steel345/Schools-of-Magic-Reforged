package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.paleimitations.schoolsofmagic.common.handlers.EclipseHandler;
import com.paleimitations.schoolsofmagic.common.world.EclipseData;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class CommandEclipse {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("eclipse")
            .requires(src -> src.hasPermission(2))
            .then(Commands.literal("start").executes(ctx -> start(ctx.getSource())))
            .then(Commands.literal("stop").executes(ctx -> stop(ctx.getSource())))
            .executes(ctx -> start(ctx.getSource()))
      );
   }

   private static int start(CommandSourceStack source) {
      ServerLevel level = source.getServer().overworld();
      EclipseData data = EclipseData.get(level);
      if (data.isRunning()) {
         source.sendFailure(Component.literal("An eclipse is already underway."));
         return 0;
      }
      data.begin(level);
      EclipseHandler.broadcast(level, data);
      source.sendSuccess(() -> Component.literal("The sun begins to darken."), true);
      return 1;
   }

   private static int stop(CommandSourceStack source) {
      ServerLevel level = source.getServer().overworld();
      EclipseData data = EclipseData.get(level);
      if (!data.isRunning()) {
         source.sendFailure(Component.literal("There is no eclipse to end."));
         return 0;
      }
      data.stop(level);
      EclipseHandler.broadcast(level, data);
      source.sendSuccess(() -> Component.literal("The eclipse fades."), true);
      return 1;
   }
}
