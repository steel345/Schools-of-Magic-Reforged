package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.network.PacketEarthenRide;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.CapabilityBanishedBlocks;
import com.paleimitations.schoolsofmagic.common.world.capabilities.banishedblocks.IBanishedBlocks;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class EarthenElevatorHandler {
   private static final double LIFT = 0.32D;
   private static final int CLOSE_DELAY = 60;


   private static final Map<UUID, double[]> RIDES = new HashMap<>();
   private static float localBar = 1.0F;

   public static void setLocalBar(float ratio) {
      localBar = ratio;
   }

   public static float localBar() {
      return localBar;
   }

   public static boolean isEarthen(BlockState state) {
      if (state.isAir()) return false;
      return state.is(BlockTags.DIRT)
         || state.is(BlockTags.SAND)
         || state.is(BlockTags.BASE_STONE_OVERWORLD)
         || state.is(BlockTags.BASE_STONE_NETHER)
         || state.is(BlockTags.TERRACOTTA)
         || state.is(net.minecraftforge.common.Tags.Blocks.GRAVEL)
         || state.is(net.minecraftforge.common.Tags.Blocks.ORES)
         || state.is(Blocks.CLAY)
         || state.is(Blocks.MUD)
         || state.is(Blocks.SOUL_SAND)
         || state.is(Blocks.SOUL_SOIL)
         || state.is(Blocks.MOSS_BLOCK)
         || state.is(Blocks.SNOW_BLOCK);
   }

   // anything an entity put there is not earth as far as this spell is concerned
   private static boolean natural(Level level, BlockPos pos) {
      return isEarthen(level.getBlockState(pos))
         && !com.paleimitations.schoolsofmagic.common.world.capabilities.placedblocks.CapabilityPlacedBlocks.isPlaced(level, pos);
   }

   private static boolean open(Level level, BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      return state.isAir() || state.canBeReplaced();
   }

   // returns the y to rise to, or Integer.MIN_VALUE when something that is not earth is in the way
   public static int findSurface(Level level, ServerPlayer player) {
      BlockPos feet = player.blockPosition();
      int surface = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, feet).getY();
      if (surface <= feet.getY() + 1) return Integer.MIN_VALUE;

      for (int y = feet.getY() + 2; y <= surface; y++) {
         BlockPos pos = new BlockPos(feet.getX(), y, feet.getZ());
         if (open(level, pos)) continue;
         if (!natural(level, pos)) return Integer.MIN_VALUE;
      }
      return surface;
   }

   public static void start(ServerPlayer player, int targetY, int ticks) {
      RIDES.put(player.getUUID(), new double[]{targetY, ticks, ticks});
      player.setNoGravity(true);
      player.fallDistance = 0.0F;
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketEarthenRide(true, ticks, ticks));
   }

   private static void stop(ServerPlayer player) {
      RIDES.remove(player.getUUID());
      player.setNoGravity(false);
      player.fallDistance = 0.0F;
      PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
         new PacketEarthenRide(false, 0, 0));
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      if (!(event.player instanceof ServerPlayer player)) return;

      double[] ride = RIDES.get(player.getUUID());
      if (ride == null) return;

      if (--ride[1] <= 0 || player.getY() >= ride[0]) {
         stop(player);
         return;
      }
      if ((int) ride[1] % 20 == 0) {
         PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
            new PacketEarthenRide(true, (int) ride[1], (int) ride[2]));
      }

      part(player);
      player.setDeltaMovement(0.0D, 0.0D, 0.0D);
      player.teleportTo(player.getX(), player.getY() + LIFT, player.getZ());
      player.fallDistance = 0.0F;
   }

   // the earth is only borrowed, the banished blocks capability puts it back on its own
   private static void part(ServerPlayer player) {
      Level level = player.level();
      IBanishedBlocks banished = level.getCapability(CapabilityBanishedBlocks.BANISHED_BLOCKS_CAPABILITY).orElse(null);
      BlockPos feet = player.blockPosition();

      for (int y = 0; y <= 2; y++) {
         BlockPos pos = feet.above(y);
         BlockState state = level.getBlockState(pos);
         if (!natural(level, pos)) continue;
         if (banished != null) banished.addSet(pos, state, CLOSE_DELAY);
         level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
      }
   }
}
