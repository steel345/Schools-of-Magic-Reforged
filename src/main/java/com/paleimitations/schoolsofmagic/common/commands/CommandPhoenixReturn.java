package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;

public class CommandPhoenixReturn {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(Commands.literal("phoenixreturn").requires(s -> s.hasPermission(2)).executes(CommandPhoenixReturn::run));
   }

   private static int run(CommandContext<CommandSourceStack> ctx) {
      int count = 0;
      for (ServerLevel level : ctx.getSource().getServer().getAllLevels()) {
         for (EntityPhoenix phoenix : level.getEntities(EntityTypeTest.forClass(EntityPhoenix.class), e -> e.isAlive())) {
            if (phoenix.hurryDelivery()) count++;
         }
      }
      int c = count;
      if (c == 0) {
         ctx.getSource().sendFailure(Component.literal("No phoenixes are currently delivering."));
         return 0;
      }
      ctx.getSource().sendSuccess(() -> Component.literal("Hurried " + c + " delivering phoenix(es) home."), true);
      return c;
   }
}
