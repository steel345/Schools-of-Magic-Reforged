package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockGemCluster extends Block implements BonemealableBlock {

   public static final DirectionProperty FACING = BlockStateProperties.FACING;
   public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

   private static final VoxelShape SHAPE_UP    = Block.box(3.0D,  0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
   private static final VoxelShape SHAPE_DOWN  = Block.box(3.0D,  0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
   private static final VoxelShape SHAPE_NORTH = Block.box(3.0D,  3.0D, 0.0D, 13.0D, 13.0D, 16.0D);
   private static final VoxelShape SHAPE_SOUTH = Block.box(3.0D,  3.0D, 0.0D, 13.0D, 13.0D, 16.0D);
   private static final VoxelShape SHAPE_WEST  = Block.box(0.0D,  3.0D, 3.0D, 16.0D, 13.0D, 13.0D);
   private static final VoxelShape SHAPE_EAST  = Block.box(0.0D,  3.0D, 3.0D, 16.0D, 13.0D, 13.0D);

   public BlockGemCluster(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(AGE, 0));
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
      BlockState st = this.defaultBlockState().setValue(FACING, face).setValue(AGE, 0);
      if (canSurvive(st, ctx.getLevel(), ctx.getClickedPos())) return st;
      for (Direction d : ctx.getNearestLookingDirections()) {
         BlockState alt = this.defaultBlockState().setValue(FACING, d.getOpposite()).setValue(AGE, 0);
         if (canSurvive(alt, ctx.getLevel(), ctx.getClickedPos())) return alt;
      }
      return st;
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      Direction face = state.getValue(FACING);
      BlockPos supportPos = pos.relative(face.getOpposite());
      return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, face);
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
   public boolean isRandomlyTicking(BlockState state) {
      return state.getValue(AGE) < 3;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {

      if (state.getValue(AGE) < 3 && rand.nextInt(12) == 0) {
         level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 2);
      }
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
      return state.getValue(AGE) < 3;
   }

   @Override
   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   @Override
   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      level.setBlock(pos, state.setValue(AGE, Math.min(3, state.getValue(AGE) + 1)), 2);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(FACING, AGE);
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
      java.util.List<ItemStack> drops = new java.util.ArrayList<>();
      int age = state.getValue(AGE);
      RandomSource rand = builder.getLevel().getRandom();
      int count = age >= 3 ? 2 + rand.nextInt(2) : 1;
      ItemStack chunk = new ItemStack(ItemRegistry.gem_chunk.get(), count);
      chunk.setDamageValue(this.getMagicType());
      drops.add(chunk);
      return drops;
   }

   public int getMagicType() {
      if (this == BlockRegistry.crystal_ruby.get())         return 0;
      if (this == BlockRegistry.crystal_sunstone.get())     return 1;
      if (this == BlockRegistry.crystal_citrine.get())      return 2;
      if (this == BlockRegistry.crystal_peridot.get())      return 3;
      if (this == BlockRegistry.crystal_jade.get())         return 4;
      if (this == BlockRegistry.crystal_turquoise.get())    return 5;
      if (this == BlockRegistry.crystal_aquamarine.get())   return 6;
      if (this == BlockRegistry.crystal_sapphire.get())     return 7;
      if (this == BlockRegistry.crystal_amethyst.get())     return 8;
      if (this == BlockRegistry.crystal_garnet.get())       return 9;
      if (this == BlockRegistry.crystal_rose_quartz.get())  return 10;
      if (this == BlockRegistry.crystal_moonstone.get())    return 11;
      if (this == BlockRegistry.crystal_putridite.get())    return 12;
      if (this == BlockRegistry.crystal_opal.get())         return 13;
      if (this == BlockRegistry.crystal_onyx.get())         return 14;
      if (this == BlockRegistry.crystal_smoky_quartz.get()) return 15;
      return 0;
   }

   public static BlockBehaviour.Properties defaultProps() {
      return BlockBehaviour.Properties.of()
         .strength(1.5F, 1.5F)
         .sound(SoundType.AMETHYST)
         .randomTicks()
         .noOcclusion();
   }
}
