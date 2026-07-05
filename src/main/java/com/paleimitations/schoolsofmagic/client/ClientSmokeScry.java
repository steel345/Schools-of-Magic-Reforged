package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSmokeScry {

   private static final Map<BlockPos, Integer> ACTIVE = new HashMap<>();

   private static Field PARTICLES;
   private static Field PX, PY, PZ, PXD, PZD;
   private static boolean resolved = false;

   public static void receive(BlockPos pos, int ticks) {
      ACTIVE.put(pos.immutable(), 60);
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END || ACTIVE.isEmpty()) {
         return;
      }
      Iterator<Map.Entry<BlockPos, Integer>> it = ACTIVE.entrySet().iterator();
      while (it.hasNext()) {
         Map.Entry<BlockPos, Integer> e = it.next();
         int left = e.getValue() - 1;
         if (left <= 0) {
            it.remove();
         } else {
            e.setValue(left);
         }
      }
      if (!ACTIVE.isEmpty()) {
         suppressVanillaSmoke();
      }
   }

   private static void suppressVanillaSmoke() {
      Minecraft mc = Minecraft.getInstance();
      if (mc.particleEngine == null) {
         return;
      }
      if (!resolveFields()) {
         return;
      }
      try {
         Map<?, Queue<Particle>> map = (Map<?, Queue<Particle>>) PARTICLES.get(mc.particleEngine);
         for (Queue<Particle> queue : map.values()) {
            for (Particle particle : queue) {
               if (!(particle instanceof CampfireSmokeParticle)) {
                  continue;
               }
               double px = PX.getDouble(particle);
               double py = PY.getDouble(particle);
               double pz = PZ.getDouble(particle);
               double xd = PXD.getDouble(particle);
               double zd = PZD.getDouble(particle);
               if (Math.abs(xd) >= 0.02D || Math.abs(zd) >= 0.02D) {
                  continue;
               }
               for (BlockPos pos : ACTIVE.keySet()) {
                  if (Math.abs(px - (pos.getX() + 0.5D)) < 0.45D
                        && Math.abs(pz - (pos.getZ() + 0.5D)) < 0.45D
                        && py >= pos.getY() && py <= pos.getY() + 10.0D) {
                     particle.remove();
                     break;
                  }
               }
            }
         }
      } catch (Exception ignored) {
      }
   }

   private static boolean resolveFields() {
      if (resolved) {
         return PARTICLES != null;
      }
      resolved = true;
      try {
         PARTICLES = ParticleEngine.class.getDeclaredField("particles");
         PARTICLES.setAccessible(true);
         PX = Particle.class.getDeclaredField("x");
         PY = Particle.class.getDeclaredField("y");
         PZ = Particle.class.getDeclaredField("z");
         PXD = Particle.class.getDeclaredField("xd");
         PZD = Particle.class.getDeclaredField("zd");
         PX.setAccessible(true);
         PY.setAccessible(true);
         PZ.setAccessible(true);
         PXD.setAccessible(true);
         PZD.setAccessible(true);
         return true;
      } catch (Exception e) {
         PARTICLES = null;
         return false;
      }
   }
}
