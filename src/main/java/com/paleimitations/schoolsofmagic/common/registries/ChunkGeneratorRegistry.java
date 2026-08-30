package com.paleimitations.schoolsofmagic.common.registries;

import com.mojang.serialization.Codec;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.world.dimensions.AstralCorridorGenerator;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ChunkGeneratorRegistry {
   public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS =
      DeferredRegister.create(Registries.CHUNK_GENERATOR, SchoolsOfMagic.MODID);

   public static final RegistryObject<Codec<? extends ChunkGenerator>> ASTRAL_CORRIDOR =
      CHUNK_GENERATORS.register("astral_corridor", () -> AstralCorridorGenerator.CODEC);

   public static void register(IEventBus modBus) {
      CHUNK_GENERATORS.register(modBus);
   }
}
