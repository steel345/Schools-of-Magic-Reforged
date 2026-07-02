package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.handlers.SetRandomSpell;
import com.paleimitations.schoolsofmagic.common.handlers.SetRandomSpellModifier;
import com.paleimitations.schoolsofmagic.common.handlers.SetRandomSpellNote;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LootFunctionRegistry {

   public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS =
      DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SchoolsOfMagic.MODID);

   public static final RegistryObject<LootItemFunctionType> SET_RANDOM_SPELL =
      LOOT_FUNCTIONS.register("set_random_spell",
         () -> SetRandomSpell.TYPE = new LootItemFunctionType(new SetRandomSpell.Serializer()));

   public static final RegistryObject<LootItemFunctionType> SET_RANDOM_SPELL_NOTE =
      LOOT_FUNCTIONS.register("set_random_spell_note",
         () -> SetRandomSpellNote.TYPE = new LootItemFunctionType(new SetRandomSpellNote.Serializer()));

   public static final RegistryObject<LootItemFunctionType> SET_RANDOM_SPELL_MODIFIER =
      LOOT_FUNCTIONS.register("set_random_spell_modifier",
         () -> SetRandomSpellModifier.TYPE = new LootItemFunctionType(new SetRandomSpellModifier.Serializer()));

   public static final RegistryObject<LootItemFunctionType> SET_RANDOM_TEA =
      LOOT_FUNCTIONS.register("set_random_tea",
         () -> com.paleimitations.schoolsofmagic.common.handlers.SetRandomTea.TYPE = new LootItemFunctionType(new com.paleimitations.schoolsofmagic.common.handlers.SetRandomTea.Serializer()));

   public static final RegistryObject<LootItemFunctionType> SET_RANDOM_MAGIC_PLANT =
      LOOT_FUNCTIONS.register("set_random_magic_plant",
         () -> com.paleimitations.schoolsofmagic.common.handlers.SetRandomMagicPlant.TYPE = new LootItemFunctionType(new com.paleimitations.schoolsofmagic.common.handlers.SetRandomMagicPlant.Serializer()));

   public static void register(IEventBus bus) {
      LOOT_FUNCTIONS.register(bus);
   }
}
