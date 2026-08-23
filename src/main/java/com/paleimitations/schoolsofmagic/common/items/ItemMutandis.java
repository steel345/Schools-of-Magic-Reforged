package com.paleimitations.schoolsofmagic.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemMutandis extends Item {
   public ItemMutandis(Properties props) {
      super(props);
   }

   private static Class<?> categoryOf(Block block) {
      if (block instanceof CropBlock || block instanceof StemBlock) {
         return CropBlock.class;
      }
      if (block instanceof DoublePlantBlock) {
         return DoublePlantBlock.class;
      }
      if (block instanceof FlowerBlock) {
         return FlowerBlock.class;
      }
      if (block instanceof MushroomBlock) {
         return MushroomBlock.class;
      }
      if (block instanceof SaplingBlock) {
         return SaplingBlock.class;
      }
      if (block instanceof BushBlock) {
         return BushBlock.class;
      }
      return null;
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      Level level = ctx.getLevel();
      BlockPos pos = ctx.getClickedPos();
      BlockState state = level.getBlockState(pos);
      Block block = state.getBlock();
      Class<?> cat = categoryOf(block);
      if (cat == null) {
         return InteractionResult.PASS;
      }
      BlockPos base = pos;
      if (state.hasProperty(DoublePlantBlock.HALF) && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
         base = pos.below();
         state = level.getBlockState(base);
         block = state.getBlock();
      }
      if (!level.isClientSide) {
         boolean underwater = state.getFluidState().is(FluidTags.WATER) || level.getFluidState(base).is(FluidTags.WATER);
         ResourceLocation origin = ForgeRegistries.BLOCKS.getKey(block);
         String ns = origin == null ? "minecraft" : origin.getNamespace();
         List<Block> candidates = new ArrayList<>();
         List<Block> fallback = new ArrayList<>();
         for (Block b : ForgeRegistries.BLOCKS.getValues()) {
            if (b == block || categoryOf(b) != cat) {
               continue;
            }
            if (!underwater && b.defaultBlockState().getFluidState().is(FluidTags.WATER)) {
               continue;
            }
            if (!b.defaultBlockState().canSurvive(level, base)) {
               continue;
            }
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(b);
            if (key != null && key.getNamespace().equals(ns)) {
               candidates.add(b);
            } else {
               fallback.add(b);
            }
         }
         if (candidates.isEmpty()) {
            candidates = fallback;
         }
         if (candidates.isEmpty()) {
            return InteractionResult.PASS;
         }
         Block target = candidates.get(level.random.nextInt(candidates.size()));
         if (cat == DoublePlantBlock.class && target instanceof DoublePlantBlock) {
            level.setBlock(base.above(), net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            DoublePlantBlock.placeAt(level, target.defaultBlockState(), base, 3);
         } else {
            level.setBlockAndUpdate(base, target.defaultBlockState());
         }
         level.levelEvent(1505, base, 0);
         Player player = ctx.getPlayer();
         if (player != null && !player.getAbilities().instabuild) {
            ItemStack stack = ctx.getItemInHand();
            int dmg = stack.getDamageValue() + 1;
            if (dmg >= stack.getMaxDamage()) {
               stack.shrink(1);
               BrewItemUtil.giveFlask(player);
            } else {
               stack.setDamageValue(dmg);
            }
         }
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return false;
   }
}
