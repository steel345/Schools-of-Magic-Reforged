package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketUpdateManaData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class CommandSpellCharge {

   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
      dispatcher.register(
         Commands.literal("spellcharge")
            .requires(src -> src.hasPermission(2))
            .executes(ctx -> execute(ctx.getSource()))
      );
   }

   private static int execute(CommandSourceStack source) throws CommandSyntaxException {
      ServerPlayer player = source.getPlayerOrException();
      IManaData data = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (data != null) {
         data.resetCharges();
         PacketUpdateManaData message = new PacketUpdateManaData(player.getId(), data.serializeNBT());
         PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
      }
      source.sendSuccess(() -> Component.literal("Spell charges filled to maximum."), false);
      return 1;
   }
}
