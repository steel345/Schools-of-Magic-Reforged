package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class LevitationHold {
   private static final double LIFT_SPEED = 0.10D;
   private static final double MAX_LIFT = 7.0D;
   private static final double CONTROL_HEIGHT = 1.0D;
   private static final double FREE_HEIGHT = 3.0D;
   private static final double MOVE_SPEED = 0.13D;
   private static final double AGGRESSIVE = 0.9D;
   private static final double LOSE_RANGE = 14.0D;
   private static final String GLOW_TEAM = "som_levitate";
   private static final double GLOW_R = 0.58D;
   private static final double GLOW_G = 0.20D;
   private static final double GLOW_B = 0.92D;

   private static final class Hold {
      int targetId;
      double groundY;
      double distance;
      Vec3 lastAnchor;
      boolean self;
      boolean atCap;
      long lastTick;
   }

   private static final Map<UUID, Hold> HOLDS = new HashMap<>();

   public static boolean isHolding(Player player) {
      return !player.level().isClientSide && HOLDS.containsKey(player.getUUID());
   }

   public static void release(Player player) {
      if (player.level().isClientSide) return;
      Hold hold = HOLDS.remove(player.getUUID());
      if (hold == null) return;
      Entity target = hold.self ? player : player.level().getEntity(hold.targetId);
      if (target != null) {
         target.setNoGravity(false);
         target.hasImpulse = true;
         glow(target, false);
      }
      player.removeEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
   }

   private static void drop(Player player, Entity target) {
      if (target != null) {
         target.setNoGravity(false);
         target.hasImpulse = true;
         glow(target, false);
      }
      player.removeEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
      HOLDS.remove(player.getUUID());
   }

   public static boolean grab(Player player, Entity target, boolean self) {
      if (player.level().isClientSide) return true;
      Entity held = self ? player : target;
      if (held == null) return false;

      Hold hold = new Hold();
      hold.self = self;
      hold.targetId = held.getId();
      hold.groundY = held.getY();
      hold.distance = self ? 0.0D : Math.max(2.5D, Math.min(7.0D, player.distanceTo(held)));
      hold.lastAnchor = anchor(player, hold);
      hold.lastTick = player.level().getGameTime();
      HOLDS.put(player.getUUID(), hold);
      glow(held, true);
      return true;
   }

   public static boolean tick(Player player) {
      Level level = player.level();
      if (level.isClientSide) return true;
      Hold hold = HOLDS.get(player.getUUID());
      if (hold == null) return false;

      hold.lastTick = level.getGameTime();
      Entity target = hold.self ? player : level.getEntity(hold.targetId);
      if (target == null || !target.isAlive()
            || (!hold.self && player.distanceTo(target) > LOSE_RANGE)) {
         drop(player, target);
         return false;
      }

      double risen = target.getY() - hold.groundY;
      if (!hold.self) {
         target.setNoGravity(true);
      }
      target.fallDistance = 0.0F;

      if (hold.self) {
         if (player.isSprinting()) {
            drop(player, target);
            return false;
         }
         if (risen >= MAX_LIFT) {
            hold.atCap = true;
         }
         if (!hold.atCap) {
            if (target.isNoGravity()) {
               target.setNoGravity(false);
            }
            net.minecraft.world.effect.MobEffectInstance rising =
               player.getEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            if (rising == null || rising.getDuration() < 10) {
               player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                  net.minecraft.world.effect.MobEffects.LEVITATION, 60, 0, false, false, false));
            }
         } else {
            if (player.hasEffect(net.minecraft.world.effect.MobEffects.LEVITATION)) {
               player.removeEffect(net.minecraft.world.effect.MobEffects.LEVITATION);
            }
            if (!target.isNoGravity()) {
               target.setNoGravity(true);
            }
         }
      } else {
         Vec3 want = anchor(player, hold);
         if (hold.lastAnchor != null && risen >= CONTROL_HEIGHT
               && want.subtract(hold.lastAnchor).horizontalDistance() > AGGRESSIVE) {
            drop(player, target);
            return false;
         }
         hold.lastAnchor = want;

         double up = risen < MAX_LIFT ? LIFT_SPEED : 0.0D;
         double dx = 0.0D;
         double dz = 0.0D;
         if (risen >= CONTROL_HEIGHT) {
            Vec3 toward = new Vec3(want.x - target.getX(), 0.0D, want.z - target.getZ());
            if (toward.lengthSqr() > 1.0E-4D) {
               double ease = Math.min(1.0D, Math.max(0.25D, (risen - CONTROL_HEIGHT) / (FREE_HEIGHT - CONTROL_HEIGHT)));
               double cap = MOVE_SPEED * ease;
               Vec3 step = toward.normalize().scale(Math.min(cap, toward.length()));
               dx = step.x;
               dz = step.z;
            }
         }
         target.setDeltaMovement(dx, up, dz);
      }
      target.hasImpulse = true;

      if (level.getGameTime() % 45L == 0L) {
         level.playSound(null, target.blockPosition(), SoundEvents.BEACON_AMBIENT,
            SoundSource.PLAYERS, 0.45F, 1.6F);
      }
      if (level instanceof ServerLevel server && level.getGameTime() % 3L == 0L) {
         for (int i = 0; i < 4; i++) {
            server.sendParticles(
               com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.SPARKLE_STAR.get(),
               target.getX() + (server.getRandom().nextDouble() - 0.5D) * 0.9D,
               target.getY() + target.getBbHeight() * (0.2D + server.getRandom().nextDouble() * 0.8D),
               target.getZ() + (server.getRandom().nextDouble() - 0.5D) * 0.9D,
               0, GLOW_R, GLOW_G, GLOW_B, 1.0D);
         }
      }
      return true;
   }

   private static void glow(Entity entity, boolean on) {
      if (entity.level().isClientSide) return;
      net.minecraft.world.scores.Scoreboard board = entity.level().getScoreboard();
      net.minecraft.world.scores.PlayerTeam team = board.getPlayerTeam(GLOW_TEAM);
      if (team == null) {
         team = board.addPlayerTeam(GLOW_TEAM);
         team.setColor(net.minecraft.ChatFormatting.BLUE);
         team.setSeeFriendlyInvisibles(false);
      }
      String name = entity.getScoreboardName();
      if (on) {
         board.addPlayerToTeam(name, team);
         entity.setGlowingTag(true);
      } else {
         entity.setGlowingTag(false);
         if (team.getPlayers().contains(name)) {
            board.removePlayerFromTeam(name, team);
         }
      }
   }

   private static Vec3 anchor(Player player, Hold hold) {
      if (hold.self) return player.position();
      Vec3 look = player.getViewVector(1.0F);
      return player.getEyePosition().add(look.x * hold.distance, 0.0D, look.z * hold.distance);
   }

   @SubscribeEvent
   public static void onStopUsing(LivingEntityUseItemEvent.Stop event) {
      if (event.getEntity() instanceof Player player) release(player);
   }

   @SubscribeEvent
   public static void onFinishUsing(LivingEntityUseItemEvent.Finish event) {
      if (event.getEntity() instanceof Player player) release(player);
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Player player = event.player;
      if (player.level().isClientSide) return;
      Hold hold = HOLDS.get(player.getUUID());
      if (hold != null && player.level().getGameTime() - hold.lastTick > 5L) {
         release(player);
      }
   }
}
