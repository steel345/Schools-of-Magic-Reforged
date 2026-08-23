package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityThunderBird;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ThunderBirdEvents {
   private static final int MAX_NEARBY = 3;

   @SubscribeEvent
   public static void onLightningStrike(EntityStruckByLightningEvent event) {
      if (!(event.getEntity() instanceof Chicken chicken)) {
         return;
      }
      if (!(chicken.level() instanceof ServerLevel server)) {
         return;
      }
      event.setCanceled(true);
      transformChicken(server, chicken, false);
   }

   @SubscribeEvent
   public static void onTridentHit(LivingAttackEvent event) {
      if (!(event.getEntity() instanceof Chicken chicken)) {
         return;
      }
      if (!(chicken.level() instanceof ServerLevel server)) {
         return;
      }
      if (!(event.getSource().getDirectEntity() instanceof ThrownTrident trident) || !isChanneling(trident)) {
         return;
      }
      event.setCanceled(true);
      transformChicken(server, chicken, true);
   }

   private static void transformChicken(ServerLevel server, Chicken chicken, boolean spawnVisualBolt) {
      EntityThunderBird bird = EntityRegistry.THUNDER_BIRD.get().create(server);
      if (bird == null) {
         return;
      }
      bird.moveTo(chicken.getX(), chicken.getY(), chicken.getZ(), chicken.getYRot(), chicken.getXRot());
      bird.setPersistenceRequired();
      bird.finalizeSpawn(server, server.getCurrentDifficultyAt(bird.blockPosition()), MobSpawnType.CONVERSION, null, null);
      if (chicken.hasCustomName()) {
         bird.setCustomName(chicken.getCustomName());
         bird.setCustomNameVisible(chicken.isCustomNameVisible());
      }
      if (spawnVisualBolt) {
         LightningBolt vfx = EntityType.LIGHTNING_BOLT.create(server);
         if (vfx != null) {
            vfx.moveTo(chicken.getX(), chicken.getY(), chicken.getZ());
            vfx.setVisualOnly(true);
            server.addFreshEntity(vfx);
         }
      }
      server.addFreshEntity(bird);
      server.sendParticles(ParticleTypes.CLOUD, chicken.getX(), chicken.getY() + 0.5D, chicken.getZ(), 25, 0.4D, 0.5D, 0.4D, 0.05D);
      server.playSound(null, chicken.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.NEUTRAL, 1.0F, 1.2F);
      chicken.discard();
   }

   private static boolean isChanneling(ThrownTrident trident) {
      try {
         for (Field field : ThrownTrident.class.getDeclaredFields()) {
            if (ItemStack.class.isAssignableFrom(field.getType())) {
               field.setAccessible(true);
               if (field.get(trident) instanceof ItemStack stack && !stack.isEmpty()) {
                  return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.CHANNELING, stack) > 0;
               }
            }
         }
      } catch (Exception ignored) {
      }
      return false;
   }

   @SubscribeEvent
   public static void onLevelTick(TickEvent.LevelTickEvent event) {
      if (event.phase != TickEvent.Phase.END || !(event.level instanceof ServerLevel server)) {
         return;
      }
      if (!server.isThundering() || server.random.nextInt(160) != 0) {
         return;
      }
      List<? extends Player> players = server.players();
      if (players.isEmpty()) {
         return;
      }
      Player player = players.get(server.random.nextInt(players.size()));
      List<EntityThunderBird> nearby = server.getEntitiesOfClass(EntityThunderBird.class, player.getBoundingBox().inflate(96.0D));
      long stormCount = nearby.stream().filter(EntityThunderBird::isStormSpawned).count();
      if (stormCount >= MAX_NEARBY) {
         return;
      }
      double angle = server.random.nextDouble() * Math.PI * 2.0D;
      double dist = 20.0D + server.random.nextInt(30);
      double x = player.getX() + Math.cos(angle) * dist;
      double z = player.getZ() + Math.sin(angle) * dist;
      int surface = server.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) Math.floor(x), (int) Math.floor(z));
      double y = Math.max(player.getY(), surface) + 30.0D + server.random.nextInt(20);
      double ceiling = server.getMaxBuildHeight() - 4.0D;
      if (y > ceiling) {
         y = ceiling;
      }
      EntityThunderBird bird = EntityRegistry.THUNDER_BIRD.get().create(server);
      if (bird == null) {
         return;
      }
      bird.moveTo(x, y, z, server.random.nextFloat() * 360.0F, 0.0F);
      bird.setStormSpawned(true);
      bird.finalizeSpawn(server, server.getCurrentDifficultyAt(bird.blockPosition()), MobSpawnType.EVENT, null, null);
      server.addFreshEntity(bird);
   }
}
