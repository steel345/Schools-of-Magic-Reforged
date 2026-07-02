package com.paleimitations.schoolsofmagic.common.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemBlockMagicPlant extends BlockItem implements IMagicType {
   public ItemBlockMagicPlant(Block block, Item.Properties props) {
      super(block, props);
   }

   @Override
   public net.minecraft.network.chat.Component getName(ItemStack stack) {
      net.minecraft.nbt.CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("BlockStateTag")) {
         String type = tag.getCompound("BlockStateTag").getString("type");
         if (type != null && !type.isEmpty()) {
            return net.minecraft.network.chat.Component.translatable("block.som.magic_plant." + type);
         }
      }
      return super.getName(stack);
   }

   private static boolean isHydromancy(ItemStack stack) {
      net.minecraft.nbt.CompoundTag tag = stack.getTag();
      return tag != null && tag.contains("BlockStateTag")
            && "hydromancy".equals(tag.getCompound("BlockStateTag").getString("type"));
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (!isHydromancy(stack)) {
         return InteractionResultHolder.pass(stack);
      }
      BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
      if (hit.getType() != HitResult.Type.BLOCK) {
         return InteractionResultHolder.pass(stack);
      }
      BlockHitResult above = hit.withPosition(hit.getBlockPos().above());
      InteractionResult result = this.useOn(new UseOnContext(player, hand, above));
      return new InteractionResultHolder<>(result, stack);
   }
}
