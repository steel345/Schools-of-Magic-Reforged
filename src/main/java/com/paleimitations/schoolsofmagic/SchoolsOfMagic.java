package com.paleimitations.schoolsofmagic;

import com.paleimitations.schoolsofmagic.client.ClientProxy;
import com.paleimitations.schoolsofmagic.common.CommonProxy;
import com.paleimitations.schoolsofmagic.common.handlers.RegistryHandler;
import com.paleimitations.schoolsofmagic.common.registries.CreativeTabRegistry;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SchoolsOfMagic.MODID)
public class SchoolsOfMagic {

   public static final String MODID = "som";
   public static SchoolsOfMagic instance;

   public static CommonProxy proxy = DistExecutor.unsafeRunForDist(
      () -> ClientProxy::new,
      () -> CommonProxy::new
   );

   public SchoolsOfMagic() {
      instance = this;

      IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

      RegistryHandler.register(modBus);

      CreativeTabRegistry.register(modBus);

      modBus.addListener(this::commonSetup);
      modBus.addListener(this::clientSetup);
      modBus.addListener(this::loadComplete);
      modBus.addListener(this::registerRenderers);

      ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON,
         com.paleimitations.schoolsofmagic.common.config.SOMFairyConfig.SPEC, "som-fairies.toml");

      MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
      MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
      MinecraftForge.EVENT_BUS.addListener(this::onFurnaceFuel);

      ModLoadingContext.get();
   }

   private void commonSetup(final FMLCommonSetupEvent event) {

      event.enqueueWork(() -> proxy.preInit());

      event.enqueueWork(com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes::init);

      RegistryHandler.commonSetup(event);

      if (net.minecraftforge.fml.ModList.get().isLoaded("terrablender")) {
         event.enqueueWork(com.paleimitations.schoolsofmagic.common.world.biomes.SOMTerraBlender::register);
      }

      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.UNICORN.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules));

      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.TOAD.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         com.paleimitations.schoolsofmagic.common.entity.EntityToad::checkToadSpawnRules));

      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.DRYAD.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         net.minecraft.world.entity.Mob::checkMobSpawnRules));
      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.SQUEAKARD.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         net.minecraft.world.entity.Mob::checkMobSpawnRules));
      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.FLOWER_FAE.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules));
      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.PHOENIX.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         (type, level, reason, pos, rand) -> true));
      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.FAIRY.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.ON_GROUND,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
         (type, level, reason, pos, rand) ->
            (com.paleimitations.schoolsofmagic.common.config.SOMFairyConfig.NATURAL_SPAWNING.get()
               || reason == net.minecraft.world.entity.MobSpawnType.SPAWN_EGG
               || reason == net.minecraft.world.entity.MobSpawnType.COMMAND)
            && net.minecraft.world.entity.Mob.checkMobSpawnRules(type, level, reason, pos, rand)));
      event.enqueueWork(() -> net.minecraft.world.entity.SpawnPlacements.register(
         com.paleimitations.schoolsofmagic.common.registries.EntityRegistry.ACOLYTE_WISP.get(),
         net.minecraft.world.entity.SpawnPlacements.Type.NO_RESTRICTIONS,
         net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING,
         com.paleimitations.schoolsofmagic.common.entity.EntityAcolyteWisp::checkSpawnRules));
   }

   private void clientSetup(final FMLClientSetupEvent event) {
      proxy.Init();

      event.enqueueWork(com.paleimitations.schoolsofmagic.common.handlers.GuiHandler::register);
      event.enqueueWork(com.paleimitations.schoolsofmagic.client.SOMItemModelProperties::register);
      event.enqueueWork(com.paleimitations.schoolsofmagic.client.SOMRenderLayers::register);
      event.enqueueWork(com.paleimitations.schoolsofmagic.common.blocks.SOMWoodTypes::registerAtlas);
   }

   private void loadComplete(final FMLLoadCompleteEvent event) {
      proxy.postInit();
      RegistryHandler.loadComplete(event);
   }

   private void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {

      proxy.registerTileEntityRenders(event);
   }

   private void serverAboutToStart(final net.minecraftforge.event.server.ServerAboutToStartEvent event) {
      net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager stm = event.getServer().getStructureManager();
      for (String n : new String[]{"acolyte_portal","mushroom_house","mushroom_house_evermore","ziggurat",
            "mini_pyromancy","mini_heliomancy","mini_aeromancy","mini_geomancy","mini_animancy","mini_electromancy",
            "mini_hydromancy","mini_cryomancy","mini_hieromancy","mini_chaotics","mini_auramancy","mini_astromancy",
            "mini_infernality","mini_spectromancy","mini_umbramancy","mini_necromancy"}) {
         stm.getOrCreate(new net.minecraft.resources.ResourceLocation(MODID, n));
      }
   }

   private void serverStarting(final ServerStartingEvent event) {
      RegistryHandler.serverStarting(event);
   }

   private void onFurnaceFuel(final net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent event) {
      net.minecraft.world.item.Item item = event.getItemStack().getItem();
      if (item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_ash.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_elder.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_pine.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_willow.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_yew.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_verde.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_log_palm.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_wood_ash.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_wood_elder.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_wood_pine.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_wood_willow.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_wood_yew.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_wood_verde.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_stripped_wood_ash.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_stripped_wood_elder.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_stripped_wood_pine.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_stripped_wood_willow.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_stripped_wood_yew.get()
            || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_stripped_wood_verde.get()) {
         event.setBurnTime(300);
      } else if (item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.bi_block_charcoal.get()) {
         event.setBurnTime(14400);
      }
   }
}
