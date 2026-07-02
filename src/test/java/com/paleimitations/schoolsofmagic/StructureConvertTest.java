package com.paleimitations.schoolsofmagic;

import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

// TOOLING (not shipped): converts the OG 1.12.2 structure templates' block palettes from the old SOM block
// names to the 1.20.1 port names, writing corrected .nbt into the mod's data/som/structures/. Run with:
//   gradlew test --tests com.paleimitations.schoolsofmagic.StructureConvertTest
public class StructureConvertTest {

   static final String SRC = "C:/Users/leojo_seszrpr/Downloads/Som_decompiled_project/SchoolsOfMagic-1.12.2/src/main/resources/assets/som/structures";

   @Test
   public void sizes() throws Exception {
      String[] probe = {"hall_base","hall1","hall2","hall5","hall10","hall21","treasure_room","puzzle_easy","zig_r1_c1_l1","zig_r2_c2_l1","maze","grand_tomb"};
      for (String n : probe) {
         File f = new File(SRC, n + ".nbt");
         if (!f.exists()) { System.out.println("SIZE " + n + " = (missing)"); continue; }
         CompoundTag root = NbtIo.readCompressed(f);
         net.minecraft.nbt.ListTag s = root.getList("size", Tag.TAG_INT);
         System.out.println("SIZE " + n + " = [" + s.getInt(0) + "," + s.getInt(1) + "," + s.getInt(2) + "]");
      }
   }

   @Test
   public void blocksCount() throws Exception {
      Path out = Paths.get("").toAbsolutePath().resolve("src/main/resources/data/som/structures");
      String[] probe = {"zig_r1_c1_l1","zig_r2_c2_l1","mini_pyromancy","forge"};
      for (String n : probe) {
         File f = out.resolve(n + ".nbt").toFile();
         if (!f.exists()) { System.out.println("BLOCKS " + n + " missing"); continue; }
         CompoundTag root = NbtIo.readCompressed(f);
         int blocks = root.contains("blocks", Tag.TAG_LIST) ? root.getList("blocks", Tag.TAG_COMPOUND).size() : -1;
         int pal = root.contains("palette", Tag.TAG_LIST) ? root.getList("palette", Tag.TAG_COMPOUND).size() : -1;
         net.minecraft.nbt.ListTag sz = root.getList("size", Tag.TAG_INT);
         int nonAir = 0;
         if (blocks > 0) {
            // count blocks whose palette state isn't air
            java.util.List<String> names = new java.util.ArrayList<>();
            for (Tag t : root.getList("palette", Tag.TAG_COMPOUND)) names.add(((CompoundTag)t).getString("Name"));
            for (Tag t : root.getList("blocks", Tag.TAG_COMPOUND)) {
               int st = ((CompoundTag)t).getInt("state");
               if (st >= 0 && st < names.size() && !names.get(st).equals("minecraft:air")) nonAir++;
            }
         }
         System.out.println("BLOCKS " + n + " size=[" + sz.getInt(0)+","+sz.getInt(1)+","+sz.getInt(2) + "] blocks=" + blocks + " palette=" + pal + " nonAir=" + nonAir);
      }
   }

