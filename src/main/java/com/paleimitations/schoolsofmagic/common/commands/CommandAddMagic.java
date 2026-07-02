package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandAddMagic {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("addmagic")
            .requires(src -> src.hasPermission(2))
            .then(
               Commands.argument("amount", FloatArgumentType.floatArg(0.0F))
                  .executes(ctx -> execute(ctx.getSource(), FloatArgumentType.getFloat(ctx, "amount")))
            )
      );
   }

   private static int execute(CommandSourceStack source, float amount) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      IManaData data = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data != null) {
         data.addMana(amount);
         source.sendSuccess(() -> Component.literal("Added " + amount + " mana"), false);
         return 1;
      }
      source.sendFailure(Component.literal("No mana cap attached"));
      return 0;
   }
}
