package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.IManaData;
import com.paleimitations.schoolsofmagic.common.handlers.SOMSoundHandler;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemMagicMirror extends Item {
   public static final int CHANNEL_TICKS = 84;
   private static final float MANA_COST = 50.0F;
   private static final int RING_POINTS = 12;
   private static final double RING_RADIUS = 1.3D;

   public ItemMagicMirror(Item.Properties props) {
      super(props);
   }

   @Override
   public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
      consumer.accept(new net.minecraftforge.client.extensions.common.IClientItemExtensions() {
         @Override
         public net.minecraft.client.model.HumanoidModel.ArmPose getArmPose(
               LivingEntity entity, InteractionHand hand, ItemStack stack) {
            if (entity.isUsingItem() && entity.getUseItem() == stack
                  && entity.getUsedItemHand() == hand) {
               return net.minecraft.client.model.HumanoidModel.ArmPose.SPYGLASS;
            }
            return null;
         }
      });
   }

   public static final String TAG_X = "MirrorX";
   public static final String TAG_Y = "MirrorY";
   public static final String TAG_Z = "MirrorZ";
   public static final String TAG_BOUND = "MirrorBound";
   public static final String TAG_BALL = "MirrorBall";
   private static final float BALL_COST = 10.0F;

   public static boolean isBallBound(ItemStack stack) {
      return hasBoundPos(stack) && stack.getTag().getBoolean(TAG_BALL);
   }

   public static boolean hasBoundPos(ItemStack stack) {
      return stack.hasTag() && stack.getTag().getBoolean(TAG_BOUND);
   }

   public static int getBound(ItemStack stack, String key) {
      return stack.hasTag() ? stack.getTag().getInt(key) : 0;
   }

   public static void setBoundPos(ItemStack stack, int x, int y, int z) {
      net.minecraft.nbt.CompoundTag tag = stack.getOrCreateTag();
      tag.putInt(TAG_X, x);
      tag.putInt(TAG_Y, y);
      tag.putInt(TAG_Z, z);
      tag.putBoolean(TAG_BOUND, true);
      tag.putBoolean(TAG_BALL, false);
   }

   public static void setBallPos(ItemStack stack, int x, int y, int z) {
      setBoundPos(stack, x, y, z);
      stack.getOrCreateTag().putBoolean(TAG_BALL, true);
   }

   public static void clearBoundPos(ItemStack stack) {
      if (stack.hasTag()) {
         stack.getTag().putBoolean(TAG_BOUND, false);
         stack.getTag().putBoolean(TAG_BALL, false);
      }
   }

   @Override
   public InteractionResult useOn(UseOnContext ctx) {
      Player player = ctx.getPlayer();
      if (player == null || !player.isShiftKeyDown()) {
         return InteractionResult.PASS;
      }
      BlockPos pos = ctx.getClickedPos();
      ItemStack stack = ctx.getItemInHand();
      Level level = ctx.getLevel();
      boolean ball = level.getBlockState(pos).getBlock()
         == com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.divination_crystal.get();
      if (ball) {
         setBallPos(stack, pos.getX(), pos.getY(), pos.getZ());
      } else {
         setBoundPos(stack, pos.getX(), pos.getY(), pos.getZ());
      }
      if (!level.isClientSide) {
         level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_BELL.value(),
            SoundSource.PLAYERS, 0.7F, 1.6F);
         player.displayClientMessage(
            net.minecraft.network.chat.Component.translatable(
               ball ? "message.som.crystal_ball_bound" : "message.som.mirror_bound",
               pos.getX(), pos.getY(), pos.getZ()), true);
      }
      return InteractionResult.sidedSuccess(level.isClientSide);
   }

   @Override
   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.NONE;
   }

   @Override
   public int getUseDuration(ItemStack stack) {
      return hasBoundPos(stack) ? 72000 : CHANNEL_TICKS;
   }

   @Override
   public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (hand != InteractionHand.MAIN_HAND) {
         return InteractionResultHolder.pass(stack);
      }
      if (player.isShiftKeyDown()) {
         if (level.isClientSide) {
            net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
               net.minecraftforge.api.distmarker.Dist.CLIENT,
               () -> () -> com.paleimitations.schoolsofmagic.client.guis.GuiMagicMirror.open(stack));
         }
         return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
      }
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null) {
         return InteractionResultHolder.pass(stack);
      }
      if (isBallBound(stack)) {
         if (mana.getMana() < BALL_COST) {
            if (!level.isClientSide) {
               level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH,
                  SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return InteractionResultHolder.fail(stack);
         }
         if (level.isClientSide) {
            com.paleimitations.schoolsofmagic.SchoolsOfMagic.proxy.openCrystalBall(player);
         } else {
            mana.setMana(mana.getMana() - BALL_COST);
            level.playSound(null, player.blockPosition(), SoundEvents.NOTE_BLOCK_BELL.value(),
               SoundSource.PLAYERS, 0.5F, 1.9F);
         }
         return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
      }
      player.startUsingItem(hand);
      return InteractionResultHolder.consume(stack);
   }

   @Override
   public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
      if (level.isClientSide) {
         if (this.getUseDuration(stack) - remaining == 1 && entity instanceof Player player) {
            net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
               net.minecraftforge.api.distmarker.Dist.CLIENT,
               () -> () -> com.paleimitations.schoolsofmagic.client.ClientMirrorSound.start(player));
         }
         return;
      }
      if (!(level instanceof ServerLevel server)) {
         return;
      }
      entity.addEffect(new net.minecraft.world.effect.MobEffectInstance(
         net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, 5, 2, false, false, false));
      int elapsed = this.getUseDuration(stack) - remaining;
      if (hasBoundPos(stack) && elapsed >= CHANNEL_TICKS && entity instanceof Player scryer) {
         if (elapsed % 4 == 0) {
            IManaData mana = scryer.getCapability(CapabilityManaData.CAP).orElse(null);
            if (mana == null || mana.getMana() < 1.0F) {
               scryer.stopUsingItem();
               return;
            }
            mana.setMana(mana.getMana() - 1.0F);
         }
      }
      net.minecraft.core.particles.SimpleParticleType sparkle =
         com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.SPARKLE_STAR.get();
      double baseY = entity.getY() + 0.05D;
      for (int i = 0; i < RING_POINTS; i++) {
         double a = (Math.PI * 2.0D / RING_POINTS) * i;
         double x = entity.getX() + Math.cos(a) * RING_RADIUS;
         double z = entity.getZ() + Math.sin(a) * RING_RADIUS;
         if (elapsed % 3 == 0) {
            server.sendParticles(sparkle, x, baseY, z, 0, 1.0D, 1.0D, 1.0D, 1.0D);
         }
         int phase = (elapsed + i * 3) % 24;
         if (phase < 10) {
            server.sendParticles(sparkle, x, baseY + phase * 0.18D, z, 0, 1.0D, 1.0D, 1.0D, 1.0D);
         }
      }
   }

   @Override
   public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
      if (level.isClientSide || !(entity instanceof ServerPlayer player)) {
         return stack;
      }
      if (hasBoundPos(stack)) {
         return stack;
      }
      IManaData mana = player.getCapability(CapabilityManaData.CAP).orElse(null);
      if (mana == null || mana.getMana() < MANA_COST) {
         level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH,
            SoundSource.PLAYERS, 1.0F, 1.0F);
         return stack;
      }
      if (!recall(player)) {
         level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH,
            SoundSource.PLAYERS, 1.0F, 1.0F);
         return stack;
      }
      mana.setMana(mana.getMana() - MANA_COST);
      return stack;
   }

   private static boolean recall(ServerPlayer player) {
      BlockPos bed = player.getRespawnPosition();
      ServerLevel target = player.server.getLevel(player.getRespawnDimension());
      if (bed != null && target != null) {
         Optional<Vec3> found = Player.findRespawnPositionAndUseSpawnBlock(
            target, bed, player.getRespawnAngle(), player.isRespawnForced(), false);
         if (found.isPresent()) {
            Vec3 at = found.get();
            player.teleportTo(target, at.x, at.y, at.z, player.getYRot(), player.getXRot());
            return true;
         }
      }
      ServerLevel overworld = player.server.overworld();
      BlockPos spawn = overworld.getSharedSpawnPos();
      player.teleportTo(overworld, spawn.getX() + 0.5D, spawn.getY(), spawn.getZ() + 0.5D,
         player.getYRot(), player.getXRot());
      return true;
   }
}
