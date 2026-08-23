package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class IceShell {
   public static final float BREAK_THRESHOLD = 50.0F;
   private static final int FROZEN_TICKS = 200;
   private static final double AGGRO_RANGE = 16.0D;

   private static class Shell {
      int ticksLeft;
      float damage;
      boolean unbreakable;
      Vec3 anchor;
      final List<BlockPos> placed = new ArrayList<>();
   }

   private static final Map<UUID, Shell> ACTIVE = new HashMap<>();
   private static final java.util.Set<BlockPos> SHELL_BLOCKS = new java.util.HashSet<>();

   public static boolean isShellBlock(BlockPos pos) {
      return SHELL_BLOCKS.contains(pos);
   }

   private static final java.util.Set<UUID> CLIENT_ACTIVE = new java.util.HashSet<>();

   public static void setClientActive(UUID id, boolean on) {
      if (on) CLIENT_ACTIVE.add(id);
      else CLIENT_ACTIVE.remove(id);
   }

   public static boolean isActive(Player player) {
      if (player == null) return false;
      if (player.level().isClientSide) return CLIENT_ACTIVE.contains(player.getUUID());
      return ACTIVE.containsKey(player.getUUID());
   }

   public static void begin(Player player, int ticks, boolean unbreakable) {
      if (player.level().isClientSide) return;
      end(player, false);

      Shell shell = new Shell();
      shell.ticksLeft = ticks;
      shell.unbreakable = unbreakable;
      shell.anchor = player.position();
      raise(player.level(), player.blockPosition(), shell);
      ACTIVE.put(player.getUUID(), shell);

      player.setTicksFrozen(FROZEN_TICKS);
      Level level = player.level();
      BlockPos at = player.blockPosition();
      level.playSound(null, at, SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 2.0F, 0.4F);
      level.playSound(null, at, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.8F, 0.6F);
      level.playSound(null, at, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1.5F, 0.7F);
      level.playSound(null, at, SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1.4F, 1.7F);
      if (player instanceof ServerPlayer sp) {
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> sp),
            new com.paleimitations.schoolsofmagic.common.network.PacketIceShell(true));
      }
   }

   private static void raise(Level level, BlockPos base, Shell shell) {
      BlockState ice = Blocks.ICE.defaultBlockState();
      for (int dy = 0; dy <= 1; dy++) {
         for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
               if (dx == 0 && dz == 0) continue;
               place(level, base.offset(dx, dy, dz), ice, shell);
            }
         }
      }
      place(level, base.above(2), ice, shell);
   }

   private static void place(Level level, BlockPos pos, BlockState ice, Shell shell) {
      BlockState there = level.getBlockState(pos);
      if (!there.isAir() && !there.canBeReplaced()) return;
      level.setBlock(pos, ice, 3);
      BlockPos fixed = pos.immutable();
      shell.placed.add(fixed);
      SHELL_BLOCKS.add(fixed);
   }

   public static void end(Player player, boolean shatter) {
      Shell shell = ACTIVE.remove(player.getUUID());
      if (shell == null) return;
      Level level = player.level();
      for (BlockPos pos : shell.placed) {
         SHELL_BLOCKS.remove(pos);
         if (level.getBlockState(pos).is(Blocks.ICE)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
         }
      }
      player.setTicksFrozen(0);
      if (shatter && level instanceof ServerLevel server) {
         server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ICE.defaultBlockState()),
            player.getX(), player.getY() + 1.0D, player.getZ(), 80, 0.8D, 1.0D, 0.8D, 0.2D);
         level.playSound(null, player.blockPosition(), SoundEvents.GLASS_BREAK,
            SoundSource.PLAYERS, 1.2F, 0.7F);
      }
      if (player instanceof ServerPlayer sp) {
         com.paleimitations.schoolsofmagic.common.network.PacketHandler.INSTANCE.send(
            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> sp),
            new com.paleimitations.schoolsofmagic.common.network.PacketIceShell(false));
      }
   }

   @SubscribeEvent
   public static void onAttack(LivingAttackEvent event) {
      if (!(event.getEntity() instanceof Player player)) return;
      Shell shell = ACTIVE.get(player.getUUID());
      if (shell == null) return;

      event.setCanceled(true);
      if (shell.unbreakable) return;
      shell.damage += event.getAmount();
      if (shell.damage >= BREAK_THRESHOLD) {
         end(player, true);
      }
   }

   private static void keepAggro(Player player) {
      net.minecraft.world.phys.AABB near = player.getBoundingBox().inflate(AGGRO_RANGE);
      for (net.minecraft.world.entity.Mob mob
            : player.level().getEntitiesOfClass(net.minecraft.world.entity.Mob.class, near)) {
         if (!(mob instanceof net.minecraft.world.entity.monster.Enemy)) continue;
         if (mob.getTarget() == null || mob.getTarget() == player) {
            mob.setTarget(player);
         }
      }
   }

   @SubscribeEvent
   public static void onBreak(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
      if (SHELL_BLOCKS.contains(event.getPos())) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onExplosion(net.minecraftforge.event.level.ExplosionEvent.Detonate event) {
      event.getAffectedBlocks().removeIf(SHELL_BLOCKS::contains);
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
      Player player = event.player;
      Shell shell = ACTIVE.get(player.getUUID());
      if (shell == null) return;

      if (--shell.ticksLeft <= 0) {
         end(player, false);
         return;
      }
      keepAggro(player);
      player.setTicksFrozen(FROZEN_TICKS);
      player.setDeltaMovement(Vec3.ZERO);
      player.fallDistance = 0.0F;
      if (player.position().distanceToSqr(shell.anchor) > 0.02D) {
         player.teleportTo(shell.anchor.x, shell.anchor.y, shell.anchor.z);
      }
   }
}
