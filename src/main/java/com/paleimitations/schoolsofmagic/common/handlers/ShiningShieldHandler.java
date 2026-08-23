package com.paleimitations.schoolsofmagic.common.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketShiningShield;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShiningShieldHandler {
   public static class Ring {
      public int shields;
      public int hitsLeft;
      public final int hitsEach;
      public int ticksLeft;

      Ring(int shields, int hitsEach, int ticks) {
         this.shields = shields;
         this.hitsEach = hitsEach;
         this.hitsLeft = hitsEach;
         this.ticksLeft = ticks;
      }
   }

   private static final Map<UUID, Ring> RINGS = new HashMap<>();

   public static void grant(Player player, int shields, int hitsEach, int ticks) {
      RINGS.put(player.getUUID(), new Ring(shields, hitsEach, ticks));
      sync(player);
   }

   public static int shieldsOf(UUID id) {
      Ring ring = RINGS.get(id);
      return ring == null ? 0 : ring.shields;
   }

   private static void sync(Player player) {
      if (player.level().isClientSide) return;
      PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
         new PacketShiningShield(player.getUUID(), shieldsOf(player.getUUID())));
   }

   @SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void onAttacked(LivingAttackEvent event) {
      if (!(event.getEntity() instanceof Player player)) return;
      if (player.level().isClientSide) return;
      Ring ring = RINGS.get(player.getUUID());
      if (ring == null || ring.shields <= 0) return;
      if (event.getSource().is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

      if (event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_FALL)) return;

      event.setCanceled(true);

      if (--ring.hitsLeft <= 0) {
         ring.shields--;
         ring.hitsLeft = ring.hitsEach;
      }

      if (event.getSource().getDirectEntity() instanceof net.minecraft.world.entity.projectile.Projectile shot) {
         reflect(shot, player);
      }

      if (event.getSource().getEntity() instanceof net.minecraft.world.entity.LivingEntity attacker) {
         double dx = attacker.getX() - player.getX();
         double dz = attacker.getZ() - player.getZ();
         double len = Math.max(0.01D, Math.sqrt(dx * dx + dz * dz));
         attacker.knockback(0.9D, -dx / len, -dz / len);
         attacker.hurtMarked = true;
      }

      player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
         SOMSoundHandler.SHIELD_HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
      sparks(player);

      if (ring.shields <= 0) {
         RINGS.remove(player.getUUID());
      }
      sync(player);
   }

   private static void reflect(net.minecraft.world.entity.projectile.Projectile shot, Player player) {
      net.minecraft.world.entity.Entity thrower = shot.getOwner();
      net.minecraft.world.phys.Vec3 back = shot.getDeltaMovement().scale(-1.0D);
      if (thrower != null) {
         net.minecraft.world.phys.Vec3 toThrower = thrower.getEyePosition()
            .subtract(shot.position()).normalize().scale(shot.getDeltaMovement().length());
         back = toThrower;
      }

      net.minecraft.world.entity.Entity copy = shot.getType().create(player.level());
      if (!(copy instanceof net.minecraft.world.entity.projectile.Projectile sent)) {
         shot.discard();
         return;
      }
      net.minecraft.nbt.CompoundTag data = new net.minecraft.nbt.CompoundTag();
      shot.saveWithoutId(data);
      sent.load(data);
      sent.setOwner(player);
      sent.setPos(shot.getX(), shot.getY(), shot.getZ());
      sent.setDeltaMovement(back);
      sent.hasImpulse = true;

      sent.setYRot((float) (Math.atan2(back.x, back.z) * (180.0D / Math.PI)));
      sent.setXRot((float) (Math.atan2(back.y,
         Math.sqrt(back.x * back.x + back.z * back.z)) * (180.0D / Math.PI)));

      shot.discard();
      player.level().addFreshEntity(sent);
   }

   private static void sparks(Player player) {
      if (!(player.level() instanceof ServerLevel sl)) return;
      sl.sendParticles(net.minecraft.core.particles.ParticleTypes.LAVA,
         player.getX(), player.getY() + 1.0D, player.getZ(), 6, 0.6D, 0.6D, 0.6D, 0.0D);
      sl.sendParticles(net.minecraft.core.particles.ParticleTypes.SMALL_FLAME,
         player.getX(), player.getY() + 1.0D, player.getZ(), 14, 0.7D, 0.7D, 0.7D, 0.02D);
   }

   @SubscribeEvent
   public static void onTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
      Ring ring = RINGS.get(event.player.getUUID());
      if (ring == null) return;
      if (--ring.ticksLeft <= 0) {
         RINGS.remove(event.player.getUUID());
         sync(event.player);
      }
   }

   @SubscribeEvent
   public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
      RINGS.remove(event.getEntity().getUUID());
   }
}
