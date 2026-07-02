package com.paleimitations.schoolsofmagic.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockOresmoky extends SOMBlock {

   private final Item drop;
   protected static final VoxelShape ORESAND_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

   public BlockOresmoky(BlockBehaviour.Properties props, Item drop) {
      super(props);
      this.drop = drop;
   }

   public Item getDrop() {
      return this.drop;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
      return ORESAND_SHAPE;
   }

   @Override
   public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
      Vec3 d = entity.getDeltaMovement();
      entity.setDeltaMovement(d.x * 0.4D, d.y, d.z * 0.4D);
      super.stepOn(level, pos, state, entity);
   }
}
