package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Gem extends Block {

   public static final EnumProperty<EnumGemState> TYPE = EnumProperty.create("type", EnumGemState.class);
   public static final DirectionProperty FACING = BlockStateProperties.FACING;

   private static final VoxelShape SHAPE_UP    = Block.box(5.0D,  0.0D, 5.0D, 11.0D,  2.0D, 11.0D);
   private static final VoxelShape SHAPE_DOWN  = Block.box(5.0D, 14.0D, 5.0D, 11.0D, 16.0D, 11.0D);
   private static final VoxelShape SHAPE_NORTH = Block.box(5.0D,  5.0D, 14.0D, 11.0D, 11.0D, 16.0D);
   private static final VoxelShape SHAPE_SOUTH = Block.box(5.0D,  5.0D,  0.0D, 11.0D, 11.0D,  2.0D);
   private static final VoxelShape SHAPE_WEST  = Block.box(14.0D, 5.0D, 5.0D, 16.0D, 11.0D, 11.0D);
   private static final VoxelShape SHAPE_EAST  = Block.box( 0.0D, 5.0D, 5.0D,  2.0D, 11.0D, 11.0D);

   public Gem(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any()
         .setValue(TYPE, EnumGemState.ROUGH)
         .setValue(FACING, Direction.UP));
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      switch (state.getValue(FACING)) {
         case DOWN:  return SHAPE_DOWN;
         case NORTH: return SHAPE_NORTH;
         case SOUTH: return SHAPE_SOUTH;
         case WEST:  return SHAPE_WEST;
         case EAST:  return SHAPE_EAST;
         case UP:
         default:    return SHAPE_UP;
      }
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {

      Direction face = ctx.getClickedFace();
      BlockState st = this.defaultBlockState().setValue(FACING, face);
      if (canSurvive(st, ctx.getLevel(), ctx.getClickedPos())) return st;

      for (Direction d : ctx.getNearestLookingDirections()) {
         BlockState alt = this.defaultBlockState().setValue(FACING, d.getOpposite());
         if (canSurvive(alt, ctx.getLevel(), ctx.getClickedPos())) return alt;
      }
      return st;
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      Direction face = state.getValue(FACING);
      BlockPos supportPos = pos.relative(face.getOpposite());
      BlockState support = level.getBlockState(supportPos);
      return support.isFaceSturdy(level, supportPos, face);
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor,
                                 LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if (dir == state.getValue(FACING).getOpposite() && !state.canSurvive(level, pos)) {
         return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
      }
      return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TYPE, FACING);
   }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(
         BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      java.util.List<net.minecraft.world.item.ItemStack> drops = new java.util.ArrayList<>();
      net.minecraft.world.item.ItemStack tool = builder.getOptionalParameter(
         net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
      boolean silk = tool != null && net.minecraft.world.item.enchantment.EnchantmentHelper
         .getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.SILK_TOUCH, tool) > 0;
      if (silk || state.getValue(TYPE) == EnumGemState.POLISHED) {
         drops.add(new net.minecraft.world.item.ItemStack(this));
         return drops;
      }
      net.minecraft.util.RandomSource rand = builder.getLevel().getRandom();
      net.minecraft.world.item.ItemStack drop = new net.minecraft.world.item.ItemStack(
         rand.nextFloat() < 0.75F
            ? com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.gem_chunk.get()
            : com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.gem_dust.get());
      drop.setDamageValue(this.getMagicType());
      drops.add(drop);
      return drops;
   }

   public int getMagicType() {
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_pyromancy.get())   return 0;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_heliomancy.get())  return 1;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_aeromancy.get())   return 2;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_geomancy.get())    return 3;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_animancy.get())    return 4;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_electromancy.get())return 5;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_hydromancy.get())  return 6;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_cryomancy.get())   return 7;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_hieromancy.get())  return 8;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_chaotics.get())    return 9;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_auramancy.get())   return 10;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_astromancy.get())  return 11;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_infernality.get()) return 12;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_spectromancy.get())return 13;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_umbramancy.get())  return 14;
      if (this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_necromancy.get())  return 15;
      return 0;
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of()
         .strength(0.5F, 0.5F)
         .sound(SoundType.GLASS)
         .noOcclusion();
   }
}
