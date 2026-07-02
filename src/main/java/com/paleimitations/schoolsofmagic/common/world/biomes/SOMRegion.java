package com.paleimitations.schoolsofmagic.common.world.biomes;

import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

public class SOMRegion extends Region {

   public SOMRegion(ResourceLocation name, int weight) {
      super(name, RegionType.OVERWORLD, weight);
   }

   @Override
   public void addBiomes(net.minecraft.core.Registry<Biome> registry,
         Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
      this.addModifiedVanillaOverworldBiomes(mapper, builder -> {

         builder.replaceBiome(Biomes.FOREST, SOMBiomes.ACOLYTE_WOODS);
         builder.replaceBiome(Biomes.BIRCH_FOREST, SOMBiomes.VERMILION_GROVE);
         builder.replaceBiome(Biomes.TAIGA, SOMBiomes.BASTION_WOODS);

         builder.replaceBiome(Biomes.DARK_FOREST, SOMBiomes.VERMILION_GROVE);

         builder.replaceBiome(Biomes.WOODED_BADLANDS, SOMBiomes.MOUNTAINOUS_JUNGLE);

         builder.replaceBiome(Biomes.DESERT, SOMBiomes.MAGIC_DESERT);
         builder.replaceBiome(Biomes.BADLANDS, SOMBiomes.BANDED_DESERT);
         builder.replaceBiome(Biomes.ERODED_BADLANDS, SOMBiomes.DESERT_CANYONS);
         builder.replaceBiome(Biomes.JUNGLE, SOMBiomes.DESERT_RIVER_CANYONS);
         builder.replaceBiome(Biomes.SAVANNA, SOMBiomes.DESERT_UPLANDS);

         builder.replaceBiome(Biomes.SAVANNA_PLATEAU, SOMBiomes.DESERT_UPLANDS);
         builder.replaceBiome(Biomes.WINDSWEPT_SAVANNA, SOMBiomes.DESERT_UPLANDS);

         builder.replaceBiome(Biomes.SPARSE_JUNGLE, SOMBiomes.WETLANDS);
         builder.replaceBiome(Biomes.MANGROVE_SWAMP, SOMBiomes.BOILING_MARSH);
         builder.replaceBiome(Biomes.OLD_GROWTH_SPRUCE_TAIGA, SOMBiomes.SINISTER_SWAMP);

         builder.replaceBiome(Biomes.STONY_SHORE, SOMBiomes.RIVER_LANDS);
      });
   }

   static ResourceKey<Biome> key(String name) {
      return ResourceKey.create(Registries.BIOME, new ResourceLocation("som", name));
   }
}
