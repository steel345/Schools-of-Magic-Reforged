package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class MushroomHousePiece extends TemplateStructurePiece {

   public MushroomHousePiece(StructureTemplateManager manager, ResourceLocation template, BlockPos pos) {
      super(StructureRegistry.MUSHROOM_HOUSE_PIECE.get(), 0, manager, template, template.toString(), makeSettings(), pos);
   }

   public MushroomHousePiece(StructureTemplateManager manager, CompoundTag tag) {
      super(StructureRegistry.MUSHROOM_HOUSE_PIECE.get(), tag, manager,
         (Function<ResourceLocation, StructurePlaceSettings>) loc -> makeSettings());
   }

   private static StructurePlaceSettings makeSettings() {
      return new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE)
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
   }

   @Override
   protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
   }

   @Override
   public void postProcess(net.minecraft.world.level.WorldGenLevel level, net.minecraft.world.level.StructureManager sm,
                           net.minecraft.world.level.chunk.ChunkGenerator gen, RandomSource random,
                           BoundingBox box, net.minecraft.world.level.ChunkPos chunkPos, BlockPos pos) {
      super.postProcess(level, sm, gen, random, box, chunkPos, pos);
      BoundingBox bb = this.getBoundingBox();
      int x0 = Math.max(box.minX(), bb.minX()), x1 = Math.min(box.maxX(), bb.maxX());
      int y0 = Math.max(box.minY(), bb.minY()), y1 = Math.min(box.maxY(), bb.maxY());
      int z0 = Math.max(box.minZ(), bb.minZ()), z1 = Math.min(box.maxZ(), bb.maxZ());
      StructureFoundation.fill(level, x0, y0, z0, x1, y1, z1);
      spawnResidents(level, box, random);
   }

   private void spawnResidents(net.minecraft.world.level.WorldGenLevel level, BoundingBox box, RandomSource random) {
      BoundingBox bb = this.getBoundingBox();
      int cx = (bb.minX() + bb.maxX()) / 2;
      int cz = (bb.minZ() + bb.maxZ()) / 2;
      if (cx < box.minX() || cx > box.maxX() || cz < box.minZ() || cz > box.maxZ()) return;

      BlockPos spot = findSecondFloor(level, bb, box, cx, cz);
      if (spot == null) return;

      net.minecraft.server.level.ServerLevel server = level.getLevel();
      boolean evermore = this.templateName != null && this.templateName.contains("evermore");

      net.minecraft.world.entity.EntityType<?> type = evermore
         ? net.minecraft.world.entity.EntityType.EVOKER
         : net.minecraft.world.entity.EntityType.WITCH;
      net.minecraft.world.entity.Entity e = type.create(server);
      if (e instanceof net.minecraft.world.entity.Mob magician) {
         magician.moveTo(spot.getX() + 0.5D, spot.getY(), spot.getZ() + 0.5D, random.nextFloat() * 360.0F, 0.0F);
         magician.setCustomName(net.minecraft.network.chat.Component.literal(MagicianNames.random(random)));
         magician.setCustomNameVisible(true);
         magician.setPersistenceRequired();
         magician.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
         magician.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.REGENERATION, Integer.MAX_VALUE, 0, false, false));
         level.addFreshEntity(magician);
      }

      net.minecraft.world.entity.EntityType<?> servantType = net.minecraft.world.entity.EntityType.HUSK;
      {
         net.minecraft.world.entity.Entity s = servantType.create(server);
         if (s instanceof net.minecraft.world.entity.Mob servant) {
            servant.moveTo(spot.getX() + 0.5D, spot.getY(), spot.getZ() + 1.0D, random.nextFloat() * 360.0F, 0.0F);
            servant.setCustomName(net.minecraft.network.chat.Component.literal("Servant"));
            servant.setPersistenceRequired();
            level.addFreshEntity(servant);
         }
      }
   }

   private static BlockPos findSecondFloor(net.minecraft.world.level.WorldGenLevel level, BoundingBox bb, BoundingBox box, int cx, int cz) {
      for (int y = bb.minY() + 10; y >= bb.minY() + 5; y--) {
         for (int r = 0; r <= 4; r++) {
            for (int dx = -r; dx <= r; dx++) {
               for (int dz = -r; dz <= r; dz++) {
                  int x = cx + dx, z = cz + dz;
                  if (x < box.minX() || x > box.maxX() || z < box.minZ() || z > box.maxZ()) continue;
                  BlockPos p = new BlockPos(x, y, z);
                  if (level.getBlockState(p).isAir() && level.getBlockState(p.above()).isAir()
                        && level.getBlockState(p.below()).isFaceSturdy(level, p.below(), net.minecraft.core.Direction.UP)) {
                     return p;
                  }
               }
            }
         }
      }
      return null;
   }
}
