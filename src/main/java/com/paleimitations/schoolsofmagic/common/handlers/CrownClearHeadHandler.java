package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// see or which way you are pointed can settle on you, and a hit while you are holding a spell
// no longer bites twice as deep
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class CrownClearHeadHandler {
   public static boolean clearHeaded(Player player) {
      if (player == null) return false;
      ItemStack crown = GarmentSlots.wornCrown(player);
      return !crown.isEmpty() && !GarmentSlots.isPlain(crown);
   }

   private static List<MobEffect> clouded() {
      return List.of(MobEffects.BLINDNESS, MobEffects.CONFUSION, MobEffects.DARKNESS,
         PotionRegistry.bewilderment.get());
   }

   @SubscribeEvent
   public static void onApplicable(MobEffectEvent.Applicable event) {
      if (!(event.getEntity() instanceof Player player) || !clearHeaded(player)) return;
      if (!clouded().contains(event.getEffectInstance().getEffect())) return;
      event.setResult(Event.Result.DENY);
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Player player = event.player;
      if (player.level().isClientSide || player.tickCount % 10 != 0) return;
      if (!clearHeaded(player)) return;

      for (MobEffect effect : clouded()) {
         if (player.hasEffect(effect)) player.removeEffect(effect);
      }
   }
}