   @Test
   public void diag() throws Exception {
      SharedConstants.tryDetectVersion();
      Bootstrap.bootStrap();
      String[] probe = {"zig_r1_c1_l1","zig_r2_c3_l1","mini_infernality"};
      for (String n : probe) {
         File f = new File(SRC, n + ".nbt");
         if (!f.exists()) { System.out.println("DIAG " + n + " missing"); continue; }
         CompoundTag root = NbtIo.readCompressed(f);
         java.util.List<String> before = new java.util.ArrayList<>();
         if (root.contains("palette", Tag.TAG_LIST)) for (Tag t : root.getList("palette", Tag.TAG_COMPOUND)) before.add(((CompoundTag)t).getString("Name"));
         stripDeadBlockEntities(root);
         int oldVer = root.contains("DataVersion") ? root.getInt("DataVersion") : 1343;
         CompoundTag fixed = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), root, oldVer);
         java.util.List<String> after = new java.util.ArrayList<>();
         if (fixed.contains("palette", Tag.TAG_LIST)) for (Tag t : fixed.getList("palette", Tag.TAG_COMPOUND)) after.add(((CompoundTag)t).getString("Name"));
         for (int i = 0; i < after.size(); i++) {
            if (after.get(i).contains("FILTER_ME")) System.out.println("DIAG " + n + " idx " + i + ": " + (i<before.size()?before.get(i):"?") + " -> FILTER_ME");
         }
      }
   }

   @Test
   public void scanBEs() throws Exception {
      Path out = Paths.get("").toAbsolutePath().resolve("src/main/resources/data/som/structures");
      TreeSet<String> ids = new TreeSet<>();
      for (File f : out.toFile().listFiles((d, n) -> n.endsWith(".nbt"))) {
         CompoundTag root = NbtIo.readCompressed(f);
         if (!root.contains("blocks", Tag.TAG_LIST)) continue;
         for (Tag t : root.getList("blocks", Tag.TAG_COMPOUND)) {
            CompoundTag b = (CompoundTag) t;
            if (b.contains("nbt", Tag.TAG_COMPOUND)) ids.add(b.getCompound("nbt").getString("id"));
         }
      }
      System.out.println("=== ALL block-entity ids across templates: " + ids);
   }

   @Test
   public void verify() throws Exception {
      Path out = Paths.get("").toAbsolutePath().resolve("src/main/resources/data/som/structures");
      String[] probe = {"mini_hydromancy","mini_pyromancy","zig_r1_c1_l1","forge","grand_tomb"};
      for (String n : probe) {
         File f = out.resolve(n + ".nbt").toFile();
         if (!f.exists()) { System.out.println("VERIFY " + n + " = (missing)"); continue; }
         CompoundTag root = NbtIo.readCompressed(f);
         int dv = root.getInt("DataVersion");
         int potBEs = 0;
         if (root.contains("blocks", Tag.TAG_LIST)) {
            for (Tag t : root.getList("blocks", Tag.TAG_COMPOUND)) {
               CompoundTag b = (CompoundTag) t;
               if (b.contains("nbt", Tag.TAG_COMPOUND) && b.getCompound("nbt").getString("id").equals("minecraft:flower_pot")) potBEs++;
            }
         }
         System.out.println("VERIFY " + n + " DataVersion=" + dv + " flowerPotBEs=" + potBEs);
      }
   }

   @Test
   public void convert() throws Exception {
      Path proj = Paths.get("").toAbsolutePath();
      Path out = proj.resolve("src/main/resources/data/som/structures");
      Files.createDirectories(out);

      Map<String, String> map = new HashMap<>();      // old(no som:) -> new(no som: OR minecraft:air)
      Map<String, String> propType = new HashMap<>(); // old -> value for the "type" blockstate property
      // auto matches (EXACT/NORM)
      for (String line : Files.readAllLines(proj.resolve("tools/structures/block_map_auto.tsv"))) {
         String[] p = line.split("\t");
         if (p.length >= 3 && (p[2].equals("EXACT") || p[2].equals("NORM"))) map.put(p[0], p[1]);
      }
      // manual flat
      String[][] flat = {
         {"tiledynamicweb","dynamic_web"},{"tileentity_dynamicweb","dynamic_web"},
         {"tilespeartrap","trap_spike"},{"tileentity_speartrap","trap_spike"},
         {"tileteapot","teapot"},{"objectteapot","teapot"},
         {"objectmortnpest","mortnpest"},{"tileentity_mortnpest","mortnpest"},
         {"tileentity_podium","podium"},{"tileentity_herbaltwine","herbal_twine"},
         {"rotted_planks_acacia","rotted_planks"},{"wallrose","plant_rose"},
         {"magic_log1","log_ash"},{"magic_log2","log_yew"},
         {"block_turquoise","crystal_turquoise"},{"gem_turquoise","crystal_turquoise"},
         {"planterpine","planter"},{"planter_verd","planter"},{"platewheat","plate"},
         {"block_voidbrick","doubleslab_void"}, // full-height void-brick block (no separate base block in port)
         {"lion","minecraft:air"},
         {"dryingwheat","dried_plant"},{"dryingrose","dried_plant"},{"dryinglavender","dried_plant"},
         {"dryinglilac","dried_plant"},{"dryingmallow","dried_plant"},{"dryingpeony","dried_plant"},
         {"dryingsunflower","dried_plant"},{"dryingfoxglove","dried_plant"},{"drying_graveroot","dried_plant"},
         {"drying_nightberry","dried_plant"},{"drying_sunflower","dried_plant"},
      };
      for (String[] m : flat) map.put(m[0], m[1]);
      // PROP cases: base block + type=<value>
      String[][] woods = {{"ash","ash"},{"elder","elder"},{"pine","pine"},{"verde","verde"},{"willow","willow"},{"yew","yew"}};
      for (String[] w : woods) { map.put("planks_"+w[0], "planks"); propType.put("planks_"+w[0], w[1]);
                                 map.put("bookshelf_"+w[0], "magic_bookshelf"); propType.put("bookshelf_"+w[0], w[1]); }
      map.put("plankspine","planks"); propType.put("plankspine","pine");
      map.put("plantlavender","magic_plant"); propType.put("plantlavender","hieromancy");
      map.put("plantmallow","magic_plant"); propType.put("plantmallow","auramancy");
      map.put("plant_pyracantha","magic_plant"); propType.put("plant_pyracantha","pyromancy");

      // Bring up vanilla so the DataFixer + registries are available, then flatten EVERY template up to the
      // current version HERE (at convert time) and stamp the current DataVersion. That way the game never runs
      // its datafixers on these templates at load time — which is what kept crashing (Unknown type flower_pot/
      // noteblock, then minecraft:%%FILTER_ME%% from double_plant/double_stone_slab). We run the same fixer once,
      // offline, and scrub the FILTER_ME placeholders to air so the final NBT is clean 1.20.1 data.
      SharedConstants.tryDetectVersion();
      Bootstrap.bootStrap();
      int current = SharedConstants.getCurrentVersion().getDataVersion().getVersion();

      int count = 0; TreeSet<String> unmapped = new TreeSet<>();
      File[] files = new File(SRC).listFiles((d, n) -> n.endsWith(".nbt"));
      for (File f : files) {
         CompoundTag root = NbtIo.readCompressed(f);
         if (root.contains("palette", Tag.TAG_LIST)) remapPalette(root.getList("palette", Tag.TAG_COMPOUND), map, propType, unmapped);
         if (root.contains("palettes", Tag.TAG_LIST)) {
            ListTag ps = root.getList("palettes", Tag.TAG_LIST);
            for (Tag t : ps) remapPalette((ListTag) t, map, propType, unmapped);
         }
         // strip the flattening-removed BE types BEFORE datafixing (else the fixer throws "Unknown type").
         stripDeadBlockEntities(root);
         int oldVer = root.contains("DataVersion") ? root.getInt("DataVersion") : 1343;
         CompoundTag fixed = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), root, oldVer);
         scrubFilterMe(fixed);
         fixed.putInt("DataVersion", current);
         NbtIo.writeCompressed(fixed, out.resolve(f.getName()).toFile());
         count++;
      }
      System.out.println("=== STRUCTURE CONVERT: " + count + " templates written to " + out);
      System.out.println("=== STILL-UNMAPPED som blocks (became air): " + unmapped);
   }

   // Block-entity types DELETED in the 1.13 flattening. Their leftover BE nbt in a 1.12.2 template makes the
   // datafixer throw "Unknown type ..." and the whole template fails to load (-> empty structure / floating
   // beard terrain). Strip them; the block itself flattens to its (empty) blockstate. These are the only
   // vanilla tile-entity types removed in flattening, so this list is exhaustive.
   private static final java.util.Set<String> DEAD_BES = java.util.Set.of("minecraft:flower_pot", "minecraft:noteblock");

   private void stripDeadBlockEntities(CompoundTag root) {
      if (!root.contains("blocks", Tag.TAG_LIST)) return;
      ListTag blocks = root.getList("blocks", Tag.TAG_COMPOUND);
      for (Tag t : blocks) {
         CompoundTag b = (CompoundTag) t;
         if (b.contains("nbt", Tag.TAG_COMPOUND)) {
            String id = b.getCompound("nbt").getString("id");
            if (DEAD_BES.contains(id)) b.remove("nbt");
         }
      }
   }

   // After datafixing, blocks the flattener decided to delete become "minecraft:%%FILTER_ME%%" (an invalid
   // ResourceLocation that crashes parsing). Replace any such palette entry with plain air.
   private void scrubFilterMe(CompoundTag root) {
      if (root.contains("palette", Tag.TAG_LIST)) scrubPalette(root.getList("palette", Tag.TAG_COMPOUND));
      if (root.contains("palettes", Tag.TAG_LIST)) {
         for (Tag t : root.getList("palettes", Tag.TAG_LIST)) scrubPalette((ListTag) t);
      }
   }

   private void scrubPalette(ListTag palette) {
      for (Tag t : palette) {
         CompoundTag e = (CompoundTag) t;
         if (e.getString("Name").contains("FILTER_ME")) {
            e.putString("Name", "minecraft:air");
            e.remove("Properties");
         }
      }
   }

   private void remapPalette(ListTag palette, Map<String, String> map, Map<String, String> propType, TreeSet<String> unmapped) {
      for (Tag t : palette) {
         CompoundTag e = (CompoundTag) t;
         String name = e.getString("Name");
         if (!name.startsWith("som:")) continue;
         String key = name.substring(4);
         if (map.containsKey(key)) {
            String nn = map.get(key);
            e.putString("Name", nn.contains(":") ? nn : "som:" + nn);
            if (propType.containsKey(key)) {
               CompoundTag props = e.contains("Properties", Tag.TAG_COMPOUND) ? e.getCompound("Properties") : new CompoundTag();
               props.putString("type", propType.get(key));
               e.put("Properties", props);
            }
         } else {
            unmapped.add(key);
            e.putString("Name", "minecraft:air"); // explicit air rather than an unresolved block
         }
      }
   }
}
