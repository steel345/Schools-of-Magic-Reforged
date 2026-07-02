package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.config.SOMFairyConfig;
import com.paleimitations.schoolsofmagic.common.entity.EntityFairy;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class FairySpawnHandler {

   private static final ResourceKey<Level> FAEGROVE =
      ResourceKey.create(Registries.DIMENSION, new ResourceLocation(SchoolsOfMagic.MODID, "faegrove"));

   private static final int INTERVAL = 120;
   private static final int LOCAL_CAP = 6;
   private static final int MIN_TRUNK_LOGS = 4;

   @SubscribeEvent
   public static void onLevelTick(TickEvent.LevelTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (!(event.level instanceof ServerLevel level)) return;
      if (level.dimension() != FAEGROVE) return;
      if (!SOMFairyConfig.NATURAL_SPAWNING.get()) return;
      if (level.getGameTime() % INTERVAL != 0L) return;

      for (ServerPlayer player : level.players()) {
         trySpawnNear(level, player);
      }
   }

   private static void trySpawnNear(ServerLevel level, ServerPlayer player) {
      if (level.getEntitiesOfClass(EntityFairy.class, player.getBoundingBox().inflate(48.0D)).size() >= LOCAL_CAP) {
         return;
      }
      RandomSource rand = level.random;
      for (int attempt = 0; attempt < 6; attempt++) {
         int dx = rand.nextInt(56) - 28;
         int dz = rand.nextInt(56) - 28;
         if (dx * dx + dz * dz < 14 * 14) continue;
         int x = player.getBlockX() + dx;
         int z = player.getBlockZ() + dz;
         BlockPos trunkTop = findBigTree(level, x, z);
         if (trunkTop == null) continue;

         int n = 1 + rand.nextInt(2);
         BlockPos home = null;
         for (int i = 0; i < n; i++) {
            BlockPos sp = canopySpawnPos(level, trunkTop, rand);
            if (sp == null) continue;
            EntityFairy fairy = EntityRegistry.FAIRY.get().create(level);
            if (fairy == null) continue;
            fairy.moveTo(sp.getX() + 0.5D, sp.getY() + 0.5D, sp.getZ() + 0.5D, rand.nextFloat() * 360.0F, 0.0F);
            fairy.finalizeSpawn(level, level.getCurrentDifficultyAt(sp), MobSpawnType.NATURAL, null, null);
            fairy.setHome(trunkTop);
            level.addFreshEntity(fairy);
            home = trunkTop;
         }
         if (home != null) return;
      }
   }

   private static BlockPos findBigTree(ServerLevel level, int x, int z) {
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      BlockPos best = null;
      int bestLogs = 0;
      for (int ox = -2; ox <= 2; ox++) {
         for (int oz = -2; oz <= 2; oz++) {
            int cx = x + ox, cz = z + oz;
            int top = level.getHeight(Heightmap.Types.WORLD_SURFACE, cx, cz);
            int colLogs = 0;
            int highestLogY = Integer.MIN_VALUE;
            for (int y = top; y >= top - 16; y--) {
               m.set(cx, y, cz);
               if (level.getBlockState(m).is(BlockTags.LOGS)) {
                  colLogs++;
                  if (y > highestLogY) highestLogY = y;
               }
            }
            if (colLogs > bestLogs && highestLogY != Integer.MIN_VALUE) {
               bestLogs = colLogs;
               best = new BlockPos(cx, highestLogY, cz);
            }
         }
      }
      return bestLogs >= MIN_TRUNK_LOGS ? best : null;
   }

   private static BlockPos canopySpawnPos(ServerLevel level, BlockPos trunkTop, RandomSource rand) {
      for (int attempt = 0; attempt < 8; attempt++) {
         int dx = rand.nextInt(7) - 3;
         int dz = rand.nextInt(7) - 3;
         int dy = rand.nextInt(4);
         BlockPos p = new BlockPos(trunkTop.getX() + dx, trunkTop.getY() + 1 + dy, trunkTop.getZ() + dz);
         if (level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()
               && level.getBlockState(p.below()).isAir()) {
            return p;
         }
      }
      return null;
   }
}
