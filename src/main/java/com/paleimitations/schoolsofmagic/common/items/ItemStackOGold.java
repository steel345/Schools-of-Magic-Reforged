package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.BlockGoldCoins;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ItemStackOGold extends BlockItem {
   public ItemStackOGold(Block block, Item.Properties props) {
      super(block, props);
   }

   public InteractionResult useOn(UseOnContext context) {
      Level worldIn = context.getLevel();
      BlockPos pos = context.getClickedPos();
      Direction facing = context.getClickedFace();
      Player player = context.getPlayer();
      ItemStack itemstack = context.getItemInHand();
      if (!itemstack.isEmpty() && player != null && player.mayUseItemAt(pos, facing, itemstack)) {
         BlockState iblockstate = worldIn.getBlockState(pos);
         Block block = iblockstate.getBlock();
         BlockPos blockpos = pos;
         if ((facing != Direction.UP || block != this.getBlock()) && !iblockstate.canBeReplaced()) {
            blockpos = pos.relative(facing);
            iblockstate = worldIn.getBlockState(blockpos);
            block = iblockstate.getBlock();
         }

         if (block == this.getBlock()) {
            int i = iblockstate.getValue(BlockGoldCoins.LAYERS);
            if (i < 8) {
               BlockState iblockstate1 = iblockstate.setValue(BlockGoldCoins.LAYERS, i + 1);
               VoxelShape voxelshape = iblockstate1.getCollisionShape(worldIn, blockpos);
               if (!voxelshape.isEmpty()
                  && worldIn.isUnobstructed(null, voxelshape.move(blockpos.getX(), blockpos.getY(), blockpos.getZ()))
                  && worldIn.setBlock(blockpos, iblockstate1, 10)) {
                  SoundType soundtype = this.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                  worldIn.playSound(
                     player,
                     blockpos,
                     soundtype.getPlaceSound(),
                     net.minecraft.sounds.SoundSource.BLOCKS,
                     (soundtype.getVolume() + 1.0F) / 2.0F,
                     soundtype.getPitch() * 0.8F
                  );
                  if (player instanceof ServerPlayer) {
                     CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos, itemstack);
                  }

                  itemstack.shrink(1);
                  return InteractionResult.SUCCESS;
               }
            }
         }

         return super.useOn(context);
      } else {
         return InteractionResult.FAIL;
      }
   }
}
