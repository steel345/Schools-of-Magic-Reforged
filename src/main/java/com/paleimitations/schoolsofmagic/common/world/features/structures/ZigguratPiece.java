package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class ZigguratPiece extends TemplateStructurePiece {

   private final long zigSeed;
   private final boolean foundation;
   private final Rotation rotation;

   private BlockPos tabletPos = null;
   private BlockPos altarPos = null;

   private final BlockPos altarLink;
   private final BlockPos tabletLink;

   public ZigguratPiece(StructureTemplateManager manager, String templateId, BlockPos pos, Rotation rotation, long zigSeed,
                        BlockPos altarLink, BlockPos tabletLink) {
      super(StructureRegistry.ZIGGURAT_PIECE.get(), 0, manager, new ResourceLocation(SchoolsOfMagic.MODID, templateId), new ResourceLocation(SchoolsOfMagic.MODID, templateId).toString(), makeSettings(rotation), pos);
      this.zigSeed = zigSeed;
      this.rotation = rotation;
      this.foundation = templateId.endsWith("_l1");
      this.altarLink = altarLink;
      this.tabletLink = tabletLink;
   }

   public ZigguratPiece(StructureTemplateManager manager, CompoundTag tag) {
      super(StructureRegistry.ZIGGURAT_PIECE.get(), tag, manager,
         (Function<ResourceLocation, StructurePlaceSettings>) loc -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
      this.zigSeed = tag.getLong("ZigSeed");
      this.foundation = tag.getBoolean("Foundation");
      this.rotation = Rotation.valueOf(tag.getString("Rot"));
      this.altarLink = tag.contains("AltarLink") ? BlockPos.of(tag.getLong("AltarLink")) : null;
      this.tabletLink = tag.contains("TabletLink") ? BlockPos.of(tag.getLong("TabletLink")) : null;
   }

   @Override
   protected void addAdditionalSaveData(net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext ctx, CompoundTag tag) {
      super.addAdditionalSaveData(ctx, tag);
      tag.putLong("ZigSeed", this.zigSeed);
      tag.putBoolean("Foundation", this.foundation);
      tag.putString("Rot", this.rotation.name());
      if (this.altarLink != null) tag.putLong("AltarLink", this.altarLink.asLong());
      if (this.tabletLink != null) tag.putLong("TabletLink", this.tabletLink.asLong());
   }

   static StructurePlaceSettings makeSettings(Rotation rotation) {

      return new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(rotation)
         .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
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
      net.minecraft.core.BlockPos.MutableBlockPos m = new net.minecraft.core.BlockPos.MutableBlockPos();
      for (int x = x0; x <= x1; x++) for (int y = y0; y <= y1; y++) for (int z = z0; z <= z1; z++) {
         m.set(x, y, z);
         net.minecraft.world.level.block.state.BlockState s = level.getBlockState(m);

         if (s.getBlock() == Blocks.FIRE) {
            level.setBlock(m, BlockRegistry.phantom_fire.get().defaultBlockState(), 2);
            continue;
         }

         if (s.getBlock() == BlockRegistry.crystal_turquoise.get()) {
            level.setBlock(m, BlockRegistry.gem_chunk_block.get().defaultBlockState()
               .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockMagicGemBlock.TYPE,
                  com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.ELECTROMANCY), 2);
            continue;
         }

         if (s.getBlock() == BlockRegistry.zig_mural.get()) {

            int xv = Math.max(1, Math.min(11, 18 - (z - bb.minZ())));
            int yv = this.foundation ? 6 : Math.max(1, Math.min(6, 5 - (y - bb.minY())));
            level.setBlock(m, s.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockZigMural.X, xv)
               .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockZigMural.Y, yv), 2);
         }

         if (s.getBlock() == BlockRegistry.rotted_chest.get() || s.getBlock() == Blocks.CHEST) {
            if (level.getBlockEntity(m) instanceof RandomizableContainerBlockEntity c) {
               c.setLootTable(s.getBlock() == BlockRegistry.rotted_chest.get() ? ROTTED_CHEST_LOOT : BuiltInLootTables.SIMPLE_DUNGEON, random.nextLong());
               c.setChanged();
            }
         }
      }

      if (this.foundation) {
         net.minecraft.world.level.block.state.BlockState SAND = Blocks.SANDSTONE.defaultBlockState();
         int yFloor = Math.max(level.getMinBuildHeight() + 1, bb.minY() - 48);
         for (int x = x0; x <= x1; x++) for (int z = z0; z <= z1; z++) {
            for (int y = bb.minY() - 1; y >= yFloor; y--) {
               m.set(x, y, z);
               net.minecraft.world.level.block.state.BlockState below = level.getBlockState(m);
               if (below.isAir() || !below.getFluidState().isEmpty()) {
                  level.setBlock(m, SAND, 2);
               } else {
                  break;
               }
            }
         }
      }

      if (this.altarPos != null && this.tabletLink != null
            && level.getBlockEntity(this.altarPos) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySacrificialAltar al) {
         al.setTabletPos(this.tabletLink); al.setChanged();
      }
      if (this.tabletPos != null && this.altarLink != null
            && level.getBlockEntity(this.tabletPos) instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySandstoneTablet tb) {
         tb.setAltarPos(this.altarLink); tb.setChanged();
      }
   }

   @Override
   protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {

      if (name.equals("treasure")) {
         level.setBlock(pos.below(), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
         level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 2);
         if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity c) {
            c.setLootTable(BuiltInLootTables.END_CITY_TREASURE, random.nextLong());
         }
         return;
      }

      if (name.startsWith("challenge_") || name.equals("treasure_room")
          || name.equals("ew_hall") || name.equals("ns_hall")) {
         level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
         return;
      }

      if (name.equals("chest") || name.equals("maze_chest") || name.startsWith("loot_")
          || name.startsWith("small_vase") || name.startsWith("large_vase") || name.startsWith("small_jar") || name.startsWith("large_jar")) {
         placeLoot(level, pos, random, name.contains("large")); return;
      }

      if (name.equals("wolves")) { mobs(level, pos, random, EntityType.WOLF, 2, 3, null); return; }
      if (name.equals("witches")) { mobs(level, pos, random, EntityType.WITCH, 1, 2, null); return; }
      if (name.equals("slimes")) { mobs(level, pos, random, EntityType.SLIME, 2, 3, null); return; }
      if (name.equals("invisible_spiders")) { mobs(level, pos, random, EntityType.SPIDER, 2, 4, MobEffects.INVISIBILITY); return; }
      if (name.equals("magma_pit")) { mobs(level, pos, random, EntityType.MAGMA_CUBE, 2, 3, null); return; }
      if (name.equals("pig")) { mobs(level, pos, random, EntityType.PIG, 1, 1, null); return; }
      if (name.equals("cow")) { mobs(level, pos, random, EntityType.COW, 1, 1, null); return; }
      if (name.equals("llama")) { mobs(level, pos, random, EntityType.LLAMA, 1, 1, null); return; }
      if (name.equals("horse")) { mobs(level, pos, random, EntityType.HORSE, 1, 1, null); return; }
      if (name.equals("fish")) { mobs(level, pos, random, EntityType.GUARDIAN, 1, 1, null); return; }
      if (name.equals("room")) {
         mobs(level, pos, random, EntityType.HUSK, 1, 1, null);
         mobs(level, pos, random, EntityType.SKELETON, 1, 1, null);
         mobs(level, pos, random, EntityType.SPIDER, 1, 1, null);
         mobs(level, pos, random, EntityType.VINDICATOR, 1, 1, null);
         mobs(level, pos, random, EntityType.ZOMBIE, 1, 1, null);
         return;
      }

      if (name.equals("brewing_stand")) { level.setBlock(pos, Blocks.BREWING_STAND.defaultBlockState(), 2); return; }
      if (name.startsWith("spear_")) {

         net.minecraft.core.Direction f;
         switch (name.substring(6)) {
            case "n": f = net.minecraft.core.Direction.NORTH; break;
            case "s": f = net.minecraft.core.Direction.SOUTH; break;
            case "e": f = net.minecraft.core.Direction.EAST;  break;
            case "w": f = net.minecraft.core.Direction.WEST;  break;
            case "d": case "down": f = net.minecraft.core.Direction.DOWN; break;
            default:  f = net.minecraft.core.Direction.UP;    break;
         }
         level.setBlock(pos, BlockRegistry.trap_spike_base.get().defaultBlockState()
            .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockTrapSpikeBase.FACING, f), 2);
         return;
      }
      if (name.startsWith("dark_crystal")) {

         level.setBlock(pos, BlockRegistry.dark_crystal.get().defaultBlockState()
            .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.TYPE, curseFor(name)), 2);
         return;
      }

      if (name.equals("grave")) { placeLoot(level, pos, random, false); return; }

      if (name.equals("podium")) { placeArcanaPedestal(level, pos); return; }

      if (name.startsWith("podium")) { placePodium(level, pos, random); return; }

      if (name.equals("altar")) {
         net.minecraft.core.Direction f = net.minecraft.core.Direction.WEST;
         net.minecraft.world.level.block.state.BlockState centre = BlockRegistry.sacrificial_altar.get().defaultBlockState()
            .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.FACING, f)
            .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.PART, com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.Part.CENTER);
         level.setBlock(pos, centre, 2);
         level.setBlock(pos.relative(f.getClockWise()), centre.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.PART, com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.Part.LEFT), 2);
         level.setBlock(pos.relative(f.getCounterClockWise()), centre.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.PART, com.paleimitations.schoolsofmagic.common.blocks.BlockSacrificialAltar.Part.RIGHT), 2);
         this.altarPos = pos.immutable();
         return;
      }

      if (name.equals("tablet")) {
         level.setBlock(pos, BlockRegistry.sandstone_tablet.get().defaultBlockState()
            .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockSandstoneTablet.FACING, net.minecraft.core.Direction.WEST), 2);
         this.tabletPos = pos.immutable();
         return;
      }

      if (name.equals("heart")) {
         level.setBlock(pos, BlockRegistry.demon_heart.get().defaultBlockState()
            .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockDemonicHeart.FACING, net.minecraft.core.Direction.WEST), 2);
         BlockEntity hbe = level.getBlockEntity(pos);
         com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart heart =
            (hbe instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityDemonHeart h) ? h : null;
         if (heart != null) {
            heart.setActivated(true);
            heart.setZigguratHeart(true);
         }
         com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords.ICurseCords cords =
            level.getLevel().getCapability(com.paleimitations.schoolsofmagic.common.world.capabilities.cursecords.CapabilityCurseCords.CURSE_CORDS_CAPABILITY).orElse(null);
         if (cords != null) cords.addZigCurseCord(pos.immutable());
         ServerLevel server = level.getLevel();
         net.minecraft.world.entity.Entity demon = com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.DEMON.get().create(server);
         if (demon instanceof Mob dm) {
            dm.moveTo(pos.getX() - 5 + 0.5, pos.getY() - 2, pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
            dm.setPersistenceRequired();
            level.addFreshEntity(dm);

            if (heart != null) { heart.setBossID(dm.getUUID()); heart.setChanged(); }
         }
         return;
      }

      level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
   }

   private static void placeArcanaPedestal(ServerLevelAccessor level, BlockPos pos) {
      BlockPos base = pos;
      net.minecraft.world.level.block.Block pedBlock = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(
         new net.minecraft.resources.ResourceLocation("som", "netherbrick_pedestal"));
      if (pedBlock == null) {
         pedBlock = BlockRegistry.PEDESTALS.get(0).get();
      }
      net.minecraft.world.level.block.state.BlockState ped = pedBlock.defaultBlockState()
         .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockPedestal.FACING, net.minecraft.core.Direction.WEST);
      level.setBlock(base.above(), Blocks.AIR.defaultBlockState(), 2);
      level.setBlock(base, ped, 2);
      BlockEntity be = level.getBlockEntity(base);
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPedestal tp) {
         net.minecraft.world.item.ItemStack tome = new net.minecraft.world.item.ItemStack(
            com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.magic_book.get());
         tome.setDamageValue(com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.INFERNALITY.ordinal());
         com.paleimitations.schoolsofmagic.common.items.ItemMagicBook.initializeBook(tome);
         tp.setItem(tome);
         tp.setChanged();
      }
   }

   private static void placePodium(ServerLevelAccessor level, BlockPos pos, RandomSource random) {
      net.minecraft.world.level.block.state.BlockState base = BlockRegistry.podium.get().defaultBlockState()
         .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockPodium.FACING, net.minecraft.core.Direction.SOUTH)
         .setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockPodium.TYPE, com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.VERDE);
      level.setBlock(pos, base.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockPodium.IS_LEFT, true), 2);
      level.setBlock(pos.relative(net.minecraft.core.Direction.SOUTH.getClockWise()),
         base.setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockPodium.IS_LEFT, false), 2);
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntityPodium tp) {
         tp.setWood(com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType.VERDE);
         net.minecraft.world.item.ItemStack book = new net.minecraft.world.item.ItemStack(
            com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.magic_book.get());
         book.setDamageValue(random.nextInt(16));
         com.paleimitations.schoolsofmagic.common.items.ItemMagicBook.initializeBook(book);
         if (tp.handler != null && tp.handler.getSlots() > 0) tp.handler.setStackInSlot(0, book);
         tp.setChanged();
      }
   }

   static com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse curseFor(String marker) {
      String s = marker.substring(marker.lastIndexOf('_') + 1);
      switch (s) {
         case "blindness":     return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.BLINDNESS;
         case "flamability":   return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.FLAMABILITY;
         case "hallucination":
         case "stench":        return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.HALLUCINATION;
         case "rising":        return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.RISING;
         case "spinning":
         case "possession":    return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.SPINNING;
         case "vulnerability":
         case "weakness":      return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.VULNERABILITY;
         default:              return com.paleimitations.schoolsofmagic.common.blocks.BlockDarkCrystal.Curse.SLOWNESS;
      }
   }

   private static void placeLoot(ServerLevelAccessor level, BlockPos pos, RandomSource random, boolean large) {
      Block block;
      switch (random.nextInt(4)) {
         case 0: block = Blocks.CHEST; break;
         case 1: block = BlockRegistry.rotted_chest.get(); break;
         case 2: block = large ? BlockRegistry.vase_big1.get() : BlockRegistry.vase1.get(); break;
         default: block = large ? BlockRegistry.vase_big2.get() : BlockRegistry.vase2.get(); break;
      }
      level.setBlock(pos, block.defaultBlockState(), 2);

      if (block == BlockRegistry.vase_big2.get()) {
         level.setBlock(pos.above(), block.defaultBlockState().setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge.HALF, com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge.EnumBlockHalf.MIDDLE), 2);
         level.setBlock(pos.above(2), block.defaultBlockState().setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge.HALF, com.paleimitations.schoolsofmagic.common.blocks.BlockVaseLarge.EnumBlockHalf.UPPER), 2);
      } else if (block == BlockRegistry.vase_big1.get()) {
         level.setBlock(pos.above(), block.defaultBlockState().setValue(com.paleimitations.schoolsofmagic.common.blocks.BlockVaseMedium.HALF, com.paleimitations.schoolsofmagic.common.blocks.BlockVaseMedium.EnumBlockHalf.UPPER), 2);
      }

      net.minecraft.resources.ResourceLocation table = (block == BlockRegistry.rotted_chest.get())
         ? ROTTED_CHEST_LOOT : LOOT_TABLES[random.nextInt(LOOT_TABLES.length)];
      BlockEntity be = level.getBlockEntity(pos);
      if (be instanceof RandomizableContainerBlockEntity c) {
         c.setLootTable(table, random.nextLong());
      } else if (be instanceof com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySmallVase v) {

         fillVase(level, pos, v, table, random);
      }
   }

   static final net.minecraft.resources.ResourceLocation ROTTED_CHEST_LOOT =
      new net.minecraft.resources.ResourceLocation(SchoolsOfMagic.MODID, "chests/rotted_chest");

   private static final net.minecraft.resources.ResourceLocation[] LOOT_TABLES = {
      BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.SIMPLE_DUNGEON,
      BuiltInLootTables.SIMPLE_DUNGEON, BuiltInLootTables.STRONGHOLD_CROSSING, BuiltInLootTables.NETHER_BRIDGE,
      BuiltInLootTables.ABANDONED_MINESHAFT, BuiltInLootTables.STRONGHOLD_LIBRARY
   };

   private static void fillVase(ServerLevelAccessor level, BlockPos pos,
         com.paleimitations.schoolsofmagic.common.tileentity.TileEntitySmallVase vase,
         net.minecraft.resources.ResourceLocation table, RandomSource random) {
      try {
         ServerLevel server = level.getLevel();
         net.minecraft.world.level.storage.loot.LootTable loot = server.getServer().getLootData().getLootTable(table);
         net.minecraft.world.level.storage.loot.LootParams params =
            new net.minecraft.world.level.storage.loot.LootParams.Builder(server)
               .withParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ORIGIN, net.minecraft.world.phys.Vec3.atCenterOf(pos))
               .create(net.minecraft.world.level.storage.loot.parameters.LootContextParamSets.CHEST);
         for (net.minecraft.world.item.ItemStack stack : loot.getRandomItems(params, random.nextLong())) {
            if (stack.isEmpty()) continue;
            for (int tries = 0; tries < 27; tries++) {
               int slot = random.nextInt(27);
               if (vase.handler.getStackInSlot(slot).isEmpty()) { vase.handler.setStackInSlot(slot, stack); break; }
            }
         }
         vase.setChanged();
      } catch (Throwable ignored) {}
   }

   private void mobs(ServerLevelAccessor level, BlockPos pos, RandomSource random, EntityType<?> type, int min, int max, net.minecraft.world.effect.MobEffect effect) {
      ServerLevel server = level.getLevel();
      level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
      int n = min + (max > min ? random.nextInt(max - min + 1) : 0);
      for (int i = 0; i < n; i++) {
         net.minecraft.world.entity.Entity e = type.create(server);
         if (!(e instanceof Mob m)) continue;
         m.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
         m.setPersistenceRequired();
         if (effect != null && m instanceof LivingEntity) {
            m.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE, 0, false, false));
         }
         level.addFreshEntity(m);
      }
   }
}
