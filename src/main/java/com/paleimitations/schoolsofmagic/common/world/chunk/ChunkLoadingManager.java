package com.paleimitations.schoolsofmagic.common.world.chunk;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.world.ForgeChunkManager;

public class ChunkLoadingManager {
   public static ChunkLoadingManager instance = new ChunkLoadingManager();

   public void forceChunkLoading(Level world, ChunkPos chunkPos) {
      this.setForced(world, chunkPos, true);
   }

   public void unforceChunkLoading(Level world, ChunkPos chunkPos) {
      this.setForced(world, chunkPos, false);
   }

   private void setForced(Level world, ChunkPos chunkPos, boolean add) {
      if (!(world instanceof ServerLevel level)) {
         return;
      }

      BlockPos owner = new BlockPos(chunkPos.getMiddleBlockX(), 0, chunkPos.getMiddleBlockZ());
      for (int x = chunkPos.x - 1; x <= chunkPos.x + 1; x++) {
         for (int z = chunkPos.z - 1; z <= chunkPos.z + 1; z++) {
            ForgeChunkManager.forceChunk(level, SchoolsOfMagic.MODID, owner, x, z, add, true);
         }
      }
   }
}
