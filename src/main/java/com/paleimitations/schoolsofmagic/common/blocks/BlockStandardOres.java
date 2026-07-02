package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockStandardOres extends BlockOre {

   public static final EnumProperty<EnumStandardOres> TYPE = EnumProperty.create("type", EnumStandardOres.class);

   public BlockStandardOres(BlockBehaviour.Properties props) {
      super(props, null);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumStandardOres.COAL));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, BlockPos pos, BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   @Override
   public int getExpDrop(BlockState state, LevelReader level, RandomSource rand,
                         BlockPos pos, int fortune, int silkTouch) {
      if (silkTouch > 0) return 0;
      return switch (state.getValue(TYPE)) {
         case COAL    -> Mth.nextInt(rand, 0, 2);
         case DIAMOND -> Mth.nextInt(rand, 3, 7);
         case EMERALD -> Mth.nextInt(rand, 3, 7);
         case LAPIS   -> Mth.nextInt(rand, 2, 5);
         default      -> 0;
      };
   }
}
