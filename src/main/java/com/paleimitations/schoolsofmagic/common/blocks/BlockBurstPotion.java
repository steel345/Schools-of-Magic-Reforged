package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockBurstPotion extends SOMBlockContainer {

   protected static final VoxelShape POTION_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 12.0D, 11.0D);

   public BlockBurstPotion(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   @Nullable
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {

      return new com.paleimitations.schoolsofmagic.common.tileentity.TileEntityBurstPotion(pos, state);
   }

   @Override
   @Nullable
   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(
         net.minecraft.world.level.Level level, BlockState state,
         net.minecraft.world.level.block.entity.BlockEntityType<T> type) {
      if (level.isClientSide()) return null;
      return (lvl, pos, st, be) -> {
         if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityBurstPotion burst) burst.tick();
      };
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return POTION_SHAPE;
   }
}
