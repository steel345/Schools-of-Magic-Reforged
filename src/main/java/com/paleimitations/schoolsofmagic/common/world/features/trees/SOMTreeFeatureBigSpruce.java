package com.paleimitations.schoolsofmagic.common.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureBigSpruce extends SOMBigTreeFeature {

   private static final RegistryObject<Block> LOG    = RegistryObject.create(new ResourceLocation("spruce_log"),    ForgeRegistries.BLOCKS);
   private static final RegistryObject<Block> LEAVES = RegistryObject.create(new ResourceLocation("spruce_leaves"), ForgeRegistries.BLOCKS);

   @Override protected RegistryObject<Block> log()    { return LOG; }
   @Override protected RegistryObject<Block> leaves() { return LEAVES; }

   @Override
   protected void placeFringeAt(WorldGenLevel w, BlockPos pos) {
      placeLeafAt(w, pos);
      placeLeafAt(w, pos.above());  placeLeafAt(w, pos.below());
      placeLeafAt(w, pos.north());  placeLeafAt(w, pos.south());
      placeLeafAt(w, pos.east());   placeLeafAt(w, pos.west());
   }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(5) + 6;
      int j = rand.nextInt(3);
      int x = position.getX(), y = position.getY(), z = position.getZ();
      int num = rand.nextInt(3) + 1;
      int sizeSize = i + y;
      int treeSize = num * 7 + 23 + i;
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 1 >= maxHeight(world)) return false;
      BlockPos down = position.below();
      if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
      if (!clearance(world, position, i, treeSize)) return false;
      if (!isSoil(world, down) || y >= maxHeight(world) - i - 1) return false;

      onPlantGrow(world, down, position);

      leafBox(world, position, -2, i, -1, 2, i, 1);
      leafBox(world, position, -1, i, -2, 1, i, 2);
      placeTrunkColumn(world, position, i);

      for (int r = 0; r < num; r++) placeTier4(world, new BlockPos(x, sizeSize + r * 7 + 1, z));
      int t3 = sizeSize + num * 7 + 1, t2 = t3 + 6, t1 = t2 + 4;
      placeTier1(world, new BlockPos(x, t1, z));
      placeTier2(world, new BlockPos(x, t2, z));
      placeTier3(world, new BlockPos(x, t3, z));

      cornerStub(world, rand, x - 1, y, z - 1);
      cornerStub(world, rand, x + 1, y, z - 1);
      cornerStub(world, rand, x - 1, y, z + 1);
      cornerStub(world, rand, x + 1, y, z + 1);

      groundRoot(world, rand, x, y, z,  1, 0, rand.nextInt(j + 1) + 1);
      groundRoot(world, rand, x, y, z, -1, 0, rand.nextInt(j + 1) + 1);
      groundRoot(world, rand, x, y, z,  0, 1, rand.nextInt(j + 1) + 1);
      groundRoot(world, rand, x, y, z,  0, -1, rand.nextInt(j + 1) + 1);
      return true;
   }

   private boolean clearance(WorldGenLevel world, BlockPos pos, int iInt, int height) {
      int i = pos.getX(), j = pos.getY(), k = pos.getZ();
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int l = 0; l <= height + 1; ++l) {
         int radius = (l <= iInt) ? 1 : 4;
         for (int j1 = -radius; j1 <= radius; ++j1)
            for (int k1 = -radius; k1 <= radius; ++k1)
               if (!isReplaceable(world, m.set(i + j1, j + l, k + k1))) return false;
      }
      return true;
   }

   private void cornerStub(WorldGenLevel world, RandomSource rand, int x, int y, int z) {
      if (rand.nextInt(2) != 0) return;
      int top = rand.nextInt(3);
      for (int a = -2; a <= top; ++a) placeLogMaybe(world, new BlockPos(x, y + a, z));
   }

   private void groundRoot(WorldGenLevel world, RandomSource rand, int x, int y, int z, int dx, int dz, int num) {
      if (world.isEmptyBlock(new BlockPos(x + dx * 4, y - 5, z + dz * 4))) return;
      int n = num;
      for (int a = 0; a <= 3; ++a) {
         for (int b = -4; b <= n; ++b) placeLogMaybe(world, new BlockPos(x + dx * (a + 1), y + b, z + dz * (a + 1)));
         n = n - rand.nextInt(2) - 1;
      }
   }

   private void branchCross(WorldGenLevel w, BlockPos pos, int up, int armX, int armZ, int diag) {
      for (int i = -armX; i <= armX; ++i) set(w, pos.offset(i, up, 0), trunkX());
      for (int i = -armZ; i <= armZ; ++i) set(w, pos.offset(0, up, i), trunkZ());
      for (int i = -diag; i <= diag; ++i) set(w, pos.offset(i, up, i),  trunkX());
      for (int i = -diag; i <= diag; ++i) set(w, pos.offset(i, up, -i), trunkZ());
   }

   private void placeTier1(WorldGenLevel w, BlockPos pos) {
      placeFringeAt(w, pos.above(9));
      placeLeafAt(w, pos.above(11));
      leafBox(w, pos, -1,0,-2, 1,4,2); leafBox(w, pos, -2,0,-1, 2,4,1);
      leafBox(w, pos, -4,0,-2, 4,0,2); leafBox(w, pos, -2,0,-4, 2,0,4); leafBox(w, pos, -3,0,-3, 3,0,3);
      leafBox(w, pos, -3,1,-1, 3,1,1); leafBox(w, pos, -1,1,-3, 1,1,3); leafBox(w, pos, -2,1,-2, 2,1,2);
      leafBox(w, pos, -3,3,-1, 3,3,1); leafBox(w, pos, -1,3,-3, 1,3,3); leafBox(w, pos, -2,3,-2, 2,3,2);
      leafBox(w, pos, -1,5,0, 1,7,0); leafBox(w, pos, 0,5,-1, 0,7,1);
      leafBox(w, pos, -1,6,-2, 1,6,2); leafBox(w, pos, -2,6,-1, 2,6,1);
      for (int k = 0; k <= 7; ++k) set(w, pos.above(k), trunkY());
      set(w, pos.east(), trunkX()); set(w, pos.west(), trunkX());
      set(w, pos.north(), trunkZ()); set(w, pos.south(), trunkZ());
   }

   private void placeTier2(WorldGenLevel w, BlockPos pos) {
      leafBox(w, pos, -6,0,-1, 6,0,1); leafBox(w, pos, -1,0,-6, 1,0,6);
      leafBox(w, pos, -5,0,-3, 5,0,3); leafBox(w, pos, -3,0,-5, 3,0,5); leafBox(w, pos, -4,0,-4, 4,0,4);
      leafBox(w, pos, -5,1,0, 5,1,0); leafBox(w, pos, 0,1,-5, 0,1,5);
      leafBox(w, pos, -4,1,-2, 4,1,2); leafBox(w, pos, -2,1,-4, 2,1,4); leafBox(w, pos, -3,1,-3, 3,1,3);
      leafBox(w, pos, -3,2,-1, 3,2,1); leafBox(w, pos, -1,2,-3, 1,2,3); leafBox(w, pos, -2,2,-2, 2,2,2);
      leafBox(w, pos, -2,3,-1, 2,3,1); leafBox(w, pos, -1,3,-2, 1,3,2);
      branchCross(w, pos, 1, 3, 3, 2);
      for (int k = 0; k < 4; ++k) set(w, pos.above(k), trunkY());
   }

   private void placeTier3(WorldGenLevel w, BlockPos pos) {
      leafBox(w, pos, -5,0,0, 5,0,0); leafBox(w, pos, 0,0,-5, 0,0,5);
      leafBox(w, pos, -4,0,-2, 4,3,2); leafBox(w, pos, -2,0,-4, 2,3,4); leafBox(w, pos, -3,0,-3, 3,3,3);
      leafBox(w, pos, -7,1,-1, 7,1,1); leafBox(w, pos, -1,1,-7, 1,1,7);
      leafBox(w, pos, -6,1,-3, 6,1,3); leafBox(w, pos, -3,1,-6, 3,1,6); leafBox(w, pos, -5,1,-4, 5,1,4); leafBox(w, pos, -4,1,-5, 4,1,5);
      leafBox(w, pos, -6,2,0, 6,2,0); leafBox(w, pos, 0,2,-6, 0,2,6);
      leafBox(w, pos, -5,2,-3, 5,2,3); leafBox(w, pos, -3,2,-5, 3,2,5); leafBox(w, pos, -4,2,-4, 4,2,4);
      leafBox(w, pos, -3,4,-1, 3,4,1); leafBox(w, pos, -1,4,-3, 1,4,3); leafBox(w, pos, -2,4,-2, 2,4,2);
      leafBox(w, pos, -2,5,-1, 2,5,1); leafBox(w, pos, -1,5,-2, 1,5,2);
      branchCross(w, pos, 2, 4, 4, 3);
      for (int k = 0; k < 6; ++k) set(w, pos.above(k), trunkY());
   }

   private void placeTier4(WorldGenLevel w, BlockPos pos) {
      leafBox(w, pos, -5,0,0, 5,0,0); leafBox(w, pos, 0,0,-5, 0,0,5);
      leafBox(w, pos, -4,0,-2, 4,4,2); leafBox(w, pos, -2,0,-4, 2,4,4); leafBox(w, pos, -3,0,-3, 3,4,3);
      leafBox(w, pos, -8,1,-2, 8,1,2); leafBox(w, pos, -2,1,-8, 2,1,8);
      leafBox(w, pos, -7,1,-4, 7,1,4); leafBox(w, pos, -4,1,-7, 4,1,7); leafBox(w, pos, -6,1,-5, 6,1,5); leafBox(w, pos, -5,1,-6, 5,1,6);
      leafBox(w, pos, -7,2,-1, 7,2,1); leafBox(w, pos, -1,2,-7, 1,2,7);
      leafBox(w, pos, -6,2,-3, 6,2,3); leafBox(w, pos, -3,2,-6, 3,2,6); leafBox(w, pos, -5,2,-4, 5,2,4); leafBox(w, pos, -4,2,-5, 4,2,5);
      leafBox(w, pos, -6,3,0, 6,3,0); leafBox(w, pos, 0,3,-6, 0,3,6);
      leafBox(w, pos, -5,3,-3, 5,3,3); leafBox(w, pos, -3,3,-5, 3,3,5); leafBox(w, pos, -4,3,-4, 4,3,4);
      leafBox(w, pos, -3,5,-1, 3,5,1); leafBox(w, pos, -1,5,-3, 1,5,3); leafBox(w, pos, -2,5,-2, 2,5,2);
      leafBox(w, pos, -2,6,-1, 2,6,1); leafBox(w, pos, -1,6,-2, 1,6,2);
      branchCross(w, pos, 2, 6, 6, 4);
      for (int k = 0; k < 7; ++k) set(w, pos.above(k), trunkY());
   }
}
