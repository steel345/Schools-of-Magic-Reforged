package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCrystalBall extends SOMBlock {

   protected static final VoxelShape CRYSTAL_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 11.0D, 12.0D);

   public BlockCrystalBall(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

      if (level.isClientSide) {
         SchoolsOfMagic.proxy.openCrystalBall(player);
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return CRYSTAL_SHAPE;
   }
}
