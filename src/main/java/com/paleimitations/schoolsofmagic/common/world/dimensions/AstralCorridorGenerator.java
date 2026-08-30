package com.paleimitations.schoolsofmagic.common.world.dimensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

// the astral plane is a corridor, six blocks across, walled in barriers and open to the sky. the
// walls run the whole height of the dimension and the whole way along z, so there is nowhere to
// go but forward and back
public class AstralCorridorGenerator extends ChunkGenerator {
   public static final Codec<AstralCorridorGenerator> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
         BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource)
      ).apply(instance, AstralCorridorGenerator::new));

   public static final int WEST_WALL = -1;
   public static final int EAST_WALL = 6;
   public static final int FLOOR = 64;

   public AstralCorridorGenerator(BiomeSource biomeSource) {
      super(biomeSource);
   }

   public static boolean inside(double x) {
      return x > WEST_WALL + 1 && x < EAST_WALL;
   }

   @Override
   protected Codec<? extends ChunkGenerator> codec() {
      return CODEC;
   }

   @Override
   public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState random,
                                                       StructureManager structures, ChunkAccess chunk) {
      int minX = chunk.getPos().getMinBlockX();
      if (minX > EAST_WALL || minX + 15 < WEST_WALL) {
         return CompletableFuture.completedFuture(chunk);
      }

      BlockState barrier = Blocks.BARRIER.defaultBlockState();
      Heightmap floor = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
      Heightmap surface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
      BlockPos.MutableBlockPos at = new BlockPos.MutableBlockPos();

      int bottom = chunk.getMinBuildHeight();
      int top = chunk.getMaxBuildHeight();

      for (int lx = 0; lx < 16; lx++) {
         int x = minX + lx;
         boolean wall = x == WEST_WALL || x == EAST_WALL;
         boolean lane = x > WEST_WALL && x < EAST_WALL;
         if (!wall && !lane) continue;

         for (int lz = 0; lz < 16; lz++) {
            if (wall) {
               for (int y = bottom; y < top; y++) {
                  at.set(x, y, chunk.getPos().getMinBlockZ() + lz);
                  chunk.setBlockState(at, barrier, false);
                  floor.update(lx, y, lz, barrier);
                  surface.update(lx, y, lz, barrier);
               }
            } else {
               at.set(x, FLOOR, chunk.getPos().getMinBlockZ() + lz);
               chunk.setBlockState(at, barrier, false);
               floor.update(lx, FLOOR, lz, barrier);
               surface.update(lx, FLOOR, lz, barrier);
            }
         }
      }
      return CompletableFuture.completedFuture(chunk);
   }

   @Override
   public void applyCarvers(WorldGenRegion region, long seed, RandomState random, BiomeManager biomes,
                            StructureManager structures, ChunkAccess chunk, GenerationStep.Carving step) {
   }

   @Override
   public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState random, ChunkAccess chunk) {
   }

   @Override
   public void spawnOriginalMobs(WorldGenRegion region) {
   }

   @Override
   public int getGenDepth() {
      return 1024;
   }

   @Override
   public int getSeaLevel() {
      return -63;
   }

   @Override
   public int getMinY() {
      return 0;
   }

   @Override
   public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor level, RandomState random) {
      if (x == WEST_WALL || x == EAST_WALL) return level.getMaxBuildHeight();
      return x > WEST_WALL && x < EAST_WALL ? FLOOR + 1 : level.getMinBuildHeight();
   }

   @Override
   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState random) {
      BlockState[] column = new BlockState[level.getHeight()];
      BlockState barrier = Blocks.BARRIER.defaultBlockState();
      BlockState air = Blocks.AIR.defaultBlockState();
      boolean wall = x == WEST_WALL || x == EAST_WALL;
      boolean lane = x > WEST_WALL && x < EAST_WALL;

      for (int i = 0; i < column.length; i++) {
         int y = level.getMinBuildHeight() + i;
         column[i] = wall || (lane && y == FLOOR) ? barrier : air;
      }
      return new NoiseColumn(level.getMinBuildHeight(), column);
   }

   @Override
   public void addDebugScreenInfo(List<String> lines, RandomState random, BlockPos pos) {
   }
}
