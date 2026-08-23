package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.books.PageUnlocks;
import com.paleimitations.schoolsofmagic.common.handlers.PageUnlockHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandPages {
   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("unlockpages")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> unlockAll(ctx.getSource()))
      );
      dispatcher.register(
         Commands.literal("lockpages")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> lockAll(ctx.getSource()))
      );
   }

   private static int unlockAll(CommandSourceStack source) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      int added = PageUnlocks.unlockAll(player);
      PageUnlockHandler.sync(player);
      source.sendSuccess(() -> Component.literal("Unlocked all book pages (" + added + " new)."), false);
      return 1;
   }

   private static int lockAll(CommandSourceStack source) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      int removed = PageUnlocks.lockAll(player);
      PageUnlockHandler.sync(player);
      source.sendSuccess(() -> Component.literal("Relocked all book pages (" + removed + " removed)."), false);
      return 1;
   }
}
