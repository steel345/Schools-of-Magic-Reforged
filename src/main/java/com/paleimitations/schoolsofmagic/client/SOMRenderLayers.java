package com.paleimitations.schoolsofmagic.client;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public final class SOMRenderLayers {
   private static final Set<String> CUTOUT = Set.of(
      "BlockBrazier", "BlockBurstPotion", "BlockCatalystBasin", "BlockCoconut", "BlockConnectedBush",
      "BlockConnectedBeanstalk", "BlockDesertBrazier", "BlockDynamicWeb",
      "BlockHangingBranch", "BlockHerbalTwine", "BlockMagicBookshelf", "BlockMagicOre", "BlockOre",
      "BlockOredirt", "BlockOrefalling", "BlockOresmoky", "BlockPalmLeaves", "BlockPhantomFire", "BlockPlantGrowingBush",
      "BlockPlantHydrangea", "BlockPlantMistletoe", "BlockPedestal", "BlockPodium", "BlockSacrificialAltar", "BlockTest",
      "BlockTrapSpikeBase", "BlockTrapSpike", "BlockWallPlant", "BlockWallPyracantha", "Gem", "SOMFence",
      "SOMLeaves1", "SOMLeaves2", "BlockCattail", "BlockWaterplant", "BlockMagicPlant", "BlockMushroomCrop",
      "BlockMagicCrop", "BlockDoorWall", "BlockToadSpawn", "BlockGemCluster",
      "BlockUnlitTorch", "BlockUnlitWallTorch",

      "SOMDoor", "TrapDoorBlock");

   private static final Set<String> TRANSLUCENT = Set.of("BlockGem", "BlockCrystalBall", "BlockFaegrovePortal");

   private SOMRenderLayers() {}

   public static void register() {
      for (RegistryObject<Block> ro : BlockRegistry.BLOCKS.getEntries()) {
         Block b = ro.get();
         String c = b.getClass().getSimpleName();
         if (TRANSLUCENT.contains(c)) {
            ItemBlockRenderTypes.setRenderLayer(b, RenderType.translucent());
         } else if (b instanceof LeavesBlock) {
            ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutoutMipped());
         } else if (b instanceof BushBlock || b instanceof SaplingBlock || b instanceof CropBlock
               || CUTOUT.contains(c)) {
            ItemBlockRenderTypes.setRenderLayer(b, RenderType.cutout());
         }
      }
   }
}
