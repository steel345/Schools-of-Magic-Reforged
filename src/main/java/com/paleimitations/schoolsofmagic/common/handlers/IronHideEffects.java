package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.potions.potions.PotionIronHide;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class IronHideEffects {
   @SubscribeEvent
   public static void onHurt(LivingHurtEvent event) {
      if (!event.getEntity().hasEffect(PotionRegistry.iron_hide.get())) return;
      if (event.getSource().is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;
      event.setAmount(Math.max(0.0F, event.getAmount() - PotionIronHide.DAMAGE_REDUCTION));
   }
}
