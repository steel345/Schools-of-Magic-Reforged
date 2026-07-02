package com.paleimitations.schoolsofmagic.common.particles;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class PlasmaParticleType extends ParticleType<PlasmaParticleOptions> {
   public PlasmaParticleType() {
      super(false, PlasmaParticleOptions.DESERIALIZER);
   }

   @Override
   public Codec<PlasmaParticleOptions> codec() {
      return PlasmaParticleOptions.CODEC;
   }
}
