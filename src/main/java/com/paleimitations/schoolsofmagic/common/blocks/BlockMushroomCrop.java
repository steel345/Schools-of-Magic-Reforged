package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class BlockMushroomCrop extends BushBlock implements BonemealableBlock, IPlantable {

   public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);

   private static final net.minecraft.world.phys.shapes.VoxelShape[] SHAPE_BY_AGE = new net.minecraft.world.phys.shapes.VoxelShape[]{
      Block.box(2.0D, 0.0D, 2.0D, 14.0D,  2.0D, 14.0D),
      Block.box(2.0D, 0.0D, 2.0D, 14.0D,  4.0D, 14.0D),
      Block.box(2.0D, 0.0D, 2.0D, 14.0D,  6.0D, 14.0D),
      Block.box(2.0D, 0.0D, 2.0D, 14.0D,  8.0D, 14.0D),
      Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D),
      Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D),
      Block.box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)
   };

   @Override
   public net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
         net.minecraft.world.phys.shapes.CollisionContext ctx) {
      int age = state.getValue(AGE);
      if (age < 0) age = 0;
      if (age >= SHAPE_BY_AGE.length) age = SHAPE_BY_AGE.length - 1;
      return SHAPE_BY_AGE[age];
   }

   public BlockMushroomCrop(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
   }

   public IntegerProperty getAgeProperty() { return AGE; }
   public int getMaxAge() { return 6; }
   protected int getAge(BlockState state) { return state.getValue(this.getAgeProperty()); }
   public BlockState withAge(int age) { return this.defaultBlockState().setValue(this.getAgeProperty(), age); }
   public boolean isMaxAge(BlockState state) { return state.getValue(this.getAgeProperty()) >= this.getMaxAge(); }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {

      if (state.is(Blocks.MYCELIUM)) return true;
      if (state.is(Blocks.PODZOL)) return true;
      if (state.is(Blocks.GRASS_BLOCK)) return true;
      if (state.getBlock() == BlockRegistry.mushroom_stalk.get()) return true;
      return state.is(net.minecraft.tags.BlockTags.LOGS);
   }

   @Override
   public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockState below = level.getBlockState(pos.below());
      if (!this.mayPlaceOn(below, level, pos.below())) {
         return level.getRawBrightness(pos, 0) < 13;
      }
      return true;
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      int i = this.getAge(state);
      if (i < this.getMaxAge()) {
         float f = getGrowthChance(this, level, pos);
         if (ForgeHooks.onCropsGrowPre(level, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
            level.setBlock(pos, this.withAge(i + 1), 2);
            ForgeHooks.onCropsGrowPost(level, pos, state);
         }
      } else if (ForgeHooks.onCropsGrowPre(level, pos, state, rand.nextInt(8) == 0)) {
         this.grow(level, pos, state);
         ForgeHooks.onCropsGrowPost(level, pos, state);
      }
   }

   public void grow(Level level, BlockPos pos, BlockState state) {
      int i = this.getAge(state) + this.getBonemealAgeIncrease(level);
      int j = this.getMaxAge();
      if (i > j) i = j;
      if (i == j) {

         if (state.getBlock() == BlockRegistry.mushroom_crop_dark.get()) {
            level.setBlock(pos, BlockRegistry.mushroom_dark.get().defaultBlockState(), 2);
         } else if (state.getBlock() == BlockRegistry.mushroom_crop_pink.get()) {
            level.setBlock(pos, BlockRegistry.mushroom_pink.get().defaultBlockState(), 2);
         } else if (state.getBlock() == BlockRegistry.mushroom_crop_white.get()) {
            level.setBlock(pos, BlockRegistry.mushroom_white.get().defaultBlockState(), 2);
         } else if (state.getBlock() == BlockRegistry.mushroom_crop_grey.get()) {
            level.setBlock(pos, BlockRegistry.mushroom_grey.get().defaultBlockState(), 2);
         }
      } else {
         level.setBlock(pos, this.withAge(i), 2);
      }
   }

   protected int getBonemealAgeIncrease(Level level) {
      return Mth.nextInt(level.random, 2, 5);
   }

   protected static float getGrowthChance(Block blockIn, Level level, BlockPos pos) {
      float f = 1.0F;
      BlockPos below = pos.below();
      for (int i = -1; i <= 1; i++) {
         for (int j = -1; j <= 1; j++) {
            float f1 = 0.0F;
            BlockState soil = level.getBlockState(below.offset(i, 0, j));
            if (soil.canSustainPlant(level, below.offset(i, 0, j), Direction.UP, (IPlantable)blockIn)) {
               f1 = 1.0F;
               if (soil.isFertile(level, below.offset(i, 0, j))) f1 = 3.0F;
            }
            if (i != 0 || j != 0) f1 /= 4.0F;
            f += f1;
         }
      }
      BlockPos n = pos.north(), s = pos.south(), w = pos.west(), e = pos.east();
      boolean rowX = blockIn == level.getBlockState(w).getBlock() || blockIn == level.getBlockState(e).getBlock();
      boolean rowZ = blockIn == level.getBlockState(n).getBlock() || blockIn == level.getBlockState(s).getBlock();
      if (rowX && rowZ) {
         f /= 2.0F;
      } else {
         boolean diag = blockIn == level.getBlockState(w.north()).getBlock()
                     || blockIn == level.getBlockState(e.north()).getBlock()
                     || blockIn == level.getBlockState(e.south()).getBlock()
                     || blockIn == level.getBlockState(w.south()).getBlock();
         if (diag) f /= 2.0F;
      }
      return f;
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) { return true; }
   @Override
   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) { return true; }
   @Override
   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) { this.grow(level, pos, state); }

   @Override
   public PlantType getPlantType(BlockGetter level, BlockPos pos) { return PlantType.CAVE; }

   @Override
   public BlockState getPlant(BlockGetter level, BlockPos pos) { return this.defaultBlockState(); }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(AGE);
   }

   protected ItemLike getBaseSeedId() { return this.asItem(); }

   @Override
   public java.util.List<net.minecraft.world.item.ItemStack> getDrops(BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
      java.util.List<net.minecraft.world.item.ItemStack> drops = new java.util.ArrayList<>();
      int age = state.getValue(AGE);
      boolean mature = age >= this.getMaxAge();
      ItemLike seedItem;
      net.minecraft.world.level.block.Block matureBlock;
      if (this == BlockRegistry.mushroom_crop_dark.get()) {
         seedItem = com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.seed_mushroom_dark.get();
         matureBlock = BlockRegistry.mushroom_dark.get();
      } else if (this == BlockRegistry.mushroom_crop_pink.get()) {
         seedItem = com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.seed_mushroom_pink.get();
         matureBlock = BlockRegistry.mushroom_pink.get();
      } else if (this == BlockRegistry.mushroom_crop_white.get()) {
         seedItem = com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.seed_mushroom_white.get();
         matureBlock = BlockRegistry.mushroom_white.get();
      } else if (this == BlockRegistry.mushroom_crop_grey.get()) {
         seedItem = com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.seed_mushroom_grey.get();
         matureBlock = BlockRegistry.mushroom_grey.get();
      } else {
         return drops;
      }
      if (!mature) {
         drops.add(new net.minecraft.world.item.ItemStack(seedItem));
         return drops;
      }
      net.minecraft.world.item.ItemStack tool = builder.getOptionalParameter(
            net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL);
      int fortune = tool == null ? 0 : net.minecraft.world.item.enchantment.EnchantmentHelper
            .getItemEnchantmentLevel(net.minecraft.world.item.enchantment.Enchantments.BLOCK_FORTUNE, tool);
      drops.add(new net.minecraft.world.item.ItemStack(matureBlock));
      net.minecraft.util.RandomSource rand = builder.getLevel().getRandom();
      int extraRounds = 3 + fortune;
      int seedCount = 1;
      for (int i = 0; i < extraRounds; i++) {
         if (rand.nextFloat() < 0.5714286F) seedCount++;
      }
      net.minecraft.world.item.ItemStack seedStack = new net.minecraft.world.item.ItemStack(seedItem, seedCount);
      drops.add(seedStack);
      return drops;
   }
}
