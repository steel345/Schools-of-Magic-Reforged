package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.world.LockedDoorData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class ItemCopperKey extends Item {

   public ItemCopperKey(Item.Properties props) {
      super(props);
   }

   public static boolean isBound(ItemStack stack) {
      return stack.hasTag() && stack.getTag().contains("BoundX");
   }

   public static BlockPos boundPos(ItemStack stack) {
      CompoundTag t = stack.getTag();
      return new BlockPos(t.getInt("BoundX"), t.getInt("BoundY"), t.getInt("BoundZ"));
   }

   private static void setBound(ItemStack stack, BlockPos pos, String dim) {
      CompoundTag t = stack.getOrCreateTag();
      t.putInt("BoundX", pos.getX());
      t.putInt("BoundY", pos.getY());
      t.putInt("BoundZ", pos.getZ());
      t.putString("BoundDim", dim);
   }

   private static void clearBound(ItemStack stack) {
      if (stack.hasTag()) {
         CompoundTag t = stack.getTag();
         t.remove("BoundX");
         t.remove("BoundY");
         t.remove("BoundZ");
         t.remove("BoundDim");
      }
   }

   public static boolean matches(ItemStack stack, BlockPos doorPos) {
      return isBound(stack) && boundPos(stack).equals(doorPos);
   }

   public static BlockPos normalize(Level level, BlockPos pos, BlockState state) {
      if (state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
         return pos.below();
      }
      return pos;
   }

   @Override
   public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext ctx) {
      Level level = ctx.getLevel();
      BlockPos clicked = ctx.getClickedPos();
      BlockState state = level.getBlockState(clicked);
      boolean isDoor = state.getBlock() instanceof DoorBlock;
      boolean isTrap = state.getBlock() instanceof TrapDoorBlock;
      if (!isDoor && !isTrap) {
         return InteractionResult.PASS;
      }
      BlockPos doorPos = normalize(level, clicked, state);

      if (level instanceof ServerLevel sl) {
         LockedDoorData data = LockedDoorData.get(sl);
         if (!data.isLocked(doorPos)) {
            if (isBound(stack)) {
               data.unlock(boundPos(stack));
            }
            setBound(stack, doorPos, sl.dimension().location().toString());
            data.lock(doorPos);
            sl.playSound(null, doorPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.9F, 1.5F);
         }
      } else {
         long until = level.getGameTime() + 60L;
         com.paleimitations.schoolsofmagic.client.events.CopperKeyGlowRenderer.addGlow(doorPos, until);
         if (isDoor) {
            com.paleimitations.schoolsofmagic.client.events.CopperKeyGlowRenderer.addGlow(doorPos.above(), until);
         }
      }
      return InteractionResult.SUCCESS;
   }

   @Override
   public net.minecraft.world.InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (player.isShiftKeyDown() && isBound(stack)) {
         if (!level.isClientSide && level.getServer() != null) {
            BlockPos pos = boundPos(stack);
            String dim = stack.getTag().getString("BoundDim");
            ServerLevel target = null;
            try {
               net.minecraft.resources.ResourceKey<Level> key = net.minecraft.resources.ResourceKey.create(
                  net.minecraft.core.registries.Registries.DIMENSION, new net.minecraft.resources.ResourceLocation(dim));
               target = level.getServer().getLevel(key);
            } catch (Exception ignored) {
            }
            if (target == null && level instanceof ServerLevel sl) {
               target = sl;
            }
            if (target != null) {
               LockedDoorData.get(target).unlock(pos);
            }
            clearBound(stack);
            level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.9F, 0.7F);
         }
         return net.minecraft.world.InteractionResultHolder.success(stack);
      }
      return net.minecraft.world.InteractionResultHolder.pass(stack);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return isBound(stack);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level level, List<Component> tip, TooltipFlag flag) {
      if (isBound(stack)) {
         BlockPos p = boundPos(stack);
         tip.add(Component.translatable("item.som.copper_key.bound", p.getX(), p.getY(), p.getZ()).withStyle(ChatFormatting.AQUA));
         tip.add(Component.translatable("item.som.copper_key.unbind").withStyle(ChatFormatting.DARK_GRAY));
      } else {
         tip.add(Component.translatable("item.som.copper_key.unbound").withStyle(ChatFormatting.GRAY));
      }
   }
}
