package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.network.PacketScryTrail;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

// the mark left by a scry, whichever one made it. the server holds where it is, the client draws
// the way to it, and standing on it is what ends the spell
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class BiomeScryHandler {
   private static final int BIOME_RADIUS = 6400;
   private static final int BIOME_STEP = 32;
   private static final double BEAST_RADIUS = 160.0D;
   private static final double ARRIVED = 2.0D;

   public static final int ANIMANCY = 0x536729;
   public static final int AURAMANCY = 0xE0769C;

   private record Mark(BlockPos at, String spell, int rgb) {}

   private static final Map<UUID, Mark> marks = new HashMap<>();

   // the search walks a long way out, so it is worth saying when it comes back empty
   public static boolean scryBiome(ServerPlayer player, ResourceLocation biome) {
      ServerLevel level = player.serverLevel();
      var registry = level.registryAccess().registryOrThrow(Registries.BIOME);
      Biome found = registry.get(biome);
      if (found == null) return false;

      var hit = level.findClosestBiome3d(
         held -> held.value() == found, player.blockPosition(), BIOME_RADIUS, BIOME_STEP, 64);
      if (hit == null) {
         clear(player);
         return false;
      }
      return set(player, hit.getFirst(), "biome_scry", ANIMANCY);
   }

   // beasts only exist where the world is loaded, so this one cannot reach anything like as far
   public static boolean scryBeast(ServerPlayer player, ResourceLocation type) {
      EntityType<?> wanted = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getValue(type);
      if (wanted == null) return false;

      ServerLevel level = player.serverLevel();
      AABB around = player.getBoundingBox().inflate(BEAST_RADIUS);
      Entity closest = null;
      double best = Double.MAX_VALUE;

      for (Entity nearby : level.getEntities((Entity) null, around, e -> e.getType() == wanted && e.isAlive())) {
         double away = nearby.distanceToSqr(player);
         if (away < best) {
            best = away;
            closest = nearby;
         }
      }
      if (closest == null) {
         clear(player);
         return false;
      }
      return set(player, closest.blockPosition(), "animal_scry", AURAMANCY);
   }

   private static boolean set(ServerPlayer player, BlockPos at, String spell, int rgb) {
      marks.put(player.getUUID(), new Mark(at, spell, rgb));
      player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
         SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.9F, 1.5F);
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketScryTrail(at, true, rgb));
      return true;
   }

   public static void clear(ServerPlayer player) {
      marks.remove(player.getUUID());
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketScryTrail(BlockPos.ZERO, false, 0));
   }

   private static boolean holding(ServerPlayer player, String spell) {
      var mana = player.getCapability(
         com.paleimitations.schoolsofmagic.common.entity.capabilities.mana_data.CapabilityManaData.CAP).orElse(null);
      if (mana == null) return false;
      var held = mana.getCurrentSpell();
      return held != null && spell.equals(held.getName());
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server == null || marks.isEmpty()) return;

      marks.entrySet().removeIf(entry -> {
         ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
         if (player == null) return true;

         Mark mark = entry.getValue();

         // the trail belongs to the spell. put the spell away and it goes with it
         if (!holding(player, mark.spell())) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
               new PacketScryTrail(BlockPos.ZERO, false, 0));
            return true;
         }

         double flat = player.distanceToSqr(
            mark.at().getX() + 0.5D, player.getY(), mark.at().getZ() + 0.5D);
         if (flat > ARRIVED * ARRIVED) return false;

         // stood on it. the mark is spent
         player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.2F);
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
            new PacketScryTrail(BlockPos.ZERO, false, 0));
         return true;
      });
   }
}
