package com.paleimitations.schoolsofmagic.common.handlers;

import java.util.List;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketEclipseState;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.world.EclipseData;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EclipseHandler {
   public static final float MONSTER_DAMAGE_MULT = 1.5F;

   private static final int SPAWN_INTERVAL = 40;
   private static final int SPAWN_ATTEMPTS = 6;
   private static final int SPAWN_MIN_RANGE = 24;
   private static final int SPAWN_MAX_RANGE = 44;
   private static final int NEARBY_CAP = 12;

   public static boolean isEclipsed(Level level) {
      if (level == null) return false;
      if (level.isClientSide) {
         return com.paleimitations.schoolsofmagic.client.ClientEclipse.isRunning();
      }
      if (!(level instanceof ServerLevel server)) return false;
      return EclipseData.get(overworld(server)).isRunning();
   }

   private static ServerLevel overworld(ServerLevel any) {
      ServerLevel over = any.getServer().overworld();
      return over != null ? over : any;
   }

   @SubscribeEvent
   public static void onLevelTick(TickEvent.LevelTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (!(event.level instanceof ServerLevel level)) return;
      if (level != level.getServer().overworld()) return;

      EclipseData data = EclipseData.get(level);

      if (!data.isRunning()) {
         if (data.isDue(level) && EclipseData.isDaylight(level)) {
            data.begin(level);
            broadcast(level, data);
            announce(level);
         }
         return;
      }

      boolean stageChanged = data.tick(level);
      if (stageChanged || level.getGameTime() % 100L == 0L) {
         broadcast(level, data);
      }

      if (data.isRunning() && level.getGameTime() % 40L == 0L) {
         for (ServerPlayer player : level.players()) {
            if (level.canSeeSky(player.blockPosition())) {
               PageUnlockHandler.eclipseSeen(player);
            }
         }
      }
      if (data.isRunning() && level.getGameTime() % SPAWN_INTERVAL == 0L) {
         extraSpawns(level);
      }
   }

   private static void announce(ServerLevel level) {
      for (ServerPlayer player : level.players()) {
         player.displayClientMessage(
            net.minecraft.network.chat.Component.translatable("som.eclipse.begin")
               .withStyle(net.minecraft.ChatFormatting.DARK_PURPLE), true);
      }
   }

   public static void broadcast(ServerLevel level, EclipseData data) {
      PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension),
         new PacketEclipseState(data.isRunning(), data.getStage(), data.getElapsed()));
   }

   @SubscribeEvent
   public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
      if (!(event.getEntity() instanceof ServerPlayer player)) return;
      ServerLevel over = player.server.overworld();
      if (over == null) return;
      EclipseData data = EclipseData.get(over);
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketEclipseState(data.isRunning(), data.getStage(), data.getElapsed()));
   }

   @SubscribeEvent
   public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
      onLogin0(event.getEntity());
   }

   @SubscribeEvent
   public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
      onLogin0(event.getEntity());
   }

   private static void onLogin0(Player entity) {
      if (!(entity instanceof ServerPlayer player)) return;
      ServerLevel over = player.server.overworld();
      if (over == null) return;
      EclipseData data = EclipseData.get(over);
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketEclipseState(data.isRunning(), data.getStage(), data.getElapsed()));
   }

   @SubscribeEvent
   public static void onHurt(LivingHurtEvent event) {
      Entity attacker = event.getSource().getEntity();
      if (!(attacker instanceof Monster)) return;
      if (!isEclipsed(attacker.level())) return;
      event.setAmount(event.getAmount() * MONSTER_DAMAGE_MULT);
   }

   private static void extraSpawns(ServerLevel level) {
      RandomSource rand = level.getRandom();
      for (ServerPlayer player : level.players()) {
         if (player.isCreative() || player.isSpectator()) continue;
         BlockPos origin = player.blockPosition();
         if (level.getEntitiesOfClass(Monster.class,
               player.getBoundingBox().inflate(SPAWN_MAX_RANGE)).size() >= NEARBY_CAP) {
            continue;
         }
         for (int attempt = 0; attempt < SPAWN_ATTEMPTS; ++attempt) {
            BlockPos spot = randomSpot(level, origin, rand);
            if (spot == null) continue;
            if (trySpawn(level, spot, rand)) break;
         }
      }
   }

   private static BlockPos randomSpot(ServerLevel level, BlockPos origin, RandomSource rand) {
      int range = SPAWN_MIN_RANGE + rand.nextInt(SPAWN_MAX_RANGE - SPAWN_MIN_RANGE + 1);
      double angle = rand.nextDouble() * Math.PI * 2.0D;
      int x = origin.getX() + (int) (Math.cos(angle) * range);
      int z = origin.getZ() + (int) (Math.sin(angle) * range);
      if (!level.hasChunkAt(new BlockPos(x, 0, z))) return null;
      int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
      return new BlockPos(x, y, z);
   }

   private static boolean trySpawn(ServerLevel level, BlockPos pos, RandomSource rand) {
      WeightedRandomList<MobSpawnSettings.SpawnerData> pool =
         level.getBiome(pos).value().getMobSettings().getMobs(MobCategory.MONSTER);
      if (pool.isEmpty()) return false;
      MobSpawnSettings.SpawnerData pick = pool.getRandom(rand).orElse(null);
      if (pick == null) return false;

      EntityType<?> type = pick.type;
      if (type.getCategory() != MobCategory.MONSTER) return false;
      if (!level.getWorldBorder().isWithinBounds(pos)) return false;
      if (!SpawnPlacements.checkSpawnRules(type, level, MobSpawnType.NATURAL, pos, rand)) return false;
      if (!level.noCollision(type.getAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D))) return false;

      Entity spawned = type.create(level);
      if (!(spawned instanceof Mob mob)) {
         if (spawned != null) spawned.discard();
         return false;
      }
      mob.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, rand.nextFloat() * 360.0F, 0.0F);
      mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
      level.addFreshEntityWithPassengers(mob);
      return true;
   }

   public static List<ServerPlayer> playersIn(ServerLevel level) {
      return level.players();
   }
}
