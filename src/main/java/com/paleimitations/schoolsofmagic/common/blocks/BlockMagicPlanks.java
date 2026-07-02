package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockMagicPlanks extends Block {

   public static final EnumProperty<EnumMagicWood> TYPE = EnumProperty.create("type", EnumMagicWood.class);

   public BlockMagicPlanks(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumMagicWood.values()[0]));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   public static net.minecraft.world.item.Item plankItem(EnumMagicWood wood) {
      switch (wood) {
         case ELDER:  return com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_planks_elder.get();
         case PINE:   return com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_planks_pine.get();
         case WILLOW: return com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_planks_willow.get();
         case YEW:    return com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_planks_yew.get();
         case VERDE:  return com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_planks_verde.get();
         case ASH:
         default:     return com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_planks_ash.get();
      }
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos,
         net.minecraft.world.level.block.state.BlockState state) {
      return new net.minecraft.world.item.ItemStack(plankItem(state.getValue(TYPE)));
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      return java.util.Collections.singletonList(new net.minecraft.world.item.ItemStack(plankItem(state.getValue(TYPE))));
   }

   private static final int ROT_TIME = 1800;

   public static boolean nearWater(net.minecraft.world.level.LevelReader level, net.minecraft.core.BlockPos pos) {
      for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.values()) {
         if (level.getFluidState(pos.relative(dir)).is(net.minecraft.tags.FluidTags.WATER)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public void onPlace(BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos, BlockState oldState, boolean moving) {
      super.onPlace(state, level, pos, oldState, moving);
      if (!level.isClientSide && nearWater(level, pos) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
         level.scheduleTick(pos, this, ROT_TIME);
      }
   }

   @Override
   public void neighborChanged(BlockState state, net.minecraft.world.level.Level level, net.minecraft.core.BlockPos pos,
         Block block, net.minecraft.core.BlockPos fromPos, boolean moving) {
      super.neighborChanged(state, level, pos, block, fromPos, moving);
      if (!level.isClientSide && nearWater(level, pos) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
         level.scheduleTick(pos, this, ROT_TIME);
      }
   }

   @Override
   public void tick(BlockState state, net.minecraft.server.level.ServerLevel level, net.minecraft.core.BlockPos pos, net.minecraft.util.RandomSource random) {
      if (nearWater(level, pos)) {
         EnumWoodType rotType = EnumWoodType.valueOf(state.getValue(TYPE).name());
         level.setBlockAndUpdate(pos, com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.rotted_planks.get()
            .defaultBlockState().setValue(BlockRottedPlanks.TYPE, rotType));
      }
   }

}
