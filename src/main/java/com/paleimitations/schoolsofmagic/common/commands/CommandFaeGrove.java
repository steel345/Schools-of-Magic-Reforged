package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.commands.util.Teleport;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

public class CommandFaeGrove {

   public static final ResourceKey<Level> FAEGROVE =
      ResourceKey.create(Registries.DIMENSION, new ResourceLocation("som", "faegrove"));

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("faegrove")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> execute(ctx.getSource()))
      );
   }

   private static int execute(CommandSourceStack source) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      ServerLevel dest = source.getServer().getLevel(FAEGROVE);
      if (dest == null) {
         source.sendFailure(Component.literal("The Fae Grove dimension is not loaded."));
         return 0;
      }
      int x = (int) Math.floor(player.getX());
      int z = (int) Math.floor(player.getZ());

      dest.getChunk(x >> 4, z >> 4);
      int y = dest.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
      if (y <= dest.getMinBuildHeight()) y = 96;
      Teleport.teleportToDim(player, dest, x + 0.5D, y + 1, z + 0.5D);
      source.sendSuccess(() -> Component.literal("Teleported to the Fae Grove."), true);
      return 1;
   }
}
