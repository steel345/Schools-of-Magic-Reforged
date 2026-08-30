package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.WizardRobes;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// the robes turn most of a spell aside, nothing else
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class WizardRobeHandler {
   @SubscribeEvent
   public static void onHurt(LivingHurtEvent event) {
      if (!(event.getEntity() instanceof Player player)) return;
      if (!event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) return;
      if (!WizardRobes.fullSet(player)) return;

      event.setAmount(event.getAmount() * (1.0F - WizardRobes.WARD));
   }
}
