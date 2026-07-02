package com.paleimitations.schoolsofmagic.common.world.dimensions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

public class FaeGroveBiomeSource extends BiomeSource {

   public static final Codec<FaeGroveBiomeSource> CODEC = RecordCodecBuilder.create(i -> i.group(
      Biome.CODEC.fieldOf("labyrinth").forGetter(s -> s.labyrinth),
      Biome.CODEC.fieldOf("acolyte").forGetter(s -> s.acolyte),
      Biome.CODEC.listOf().fieldOf("others").forGetter(s -> s.others)
   ).apply(i, FaeGroveBiomeSource::new));

   private final Holder<Biome> labyrinth;
   private final Holder<Biome> acolyte;
   private final List<Holder<Biome>> others;

   private static final int CELL = 384;
   private static final int CORE_R = 88;
   private static final int RING_R = 150;

   public FaeGroveBiomeSource(Holder<Biome> labyrinth, Holder<Biome> acolyte, List<Holder<Biome>> others) {
      this.labyrinth = labyrinth;
      this.acolyte = acolyte;
      this.others = others;
   }

   @Override
   protected Codec<? extends BiomeSource> codec() {
      return CODEC;
   }

   @Override
   protected Stream<Holder<Biome>> collectPossibleBiomes() {
      List<Holder<Biome>> all = new ArrayList<>(this.others);
      all.add(this.labyrinth);
      all.add(this.acolyte);
      return all.stream();
   }

   private static long hash(int cx, int cz) {
      long h = ((long) cx * 341873128712L) ^ ((long) cz * 132897987541L) ^ 0x5FAE6406L;
      h ^= (h >>> 33); h *= 0xff51afd7ed558ccdL; h ^= (h >>> 33);
      return h & Long.MAX_VALUE;
   }

   @Override
   public Holder<Biome> getNoiseBiome(int qx, int qy, int qz, Climate.Sampler sampler) {
      int bx = qx << 2, bz = qz << 2;
      int cx = Math.floorDiv(bx, CELL), cz = Math.floorDiv(bz, CELL);
      if (hash(cx, cz) % 6L == 0L) {
         double centerX = (double) cx * CELL + CELL / 2.0;
         double centerZ = (double) cz * CELL + CELL / 2.0;
         double dx = bx - centerX, dz = bz - centerZ;
         double d = Math.sqrt(dx * dx + dz * dz);
         if (d < CORE_R) return this.labyrinth;
         if (d < RING_R) return this.acolyte;
      }
      if (this.others.isEmpty()) return this.labyrinth;
      Climate.TargetPoint t = sampler.sample(qx, qy, qz);
      double temp = Climate.unquantizeCoord(t.temperature());
      int idx = (int) Math.floor((temp + 1.0) / 2.0 * this.others.size());
      if (idx < 0) idx = 0;
      if (idx >= this.others.size()) idx = this.others.size() - 1;
      return this.others.get(idx);
   }
}
