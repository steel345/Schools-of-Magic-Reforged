package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SOMBigTreeFeature extends SOMTreeFeature {

   protected boolean placeTreeOfHeight(WorldGenLevel world, BlockPos pos, int iInt, int hInt, int height) {
      int i = pos.getX(), j = pos.getY(), k = pos.getZ();
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int l = 0; l <= height + 1; ++l) {
         int radius = (l <= hInt) ? 1 : (6 + iInt);
         for (int j1 = -radius; j1 <= radius; ++j1) {
            for (int k1 = -radius; k1 <= radius; ++k1) {
               if (!isReplaceable(world, m.set(i + j1, j + l, k + k1))) return false;
            }
         }
      }
      return true;
   }

   protected void placeCanopy(WorldGenLevel world, BlockPos pos) {
      int x = pos.getX(), y = pos.getY(), z = pos.getZ();
      for (int a = -1; a <= 1; ++a)
         for (int b = 0; b <= 2; ++b)
            for (int c = -2; c <= 2; ++c) {
               placeLeafAt(world, new BlockPos(x + a, y + b, z + c));
               placeLeafAt(world, new BlockPos(x + c, y + b, z + a));
            }
      for (int a = -1; a <= 1; ++a)
         for (int c = -3; c <= 3; ++c) {
            placeLeafAt(world, new BlockPos(x + a, y + 1, z + c));
            placeLeafAt(world, new BlockPos(x + c, y + 1, z + a));
         }
      for (int a = -2; a <= 2; ++a)
         for (int c = -2; c <= 2; ++c) {
            placeLeafAt(world, new BlockPos(x + a, y + 1, z + c));
            placeLeafAt(world, new BlockPos(x + c, y + 1, z + a));
         }
      placeFringeAt(world, new BlockPos(x, y + 3, z));
      for (int kk = 0; kk <= 2; ++kk) set(world, new BlockPos(x, y + kk, z), trunkY());
   }

   protected void placeFringeAt(WorldGenLevel world, BlockPos pos) {
      placeLeafAt(world, pos);
      placeLeafAt(world, pos.north());
      placeLeafAt(world, pos.south());
      placeLeafAt(world, pos.east());
      placeLeafAt(world, pos.west());
   }

   protected void placeTrunkColumn(WorldGenLevel world, BlockPos position, int top) {
      for (int a = 0; a <= top; ++a) {
         placeLogAt(world, position.above(a));
         placeLogMaybe(world, position.east().above(a));
         placeLogMaybe(world, position.west().above(a));
         placeLogMaybe(world, position.south().above(a));
         placeLogMaybe(world, position.north().above(a));
         placeLogMaybe(world, position.east().below(a / 4));
         placeLogMaybe(world, position.west().below(a / 4));
         placeLogMaybe(world, position.south().below(a / 4));
         placeLogMaybe(world, position.north().below(a / 4));
      }
   }

   protected void placeStubRoots(WorldGenLevel world, RandomSource rand, BlockPos position) {
      stub(world, rand, position, -1, -1, 2); stub(world, rand, position, 1, -1, 2);
      stub(world, rand, position, -1,  1, 2); stub(world, rand, position, 1,  1, 2);
      stub(world, rand, position, -2,  0, 3); stub(world, rand, position, 2,  0, 3);
      stub(world, rand, position,  0, -2, 3); stub(world, rand, position, 0,  2, 3);
   }

   private void stub(WorldGenLevel world, RandomSource rand, BlockPos position, int dx, int dz, int chanceDenom) {
      if (rand.nextInt(chanceDenom) != 0) return;
      int bottom = (chanceDenom == 2) ? -2 : -3;
      int top = (chanceDenom == 2) ? rand.nextInt(2) : rand.nextInt(1);
      for (int a = bottom; a <= top; ++a) placeLogMaybe(world, position.offset(dx, a, dz));
   }

   protected void leafBox(WorldGenLevel w, BlockPos pos, int x1, int y1, int z1, int x2, int y2, int z2) {
      for (BlockPos p : BlockPos.betweenClosed(pos.offset(x1, y1, z1), pos.offset(x2, y2, z2))) placeLeafAt(w, p);
   }

   protected void drawLimbToCore(WorldGenLevel world, BlockPos canopyCore, BlockPos canopyPos, int limbLength) {
      int xDif = canopyPos.getX() - canopyCore.getX();
      int zDif = canopyPos.getZ() - canopyCore.getZ();
      int yDif = canopyPos.getY() - canopyCore.getY();
      int dd = 0;
      while (xDif != 0 || zDif != 0) {
         BlockState log = trunkY();
         int xx = 0, zz = 0;
         if (xDif > 0 && zDif > 0) xx = 1;
         if (xDif > 0 && zDif < 0) zz = 1;
         if (xDif < 0 && zDif > 0) zz = 1;
         if (xDif < 0 && zDif < 0) xx = 1;
         if (xDif == 0) { zz = 1; xx = 0; }
         if (zDif == 0) { zz = 0; xx = 1; }
         if (dd >= limbLength) {
            if (xDif > 0 && zDif > 0) log = trunkX();
            if (xDif > 0 && zDif < 0) log = trunkZ();
            if (xDif < 0 && zDif > 0) log = trunkZ();
            if (xDif < 0 && zDif < 0) log = trunkX();
            if (xDif == 0) log = trunkZ();
            if (zDif == 0) log = trunkX();
         }
         placeLog1At(world, canopyCore.offset(xDif, yDif - dd, zDif), log);
         placeLog1At(world, canopyCore.offset(xDif + xx, yDif - dd, zDif + zz), log);
         if (xDif != 0) xDif -= xDif / Math.abs(xDif);
         if (zDif != 0) zDif -= zDif / Math.abs(zDif);
         if (dd <= limbLength) dd++;
      }
   }

   protected void placeRoot(WorldGenLevel w, BlockPos pos, net.minecraft.core.Direction facing, RandomSource rand, int magnitude) {
      if (magnitude <= 0) return;
      BlockState state = (facing.getAxis() == net.minecraft.core.Direction.Axis.Z) ? trunkZ() : trunkX();
      int length = magnitude;
      boolean proceed = false;
      int drop = 0;
      for (int i = 0; i <= 10; ++i) {
         drop++;
         if (!canOverwriteLogMaybe(w.getBlockState(pos.relative(facing, length).below(i)))) { proceed = true; break; }
      }
      for (int ix = 0; ix <= length; ++ix) {
         if (!canOverwriteLogMaybe(w.getBlockState(pos.relative(facing, ix)))) { proceed = false; break; }
      }
      if (!proceed) return;
      boolean canNode = false;
      for (int ixx = 0; ixx < length; ++ixx) {
         BlockPos logPos = pos.relative(facing, ixx);
         set(w, logPos, state);
         boolean prev = canNode;
         if (!canNode) canNode = drop > 0;
         if (prev && rand.nextDouble() > 0.45 && ixx > 0) {
            switch (rand.nextInt(3)) {
               case 0: if (ixx < length - 1) dropRoot(w, logPos.relative(facing, ixx).below(), rand); break;
               case 1: placeRoot(w, logPos.relative(facing.getClockWise()), facing.getClockWise(), rand, magnitude - (rand.nextInt(2) + 1)); break;
               default: placeRoot(w, logPos.relative(facing.getCounterClockWise()), facing.getCounterClockWise(), rand, magnitude - (rand.nextInt(2) + 1));
            }
            canNode = false;
         }
      }
      canNode = false;
      for (int ixx = 1; ixx <= drop; ++ixx) {
         BlockPos logPosx = pos.relative(facing, length).below(ixx);
         placeLogMaybe(w, logPosx);
         boolean prev = canNode;
         if (!canNode) canNode = drop > 0;
         if (prev && rand.nextDouble() > 0.45) {
            switch (rand.nextInt(3)) {
               case 0: placeRoot(w, logPosx.relative(facing), facing, rand, magnitude - (rand.nextInt(2) + 1)); break;
               case 1: placeRoot(w, logPosx.relative(facing.getClockWise()), facing.getClockWise(), rand, magnitude - (rand.nextInt(2) + 1)); break;
               default: placeRoot(w, logPosx.relative(facing.getCounterClockWise()), facing.getCounterClockWise(), rand, magnitude - (rand.nextInt(2) + 1));
            }
            canNode = false;
         }
      }
   }

   private void dropRoot(WorldGenLevel w, BlockPos pos, RandomSource rand) {
      boolean proceed = false;
      int drop = 0;
      for (int i = 0; i < 10; ++i) {
         drop++;
         if (!canOverwriteLogMaybe(w.getBlockState(pos))) { proceed = true; break; }
      }
      if (proceed) for (int ix = 0; ix < drop; ++ix) placeLogMaybe(w, pos.below(ix));
   }

   protected void drawCanopy(WorldGenLevel world, RandomSource rand, BlockPos canopyCore,
         int minR, int maxR, int vLow, int iterations, boolean placeInitialTop) {
      List<BlockPos> acceptable = Lists.newArrayList();
      for (BlockPos ph : BlockPos.betweenClosed(canopyCore.offset(-maxR, vLow, -maxR), canopyCore.offset(maxR, maxR, maxR))) {
         double d = Utils.getDistance(ph, canopyCore);
         if (d <= maxR && d >= minR) acceptable.add(ph.immutable());
      }
      if (acceptable.isEmpty()) return;
      List<BlockPos> placed = Lists.newArrayList();
      if (placeInitialTop) { placeCanopy(world, canopyCore.above(maxR)); placed.add(canopyCore.above(maxR)); }
      for (int a = 0; a <= iterations; ++a) {
         int p = rand.nextInt(acceptable.size());
         for (BlockPos loc : placed) {
            int guard = 0;
            while (Utils.getDistance(acceptable.get(p), loc) < 3.0 && guard++ < 64) p = rand.nextInt(acceptable.size());
         }
         BlockPos canopyPos = acceptable.get(p);
         placed.add(canopyPos);
         int xDif = canopyPos.getX() - canopyCore.getX();
         int zDif = canopyPos.getZ() - canopyCore.getZ();
         int yDif = canopyPos.getY() - canopyCore.getY();
         int limbLength = rand.nextInt(2) + 1;
         int dd = 0;
         placeCanopy(world, canopyPos);
         while (xDif != 0 || zDif != 0) {
            BlockState log = trunkY();
            int xx = 0, zz = 0;
            if (xDif > 0 && zDif > 0) xx = 1;
            if (xDif > 0 && zDif < 0) zz = 1;
            if (xDif < 0 && zDif > 0) zz = 1;
            if (xDif < 0 && zDif < 0) xx = 1;
            if (xDif == 0) { zz = 1; xx = 0; }
            if (zDif == 0) { zz = 0; xx = 1; }
            if (dd < limbLength) {
               log = trunkY();
            } else {
               if (xDif > 0 && zDif > 0) log = trunkX();
               if (xDif > 0 && zDif < 0) log = trunkZ();
               if (xDif < 0 && zDif > 0) log = trunkZ();
               if (xDif < 0 && zDif < 0) log = trunkX();
               if (xDif == 0) log = trunkZ();
               if (zDif == 0) log = trunkX();
            }
            placeLog1At(world, canopyCore.offset(xDif, yDif - dd, zDif), log);
            placeLog1At(world, canopyCore.offset(xDif + xx, yDif - dd, zDif + zz), log);
            if (xDif != 0) xDif -= xDif / Math.abs(xDif);
            if (zDif != 0) zDif -= zDif / Math.abs(zDif);
            if (dd <= limbLength) dd++;
         }
      }
   }
}
