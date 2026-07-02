package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockPlantGrowingBush extends SOMPlant implements BonemealableBlock {

   public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
   public static final IntegerProperty POSITION = IntegerProperty.create("position", 0, 3);
   protected static final VoxelShape OLEAND_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

   public BlockPlantGrowingBush(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(POSITION, 0));
   }

   @Override
   public float getDestroyProgress(net.minecraft.world.level.block.state.BlockState state,
         net.minecraft.world.entity.player.Player player,
         net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos pos) {
      return BushBreak.progress(player);
   }

   @Override
   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return OLEAND_SHAPE;
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      int age = state.getValue(AGE);
      int i = 1;
      while (level.getBlockState(pos.below(i)).getBlock() == this) i++;
      if (this == BlockRegistry.plant_ocotillo.get() && age > 2 && i > 1) {
         if (!level.isClientSide) {

            ItemStack drop = new ItemStack(ItemRegistry.item_ocotillo_flowers.get());
            popResource((ServerLevel) level, pos, drop);

            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
               net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, state.setValue(AGE, 0), 2);
         }
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      if ((level.getBlockState(pos.below()).getBlock() == this || canSurvive(state, level, pos))
          && level.isEmptyBlock(pos.above())) {
         int i = 1;
         while (level.getBlockState(pos.below(i)).getBlock() == this) i++;
         if (i < 4) {
            int age = state.getValue(AGE);
            if (age == 3 && i < 3) {

               level.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(POSITION, 3));
               level.setBlock(pos, state.setValue(AGE, 0).setValue(POSITION, computeOcotilloPosition(level, pos)), 2);
            } else if (age < 3) {
               level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
         }
      }
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
      if (state.getValue(AGE) < 3) return true;
      int i = 1;
      while (level.getBlockState(pos.below(i)).getBlock() == this) i++;
      return i < 3 && level.isEmptyBlock(pos.above());
   }

   @Override
   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return true;
   }

   @Override
   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      int i = 1;
      while (level.getBlockState(pos.below(i)).getBlock() == this) i++;
      int age = state.getValue(AGE);
      if (age == 3 && i < 3 && level.isEmptyBlock(pos.above())) {
         level.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(POSITION, 3));
         level.setBlock(pos, state.setValue(AGE, 0).setValue(POSITION, computeOcotilloPosition(level, pos)), 2);
      } else if (age < 3) {
         level.setBlock(pos, state.setValue(AGE, age + 1), 2);
      }
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      if (state.is(this)) return true;
      if (this == BlockRegistry.plant_ocotillo.get() && state.is(Blocks.SAND)) return true;
      return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT) || state.is(Blocks.FARMLAND);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      return mayPlaceOn(level.getBlockState(pos.below()), level, pos.below());
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AGE, POSITION);
   }

   private int computeOcotilloPosition(LevelReader level, BlockPos pos) {
      if (level.getBlockState(pos.below()).is(this)) {
         return level.getBlockState(pos.above()).is(this) ? 2 : 3;
      }
      return level.getBlockState(pos.below()).is(Blocks.SAND) ? 0 : 1;
   }

   @Override
   public BlockState getStateForPlacement(BlockPlaceContext ctx) {
      BlockState state = this.defaultBlockState();
      if (this == BlockRegistry.plant_ocotillo.get()) {
         state = state.setValue(POSITION, computeOcotilloPosition(ctx.getLevel(), ctx.getClickedPos()));
      }
      return state;
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
      if (this == BlockRegistry.plant_ocotillo.get() && (dir == Direction.UP || dir == Direction.DOWN)) {
         state = state.setValue(POSITION, computeOcotilloPosition(level, pos));
      }
      return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
   }

   @Override
   public void entityInside(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.Level world, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.Entity entity) {
      super.entityInside(state, world, pos, entity);
      BushSound.rustle(world, entity);

      if ((this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.plant_rose.get()
            || this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.plant_oleander.get()
            || this == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.plant_sage.get())
            && entity instanceof net.minecraft.world.entity.LivingEntity) {
         entity.makeStuckInBlock(state, new net.minecraft.world.phys.Vec3(0.8D, 0.75D, 0.8D));
      }
   }
}
