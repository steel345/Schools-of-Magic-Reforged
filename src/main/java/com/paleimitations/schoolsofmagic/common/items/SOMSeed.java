package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.BlockMushroomCrop;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class SOMSeed extends Item implements IPlantable {
   public SOMSeed(Item.Properties props) {
      super(props);
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      Level worldIn = ctx.getLevel();
      BlockPos pos = ctx.getClickedPos();
      Direction facing = ctx.getClickedFace();
      Player player = ctx.getPlayer();
      ItemStack itemstack = ctx.getItemInHand();
      BlockState state = worldIn.getBlockState(pos);

      BlockState plant = this.getBlock();
      net.minecraftforge.common.IPlantable plantable = (plant != null && plant.getBlock() instanceof net.minecraftforge.common.IPlantable p)
         ? p : this;
      if (facing == Direction.UP
         && player != null
         && plant != null
         && player.mayUseItemAt(pos.relative(facing), facing, itemstack)
         && state.getBlock().canSustainPlant(state, worldIn, pos, Direction.UP, plantable)
         && worldIn.isEmptyBlock(pos.above())) {
         worldIn.setBlockAndUpdate(pos.above(), this.getBlock());
         itemstack.shrink(1);
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.FAIL;
      }
   }

   @Override
   public BlockState getPlant(BlockGetter world, BlockPos pos) {
      return this.getBlock();
   }

   public BlockState getBlock() {
      if (this == ItemRegistry.seed_mushroom_dark.get()) {
         return BlockRegistry.mushroom_crop_dark.get().defaultBlockState().setValue(BlockMushroomCrop.AGE, 0);
      } else if (this == ItemRegistry.seed_mushroom_white.get()) {
         return BlockRegistry.mushroom_crop_white.get().defaultBlockState().setValue(BlockMushroomCrop.AGE, 0);
      } else if (this == ItemRegistry.seed_mushroom_grey.get()) {
         return BlockRegistry.mushroom_crop_grey.get().defaultBlockState().setValue(BlockMushroomCrop.AGE, 0);
      } else {
         return this == ItemRegistry.seed_mushroom_pink.get()
            ? BlockRegistry.mushroom_crop_pink.get().defaultBlockState().setValue(BlockMushroomCrop.AGE, 0)
            : null;
      }
   }

   @Override
   public PlantType getPlantType(BlockGetter world, BlockPos pos) {
      return PlantType.CROP;
   }
}
