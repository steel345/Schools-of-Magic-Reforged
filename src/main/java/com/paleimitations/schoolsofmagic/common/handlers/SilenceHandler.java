package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketSilence;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

// who is walking quietly, and for how much longer. everyone near enough to hear them has to be
// told, not just the one holding the spell
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class SilenceHandler {
   private static final Map<UUID, Integer> hushed = new HashMap<>();

   public static boolean isHushed(java.util.UUID who) {
      return who != null && hushed.containsKey(who);
   }

   // wardens and sculk do not listen for sounds, they listen for vibrations, which are their own
   // thing entirely. stopping the noise would not have stopped them hearing it
   @SubscribeEvent
   public static void onVibration(net.minecraftforge.event.VanillaGameEvent event) {
      if (hushed.isEmpty()) return;
      net.minecraft.world.entity.Entity source = event.getContext().sourceEntity();
      if (source == null || !isHushed(source.getUUID())) return;
      if (source.level().random.nextFloat() < 0.125F) return;
      event.setCanceled(true);
   }

   public static void begin(ServerPlayer player, int ticks) {
      hushed.put(player.getUUID(), ticks);
      tell(player, true);
   }

   private static void tell(ServerPlayer player, boolean on) {
      PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
         new PacketSilence(player.getId(), on));
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server == null || hushed.isEmpty()) return;

      hushed.entrySet().removeIf(entry -> {
         ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
         if (player == null) return true;

         int left = entry.getValue() - 1;
         entry.setValue(left);
         if (left > 0) {
            // anyone who has just walked into range needs telling as well
            if (left % 20 == 0) tell(player, true);
            return false;
         }
         tell(player, false);
         return true;
      });
   }
}
