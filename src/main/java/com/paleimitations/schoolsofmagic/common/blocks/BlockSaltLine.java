package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockSaltLine extends Block {
   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;

   private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

   public BlockSaltLine(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(NORTH, RedstoneSide.NONE).setValue(EAST, RedstoneSide.NONE)
         .setValue(SOUTH, RedstoneSide.NONE).setValue(WEST, RedstoneSide.NONE));
   }

   public static boolean isWarded(net.minecraft.world.entity.Entity e) {
      if (!(e instanceof LivingEntity le) || !(e instanceof Mob)) {
         return false;
      }
      return le.getMobType() == MobType.UNDEAD
         || le instanceof Witch || le instanceof Evoker || le instanceof Illusioner;
   }

   private static EnumProperty<RedstoneSide> propFor(Direction dir) {
      return switch (dir) {
         case NORTH -> NORTH;
         case EAST -> EAST;
         case SOUTH -> SOUTH;
         case WEST -> WEST;
         default -> null;
      };
   }

   private static boolean isSalt(BlockState state) {
      return state.getBlock() instanceof BlockSaltLine;
   }

   private static RedstoneSide sideFor(LevelReader level, BlockPos pos, Direction face) {
      BlockPos neighborPos = pos.relative(face);
      BlockState neighbor = level.getBlockState(neighborPos);
      BlockPos above = pos.above();
      boolean canClimb = !level.getBlockState(above).isRedstoneConductor(level, above);
      if (canClimb && isSalt(level.getBlockState(neighborPos.above()))) {
         return neighbor.isFaceSturdy(level, neighborPos, face.getOpposite())
            ? RedstoneSide.UP : RedstoneSide.SIDE;
      }
      if (isSalt(neighbor)) return RedstoneSide.SIDE;
      if (neighbor.isRedstoneConductor(level, neighborPos)) return RedstoneSide.NONE;
      return isSalt(level.getBlockState(neighborPos.below())) ? RedstoneSide.SIDE : RedstoneSide.NONE;
   }

   private BlockState computeConnections(LevelReader level, BlockPos pos, BlockState state) {
      for (Direction d : Direction.Plane.HORIZONTAL) {
         state = state.setValue(propFor(d), sideFor(level, pos, d));
      }

      boolean north = state.getValue(NORTH).isConnected();
      boolean south = state.getValue(SOUTH).isConnected();
      boolean east = state.getValue(EAST).isConnected();
      boolean west = state.getValue(WEST).isConnected();
      boolean freeNorthSouth = !north && !south;
      boolean freeEastWest = !east && !west;
      if (!west && freeNorthSouth) state = state.setValue(WEST, RedstoneSide.SIDE);
      if (!east && freeNorthSouth) state = state.setValue(EAST, RedstoneSide.SIDE);
      if (!north && freeEastWest) state = state.setValue(NORTH, RedstoneSide.SIDE);
      if (!south && freeEastWest) state = state.setValue(SOUTH, RedstoneSide.SIDE);
      return state;
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      return computeConnections(ctx.getLevel(), ctx.getClickedPos(), this.defaultBlockState());
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level,
                                 BlockPos pos, BlockPos neighborPos) {
      if (!state.canSurvive(level, pos)) {
         return Blocks.AIR.defaultBlockState();
      }

      return computeConnections(level, pos, state);
   }

   @Override
   public void updateIndirectNeighbourShapes(BlockState state, LevelAccessor level, BlockPos pos, int flags, int recursion) {
      BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
      for (Direction dir : Direction.Plane.HORIZONTAL) {
         RedstoneSide side = state.getValue(propFor(dir));
         if (side == RedstoneSide.NONE) continue;
         if (isSalt(level.getBlockState(cursor.setWithOffset(pos, dir)))) continue;

         cursor.move(Direction.DOWN);
         if (isSalt(level.getBlockState(cursor))) {
            BlockPos from = cursor.relative(dir.getOpposite());
            level.neighborShapeChanged(dir.getOpposite(), level.getBlockState(from), cursor, from, flags, recursion);
         }

         cursor.setWithOffset(pos, dir).move(Direction.UP);
         if (isSalt(level.getBlockState(cursor))) {
            BlockPos from = cursor.relative(dir.getOpposite());
            level.neighborShapeChanged(dir.getOpposite(), level.getBlockState(from), cursor, from, flags, recursion);
         }
      }
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return SHAPE;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      if (ctx instanceof EntityCollisionContext ec && isWarded(ec.getEntity())) {
         return Shapes.block();
      }
      return Shapes.empty();
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockPos below = pos.below();
      return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
   }

   @Override
   public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
      return true;
   }

   @Override
   public BlockState rotate(BlockState state, Rotation rot) {
      return switch (rot) {
         case CLOCKWISE_180 -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST))
            .setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
         case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH))
            .setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
         case CLOCKWISE_90 -> state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH))
            .setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
         default -> state;
      };
   }

   @Override
   public BlockState mirror(BlockState state, Mirror mir) {
      return switch (mir) {
         case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
         case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
         default -> state;
      };
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(NORTH, EAST, SOUTH, WEST);
   }
}
