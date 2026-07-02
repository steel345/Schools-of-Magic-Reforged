package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.commands.util.Teleport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandBiomeTeleport {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("tpbio")
            .requires(src -> src.hasPermission(2))
            .then(
               Commands.argument("id", StringArgumentType.string())
                  .executes(ctx -> execute(ctx.getSource(), StringArgumentType.getString(ctx, "id")))
            )
      );
   }

   private static int execute(CommandSourceStack source, String arg) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      String biomeId = arg.substring(1);
      try {
         Teleport.teleportToBiome(player, biomeId);
      } catch (IllegalArgumentException e) {
         source.sendFailure(Component.literal(e.getMessage()));
      }

      return 1;
   }
}
