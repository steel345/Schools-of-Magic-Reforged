package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandMasterMagic {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("mastermagic")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> execute(ctx.getSource()))
      );
   }

   private static int execute(CommandSourceStack source) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      IManaData data = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data != null) {
         data.addMagicianXP(1000000.0F);
         data.addMana(1000000.0F);
         data.addPotionXP(100000.0F);
         data.addRitualXP(100000.0F);
         data.addPotionXP(100000.0F);

         for (MagicElement element : MagicElementRegistry.ELEMENTS) {
            data.addElementXP(element, 100000.0F);
         }

         for (MagicSchool school : MagicSchoolRegistry.SCHOOLS) {
            data.addSchoolXP(school, 100000.0F);
         }
      }

      source.sendSuccess(() -> Component.literal("You're a master Magician!"), false);
      return 1;
   }
}
