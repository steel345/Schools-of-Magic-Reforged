package com.paleimitations.schoolsofmagic.common.world.biomes;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public final class SOMTerraBlender {
   private SOMTerraBlender() {}

   public static void register() {

      Regions.register(new SOMRegion(new ResourceLocation(SchoolsOfMagic.MODID, "overworld"), 14));
      SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, SchoolsOfMagic.MODID, makeSurfaceRules());
   }

   private static SurfaceRules.RuleSource makeSurfaceRules() {
      SurfaceRules.RuleSource sand = SurfaceRules.state(Blocks.SAND.defaultBlockState());
      SurfaceRules.RuleSource sandstone = SurfaceRules.state(Blocks.SANDSTONE.defaultBlockState());

      SurfaceRules.RuleSource desert = SurfaceRules.ifTrue(
         SurfaceRules.isBiome(
            SOMBiomes.MAGIC_DESERT, SOMBiomes.BANDED_DESERT, SOMBiomes.DESERT_CANYONS,
            SOMBiomes.DESERT_RIVER_CANYONS, SOMBiomes.DESERT_UPLANDS),
         SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, sand),
            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, sand),
            sandstone));

      SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
      SurfaceRules.RuleSource dirt = SurfaceRules.state(Blocks.DIRT.defaultBlockState());
      SurfaceRules.RuleSource bands = SurfaceRules.bandlands();

      SurfaceRules.RuleSource jungle = SurfaceRules.ifTrue(
         SurfaceRules.isBiome(SOMBiomes.MOUNTAINOUS_JUNGLE),
         SurfaceRules.sequence(
            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grass),
            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, dirt),
            bands));

      return SurfaceRules.sequence(desert, jungle);
   }
}
