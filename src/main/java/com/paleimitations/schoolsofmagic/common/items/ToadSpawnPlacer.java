package com.paleimitations.schoolsofmagic.common.items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.BlockTags;

import com.paleimitations.schoolsofmagic.common.blocks.BlockToadSpawn;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ToadSpawnPlacer extends ItemWaterplantPlacer implements IItemMetaHandler {
   public ToadSpawnPlacer(Item.Properties props) {
      super(props);
   }

   @Override public int getDamage() { return 12; }
   @Override public String handleMeta(int meta) { return String.valueOf(meta); }

   @Override
   public net.minecraft.network.chat.Component getName(net.minecraft.world.item.ItemStack stack) {
      int meta = stack.getDamageValue();
      if (meta < 0 || meta > 11) meta = 0;
      return net.minecraft.network.chat.Component.translatable("item.som.toad_spawn_" + meta);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      BlockHitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
      BlockState block = BlockRegistry.spawn.get().defaultBlockState().setValue(BlockToadSpawn.TOAD_TYPE, itemstack.getDamageValue());
      if (raytraceresult.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(itemstack);
      } else {
         BlockPos blockpos = raytraceresult.getBlockPos();
         if (!worldIn.mayInteract(playerIn, blockpos) || !playerIn.mayUseItemAt(blockpos.relative(raytraceresult.getDirection()), raytraceresult.getDirection(), itemstack)) {
            return InteractionResultHolder.fail(itemstack);
         } else {
            BlockPos blockpos1 = blockpos.above();
            BlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getFluidState().is(FluidTags.WATER) && iblockstate.getValue(LiquidBlock.LEVEL) == 0 && worldIn.isEmptyBlock(blockpos1)) {
               if (block.getBlock() != BlockRegistry.plant_cattail.get()) {
                  worldIn.setBlock(blockpos1, block, 11);
                  if (!playerIn.getAbilities().instabuild) {
                     itemstack.shrink(1);
                  }

                  playerIn.awardStat(Stats.ITEM_USED.get(this));
                  worldIn.playSound(playerIn, blockpos, SoundEvents.LILY_PAD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                  return InteractionResultHolder.success(itemstack);
               }

               if (worldIn.getBlockState(blockpos.below()).getBlock() != Blocks.DIRT
                  && worldIn.getBlockState(blockpos.below()).getBlock() != Blocks.SAND
                  && worldIn.getBlockState(blockpos.below()).getBlock() != BlockRegistry.block_mud.get()) {
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
