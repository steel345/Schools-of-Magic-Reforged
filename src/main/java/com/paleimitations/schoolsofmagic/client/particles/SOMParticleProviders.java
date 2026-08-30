package com.paleimitations.schoolsofmagic.client.particles;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SOMParticleProviders {
    public static final java.util.Map<SOMParticleType, SpriteSet> SPRITES =
        new java.util.EnumMap<>(SOMParticleType.class);

    @SubscribeEvent
    public static void register(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.FLAME.get(),      s -> { SPRITES.put(SOMParticleType.FLAME, s);      return new ProviderFlame(s); });
        event.registerSpriteSet(ParticleTypeRegistry.EMBER.get(),      s -> { SPRITES.put(SOMParticleType.EMBER, s);      return new ProviderEmbers(s); });
        event.registerSpriteSet(ParticleTypeRegistry.LEAF.get(),       s -> { SPRITES.put(SOMParticleType.LEAF, s);       return new ProviderLeaf(s); });
        event.registerSpriteSet(ParticleTypeRegistry.FLOWER.get(),     s -> { SPRITES.put(SOMParticleType.FLOWER, s);     return new ProviderPetal(s); });
        event.registerSpriteSet(ParticleTypeRegistry.BUG.get(),        s -> { SPRITES.put(SOMParticleType.BUG, s);        return new ProviderFly(s); });
        event.registerSpriteSet(ParticleTypeRegistry.WATER.get(),      s -> { SPRITES.put(SOMParticleType.WATER, s);      return new ProviderWater(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SNORE.get(),      s -> { SPRITES.put(SOMParticleType.SNORE, s);      return new ProviderSnore(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SNOW.get(),       s -> { SPRITES.put(SOMParticleType.SNOW, s);       return new ProviderSnow(s); });
        event.registerSpriteSet(ParticleTypeRegistry.FIRE_RING.get(),  s -> { SPRITES.put(SOMParticleType.FIRE_RING, s);  return new ProviderFireRing(s); });
        event.registerSpriteSet(ParticleTypeRegistry.FIRE_PLUME.get(), s -> { SPRITES.put(SOMParticleType.FIRE_PLUME, s); return new ProviderFirePlume(s); });
        event.registerSpriteSet(ParticleTypeRegistry.AIR.get(),        s -> { SPRITES.put(SOMParticleType.AIR, s);        return new ProviderAir(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SKULL.get(),      s -> { SPRITES.put(SOMParticleType.SKULL, s);      return new ProviderSkull(s); });
        event.registerSpriteSet(ParticleTypeRegistry.ORB.get(),        s -> { SPRITES.put(SOMParticleType.ORB, s);        return new ProviderOrb(s); });
        event.registerSpriteSet(ParticleTypeRegistry.ORB_CORE.get(),   s -> { SPRITES.put(SOMParticleType.ORB_CORE, s);   return new ProviderOrbCore(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SPARKLE_STAR.get(), s -> { SPRITES.put(SOMParticleType.SPARKLE_STAR, s); return new ProviderSparkleStar(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SPARKLE_RAY.get(), s -> { SPRITES.put(SOMParticleType.SPARKLE_RAY, s); return new ProviderSparkleRay(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SCULK_BLOOM.get(), s -> { SPRITES.put(SOMParticleType.SCULK_BLOOM, s); return new ProviderSculkBloom(s); });
        event.registerSpriteSet(ParticleTypeRegistry.HOURGLASS.get(), s -> { SPRITES.put(SOMParticleType.HOURGLASS, s); return new ProviderHourglass(s); });
        event.registerSpriteSet(ParticleTypeRegistry.CAST_CIRCLE.get(), s -> { SPRITES.put(SOMParticleType.CAST_CIRCLE, s); return new ProviderCastCircle(s); });
        event.registerSpriteSet(ParticleTypeRegistry.GAS.get(), s -> { SPRITES.put(SOMParticleType.GAS, s); return new ProviderGas(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SHOCKWAVE.get(), s -> { SPRITES.put(SOMParticleType.SHOCKWAVE, s); return new ProviderShockwave(s); });
        event.registerSpriteSet(ParticleTypeRegistry.FOG.get(), s -> { SPRITES.put(SOMParticleType.FOG, s); return new ProviderFog(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SPORE.get(), s -> { SPRITES.put(SOMParticleType.SPORE, s); return new ProviderSpore(s); });
        event.registerSpriteSet(ParticleTypeRegistry.SPORE_SEED.get(), s -> new ProviderSporeSeed());
        event.registerSpriteSet(ParticleTypeRegistry.PLASMA.get(),     ParticlePlasma.Provider::new);
    }

    private static abstract class BaseProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprites;
        BaseProvider(SpriteSet sprites) { this.sprites = sprites; }
        protected void applySprite(Particle p) {
            if (p instanceof IAnimatedParticle a) {
                a.setSprites(sprites);
            } else if (p instanceof TextureSheetParticle tsp) {
                tsp.setSpriteFromAge(sprites);
            }
        }
    }

    private static final class ProviderSpore extends BaseProvider {
        ProviderSpore(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            ParticleSpore p = new ParticleSpore(l, x, y, z, vx, vy, vz);
            p.tint(SporeTint.red(), SporeTint.green(), SporeTint.blue());
            p.sprites(this.sprites);
            return p;
        }
    }

    private static final class ProviderFog extends BaseProvider {
        ProviderFog(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            ParticleFog p = new ParticleFog(l, x, y, z, vx, vy, vz);
            // the spell puts how long the bank should hold in the vertical argument
            if (vy > 1.0D) p.setLifetime((int) vy);
            this.applySprite(p);
            return p;
        }
    }

    private static final class ProviderSporeSeed implements ParticleProvider<SimpleParticleType> {
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            return new ParticleSporeSeed(l, x, y, z, vx, vy, vz);
        }
    }

    private static final class ProviderHourglass extends BaseProvider {
        ProviderHourglass(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleHourglass(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }

    private static final class ProviderCastCircle extends BaseProvider {
        ProviderCastCircle(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleCastCircle(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }

    private static final class ProviderGas extends BaseProvider {
        ProviderGas(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleGas(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }

    private static final class ProviderShockwave extends BaseProvider {
        ProviderShockwave(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleShockwave(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }

    private static final class ProviderFlame extends BaseProvider {
        ProviderFlame(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleFlame(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderEmbers extends BaseProvider {
        ProviderEmbers(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleEmbers(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderLeaf extends BaseProvider {
        ProviderLeaf(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleLeaf(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderPetal extends BaseProvider {
        ProviderPetal(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticlePetal(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderFly extends BaseProvider {
        ProviderFly(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleFly(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderWater extends BaseProvider {
        ProviderWater(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleWater(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderSnore extends BaseProvider {
        ProviderSnore(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleSnore(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderOrb extends BaseProvider {
        ProviderOrb(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleOrb(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderOrbCore extends BaseProvider {
        ProviderOrbCore(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleOrb(l, x, y, z, 0.0D, 0.0D, 0.0D, true);
            applySprite(p); return p;
        }
    }
    private static final class ProviderSculkBloom extends BaseProvider {
        ProviderSculkBloom(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleSculkBloom(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderSparkleRay extends BaseProvider {
        private static final double R = 115.0D / 255.0D;
        private static final double G = 39.0D / 255.0D;
        private static final double B = 177.0D / 255.0D;
        ProviderSparkleRay(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleSparkleStar(l, x, y, z, R, G, B, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderSparkleStar extends BaseProvider {
        ProviderSparkleStar(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleSparkleStar(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderSkull extends BaseProvider {
        ProviderSkull(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleSkull(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderSnow extends BaseProvider {
        ProviderSnow(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleSnowFlake(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderFireRing extends BaseProvider {
        ProviderFireRing(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleFireRing(l, x, y, z, vx, vy, vz, 0f, 0f, 0f);
            applySprite(p); return p;
        }
    }
    private static final class ProviderFirePlume extends BaseProvider {
        ProviderFirePlume(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleFirePlume(l, x, y, z, vx, vy, vz);
            applySprite(p); return p;
        }
    }
    private static final class ProviderAir extends BaseProvider {
        ProviderAir(SpriteSet s) { super(s); }
        @Override @Nullable public Particle createParticle(SimpleParticleType t, ClientLevel l, double x, double y, double z, double vx, double vy, double vz) {
            Particle p = new ParticleAir(l, x, y, z, 0, 40, 1f, 1f, 1f, 1f);
            applySprite(p); return p;
        }
    }
}
