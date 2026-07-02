package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FaeGroveAmbience {

   private static final ResourceKey<Level> FAEGROVE =
      ResourceKey.create(Registries.DIMENSION, new ResourceLocation("som", "faegrove"));

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      Level level = mc.level;
      Player player = mc.player;
      if (level == null || player == null || mc.isPaused()) return;
      if (!level.dimension().equals(FAEGROVE)) return;

      RandomSource r = level.random;

      if (r.nextInt(3) == 0) {
         int lx = Mth.floor(player.getX()) + r.nextInt(25) - 12;
         int lz = Mth.floor(player.getZ()) + r.nextInt(25) - 12;
         int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, lx, lz);

         double ly = surface + 1 + r.nextDouble() * 12.0;
         level.addParticle(ParticleTypeRegistry.LEAF.get(), lx + r.nextDouble(), ly, lz + r.nextDouble(), 0.0D, -0.02D, 0.0D);
      }

      long t = level.getDayTime() % 24000L;
      boolean night = t >= 13000L && t <= 23000L;
      if (night && r.nextInt(4) == 0) {
         int fx = Mth.floor(player.getX()) + r.nextInt(25) - 12;
         int fz = Mth.floor(player.getZ()) + r.nextInt(25) - 12;
         int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fx, fz);
         double fy = surface + 0.5 + r.nextDouble() * 3.0;
         level.addParticle(ParticleTypeRegistry.BUG.get(), fx + r.nextDouble(), fy, fz + r.nextDouble(), 0.0D, 0.0D, 0.0D);
      }
   }
}
