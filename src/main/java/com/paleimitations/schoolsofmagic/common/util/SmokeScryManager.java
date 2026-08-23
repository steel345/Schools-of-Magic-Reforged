package com.paleimitations.schoolsofmagic.common.util;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SmokeScryManager {
   private static final List<Scry> ACTIVE = new ArrayList<>();

   private static class Scry {
      final ServerLevel level;
      final BlockPos pos;
      final double dirX;
      final double dirZ;
      int ticksLeft;

      Scry(ServerLevel level, BlockPos pos, double dirX, double dirZ, int ticksLeft) {
         this.level = level;
         this.pos = pos;
         this.dirX = dirX;
         this.dirZ = dirZ;
         this.ticksLeft = ticksLeft;
      }
   }

   public static void start(ServerLevel level, BlockPos pos, double dirX, double dirZ, int ticks) {
      ACTIVE.removeIf(s -> s.level == level && s.pos.equals(pos));
      ACTIVE.add(new Scry(level, pos, dirX, dirZ, ticks));
      com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
         net.minecraftforge.network.PacketDistributor.NEAR.with(() ->
            new net.minecraftforge.network.PacketDistributor.TargetPoint(
               pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 64.0D, level.dimension())),
         new com.paleimitations.schoolsofmagic.common.network.PacketSmokeScry(pos, ticks));
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || ACTIVE.isEmpty()) {
         return;
      }
      Iterator<Scry> it = ACTIVE.iterator();
      while (it.hasNext()) {
         Scry s = it.next();
         s.ticksLeft--;
         if (s.ticksLeft <= 0 || !s.level.isLoaded(s.pos) || !isLitCampfire(s)) {
            it.remove();
            continue;
         }
         if (s.ticksLeft % 3 == 0) {
            emitTrail(s);
         }
         if (s.ticksLeft % 40 == 0) {
            com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
               net.minecraftforge.network.PacketDistributor.NEAR.with(() ->
                  new net.minecraftforge.network.PacketDistributor.TargetPoint(
                     s.pos.getX() + 0.5D, s.pos.getY() + 0.5D, s.pos.getZ() + 0.5D, 64.0D, s.level.dimension())),
               new com.paleimitations.schoolsofmagic.common.network.PacketSmokeScry(s.pos, s.ticksLeft));
         }
      }
   }

   private static boolean isLitCampfire(Scry s) {
      var state = s.level.getBlockState(s.pos);
      return state.getBlock() instanceof CampfireBlock
         && state.hasProperty(BlockStateProperties.LIT)
         && state.getValue(BlockStateProperties.LIT);
   }

   private static void emitTrail(Scry s) {
      double x = s.pos.getX() + 0.5D;
      double y = s.pos.getY() + 0.9D;
      double z = s.pos.getZ() + 0.5D;
      double drift = 0.06D;
      double rise = 0.07D;
      for (int n = 0; n < 2; n++) {
         s.level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0,
            s.dirX * drift, rise, s.dirZ * drift, 1.0D);
      }
   }
}
