package com.paleimitations.schoolsofmagic.common.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureBigAcacia extends SOMBigTreeFeature {

   private static final RegistryObject<Block> LOG    = RegistryObject.create(new ResourceLocation("acacia_log"),    ForgeRegistries.BLOCKS);
   private static final RegistryObject<Block> LEAVES = RegistryObject.create(new ResourceLocation("acacia_leaves"), ForgeRegistries.BLOCKS);

   @Override protected RegistryObject<Block> log()    { return LOG; }
   @Override protected RegistryObject<Block> leaves() { return LEAVES; }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(4) + 2;
      int rootMod = i * 2 / 3 + 1;
      int treeSize = i + 9;
      int x = position.getX(), y = position.getY(), z = position.getZ();
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 20 >= maxHeight(world)) return false;
      BlockPos down = position.below();
      if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
      if (!placeTreeOfHeight(world, position, i, treeSize - 2, treeSize)) return false;
      if (!isSoil(world, down) || y >= maxHeight(world) - i - 1) return false;

      onPlantGrow(world, down, position);
      int canopyMaxRadius = 8 + i;
      drawCanopy(world, rand, position.above(3 + i), 7 + i, canopyMaxRadius, 6, 19 + i * 2, true);

      placeTrunkColumn(world, position, 3 + i + canopyMaxRadius);

      for (int a = 0; a <= 5 + i; ++a) {
         placeLogMaybe(world, new BlockPos(x + 1, y + a, z + 1));
         placeLogMaybe(world, new BlockPos(x - 1, y + a, z - 1));
         placeLogMaybe(world, new BlockPos(x + 1, y + a, z - 1));
         placeLogMaybe(world, new BlockPos(x - 1, y + a, z + 1));
         placeLogMaybe(world, new BlockPos(x + 1, y - a / 2, z + 1));
         placeLogMaybe(world, new BlockPos(x - 1, y - a / 2, z - 1));
         placeLogMaybe(world, new BlockPos(x + 1, y - a / 2, z - 1));
         placeLogMaybe(world, new BlockPos(x - 1, y - a / 2, z + 1));
      }

      placeRoot(world, position.relative(Direction.EAST, 2).above(rootMod - rand.nextInt(3)),  Direction.EAST,  rand, i / 2 - rand.nextInt(2) + 1);
      placeRoot(world, position.relative(Direction.NORTH, 2).above(rootMod - rand.nextInt(3)), Direction.NORTH, rand, i / 2 - rand.nextInt(2) + 1);
      placeRoot(world, position.relative(Direction.SOUTH, 2).above(rootMod - rand.nextInt(3)), Direction.SOUTH, rand, i / 2 - rand.nextInt(2) + 1);
      placeRoot(world, position.relative(Direction.WEST, 2).above(rootMod - rand.nextInt(3)),  Direction.WEST,  rand, i / 2 - rand.nextInt(2) + 1);
      return true;
   }
}
