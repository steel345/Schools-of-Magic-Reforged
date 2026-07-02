package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SOMPlant extends BushBlock {

   protected static final VoxelShape BUSH_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

   public SOMPlant(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return BUSH_SHAPE;
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F).sound(SoundType.GRASS).noCollission();
   }

   @Override
   public SoundType getSoundType(BlockState state) {
      return SoundType.GRASS;
   }
}
