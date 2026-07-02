package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.registries.RegistryObject;

public class ItemMagicPlantSeed extends Item implements IMagicType {

   private static RegistryObject<Block> cropFor(int meta) {
      switch (meta) {
         case 0:  return BlockRegistry.crop_pyromancy;
         case 1:  return BlockRegistry.crop_heliomancy;
         case 2:  return BlockRegistry.crop_aeromancy;
         case 3:  return BlockRegistry.crop_geomancy;
         case 4:  return BlockRegistry.crop_animancy;
         case 5:  return BlockRegistry.crop_electromancy;
         case 6:  return BlockRegistry.crop_hydromancy;
         case 7:  return BlockRegistry.crop_cryomancy;
         case 8:  return BlockRegistry.crop_hieromancy;
         case 9:  return BlockRegistry.crop_chaotics;
         case 10: return BlockRegistry.crop_auramancy;
         case 11: return BlockRegistry.crop_astromancy;
         case 12: return BlockRegistry.crop_infernality;
         case 13: return BlockRegistry.crop_spectromancy;
         case 14: return BlockRegistry.crop_umbramancy;
         case 15: return BlockRegistry.crop_necromancy;
         default: return BlockRegistry.crop_pyromancy;
      }
   }

   public ItemMagicPlantSeed(Item.Properties props) {
      super(props);
   }

   @Override
   public net.minecraft.network.chat.Component getName(ItemStack stack) {
      int meta = Math.max(0, Math.min(stack.getDamageValue(), this.getDamage() - 1));
      return net.minecraft.network.chat.Component.translatable(this.getDescriptionId() + "_" + this.handleMeta(meta));
   }

   public InteractionResult useOn(UseOnContext context) {
      Level worldIn = context.getLevel();
      BlockPos pos = context.getClickedPos();
      Direction facing = context.getClickedFace();
      Player player = context.getPlayer();
      ItemStack itemstack = context.getItemInHand();
      BlockState soil = worldIn.getBlockState(pos);
      int meta = Math.max(0, Math.min(itemstack.getDamageValue(), EnumMagicType.values().length - 1));
      BlockState crop = cropFor(meta).get().defaultBlockState();
      if (facing == Direction.UP
         && player != null
         && player.mayUseItemAt(pos.relative(facing), facing, itemstack)
         && crop.canSurvive(worldIn, pos.above())
         && worldIn.isEmptyBlock(pos.above())) {
         worldIn.setBlockAndUpdate(pos.above(), crop);
         if (player instanceof ServerPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos.above(), itemstack);
         }

         itemstack.shrink(1);
         return InteractionResult.SUCCESS;
      } else {
         return InteractionResult.FAIL;
      }
   }
}
