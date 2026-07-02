package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockFaeStone extends Block {

   public static final EnumProperty<EnumFaeStone> VARIANT = EnumProperty.create("variant", EnumFaeStone.class);

   public BlockFaeStone(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumFaeStone.NORMAL));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(VARIANT);
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos,
         BlockState state) {
      return VariantDrop.variantStack(this, state, VARIANT);
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {

      net.minecraft.world.item.ItemStack tool = builder.getOptionalParameter(
            net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
      boolean silk = tool != null && tool.getEnchantmentLevel(
            net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH) > 0;
      EnumFaeStone v = state.getValue(VARIANT);
      if (!silk && v == EnumFaeStone.NORMAL) {
         BlockState cobbleState = this.defaultBlockState().setValue(VARIANT, EnumFaeStone.COBBLE);
         return java.util.Collections.singletonList(VariantDrop.variantStack(this, cobbleState, VARIANT));
      }
      return java.util.Collections.singletonList(VariantDrop.variantStack(this, state, VARIANT));
   }
}
