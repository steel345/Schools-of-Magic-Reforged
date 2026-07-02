package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.util.References;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PatternRegistry {

   public static final DeferredRegister<BannerPattern> PATTERNS =
      DeferredRegister.create(Registries.BANNER_PATTERN, References.MODID);

   public static final RegistryObject<BannerPattern> PYROMANCY =
      PATTERNS.register("pyromancy", () -> new BannerPattern("som_pyromancy"));
   public static final RegistryObject<BannerPattern> HELIOMANCY =
      PATTERNS.register("heliomancy", () -> new BannerPattern("som_heliomancy"));
   public static final RegistryObject<BannerPattern> AEROMANCY =
      PATTERNS.register("aeromancy", () -> new BannerPattern("som_aeromancy"));
   public static final RegistryObject<BannerPattern> GEOMANCY =
      PATTERNS.register("geomancy", () -> new BannerPattern("som_geomancy"));
   public static final RegistryObject<BannerPattern> ANIMANCY =
      PATTERNS.register("animancy", () -> new BannerPattern("som_animancy"));
   public static final RegistryObject<BannerPattern> ELECTROMANCY =
      PATTERNS.register("electromancy", () -> new BannerPattern("som_electromancy"));
   public static final RegistryObject<BannerPattern> HYDROMANCY =
      PATTERNS.register("hydromancy", () -> new BannerPattern("som_hydromancy"));
   public static final RegistryObject<BannerPattern> CRYOMANCY =
      PATTERNS.register("cryomancy", () -> new BannerPattern("som_cryomancy"));
   public static final RegistryObject<BannerPattern> HIEROMANCY =
      PATTERNS.register("hieromancy", () -> new BannerPattern("som_hieromancy"));
   public static final RegistryObject<BannerPattern> CHAOTICS =
      PATTERNS.register("chaotics", () -> new BannerPattern("som_chaotics"));
   public static final RegistryObject<BannerPattern> AURAMANCY =
      PATTERNS.register("auramancy", () -> new BannerPattern("som_auramancy"));
   public static final RegistryObject<BannerPattern> ASTROMANCY =
      PATTERNS.register("astromancy", () -> new BannerPattern("som_astromancy"));
   public static final RegistryObject<BannerPattern> INFERNALITY =
      PATTERNS.register("infernality", () -> new BannerPattern("som_infernality"));
   public static final RegistryObject<BannerPattern> SPECTROMANCY =
      PATTERNS.register("spectromancy", () -> new BannerPattern("som_spectromancy"));
   public static final RegistryObject<BannerPattern> UMBRAMANCY =
      PATTERNS.register("umbramancy", () -> new BannerPattern("som_umbramancy"));
   public static final RegistryObject<BannerPattern> NECROMANCY =
      PATTERNS.register("necromancy", () -> new BannerPattern("som_necromancy"));

   public static TagKey<BannerPattern> patternTag(String element) {
      return TagKey.create(Registries.BANNER_PATTERN, new ResourceLocation(References.MODID, element));
   }

   public static void register(IEventBus bus) {
      PATTERNS.register(bus);
   }
}
