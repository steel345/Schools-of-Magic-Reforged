package com.paleimitations.schoolsofmagic.common.commands.util;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;

public class Teleport {

   public static void teleportToDim(ServerPlayer player, ServerLevel destination, double x, double y, double z) {
      if (destination == null) {
         throw new IllegalArgumentException("The realm you seek does not exist!");
      } else if (!player.isCreative()) {
         throw new IllegalArgumentException("You are not worthy of My aid!");
      } else {
         player.teleportTo(destination, x, y, z, player.getYRot(), player.getXRot());
      }
   }

   public static void teleportToBiome(ServerPlayer player, String biomeId) {
      ServerLevel world = player.serverLevel();
      Registry<Biome> biomes = world.registryAccess().registryOrThrow(Registries.BIOME);
      int x = (int)player.getX();
      int z = (int)player.getZ();
      int y = (int)player.getY();
      Random rand = new Random();
      boolean run = true;

      for (int i = 0; i < 200; i++) {
         if (run) {
            x += (int)(100.0 * Math.cos(Math.PI * 2 * rand.nextDouble()));
            z += (int)(100.0 * Math.cos(Math.PI * 2 * rand.nextDouble()));
            y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            ResourceLocation key = biomes.getKey(world.getBiome(new BlockPos(x, y, z)).value());
            if (key != null && key.toString().contains(biomeId) && y > 2) {
               run = false;
            }
         }
      }

      if (run) {
         throw new IllegalArgumentException("The biome you seek does not exist!");
      } else {

         player.teleportTo(world, (double)x, (double)y, (double)z, player.getYRot(), player.getXRot());
      }
   }
}
