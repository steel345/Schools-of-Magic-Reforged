package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.ForgeHooks;

public class BlockMushroom extends SOMPlant implements BonemealableBlock {

   public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);
   public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
   public static final BooleanProperty STOPPED = BooleanProperty.create("stopped");

   public BlockMushroom(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(NATURAL, false).setValue(STOPPED, false));
   }

   public IntegerProperty getAgeProperty() { return AGE; }
   public int getMaxAge() { return 7; }
   protected int getAge(BlockState state) { return state.getValue(this.getAgeProperty()); }
   public BlockState withAge(int age) { return this.defaultBlockState().setValue(this.getAgeProperty(), age); }
   public boolean isMaxAge(BlockState state) { return state.getValue(this.getAgeProperty()) >= this.getMaxAge(); }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      if (!this.canSurvive(state, level, pos)) { level.destroyBlock(pos, true); return; }
      if (state.getValue(NATURAL)) return;

      int i = this.getAge(state);

      if (i < this.getMaxAge()) {
         if (ForgeHooks.onCropsGrowPre(level, pos, state, rand.nextInt(4) == 0)) {
            level.setBlock(pos, state.setValue(AGE, i + 1), 2);
            ForgeHooks.onCropsGrowPost(level, pos, state);
         }
         return;
      }

      if (!this.canStack(level, pos)) return;
      if (ForgeHooks.onCropsGrowPre(level, pos, state, rand.nextInt(4) == 0)
            && this.stack(level, pos, state)) {
         ForgeHooks.onCropsGrowPost(level, pos, state);
      }
   }

   private boolean canStack(LevelReader level, BlockPos pos) {
      int stalks = 0;
      while (level.getBlockState(pos.below(stalks + 1)).getBlock() == BlockRegistry.mushroom_stalk.get()) { ++stalks; }
      return stalks < 3 && level.isEmptyBlock(pos.above());
   }

   private boolean stack(Level level, BlockPos pos, BlockState state) {
      if (!this.canStack(level, pos)) return false;
      net.minecraft.world.level.block.Block stalk = BlockRegistry.mushroom_stalk.get();
      level.setBlock(pos, stalk.defaultBlockState().setValue(BlockMushroomStalk.TYPE, 0), 2);
      if (level.getBlockState(pos.below()).getBlock() == stalk)
         level.setBlock(pos.below(), stalk.defaultBlockState().setValue(BlockMushroomStalk.TYPE, 1), 2);
      if (level.getBlockState(pos.below(2)).getBlock() == stalk)
         level.setBlock(pos.below(2), stalk.defaultBlockState().setValue(BlockMushroomStalk.TYPE, 2), 2);
      level.setBlock(pos.above(), this.defaultBlockState().setValue(AGE, 0).setValue(NATURAL, state.getValue(NATURAL)), 2);
      return true;
   }

   public void grow(Level level, BlockPos pos, BlockState state) {
      int i = this.getAge(state);
      if (i < this.getMaxAge()) {
         int n = i + this.getBonemealAgeIncrease(level);
         if (n > this.getMaxAge()) n = this.getMaxAge();
         level.setBlock(pos, state.setValue(AGE, n), 2);
      } else {
         this.stack(level, pos, state);
      }
   }

   protected int getBonemealAgeIncrease(Level level) {
      return Mth.nextInt(level.random, 2, 5);
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {

      return state.isFaceSturdy(level, pos, net.minecraft.core.Direction.UP)
          || state.getBlock() == BlockRegistry.mushroom_stalk.get();
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {

      BlockPos below = pos.below();
      return this.mayPlaceOn(level.getBlockState(below), level, below);
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) { return !this.isMaxAge(state) || this.canStack(level, pos); }
   @Override
   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) { return true; }
   @Override
   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) { this.grow(level, pos, state); }

   @Override
   public void fallOn(Level level, BlockState state, BlockPos pos, net.minecraft.world.entity.Entity entity, float fallDistance) {
      entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
   }

   @Override
   public void updateEntityAfterFallOn(BlockGetter level, net.minecraft.world.entity.Entity entity) {
      if (!entity.isSuppressingBounce()) {
         net.minecraft.world.phys.Vec3 v = entity.getDeltaMovement();
         if (v.y < 0.0D) {
            double mult = entity instanceof net.minecraft.world.entity.LivingEntity ? 1.0D : 0.8D;
            entity.setDeltaMovement(v.x, -v.y * 0.66D * mult, v.z);
         }
      } else {
         super.updateEntityAfterFallOn(level, entity);
      }
   }

   private static final net.minecraft.world.phys.shapes.VoxelShape UMBRELLA_SHAPE =
      net.minecraft.world.phys.shapes.Shapes.or(
         Block.box(6.5D, 0.0D, 6.5D, 9.5D, 14.0D, 9.5D),
         Block.box(0.0D, 13.5D, 0.0D, 16.0D, 15.5D, 16.0D));

   private static final net.minecraft.world.phys.shapes.VoxelShape FAIRYSTOOL_SHAPE =
      net.minecraft.world.phys.shapes.Shapes.or(
         Block.box(6.5D, 0.0D, 6.5D, 9.5D, 2.0D, 9.5D),
         Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D));

   private static final net.minecraft.world.phys.shapes.VoxelShape REDCAP_SHAPE =
      net.minecraft.world.phys.shapes.Shapes.or(
         Block.box(5.5D, 0.0D, 5.5D, 10.5D, 16.0D, 10.5D),
         Block.box(2.0D, 3.0D, 2.0D, 14.0D, 12.0D, 14.0D),
         Block.box(1.0D, 4.0D, 2.0D, 15.0D, 9.0D, 14.0D),
         Block.box(2.0D, 4.0D, 1.0D, 14.0D, 9.0D, 15.0D),
         Block.box(3.0D, 12.0D, 3.0D, 13.0D, 15.0D, 13.0D));

   private static final net.minecraft.world.phys.shapes.VoxelShape GREYCAP_SHAPE =
      net.minecraft.world.phys.shapes.Shapes.or(
         Block.box(6.5D, 0.0D, 6.5D, 9.5D, 8.0D, 9.5D),
         Block.box(0.0D, 8.0D, 0.0D, 16.0D, 10.0D, 16.0D),
         Block.box(1.5D, 10.0D, 1.5D, 14.5D, 11.0D, 14.5D),
         Block.box(4.0D, 11.0D, 4.0D, 12.0D, 12.0D, 12.0D));

   @Override
   public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
         net.minecraft.world.phys.shapes.CollisionContext ctx) {
      if (this == BlockRegistry.mushroom_pink.get()) return UMBRELLA_SHAPE;
      if (this == BlockRegistry.mushroom_white.get()) return FAIRYSTOOL_SHAPE;
      if (this == BlockRegistry.mushroom_dark.get()) return REDCAP_SHAPE;
      if (this == BlockRegistry.mushroom_grey.get()) return GREYCAP_SHAPE;
      return super.getShape(state, level, pos, ctx);
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AGE, NATURAL, STOPPED);
   }
}
