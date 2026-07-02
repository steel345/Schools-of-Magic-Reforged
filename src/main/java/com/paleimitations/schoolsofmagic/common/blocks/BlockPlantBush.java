package com.paleimitations.schoolsofmagic.common.blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BlockPlantBush extends SOMPlant implements BonemealableBlock {

   public static final EnumProperty<EnumBushGrowth> GROWN = EnumProperty.create("grown", EnumBushGrowth.class);

   public BlockPlantBush(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(GROWN, EnumBushGrowth.GROWN));
   }

   @Override
   public float getDestroyProgress(BlockState state, net.minecraft.world.entity.player.Player player,
         net.minecraft.world.level.BlockGetter level, BlockPos pos) {
      return BushBreak.progress(player);
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) { return true; }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
      super.randomTick(state, level, pos, rand);
      if (level.getRawBrightness(pos.above(), 0) >= 9 && state.getValue(GROWN) == EnumBushGrowth.DEAD) {
         boolean nearWater = false;
         for (Direction face : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(pos.below().relative(face)).getFluidState().is(FluidTags.WATER)) { nearWater = true; break; }
         }
         if (nearWater && rand.nextInt(20) == 0) {
            level.setBlock(pos, state.setValue(GROWN, EnumBushGrowth.GROWN), 2);
         }
      }
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack held = player.getItemInHand(hand);

      if (held.getItem() == Items.SHEARS && state.getValue(GROWN) == EnumBushGrowth.GROWN
            && (this == BlockRegistry.plant_brittle.get() || this == BlockRegistry.plant_creosote.get())) {

         int count = 1 + level.random.nextInt(2);
         net.minecraft.world.item.Item leaf = (this == BlockRegistry.plant_brittle.get())
            ? ItemRegistry.item_brittle_leaves.get()
            : ItemRegistry.item_creosote_leaves.get();
         ItemStack drop = new ItemStack(leaf, count);
         if (!player.getInventory().add(drop) && !level.isClientSide) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), drop);
         }
         if (!player.getAbilities().instabuild) held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
         level.setBlock(pos, state.setValue(GROWN, EnumBushGrowth.DEAD), 2);

         player.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
         return InteractionResult.SUCCESS;
      }
      return InteractionResult.PASS;
   }

   @Override
   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
      if (this == BlockRegistry.plant_brittle.get() || this == BlockRegistry.plant_creosote.get()) {
         return state.is(Blocks.SAND) || state.is(Blocks.DIRT) || state.is(Blocks.FARMLAND) || state.is(Blocks.GRASS_BLOCK);
      }
      return super.mayPlaceOn(state, level, pos);
   }

   @Override
   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
      return state.getValue(GROWN) == EnumBushGrowth.DEAD;
   }

   @Override
   public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
      return state.getValue(GROWN) == EnumBushGrowth.DEAD;
   }

   @Override
   public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
      level.setBlockAndUpdate(pos, state.setValue(GROWN, EnumBushGrowth.GROWN));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(GROWN);
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state,
         net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      ItemStack stack = new ItemStack(this);
      if (state.getValue(GROWN) == EnumBushGrowth.DEAD) {
         net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
         bs.putString("grown", EnumBushGrowth.DEAD.getSerializedName());
         stack.getOrCreateTag().put("BlockStateTag", bs);
      }
      return java.util.Collections.singletonList(stack);
   }

   @Override
   public void entityInside(net.minecraft.world.level.block.state.BlockState state, net.minecraft.world.level.Level world, net.minecraft.core.BlockPos pos, net.minecraft.world.entity.Entity entity) {
      super.entityInside(state, world, pos, entity);
      BushSound.rustle(world, entity);

      if (entity instanceof net.minecraft.world.entity.LivingEntity) {
         entity.makeStuckInBlock(state, new net.minecraft.world.phys.Vec3(0.8D, 0.75D, 0.8D));
      }
   }
}
