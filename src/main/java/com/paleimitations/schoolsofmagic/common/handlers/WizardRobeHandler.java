package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.WizardRobes;
import com.paleimitations.schoolsofmagic.common.handlers.AdvancementHelper;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class WizardRobeHandler {
   @SubscribeEvent
   public static void onPlayerTick(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
      if (event.phase != net.minecraftforge.event.TickEvent.Phase.END) return;
      Player player = event.player;
      if (player.level().isClientSide || player.tickCount % 20 != 0) return;
      if (!(player instanceof net.minecraft.server.level.ServerPlayer sp)) return;
      if (WizardRobes.fullSet(player)) {
         AdvancementHelper.grant(sp, "som/drip_of_the_arcane", "wear_robes");
      }
   }

   @SubscribeEvent
   public static void onHurt(LivingHurtEvent event) {
      if (!(event.getEntity() instanceof Player player)) return;
      if (!event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) return;
      if (!WizardRobes.fullSet(player)) return;

      event.setAmount(event.getAmount() * (1.0F - WizardRobes.WARD));
   }
}
