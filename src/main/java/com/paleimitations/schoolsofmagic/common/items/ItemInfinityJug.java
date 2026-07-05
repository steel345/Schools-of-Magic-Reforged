package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ItemInfinityJug extends Item {

   public ItemInfinityJug(Properties props) {
      super(props);
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      Level level = ctx.getLevel();
      BlockPos clicked = ctx.getClickedPos();
      BlockState clickedState = level.getBlockState(clicked);
      if (clickedState.getBlock() instanceof net.minecraft.world.level.block.FarmBlock) {
         if (!level.isClientSide) {
            if (clickedState.getValue(net.minecraft.world.level.block.FarmBlock.MOISTURE) < 7) {
               level.setBlock(clicked, clickedState.setValue(net.minecraft.world.level.block.FarmBlock.MOISTURE, 7), 3);
            }
            level.playSound(null, clicked, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            Player p = ctx.getPlayer();
            if (p != null && !p.getAbilities().instabuild) {
               ctx.getItemInHand().hurtAndBreak(1, p, pl -> pl.broadcastBreakEvent(ctx.getHand()));
            }
         }
         return InteractionResult.sidedSuccess(level.isClientSide);
      }
      if (clickedState.is(Blocks.CAULDRON)) {
         if (!level.isClientSide) {
            level.setBlock(clicked, Blocks.WATER_CAULDRON.defaultBlockState()
               .setValue(net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL, 3), 3);
            level.playSound(null, clicked, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            Player p = ctx.getPlayer();
            if (p != null && !p.getAbilities().instabuild) {
               ctx.getItemInHand().hurtAndBreak(1, p, pl -> pl.broadcastBreakEvent(ctx.getHand()));
            }
         }
         return InteractionResult.sidedSuccess(level.isClientSide);
      }
      BlockPos target = clickedState.canBeReplaced() ? clicked : clicked.relative(ctx.getClickedFace());
      BlockState targetState = level.getBlockState(target);
      if (!targetState.canBeReplaced()) {
         return InteractionResult.PASS;
      }
      if (!level.isClientSide) {
         level.setBlock(target, Blocks.WATER.defaultBlockState(), 11);
         level.playSound(null, target, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
         Player player = ctx.getPlayer();
         ItemStack stack = ctx.getItemInHand();
         if (player != null && !player.getAbilities().instabuild) {
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(ctx.getHand()));
         }
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return false;
   }
}
