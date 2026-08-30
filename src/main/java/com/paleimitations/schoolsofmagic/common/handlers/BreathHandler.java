package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// listens for anything drawing breath nearby and marks it. what does not breathe is not found
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class BreathHandler {
   private static class Listen {
      int left;
      final double reach;
      final Set<Integer> marked = new HashSet<>();

      Listen(int left, double reach) {
         this.left = left;
         this.reach = reach;
      }
   }

   private static final Map<UUID, Listen> listening = new HashMap<>();

   public static void begin(ServerPlayer player, int ticks, double reach) {
      stop(player);
      listening.put(player.getUUID(), new Listen(ticks, reach));
   }

   private static void stop(ServerPlayer player) {
      Listen was = listening.remove(player.getUUID());
      if (was == null) return;
      for (int id : was.marked) {
         if (player.level().getEntity(id) instanceof LivingEntity living) living.setGlowingTag(false);
      }
   }

   // the dead do not breathe, so they are never found by this
   private static boolean breathes(LivingEntity living) {
      return living.isAlive() && living.getMobType() != MobType.UNDEAD;
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server == null || listening.isEmpty()) return;

      for (Map.Entry<UUID, Listen> entry : new HashMap<>(listening).entrySet()) {
         ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
         Listen listen = entry.getValue();

         if (player == null) {
            listening.remove(entry.getKey());
            continue;
         }

         listen.left--;
         if (listen.left <= 0) {
            stop(player);
            continue;
         }

         if (listen.left % 5 != 0) continue;
         sweep(player, listen);

         if (listen.left % 40 == 0) {
            player.playNotifySound(SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 0.6F, 1.3F);
         }
      }
   }

   private static void sweep(ServerPlayer player, Listen listen) {
      ServerLevel level = player.serverLevel();
      Set<Integer> found = new HashSet<>();

      for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class,
            player.getBoundingBox().inflate(listen.reach))) {
         if (living == player || !breathes(living)) continue;
         if (living.distanceToSqr(player) > listen.reach * listen.reach) continue;

         living.setGlowingTag(true);
         found.add(living.getId());
      }

      // anything that has wandered out of hearing loses its mark
      for (int id : listen.marked) {
         if (found.contains(id)) continue;
         if (level.getEntity(id) instanceof LivingEntity gone) gone.setGlowingTag(false);
      }
      listen.marked.clear();
      listen.marked.addAll(found);
   }
}
