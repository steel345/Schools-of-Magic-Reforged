package com.paleimitations.schoolsofmagic.common.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class PlasmaParticleOptions implements ParticleOptions {
   public static final Codec<PlasmaParticleOptions> CODEC = RecordCodecBuilder.create(inst -> inst.group(
      Codec.FLOAT.fieldOf("r").forGetter(o -> o.r),
      Codec.FLOAT.fieldOf("g").forGetter(o -> o.g),
      Codec.FLOAT.fieldOf("b").forGetter(o -> o.b),
      Codec.FLOAT.fieldOf("scale").forGetter(o -> o.scale)
   ).apply(inst, PlasmaParticleOptions::new));

   @SuppressWarnings("deprecation")
   public static final ParticleOptions.Deserializer<PlasmaParticleOptions> DESERIALIZER =
      new ParticleOptions.Deserializer<PlasmaParticleOptions>() {
         @Override
         public PlasmaParticleOptions fromCommand(ParticleType<PlasmaParticleOptions> type, StringReader reader)
               throws CommandSyntaxException {
            reader.expect(' '); float r = reader.readFloat();
            reader.expect(' '); float g = reader.readFloat();
            reader.expect(' '); float b = reader.readFloat();
            reader.expect(' '); float s = reader.readFloat();
            return new PlasmaParticleOptions(r, g, b, s);
         }

         @Override
         public PlasmaParticleOptions fromNetwork(ParticleType<PlasmaParticleOptions> type, FriendlyByteBuf buf) {
            return new PlasmaParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
         }
      };

   public final float r;
   public final float g;
   public final float b;
   public final float scale;

   public PlasmaParticleOptions(float r, float g, float b, float scale) {
      this.r = r;
      this.g = g;
      this.b = b;
      this.scale = scale;
   }

   @Override
   public ParticleType<?> getType() {
      return ParticleTypeRegistry.PLASMA.get();
   }

   @Override
   public void writeToNetwork(FriendlyByteBuf buf) {
      buf.writeFloat(this.r);
      buf.writeFloat(this.g);
      buf.writeFloat(this.b);
      buf.writeFloat(this.scale);
   }

   @Override
   public String writeToString() {
      return String.format(java.util.Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
         net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
         this.r, this.g, this.b, this.scale);
   }
}
