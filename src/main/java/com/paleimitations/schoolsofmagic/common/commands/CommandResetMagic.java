package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CommandResetMagic {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("resetmagic")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> execute(ctx.getSource()))
      );
   }

   private static int execute(CommandSourceStack source) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      IManaData data = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data != null) {
         data.setMagicianXP(0.0F);
         data.setSpellXP(0.0F);
         data.setPotionXP(0.0F);
         data.setRitualXP(0.0F);
         data.setMana(0.0F);
         data.setDeadMana(0.0F);
         data.setManaDiscountRate(0.0F);
         data.setXPBonusRate(0.0F);
         data.setMaxManaBonus(0);
         data.setLevelBonus(0);
         data.setElementXPs(new float[MagicElementRegistry.ELEMENTS.size()]);
         data.setSchoolXPs(new float[MagicSchoolRegistry.SCHOOLS.size()]);
      }

      source.sendSuccess(() -> Component.literal("Your magic is gone!"), false);
      return 1;
   }
}
