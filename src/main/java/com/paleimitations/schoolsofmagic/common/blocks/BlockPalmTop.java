package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPalmTop extends SOMBlock {

   public static final DirectionProperty COCONUT_FACING = BlockStateProperties.HORIZONTAL_FACING;
   protected static final VoxelShape LOG_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

   public BlockPalmTop(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.8F).sound(SoundType.WOOD).randomTicks();
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return LOG_SHAPE;
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean moving) {
      super.neighborChanged(state, level, pos, block, fromPos, moving);
      if (!level.isClientSide) level.scheduleTick(pos, this, 1);
   }

   @Override
   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      Block below = level.getBlockState(pos.below()).getBlock();
      if (below != BlockRegistry.log_palm.get() && below != BlockRegistry.palm_top.get()) {
         level.destroyBlock(pos, true);
      }
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      if (level.getRawBrightness(pos.above(), 0) >= 9
          && rand.nextInt(7) == 0
          && level.getBlockState(pos.below()).getBlock() == BlockRegistry.log_palm.get()) {
         int i = rand.nextInt(4);

         BlockState coconut = BlockRegistry.coconut.get().defaultBlockState();
         BlockPos below = pos.below();
         tryPlace(level, below.east(),  coconut.trySetValue(COCONUT_FACING, Direction.WEST),  i == 0);
         tryPlace(level, below.west(),  coconut.trySetValue(COCONUT_FACING, Direction.EAST),  i == 1);
         tryPlace(level, below.south(), coconut.trySetValue(COCONUT_FACING, Direction.NORTH), i == 2);
         tryPlace(level, below.north(), coconut.trySetValue(COCONUT_FACING, Direction.SOUTH), i == 3);
      }
   }

   private static void tryPlace(Level level, BlockPos pos, BlockState state, boolean roll) {
      if (roll && level.isEmptyBlock(pos)) level.setBlockAndUpdate(pos, state);
   }
}
