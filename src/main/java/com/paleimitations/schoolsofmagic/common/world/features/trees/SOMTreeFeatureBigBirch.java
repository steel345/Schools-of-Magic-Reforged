package com.paleimitations.schoolsofmagic.common.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureBigBirch extends SOMBigTreeFeature {

   private static final RegistryObject<Block> LOG    = RegistryObject.create(new ResourceLocation("birch_log"),    ForgeRegistries.BLOCKS);
   private static final RegistryObject<Block> LEAVES = RegistryObject.create(new ResourceLocation("birch_leaves"), ForgeRegistries.BLOCKS);

   @Override protected RegistryObject<Block> log()    { return LOG; }
   @Override protected RegistryObject<Block> leaves() { return LEAVES; }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(3);
      int trunk = i + 5;
      int treeSize = 5 + i + trunk * 2;
      int y = position.getY();
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 1 >= maxHeight(world)) return false;
      BlockPos down = position.below();
      if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
      if (!placeTreeOfHeight(world, position, i, i + 4, treeSize)) return false;
      if (!isSoil(world, down) || y >= maxHeight(world) - i - 1) return false;

      onPlantGrow(world, down, position);
      int canopyMaxRadius = 7 + i;
      drawCanopy(world, rand, position.above(trunk),     6 + i, canopyMaxRadius, 1, 7 + i * 2, true);
      drawCanopy(world, rand, position.above(trunk * 2), 4 + i, 5 + i,          1, 7 + i * 2, true);

      rand.nextInt(3);
      placeTrunkColumn(world, position, canopyMaxRadius + trunk * 2);
      placeStubRoots(world, rand, position);
      return true;
   }
}
