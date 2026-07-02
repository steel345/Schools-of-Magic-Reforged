package com.paleimitations.schoolsofmagic.common.blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPlantWaterplant extends SOMPlant {

   protected static final VoxelShape LILY_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.5D, 15.0D);

   public BlockPlantWaterplant(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return LILY_SHAPE;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return ctx.isDescending() ? net.minecraft.world.phys.shapes.Shapes.empty() : LILY_SHAPE;
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
      super.entityInside(state, level, pos, entity);
      if (entity instanceof Boat) {
         level.destroyBlock(pos, true, entity);
      }
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      if (BlockRegistry.block_mud != null && state.is(BlockRegistry.block_mud.get())) return true;
      return state.getFluidState().is(net.minecraft.tags.FluidTags.WATER)
          || state.is(Blocks.ICE) || state.is(Blocks.FROSTED_ICE);
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level,
                                                              BlockPos pos, BlockState state) {
      Block self = state.getBlock();
      if (self == BlockRegistry.plant_algae.get())
         return new net.minecraft.world.item.ItemStack(ItemRegistry.item_algae.get());
      if (self == BlockRegistry.plant_bladderwort.get())
         return new net.minecraft.world.item.ItemStack(ItemRegistry.item_bladderwort.get());
      if (self == BlockRegistry.plant_duckweed.get())
         return new net.minecraft.world.item.ItemStack(ItemRegistry.item_duckweed.get());
      return super.getCloneItemStack(level, pos, state);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below = level.getBlockState(pos.below());
      if (BlockRegistry.block_mud != null && below.is(BlockRegistry.block_mud.get())) return true;
      return below.getFluidState().is(FluidTags.WATER) || below.is(BlockTags.ICE) || below.is(Blocks.ICE);
   }
}
