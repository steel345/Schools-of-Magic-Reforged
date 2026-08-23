package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class AcolyteWispSpawns {
   private static final int INTERVAL = 200;
   private static final int SEARCH_RADIUS = 24;
   private static final int TRIES = 24;

   private static final int CROWD = 3;
   private static final double CROWD_RADIUS = 12.0D;
   private static final float CHANCE = 0.35F;

   @SubscribeEvent
   public static void onLevelTick(TickEvent.LevelTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (!(event.level instanceof ServerLevel level)) return;
      if (level.getGameTime() % INTERVAL != 0L) return;

      if (level.isDay()) return;

      for (ServerPlayer player : level.players()) {
         if (level.random.nextFloat() > CHANCE) continue;
         trySpawnNear(level, player.blockPosition());
      }
   }

   private static void trySpawnNear(ServerLevel level, BlockPos around) {
      RandomSource rand = level.random;
      for (int i = 0; i < TRIES; i++) {
         BlockPos pos = around.offset(
            rand.nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS,
            rand.nextInt(12) - 6,
            rand.nextInt(SEARCH_RADIUS * 2) - SEARCH_RADIUS);
         if (!level.isLoaded(pos)) continue;
         if (!isAshTree(level.getBlockState(pos))) continue;

         BlockPos spot = openSpotBeside(level, pos, rand);
         if (spot == null) continue;
         if (crowded(level, spot)) return;

         EntityAcolyteWisp wisp = EntityRegistry.ACOLYTE_WISP.get().create(level);
         if (wisp == null) return;
         wisp.moveTo(spot.getX() + 0.5D, spot.getY() + 0.5D, spot.getZ() + 0.5D,
            rand.nextFloat() * 360.0F, 0.0F);
         level.addFreshEntity(wisp);
         return;
      }
   }

   private static boolean isAshTree(BlockState state) {
      return state.is(BlockRegistry.log_ash.get())
         || state.is(BlockRegistry.wood_ash.get())
         || state.is(BlockRegistry.leaves_ash.get());
   }

   private static BlockPos openSpotBeside(ServerLevel level, BlockPos wood, RandomSource rand) {
      for (int i = 0; i < 8; i++) {
         BlockPos p = wood.offset(rand.nextInt(5) - 2, rand.nextInt(4) - 1, rand.nextInt(5) - 2);
         if (level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()) {
            return p;
         }
      }
      return null;
   }

   private static boolean crowded(ServerLevel level, BlockPos pos) {
      List<EntityAcolyteWisp> near = level.getEntitiesOfClass(EntityAcolyteWisp.class,
         new AABB(pos).inflate(CROWD_RADIUS));
      return near.size() >= CROWD;
   }
}
