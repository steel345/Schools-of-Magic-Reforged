package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockHangingBranch extends LeavesBlock {

   public BlockHangingBranch(BlockBehaviour.Properties props) {
      super(props);
      this.registerDefaultState(this.defaultBlockState()
         .setValue(DISTANCE, Integer.valueOf(1)).setValue(PERSISTENT, Boolean.TRUE));
   }

   @Override
   public boolean isRandomlyTicking(BlockState state) {
      return true;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return Shapes.empty();
   }

   private boolean connectedToLog(LevelReader level, BlockPos start) {
      Set<BlockPos> visited = new HashSet<>();
      ArrayDeque<BlockPos> queue = new ArrayDeque<>();
      queue.add(start);
      visited.add(start);

      int maxNodes = 8192;
      while (!queue.isEmpty() && visited.size() < maxNodes) {
         BlockPos p = queue.poll();
         for (Direction d : Direction.values()) {
            BlockPos np = p.relative(d);
            if (visited.contains(np)) continue;
            BlockState s = level.getBlockState(np);
            boolean inBounds = Math.abs(np.getX() - start.getX()) <= 10
                  && Math.abs(np.getZ() - start.getZ()) <= 10
                  && Math.abs(np.getY() - start.getY()) <= 32;
            if (s.is(BlockTags.LOGS)) {

               if (s.hasProperty(net.minecraft.world.level.block.RotatedPillarBlock.AXIS)
                     && s.getValue(net.minecraft.world.level.block.RotatedPillarBlock.AXIS) == Direction.Axis.Y) {
                  return true;
               }
               if (inBounds) { visited.add(np); queue.add(np); }
               continue;
            }

            if ((s.is(this) || s.is(BlockTags.LEAVES)) && inBounds) {
               visited.add(np);
               queue.add(np);
            }
         }
      }
      return false;
   }

   private void decayIfUnsupported(ServerLevel level, BlockPos pos) {
      if (!connectedToLog(level, pos)) {
         level.removeBlock(pos, false);
      }
   }

   @Override
   public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor,
                                 LevelAccessor level, BlockPos pos, BlockPos neighborPos) {

      level.scheduleTick(pos, this, 1);

      return state;
   }

   @Override
   public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      decayIfUnsupported(level, pos);
   }

   @Override
   public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
      decayIfUnsupported(level, pos);
   }

   @Override
   public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {

      ItemStack stack = new ItemStack(ItemRegistry.bi_magic_sapling.get());
      CompoundTag bs = new CompoundTag();
      bs.putString("type", "willow");
      stack.getOrCreateTag().put("BlockStateTag", bs);
      stack.setHoverName(Component.translatable("block.som.magic_sapling.willow").withStyle(s -> s.withItalic(false)));
      return stack;
   }

   @Override
   public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
      return true;
   }

   @Override
   public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
      return 20;
   }
}
