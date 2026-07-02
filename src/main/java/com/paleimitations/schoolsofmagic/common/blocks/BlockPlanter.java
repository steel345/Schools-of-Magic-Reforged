package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPlanter extends Block {

   public static final EnumProperty<EnumWoodType> TYPE = EnumProperty.create("type", EnumWoodType.class);
   public static final BooleanProperty NORTH = BooleanProperty.create("north");
   public static final BooleanProperty SOUTH = BooleanProperty.create("south");
   public static final BooleanProperty EAST  = BooleanProperty.create("east");
   public static final BooleanProperty WEST  = BooleanProperty.create("west");

   protected static final VoxelShape PLANTER_SHAPE = net.minecraft.world.phys.shapes.Shapes.or(
      Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
      Block.box(1.0D, 4.0D, 1.0D, 15.0D, 14.0D, 15.0D),
      Block.box(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D));

   public BlockPlanter(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(TYPE, EnumWoodType.OAK)
         .setValue(NORTH, false).setValue(SOUTH, false).setValue(EAST, false).setValue(WEST, false));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return PLANTER_SHAPE;
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos nPos) {
      boolean connect = neighbor.is(this) || neighbor.isFaceSturdy(level, nPos, dir.getOpposite());
      switch (dir) {
         case NORTH: return state.setValue(NORTH, connect);
         case SOUTH: return state.setValue(SOUTH, connect);
         case EAST:  return state.setValue(EAST, connect);
         case WEST:  return state.setValue(WEST, connect);
         default:    return state;
      }
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE, NORTH, SOUTH, EAST, WEST);
   }

   @Override
   public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing,
                                  net.minecraftforge.common.IPlantable plantable) {
      return true;
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
