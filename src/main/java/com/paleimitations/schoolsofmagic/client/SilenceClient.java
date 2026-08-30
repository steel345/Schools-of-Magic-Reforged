package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// sound is stopped at the very last moment, where every sound the game is about to play passes
// through. going through the level would miss the ones the client makes for itself, and breaking
// a block is one of those
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SilenceClient {
   private static final double REACH = 2.5D;

   // it slips now and then. one in eight gets through, which is enough to give somebody away
   private static final float SLIPS = 0.125F;

   private static final Set<Integer> quiet = new HashSet<>();

   public static void set(int who, boolean on) {
      if (on) quiet.add(who); else quiet.remove(who);
   }

   public static void clear() {
      quiet.clear();
   }

   // ids only mean anything to the world they came from. leaving one behind would have us
   // muffling whatever happens to hold that id in the next world joined
   @SubscribeEvent
   public static void onLeave(net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut event) {
      clear();
   }

   @SubscribeEvent
   public static void onJoin(net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn event) {
      clear();
   }

   @SubscribeEvent
   public static void onSound(PlaySoundEvent event) {
      if (quiet.isEmpty() || event.getSound() == null) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;

      double x = event.getSound().getX();
      double y = event.getSound().getY();
      double z = event.getSound().getZ();

      for (int id : quiet) {
         Entity hushed = mc.level.getEntity(id);
         if (hushed == null) continue;
         if (hushed.distanceToSqr(x, y, z) <= REACH * REACH) {
            if (mc.level.random.nextFloat() >= SLIPS) event.setSound(null);
            return;
         }
      }
   }
}
