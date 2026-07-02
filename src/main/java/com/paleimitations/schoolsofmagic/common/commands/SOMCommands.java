package com.paleimitations.schoolsofmagic.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SOMCommands {

   @SubscribeEvent
   public static void onRegisterCommands(RegisterCommandsEvent event) {
      CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
      CommandAddMagic.register(dispatcher);
      CommandMasterMagic.register(dispatcher);
      CommandResetMagic.register(dispatcher);
      CommandSpellCharge.register(dispatcher);
      CommandWeather.register(dispatcher);
      CommandBiomeTeleport.register(dispatcher);
      CommandDimensionalTeleport.register(dispatcher);
      CommandZiggurat.register(dispatcher);
      CommandFaeGrove.register(dispatcher);
      CommandPhoenixReturn.register(dispatcher);
   }
}
