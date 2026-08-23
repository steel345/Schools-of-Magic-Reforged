package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class FortifiedBreathEffects {
   private static final int LINGER_TICKS = 40;
   private static final float IN_WALL_LEFT = 0.15F;

   private static final Map<UUID, Long> fortified = new HashMap<>();

   public static void fortify(Player player) {
      if (player == null || player.level().isClientSide) return;
      fortified.put(player.getUUID(), player.level().getGameTime() + LINGER_TICKS);
   }

   public static boolean isFortified(LivingEntity living) {
      if (!(living instanceof Player player)) return false;
      Long until = fortified.get(player.getUUID());
      if (until == null) return false;
      if (player.level().getGameTime() > until) {
         fortified.remove(player.getUUID());
         return false;
      }
      return true;
   }

   @SubscribeEvent
   public static void onHurt(LivingHurtEvent event) {
      boolean drown = event.getSource().is(DamageTypes.DROWN);
      boolean inWall = event.getSource().is(DamageTypes.IN_WALL);
      if (!drown && !inWall) return;
      if (!isFortified(event.getEntity())) return;
      if (drown) {
         event.setCanceled(true);
         return;
      }
      event.setAmount(event.getAmount() * IN_WALL_LEFT);
   }
}
