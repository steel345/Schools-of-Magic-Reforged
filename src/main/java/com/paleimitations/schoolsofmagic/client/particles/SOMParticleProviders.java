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
