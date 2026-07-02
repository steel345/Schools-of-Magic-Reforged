package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockMagicBookshelf extends Block {

   public static final EnumProperty<EnumWoodType> TYPE = EnumProperty.create("type", EnumWoodType.class);

   public BlockMagicBookshelf(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumWoodType.OAK));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   @Override
   public float getEnchantPowerBonus(net.minecraft.world.level.block.state.BlockState state,
                                      net.minecraft.world.level.LevelReader level,
                                      BlockPos pos) {
      return 1.0F;
   }

   public float getEnchantPowerBonus(Level world, BlockPos pos) {
      return 1.0F;
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos,
         net.minecraft.world.level.block.state.BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      return java.util.Collections.singletonList(VariantDrop.variantStack(this, state, TYPE));
   }

}
