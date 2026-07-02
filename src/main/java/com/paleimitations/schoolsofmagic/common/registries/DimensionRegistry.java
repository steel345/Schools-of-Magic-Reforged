package com.paleimitations.schoolsofmagic.common.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;

public class DimensionRegistry {
    public static final ResourceKey<Level> FAEGROVE = ResourceKey.create(
            net.minecraft.core.registries.Registries.DIMENSION,
            new ResourceLocation(SchoolsOfMagic.MODID, "fae_grove"));

    public static void registerDimensions() {

    }
}
