package com.paleimitations.schoolsofmagic.common.blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockPlantCattail extends SOMPlant {

   public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

   public BlockPlantCattail(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, 0));
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return this.defaultBlockState().setValue(TYPE, ctx.getLevel().random.nextInt(3));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   private static final net.minecraft.world.phys.shapes.VoxelShape MAT =
      Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);

   @Override
   public net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext ctx) {
      return ctx.isDescending() ? net.minecraft.world.phys.shapes.Shapes.empty() : MAT;
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level,
                                                              BlockPos pos, BlockState state) {
      return new net.minecraft.world.item.ItemStack(
         com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.item_cattail.get());
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below2 = level.getBlockState(pos.below(2));
      boolean subOk = below2.is(BlockTags.DIRT)
                   || below2.is(BlockTags.SAND)
                   || below2.getBlock() == BlockRegistry.block_mud.get();
      return subOk && super.canSurvive(state, level, pos);
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      return state.is(Blocks.WATER) || state.is(Blocks.DIRT) || state.is(Blocks.SAND) || state.is(Blocks.GRASS_BLOCK)
          || state.getBlock() == BlockRegistry.block_mud.get();
   }
}
