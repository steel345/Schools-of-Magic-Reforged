package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class HorseshoeCharmHandler {
   public static final float SOFTEN = 0.35F;
   public static final float HARD_FALL = 7.0F;
   public static final int WEAR = 5;

   public static ItemStack worn(Player player) {
      if (player == null) return ItemStack.EMPTY;
      return GarmentSlots.findCharmPouch(player, s -> s.is(ItemRegistry.horseshoe.get()));
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Player player = event.player;
      if (player.level().isClientSide || player.tickCount % 20 != 0) return;
      if (worn(player).isEmpty()) return;

      player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 0, true, false, false));
      player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 0, true, false, false));
   }

   @SubscribeEvent
   public static void onFall(LivingFallEvent event) {
      if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) return;
      ItemStack shoe = worn(player);
      if (shoe.isEmpty()) return;

      event.setDamageMultiplier(event.getDamageMultiplier() * (1.0F - SOFTEN));
      if (event.getDistance() > HARD_FALL) {
         shoe.hurtAndBreak(WEAR, player, broken -> broken.broadcastBreakEvent(player.getUsedItemHand()));
      }
   }
}
