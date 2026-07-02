package com.paleimitations.schoolsofmagic.common.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

public class BiomeRegistry {
    private static ResourceKey<Biome> key(String name) {
        return ResourceKey.create(net.minecraft.core.registries.Registries.BIOME,
                new ResourceLocation(SchoolsOfMagic.MODID, name));
    }

    public static final ResourceKey<Biome> WETLANDS = key("wetlands");
    public static final ResourceKey<Biome> SINISTERSWAMP = key("sinister_swamp");
    public static final ResourceKey<Biome> STRANGEHILLS = key("bastion_hills");
    public static final ResourceKey<Biome> AUTUMNWOODS = key("bastion_woods");
    public static final ResourceKey<Biome> ACOLYTEWOODS = key("acolyte_woods");
    public static final ResourceKey<Biome> DESERTCANYONS = key("desert_canyons");
    public static final ResourceKey<Biome> DESERTUPLANDS = key("desert_uplands");
    public static final ResourceKey<Biome> BANDEDDESERT = key("banded_desert");
    public static final ResourceKey<Biome> VERMILIONGROVE = key("vermilion_grove");
    public static final ResourceKey<Biome> MOUNTAINOUS_JUNGLE = key("mountainous_jungle");
    public static final ResourceKey<Biome> LABYRINTH = key("labyrinth");
    public static final ResourceKey<Biome> BOILING_MARSH = key("boiling_marsh");
    public static final ResourceKey<Biome> MAGIC_DESERT = key("magic_desert");
    public static final ResourceKey<Biome> DESERT_RIVER_CANYONS = key("desert_river_canyons");
    public static final ResourceKey<Biome> FAE_GROVE = key("fae_grove");
    public static final ResourceKey<Biome> RIVER_LANDS = key("river_lands");

    public static void registerBiomes() {

    }
}
