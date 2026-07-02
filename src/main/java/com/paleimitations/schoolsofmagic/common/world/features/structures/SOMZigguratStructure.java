package com.paleimitations.schoolsofmagic.common.world.features.structures;

import com.mojang.serialization.Codec;
import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class SOMZigguratStructure extends Structure {

   public static final Codec<SOMZigguratStructure> CODEC = simpleCodec(SOMZigguratStructure::new);

   private static final int[] DX = {-61,-61,-61,-29,-29,-29,-61,-61,-61,-29,-29,-29,3,3,3,35};
   private static final int[] DY = {0,0,0,0,0,0,32,32,32,32,32,32,0,0,0,0};
   private static final int[] DZ = {-37,-13,12,-37,-13,12,-37,-13,12,-37,-13,12,-37,-13,12,-13};
   private static final String[] GRID = {
      "zig_r1_c1_l1","zig_r2_c1_l1","zig_r3_c1_l1","zig_r1_c2_l1","zig_r2_c2_l1","zig_r3_c2_l1",
      "zig_r1_c1_l2","zig_r2_c1_l2","zig_r3_c1_l2","zig_r1_c2_l2","zig_r2_c2_l2","zig_r3_c2_l2",
      "zig_r1_c3_l1","zig_r2_c3_l1","zig_r3_c3_l1","zig_r2_c4_l1"};

   private static final String[] TIER1 = {"warning","puzzle_easy","mud_pit","charcoal_pit","maze","stables","fish_tank","dark_crystal_slowness"};
   private static final String[] TIER2 = {"blaze_pyre","witch_library","wolf_den","zombie_dungeon","undead_tomb","slimey_swamp","spike_trap","forge","dark_crystal_vulnerability","dark_crystal_flamability","dark_crystal_spinning","puzzle_medium","magma_pit","skeleton_tomb","jar_pool","lava_pit_posession_curse"};
   private static final String[] TIER3 = {"brewing_room","creeper_room","dark_crystal_blindness","dark_crystal_hallucination","dark_crystal_rising","grand_tomb","infested_library","invisible_spiders","lava_stink","puzzle_ender","puzzle_hard","pyre_jar","spike_trap_rising_curse","storage_room","water_trap_sinking_curse","wither_pit_sinking_curse"};

   private static final int MAX_PIECES = 256;

   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();

   public SOMZigguratStructure(Structure.StructureSettings settings) {
      super(settings);
   }

   private record Node(String template, BlockPos origin, Rotation rotation, int depth) {}

   @Override
   public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
      ChunkPos chunkPos = context.chunkPos();
      int x = chunkPos.getMinBlockX();
      int z = chunkPos.getMinBlockZ();

      java.util.List<Integer> heights = new java.util.ArrayList<>();
      for (int sx = -61; sx <= 67; sx += 16) {
         for (int sz = -37; sz <= 36; sz += 16) {
            heights.add(context.chunkGenerator().getFirstOccupiedHeight(
               x + sx, z + sz, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()));
         }
      }
      heights.sort(null);
      int ground = heights.get(heights.size() / 2);
      BlockPos origin = new BlockPos(x, ground, z);
      StructureTemplateManager tm = context.structureTemplateManager();
      long zigSeed = origin.asLong();

      return Optional.of(new GenerationStub(origin, builder -> {

         BlockPos altarLink = null, tabletLink = null;
         for (int r = 0; r < 16; r++) {
            try {
               StructureTemplate t = tm.get(new ResourceLocation(SchoolsOfMagic.MODID, GRID[r])).orElse(null);
               if (t == null) continue;
               BlockPos cellOrigin = origin.offset(DX[r], DY[r], DZ[r]);
               StructurePlaceSettings s = ZigguratPiece.makeSettings(Rotation.NONE);
               for (StructureTemplate.StructureBlockInfo info : t.filterBlocks(cellOrigin, s, Blocks.STRUCTURE_BLOCK)) {
                  if (info.nbt() == null || !"DATA".equals(info.nbt().getString("mode"))) continue;
                  String m = info.nbt().getString("metadata");
                  if (m.equals("altar")) altarLink = info.pos();
                  else if (m.equals("tablet")) tabletLink = info.pos();
               }
            } catch (Exception e) {
               LOGGER.error("ZIGGURAT: ritual pre-scan failed for {}: {}", GRID[r], e.toString());
            }
         }
         final BlockPos fAltar = altarLink, fTablet = tabletLink;
         ArrayDeque<Node> queue = new ArrayDeque<>();
         for (int r = 0; r < 16; r++) {
            queue.add(new Node(GRID[r], origin.offset(DX[r], DY[r], DZ[r]), Rotation.NONE, 0));
         }
         int count = 0;
         while (!queue.isEmpty() && count < MAX_PIECES) {
            Node n = queue.poll();
            try {
               builder.addPiece(new ZigguratPiece(tm, n.template(), n.origin(), n.rotation(), zigSeed, fAltar, fTablet));
            } catch (Exception e) {
               LOGGER.error("ZIGGURAT: failed to add piece {} at {}: {}", n.template(), n.origin(), e.toString());
            }
            count++;
            if (n.depth() >= 2) continue;
            try {
               StructureTemplate t = tm.get(new ResourceLocation(SchoolsOfMagic.MODID, n.template())).orElse(null);
               if (t == null) continue;
               StructurePlaceSettings s = ZigguratPiece.makeSettings(n.rotation());
               for (StructureTemplate.StructureBlockInfo info : t.filterBlocks(n.origin(), s, Blocks.STRUCTURE_BLOCK)) {
                  if (info.nbt() == null || !"DATA".equals(info.nbt().getString("mode"))) continue;
                  String m = info.nbt().getString("metadata");
                  BlockPos mp = info.pos();

                  if (n.depth() == 0) {
                     String room = null;
                     if (m.equals("treasure_room") || m.equals("treasure")) room = "zig_treasure_room";
                     else if (m.startsWith("challenge_tier1_")) room = pick(TIER1, 1, m, zigSeed);
                     else if (m.startsWith("challenge_tier2_")) room = pick(TIER2, 2, m, zigSeed);
                     else if (m.startsWith("challenge_tier3_")) room = pick(TIER3, 3, m, zigSeed);
                     if (room != null) { queue.add(new Node(room, mp.offset(-8, -1, -8), Rotation.NONE, n.depth() + 1)); continue; }
                  }

                  if (m.equals("ew_hall")) {
                     queue.add(new Node(hall(zigSeed, mp), mp.offset(-8, -1, -1), Rotation.NONE, n.depth() + 1));
                  } else if (m.equals("ns_hall")) {
                     queue.add(new Node(hall(zigSeed, mp), mp.offset(1, -1, -8), Rotation.CLOCKWISE_90, n.depth() + 1));
                  }
               }
            } catch (Exception e) {
               LOGGER.error("ZIGGURAT: marker expansion failed for {}: {}", n.template(), e.toString());
            }
         }
         LOGGER.info("ZIGGURAT: generated start at {} with {} pieces (ground y={})", origin, count, origin.getY());
      }));
   }

   private static String pick(String[] tier, int tierNum, String markerName, long zigSeed) {
      int idx;
      try { idx = Integer.parseInt(markerName.substring(markerName.lastIndexOf('_') + 1)); } catch (NumberFormatException e) { idx = 0; }
      List<String> list = new ArrayList<>(Arrays.asList(tier));
      Collections.shuffle(list, new Random(zigSeed * 31L + tierNum));
      return list.get(((idx % list.size()) + list.size()) % list.size());
   }

   private static String hall(long zigSeed, BlockPos mp) {
      return "hall" + (1 + new Random(zigSeed ^ mp.asLong()).nextInt(21));
   }

   @Override
   public StructureType<?> type() {
      return StructureRegistry.ZIGGURAT.get();
   }
}
