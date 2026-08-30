package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketGaseousForm;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class GaseousFormHandler {
   public static final float FLY_SPEED = 0.05F;
   private static final int DROWN_INTERVAL = 20;
   private static final float DROWN_DAMAGE = 2.0F;
   private static final String GAS_TEAM = "som_gaseous";

   private static final class Gas {
      int ticks;
      int max;
      int drownTimer;
      boolean caster;
      UUID owner;
   }

   private static final Map<UUID, Gas> ACTIVE = new HashMap<>();
   private static final Map<UUID, int[]> PENDING = new HashMap<>();
   private static final Map<Integer, int[]> CLIENT = new HashMap<>();

   private static boolean selfHarm;
   private static float localBar = 1.0F;

   // vanilla has no per entity collision switch, a team with the rule turned off is how it does it
   private static void solid(Entity entity, boolean on) {
      net.minecraft.world.scores.Scoreboard board = entity.level().getScoreboard();
      net.minecraft.world.scores.PlayerTeam team = board.getPlayerTeam(GAS_TEAM);
      if (team == null) {
         team = board.addPlayerTeam(GAS_TEAM);
         team.setCollisionRule(net.minecraft.world.scores.Team.CollisionRule.NEVER);
         team.setSeeFriendlyInvisibles(false);
      }
      String name = entity.getScoreboardName();
      if (on) {
         if (team.getPlayers().contains(name)) board.removePlayerFromTeam(name, team);
      } else {
         board.addPlayerToTeam(name, team);
      }
   }

   private static void hide(LivingEntity living, int ticks) {
      living.addEffect(new net.minecraft.world.effect.MobEffectInstance(
         net.minecraft.world.effect.MobEffects.INVISIBILITY, ticks + 40, 0, false, false, false));
      living.setInvisible(true);
   }

   public static void schedule(ServerPlayer player, int ticks, int delay, int targetId) {
      PENDING.put(player.getUUID(), new int[]{delay, ticks, targetId});
   }

   public static void begin(LivingEntity living, int ticks, boolean caster) {
      if (living.level().isClientSide) return;
      Gas gas = new Gas();
      gas.ticks = ticks;
      gas.max = ticks;
      gas.caster = caster;
      ACTIVE.put(living.getUUID(), gas);

      hide(living, ticks);
      solid(living, false);
      living.refreshDimensions();
      if (caster && living instanceof ServerPlayer player) {
         player.noPhysics = true;
         player.setNoGravity(true);
         player.setSprinting(false);
      }

      living.level().playSound(null, living.getX(), living.getY(), living.getZ(),
         SOMSoundHandler.CHIMES.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
      sync(living, ticks, ticks);
      bar(living, gas);
   }

   public static void end(LivingEntity living, boolean burn) {
      if (living.level().isClientSide) return;
      Gas gas = ACTIVE.remove(living.getUUID());
      if (gas == null) return;

      living.removeEffect(net.minecraft.world.effect.MobEffects.INVISIBILITY);
      living.setInvisible(false);
      living.setNoGravity(false);
      solid(living, true);
      living.refreshDimensions();
      if (gas.caster && living instanceof ServerPlayer player) {
         player.noPhysics = false;
      }

      if (burn) {
         living.clearFire();
         living.setSecondsOnFire(5);
      }
      sync(living, 0, 0);
      gas.ticks = 0;
      bar(living, gas);
   }

   public static boolean isGas(Entity entity) {
      if (entity == null) return false;
      if (entity.level().isClientSide) {
         int[] state = CLIENT.get(entity.getId());
         return state != null && state[0] > 0;
      }
      return ACTIVE.containsKey(entity.getUUID());
   }

   public static void tickClient() {
      for (int[] state : CLIENT.values()) {
         if (state[0] > 0) state[0]--;
      }
      CLIENT.values().removeIf(state -> state[0] <= 0);
   }

   public static void setClientState(int entityId, int ticks, int max) {
      if (ticks <= 0) CLIENT.remove(entityId);
      else CLIENT.put(entityId, new int[]{ticks, max});
   }

   public static void setLocalBar(float ratio) {
      localBar = ratio;
   }

   public static float localBar() {
      return localBar;
   }

   public static float clientRatio(Entity entity) {
      if (entity == null) return 1.0F;
      int[] state = CLIENT.get(entity.getId());
      if (state == null || state[1] <= 0) return 1.0F;
      return Math.max(0.0F, Math.min(1.0F, (float) state[0] / (float) state[1]));
   }

   // move skips checkInsideBlocks while phasing, so fire never touches you unless we look ourselves
   private static boolean fireAround(LivingEntity living) {
      net.minecraft.world.phys.AABB box = living.getBoundingBox().inflate(0.05D);
      for (net.minecraft.core.BlockPos pos : net.minecraft.core.BlockPos.betweenClosed(
            net.minecraft.core.BlockPos.containing(box.minX, box.minY, box.minZ),
            net.minecraft.core.BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
         net.minecraft.world.level.block.state.BlockState state = living.level().getBlockState(pos);
         if (state.getBlock() instanceof net.minecraft.world.level.block.BaseFireBlock) return true;
         if (state.getBlock() instanceof net.minecraft.world.level.block.CampfireBlock
               && state.getValue(net.minecraft.world.level.block.CampfireBlock.LIT)) return true;
         if (state.is(net.minecraft.world.level.block.Blocks.MAGMA_BLOCK)) return true;
      }
      return false;
   }

   private static void bar(LivingEntity living, Gas gas) {
      if (gas.owner == null || !(living.level().getServer() != null)) return;
      net.minecraft.server.level.ServerPlayer caster =
         living.level().getServer().getPlayerList().getPlayer(gas.owner);
      if (caster == null) return;
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> caster),
         new PacketGaseousForm(-1, gas.ticks, gas.max));
   }

   private static void sync(LivingEntity living, int ticks, int max) {
      if (living.level().isClientSide) return;
      PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> living),
         new PacketGaseousForm(living.getId(), ticks, max));
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (!(event.player instanceof ServerPlayer player)) return;

      int[] pending = PENDING.get(player.getUUID());
      if (pending == null) return;
      if (--pending[0] > 0) return;

      PENDING.remove(player.getUUID());
      LivingEntity victim = player;
      if (pending[2] >= 0 && player.level().getEntity(pending[2]) instanceof LivingEntity target) {
         victim = target;
      }
      begin(victim, pending[1], victim == player);
      if (victim != player) {
         Gas gas = ACTIVE.get(victim.getUUID());
         if (gas != null) gas.owner = player.getUUID();
      }
   }

   @SubscribeEvent
   public static void onLivingTick(LivingEvent.LivingTickEvent event) {
      LivingEntity living = event.getEntity();
      if (living.level().isClientSide) return;
      Gas gas = ACTIVE.get(living.getUUID());
      if (gas == null) return;

      if (--gas.ticks <= 0) {
         end(living, false);
         return;
      }

      if (gas.ticks % 40 == 0) hide(living, gas.ticks);
      if (gas.caster && living instanceof ServerPlayer player) {
         player.setSprinting(false);
         player.noPhysics = true;
         player.setNoGravity(true);
         player.fallDistance = 0.0F;
         player.setAirSupply(player.getMaxAirSupply());
         player.setOnGround(false);
         player.clearFire();

         if (player.isInWaterOrBubble() || player.isInLava()) {
            if (++gas.drownTimer >= DROWN_INTERVAL) {
               gas.drownTimer = 0;
               selfHarm = true;
               player.hurt(player.damageSources().inWall(), DROWN_DAMAGE);
               selfHarm = false;
            }
         } else {
            gas.drownTimer = 0;
         }
      }

      if (gas.ticks % 20 == 0) {
         sync(living, gas.ticks, gas.max);
         bar(living, gas);
      }
      if (fireAround(living)) {
         end(living, true);
      }
   }

   @SubscribeEvent
   public static void onAttack(LivingAttackEvent event) {
      if (event.getEntity().level().isClientSide) return;
      DamageSource source = event.getSource();
      if (source.getEntity() instanceof LivingEntity attacker && isCaught(attacker)) {
         event.setCanceled(true);
         return;
      }

      LivingEntity victim = event.getEntity();
      Gas gas = ACTIVE.get(victim.getUUID());
      if (gas == null || selfHarm) return;

      event.setCanceled(true);
      if (isFire(source)) {
         end(victim, true);
      }
   }

   private static boolean isCaught(LivingEntity living) {
      Gas gas = ACTIVE.get(living.getUUID());
      return gas != null && !gas.caster;
   }

   private static boolean fiery(Entity projectile) {
      if (projectile == null) return false;
      if (projectile instanceof com.paleimitations.schoolsofmagic.common.entity.projectile.EntityFireBall) return true;
      if (projectile instanceof net.minecraft.world.entity.projectile.Fireball) return true;
      return projectile.isOnFire();
   }

   private static boolean isFire(DamageSource source) {
      if (source.is(net.minecraft.world.damagesource.DamageTypes.LAVA)) return false;
      if (source.is(DamageTypeTags.IS_FIRE)) return true;
      return fiery(source.getDirectEntity()) || fiery(source.getEntity());
   }

   @SubscribeEvent
   public static void onProjectile(net.minecraftforge.event.entity.ProjectileImpactEvent event) {
      if (event.getProjectile().level().isClientSide) return;
      if (event.getProjectile().getOwner() instanceof LivingEntity owner && isCaught(owner)) {
         event.setCanceled(true);
         event.getProjectile().discard();
         return;
      }
      if (!(event.getRayTraceResult() instanceof net.minecraft.world.phys.EntityHitResult hit)) return;
      if (isGas(hit.getEntity()) && !fiery(event.getProjectile())) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onTarget(LivingChangeTargetEvent event) {
      if (event.getNewTarget() instanceof Player player && isGas(player)) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onSize(net.minecraftforge.event.entity.EntityEvent.Size event) {
      if (event.getEntity() instanceof Player player && isGas(player)) {
         event.setNewSize(net.minecraft.world.entity.EntityDimensions.scalable(0.6F, 0.6F));
         event.setNewEyeHeight(0.4F);
      }
   }

   @SubscribeEvent
   public static void onInteract(net.minecraftforge.event.entity.player.PlayerInteractEvent event) {
      if (!isGas(event.getEntity())) return;
      if (event.isCancelable()) event.setCanceled(true);
      if (event instanceof net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock click) {
         click.setUseBlock(net.minecraftforge.eventbus.api.Event.Result.DENY);
         click.setUseItem(net.minecraftforge.eventbus.api.Event.Result.DENY);
      }
   }

   @SubscribeEvent
   public static void onAttackEntity(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
      if (isGas(event.getEntity()) || isGas(event.getTarget())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onPickup(net.minecraftforge.event.entity.player.EntityItemPickupEvent event) {
      if (isGas(event.getEntity())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onUseItem(net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Start event) {
      if (isGas(event.getEntity())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onHeal(net.minecraftforge.event.entity.living.LivingHealEvent event) {
      if (isGas(event.getEntity())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onBreak(net.minecraftforge.event.level.BlockEvent.BreakEvent event) {
      if (isGas(event.getPlayer())) event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onPlace(net.minecraftforge.event.level.BlockEvent.EntityPlaceEvent event) {
      if (event.getEntity() instanceof Player player && isGas(player)) event.setCanceled(true);
   }
}
