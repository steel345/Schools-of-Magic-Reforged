package com.paleimitations.schoolsofmagic.common.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureBigDarkOak extends SOMBigTreeFeature {

   private static final RegistryObject<Block> LOG    = RegistryObject.create(new ResourceLocation("dark_oak_log"),    ForgeRegistries.BLOCKS);
   private static final RegistryObject<Block> LEAVES = RegistryObject.create(new ResourceLocation("dark_oak_leaves"), ForgeRegistries.BLOCKS);

   @Override protected RegistryObject<Block> log()    { return LOG; }
   @Override protected RegistryObject<Block> leaves() { return LEAVES; }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(3);
      int trunk = i + 6;
      int rootMod = i + 2;
      int treeSize = 8 + i + trunk;
      int y = position.getY();
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 1 >= maxHeight(world)) return false;
      BlockPos down = position.below();
      if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
      if (!placeTreeOfHeight(world, position, i, i + 4, treeSize)) return false;
      if (!isSoil(world, down) || y >= maxHeight(world) - i - 1) return false;

      onPlantGrow(world, down, position);
      int canopyMaxRadius = 8 + i;
      drawCanopy(world, rand, position.above(trunk), 7 + i, canopyMaxRadius, 1, 20 + i * 3, true);

      rand.nextInt(3);
      placeTrunkColumn(world, position, canopyMaxRadius + trunk);

      placeRoot(world, position.relative(Direction.EAST, 2).above(rootMod - rand.nextInt(3)),  Direction.EAST,  rand, rootMod / 2 - rand.nextInt(2) + 1);
      placeRoot(world, position.relative(Direction.NORTH, 2).above(rootMod - rand.nextInt(3)), Direction.NORTH, rand, rootMod / 2 - rand.nextInt(2) + 1);
      placeRoot(world, position.relative(Direction.SOUTH, 2).above(rootMod - rand.nextInt(3)), Direction.SOUTH, rand, rootMod / 2 - rand.nextInt(2) + 1);
      placeRoot(world, position.relative(Direction.WEST, 2).above(rootMod - rand.nextInt(3)),  Direction.WEST,  rand, rootMod / 2 - rand.nextInt(2) + 1);
      return true;
   }
}
