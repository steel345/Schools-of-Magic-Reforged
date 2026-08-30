package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// a bank of fog sat where it was put. it does not follow anybody about, and it is kept topped up
// only while it is meant to be thick. the puffs fade on their own, so the tail of it is the fog
// clearing rather than winking out
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class FogHandler {
   public static final int CLEARING = 100;

   private static final int BATCH = 26;
   private static final double SPREAD = 4.5D;
   private static final double TALL = 3.0D;

   private static class Bank {
      final ResourceKey<Level> where;
      final Vec3 at;
      int left;

      Bank(ResourceKey<Level> where, Vec3 at, int left) {
         this.where = where;
         this.at = at;
         this.left = left;
      }
   }

   private static final List<Bank> banks = new ArrayList<>();

   public static void plant(ServerLevel level, Vec3 at, int ticks) {
      banks.add(new Bank(level.dimension(), at, ticks));
      seed(level, at, BATCH * 4);
   }

   private static void seed(ServerLevel level, Vec3 at, int count) {
      for (int i = 0; i < count; i++) {
         double x = at.x + (level.random.nextDouble() - 0.5D) * SPREAD * 2.0D;
         double y = at.y + level.random.nextDouble() * TALL;
         double z = at.z + (level.random.nextDouble() - 0.5D) * SPREAD * 2.0D;
         level.sendParticles(ParticleTypeRegistry.FOG.get(), x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
      }
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server == null || banks.isEmpty()) return;

      banks.removeIf(bank -> {
         ServerLevel level = server.getLevel(bank.where);
         if (level == null) return true;

         bank.left--;
         if (bank.left <= 0) return true;

         // the tail of it is the fog clearing. nothing more is put out from here on
         if (bank.left <= CLEARING) return false;

         if (bank.left % 10 == 0) seed(level, bank.at, BATCH);
         return false;
      });
   }
}
