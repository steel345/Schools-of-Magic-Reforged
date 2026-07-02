package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;

public class BlockRottedPlanks extends Block {

   public static final EnumProperty<EnumWoodType> TYPE = EnumProperty.create("type", EnumWoodType.class);

   public BlockRottedPlanks(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumWoodType.values()[0]));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   @Override
   public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
      return java.util.Collections.singletonList(VariantDrop.variantStack(this, state, TYPE));
   }

   @Override
   public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
      return true;
   }

   @Override
   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
      return 5;
   }
}
