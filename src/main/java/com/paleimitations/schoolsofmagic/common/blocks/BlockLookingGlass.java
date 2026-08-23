package com.paleimitations.schoolsofmagic.common.blocks;

import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityLookingGlass;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockLookingGlass extends Block implements EntityBlock {
   public BlockLookingGlass(Properties props) {
      super(props);
   }

   @Nullable
   @Override
   public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new TileEntityLookingGlass(pos, state);
   }

   @Override
   public void setPlacedBy(Level level, BlockPos pos, BlockState state,
         @Nullable LivingEntity placer, ItemStack stack) {
      super.setPlacedBy(level, pos, state, placer, stack);
      if (!level.isClientSide && placer instanceof Player player
            && level.getBlockEntity(pos) instanceof TileEntityLookingGlass glass) {
         if (player.isShiftKeyDown()) {
            glass.setCamo(camouflageFrom(level, pos.below()));
         }
         glass.setOwner(player.getUUID());
      }
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
         CollisionContext ctx) {
      if (ctx instanceof EntityCollisionContext entityCtx) {
         Entity entity = entityCtx.getEntity();
         if (entity instanceof Player player && isOwner(level, pos, player)) {
            return Shapes.empty();
         }
      }
      return super.getCollisionShape(state, level, pos, ctx);
   }

   @Nullable
   private static BlockState camouflageFrom(Level level, BlockPos below) {
      BlockState state = level.getBlockState(below);
      if (state.getBlock() instanceof BlockLookingGlass) {
         return level.getBlockEntity(below) instanceof TileEntityLookingGlass glass
            ? glass.getCamo() : null;
      }
      if (state.isAir() || state.getRenderShape() != net.minecraft.world.level.block.RenderShape.MODEL) {
         return null;
      }
      return state;
   }

   private static boolean isOwner(BlockGetter level, BlockPos pos, Player player) {
      if (level.getBlockEntity(pos) instanceof TileEntityLookingGlass glass) {
         UUID owner = glass.getOwner();
         return owner != null && owner.equals(player.getUUID());
      }
      return false;
   }

   @Override
   public boolean skipRendering(BlockState state, BlockState adjacent, Direction dir) {
      return adjacent.is(this) || super.skipRendering(state, adjacent, dir);
   }

   @Override
   public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos,
         CollisionContext ctx) {
      return Shapes.empty();
   }

   @Override
   public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
      return 1.0F;
   }

   @Override
   public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
      return true;
   }
}
