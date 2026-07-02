package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockTile extends Block {

   public static final EnumProperty<EnumTileStyles> VARIANT = EnumProperty.create("variant", EnumTileStyles.class);

   public BlockTile(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumTileStyles.FULL));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(VARIANT);
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos, BlockState state) {
      return VariantDrop.variantStack(this, state, VARIANT);
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      return java.util.Collections.singletonList(VariantDrop.variantStack(this, state, VARIANT));
   }
}
