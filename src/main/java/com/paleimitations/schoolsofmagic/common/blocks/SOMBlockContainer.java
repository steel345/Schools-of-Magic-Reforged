package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;

public abstract class SOMBlockContainer extends net.minecraft.world.level.block.Block implements EntityBlock {

   public SOMBlockContainer(BlockBehaviour.Properties props) {
      super(props);
   }

   @Override
   public RenderShape getRenderShape(BlockState state) {
      return RenderShape.MODEL;
   }

   @Override
   @Nullable
   public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F);
   }

   public static BlockBehaviour.Properties defaultProps(SoundType sound) {
      return BlockBehaviour.Properties.of().strength(0.5F, 0.5F).sound(sound);
   }
}
