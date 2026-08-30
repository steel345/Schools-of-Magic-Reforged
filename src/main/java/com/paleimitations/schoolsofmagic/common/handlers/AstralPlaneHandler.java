package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.EntityRift;
import com.paleimitations.schoolsofmagic.common.entity.EntityRiftItem;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.CapabilityRiftStorage;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.rift_storage.IRiftStorage;
import com.paleimitations.schoolsofmagic.common.network.PacketAstralSwell;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.registries.DimensionRegistry;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.world.dimensions.AstralCorridorGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

// standing on a rift long enough pulls you through it. the way back is the rift that turns up
// behind you on the other side
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class AstralPlaneHandler {
   public static final int COUNT_FROM = 4;
   public static final int SECOND = 20;
   public static final int SETTLE = 80;

   private static final String TAG = "SomAstralReturn";
   private static final int LANE = 256;
   private static final int GRACE = 40;

   private static final Map<UUID, Integer> standing = new HashMap<>();
   private static final Map<UUID, Integer> grace = new HashMap<>();
   private static final java.util.Set<UUID> seen = new java.util.HashSet<>();

   private static long frozenDay = -1L;

   // called from the rift itself, it is the one that knows who is standing in it. every second
   // held is one number off the count
   public static void standingOn(EntityRift rift, ServerPlayer player) {
      UUID id = player.getUUID();
      boolean home = rift.isHomeward();
      if (!home && !rift.isOwner(player)) return;
      if (home) {
         Integer wait = grace.get(id);
         if (wait != null && wait > 0) return;
      }

      // holding it open takes sneaking the whole way through. let go of shift and the count is
      // dropped by the same path as stepping out of it
      if (!player.isShiftKeyDown()) return;

      seen.add(id);
      int held = standing.merge(id, 1, Integer::sum);

      if (held <= SETTLE) return;
      int counting = held - SETTLE;

      if (counting % SECOND == 1) {
         int left = COUNT_FROM - (counting / SECOND);
         if (left > 0) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
               new PacketAstralSwell(left));
            float pitch = 0.8F + (COUNT_FROM - left) * 0.22F;
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
               SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.PLAYERS, 0.7F, pitch);
         }
      }

      if (counting >= COUNT_FROM * SECOND) {
         standing.remove(id);
         seen.remove(id);
         clear(player);
         player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.PORTAL_TRIGGER, SoundSource.PLAYERS, 0.8F, 1.6F);
         if (home) leave(player); else enter(player);
      }
   }

   private static void clear(ServerPlayer player) {
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new PacketAstralSwell(0));
   }

   @SubscribeEvent
   public static void onServerTick(TickEvent.ServerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
      if (server == null) return;

      // stepping out of the rift stops the count where it stands
      for (UUID id : new java.util.ArrayList<>(standing.keySet())) {
         if (seen.contains(id)) continue;
         standing.remove(id);
         ServerPlayer quitter = server.getPlayerList().getPlayer(id);
         if (quitter != null) clear(quitter);
      }
      seen.clear();

      grace.replaceAll((id, left) -> left - 1);
      grace.values().removeIf(left -> left <= 0);

      holdTime(server);
   }

   private static boolean anyoneInside(MinecraftServer server) {
      for (ServerPlayer player : server.getPlayerList().getPlayers()) {
         if (player.level().dimension().equals(DimensionRegistry.ASTRAL_PLANE_RIFT)) return true;
      }
      return false;
   }

   // in a single player world nothing outside should move on while you are in there. with company
   // about it would be freezing everyone elses world too, so it is left alone
   private static void holdTime(MinecraftServer server) {
      boolean alone = server.isSingleplayer() && server.getPlayerList().getPlayerCount() <= 1;
      if (!alone || !anyoneInside(server)) {
         frozenDay = -1L;
         return;
      }

      ServerLevel overworld = server.overworld();
      if (frozenDay < 0L) frozenDay = overworld.getDayTime();
      for (ServerLevel level : server.getAllLevels()) {
         if (level.dimension().equals(DimensionRegistry.ASTRAL_PLANE_RIFT)) continue;
         level.setDayTime(frozenDay);
      }
   }

   private static long lane(ServerPlayer player) {
      return Math.floorMod(player.getUUID().getMostSignificantBits(), 64L) * LANE;
   }

   private static void enter(ServerPlayer player) {
      MinecraftServer server = player.getServer();
      if (server == null) return;
      ServerLevel astral = server.getLevel(DimensionRegistry.ASTRAL_PLANE_RIFT);
      if (astral == null) return;

      CompoundTag tag = new CompoundTag();
      tag.putString("Dim", player.level().dimension().location().toString());
      tag.putDouble("X", player.getX());
      tag.putDouble("Y", player.getY());
      tag.putDouble("Z", player.getZ());
      tag.putFloat("Yaw", player.getYRot());
      tag.putFloat("Pitch", player.getXRot());
      player.getPersistentData().put(TAG, tag);

      double z = lane(player) + 0.5D;
      double x = (AstralCorridorGenerator.WEST_WALL + AstralCorridorGenerator.EAST_WALL) * 0.5D + 0.5D;
      double y = AstralCorridorGenerator.FLOOR + 1;

      player.changeDimension(astral, new Fixed(new Vec3(x, y, z), 0.0F, 0.0F));
      player.setDeltaMovement(Vec3.ZERO);
      player.fallDistance = 0.0F;

      EntityRift back = EntityRegistry.RIFT.get().create(astral);
      if (back != null) {
         back.moveTo(x, y + 0.6D, z - 2.0D, 0.0F, 0.0F);
         back.setLife(Integer.MAX_VALUE);
         back.setOwner(player);
         back.setHomeward(true);
         astral.addFreshEntity(back);
      }
      grace.put(player.getUUID(), GRACE);
      scatter(player, astral, x, y, z);
   }

   // everything in the rift gets laid out down the corridor. the run is measured off how much
   // there is, so a handful stays by the way in and a hoard fills the hall rather than piling up
   // in one heap at the door
   private static void scatter(ServerPlayer player, ServerLevel astral, double x, double y, double z) {
      IRiftStorage storage = CapabilityRiftStorage.get(player);
      if (storage == null) return;

      java.util.List<Integer> filled = new java.util.ArrayList<>();
      for (int i = 0; i < storage.size(); i++) {
         if (!storage.get(i).isEmpty()) filled.add(i);
      }
      if (filled.isEmpty()) return;

      int count = filled.size();
      double run = Math.max(7.0D, count * 0.85D);
      double slot = run / count;
      RandomSource random = RandomSource.create(player.getUUID().getMostSignificantBits() ^ count);

      double west = AstralCorridorGenerator.WEST_WALL + 1 + 0.45D;
      double wide = AstralCorridorGenerator.EAST_WALL - 0.45D - west;

      for (int i = 0; i < count; i++) {
         // one to a slot down the length of the run, jittered inside it. handing them all a free
         // random z would clump some together and leave gaps elsewhere
         double along = 2.0D + (i + 0.5D) * slot + (random.nextDouble() - 0.5D) * slot * 0.9D;
         double spotX = west + random.nextDouble() * wide;
         double spotZ = z + along;

         EntityRiftItem lying = EntityRegistry.RIFT_ITEM.get().create(astral);
         if (lying == null) continue;
         lying.moveTo(spotX, y + 0.01D, spotZ, 0.0F, 0.0F);
         lying.setup(player, filled.get(i), storage.get(filled.get(i)), random.nextFloat() * 360.0F);
         astral.addFreshEntity(lying);
      }
   }

   private static void sweep(ServerLevel astral, ServerPlayer player, double z) {
      double x = (AstralCorridorGenerator.WEST_WALL + AstralCorridorGenerator.EAST_WALL) * 0.5D;
      net.minecraft.world.phys.AABB reach =
         new net.minecraft.world.phys.AABB(x - 16.0D, AstralCorridorGenerator.FLOOR - 8.0D, z - 400.0D,
                                           x + 16.0D, AstralCorridorGenerator.FLOOR + 24.0D, z + 400.0D);
      for (EntityRiftItem lying : astral.getEntitiesOfClass(EntityRiftItem.class, reach)) {
         if (lying.owner() == null || lying.owner().equals(player.getUUID())) lying.discard();
      }
   }

   private static void leave(ServerPlayer player) {
      MinecraftServer server = player.getServer();
      if (server == null) return;

      CompoundTag tag = player.getPersistentData().getCompound(TAG);
      ResourceKey<Level> key = tag.contains("Dim")
         ? ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
            new ResourceLocation(tag.getString("Dim")))
         : Level.OVERWORLD;

      ServerLevel home = server.getLevel(key);
      if (home == null) home = server.overworld();

      Vec3 spot = tag.contains("X")
         ? new Vec3(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"))
         : Vec3.atBottomCenterOf(home.getSharedSpawnPos());

      player.changeDimension(home, new Fixed(spot, tag.getFloat("Yaw"), tag.getFloat("Pitch")));
      player.setDeltaMovement(Vec3.ZERO);
      player.fallDistance = 0.0F;
      player.getPersistentData().remove(TAG);

      ServerLevel astral = server.getLevel(DimensionRegistry.ASTRAL_PLANE_RIFT);
      if (astral != null) {
         sweep(astral, player, lane(player));
         BlockPos at = BlockPos.containing(
            (AstralCorridorGenerator.WEST_WALL + AstralCorridorGenerator.EAST_WALL) * 0.5D,
            AstralCorridorGenerator.FLOOR + 1, lane(player));
         for (EntityRift rift : astral.getEntitiesOfClass(EntityRift.class,
               new net.minecraft.world.phys.AABB(at).inflate(8.0D))) {
            if (rift.isHomeward() && rift.isOwner(player)) rift.discard();
         }
      }
   }

   private record Fixed(Vec3 spot, float yaw, float pitch) implements ITeleporter {
      @Override
      public PortalInfo getPortalInfo(Entity entity, ServerLevel dest,
                                      java.util.function.Function<ServerLevel, PortalInfo> fallback) {
         return new PortalInfo(this.spot, Vec3.ZERO, this.yaw, this.pitch);
      }

      @Override
      public boolean playTeleportSound(ServerPlayer player, ServerLevel from, ServerLevel to) {
         return false;
      }
   }
}
