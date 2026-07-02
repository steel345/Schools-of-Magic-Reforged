package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPodium;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class ShrinePiece extends TemplateStructurePiece {

   private final String element;

   public ShrinePiece(StructureTemplateManager manager, String element, BlockPos pos) {
      super(StructureRegistry.SHRINE_PIECE.get(), 0, manager, new ResourceLocation(SchoolsOfMagic.MODID, "mini_" + element),
         new ResourceLocation(SchoolsOfMagic.MODID, "mini_" + element).toString(), makeSettings(), pos);
      this.element = element;
   }

   public ShrinePiece(StructureTemplateManager manager, CompoundTag tag) {
      super(StructureRegistry.SHRINE_PIECE.get(), tag, manager, (Function<ResourceLocation, StructurePlaceSettings>) loc -> makeSettings());
      this.element = tag.getString("Element");
   }

   @Override
   protected void addAdditionalSaveData(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext ctx, CompoundTag tag) {
      super.addAdditionalSaveData(ctx, tag);
      tag.putString("Element", this.element);
   }

   private static StructurePlaceSettings makeSettings() {
      return new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE)
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
         .addProcessor(CloseDoorsProcessor.INSTANCE);
   }

   @Override
   public void postProcess(net.minecraft.world.level.WorldGenLevel level, net.minecraft.world.level.StructureManager sm,
                           net.minecraft.world.level.chunk.ChunkGenerator gen, RandomSource random,
                           BoundingBox box, net.minecraft.world.level.ChunkPos chunkPos, BlockPos pos) {
      super.postProcess(level, sm, gen, random, box, chunkPos, pos);
      net.minecraft.world.level.block.Block gem = gemFor(this.element);
      net.minecraft.world.level.block.Block cluster = clusterFor(this.element);
      BoundingBox bb = this.getBoundingBox();
      int x0 = Math.max(box.minX(), bb.minX()), x1 = Math.min(box.maxX(), bb.maxX());
      int y0 = Math.max(box.minY(), bb.minY()), y1 = Math.min(box.maxY(), bb.maxY());
      int z0 = Math.max(box.minZ(), bb.minZ()), z1 = Math.min(box.maxZ(), bb.maxZ());
      net.minecraft.core.BlockPos.MutableBlockPos m = new net.minecraft.core.BlockPos.MutableBlockPos();

      net.minecraft.world.level.block.state.BlockState cl = (gem != null && cluster != null) ? cluster.defaultBlockState()
         .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.FACING, Direction.UP)
         .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockGemCluster.AGE, 0) : null;
      for (int x = x0; x <= x1; x++) for (int y = y0; y <= y1; y++) for (int z = z0; z <= z1; z++) {
         m.set(x, y, z);
         net.minecraft.world.level.block.state.BlockState s = level.getBlockState(m);
         if (cl != null && s.is(gem)) { level.setBlock(m, cl, 2); continue; }

         if (s.getBlock() instanceof net.minecraft.world.level.block.DoorBlock
             && s.hasProperty(net.minecraft.world.level.block.DoorBlock.HALF)
             && s.getValue(net.minecraft.world.level.block.DoorBlock.HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER) {
            fixDoor(level, m.immutable(), s);
            continue;
         }

         closeDoor(level, m, s);
      }

      buryGrass(level, x0, y0, z0, x1, y1, z1);
      StructureFoundation.fill(level, x0, y0, z0, x1, y1, z1);
      desertSand(level, x0, y0, z0, x1, y1, z1);
   }

   private static void desertSand(net.minecraft.world.level.WorldGenLevel level, int x0, int y0, int z0, int x1, int y1, int z1) {
      BlockPos center = new BlockPos((x0 + x1) / 2, (y0 + y1) / 2, (z0 + z1) / 2);
      boolean desert = level.getBiome(center).unwrapKey()
         .map(k -> k.location().getPath().contains("desert")).orElse(false);
      if (!desert) return;
      net.minecraft.world.level.block.state.BlockState sand = Blocks.SAND.defaultBlockState();
      net.minecraft.core.BlockPos.MutableBlockPos m = new net.minecraft.core.BlockPos.MutableBlockPos();
      for (int x = x0; x <= x1; x++) for (int y = y0; y <= y1; y++) for (int z = z0; z <= z1; z++) {
         m.set(x, y, z);
         if (isGroundBlock(level.getBlockState(m))) level.setBlock(m, sand, 2);
      }
   }

   static void fixDoor(ServerLevelAccessor level, BlockPos lower, net.minecraft.world.level.block.state.BlockState lowerState) {
      net.minecraft.world.level.block.Block door = lowerState.getBlock();
      BlockPos upper = lower.above();
      net.minecraft.world.level.block.state.BlockState upperState = level.getBlockState(upper);
      if (upperState.getBlock() != door || !upperState.hasProperty(net.minecraft.world.level.block.DoorBlock.HALF)) return;

      Direction facing = lowerState.getValue(net.minecraft.world.level.block.DoorBlock.FACING);
      net.minecraft.world.level.block.state.properties.DoorHingeSide hinge = upperState.getValue(net.minecraft.world.level.block.DoorBlock.HINGE);

      level.setBlock(lower, lowerState
         .setValue(net.minecraft.world.level.block.DoorBlock.FACING, facing)
         .setValue(net.minecraft.world.level.block.DoorBlock.OPEN, false)
         .setValue(net.minecraft.world.level.block.DoorBlock.HINGE, hinge)
         .setValue(net.minecraft.world.level.block.DoorBlock.POWERED, false)
         .setValue(net.minecraft.world.level.block.DoorBlock.HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER), 2);
      level.setBlock(upper, upperState
         .setValue(net.minecraft.world.level.block.DoorBlock.FACING, facing)
         .setValue(net.minecraft.world.level.block.DoorBlock.OPEN, false)
         .setValue(net.minecraft.world.level.block.DoorBlock.HINGE, hinge)
         .setValue(net.minecraft.world.level.block.DoorBlock.POWERED, false)
         .setValue(net.minecraft.world.level.block.DoorBlock.HALF, net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER), 2);
   }

   private static boolean isGroundBlock(net.minecraft.world.level.block.state.BlockState s) {
      return s.is(Blocks.GRASS_BLOCK) || s.is(Blocks.DIRT) || s.is(Blocks.COARSE_DIRT)
          || s.is(Blocks.PODZOL) || s.is(Blocks.DIRT_PATH) || s.is(Blocks.MYCELIUM) || s.is(Blocks.ROOTED_DIRT);
   }

   private static void buryGrass(ServerLevelAccessor level, int x0, int y0, int z0, int x1, int y1, int z1) {
      java.util.List<BlockPos> caps = new java.util.ArrayList<>();
      for (int x = x0; x <= x1; x++) for (int z = z0; z <= z1; z++) for (int y = y1; y >= y0; y--) {
         BlockPos p = new BlockPos(x, y, z);
         net.minecraft.world.level.block.state.BlockState s = level.getBlockState(p);
         if (s.isAir()) continue;
         if (s.getBlock() instanceof net.minecraft.world.level.block.DoorBlock) continue;
         if (level.getBlockState(p.above()).getBlock() instanceof net.minecraft.world.level.block.DoorBlock) continue;

         boolean qualifies = isGroundBlock(s);
         if (!qualifies) {
            for (Direction d : Direction.values()) {
               int nx = x + d.getStepX(), ny = y + d.getStepY(), nz = z + d.getStepZ();
               if (nx < x0 || nx > x1 || ny < y0 || ny > y1 || nz < z0 || nz > z1) continue;
               if (isGroundBlock(level.getBlockState(new BlockPos(nx, ny, nz)))) { qualifies = true; break; }
            }
         }
         if (!qualifies) continue;

         boolean exposed = true;
         for (int yy = y + 1; yy <= y1; yy++) {
            if (!level.getBlockState(new BlockPos(x, yy, z)).isAir()) { exposed = false; break; }
         }
         if (!exposed) continue;

         caps.add(p);
      }

      for (BlockPos p : caps) {
         level.setBlock(p.above(), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
         if (level.getBlockState(p).is(Blocks.GRASS_BLOCK)) {
            level.setBlock(p, Blocks.DIRT.defaultBlockState(), 2);
         }
      }
   }

   static void closeDoor(ServerLevelAccessor level, BlockPos pos, net.minecraft.world.level.block.state.BlockState s) {
      if (s.getBlock() instanceof net.minecraft.world.level.block.DoorBlock
          && s.hasProperty(net.minecraft.world.level.block.DoorBlock.OPEN)
          && s.getValue(net.minecraft.world.level.block.DoorBlock.OPEN)) {
         level.setBlock(pos, s.setValue(net.minecraft.world.level.block.DoorBlock.OPEN, false), 2);
      }
   }

   @Override
   protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
      if (name.equals("chest")) {
         level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH), 2);
         BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof RandomizableContainerBlockEntity c) {
            c.setLootTable(BuiltInLootTables.STRONGHOLD_CROSSING, random.nextLong());
         }
         return;
      }
      if (name.equals("podium")) {
         level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
         EnumWoodType wood = woodFor(this.element);

         recolourPodium(level, pos.below(), wood);
         for (Direction d : Direction.Plane.HORIZONTAL) {
            recolourPodium(level, pos.below().relative(d), wood);
         }
         return;
      }
      if (name.equals("enemy")) {
         level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
         spawnEnemies(level, pos, random);
         return;
      }

      level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
   }

   private static void recolourPodium(ServerLevelAccessor level, BlockPos pos, EnumWoodType wood) {
      BlockState s = level.getBlockState(pos);
      if (s.getBlock() instanceof BlockPodium) {
         level.setBlock(pos, s.setValue(BlockPodium.TYPE, wood), 2);

         net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
         if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium tp) {
            tp.setWood(wood);
            tp.setChanged();
         }
      }
   }

   private static EnumWoodType woodFor(String element) {
      switch (element) {
         case "pyromancy":     return EnumWoodType.OAK;
         case "electromancy":  return EnumWoodType.ACACIA;
         case "aeromancy":
         case "auramancy":     return EnumWoodType.DARK_OAK;
         case "geomancy":
         case "spectromancy":  return EnumWoodType.WILLOW;
         case "animancy":      return EnumWoodType.JUNGLE;
         case "hydromancy":    return EnumWoodType.BIRCH;
         case "cryomancy":     return EnumWoodType.SPRUCE;
         case "hieromancy":
         case "umbramancy":    return EnumWoodType.YEW;
         case "heliomancy":
         case "chaotics":      return EnumWoodType.VERDE;
         case "astromancy":    return EnumWoodType.ELDER;
         case "infernality":   return EnumWoodType.ASH;
         case "necromancy":    return EnumWoodType.PINE;
         default:              return EnumWoodType.OAK;
      }
   }

   private static net.minecraft.world.level.block.Block gemFor(String element) {
      switch (element) {
         case "pyromancy":    return BlockRegistry.gem_pyromancy.get();
         case "heliomancy":   return BlockRegistry.gem_heliomancy.get();
         case "aeromancy":    return BlockRegistry.gem_aeromancy.get();
         case "geomancy":     return BlockRegistry.gem_geomancy.get();
         case "animancy":     return BlockRegistry.gem_animancy.get();
         case "electromancy": return BlockRegistry.gem_electromancy.get();
         case "hydromancy":   return BlockRegistry.gem_hydromancy.get();
         case "cryomancy":    return BlockRegistry.gem_cryomancy.get();
         case "hieromancy":   return BlockRegistry.gem_hieromancy.get();
         case "chaotics":     return BlockRegistry.gem_chaotics.get();
         case "auramancy":    return BlockRegistry.gem_auramancy.get();
         case "astromancy":   return BlockRegistry.gem_astromancy.get();
         case "infernality":  return BlockRegistry.gem_infernality.get();
         case "spectromancy": return BlockRegistry.gem_spectromancy.get();
         case "umbramancy":   return BlockRegistry.gem_umbramancy.get();
         case "necromancy":   return BlockRegistry.gem_necromancy.get();
         default:             return null;
      }
   }

   private static net.minecraft.world.level.block.Block clusterFor(String element) {
      switch (element) {
         case "pyromancy":    return BlockRegistry.crystal_ruby.get();
         case "heliomancy":   return BlockRegistry.crystal_sunstone.get();
         case "aeromancy":    return BlockRegistry.crystal_citrine.get();
         case "geomancy":     return BlockRegistry.crystal_peridot.get();
         case "animancy":     return BlockRegistry.crystal_jade.get();
         case "electromancy": return BlockRegistry.crystal_turquoise.get();
         case "hydromancy":   return BlockRegistry.crystal_aquamarine.get();
         case "cryomancy":    return BlockRegistry.crystal_sapphire.get();
         case "hieromancy":   return BlockRegistry.crystal_amethyst.get();
         case "chaotics":     return BlockRegistry.crystal_garnet.get();
         case "auramancy":    return BlockRegistry.crystal_rose_quartz.get();
         case "astromancy":   return BlockRegistry.crystal_moonstone.get();
         case "infernality":  return BlockRegistry.crystal_putridite.get();
         case "spectromancy": return BlockRegistry.crystal_opal.get();
         case "umbramancy":   return BlockRegistry.crystal_onyx.get();
         case "necromancy":   return BlockRegistry.crystal_smoky_quartz.get();
         default:             return null;
      }
   }

   private void spawnEnemies(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
      ServerLevel server = level.getLevel();
      int pick = random.nextInt(3);
      EntityType<?> type = (pick == 0) ? EntityType.WITCH : (pick == 1) ? EntityType.EVOKER : EntityType.ILLUSIONER;
      net.minecraft.world.entity.Entity e = type.create(server);

      if (e instanceof Mob magician) {
         magician.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
         magician.setCustomName(net.minecraft.network.chat.Component.literal(MagicianNames.random(random)));
         magician.setCustomNameVisible(true);
         magician.setPersistenceRequired();
         if (pick == 0) {
            magician.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
            magician.addEffect(new MobEffectInstance(MobEffects.REGENERATION, Integer.MAX_VALUE, 0, false, false));
         }
         level.addFreshEntity(magician);
      }

      EntityType<?> minion = null;
      int count = 1;
      switch (this.element) {
         case "pyromancy":    minion = EntityType.MAGMA_CUBE; count = 1; break;
         case "heliomancy":   minion = EntityType.HUSK; count = 2; break;
         case "aeromancy":    minion = EntityType.BAT; count = 2 + random.nextInt(3); break;
         case "geomancy":     minion = EntityType.CAVE_SPIDER; count = 1 + random.nextInt(3); break;
         case "animancy":     minion = EntityType.WOLF; count = 1 + random.nextInt(3); break;
         case "hydromancy":   minion = EntityType.SLIME; count = 1; break;
         case "cryomancy":    minion = EntityType.STRAY; count = 1; break;
         case "chaotics":     minion = EntityType.ENDERMAN; count = 1; break;
         case "auramancy":    minion = EntityType.HUSK; count = 1; break;
         case "infernality":  minion = EntityType.ZOMBIFIED_PIGLIN; count = 1 + random.nextInt(3); break;
         case "umbramancy":   minion = EntityType.SPIDER; count = 1 + random.nextInt(3); break;
         case "necromancy":   minion = EntityType.WITHER_SKELETON; count = 1 + random.nextInt(3); break;
         default:             minion = null; break;
      }
      if (minion != null) {
         for (int i = 0; i < count; i++) spawn(server, level, minion, pos);
      }
   }

   private static Mob spawn(ServerLevel server, ServerLevelAccessor level, EntityType<?> type, BlockPos pos) {
      Entity e = type.create(server);
      if (!(e instanceof Mob m)) return null;
      m.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0F, 0.0F);
      m.setPersistenceRequired();
      level.addFreshEntity(m);
      return m;
   }
}
