package com.paleimitations.schoolsofmagic.common.util;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class FaegrovePortalShape {

   private FaegrovePortalShape() {}

   public static class Result {
      public final BlockPos lowerInterior;
      public final Direction.Axis axis;
      public final int through;
      public final int u;
      public final int v;

      public Result(BlockPos lowerInterior, Direction.Axis axis, int through, int u, int v) {
         this.lowerInterior = lowerInterior;
         this.axis = axis;
         this.through = through;
         this.u = u;
         this.v = v;
      }
   }

   public static boolean isAcolyteWood(BlockState state) {
      return state.is(BlockRegistry.wood_ash.get())
          || state.is(BlockRegistry.stripped_wood_ash.get());
   }

   public static boolean isInterior(LevelAccessor level, BlockPos pos) {
      BlockState s = level.getBlockState(pos);
      return s.isAir() || s.is(BlockRegistry.faegrove_portal.get());
   }

   private static BlockPos at(Direction.Axis axis, int u, int y, int through) {
      if (axis == Direction.Axis.X) {
         return new BlockPos(u, y, through);
      }
      return new BlockPos(through, y, u);
   }

   public static Result validateFromClick(Level level, BlockPos clicked, Direction face) {
      for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
         Direction.Axis through = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
         if (face.getAxis() == through) continue;
         BlockPos air = clicked.relative(face);
         if (!isInterior(level, air)) continue;
         int wallThrough = (through == Direction.Axis.X) ? air.getX() : air.getZ();
         int au = (axis == Direction.Axis.X) ? air.getX() : air.getZ();
         int av = air.getY();
         for (int u = au - 1; u <= au; u++) {
            for (int v = av - 1; v <= av; v++) {
               if (checkFrame(level, axis, through, wallThrough, u, v)) {
                  return new Result(at(axis, u, v, wallThrough), axis, wallThrough, u, v);
               }
            }
         }
      }
      return null;
   }

   private static boolean checkFrame(Level level, Direction.Axis axis, Direction.Axis through, int t, int uStart, int vStart) {
      for (int du = 0; du < 2; du++) {
         for (int dv = 0; dv < 2; dv++) {
            if (!isInterior(level, at(axis, uStart + du, vStart + dv, t))) return false;
         }
      }
      for (int u = uStart - 1; u <= uStart + 2; u++) {
         for (int v = vStart - 1; v <= vStart + 2; v++) {
            boolean interior = (u >= uStart && u <= uStart + 1 && v >= vStart && v <= vStart + 1);
            if (interior) continue;
            boolean corner = (u == uStart - 1 || u == uStart + 2) && (v == vStart - 1 || v == vStart + 2);
            if (corner) continue;
            if (!isAcolyteWood(level.getBlockState(at(axis, u, v, t)))) return false;
         }
      }
      return true;
   }

   public static void place(Level level, Result r) {
      BlockState portal = BlockRegistry.faegrove_portal.get().defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_AXIS, r.axis);
      for (int du = 0; du < 2; du++) {
         for (int dv = 0; dv < 2; dv++) {
            level.setBlock(at(r.axis, r.u + du, r.v + dv, r.through), portal, 18);
         }
      }
   }

   public static BlockPos buildFrame(ServerLevel level, int ix, int iz, int groundY) {
      Direction.Axis axis = Direction.Axis.X;
      Direction.Axis through = Direction.Axis.Z;
      int t = iz;
      int uStart = ix;
      int vStart = groundY;
      BlockState wood = BlockRegistry.wood_ash.get().defaultBlockState();
      BlockState portal = BlockRegistry.faegrove_portal.get().defaultBlockState()
            .setValue(BlockStateProperties.HORIZONTAL_AXIS, axis);
      for (int u = uStart - 1; u <= uStart + 2; u++) {
         for (int v = vStart - 1; v <= vStart + 2; v++) {
            boolean interior = (u >= uStart && u <= uStart + 1 && v >= vStart && v <= vStart + 1);
            BlockPos p = at(axis, u, v, t);
            level.setBlock(p, interior ? portal : wood, 18);
         }
      }
      for (int u = uStart; u <= uStart + 1; u++) {
         for (int v = vStart; v <= vStart + 1; v++) {
            for (int d = -1; d <= 1; d += 2) {
               BlockPos p = at(axis, u, v, t + d);
               if (!level.getBlockState(p).isAir()) {
                  level.setBlock(p, Blocks.AIR.defaultBlockState(), 18);
               }
            }
         }
      }
      return at(axis, uStart, vStart, t);
   }
}
