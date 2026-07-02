package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.entity.EntityTadpole;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.util.RandomSource;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockToadSpawn extends BlockPlantWaterplant {

   public static final IntegerProperty TOAD_TYPE = IntegerProperty.create("toad", 0, 11);

   public BlockToadSpawn(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.stateDefinition.any().setValue(TOAD_TYPE, 0));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(TOAD_TYPE);
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return true;
   }

   @Override
   public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
      super.randomTick(state, world, pos, rand);
      if (rand.nextInt(5) == 0) {

         EntityTadpole tad = new EntityTadpole(EntityRegistry.TADPOLE.get(), world);
         tad.setToadType(this.getToadType(state));
         tad.setPos(pos.getX() + 0.5, pos.getY() - 0.1, pos.getZ() + 0.5);
         world.addFreshEntity(tad);
         if (rand.nextBoolean()) world.removeBlock(pos, false);
      }
   }

   public IntegerProperty getToadProperty() {
      return TOAD_TYPE;
   }

   protected int getToadType(BlockState state) {
      return state.getValue(this.getToadProperty());
   }

   public BlockState withToadType(int toad) {
      return this.defaultBlockState().setValue(this.getToadProperty(), toad);
   }

   @Override
   public ItemStack getCloneItemStack(net.minecraft.world.level.BlockGetter level,
                                      BlockPos pos, BlockState state) {
      ItemStack stack = new ItemStack(ItemRegistry.toad_spawn.get());
      stack.setDamageValue(state.getValue(TOAD_TYPE));
      return stack;
   }

   public ItemStack getPickItemStack() {
      return new ItemStack(ItemRegistry.toad_spawn.get());
   }

   @Override
   public java.util.List<ItemStack> getDrops(BlockState state,
                                             net.minecraft.world.level.storage.loot.LootParams.Builder params) {
      ItemStack stack = new ItemStack(ItemRegistry.toad_spawn.get());
      stack.setDamageValue(state.getValue(TOAD_TYPE));
      return java.util.Collections.singletonList(stack);
   }
}
