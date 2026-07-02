package com.paleimitations.schoolsofmagic.common.items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantCattail;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemWaterplantPlacer extends ItemHerbology {
   public ItemWaterplantPlacer(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      BlockHitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
      BlockState block = null;
      if (this == ItemRegistry.item_bladderwort.get()) {
         block = BlockRegistry.plant_bladderwort.get().defaultBlockState();
      }

      if (this == ItemRegistry.item_duckweed.get()) {
         block = BlockRegistry.plant_duckweed.get().defaultBlockState();
      }

      if (this == ItemRegistry.item_algae.get()) {
         block = BlockRegistry.plant_algae.get().defaultBlockState();
      }

      if (this == ItemRegistry.item_cattail.get()) {
         block = BlockRegistry.plant_cattail.get().defaultBlockState().setValue(BlockPlantCattail.TYPE, worldIn.random.nextInt(3));
      }

      if (raytraceresult.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(itemstack);
      } else {
         BlockPos blockpos = raytraceresult.getBlockPos();
         if (!worldIn.mayInteract(playerIn, blockpos) || !playerIn.mayUseItemAt(blockpos.relative(raytraceresult.getDirection()), raytraceresult.getDirection(), itemstack)) {
            return InteractionResultHolder.fail(itemstack);
         } else {
            BlockPos blockpos1 = blockpos.above();
            BlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getFluidState().is(FluidTags.WATER) && iblockstate.getValue(LiquidBlock.LEVEL) == 0 && worldIn.isEmptyBlock(blockpos1)
               || iblockstate.getBlock() == BlockRegistry.block_mud.get()
               || iblockstate.is(BlockTags.ICE)) {
               if (block.getBlock() != BlockRegistry.plant_cattail.get()) {
                  worldIn.setBlock(blockpos1, block, 11);
                  if (!playerIn.getAbilities().instabuild) {
                     itemstack.shrink(1);
                  }

                  playerIn.awardStat(Stats.ITEM_USED.get(this));
                  worldIn.playSound(playerIn, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                  return InteractionResultHolder.success(itemstack);
               }

               if (!worldIn.getBlockState(blockpos.below()).is(BlockTags.DIRT)
                  && worldIn.getBlockState(blockpos.below()).getBlock() != BlockRegistry.block_mud.get()
                  && !worldIn.getBlockState(blockpos.below()).is(BlockTags.SAND)) {
                  return InteractionResultHolder.fail(itemstack);
               }

               worldIn.setBlock(blockpos1, block, 11);
               if (!playerIn.getAbilities().instabuild) {
                  itemstack.shrink(1);
               }

               playerIn.awardStat(Stats.ITEM_USED.get(this));
               worldIn.playSound(playerIn, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
               return InteractionResultHolder.success(itemstack);
            } else {
               return InteractionResultHolder.fail(itemstack);
            }
         }
      }
   }
}
