package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureAsh;
import com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureElder;
import com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeaturePine;
import com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureVerde;
import com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureWillow;
import com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureYew;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TreeFeatureRegistry {

   public static final DeferredRegister<Feature<?>> FEATURES =
      DeferredRegister.create(ForgeRegistries.FEATURES, SchoolsOfMagic.MODID);

   public static final RegistryObject<Feature<?>> TREE_ASH    = FEATURES.register("tree_ash",    SOMTreeFeatureAsh::new);
   public static final RegistryObject<Feature<?>> TREE_ELDER  = FEATURES.register("tree_elder",  SOMTreeFeatureElder::new);
   public static final RegistryObject<Feature<?>> TREE_PINE   = FEATURES.register("tree_pine",   SOMTreeFeaturePine::new);
   public static final RegistryObject<Feature<?>> TREE_YEW    = FEATURES.register("tree_yew",    SOMTreeFeatureYew::new);
   public static final RegistryObject<Feature<?>> TREE_WILLOW = FEATURES.register("tree_willow", SOMTreeFeatureWillow::new);
   public static final RegistryObject<Feature<?>> TREE_VERDE  = FEATURES.register("tree_verde",  SOMTreeFeatureVerde::new);
   public static final RegistryObject<Feature<?>> WILD_MUSHROOM =
      FEATURES.register("wild_mushroom", com.paleimitations.schoolsofmagic.common.world.features.WildMushroomFeature::new);

   public static final RegistryObject<Feature<?>> TREE_BIG_OAK =
      FEATURES.register("tree_big_oak", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureBigOak::new);
   public static final RegistryObject<Feature<?>> TREE_BIG_BIRCH =
      FEATURES.register("tree_big_birch", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureBigBirch::new);
   public static final RegistryObject<Feature<?>> TREE_BIG_DARK_OAK =
      FEATURES.register("tree_big_dark_oak", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureBigDarkOak::new);
   public static final RegistryObject<Feature<?>> TREE_BIG_ACACIA =
      FEATURES.register("tree_big_acacia", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureBigAcacia::new);
   public static final RegistryObject<Feature<?>> TREE_BIG_JUNGLE =
      FEATURES.register("tree_big_jungle", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureBigJungle::new);
   public static final RegistryObject<Feature<?>> TREE_BIG_SPRUCE =
      FEATURES.register("tree_big_spruce", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeatureBigSpruce::new);

   public static final RegistryObject<Feature<?>> TREE_PALM =
      FEATURES.register("tree_palm", com.paleimitations.schoolsofmagic.common.world.features.trees.SOMTreeFeaturePalm::new);

   public static final RegistryObject<Feature<?>> LABYRINTH_MAZE =
      FEATURES.register("labyrinth_maze", com.paleimitations.schoolsofmagic.common.world.features.LabyrinthMazeFeature::new);

   public static final RegistryObject<Feature<?>> FAE_FLOOR =
      FEATURES.register("fae_floor", com.paleimitations.schoolsofmagic.common.world.features.FaeFloorFeature::new);

   public static final RegistryObject<Feature<?>> STUMP =
      FEATURES.register("stump", com.paleimitations.schoolsofmagic.common.world.features.SOMStumpFeature::new);

   public static final RegistryObject<Feature<?>> DEAD_LOG =
      FEATURES.register("dead_log", com.paleimitations.schoolsofmagic.common.world.features.SOMDeadLogFeature::new);

   public static final RegistryObject<Feature<?>> WATER_SHORE_GRASS =
      FEATURES.register("water_shore_grass", com.paleimitations.schoolsofmagic.common.world.features.WaterShoreGrassFeature::new);

   public static void register(IEventBus modBus) {
      FEATURES.register(modBus);
   }
}
