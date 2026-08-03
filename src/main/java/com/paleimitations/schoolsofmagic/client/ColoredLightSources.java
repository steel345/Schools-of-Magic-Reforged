package com.paleimitations.schoolsofmagic.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

// The world's coloured lights. Candidates are found by an occasional sweep of the
// nearby chunks, but their colour and whether they are lit at all is read fresh every
// frame, so a fire that goes out stops lighting the room on the very next one.
public class ColoredLightSources {

   public record Source(double x, double y, double z, float r, float g, float b,
                        float radius, float strength) {}


   private static final List<BlockEntity> CANDIDATES = new ArrayList<>();
   private static final List<Source> LIVE = new ArrayList<>();

   public static void clearCandidates() {
      CANDIDATES.clear();
   }

   public static void addCandidate(BlockEntity be) {
      if (tintOf(be) != -1 || couldLight(be)) CANDIDATES.add(be);
   }

   public static int candidateCount() {
      return CANDIDATES.size();
   }

   // Rebuilt each frame from the current state of each candidate.
   public static List<Source> live() {
      LIVE.clear();
      for (BlockEntity be : CANDIDATES) {
         if (be.isRemoved()) continue;
         int tint = tintOf(be);
         if (tint == -1) continue;
         BlockPos pos = be.getBlockPos();
         LIVE.add(new Source(
            pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D,
            (tint >> 16 & 0xFF) / 255.0F,
            (tint >> 8 & 0xFF) / 255.0F,
            (tint & 0xFF) / 255.0F,
            com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLightRadius(),
            com.paleimitations.schoolsofmagic.common.config.SOMClientConfig.coloredLightStrength()));
      }
      return LIVE;
   }

   // Worth keeping in the list even while dark, since it may be lit again.
   private static boolean couldLight(BlockEntity be) {
      return be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter;
   }

   private static int tintOf(BlockEntity be) {
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter rc) {
         if (!isLitBrazier(rc)) return -1;
         return rc.getFireTint();
      }
      return -1;
   }

   private static boolean isLitBrazier(com.paleimitations.schoolsofmagic.common.tileentity.TileEntityRitualCenter rc) {
      if (rc.getLevel() == null) return false;
      net.minecraft.world.level.block.state.BlockState state = rc.getLevel().getBlockState(rc.getBlockPos());
      return state.hasProperty(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME)
         && state.getValue(com.paleimitations.schoolsofmagic.common.blocks.BlockBrazier.FLAME) > 0;
   }
}
