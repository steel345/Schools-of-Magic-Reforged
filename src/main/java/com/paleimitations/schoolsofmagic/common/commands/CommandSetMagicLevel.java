package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

// /setmagiclevel <level> - sets the magician level by writing the matching amount
// of magician XP (the level is derived from XP, it is not stored on its own).
public class CommandSetMagicLevel {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("setmagiclevel")
            .requires(src -> src.hasPermission(2))
            .then(
               Commands.argument("level", IntegerArgumentType.integer(0, 500))
                  .executes(ctx -> execute(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "level")))
            )
      );
   }

   // Levelling spends 50 + 10*level XP per level, so reaching level N costs the sum
   // of every step below it; one spare point puts the player just inside the level.
   private static float xpForLevel(int level) {
      float total = 0.0F;
      for (int i = 0; i < level; i++) total += 50.0F + i * 10.0F;
      return total + 1.0F;
   }

   private static int execute(CommandSourceStack source, int level) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      IManaData data = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data == null) {
         source.sendFailure(Component.literal("No mana cap attached"));
         return 0;
      }
      data.setMagicianXP(xpForLevel(level));
      source.sendSuccess(() -> Component.literal("Magician level set to " + level), false);
      return 1;
   }
}
