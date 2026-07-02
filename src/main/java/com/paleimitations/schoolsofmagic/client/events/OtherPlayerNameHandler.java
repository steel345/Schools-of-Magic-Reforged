package com.paleimitations.schoolsofmagic.client.events;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.handlers.OtherPlayerHandler;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class OtherPlayerNameHandler {

   @SubscribeEvent
   public static void onNameTag(RenderNameTagEvent event) {
      if (event.getEntity() instanceof Player player
            && player.hasEffect(PotionRegistry.other_player.get())) {
         event.setContent(Component.literal(OtherPlayerHandler.ALIAS));
      }
   }
}
