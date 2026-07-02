package com.paleimitations.schoolsofmagic.common.blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class BlockPlantHydrangea extends SOMBlock implements IPlantable {

   public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);
   protected static final VoxelShape BUSH_SHAPE = Block.box(1.6D, 0.0D, 1.6D, 14.4D, 16.0D, 14.4D);

   public BlockPlantHydrangea(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(POSITION, 0));
   }

   @Override
   public float getDestroyProgress(net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.entity.player.Player player,
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos) {
      return BushBreak.progress(player);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return BUSH_SHAPE;
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below = level.getBlockState(pos.below());
      if (below.canSustainPlant(level, pos.below(), Direction.UP, this)) return true;
      if (below.getBlock() == this || below.is(Blocks.SAND)) return true;
      if (!below.is(Blocks.GRASS_BLOCK) && !below.is(Blocks.DIRT) && !below.is(Blocks.SAND)) return false;
      for (Direction face : Direction.Plane.HORIZONTAL) {
         BlockState near = level.getBlockState(pos.below().relative(face));
         if (near.getFluidState().is(FluidTags.WATER) || near.is(Blocks.FROSTED_ICE)) return true;
      }
      return false;
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if (!this.canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
      return computePosition(state, level, pos);
   }

   private BlockState computePosition(BlockState state, BlockGetter level, BlockPos pos) {
      boolean above = level.getBlockState(pos.above()).getBlock() == this;
      boolean below = level.getBlockState(pos.below()).getBlock() == this;
      if (above) return state.setValue(POSITION, below ? 2 : 1);
      return state.setValue(POSITION, below ? 3 : 0);
   }

   @Override
   public PlantType getPlantType(BlockGetter level, BlockPos pos) { return PlantType.PLAINS; }

   @Override
   public BlockState getPlant(BlockGetter level, BlockPos pos) { return this.defaultBlockState(); }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(POSITION);
   }

   @Override
   public void entityInside(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.Level world, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.Entity entity) {
      super.entityInside(state, world, pos, entity);
      BushSound.rustle(world, entity);
   }
}
