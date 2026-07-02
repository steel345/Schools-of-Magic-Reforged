package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SOMFaeGroveSpawns {

   public static final ResourceKey<Level> FAEGROVE =
      ResourceKey.create(Registries.DIMENSION, new ResourceLocation("som", "faegrove"));

   @SubscribeEvent
   public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
      ServerLevelAccessor level = event.getLevel();
      if (!level.getLevel().dimension().equals(FAEGROVE)) return;
      if (!(event.getEntity() instanceof Enemy)) return;

      MobSpawnType type = event.getSpawnType();
      if (type != MobSpawnType.NATURAL && type != MobSpawnType.CHUNK_GENERATION) return;

      int x = Mth.floor(event.getX());
      int y = Mth.floor(event.getY());
      int z = Mth.floor(event.getZ());
      int surface = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

      if (y >= surface - 1) {
         event.setSpawnCancelled(true);
         return;
      }

      if (event.getEntity().getRandom().nextBoolean()) {
         event.setSpawnCancelled(true);
      }
   }
}
