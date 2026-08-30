package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.client.effects.EffectHelper;
import com.paleimitations.schoolsofmagic.client.effects.effects.StarEffect;
import com.paleimitations.schoolsofmagic.common.handlers.DazzlingLightHandler;
import com.paleimitations.imitationcore.client.effects.ImitationEffectHandler;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT)
public class DazzlingLightShow {
   private static final int LENGTH = DazzlingLightHandler.SHOW_TICKS;

   private static final int IGNITE_END = 8;
   private static final int BLOOM_START = 8;
   private static final int BLOOM_END = 92;
   private static final int BURST_START = 40;
   private static final int BURST_END = 170;
   private static final int BURST_EVERY = 8;
   private static final int RING_START = 45;
   private static final int RING_END = 168;
   private static final int CLIMAX_START = 168;
   private static final int CLIMAX_END = 188;

   private static final Color[] PALETTE = {
      new Color(255, 255, 255),
      new Color(198, 226, 255),
      new Color(255, 236, 170),
      new Color(214, 186, 255),
      new Color(170, 240, 255)
   };

   private static final class Show {
      Vec3 pos;
      int tick;
      float spin;
   }

   private static final List<Show> SHOWS = new ArrayList<>();

   public static void start(double x, double y, double z) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null) return;
      Show show = new Show();
      show.pos = new Vec3(x, y, z);
      show.tick = 0;
      show.spin = mc.level.random.nextFloat() * Mth.TWO_PI;
      SHOWS.add(show);
   }

   public static java.util.List<Vec3> livePositions() {
      java.util.List<Vec3> out = new ArrayList<>();
      for (Show show : SHOWS) out.add(show.pos);
      return out;
   }

   public static float brightness(Vec3 pos) {
      for (Show show : SHOWS) {
         if (show.pos != pos) continue;
         float fade = 1.0F - Math.max(0.0F, (show.tick - (LENGTH - 20)) / 20.0F);
         float rise = Math.min(1.0F, show.tick / 6.0F);
         return Math.max(0.0F, Math.min(rise, fade));
      }
      return 0.0F;
   }

   private static Color tint(RandomSource rand) {
      return PALETTE[rand.nextInt(PALETTE.length)];
   }

   private static void star(Level level, Vec3 at, Vec3 motion, float scale, int life, Color color) {
      StarEffect star = new StarEffect(level, at.x, at.y, at.z,
         motion.x, motion.y, motion.z, scale, 0, life, color);
      ImitationEffectHandler.getInstance().registerEffect(star);
   }

   @SubscribeEvent
   public static void onClientTick(TickEvent.ClientTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Minecraft mc = Minecraft.getInstance();
      if (mc.level == null || mc.isPaused()) return;

      if (SHOWS.isEmpty()) {
         DazzlingLightHandler.setLocalBar(1.0F);
         return;
      }

      float longest = 0.0F;
      for (Show show : SHOWS) {
         run(mc.level, show);
         longest = Math.max(longest, 1.0F - (float) show.tick / (float) LENGTH);
      }
      DazzlingLightHandler.setLocalBar(longest);
      SHOWS.removeIf(show -> show.tick++ >= LENGTH);
   }

   private static void run(Level level, Show show) {
      RandomSource rand = level.random;
      Vec3 heart = show.pos;
      int t = show.tick;

      if (t < IGNITE_END) {
         ignite(level, rand, heart, t);
      }
      if (t >= BLOOM_START && t < BLOOM_END) {
         bloom(level, rand, heart, show, t);
      }
      if (t >= BURST_START && t < BURST_END && t % BURST_EVERY == 0) {
         sideBurst(level, rand, heart);
      }
      if (t >= IGNITE_END && t < LENGTH) {
         drift(level, rand, heart);
      }
      if (t >= RING_START && t < RING_END) {
         ring(level, rand, heart, (t - RING_START) % 40);
      }
      if (t >= CLIMAX_START && t < CLIMAX_END) {
         climax(level, rand, heart, t - CLIMAX_START);
      }
      if (t % 2 == 0) {
         twinkle(level, rand, heart, t);
      }
   }

   private static void ignite(Level level, RandomSource rand, Vec3 heart, int t) {
      EffectHelper.createFlareParticle(level, heart.x, heart.y, heart.z, Color.WHITE);
      if (t == 0) {
         level.addParticle(ParticleTypes.FLASH, heart.x, heart.y, heart.z, 0.0D, 0.0D, 0.0D);
      }
      for (int i = 0; i < 12; i++) {
         Vec3 dir = randomDirection(rand).scale(0.35D + rand.nextDouble() * 0.25D);
         level.addParticle(ParticleTypes.END_ROD, heart.x, heart.y, heart.z, dir.x, dir.y, dir.z);
      }
   }

   // stars ride an opening spiral instead of flying straight, that is what sells it as a working
   private static void bloom(Level level, RandomSource rand, Vec3 heart, Show show, int t) {
      float progress = (float) (t - BLOOM_START) / (float) (BLOOM_END - BLOOM_START);
      int arms = 5;
      int perArm = 3;

      for (int arm = 0; arm < arms; arm++) {
         double base = show.spin + arm * (Mth.TWO_PI / arms) + t * 0.22D;
         for (int i = 0; i < perArm; i++) {
            double angle = base + i * 0.18D;
            double lift = Math.sin(progress * Math.PI) * 0.9D;
            double radius = 0.25D + progress * 0.5D;

            Vec3 at = heart.add(Math.cos(angle) * radius,
               (rand.nextDouble() - 0.4D) * 0.5D,
               Math.sin(angle) * radius);

            double outward = 0.13D + progress * 0.06D;
            Vec3 motion = new Vec3(
               Math.cos(angle) * outward - Math.sin(angle) * 0.07D,
               0.035D + rand.nextDouble() * 0.03D * lift,
               Math.sin(angle) * outward + Math.cos(angle) * 0.07D);

            star(level, at, motion, 0.10F + rand.nextFloat() * 0.06F, 40 + rand.nextInt(25), tint(rand));
         }
      }
      if (t % 3 == 0) {
         level.addParticle(ParticleTypes.GLOW, heart.x, heart.y + 0.2D, heart.z, 0.0D, 0.02D, 0.0D);
      }
   }

   private static void sideBurst(Level level, RandomSource rand, Vec3 heart) {
      Vec3 at = heart.add((rand.nextDouble() - 0.5D) * 6.0D,
         (rand.nextDouble() - 0.2D) * 3.5D,
         (rand.nextDouble() - 0.5D) * 6.0D);
      Color color = tint(rand);

      EffectHelper.createFlareParticle(level, at.x, at.y, at.z, color);
      for (int i = 0; i < 14; i++) {
         Vec3 dir = randomDirection(rand).scale(0.07D + rand.nextDouble() * 0.07D);
         star(level, at, dir, 0.07F + rand.nextFloat() * 0.05F, 25 + rand.nextInt(20), color);
      }
      for (int i = 0; i < 6; i++) {
         Vec3 dir = randomDirection(rand).scale(0.12D);
         level.addParticle(ParticleTypes.FIREWORK, at.x, at.y, at.z, dir.x, dir.y, dir.z);
      }
   }

   private static void drift(Level level, RandomSource rand, Vec3 heart) {
      for (int i = 0; i < 2; i++) {
         Vec3 at = heart.add((rand.nextDouble() - 0.5D) * 7.0D,
            (rand.nextDouble() - 0.3D) * 4.0D,
            (rand.nextDouble() - 0.5D) * 7.0D);
         Vec3 motion = new Vec3((rand.nextDouble() - 0.5D) * 0.012D,
            0.006D + rand.nextDouble() * 0.01D,
            (rand.nextDouble() - 0.5D) * 0.012D);
         star(level, at, motion, 0.06F + rand.nextFloat() * 0.05F, 70 + rand.nextInt(40), tint(rand));
      }
   }

   private static void ring(Level level, RandomSource rand, Vec3 heart, int age) {
      double radius = 0.6D + age * 0.16D;
      int points = 26;
      for (int i = 0; i < points; i++) {
         double angle = i * (Mth.TWO_PI / points) + age * 0.05D;
         double x = heart.x + Math.cos(angle) * radius;
         double z = heart.z + Math.sin(angle) * radius;
         double y = heart.y + Math.sin(age * 0.2D + i) * 0.15D;

         if (i % 3 == 0) {
            star(level, new Vec3(x, y, z),
               new Vec3(Math.cos(angle) * 0.02D, 0.008D, Math.sin(angle) * 0.02D),
               0.06F + rand.nextFloat() * 0.03F, 22 + rand.nextInt(12), tint(rand));
         } else {
            level.addParticle(ParticleTypes.END_ROD, x, y, z,
               Math.cos(angle) * 0.01D, 0.004D, Math.sin(angle) * 0.01D);
         }
      }
   }

   private static void twinkle(Level level, RandomSource rand, Vec3 heart, int t) {
      for (int i = 0; i < 2; i++) {
         Vec3 at = heart.add((rand.nextDouble() - 0.5D) * 8.0D,
            (rand.nextDouble() - 0.25D) * 4.5D,
            (rand.nextDouble() - 0.5D) * 8.0D);
         EffectHelper.createFlareParticle(level, at.x, at.y, at.z, tint(rand));
      }
   }

   private static void climax(Level level, RandomSource rand, Vec3 heart, int age) {
      if (age == 0) {
         level.addParticle(ParticleTypes.FLASH, heart.x, heart.y, heart.z, 0.0D, 0.0D, 0.0D);
         EffectHelper.createFlareParticle(level, heart.x, heart.y, heart.z, Color.WHITE);
      }

      int count = 34 - age * 2;
      for (int i = 0; i < Math.max(6, count); i++) {
         Vec3 dir = randomDirection(rand);
         double speed = 0.22D + rand.nextDouble() * 0.2D;
         star(level, heart, dir.scale(speed), 0.11F + rand.nextFloat() * 0.07F,
            30 + rand.nextInt(25), tint(rand));
      }
      for (int i = 0; i < 8; i++) {
         Vec3 dir = randomDirection(rand).scale(0.3D);
         level.addParticle(ParticleTypes.FIREWORK, heart.x, heart.y, heart.z, dir.x, dir.y, dir.z);
      }
      if (age % 2 == 0) {
         ring(level, rand, heart, age * 3);
      }
   }

   private static Vec3 randomDirection(RandomSource rand) {
      double theta = rand.nextDouble() * Mth.TWO_PI;
      double phi = Math.acos(2.0D * rand.nextDouble() - 1.0D);
      return new Vec3(Math.sin(phi) * Math.cos(theta), Math.cos(phi) * 0.7D, Math.sin(phi) * Math.sin(theta));
   }
}
