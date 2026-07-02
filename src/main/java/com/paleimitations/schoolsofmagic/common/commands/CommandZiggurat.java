package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

public class CommandZiggurat {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(Commands.literal("zig").requires(s -> s.hasPermission(2)).executes(CommandZiggurat::run));
   }

   private static int run(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
      ServerPlayer player = ctx.getSource().getPlayerOrException();
      ServerLevel level = player.serverLevel();
      ResourceKey<Structure> key = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(SchoolsOfMagic.MODID, "ziggurat"));
      Holder<Structure> holder = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getHolder(key).orElse(null);
      if (holder == null) {
         ctx.getSource().sendFailure(Component.literal("The ziggurat structure isn't registered."));
         return 0;
      }
      ctx.getSource().sendSystemMessage(Component.literal("Searching for the nearest ziggurat (this may take a moment)..."));
      Pair<BlockPos, Holder<Structure>> result = level.getChunkSource().getGenerator()
         .findNearestMapStructure(level, HolderSet.direct(holder), player.blockPosition(), 200, false);
      if (result == null) {
         ctx.getSource().sendFailure(Component.literal("No ziggurat found within range. Explore further out and try again."));
         return 0;
      }
      BlockPos p = result.getFirst();

      level.getChunk(p.getX() >> 4, p.getZ() >> 4);
      int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, p.getX(), p.getZ());
      if (y <= level.getMinBuildHeight() + 1) y = 120;
      player.teleportTo(level, p.getX() + 0.5, y + 1.0, p.getZ() + 0.5, player.getYRot(), player.getXRot());
      ctx.getSource().sendSystemMessage(Component.literal("Teleported onto the ziggurat at " + p.getX() + ", " + (y + 1) + ", " + p.getZ() + "."));
      return 1;
   }
}
