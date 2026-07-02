package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.commands.util.Teleport;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class CommandDimensionalTeleport {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("tpdim")
            .requires(src -> src.hasPermission(2))
            .then(
               Commands.argument("dimension", DimensionArgument.dimension())
                  .executes(ctx -> execute(ctx.getSource(), DimensionArgument.getDimension(ctx, "dimension")))
            )
      );
   }

   private static int execute(CommandSourceStack source, ServerLevel destination) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      try {
         Teleport.teleportToDim(player, destination, player.getX(), player.getY(), player.getZ());
      } catch (IllegalArgumentException e) {
         source.sendFailure(Component.literal(e.getMessage()));
      }

      return 1;
   }
}
