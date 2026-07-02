package com.paleimitations.schoolsofmagic.client.effects;

import java.awt.Color;
import java.util.Random;

import com.paleimitations.imitationcore.client.effects.ImitationEffectHandler;
import com.paleimitations.schoolsofmagic.client.effects.effects.Air2Effect;
import com.paleimitations.schoolsofmagic.client.effects.effects.AirEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.Cloud2Effect;
import com.paleimitations.schoolsofmagic.client.effects.effects.CloudEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.DivinationEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.FlameRingEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.FlareEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.SpiralCloudEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.StarEffect;
import com.paleimitations.schoolsofmagic.client.effects.effects.WindEffect;

import net.minecraft.world.level.Level;

public class EffectHelper {
   public EffectHelper() {
   }

   public static StarEffect createFallingStarParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      StarEffect p = new StarEffect(world, x, y, z, 0.0, -0.04F, 0.0, 0.075F + rand.nextFloat() * 0.05F, rand.nextInt(40), 50, color);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static StarEffect createRisingStarParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      StarEffect p = new StarEffect(world, x, y, z, 0.0, 0.04F, 0.0, 0.075F + rand.nextFloat() * 0.05F, rand.nextInt(40), 50, color);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static FlareEffect createFlareParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      FlareEffect p = new FlareEffect(world, x, y, z, 0.0, 0.0, 0.0, 0.75F + rand.nextFloat() * 0.5F, rand.nextInt(40), 50, color);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static SpiralCloudEffect createCloudParticle(Level world, double x, double y, double z) {
      Random rand = new Random();
      SpiralCloudEffect p = new SpiralCloudEffect(
         world, x, y, z, 0.15F + rand.nextFloat() * 0.2F, rand.nextInt(10) - 5, 0, rand.nextBoolean(), rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static SpiralCloudEffect createColoredCloudParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      SpiralCloudEffect p = new SpiralCloudEffect(
         world,
         x,
         y,
         z,
         0.0,
         0.0,
         0.0,
         0.15F + rand.nextFloat() * 0.2F,
         rand.nextInt(10) - 5,
         0,
         color,
         rand.nextBoolean(),
         rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static DivinationEffect createColoredDivination(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      DivinationEffect p = new DivinationEffect(
         world,
         x,
         y,
         z,
         0.0,
         0.0,
         0.0,
         0.15F + rand.nextFloat() * 0.2F,
         rand.nextInt(10) - 5,

         20,
         color,
         rand.nextBoolean(),
         rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static CloudEffect createPuffParticle(Level world, double x, double y, double z) {
      Random rand = new Random();
      CloudEffect p = new CloudEffect(
         world, x, y, z, 0.25F + rand.nextFloat() * 0.25F, rand.nextInt(5), 0, rand.nextBoolean(), rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static CloudEffect createPuffParticle(Level world, double x, double y, double z, double vX, double vZ, Color color) {
      Random rand = new Random();
      CloudEffect p = new CloudEffect(
         world,
         x,
         y,
         z,
         vX,
         0.0,
         vZ,
         0.25F + rand.nextFloat() * 0.25F,
         rand.nextInt(5),
         0,
         color,
         rand.nextBoolean(),
         rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static CloudEffect createSmallPuffParticle(Level world, double x, double y, double z, float scale, Color color) {
      Random rand = new Random();
      CloudEffect p = new CloudEffect(
         world, x, y, z, 0.0, 0.0, 0.0, scale, rand.nextInt(5), 0, color, rand.nextBoolean(), rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static StarEffect createStaticStarParticle(Level world, double x, double y, double z, float scale, Color color) {
      Random rand = new Random();
      StarEffect p = new StarEffect(world, x, y, z, 0.0, 0.0, 0.0, scale, rand.nextInt(40), 50, color);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static CloudEffect createColoredPuffParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      CloudEffect p = new CloudEffect(
         world,
         x,
         y,
         z,
         0.0,
         0.0,
         0.0,
         0.25F + rand.nextFloat() * 0.25F,
         rand.nextInt(5),
         0,
         color,
         rand.nextBoolean(),
         rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static FlameRingEffect createFireRingParticle(Level world, double x, double y, double z, double vx, double vy, double vz, double ry) {
      new Random();

      FlameRingEffect p = new FlameRingEffect(world, x, y, z, 90.0, ry, 0.0, vx, vy, vz, 0.75F, 0, 14, Color.WHITE);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static FlameRingEffect createColoredFireRingParticle(
      Level world, double x, double y, double z, double vx, double vy, double vz, double ry, Color color
   ) {
      new Random();

      FlameRingEffect p = new FlameRingEffect(world, x, y, z, 90.0, ry, 0.0, vx, vy, vz, 0.75F, 0, 14, color, true);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static CloudEffect createPotionPuffParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();

      CloudEffect p = new CloudEffect(
         world,
         x,
         y,
         z,
         0.0,
         0.04,
         0.0,
         0.1F + rand.nextFloat() * 0.05F,
         rand.nextInt(5),
         24,
         color,
         rand.nextBoolean(),
         rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static WindEffect createWindParticle(Level world, double x, double y, double z, double angle, double speed, Color color) {
      Random rand = new Random();
      WindEffect p = new WindEffect(world, x, y, z, angle, speed, 0, 0, 3.5F + rand.nextFloat() * 3.5F, color);
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static AirEffect createAirParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      AirEffect p = new AirEffect(
         world, x, y, z, 0.0, 0.0, 0.0, 0.5F + rand.nextFloat() * 3.0F, 0, 0, color, rand.nextBoolean(), rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static Air2Effect createFogParticle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      Air2Effect p = new Air2Effect(
         world, x, y, z, 0.0, 0.0, 0.0, 0.5F + rand.nextFloat() * 3.0F, 0, 0, color, rand.nextBoolean(), rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }

   public static Cloud2Effect createFog2Particle(Level world, double x, double y, double z, Color color) {
      Random rand = new Random();
      Cloud2Effect p = new Cloud2Effect(
         world, x, y, z, 0.0, 0.0, 0.0, 1.0F + rand.nextFloat() * 2.0F, 0, 0, color, rand.nextBoolean(), rand.nextFloat() * (float) Math.PI * 2.0F
      );
      ImitationEffectHandler.getInstance().registerEffect(p);
      return p;
   }
}
