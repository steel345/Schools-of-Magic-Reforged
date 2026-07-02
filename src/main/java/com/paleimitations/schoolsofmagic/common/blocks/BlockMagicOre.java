package com.paleimitations.schoolsofmagic.common.blocks;

import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockMagicOre extends Block {

   public static final EnumProperty<EnumMagicType> TYPE = EnumProperty.create("type", EnumMagicType.class);

   private static final Set<EnumMagicType> DIAMOND_XP_TYPES = EnumSet.of(
      EnumMagicType.PYROMANCY, EnumMagicType.AEROMANCY, EnumMagicType.GEOMANCY, EnumMagicType.ANIMANCY,
      EnumMagicType.HYDROMANCY, EnumMagicType.CRYOMANCY, EnumMagicType.HIEROMANCY, EnumMagicType.ASTROMANCY,
      EnumMagicType.UMBRAMANCY);

   private final Item drop;

   public BlockMagicOre(BlockBehaviour.Properties props, Item drop) {
      super(props);
      this.drop = drop;
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumMagicType.PYROMANCY));
   }

   public BlockMagicOre(BlockBehaviour.Properties props) {
      this(props, null);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   public Item getDrop() {
      return this.drop;
   }

   @Override
   public int getExpDrop(BlockState state, LevelReader level, RandomSource random, BlockPos pos, int fortune, int silkTouch) {
      if (silkTouch > 0) {
         return 0;
      }
      return DIAMOND_XP_TYPES.contains(state.getValue(TYPE)) ? random.nextInt(5) + 3 : 0;
   }

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos,
         net.minecraft.world.level.block.state.BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

}
