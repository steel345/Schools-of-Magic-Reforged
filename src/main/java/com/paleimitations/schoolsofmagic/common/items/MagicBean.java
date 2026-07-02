package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantBeanstalk;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class MagicBean extends ItemHerbology {
   public MagicBean(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      RandomSource rand = worldIn.random;
      ItemStack itemstack = playerIn.getItemInHand(handIn);
      BlockHitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, net.minecraft.world.level.ClipContext.Fluid.NONE);
      if (raytraceresult.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(itemstack);
      } else {
         BlockPos pos = raytraceresult.getBlockPos().above();
         if (BlockRegistry.plant_beanstalk.get().defaultBlockState().canSurvive(worldIn, pos)
            && worldIn.isEmptyBlock(pos.above())
            && worldIn.isEmptyBlock(pos.above(2))) {
            this.generate(playerIn, worldIn, rand, pos);
            return InteractionResultHolder.success(itemstack);
         } else {
            return InteractionResultHolder.fail(itemstack);
         }
      }
   }

   private void generate(Player playerIn, Level worldIn, RandomSource rand, BlockPos pos) {
      boolean flag1 = false;

      for (int i = 0; i <= 20; i++) {
         if (i + pos.getY() < 255) {
            if (worldIn.isEmptyBlock(pos.above(i))
               && worldIn.isEmptyBlock(pos.above(i + 1))
               && worldIn.isEmptyBlock(pos.above(i + 2))
               && BlockRegistry.plant_beanstalk.get().defaultBlockState().canSurvive(worldIn, pos.above(i))) {
               if (worldIn.getBlockState(pos.above(i).east()).isFaceSturdy(worldIn, pos.above(i).east(), Direction.UP)
                  && worldIn.isEmptyBlock(pos.above(i + 1).east())
                  && worldIn.isEmptyBlock(pos.above(i + 2).east())) {
                  flag1 = true;
               }

               if (worldIn.getBlockState(pos.above(i).west()).isFaceSturdy(worldIn, pos.above(i).west(), Direction.UP)
                  && worldIn.isEmptyBlock(pos.above(i + 1).west())
                  && worldIn.isEmptyBlock(pos.above(i + 2).west())) {
                  flag1 = true;
               }

               if (worldIn.getBlockState(pos.above(i).south()).isFaceSturdy(worldIn, pos.above(i).south(), Direction.UP)
                  && worldIn.isEmptyBlock(pos.above(i + 1).south())
                  && worldIn.isEmptyBlock(pos.above(i + 2).south())) {
                  flag1 = true;
               }

               if (worldIn.getBlockState(pos.above(i).north()).isFaceSturdy(worldIn, pos.above(i).north(), Direction.UP)
                  && worldIn.isEmptyBlock(pos.above(i + 1).north())
                  && worldIn.isEmptyBlock(pos.above(i + 2).north())) {
                  flag1 = true;
               }

               worldIn.setBlock(
                  pos.above(i),
                  BlockRegistry.plant_beanstalk.get().defaultBlockState().setValue(BlockPlantBeanstalk.FACING, BlockPlantBeanstalk.randomFacing(rand)),
                  2
               );
               playerIn.setPos((double)pos.getX() + 0.5, (double)(i + pos.getY() + 1), (double)pos.getZ() + 0.5);
            }

            if (flag1) {
               break;
            }
         }
      }
   }
}
