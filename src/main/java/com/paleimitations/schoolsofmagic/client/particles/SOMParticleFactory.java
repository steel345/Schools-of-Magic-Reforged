package com.paleimitations.schoolsofmagic.client.particles;

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.level.Level;

public class SOMParticleFactory {
   private static final Map<SOMParticleType, IParticleFactory> factories = new EnumMap<>(SOMParticleType.class);

   @Nullable
   public static Particle createParticle(SOMParticleType particleType, Level world, double x, double y, double z, double vx, double vy, double vz, float rotationX, float rotationY, float rotationZ, float alpha) {

      if (!(world instanceof ClientLevel clientLevel)) {
         return null;
      }
      IParticleFactory factory = factories.get(particleType);
      if (factory == null) return null;
      Particle p = factory.createParticle(clientLevel, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha);

      net.minecraft.client.particle.SpriteSet set = SOMParticleProviders.SPRITES.get(particleType);
      if (set != null) {

         if (p instanceof IAnimatedParticle a) {
            a.setSprites(set);
         } else if (p instanceof net.minecraft.client.particle.TextureSheetParticle tsp) {
            tsp.setSpriteFromAge(set);
         }
      }
      return p;
   }

   static {
      factories.put(SOMParticleType.BUG, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleFly(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.EMBER, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleEmbers(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.SNORE, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleSnore(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.SNOW, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleSnowFlake(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.FIRE_PLUME, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleFirePlume(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.LEAF, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleLeaf(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.FLOWER, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticlePetal(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.WATER, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleWater(world, x, y, z, vx, vy, vz));
      factories.put(SOMParticleType.FIRE_RING, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleFireRing(world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ));
      factories.put(SOMParticleType.AIR, (world, x, y, z, vx, vy, vz, rotationX, rotationY, rotationZ, alpha) -> new ParticleAir(world, x, y, z, 0, 40, rotationX, rotationY, rotationZ, alpha));
   }

   @FunctionalInterface
   private interface IParticleFactory {
      Particle createParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, float rotationX, float rotationY, float rotationZ, float alpha);
   }
}
