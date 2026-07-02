package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class AcolytePortalPiece extends TemplateStructurePiece {

   public AcolytePortalPiece(StructureTemplateManager manager, BlockPos pos) {
      super(StructureRegistry.ACOLYTE_PORTAL_PIECE.get(), 0, manager,
         new ResourceLocation(SchoolsOfMagic.MODID, "acolyte_portal"),
         new ResourceLocation(SchoolsOfMagic.MODID, "acolyte_portal").toString(), makeSettings(), pos);
   }

   public AcolytePortalPiece(StructureTemplateManager manager, CompoundTag tag) {
      super(StructureRegistry.ACOLYTE_PORTAL_PIECE.get(), tag, manager,
         (Function<ResourceLocation, StructurePlaceSettings>) loc -> makeSettings());
   }

   private static StructurePlaceSettings makeSettings() {
      return new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE)
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
   }

   @Override
   protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
      super.addAdditionalSaveData(ctx, tag);
   }

   @Override
   protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
   }

   @Override
   public void postProcess(WorldGenLevel level, net.minecraft.world.level.StructureManager sm,
                           net.minecraft.world.level.chunk.ChunkGenerator gen, RandomSource random,
                           BoundingBox box, net.minecraft.world.level.ChunkPos chunkPos, BlockPos pos) {
      super.postProcess(level, sm, gen, random, box, chunkPos, pos);

      BlockState wood = BlockRegistry.wood_ash.get().defaultBlockState();
      BoundingBox bb = this.getBoundingBox();
      int x0 = Math.max(box.minX(), bb.minX()), x1 = Math.min(box.maxX(), bb.maxX());
      int y0 = Math.max(box.minY(), bb.minY()), y1 = Math.min(box.maxY(), bb.maxY());
      int z0 = Math.max(box.minZ(), bb.minZ()), z1 = Math.min(box.maxZ(), bb.maxZ());
      BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos();
      for (int x = x0; x <= x1; x++) {
         for (int y = y0; y <= y1; y++) {
            for (int z = z0; z <= z1; z++) {
               m.set(x, y, z);
               BlockState s = level.getBlockState(m);
               if (s.is(BlockRegistry.log_ash.get()) || s.is(BlockRegistry.stripped_log_ash.get())) {
                  level.setBlock(m, wood, 2);
               }
            }
         }
      }

      StructureFoundation.fill(level, x0, y0, z0, x1, y1, z1);
   }
}
