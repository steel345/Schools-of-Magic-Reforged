package com.paleimitations.schoolsofmagic.common.handlers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KnowledgeGather {
   public static final int RADIUS = 60;
   public static final int CAP = 256;

   public static class Found {
      public final ItemStack stack;
      public final BlockPos shelf;
      public final int slot;

      public Found(ItemStack stack, BlockPos shelf, int slot) {
         this.stack = stack;
         this.shelf = shelf;
         this.slot = slot;
      }
   }

   public static List<Found> gather(Level level, BlockPos origin) {
      List<Found> out = new ArrayList<>();
      if (level == null || origin == null) return out;
      int r = RADIUS;
      int cxMin = (origin.getX() - r) >> 4;
      int cxMax = (origin.getX() + r) >> 4;
      int czMin = (origin.getZ() - r) >> 4;
      int czMax = (origin.getZ() + r) >> 4;
      for (int cx = cxMin; cx <= cxMax && out.size() < CAP; cx++) {
         for (int cz = czMin; cz <= czMax && out.size() < CAP; cz++) {
            ChunkAccess ca = level.getChunk(cx, cz, ChunkStatus.FULL, false);
            if (!(ca instanceof LevelChunk chunk)) continue;
            for (Map.Entry<BlockPos, BlockEntity> e : chunk.getBlockEntities().entrySet()) {
               if (!(e.getValue() instanceof ChiseledBookShelfBlockEntity shelf)) continue;
               BlockPos p = e.getKey();
               if (Math.abs(p.getX() - origin.getX()) > r
                     || Math.abs(p.getY() - origin.getY()) > r
                     || Math.abs(p.getZ() - origin.getZ()) > r) continue;
               for (int slot = 0; slot < shelf.getContainerSize() && out.size() < CAP; slot++) {
                  ItemStack st = shelf.getItem(slot);
                  if (!st.isEmpty()) out.add(new Found(st.copy(), p.immutable(), slot));
               }
            }
         }
      }
      return out;
   }
}
