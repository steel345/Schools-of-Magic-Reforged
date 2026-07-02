package com.paleimitations.schoolsofmagic.common.registries;

import com.mojang.serialization.Codec;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.world.dimensions.FaeGroveBiomeSource;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BiomeSourceRegistry {

   public static final DeferredRegister<Codec<? extends BiomeSource>> BIOME_SOURCES =
      DeferredRegister.create(Registries.BIOME_SOURCE, SchoolsOfMagic.MODID);

   public static final RegistryObject<Codec<? extends BiomeSource>> FAE_GROVE =
      BIOME_SOURCES.register("fae_grove", () -> FaeGroveBiomeSource.CODEC);

   public static void register(IEventBus modBus) {
      BIOME_SOURCES.register(modBus);
   }
}
