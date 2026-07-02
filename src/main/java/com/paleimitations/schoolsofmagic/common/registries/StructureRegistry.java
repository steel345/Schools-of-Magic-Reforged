package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureRegistry {

   public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
      DeferredRegister.create(Registries.STRUCTURE_TYPE, SchoolsOfMagic.MODID);

   public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
      DeferredRegister.create(Registries.STRUCTURE_PIECE, SchoolsOfMagic.MODID);

   public static final DeferredRegister<net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType<?>> STRUCTURE_PROCESSORS =
      DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, SchoolsOfMagic.MODID);

   public static final RegistryObject<net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType<com.paleimitations.schoolsofmagic.common.world.features.structures.CloseDoorsProcessor>> CLOSE_DOORS =
      STRUCTURE_PROCESSORS.register("close_doors",
         () -> () -> com.paleimitations.schoolsofmagic.common.world.features.structures.CloseDoorsProcessor.CODEC);

   public static final RegistryObject<StructureType<com.paleimitations.schoolsofmagic.common.world.features.structures.SOMZigguratStructure>> ZIGGURAT =
      STRUCTURE_TYPES.register("ziggurat",
         () -> () -> com.paleimitations.schoolsofmagic.common.world.features.structures.SOMZigguratStructure.CODEC);

   public static final RegistryObject<StructurePieceType> ZIGGURAT_PIECE =
      STRUCTURE_PIECES.register("ziggurat",
         () -> (StructurePieceType.StructureTemplateType)(manager, tag) ->
            new com.paleimitations.schoolsofmagic.common.world.features.structures.ZigguratPiece(manager, tag));

   public static final RegistryObject<StructureType<com.paleimitations.schoolsofmagic.common.world.features.structures.ShrineStructure>> SHRINE =
      STRUCTURE_TYPES.register("shrine",
         () -> () -> com.paleimitations.schoolsofmagic.common.world.features.structures.ShrineStructure.CODEC);

   public static final RegistryObject<StructurePieceType> SHRINE_PIECE =
      STRUCTURE_PIECES.register("shrine",
         () -> (StructurePieceType.StructureTemplateType)(manager, tag) ->
            new com.paleimitations.schoolsofmagic.common.world.features.structures.ShrinePiece(manager, tag));

   public static final RegistryObject<StructureType<com.paleimitations.schoolsofmagic.common.world.features.structures.AcolytePortalStructure>> ACOLYTE_PORTAL =
      STRUCTURE_TYPES.register("acolyte_portal",
         () -> () -> com.paleimitations.schoolsofmagic.common.world.features.structures.AcolytePortalStructure.CODEC);

   public static final RegistryObject<StructurePieceType> ACOLYTE_PORTAL_PIECE =
      STRUCTURE_PIECES.register("acolyte_portal",
         () -> (StructurePieceType.StructureTemplateType)(manager, tag) ->
            new com.paleimitations.schoolsofmagic.common.world.features.structures.AcolytePortalPiece(manager, tag));

   public static final RegistryObject<StructureType<com.paleimitations.schoolsofmagic.common.world.features.structures.MushroomHouseStructure>> MUSHROOM_HOUSE =
      STRUCTURE_TYPES.register("mushroom_house",
         () -> () -> com.paleimitations.schoolsofmagic.common.world.features.structures.MushroomHouseStructure.CODEC);

   public static final RegistryObject<StructurePieceType> MUSHROOM_HOUSE_PIECE =
      STRUCTURE_PIECES.register("mushroom_house",
         () -> (StructurePieceType.StructureTemplateType)(manager, tag) ->
            new com.paleimitations.schoolsofmagic.common.world.features.structures.MushroomHousePiece(manager, tag));

   public static void register(IEventBus bus) {
      STRUCTURE_TYPES.register(bus);
      STRUCTURE_PIECES.register(bus);
      STRUCTURE_PROCESSORS.register(bus);
   }

   public static void initStructures() {

   }
}
