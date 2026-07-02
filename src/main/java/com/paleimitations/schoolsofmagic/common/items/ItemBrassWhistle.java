package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.EntityPhoenix;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemBrassWhistle extends Item {

   private static final int CLEAN_TICKS = 100;

   public ItemBrassWhistle(Properties properties) {
      super(properties.durability(20));
   }

   public static boolean isAged(ItemStack stack) {
      return stack.hasTag() && stack.getTag().getBoolean("Aged");
   }

   public static boolean isWaxed(ItemStack stack) {
      return stack.hasTag() && stack.getTag().getBoolean("NoAge");
   }

   public static ItemStack waxed(ItemStack stack) {
      ItemStack copy = stack.copy();
      CompoundTag tag = copy.getOrCreateTag();
      tag.putBoolean("NoAge", true);
      tag.putBoolean("Aged", false);
      tag.putInt("WaterTimer", 0);
      return copy;
   }

   @Override
   public Component getName(ItemStack stack) {
      if (isAged(stack)) return Component.translatable("item.som.brass_whistle.aged");
      return Component.translatable("item.som.brass_whistle");
   }

   @Override
   public boolean isFoil(ItemStack stack) {
      return isWaxed(stack);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
      if (isWaxed(stack)) tooltip.add(Component.translatable("item.som.brass_whistle.waxed").withStyle(net.minecraft.ChatFormatting.GOLD));
   }

   @Override
   public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
      if (level.isClientSide) return;
      CompoundTag tag = stack.getOrCreateTag();
      long day = level.getDayTime() / 24000L;
      if (!tag.contains("LastDay")) {
         tag.putLong("LastDay", day);
         return;
      }
      long lastDay = tag.getLong("LastDay");
      if (day > lastDay) {
         tag.putLong("LastDay", day);
         if (!tag.getBoolean("NoAge") && !tag.getBoolean("Aged")) {
            for (long d = lastDay; d < day; d++) {
               if (level.random.nextFloat() < 0.2F) {
                  tag.putBoolean("Aged", true);
                  break;
               }
            }
         }
      }
   }

   @Override
   public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
      if (entity.level().isClientSide) return false;
      CompoundTag tag = stack.getOrCreateTag();
      if (tag.getBoolean("Aged") && entity.isInWater()) {
         int wt = tag.getInt("WaterTimer") + 1;
         tag.putInt("WaterTimer", wt);
         if (wt >= CLEAN_TICKS) {
            tag.putBoolean("Aged", false);
            tag.putInt("WaterTimer", 0);
            entity.level().playSound(null, entity.blockPosition(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 0.7F, 1.4F);
            if (entity.level() instanceof net.minecraft.server.level.ServerLevel sl) {
               sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SPLASH, entity.getX(), entity.getY() + 0.2D, entity.getZ(), 12, 0.2D, 0.2D, 0.2D, 0.0D);
            }
         }
      } else if (!entity.isInWater() && tag.getInt("WaterTimer") != 0) {
         tag.putInt("WaterTimer", 0);
      }
      return false;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (isAged(stack)) {
         level.playSound(player, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.PLAYERS, 0.5F, 0.5F);
         player.getCooldowns().addCooldown(this, 20);
         return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
      }
      level.playSound(player, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.4F);
      if (!level.isClientSide) {
         List<EntityPhoenix> phoenixes = level.getEntitiesOfClass(EntityPhoenix.class,
            player.getBoundingBox().inflate(200.0D), p -> p.isTame() && p.isOwnedBy(player) && !p.isBaby());
         boolean falling = !player.onGround() && player.getDeltaMovement().y < -0.2D && player.fallDistance > 2.0F;
         EntityPhoenix catcher = null;
         for (EntityPhoenix p : phoenixes) {
            if (catcher == null) {
               catcher = p;
               continue;
            }
            p.summonTo(player);
         }
         if (catcher != null) {
            if (falling) {
               net.minecraft.core.BlockPos sp = player.blockPosition().below(10);
               boolean deepOpen = player.fallDistance > 15.0F
                  && level.getBlockState(sp).isAir()
                  && level.getBlockState(sp.above()).isAir()
                  && level.getBlockState(sp.above(2)).isAir();
               if (deepOpen) catcher.beginCatch(player, sp);
               else catcher.catchFalling(player);
            } else {
               catcher.summonTo(player);
            }
         }
         if (!isAged(stack)) {
            stack.hurtAndBreak(1, player, pl -> pl.broadcastBreakEvent(hand));
         }
      }
      player.getCooldowns().addCooldown(this, 20);
      return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
   }
}
