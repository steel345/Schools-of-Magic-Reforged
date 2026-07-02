package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeaturePalm extends SOMTreeFeature {

   @Override protected RegistryObject<Block> log()    { return BlockRegistry.log_palm; }
   @Override protected RegistryObject<Block> leaves() { return BlockRegistry.leaves_palm; }

   private boolean clearance(WorldGenLevel world, BlockPos pos, int height) {
      int i = pos.getX(), j = pos.getY(), k = pos.getZ();
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int l = 1; l <= height + 1; ++l)
         for (int j1 = -1; j1 <= 1; ++j1)
            for (int k1 = -1; k1 <= 1; ++k1)
               if (!isReplaceable(world, m.set(i + j1, j + l, k + k1))) return false;
      return true;
   }

   private void frond(WorldGenLevel world, BlockPos pos, Direction facing) {
      world.setBlock(pos, BlockRegistry.leaves_palm.get().defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing), 3);
   }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int y = position.getY();
      int treeSize = 0;
      for (int i = 1; i < 15; ++i) {
         treeSize++;
         if (!world.isEmptyBlock(position.above(i)) || y + i + 1 > maxHeight(world) || rand.nextInt(15 - i) == 0) break;
      }
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 1 >= maxHeight(world)) return false;
      if (y >= maxHeight(world) - treeSize - 1) return false;
      if (!clearance(world, position, treeSize + 3)) return false;

      BlockPos down = position.below();
      onPlantGrow(world, down, position);

      for (int ix = 0; ix < treeSize; ++ix) {
         set(world, position.above(ix), trunkY());
         if (rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean()) {
            Direction facing = Direction.from2DDataValue(rand.nextInt(4));
            BlockPos cp = position.above(ix).relative(facing);
            if (world.isEmptyBlock(cp)) {
               world.setBlock(cp, BlockRegistry.coconut.get().defaultBlockState()
                  .setValue(HorizontalDirectionalBlock.FACING, facing.getOpposite()), 3);
            }
         }
      }

      BlockPos top = position.above(treeSize);
      set(world, top, BlockRegistry.palm_top.get().defaultBlockState());
      frond(world, top.east(),  Direction.WEST);
      frond(world, top.west(),  Direction.EAST);
      frond(world, top.south(), Direction.NORTH);
      frond(world, top.north(), Direction.SOUTH);

      if (rand.nextBoolean() && treeSize < 6) {
         set(world, top.above(), BlockRegistry.sapling_palm.get().defaultBlockState());
      }
      return true;
   }
}
