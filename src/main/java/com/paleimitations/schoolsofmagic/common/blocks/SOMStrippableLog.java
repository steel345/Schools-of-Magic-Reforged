package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SOMStrippableLog extends RotatedPillarBlock {

   private final Supplier<? extends net.minecraft.world.level.block.Block> stripped;

   public SOMStrippableLog(BlockBehaviour.Properties props,
                           Supplier<? extends net.minecraft.world.level.block.Block> stripped) {
      super(props);
      this.stripped = stripped;
   }

   @Nullable
   @Override
   public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
      if (ToolActions.AXE_STRIP.equals(toolAction)) {
         return this.stripped.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
      }
      return super.getToolModifiedState(state, context, toolAction, simulate);
   }
}
