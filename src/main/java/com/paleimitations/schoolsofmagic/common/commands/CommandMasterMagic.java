package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.ManaData;
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
         float skill = ManaData.xpForSkillCap();
         data.setMagicianXP(ManaData.xpForMagicianCap());
         data.setSpellXP(skill);
         data.setPotionXP(skill);
         data.setRitualXP(skill);
         for (MagicElement element : MagicElementRegistry.ELEMENTS) {
            data.setElementXP(element, skill);
         }
         for (MagicSchool school : MagicSchoolRegistry.SCHOOLS) {
            data.setSchoolXP(school, skill);
         }

         data.setXPFrozen(true);

         data.setMaxManaBonus(0);
         data.setDeadMana(0.0F);
         data.setMana(data.getMaxMana());

         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
            new com.paleimitations.schoolsofmagic.common.network.PacketUpdateManaData(player.getId(), data.serializeNBT()));
      }

      source.sendSuccess(() -> Component.literal("You're a master Magician! Your levels are frozen."), false);
      return 1;
   }
}
