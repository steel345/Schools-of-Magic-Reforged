package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockDoorWall extends SOMBlock {

   public static final BooleanProperty KEYHOLE = BooleanProperty.create("keyhole");
   public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

   public BlockDoorWall(BlockBehaviour.Properties props) {
      super(props.randomTicks());
      this.registerDefaultState(this.stateDefinition.any().setValue(KEYHOLE, true).setValue(LOCKED, true));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      builder.add(KEYHOLE, LOCKED);
   }

   @Override
   public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack stack = player.getItemInHand(hand);
      if (world.isClientSide || !state.getValue(LOCKED)) return InteractionResult.PASS;
      if (stack.getItem() != ItemRegistry.ziggurat_door_key.get()) return InteractionResult.PASS;
      if (!stack.hasTag() || !stack.getTag().contains("lock")) return InteractionResult.PASS;
      BlockPos lockPos = BlockPos.of(stack.getTag().getLong("lock"));

      int dx = Math.abs(lockPos.getX() - pos.getX());
      int dy = Math.abs(lockPos.getY() - pos.getY());
      int dz = Math.abs(lockPos.getZ() - pos.getZ());
      if (Math.max(dx, Math.max(dy, dz)) > 11) return InteractionResult.PASS;

      stack.shrink(1);
      world.setBlock(pos, state.setValue(LOCKED, false), 3);
      world.scheduleTick(pos, this, 3);
      return InteractionResult.SUCCESS;
   }

   @Override
   public void tick(BlockState state, ServerLevel world, BlockPos pos, net.minecraft.util.RandomSource rand) {
      if (!state.getValue(LOCKED)) {
         for (Direction face : Direction.values()) {
            BlockState offset = world.getBlockState(pos.relative(face));
            if (offset.is(this) && offset.getValue(LOCKED)) {
               world.setBlock(pos.relative(face), offset.setValue(LOCKED, false), 3);
               world.scheduleTick(pos.relative(face), this, 3);
            }
         }
         world.removeBlock(pos, false);
      }
   }

   @Override
   public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(world, pos, state, placer, stack);
      if (placer == null) return;
      Direction face = placer.getDirection();
      BlockState wall = this.defaultBlockState().setValue(LOCKED, true).setValue(KEYHOLE, false);

      for (int i = 1; i <= 10 && world.getBlockState(pos.below(i)).isAir(); i++) world.setBlock(pos.below(i), wall, 3);
      for (int i = 1; i <= 10 && world.getBlockState(pos.above(i)).isAir(); i++) world.setBlock(pos.above(i), wall, 3);
      for (int i = 1; i <= 10 && world.getBlockState(pos.relative(face.getClockWise(), i)).isAir(); i++) this.setBlocks(world, pos.relative(face.getClockWise(), i));
      for (int i = 1; i <= 10 && world.getBlockState(pos.relative(face.getCounterClockWise(), i)).isAir(); i++) this.setBlocks(world, pos.relative(face.getCounterClockWise(), i));

      if (placer instanceof Player player) {
         ItemStack key = new ItemStack(ItemRegistry.ziggurat_door_key.get());
         CompoundTag nbt = new CompoundTag();
         nbt.putLong("lock", pos.asLong());
         key.setTag(nbt);
         if (!player.getInventory().add(key)) {
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), key));
         }
      }
   }

   public void setBlocks(Level world, BlockPos pos) {
      BlockState state = this.defaultBlockState().setValue(LOCKED, true).setValue(KEYHOLE, false);
      if (world.getBlockState(pos).isAir()) world.setBlock(pos, state, 3);
      for (int i = 1; i <= 10 && world.getBlockState(pos.below(i)).isAir(); i++) world.setBlock(pos.below(i), state, 3);
      for (int i = 1; i <= 10 && world.getBlockState(pos.above(i)).isAir(); i++) world.setBlock(pos.above(i), state, 3);
   }
}
