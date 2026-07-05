package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SOMDoubleSlab extends SlabBlock {

   public SOMDoubleSlab(BlockBehaviour.Properties props) {
      super(props);
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Shapes.block();
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Shapes.block();
   }

   @Override
   public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
      return Shapes.block();
   }

   @Override
   public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
      return Shapes.block();
   }

   @Override
   public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
      return Shapes.block();
   }

   @Override
   public boolean useShapeForLightOcclusion(BlockState state) {
      return false;
   }
}
