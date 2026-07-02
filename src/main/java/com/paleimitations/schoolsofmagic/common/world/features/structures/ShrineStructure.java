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

public class ShrineStructure extends Structure {

   public static final Codec<ShrineStructure> CODEC = RecordCodecBuilder.create(in -> in.group(
      settingsCodec(in),
      Codec.STRING.fieldOf("element").forGetter(s -> s.element)
   ).apply(in, ShrineStructure::new));

   private final String element;

   public ShrineStructure(Structure.StructureSettings settings, String element) {
      super(settings);
      this.element = element;
   }

   @Override
   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      int x = chunkPos.getMinBlockX();
      int z = chunkPos.getMinBlockZ();

      BlockPos checkPos = new BlockPos(x, context.chunkGenerator().getSeaLevel(), z);
      return Optional.of(new GenerationStub(checkPos, builder -> {
         int ground = context.chunkGenerator()
            .getFirstOccupiedHeight(x + 8, z + 8, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
         BlockPos origin = new BlockPos(x, ground - 4, z);
         builder.addPiece(new ShrinePiece(context.structureTemplateManager(), this.element, origin));
      }));
   }

   @Override
   public StructureType<?> type() {
      return StructureRegistry.SHRINE.get();
   }
}
