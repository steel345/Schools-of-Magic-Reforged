package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.common.blocks.BlockConnectedBush;
import com.paleimitations.schoolsofmagic.common.blocks.BlockHangingBranch;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmLeaves;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmLog;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPalmTop;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantBush;
import com.paleimitations.schoolsofmagic.common.blocks.BlockPlantGrowingBush;
import com.paleimitations.schoolsofmagic.common.blocks.SOMLeaves1;
import com.paleimitations.schoolsofmagic.common.blocks.SOMLeaves2;
import com.paleimitations.schoolsofmagic.common.blocks.SOMStrippableLog;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.registries.RegistryObject;

public final class FlammabilityHandler {
   private FlammabilityHandler() {}

   private static java.lang.reflect.Method setFlammable;

   public static void register() {
      FireBlock fire = (FireBlock) Blocks.FIRE;
      try {
         if (setFlammable == null) {
            for (java.lang.reflect.Method cand : FireBlock.class.getDeclaredMethods()) {
               Class<?>[] p = cand.getParameterTypes();
               if (p.length == 3 && p[0] == Block.class && p[1] == int.class && p[2] == int.class) {
                  cand.setAccessible(true);
                  setFlammable = cand;
                  break;
               }
            }
         }
      } catch (Exception e) {
         org.apache.logging.log4j.LogManager.getLogger().error("SOM: could not resolve FireBlock.setFlammable", e);
         return;
      }
      if (setFlammable == null) {
         org.apache.logging.log4j.LogManager.getLogger().error("SOM: FireBlock.setFlammable not found, block flammability NOT registered");
         return;
      }

      for (RegistryObject<Block> ro : BlockRegistry.BLOCKS.getEntries()) {
         Block b = ro.get();

         if (!b.defaultBlockState().ignitedByLava()) continue;

         int encouragement;
         int flammability;
         if (b instanceof SOMLeaves1 || b instanceof SOMLeaves2 || b instanceof BlockHangingBranch
               || b instanceof BlockPalmLeaves || b instanceof LeavesBlock) {
            encouragement = 30;
            flammability = 60;
         } else if (b instanceof BlockPlantBush || b instanceof BlockConnectedBush
               || b instanceof BlockPlantGrowingBush) {
            encouragement = 60;
            flammability = 100;
         } else if (b instanceof SOMStrippableLog || b instanceof BlockPalmLog
               || b instanceof BlockPalmTop || b instanceof RotatedPillarBlock) {
            encouragement = 5;
            flammability = 5;
         } else {
            encouragement = 5;
            flammability = 20;
         }
         try {
            setFlammable.invoke(fire, b, encouragement, flammability);
         } catch (Exception e) {
            org.apache.logging.log4j.LogManager.getLogger().error("SOM: failed to set flammability for {}", b, e);
         }
      }
   }
}
