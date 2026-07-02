package com.paleimitations.schoolsofmagic.common.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureBigOak extends SOMBigTreeFeature {

   private static final RegistryObject<Block> LOG    = RegistryObject.create(new ResourceLocation("oak_log"),    ForgeRegistries.BLOCKS);
   private static final RegistryObject<Block> LEAVES = RegistryObject.create(new ResourceLocation("oak_leaves"), ForgeRegistries.BLOCKS);

   @Override protected RegistryObject<Block> log()    { return LOG; }
   @Override protected RegistryObject<Block> leaves() { return LEAVES; }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(3);
      int trunk = i + 6;
      int treeSize = 5 + i + trunk;
      int x = position.getX(), y = position.getY(), z = position.getZ();
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 1 >= maxHeight(world)) return false;
      BlockPos down = position.below();
      if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
      if (!placeTreeOfHeight(world, position, i, i + 4, treeSize)) return false;
      if (!isSoil(world, down) || y >= maxHeight(world) - i - 1) return false;

      onPlantGrow(world, down, position);
      int canopyMaxRadius = 8 + i;
      BlockPos canopyCore = position.above(trunk);
      drawCanopy(world, rand, canopyCore, 7 + i, canopyMaxRadius, 1, 20 + i * 3, true);

      rand.nextInt(3);
      placeTrunkColumn(world, position, canopyMaxRadius + trunk);
      placeStubRoots(world, rand, position);
      return true;
   }
}
