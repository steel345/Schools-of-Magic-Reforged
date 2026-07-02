package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SchoolsOfMagic.MODID);

    public static final RegistryObject<SimpleParticleType> FLOWER     = PARTICLES.register("flower",     () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> LEAF       = PARTICLES.register("leaf",       () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BUG        = PARTICLES.register("bug",        () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> FLAME      = PARTICLES.register("flame",      () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> FIRE_RING  = PARTICLES.register("fire_ring",  () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WATER      = PARTICLES.register("water",      () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> FIRE_PLUME = PARTICLES.register("fire_plume", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> EMBER      = PARTICLES.register("ember",      () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SNOW       = PARTICLES.register("snow",       () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> AIR        = PARTICLES.register("air",        () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SNORE      = PARTICLES.register("snore",      () -> new SimpleParticleType(false));
    public static final RegistryObject<com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleType> PLASMA =
        PARTICLES.register("plasma", com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleType::new);

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }
}
