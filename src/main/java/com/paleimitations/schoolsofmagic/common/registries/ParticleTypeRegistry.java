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
    public static final RegistryObject<SimpleParticleType> SKULL      = PARTICLES.register("skull",      () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> ORB        = PARTICLES.register("orb",        () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> ORB_CORE   = PARTICLES.register("orb_core",   () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> SPARKLE_STAR = PARTICLES.register("sparkle_star", () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> SPARKLE_RAY = PARTICLES.register("sparkle_ray", () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> FOG = PARTICLES.register("fog", () -> new SimpleParticleType(true));
   public static final RegistryObject<SimpleParticleType> SPORE = PARTICLES.register("spore", () -> new SimpleParticleType(true));
   public static final RegistryObject<SimpleParticleType> SPORE_SEED = PARTICLES.register("spore_seed", () -> new SimpleParticleType(true));
   public static final RegistryObject<SimpleParticleType> HOURGLASS = PARTICLES.register("hourglass", () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> CAST_CIRCLE = PARTICLES.register("cast_circle", () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> GAS = PARTICLES.register("gas", () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> SHOCKWAVE = PARTICLES.register("shockwave", () -> new SimpleParticleType(false));
   public static final RegistryObject<SimpleParticleType> SCULK_BLOOM = PARTICLES.register("sculk_bloom", () -> new SimpleParticleType(false));
    public static final RegistryObject<com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleType> PLASMA =
        PARTICLES.register("plasma", com.paleimitations.schoolsofmagic.common.particles.PlasmaParticleType::new);

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }
}
