package com.paleimitations.schoolsofmagic.common.world.biomes;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

public final class SOMBiomes {
   private SOMBiomes() {}

   private static ResourceKey<Biome> key(String name) {
      return ResourceKey.create(Registries.BIOME, new ResourceLocation(SchoolsOfMagic.MODID, name));
   }

   public static final ResourceKey<Biome> VERMILION_GROVE     = key("vermilion_grove");
   public static final ResourceKey<Biome> ACOLYTE_WOODS       = key("acolyte_woods");
   public static final ResourceKey<Biome> BASTION_WOODS       = key("bastion_woods");
   public static final ResourceKey<Biome> LABYRINTH           = key("labyrinth");
   public static final ResourceKey<Biome> MAGIC_DESERT        = key("magic_desert");
   public static final ResourceKey<Biome> BANDED_DESERT       = key("banded_desert");
   public static final ResourceKey<Biome> DESERT_CANYONS      = key("desert_canyons");
   public static final ResourceKey<Biome> DESERT_RIVER_CANYONS = key("desert_river_canyons");
   public static final ResourceKey<Biome> DESERT_UPLANDS      = key("desert_uplands");
   public static final ResourceKey<Biome> WETLANDS            = key("wetlands");
   public static final ResourceKey<Biome> BOILING_MARSH       = key("boiling_marsh");
   public static final ResourceKey<Biome> SINISTER_SWAMP      = key("sinister_swamp");
   public static final ResourceKey<Biome> MOUNTAINOUS_JUNGLE  = key("mountainous_jungle");
   public static final ResourceKey<Biome> RIVER_LANDS         = key("river_lands");
}
