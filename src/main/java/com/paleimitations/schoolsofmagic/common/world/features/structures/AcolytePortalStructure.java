package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class AcolytePortalStructure extends Structure {

   public static final Codec<AcolytePortalStructure> CODEC = RecordCodecBuilder.create(in -> in.group(
      settingsCodec(in)
   ).apply(in, AcolytePortalStructure::new));

   public AcolytePortalStructure(Structure.StructureSettings settings) {
      super(settings);
   }

   @Override
   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      int x = chunkPos.getMinBlockX() + 4;
      int z = chunkPos.getMinBlockZ() + 4;

      BlockPos checkPos = new BlockPos(x, context.chunkGenerator().getSeaLevel(), z);
      return Optional.of(new GenerationStub(checkPos, builder -> {
         int ground = sample(context, x + 3, z + 5);
         BlockPos origin = new BlockPos(x, ground, z);
         builder.addPiece(new AcolytePortalPiece(context.structureTemplateManager(), origin));
      }));
   }

   private static int sample(GenerationContext context, int x, int z) {
      return context.chunkGenerator().getFirstOccupiedHeight(
         x, z, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
   }

   @Override
   public StructureType<?> type() {
      return StructureRegistry.ACOLYTE_PORTAL.get();
   }
}
