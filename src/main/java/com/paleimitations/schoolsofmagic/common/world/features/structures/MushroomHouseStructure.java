package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class MushroomHouseStructure extends Structure {

   public static final Codec<MushroomHouseStructure> CODEC = RecordCodecBuilder.create(in -> in.group(
      settingsCodec(in),
      ResourceLocation.CODEC.fieldOf("template").forGetter(s -> s.template)
   ).apply(in, MushroomHouseStructure::new));

   private final ResourceLocation template;

   public MushroomHouseStructure(Structure.StructureSettings settings, ResourceLocation template) {
      super(settings);
      this.template = template;
   }

   @Override
   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      int x = chunkPos.getMinBlockX() + 4;
      int z = chunkPos.getMinBlockZ() + 4;

      BlockPos checkPos = new BlockPos(x, context.chunkGenerator().getSeaLevel(), z);
      return Optional.of(new GenerationStub(checkPos, builder -> {
         int ground = context.chunkGenerator().getFirstOccupiedHeight(
            x + 12, z + 13, Heightmap.Types.OCEAN_FLOOR_WG, context.heightAccessor(), context.randomState());
         BlockPos origin = new BlockPos(x, ground - 1, z);
         builder.addPiece(new MushroomHousePiece(context.structureTemplateManager(), this.template, origin));
      }));
   }

   @Override
   public StructureType<?> type() {
      return StructureRegistry.MUSHROOM_HOUSE.get();
   }
}
