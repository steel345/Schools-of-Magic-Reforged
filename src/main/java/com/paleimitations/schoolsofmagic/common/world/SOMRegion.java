package com.paleimitations.schoolsofmagic.common.world;

import com.mojang.datafixers.util.Pair;
import com.paleimitations.schoolsofmagic.common.registries.BiomeRegistry;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
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
   public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
      this.addModifiedVanillaOverworldBiomes(mapper, builder -> {

         builder.replaceBiome(Biomes.DESERT, BiomeRegistry.MAGIC_DESERT);
         builder.replaceBiome(Biomes.SAVANNA, BiomeRegistry.BANDEDDESERT);
         builder.replaceBiome(Biomes.BADLANDS, BiomeRegistry.DESERTCANYONS);
         builder.replaceBiome(Biomes.ERODED_BADLANDS, BiomeRegistry.DESERT_RIVER_CANYONS);
         builder.replaceBiome(Biomes.WOODED_BADLANDS, BiomeRegistry.DESERTUPLANDS);

         builder.replaceBiome(Biomes.SWAMP, BiomeRegistry.WETLANDS);
         builder.replaceBiome(Biomes.MANGROVE_SWAMP, BiomeRegistry.BOILING_MARSH);

         builder.replaceBiome(Biomes.DARK_FOREST, BiomeRegistry.ACOLYTEWOODS);
         builder.replaceBiome(Biomes.FOREST, BiomeRegistry.AUTUMNWOODS);
         builder.replaceBiome(Biomes.FLOWER_FOREST, BiomeRegistry.STRANGEHILLS);

         builder.replaceBiome(Biomes.JUNGLE, BiomeRegistry.MOUNTAINOUS_JUNGLE);

      });
   }
}
