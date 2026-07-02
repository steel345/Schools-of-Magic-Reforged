package com.paleimitations.schoolsofmagic.common.world.features.trees;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.RegistryObject;

public abstract class SOMTreeFeature extends Feature<NoneFeatureConfiguration> {

   public SOMTreeFeature() {
      super(NoneFeatureConfiguration.CODEC);
   }

   protected abstract RegistryObject<net.minecraft.world.level.block.Block> log();

   protected abstract RegistryObject<net.minecraft.world.level.block.Block> leaves();

   protected abstract boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position);

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
      return generate(ctx.level(), ctx.random(), ctx.origin());
   }

   protected BlockState trunkY() { return log().get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y); }
   protected BlockState trunkX() { return log().get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X); }
   protected BlockState trunkZ() { return log().get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z); }

   protected BlockState leaf() {
      return leaves().get().defaultBlockState()
         .setValue(LeavesBlock.DISTANCE, 1).setValue(LeavesBlock.PERSISTENT, false);
   }

   protected void set(WorldGenLevel w, BlockPos p, BlockState s) {
      w.setBlock(p, s, 3);
   }

   protected boolean isReplaceable(WorldGenLevel w, BlockPos p) {
      BlockState s = w.getBlockState(p);
      return s.isAir() || s.is(BlockTags.LEAVES) || s.is(BlockTags.LOGS)
         || s.is(BlockTags.SAPLINGS) || s.is(BlockTags.FLOWERS) || s.canBeReplaced()
         || s.getBlock() instanceof BushBlock;
   }

   protected boolean canOverwriteLog(BlockState s) {
      return s.isAir() || s.is(BlockTags.LEAVES) || s.canBeReplaced() || s.getBlock() == log().get();
   }

   protected boolean canOverwriteLogMaybe(BlockState s) {
      return canOverwriteLog(s) || s.getBlock() instanceof BushBlock || s.is(Blocks.WATER);
   }

   protected void placeLogAt(WorldGenLevel w, BlockPos p) {
      set(w, p, trunkY());
   }

   protected void placeLog1At(WorldGenLevel w, BlockPos p, BlockState block) {
      if (canOverwriteLog(w.getBlockState(p))) set(w, p, block);
   }

   protected void placeLogMaybe(WorldGenLevel w, BlockPos p) {
      if (canOverwriteLogMaybe(w.getBlockState(p))) set(w, p, trunkY());
   }

   protected void placeLeafAt(WorldGenLevel w, BlockPos p) {
      BlockState s = w.getBlockState(p);
      if (s.isAir() || s.is(BlockTags.LEAVES) || s.getBlock() == Blocks.VINE) {
         set(w, p, leaf());
      }
   }

   protected boolean isSoil(WorldGenLevel w, BlockPos down) {
      BlockState soil = w.getBlockState(down);
      return soil.canSustainPlant(w, down, Direction.UP, (IPlantable) Blocks.OAK_SAPLING);
   }

   protected void onPlantGrow(WorldGenLevel w, BlockPos down, BlockPos source) {
      BlockState soil = w.getBlockState(down);
      if (soil.is(Blocks.GRASS_BLOCK) || soil.is(Blocks.MYCELIUM) || soil.is(Blocks.PODZOL)) {
         set(w, down, Blocks.DIRT.defaultBlockState());
      }
   }

   protected int maxHeight(WorldGenLevel w) { return w.getMaxBuildHeight(); }
}
