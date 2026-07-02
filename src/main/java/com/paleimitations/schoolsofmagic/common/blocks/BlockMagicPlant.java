package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BlockMagicPlant extends BushBlock {

   public static final EnumProperty<EnumMagicType> TYPE = EnumProperty.create("type", EnumMagicType.class);

   public BlockMagicPlant(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TYPE, EnumMagicType.values()[0]));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE);
   }

   private static final net.minecraft.world.phys.shapes.VoxelShape PLANT_SHAPE =
      Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

   private static final net.minecraft.world.phys.shapes.VoxelShape CLOVER_SHAPE =
      Block.box(0.0D, 1.0D, 0.0D, 16.0D, 2.0D, 16.0D);

   @Override
   public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext ctx) {
      return state.getValue(TYPE) == EnumMagicType.GEOMANCY ? CLOVER_SHAPE : PLANT_SHAPE;
   }

   @Override
   protected boolean mayPlaceOn(BlockState soil, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
      return super.mayPlaceOn(soil, level, pos);
   }

   @Override
   public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
      EnumMagicType type = EnumMagicType.values()[0];
      net.minecraft.nbt.CompoundTag tag = ctx.getItemInHand().getTag();
      if (tag != null && tag.contains("BlockStateTag")) {
         String t = tag.getCompound("BlockStateTag").getString("type");
         for (EnumMagicType e : EnumMagicType.values()) {
            if (e.getSerializedName().equals(t)) { type = e; break; }
         }
      }
      return this.defaultBlockState().setValue(TYPE, type);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below = level.getBlockState(pos.below());
      EnumMagicType type = state.getValue(TYPE);
      if (type == EnumMagicType.HYDROMANCY) {
         if (below.getFluidState().is(net.minecraft.tags.FluidTags.WATER)
             || below.is(net.minecraft.world.level.block.Blocks.MUD)
             || (com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.block_mud != null && below.is(com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.block_mud.get()))) {
            return true;
         }
         for (net.minecraft.core.Direction d : net.minecraft.core.Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.relative(d)).getFluidState().is(net.minecraft.tags.FluidTags.WATER)) {
               return true;
            }
         }
         return false;
      }
      if (type == EnumMagicType.HELIOMANCY || type == EnumMagicType.NECROMANCY) {
         if (below.is(net.minecraft.tags.BlockTags.SAND)
             || below.is(net.minecraft.tags.BlockTags.DIRT)
             || below.is(net.minecraft.world.level.block.Blocks.FARMLAND)) return true;
      }
      return this.mayPlaceOn(below, level, pos.below());
   }

   public boolean isLeavesCompat(BlockState state, LevelReader world, BlockPos pos) {
      return true;
   }

   @Override
   public void entityInside(BlockState state, net.minecraft.world.level.Level world, BlockPos pos, net.minecraft.world.entity.Entity entity) {
      super.entityInside(state, world, pos, entity);
      if (entity instanceof net.minecraft.world.entity.LivingEntity) {
         EnumMagicType type = state.getValue(TYPE);
         if (type == EnumMagicType.AURAMANCY) {
            entity.makeStuckInBlock(state, new net.minecraft.world.phys.Vec3(0.8D, 0.75D, 0.8D));
         }

      }
   }

   @SuppressWarnings("unused")
   private static final Class<?> __BLOCK = Block.class;

   @Override
   public net.minecraft.world.item.ItemStack getCloneItemStack(
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos,
         BlockState state) {
      return VariantDrop.variantStack(this, state, TYPE);
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      java.util.List<net.minecraft.world.item.ItemStack> drops = new java.util.ArrayList<>();
      drops.add(VariantDrop.variantStack(this, state, TYPE));
      return drops;
   }
}
