package com.paleimitations.schoolsofmagic.common.world.features.trees;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.util.Utils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SOMTreeFeatureBigJungle extends SOMBigTreeFeature {

   private static final RegistryObject<Block> LOG    = RegistryObject.create(new ResourceLocation("jungle_log"),    ForgeRegistries.BLOCKS);
   private static final RegistryObject<Block> LEAVES = RegistryObject.create(new ResourceLocation("jungle_leaves"), ForgeRegistries.BLOCKS);

   @Override protected RegistryObject<Block> log()    { return LOG; }
   @Override protected RegistryObject<Block> leaves() { return LEAVES; }

   @Override
   protected boolean generate(WorldGenLevel world, RandomSource rand, BlockPos position) {
      int i = rand.nextInt(3);
      int trunk = i + 5;
      int treeSize = 9 + i + trunk * (5 + i);
      int l = rand.nextInt(2);
      int numberOfCanopies = 6 + l;
      int y = position.getY();
      if (y < world.getMinBuildHeight() + 1 || y + treeSize + 1 >= maxHeight(world)) return false;
      BlockPos down = position.below();
      if (!isSoil(world, down) || y >= maxHeight(world) - treeSize - 1) return false;
      if (!placeTreeOfHeight(world, position, i, i + 4, treeSize)) return false;
      if (!isSoil(world, down) || y >= maxHeight(world) - i - 1) return false;

      onPlantGrow(world, down, position);
      int canopyMaxRadius = 7 + i;

      for (int k = 2; k < numberOfCanopies; k++) {
         if (rand.nextDouble() < 0.13 * k) {
            for (int a = 0; a <= 2 + l; a++) {
               BlockPos core = position.above(trunk * k);
               List<BlockPos> acc = ring(core, 3 + l, 4 + l, 0, 0);
               if (acc.isEmpty()) continue;
               BlockPos cp = acc.get(rand.nextInt(acc.size()));
               int limbLength = rand.nextInt(2) + 1;
               placeCanopyVine(world, cp, rand);
               drawLimbToCore(world, core, cp, limbLength);
            }
         }
      }

      BlockPos core = position.above(trunk * numberOfCanopies);
      List<BlockPos> acc = ring(core, 6 + i, canopyMaxRadius, 0, canopyMaxRadius);
      List<BlockPos> placed = Lists.newArrayList();
      placeCanopyVine(world, core.above(canopyMaxRadius), rand);
      placed.add(core.above(canopyMaxRadius));
      if (!acc.isEmpty()) {
         for (int a = 0; a <= 12 + i * 3; a++) {
            int p = rand.nextInt(acc.size());
            for (BlockPos loc : placed) {
               int guard = 0;
               while (Utils.getDistance(acc.get(p), loc) < 3.0 && guard++ < 64) p = rand.nextInt(acc.size());
            }
            BlockPos cp = acc.get(p);
            placed.add(cp);
            int limbLength = rand.nextInt(2) + 1;
            placeCanopyVine(world, cp, rand);
            drawLimbToCore(world, core, cp, limbLength);
         }
      }

      placeTrunkColumn(world, position, canopyMaxRadius + trunk * numberOfCanopies);
      placeStubRoots(world, rand, position);
      return true;
   }

   private List<BlockPos> ring(BlockPos core, int minR, int maxR, int vLow, int vHigh) {
      List<BlockPos> out = Lists.newArrayList();
      for (BlockPos ph : BlockPos.betweenClosed(core.offset(-maxR, vLow, -maxR), core.offset(maxR, vHigh, maxR))) {
         double d = Utils.getDistance(ph, core);
         if (d <= maxR && d >= minR) out.add(ph.immutable());
      }
      return out;
   }

   private void placeCanopyVine(WorldGenLevel w, BlockPos pos, RandomSource rand) {
      for (BlockPos p : BlockPos.betweenClosed(pos.offset(-2, 1, -1), pos.offset(2, 1, 1))) placeFringeAt(w, p);
      for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, 1, -2), pos.offset(1, 1, 2))) placeFringeAt(w, p);
      for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, 2, 0), pos.offset(1, 2, 0))) placeFringeAt(w, p);
      for (BlockPos p : BlockPos.betweenClosed(pos.offset(0, 2, -1), pos.offset(0, 2, 1))) placeFringeAt(w, p);
      int i = rand.nextInt(3);
      for (int i2 = pos.getY() - 3 + i + 2; i2 <= pos.getY() + i + 2; i2++) {
         int k2 = i2 - (pos.getY() + i + 2);
         int i3 = 2 - k2 / 2;
         for (int l3 = pos.getX() - i3; l3 <= pos.getX() + i3; l3++) {
            for (int j4 = pos.getZ() - i3; j4 <= pos.getZ() + i3; j4++) {
               BlockPos lp = new BlockPos(l3, i2, j4);
               if (w.getBlockState(lp).is(BlockTags.LEAVES)) {

                  if (rand.nextInt(4) == 0 && w.isEmptyBlock(lp.west()))  addVine(w, lp.west(),  VineBlock.EAST,  rand);
                  if (rand.nextInt(4) == 0 && w.isEmptyBlock(lp.east()))  addVine(w, lp.east(),  VineBlock.WEST,  rand);
                  if (rand.nextInt(4) == 0 && w.isEmptyBlock(lp.north())) addVine(w, lp.north(), VineBlock.SOUTH, rand);
                  if (rand.nextInt(4) == 0 && w.isEmptyBlock(lp.south())) addVine(w, lp.south(), VineBlock.NORTH, rand);
               }
            }
         }
      }
      for (int k = 0; k <= 2; k++) set(w, pos.above(k), trunkY());
   }

   private void addVine(WorldGenLevel w, BlockPos pos, BooleanProperty face, RandomSource rand) {
      BlockState vine = Blocks.VINE.defaultBlockState().setValue(face, true);
      set(w, pos, vine);
      int len = 4 + rand.nextInt(2);
      for (BlockPos bp = pos.below(); w.isEmptyBlock(bp) && len > 0; len--) {
         set(w, bp, vine);
         bp = bp.below();
      }
   }
}
