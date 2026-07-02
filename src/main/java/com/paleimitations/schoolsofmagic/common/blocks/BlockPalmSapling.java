package com.paleimitations.schoolsofmagic.common.blocks;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.OakTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPalmSapling extends SaplingBlock {

   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.8D, 14.0D);

   public BlockPalmSapling(AbstractTreeGrower grower, BlockBehaviour.Properties props) {
      super(grower, props);
   }

   public BlockPalmSapling(BlockBehaviour.Properties props) {

      super(new PalmTreeGrower(PALM_TREE_FEATURE), props);
   }

   public static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE_FEATURE =
      ResourceKey.create(net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                         new net.minecraft.resources.ResourceLocation("som", "palm_tree"));

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   public void advanceTree(net.minecraft.server.level.ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
      if (state.getValue(STAGE) == 0) {
         level.setBlock(pos, state.cycle(STAGE), 4);
         return;
      }
      generatePalm(level, pos, random);
   }

   private void generatePalm(net.minecraft.server.level.ServerLevel level, BlockPos origin, RandomSource rand) {
      Block logB  = com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.log_palm.get();
      Block topB  = com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.palm_top.get();
      Block leafB = com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.leaves_palm.get();

      int trunk = 4;
      for (int i = 5; i < 14; i++) {
         if (rand.nextInt(15 - i) == 0) break;
         trunk = i;
      }

      for (int y = 1; y <= trunk; y++) {
         BlockState s = level.getBlockState(origin.above(y));
         if (!s.isAir() && !s.canBeReplaced()) { trunk = y - 1; break; }
      }
      if (trunk < 4) return;

      BlockState log = logB.defaultBlockState()
         .setValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS, net.minecraft.core.Direction.Axis.Y);
      for (int y = 0; y < trunk; y++) {
         level.setBlock(origin.above(y), log, 3);
      }

      BlockPos head = origin.above(trunk);
      level.setBlock(head, topB.defaultBlockState(), 3);

      placeFrond(level, head.east(),  leafB, net.minecraft.core.Direction.WEST);
      placeFrond(level, head.west(),  leafB, net.minecraft.core.Direction.EAST);
      placeFrond(level, head.south(), leafB, net.minecraft.core.Direction.NORTH);
      placeFrond(level, head.north(), leafB, net.minecraft.core.Direction.SOUTH);
   }

   private void placeFrond(net.minecraft.server.level.ServerLevel level, BlockPos pos, Block leaf,
                           net.minecraft.core.Direction facing) {
      BlockState s = level.getBlockState(pos);
      if (s.isAir() || s.canBeReplaced()) {
         level.setBlock(pos, leaf.defaultBlockState().setValue(BlockPalmLeaves.FACING, facing), 3);
      }
   }

   @Override
   protected boolean mayPlaceOn(BlockState soil, BlockGetter level, BlockPos pos) {
      return soil.is(net.minecraft.world.level.block.Blocks.SAND)
          || soil.is(net.minecraft.world.level.block.Blocks.RED_SAND)
          || super.mayPlaceOn(soil, level, pos);
   }

   public static class PalmTreeGrower extends AbstractTreeGrower {
      private final ResourceKey<ConfiguredFeature<?, ?>> key;
      public PalmTreeGrower(ResourceKey<ConfiguredFeature<?, ?>> key) { this.key = key; }
      @Nullable
      @Override
      protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean largeHive) {
         return this.key;
      }
   }
}
