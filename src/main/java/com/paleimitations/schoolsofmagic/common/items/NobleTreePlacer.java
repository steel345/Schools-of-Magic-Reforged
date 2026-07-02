package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.EntityNobleTree;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class NobleTreePlacer extends ItemHerbology {
   public NobleTreePlacer(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      BlockHitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, net.minecraft.world.level.ClipContext.Fluid.NONE);
      if (raytraceresult.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(itemstack);
      } else {
         BlockPos blockpos = raytraceresult.getBlockPos();
         BlockState state = worldIn.getBlockState(blockpos);
         BlockPos pos = null;
         int type = 12;
         float rot = 0.0F;
         if (state.getBlock() == BlockRegistry.log_ash.get()) {
            type = 0;
         }

         if (state.getBlock() == BlockRegistry.log_willow.get()) {
            type = 3;
         }

         if (state.getBlock() == Blocks.OAK_LOG) {
            type = 6;
         }

         if (state.getBlock() == Blocks.BIRCH_LOG) {
            type = 7;
         }

         if (state.getBlock() == Blocks.SPRUCE_LOG) {
            type = 8;
         }

         if (state.getBlock() == Blocks.DARK_OAK_LOG) {
            type = 9;
         }

         if (state.getBlock() == Blocks.JUNGLE_LOG) {
            type = 10;
         }

         if (state.getBlock() == Blocks.ACACIA_LOG) {
            type = 11;
         }

         Direction facing = raytraceresult.getDirection();
         switch (facing) {
            case DOWN:
            case UP:
            default:
               break;
            case EAST:
               pos = blockpos.east();
               rot = -90.0F;
               break;
            case WEST:
               pos = blockpos.west();
               rot = 90.0F;
               break;
            case SOUTH:
               pos = blockpos.south();
               rot = 0.0F;
               break;
            case NORTH:
               pos = blockpos.north();
               rot = 180.0F;
         }

         if (!worldIn.isClientSide && pos != null && type < 12) {
            EntityNobleTree tree = new EntityNobleTree(worldIn, blockpos);
            tree.setPos((double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5);
            tree.setTreeType(type);
            tree.setFaceType(worldIn.random.nextInt(4));
            tree.yBodyRot = rot;
            tree.yBodyRotO = rot;
            tree.setYRot(rot);
            tree.yRotO = rot;
            tree.yHeadRot = rot;
            tree.yHeadRotO = rot;
            worldIn.addFreshEntity(tree);
         }

         return InteractionResultHolder.success(itemstack);
      }
   }
}
