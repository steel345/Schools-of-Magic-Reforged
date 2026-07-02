package com.paleimitations.schoolsofmagic.common.blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockPlantShrooms extends SOMPlant {

   public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
   public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

   public BlockPlantShrooms(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, 0).setValue(FACING, Direction.SOUTH));
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below = level.getBlockState(pos.below());
      if (below.is(Blocks.MYCELIUM) || below.is(Blocks.PODZOL) || below.is(Blocks.GRASS_BLOCK)) return true;
      if ((below.is(BlockTags.LOGS) || below.is(BlockTags.PLANKS))) return true;
      if (BlockRegistry.mushroom_stalk != null && below.is(BlockRegistry.mushroom_stalk.get())) return true;
      return level.getRawBrightness(pos, 0) < 13 && below.isSolidRender(level, pos.below());
   }

   @Override
   @Nullable
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      RandomSource rand = ctx.getLevel().random;
      return this.defaultBlockState().setValue(FACING, randomFacing(rand)).setValue(TYPE, rand.nextInt(3));
   }

   public static Direction randomFacing(RandomSource rand) {
      switch (rand.nextInt(5)) {
         case 1: return Direction.EAST;
         case 2: return Direction.NORTH;
         case 3: return Direction.WEST;
         default: return Direction.SOUTH;
      }
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return Shapes.empty();
   }

   @Override
   public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

      entity.makeStuckInBlock(state, new Vec3(0.8D, 0.9D, 0.8D));
      super.entityInside(state, level, pos, entity);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, TYPE);
   }
}
