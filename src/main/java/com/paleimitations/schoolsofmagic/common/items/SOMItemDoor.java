package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class SOMItemDoor extends Item {
   public SOMItemDoor(Item.Properties props) {
      super(props);
   }

   public Block getDoor() {
      if (this == ItemRegistry.itemdoor_ash.get()) {
         return BlockRegistry.door_ash.get();
      } else if (this == ItemRegistry.itemdoor_elder.get()) {
         return BlockRegistry.door_elder.get();
      } else if (this == ItemRegistry.itemdoor_pine.get()) {
         return BlockRegistry.door_pine.get();
      } else if (this == ItemRegistry.itemdoor_verde.get()) {
         return BlockRegistry.door_verde.get();
      } else if (this == ItemRegistry.itemdoor_willow.get()) {
         return BlockRegistry.door_willow.get();
      } else {
         return this == ItemRegistry.itemdoor_yew.get() ? BlockRegistry.door_yew.get() : Blocks.OAK_DOOR;
      }
   }

   @Override
   public InteractionResult useOn(UseOnContext context) {
      Direction facing = context.getClickedFace();
      if (facing != Direction.UP) {
         return InteractionResult.FAIL;
      } else {
         Level worldIn = context.getLevel();
         Player player = context.getPlayer();
         BlockPos pos = context.getClickedPos();
         BlockState iblockstate = worldIn.getBlockState(pos);
         Block block = iblockstate.getBlock();
         if (!iblockstate.canBeReplaced()) {
            pos = pos.relative(facing);
         }

         ItemStack itemstack = context.getItemInHand();
         if (player != null && player.mayUseItemAt(pos, facing, itemstack) && this.getDoor().defaultBlockState().canSurvive(worldIn, pos)) {
            Direction enumfacing = Direction.fromYRot(player.getYRot());
            int i = enumfacing.getStepX();
            int j = enumfacing.getStepZ();
            float hitX = (float)(context.getClickLocation().x - pos.getX());
            float hitZ = (float)(context.getClickLocation().z - pos.getZ());
            boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
            placeDoor(worldIn, pos, enumfacing, this.getDoor(), flag);
            SoundType soundtype = worldIn.getBlockState(pos).getSoundType(worldIn, pos, player);
            worldIn.playSound(
               player, pos, soundtype.getPlaceSound(), net.minecraft.sounds.SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F
            );
            itemstack.shrink(1);
            return InteractionResult.SUCCESS;
         } else {
            return InteractionResult.FAIL;
         }
      }
   }

   public static void placeDoor(Level worldIn, BlockPos pos, Direction facing, Block door, boolean isRightHinge) {
      BlockPos blockpos = pos.relative(facing.getClockWise());
      BlockPos blockpos1 = pos.relative(facing.getCounterClockWise());
      int i = (worldIn.getBlockState(blockpos1).isRedstoneConductor(worldIn, blockpos1) ? 1 : 0)
         + (worldIn.getBlockState(blockpos1.above()).isRedstoneConductor(worldIn, blockpos1.above()) ? 1 : 0);
      int j = (worldIn.getBlockState(blockpos).isRedstoneConductor(worldIn, blockpos) ? 1 : 0)
         + (worldIn.getBlockState(blockpos.above()).isRedstoneConductor(worldIn, blockpos.above()) ? 1 : 0);
      boolean flag = worldIn.getBlockState(blockpos1).getBlock() == door || worldIn.getBlockState(blockpos1.above()).getBlock() == door;
      boolean flag1 = worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.above()).getBlock() == door;
      if ((!flag || flag1) && j <= i) {
         if (flag1 && !flag || j < i) {
            isRightHinge = false;
         }
      } else {
         isRightHinge = true;
      }

      BlockPos blockpos2 = pos.above();
      boolean flag2 = worldIn.hasNeighborSignal(pos) || worldIn.hasNeighborSignal(blockpos2);
      BlockState iblockstate = door.defaultBlockState()
         .setValue(DoorBlock.FACING, facing)
         .setValue(DoorBlock.HINGE, isRightHinge ? DoorHingeSide.RIGHT : DoorHingeSide.LEFT)
         .setValue(DoorBlock.POWERED, flag2)
         .setValue(DoorBlock.OPEN, flag2);
      worldIn.setBlock(pos, iblockstate.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER), 2);
      worldIn.setBlock(blockpos2, iblockstate.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 2);
      worldIn.updateNeighborsAt(pos, door);
      worldIn.updateNeighborsAt(blockpos2, door);
   }
}
