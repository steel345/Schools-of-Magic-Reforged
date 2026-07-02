package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.common.entity.DryadQuests;
import com.paleimitations.schoolsofmagic.common.network.PacketHandler;
import com.paleimitations.schoolsofmagic.common.registries.BasinRecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.BiomeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.registries.BuyableRegistry;
import com.paleimitations.schoolsofmagic.common.registries.DimensionRegistry;
import com.paleimitations.schoolsofmagic.common.registries.EntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.FurnaceRecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MortarRecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TeaRecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MenuTypeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionRegistry;
import com.paleimitations.schoolsofmagic.common.registries.PotionTypeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.QuestRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RitualRecipeRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RitualRegistry;
import com.paleimitations.schoolsofmagic.common.registries.SpellRegistry;
import com.paleimitations.schoolsofmagic.common.registries.StructureRegistry;
import com.paleimitations.schoolsofmagic.common.registries.TileEntityRegistry;
import com.paleimitations.schoolsofmagic.common.registries.WorldGeneratorRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.event.server.ServerStartingEvent;

public class RegistryHandler {

   public static void register(IEventBus modBus) {
      BlockRegistry.register(modBus);
      ItemRegistry.register(modBus);
      TileEntityRegistry.register(modBus);
      EntityRegistry.register(modBus);
      PotionRegistry.register(modBus);
      PotionTypeRegistry.register(modBus);
      MenuTypeRegistry.register(modBus);
      StructureRegistry.register(modBus);
      SOMSoundHandler.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.GlobalLootModifierRegistry.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.LootFunctionRegistry.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.ParticleTypeRegistry.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.TreeFeatureRegistry.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.PatternRegistry.register(modBus);
      com.paleimitations.schoolsofmagic.common.registries.BiomeSourceRegistry.register(modBus);
   }

   public static void commonSetup(FMLCommonSetupEvent event) {
      event.enqueueWork(() -> {

         net.minecraftforge.common.crafting.CraftingHelper.register(
            com.paleimitations.schoolsofmagic.common.crafting.BlockStateVariantIngredient.ID,
            com.paleimitations.schoolsofmagic.common.crafting.BlockStateVariantIngredient.SERIALIZER);
         PacketHandler.registerMessages();

         FlammabilityHandler.register();
         MagicSchoolRegistry.init();
         MagicElementRegistry.init();
         SpellRegistry.init();
         RitualRegistry.init();
         QuestRegistry.init();
         DryadQuests.init();
         BuyableRegistry.register();
         FurnaceRecipeRegistry.initRecipes();
         MortarRecipeRegistry.init();
         TeaRecipeRegistry.init();
         RitualRecipeRegistry.register();
         com.paleimitations.schoolsofmagic.common.registries.RingRecipeRegistry.register();
         BasinRecipeRegistry.register();
         com.paleimitations.schoolsofmagic.common.registries.BrewingRegistry.register();

         BookPageRegistry.init();
         com.paleimitations.schoolsofmagic.common.registries.SOMComposter.register();
         StructureRegistry.initStructures();
         BiomeRegistry.registerBiomes();
         DimensionRegistry.registerDimensions();
         WorldGeneratorRegistry.mainRegistry();

      });

   }

   public static void loadComplete(FMLLoadCompleteEvent event) {

   }

   public static void serverStarting(ServerStartingEvent event) {

   }
}
