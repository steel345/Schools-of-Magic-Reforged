package com.paleimitations.schoolsofmagic.client.particles;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public enum SOMParticleType {
   FLOWER("flower", 0, false),
   LEAF("leaf", 1, false),
   BUG("bug", 2, false),
   FLAME("flame", 3, false),
   FIRE_RING("fire_ring", 4, false),
   WATER("water", 5, false),
   FIRE_PLUME("fire_plume", 6, false),
   EMBER("ember", 7, false),
   SNOW("snow", 8, false),
   AIR("air", 9, false),
   SNORE("snore", 10, false);

   private final String particleName;
   private final int particleID;
   private final boolean shouldIgnoreRange;
   private final int argumentCount;
   private static final Map<Integer, SOMParticleType> PARTICLES = new HashMap<>();
   private static final Map<String, SOMParticleType> BY_NAME = new HashMap<>();

   SOMParticleType(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn, int argumentCountIn) {
      this.particleName = particleNameIn;
      this.particleID = particleIDIn;
      this.shouldIgnoreRange = shouldIgnoreRangeIn;
      this.argumentCount = argumentCountIn;
   }

   SOMParticleType(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn) {
      this(particleNameIn, particleIDIn, shouldIgnoreRangeIn, 0);
   }

   public static Set<String> getParticleNames() {
      return BY_NAME.keySet();
   }

   public String getParticleName() {
      return this.particleName;
   }

   public int getParticleID() {
      return this.particleID;
   }

   public int getArgumentCount() {
      return this.argumentCount;
   }

   public boolean getShouldIgnoreRange() {
      return this.shouldIgnoreRange;
   }

   @Nullable
   public static SOMParticleType getParticleFromId(int particleId) {
      return PARTICLES.get(particleId);
   }

   @Nullable
   public static SOMParticleType getByName(String nameIn) {
      return BY_NAME.get(nameIn);
   }

   static {
      for (SOMParticleType t : SOMParticleType.values()) {
         PARTICLES.put(t.getParticleID(), t);
         BY_NAME.put(t.getParticleName(), t);
      }
   }
}
