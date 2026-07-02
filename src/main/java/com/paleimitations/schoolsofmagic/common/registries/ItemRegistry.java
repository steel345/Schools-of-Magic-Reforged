package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SchoolsOfMagic.MODID);

   public static final java.util.Map<String, RegistryObject<Item>> BOAT_ITEMS = new java.util.HashMap<>();
   public static final java.util.Map<String, RegistryObject<Item>> CHEST_BOAT_ITEMS = new java.util.HashMap<>();
   static {
      for (String w : com.paleimitations.schoolsofmagic.common.entity.boat.SOMBoats.WOODS) {
         final String wood = w;
         BOAT_ITEMS.put(wood, ITEMS.register("boat_" + wood,
            () -> new SOMBoatItem(false, wood, new Item.Properties().stacksTo(1))));
         CHEST_BOAT_ITEMS.put(wood, ITEMS.register("chest_boat_" + wood,
            () -> new SOMBoatItem(true, wood, new Item.Properties().stacksTo(1))));
      }
   }

   public static final RegistryObject<Item> sword_light = ITEMS.register("sword_light",
      () -> new SOMSword(SOMTiers.LIGHT, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_light = ITEMS.register("pickaxe_light",
      () -> new SOMPickax(SOMTiers.LIGHT, new Item.Properties()));
   public static final RegistryObject<Item> axe_light = ITEMS.register("axe_light",
      () -> new SOMAxe(SOMTiers.LIGHT, 5.0F, -3.0F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_light = ITEMS.register("shovel_light",
      () -> new SOMShovel(SOMTiers.LIGHT, new Item.Properties()));
   public static final RegistryObject<Item> hoe_light = ITEMS.register("hoe_light",
      () -> new SOMHoe(SOMTiers.LIGHT, -3, 0.0F, new Item.Properties()));

   public static final RegistryObject<Item> magic_book = ITEMS.register("magic_book",
      () -> new ItemMagicBook(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> basic_spellbook = ITEMS.register("basic_spellbook",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> intermediate_spellbook = ITEMS.register("intermediate_spellbook",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> advanced_spellbook = ITEMS.register("advanced_spellbook",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> complete_spellbook = ITEMS.register("complete_spellbook",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> alchemical_manual = ITEMS.register("alchemical_manual",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> exploration_book = ITEMS.register("exploration_book",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> tea_makers_guide = ITEMS.register("tea_makers_guide",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> spellworkers_handbook = ITEMS.register("spellworkers_handbook",
      () -> new ItemBookBase(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> spellbook = ITEMS.register("spellbook",
      () -> new ItemSpellbook(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> grimoire_page = ITEMS.register("grimoire_page",
      () -> new ItemPageBase(new Item.Properties()));
   public static final RegistryObject<Item> spell_note = ITEMS.register("spell_note",
      () -> new ItemSpellNotes(new Item.Properties()));
   public static final RegistryObject<Item> spell_modifier_scroll = ITEMS.register("spell_modifier_scroll",
      () -> new ItemSpellModifierScroll(new Item.Properties()));
   public static final RegistryObject<Item> spell_parchment = ITEMS.register("spell_parchment",
      () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> scroll_seal = ITEMS.register("scroll_seal",
      () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> quest_note = ITEMS.register("quest_note",
      () -> new ItemQuest(new Item.Properties()));
   public static final RegistryObject<Item> sticker = ITEMS.register("sticker",
      () -> new ItemSticker(new Item.Properties()));
   public static final RegistryObject<Item> sticker_pack = ITEMS.register("sticker_pack",
      () -> new ItemStickerPack(new Item.Properties()));
   public static final RegistryObject<Item> bell = ITEMS.register("bell",
      () -> new ItemBell(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> ziggurat_door_key = ITEMS.register("ziggurat_door_key",
      () -> new ItemDoorWallKey(new Item.Properties().stacksTo(1)));

   public static final RegistryObject<Item> wand_apprentice = ITEMS.register("wand_apprentice",
      () -> new ItemApprenticeWand(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> wand_advanced = ITEMS.register("wand_advanced",
      () -> new ItemAdvancedWand(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> apprentice_ring = ITEMS.register("apprentice_ring",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing(new Item.Properties().stacksTo(1)));

   public static final RegistryObject<Item> potion_bag = ITEMS.register("potion_bag",
      () -> new com.paleimitations.schoolsofmagic.common.items.PotionBag(new Item.Properties().stacksTo(1)));

   public static final RegistryObject<Item> seed_magic_plant = ITEMS.register("seed_magic_plant",
      () -> new ItemMagicPlantSeed(new Item.Properties()));
   public static final RegistryObject<Item> seed_mushroom_dark = ITEMS.register("seed_mushroom_dark",
      () -> new SOMSeed(new Item.Properties()));
   public static final RegistryObject<Item> seed_mushroom_white = ITEMS.register("seed_mushroom_white",
      () -> new SOMSeed(new Item.Properties()));
   public static final RegistryObject<Item> seed_mushroom_grey = ITEMS.register("seed_mushroom_grey",
      () -> new SOMSeed(new Item.Properties()));
   public static final RegistryObject<Item> seed_mushroom_pink = ITEMS.register("seed_mushroom_pink",
      () -> new SOMSeed(new Item.Properties()));

   public static final RegistryObject<Item> nugget = ITEMS.register("nugget",
      () -> new ItemMetal(new Item.Properties()));
   public static final RegistryObject<Item> ingot = ITEMS.register("ingot",
      () -> new ItemMetal(new Item.Properties()));
   public static final RegistryObject<Item> copper_key = ITEMS.register("copper_key",
      () -> new ItemCopperKey(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> item = ITEMS.register("item",
      () -> new ItemMisc(new Item.Properties()));

   public static final RegistryObject<Item> brambleberry = ITEMS.register("brambleberry",
      () -> new SOMFood(new Item.Properties(), com.paleimitations.schoolsofmagic.common.items.SOMFood.BRAMBLEBERRY_FOOD));
   public static final RegistryObject<Item> brambleberry_toast = ITEMS.register("brambleberry_toast",
      () -> new SOMFood(new Item.Properties()));
   public static final RegistryObject<Item> dried_plant = ITEMS.register("dried_plant",
      () -> new ItemPlant(new Item.Properties()));
   public static final RegistryObject<Item> crushed_plant = ITEMS.register("crushed_plant",
      () -> new ItemPlant(new Item.Properties()));
   public static final RegistryObject<Item> magic_letter = ITEMS.register("magic_letter",
      () -> new ItemLetter(new Item.Properties()));

   public static final RegistryObject<Item> teacup_empty = ITEMS.register("teacup_empty",
      () -> new ItemPotionry(new Item.Properties().stacksTo(64)));

   public static final RegistryObject<Item> teacup = ITEMS.register("teacup",
      () -> new ItemTea(new Item.Properties().stacksTo(64)));

   public static final RegistryObject<Item> bottle_empty = ITEMS.register("bottle_empty",
      () -> new ItemPotionry(new Item.Properties().stacksTo(64)));
   public static final RegistryObject<Item> bottle = ITEMS.register("bottle",
      () -> new ItemBottle(new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> bottle_egg = ITEMS.register("bottle_egg",
      () -> new ItemPotionry(new Item.Properties().stacksTo(16)));

   public static final RegistryObject<Item> potion_drinkable = ITEMS.register("potion_drinkable",
      () -> new ItemPotionDrink(new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> potion_jug = ITEMS.register("potion_jug",
      () -> new ItemPotionDrink(new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> potion_throwable = ITEMS.register("potion_throwable",
      () -> new ItemPotionSplash(new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> potion_lingering = ITEMS.register("potion_lingering",
      () -> new ItemPotionLingering(new Item.Properties().stacksTo(16)));

   public static final RegistryObject<Item> burst_bottle = ITEMS.register("burst_bottle",
      () -> new ItemPotionBurst(new Item.Properties().stacksTo(64)));
   public static final RegistryObject<Item> potion_burst = ITEMS.register("potion_burst",
      () -> new ItemPotionBurst(new Item.Properties().stacksTo(16)));

   public static final RegistryObject<Item> potion_crystal = ITEMS.register("potion_crystal",
      () -> new ItemPotionCrystal(new Item.Properties().durability(24)));

   public static final RegistryObject<Item> potion_charm = ITEMS.register("potion_charm",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemPotionCharm(new Item.Properties().durability(24)));
   public static final RegistryObject<Item> talisman_of_knowledge = ITEMS.register("talisman_of_knowledge",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemTalismanOfKnowledge(new Item.Properties().stacksTo(1)));

   public static final RegistryObject<Item> helmet_obsidian = ITEMS.register("helmet_obsidian",
      () -> new SOMArmor(net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> chestplate_obsidian = ITEMS.register("chestplate_obsidian",
      () -> new SOMArmor(net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> leggings_obsidian = ITEMS.register("leggings_obsidian",
      () -> new SOMArmor(net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> boots_obsidian = ITEMS.register("boots_obsidian",
      () -> new SOMArmor(net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> sword_obsidian = ITEMS.register("sword_obsidian",
      () -> new SOMSword(SOMTiers.OBSIDIAN, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_obsidian = ITEMS.register("pickaxe_obsidian",
      () -> new SOMPickax(SOMTiers.OBSIDIAN, new Item.Properties()));
   public static final RegistryObject<Item> axe_obsidian = ITEMS.register("axe_obsidian",
      () -> new SOMAxe(SOMTiers.OBSIDIAN, 5.0F, -3.2F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_obsidian = ITEMS.register("shovel_obsidian",
      () -> new SOMShovel(SOMTiers.OBSIDIAN, new Item.Properties()));
   public static final RegistryObject<Item> hoe_obsidian = ITEMS.register("hoe_obsidian",
      () -> new SOMHoe(SOMTiers.OBSIDIAN, -4, 0.0F, new Item.Properties()));
   public static final RegistryObject<Item> helmet_silver = ITEMS.register("helmet_silver", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.SILVER, net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> chestplate_silver = ITEMS.register("chestplate_silver", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.SILVER, net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> leggings_silver = ITEMS.register("leggings_silver", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.SILVER, net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> boots_silver = ITEMS.register("boots_silver", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.SILVER, net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> sword_silver = ITEMS.register("sword_silver", () -> new SOMSword(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.SILVER, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_silver = ITEMS.register("pickaxe_silver", () -> new SOMPickax(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.SILVER, new Item.Properties()));
   public static final RegistryObject<Item> axe_silver = ITEMS.register("axe_silver", () -> new SOMAxe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.SILVER, 6.0F, -3.1F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_silver = ITEMS.register("shovel_silver", () -> new SOMShovel(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.SILVER, new Item.Properties()));
   public static final RegistryObject<Item> hoe_silver = ITEMS.register("hoe_silver", () -> new SOMHoe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.SILVER, -2, -1.0F, new Item.Properties()));
   public static final RegistryObject<Item> helmet_copper = ITEMS.register("helmet_copper", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.COPPER, net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> chestplate_copper = ITEMS.register("chestplate_copper", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.COPPER, net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> leggings_copper = ITEMS.register("leggings_copper", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.COPPER, net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> boots_copper = ITEMS.register("boots_copper", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.COPPER, net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> sword_copper = ITEMS.register("sword_copper", () -> new SOMSword(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.COPPER, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_copper = ITEMS.register("pickaxe_copper", () -> new SOMPickax(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.COPPER, new Item.Properties()));
   public static final RegistryObject<Item> axe_copper = ITEMS.register("axe_copper", () -> new SOMAxe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.COPPER, 6.0F, -3.1F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_copper = ITEMS.register("shovel_copper", () -> new SOMShovel(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.COPPER, new Item.Properties()));
   public static final RegistryObject<Item> hoe_copper = ITEMS.register("hoe_copper", () -> new SOMHoe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.COPPER, -2, -1.0F, new Item.Properties()));
   public static final RegistryObject<Item> helmet_bronze = ITEMS.register("helmet_bronze", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRONZE, net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> chestplate_bronze = ITEMS.register("chestplate_bronze", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRONZE, net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> leggings_bronze = ITEMS.register("leggings_bronze", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRONZE, net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> boots_bronze = ITEMS.register("boots_bronze", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRONZE, net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> sword_bronze = ITEMS.register("sword_bronze", () -> new SOMSword(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRONZE, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_bronze = ITEMS.register("pickaxe_bronze", () -> new SOMPickax(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRONZE, new Item.Properties()));
   public static final RegistryObject<Item> axe_bronze = ITEMS.register("axe_bronze", () -> new SOMAxe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRONZE, 6.0F, -3.1F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_bronze = ITEMS.register("shovel_bronze", () -> new SOMShovel(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRONZE, new Item.Properties()));
   public static final RegistryObject<Item> hoe_bronze = ITEMS.register("hoe_bronze", () -> new SOMHoe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRONZE, -2, -1.0F, new Item.Properties()));
   public static final RegistryObject<Item> helmet_brass = ITEMS.register("helmet_brass", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRASS, net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> chestplate_brass = ITEMS.register("chestplate_brass", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRASS, net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> leggings_brass = ITEMS.register("leggings_brass", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRASS, net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> boots_brass = ITEMS.register("boots_brass", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.BRASS, net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> sword_brass = ITEMS.register("sword_brass", () -> new SOMSword(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRASS, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_brass = ITEMS.register("pickaxe_brass", () -> new SOMPickax(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRASS, new Item.Properties()));
   public static final RegistryObject<Item> axe_brass = ITEMS.register("axe_brass", () -> new SOMAxe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRASS, 5.0F, -3.1F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_brass = ITEMS.register("shovel_brass", () -> new SOMShovel(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRASS, new Item.Properties()));
   public static final RegistryObject<Item> hoe_brass = ITEMS.register("hoe_brass", () -> new SOMHoe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.BRASS, -2, -1.0F, new Item.Properties()));
   public static final RegistryObject<Item> helmet_steel = ITEMS.register("helmet_steel", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.STEEL, net.minecraft.world.item.ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> chestplate_steel = ITEMS.register("chestplate_steel", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.STEEL, net.minecraft.world.item.ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> leggings_steel = ITEMS.register("leggings_steel", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.STEEL, net.minecraft.world.item.ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> boots_steel = ITEMS.register("boots_steel", () -> new com.paleimitations.schoolsofmagic.common.items.MetalArmor(com.paleimitations.schoolsofmagic.common.items.MetalArmorMaterial.STEEL, net.minecraft.world.item.ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> sword_steel = ITEMS.register("sword_steel", () -> new SOMSword(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.STEEL, new Item.Properties()));
   public static final RegistryObject<Item> pickaxe_steel = ITEMS.register("pickaxe_steel", () -> new SOMPickax(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.STEEL, new Item.Properties()));
   public static final RegistryObject<Item> axe_steel = ITEMS.register("axe_steel", () -> new SOMAxe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.STEEL, 7.0F, -3.1F, new Item.Properties()));
   public static final RegistryObject<Item> shovel_steel = ITEMS.register("shovel_steel", () -> new SOMShovel(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.STEEL, new Item.Properties()));
   public static final RegistryObject<Item> hoe_steel = ITEMS.register("hoe_steel", () -> new SOMHoe(com.paleimitations.schoolsofmagic.common.items.SOMMetalTiers.STEEL, -2, -1.0F, new Item.Properties()));
   public static final RegistryObject<Item> athame = ITEMS.register("athame",
      () -> new SOMSword(SOMTiers.ATHAME, -2.0F, new Item.Properties()));
   public static final RegistryObject<Item> bone_knife = ITEMS.register("bone_knife",
      () -> new SOMSword(SOMTiers.BONE_KNIFE, -1.6F, new Item.Properties()));

   public static final RegistryObject<Item> shard_netherstar = ITEMS.register("shard_netherstar",
      () -> new ItemGeomancy(new Item.Properties()));
   public static final RegistryObject<Item> gem_chunk = ITEMS.register("gem_chunk",
      () -> new ItemMagicGem(new Item.Properties()));
   public static final RegistryObject<Item> gem_dust = ITEMS.register("gem_dust",
      () -> new ItemMagicGem(new Item.Properties()));
   public static final RegistryObject<Item> sandpaper = ITEMS.register("sandpaper",
      () -> new SOMSandpaper(new Item.Properties().durability(10)));

   public static final RegistryObject<Item> item_duckweed = ITEMS.register("item_duckweed",
      () -> new ItemWaterplantPlacer(new Item.Properties()));
   public static final RegistryObject<Item> item_bladderwort = ITEMS.register("item_bladderwort",
      () -> new ItemWaterplantPlacer(new Item.Properties()));
   public static final RegistryObject<Item> item_algae = ITEMS.register("item_algae",
      () -> new ItemWaterplantPlacer(new Item.Properties()));
   public static final RegistryObject<Item> item_cattail = ITEMS.register("item_cattail",
      () -> new ItemWaterplantPlacer(new Item.Properties()));

   public static final RegistryObject<Item> ingredient = ITEMS.register("ingredient",
      () -> new ItemIngredient(new Item.Properties()));
   public static final RegistryObject<Item> feather_thunderbird = ITEMS.register("feather_thunderbird",
      () -> new ItemPotionry(new Item.Properties()));
   public static final RegistryObject<Item> feather_phoenix = ITEMS.register("feather_phoenix",
      () -> new ItemPotionry(new Item.Properties()));
   public static final RegistryObject<Item> brass_whistle = ITEMS.register("brass_whistle",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemBrassWhistle(new Item.Properties()));
   public static final RegistryObject<Item> mysterious_application = ITEMS.register("mysterious_application",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemMysteriousApplication(new Item.Properties()));
   public static final RegistryObject<Item> toad_spawn = ITEMS.register("toad_spawn",
      () -> new ToadSpawnPlacer(new Item.Properties()));
   public static final RegistryObject<Item> horn_unicorn = ITEMS.register("horn_unicorn",
      () -> new ItemPotionry(new Item.Properties()));
   public static final RegistryObject<Item> crushed_horn_unicorn = ITEMS.register("crushed_horn_unicorn",
      () -> new ItemPotionry(new Item.Properties()));

   public static final RegistryObject<Item> wand_core = ITEMS.register("wand_core",
      () -> new ItemWandCore(new Item.Properties()));

   public static final RegistryObject<Item> tree_item = ITEMS.register("tree_item",
      () -> new ItemTree(new Item.Properties()));
   public static final RegistryObject<Item> itemdoor_ash = ITEMS.register("itemdoor_ash",
      () -> new SOMItemDoor(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> itemdoor_elder = ITEMS.register("itemdoor_elder",
      () -> new SOMItemDoor(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> itemdoor_pine = ITEMS.register("itemdoor_pine",
      () -> new SOMItemDoor(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> itemdoor_verde = ITEMS.register("itemdoor_verde",
      () -> new SOMItemDoor(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> itemdoor_willow = ITEMS.register("itemdoor_willow",
      () -> new SOMItemDoor(new Item.Properties().stacksTo(1)));
   public static final RegistryObject<Item> itemdoor_yew = ITEMS.register("itemdoor_yew",
      () -> new SOMItemDoor(new Item.Properties().stacksTo(1)));

   public static final RegistryObject<Item> bi_brazier = ITEMS.register("brazier", () -> new com.paleimitations.schoolsofmagic.common.items.BrazierBlockItem(BlockRegistry.brazier.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_catalyst_basin = ITEMS.register("catalyst_basin", () -> new BlockItem(BlockRegistry.catalyst_basin.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_unlit_torch = ITEMS.register("unlit_torch", () -> new net.minecraft.world.item.StandingAndWallBlockItem(BlockRegistry.unlit_torch.get(), BlockRegistry.unlit_wall_torch.get(), new Item.Properties(), net.minecraft.core.Direction.DOWN));
   public static final RegistryObject<Item> bi_phantom_fire = ITEMS.register("phantom_fire", () -> new BlockItem(BlockRegistry.phantom_fire.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_block_charcoal = ITEMS.register("block_charcoal", () -> new BlockItem(BlockRegistry.block_charcoal.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_obsidian_pressure_plate = ITEMS.register("obsidian_pressure_plate", () -> new BlockItem(BlockRegistry.obsidian_pressure_plate.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_cauldron = ITEMS.register("cauldron",
      () -> new CauldronBlockItem(BlockRegistry.cauldron.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_spell_forge = ITEMS.register("spell_forge",
      () -> new BlockItem(BlockRegistry.spell_forge.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_spell_obelisk = ITEMS.register("spell_obelisk", () -> new SOMBerBlockItem(
      BlockRegistry.spell_obelisk.get(), new Item.Properties(),
      new net.minecraft.resources.ResourceLocation("som", "spell_obelisk"),
      new net.minecraft.resources.ResourceLocation("som", "textures/entity/spellobelisk.png")));

   public static final RegistryObject<Item> bi_divination_crystal = ITEMS.register("divination_crystal", () -> new BlockItem(BlockRegistry.divination_crystal.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mortnpest = ITEMS.register("mortnpest", () -> new BlockItem(BlockRegistry.mortnpest.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_teapot = ITEMS.register("teapot", () -> new BlockItem(BlockRegistry.teapot.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plate = ITEMS.register("plate", () -> new BlockItem(BlockRegistry.plate.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_demon_heart = ITEMS.register("demon_heart", () -> new SOMBerBlockItem(
      BlockRegistry.demon_heart.get(), new Item.Properties(),
      new net.minecraft.resources.ResourceLocation("som", "demon_heart"),
      new net.minecraft.resources.ResourceLocation("som", "textures/entity/demon_heart.png")));
   public static final RegistryObject<Item> bi_sacrificial_altar = ITEMS.register("sacrificial_altar", () -> new BlockItem(BlockRegistry.sacrificial_altar.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_sandstone_tablet = ITEMS.register("sandstone_tablet", () -> new BlockItem(BlockRegistry.sandstone_tablet.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_zig_mural = ITEMS.register("zig_mural", () -> new BlockItem(BlockRegistry.zig_mural.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_vase1 = ITEMS.register("vase1", () -> new BlockItem(BlockRegistry.vase1.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_vase2 = ITEMS.register("vase2", () -> new BlockItem(BlockRegistry.vase2.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_vase_big1 = ITEMS.register("vase_big1", () -> new BlockItem(BlockRegistry.vase_big1.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_vase_big2 = ITEMS.register("vase_big2", () -> new BlockItem(BlockRegistry.vase_big2.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base = ITEMS.register("trap_spike_base", () -> new BlockItem(BlockRegistry.trap_spike_base.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_fae = ITEMS.register("trap_spike_base_fae", () -> new BlockItem(BlockRegistry.trap_spike_base_fae.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_sandstone = ITEMS.register("trap_spike_base_sandstone", () -> new BlockItem(BlockRegistry.trap_spike_base_sandstone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_red_sandstone = ITEMS.register("trap_spike_base_red_sandstone", () -> new BlockItem(BlockRegistry.trap_spike_base_red_sandstone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_nether = ITEMS.register("trap_spike_base_nether", () -> new BlockItem(BlockRegistry.trap_spike_base_nether.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_purpur = ITEMS.register("trap_spike_base_purpur", () -> new BlockItem(BlockRegistry.trap_spike_base_purpur.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_quartz = ITEMS.register("trap_spike_base_quartz", () -> new BlockItem(BlockRegistry.trap_spike_base_quartz.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta = ITEMS.register("trap_spike_base_terracotta", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_red = ITEMS.register("trap_spike_base_terracotta_red", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_red.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_orange = ITEMS.register("trap_spike_base_terracotta_orange", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_orange.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_yellow = ITEMS.register("trap_spike_base_terracotta_yellow", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_yellow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_lime = ITEMS.register("trap_spike_base_terracotta_lime", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_lime.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_green = ITEMS.register("trap_spike_base_terracotta_green", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_green.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_cyan = ITEMS.register("trap_spike_base_terracotta_cyan", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_cyan.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_light_blue = ITEMS.register("trap_spike_base_terracotta_light_blue", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_light_blue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_blue = ITEMS.register("trap_spike_base_terracotta_blue", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_blue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_purple = ITEMS.register("trap_spike_base_terracotta_purple", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_purple.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_magenta = ITEMS.register("trap_spike_base_terracotta_magenta", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_magenta.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_pink = ITEMS.register("trap_spike_base_terracotta_pink", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_pink.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_white = ITEMS.register("trap_spike_base_terracotta_white", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_white.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_silver = ITEMS.register("trap_spike_base_terracotta_silver", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_silver.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_gray = ITEMS.register("trap_spike_base_terracotta_gray", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_gray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_black = ITEMS.register("trap_spike_base_terracotta_black", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_black.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike_base_terracotta_brown = ITEMS.register("trap_spike_base_terracotta_brown", () -> new BlockItem(BlockRegistry.trap_spike_base_terracotta_brown.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trap_spike = ITEMS.register("trap_spike", () -> new BlockItem(BlockRegistry.trap_spike.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mystic_gold_block = ITEMS.register("mystic_gold_block", () -> new BlockItem(BlockRegistry.mystic_gold_block.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_block_gold_coins = ITEMS.register("block_gold_coins", () -> new BlockItem(BlockRegistry.block_gold_coins.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum = ITEMS.register("gypsum", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.gypsum.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_ore = ITEMS.register("gypsum_ore", () -> new BlockItem(BlockRegistry.gypsum_ore.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_keystone = ITEMS.register("gypsum_keystone", () -> new BlockItem(BlockRegistry.gypsum_keystone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_stalactite = ITEMS.register("gypsum_stalactite", () -> new BlockItem(BlockRegistry.gypsum_stalactite.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_pillar = ITEMS.register("gypsum_pillar", () -> new BlockItem(BlockRegistry.gypsum_pillar.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_gypsum_stone = ITEMS.register("stairs_gypsum_stone", () -> new BlockItem(BlockRegistry.stairs_gypsum_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_gypsum_cobble = ITEMS.register("stairs_gypsum_cobble", () -> new BlockItem(BlockRegistry.stairs_gypsum_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_gypsum_bricks = ITEMS.register("stairs_gypsum_bricks", () -> new BlockItem(BlockRegistry.stairs_gypsum_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_gypsum_stone = ITEMS.register("doubleslab_gypsum_stone", () -> new BlockItem(BlockRegistry.doubleslab_gypsum_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_gypsum_cobble = ITEMS.register("doubleslab_gypsum_cobble", () -> new BlockItem(BlockRegistry.doubleslab_gypsum_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_gypsum_bricks = ITEMS.register("doubleslab_gypsum_bricks", () -> new BlockItem(BlockRegistry.doubleslab_gypsum_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_gypsum_stone = ITEMS.register("halfslab_gypsum_stone", () -> new BlockItem(BlockRegistry.halfslab_gypsum_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_gypsum_cobble = ITEMS.register("halfslab_gypsum_cobble", () -> new BlockItem(BlockRegistry.halfslab_gypsum_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_gypsum_bricks = ITEMS.register("halfslab_gypsum_bricks", () -> new BlockItem(BlockRegistry.halfslab_gypsum_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_stone_wall = ITEMS.register("gypsum_stone_wall", () -> new BlockItem(BlockRegistry.gypsum_stone_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_cobble_wall = ITEMS.register("gypsum_cobble_wall", () -> new BlockItem(BlockRegistry.gypsum_cobble_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gypsum_brick_wall = ITEMS.register("gypsum_brick_wall", () -> new BlockItem(BlockRegistry.gypsum_brick_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_stone = ITEMS.register("fae_stone", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.fae_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_ore = ITEMS.register("fae_ore", () -> new BlockItem(BlockRegistry.fae_ore.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_keystone = ITEMS.register("fae_keystone", () -> new BlockItem(BlockRegistry.fae_keystone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_stalactite = ITEMS.register("fae_stalactite", () -> new BlockItem(BlockRegistry.fae_stalactite.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_pillar = ITEMS.register("fae_pillar", () -> new BlockItem(BlockRegistry.fae_pillar.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_fae_stone = ITEMS.register("stairs_fae_stone", () -> new BlockItem(BlockRegistry.stairs_fae_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_fae_cobble = ITEMS.register("stairs_fae_cobble", () -> new BlockItem(BlockRegistry.stairs_fae_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_fae_bricks = ITEMS.register("stairs_fae_bricks", () -> new BlockItem(BlockRegistry.stairs_fae_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_fae_stone = ITEMS.register("doubleslab_fae_stone", () -> new BlockItem(BlockRegistry.doubleslab_fae_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_fae_cobble = ITEMS.register("doubleslab_fae_cobble", () -> new BlockItem(BlockRegistry.doubleslab_fae_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_fae_bricks = ITEMS.register("doubleslab_fae_bricks", () -> new BlockItem(BlockRegistry.doubleslab_fae_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_fae_stone = ITEMS.register("halfslab_fae_stone", () -> new BlockItem(BlockRegistry.halfslab_fae_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_fae_cobble = ITEMS.register("halfslab_fae_cobble", () -> new BlockItem(BlockRegistry.halfslab_fae_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_fae_bricks = ITEMS.register("halfslab_fae_bricks", () -> new BlockItem(BlockRegistry.halfslab_fae_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_stone_wall = ITEMS.register("fae_stone_wall", () -> new BlockItem(BlockRegistry.fae_stone_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_cobble_wall = ITEMS.register("fae_cobble_wall", () -> new BlockItem(BlockRegistry.fae_cobble_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fae_brick_wall = ITEMS.register("fae_brick_wall", () -> new BlockItem(BlockRegistry.fae_brick_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble = ITEMS.register("mud_marble", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.mud_marble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_ore = ITEMS.register("mud_marble_ore", () -> new BlockItem(BlockRegistry.mud_marble_ore.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_keystone = ITEMS.register("mud_marble_keystone", () -> new BlockItem(BlockRegistry.mud_marble_keystone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_stalactite = ITEMS.register("mud_marble_stalactite", () -> new BlockItem(BlockRegistry.mud_marble_stalactite.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_pillar = ITEMS.register("mud_marble_pillar", () -> new BlockItem(BlockRegistry.mud_marble_pillar.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_mud_marble_stone = ITEMS.register("stairs_mud_marble_stone", () -> new BlockItem(BlockRegistry.stairs_mud_marble_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_mud_marble_cobble = ITEMS.register("stairs_mud_marble_cobble", () -> new BlockItem(BlockRegistry.stairs_mud_marble_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_mud_marble_bricks = ITEMS.register("stairs_mud_marble_bricks", () -> new BlockItem(BlockRegistry.stairs_mud_marble_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_mud_marble_stone = ITEMS.register("doubleslab_mud_marble_stone", () -> new BlockItem(BlockRegistry.doubleslab_mud_marble_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_mud_marble_cobble = ITEMS.register("doubleslab_mud_marble_cobble", () -> new BlockItem(BlockRegistry.doubleslab_mud_marble_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_mud_marble_bricks = ITEMS.register("doubleslab_mud_marble_bricks", () -> new BlockItem(BlockRegistry.doubleslab_mud_marble_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_mud_marble_stone = ITEMS.register("halfslab_mud_marble_stone", () -> new BlockItem(BlockRegistry.halfslab_mud_marble_stone.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_mud_marble_cobble = ITEMS.register("halfslab_mud_marble_cobble", () -> new BlockItem(BlockRegistry.halfslab_mud_marble_cobble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_mud_marble_bricks = ITEMS.register("halfslab_mud_marble_bricks", () -> new BlockItem(BlockRegistry.halfslab_mud_marble_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_stone_wall = ITEMS.register("mud_marble_stone_wall", () -> new BlockItem(BlockRegistry.mud_marble_stone_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_cobble_wall = ITEMS.register("mud_marble_cobble_wall", () -> new BlockItem(BlockRegistry.mud_marble_cobble_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mud_marble_brick_wall = ITEMS.register("mud_marble_brick_wall", () -> new BlockItem(BlockRegistry.mud_marble_brick_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_block_mud = ITEMS.register("block_mud", () -> new BlockItem(BlockRegistry.block_mud.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_dynamic_web = ITEMS.register("dynamic_web", () -> new BlockItem(BlockRegistry.dynamic_web.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_plant_aloe = ITEMS.register("plant_aloe", () -> new BlockItem(BlockRegistry.plant_aloe.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_barrel = ITEMS.register("plant_barrel", () -> new BlockItem(BlockRegistry.plant_barrel.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_beanstalk = ITEMS.register("plant_beanstalk", () -> new BlockItem(BlockRegistry.plant_beanstalk.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_brittle = ITEMS.register("plant_brittle", () -> new BlockItem(BlockRegistry.plant_brittle.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_creosote = ITEMS.register("plant_creosote", () -> new BlockItem(BlockRegistry.plant_creosote.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_bush = ITEMS.register("bush", () -> new BlockItem(BlockRegistry.bush.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hydrangea = ITEMS.register("hydrangea", () -> new BlockItem(BlockRegistry.hydrangea.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_mistletoe = ITEMS.register("plant_mistletoe", () -> new BlockItem(BlockRegistry.plant_mistletoe.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_ocotillo = ITEMS.register("plant_ocotillo", () -> new BlockItem(BlockRegistry.plant_ocotillo.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_oleander = ITEMS.register("plant_oleander", () -> new BlockItem(BlockRegistry.plant_oleander.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_pitcher = ITEMS.register("plant_pitcher", () -> new BlockItem(BlockRegistry.plant_pitcher.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_prickly = ITEMS.register("plant_prickly", () -> new BlockItem(BlockRegistry.plant_prickly.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_rose = ITEMS.register("plant_rose", () -> new BlockItem(BlockRegistry.plant_rose.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_shrooms = ITEMS.register("plant_shrooms", () -> new BlockItem(BlockRegistry.plant_shrooms.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_venus = ITEMS.register("plant_venus", () -> new BlockItem(BlockRegistry.plant_venus.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_stalk = ITEMS.register("mushroom_stalk", () -> new BlockItem(BlockRegistry.mushroom_stalk.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_crop_dark = ITEMS.register("mushroom_crop_dark", () -> new BlockItem(BlockRegistry.mushroom_crop_dark.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_dark = ITEMS.register("mushroom_dark", () -> new BlockItem(BlockRegistry.mushroom_dark.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_crop_white = ITEMS.register("mushroom_crop_white", () -> new BlockItem(BlockRegistry.mushroom_crop_white.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_white = ITEMS.register("mushroom_white", () -> new BlockItem(BlockRegistry.mushroom_white.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_crop_grey = ITEMS.register("mushroom_crop_grey", () -> new BlockItem(BlockRegistry.mushroom_crop_grey.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_grey = ITEMS.register("mushroom_grey", () -> new BlockItem(BlockRegistry.mushroom_grey.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_crop_pink = ITEMS.register("mushroom_crop_pink", () -> new BlockItem(BlockRegistry.mushroom_crop_pink.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_mushroom_pink = ITEMS.register("mushroom_pink", () -> new BlockItem(BlockRegistry.mushroom_pink.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_coconut = ITEMS.register("coconut", () -> new BlockItem(BlockRegistry.coconut.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_burst_potion = ITEMS.register("burst_potion", () -> new BlockItem(BlockRegistry.burst_potion.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_herbal_twine = ITEMS.register("herbal_twine", () -> new BlockItem(BlockRegistry.herbal_twine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_sage = ITEMS.register("plant_sage", () -> new BlockItem(BlockRegistry.plant_sage.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_plant_valleylily = ITEMS.register("plant_valleylily", () -> new BlockItem(BlockRegistry.plant_valleylily.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_magic_plant = ITEMS.register("magic_plant", () -> new ItemBlockMagicPlant(BlockRegistry.magic_plant.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_magic_sapling = ITEMS.register("magic_sapling", () -> new ItemBlockMagicSapling(BlockRegistry.magic_sapling.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_sapling_palm = ITEMS.register("sapling_palm", () -> new BlockItem(BlockRegistry.sapling_palm.get(), new Item.Properties()));

   public static final RegistryObject<Item> acolyte_sign   = ITEMS.register("acolyte_sign",   () -> new net.minecraft.world.item.SignItem(new Item.Properties().stacksTo(16), BlockRegistry.acolyte_sign.get(),   BlockRegistry.acolyte_wall_sign.get()));
   public static final RegistryObject<Item> vermilion_sign = ITEMS.register("vermilion_sign", () -> new net.minecraft.world.item.SignItem(new Item.Properties().stacksTo(16), BlockRegistry.vermilion_sign.get(), BlockRegistry.vermilion_wall_sign.get()));
   public static final RegistryObject<Item> bastion_sign   = ITEMS.register("bastion_sign",   () -> new net.minecraft.world.item.SignItem(new Item.Properties().stacksTo(16), BlockRegistry.bastion_sign.get(),   BlockRegistry.bastion_wall_sign.get()));
   public static final RegistryObject<Item> wartwood_sign  = ITEMS.register("wartwood_sign",  () -> new net.minecraft.world.item.SignItem(new Item.Properties().stacksTo(16), BlockRegistry.wartwood_sign.get(),  BlockRegistry.wartwood_wall_sign.get()));
   public static final RegistryObject<Item> evermore_sign  = ITEMS.register("evermore_sign",  () -> new net.minecraft.world.item.SignItem(new Item.Properties().stacksTo(16), BlockRegistry.evermore_sign.get(),  BlockRegistry.evermore_wall_sign.get()));
   public static final RegistryObject<Item> jubilee_sign   = ITEMS.register("jubilee_sign",   () -> new net.minecraft.world.item.SignItem(new Item.Properties().stacksTo(16), BlockRegistry.jubilee_sign.get(),   BlockRegistry.jubilee_wall_sign.get()));

   public static final RegistryObject<Item> acolyte_hanging_sign   = ITEMS.register("acolyte_hanging_sign",   () -> new net.minecraft.world.item.HangingSignItem(BlockRegistry.acolyte_hanging_sign.get(),   BlockRegistry.acolyte_wall_hanging_sign.get(),   new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> vermilion_hanging_sign = ITEMS.register("vermilion_hanging_sign", () -> new net.minecraft.world.item.HangingSignItem(BlockRegistry.vermilion_hanging_sign.get(), BlockRegistry.vermilion_wall_hanging_sign.get(), new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> bastion_hanging_sign   = ITEMS.register("bastion_hanging_sign",   () -> new net.minecraft.world.item.HangingSignItem(BlockRegistry.bastion_hanging_sign.get(),   BlockRegistry.bastion_wall_hanging_sign.get(),   new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> wartwood_hanging_sign  = ITEMS.register("wartwood_hanging_sign",  () -> new net.minecraft.world.item.HangingSignItem(BlockRegistry.wartwood_hanging_sign.get(),  BlockRegistry.wartwood_wall_hanging_sign.get(),  new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> evermore_hanging_sign  = ITEMS.register("evermore_hanging_sign",  () -> new net.minecraft.world.item.HangingSignItem(BlockRegistry.evermore_hanging_sign.get(),  BlockRegistry.evermore_wall_hanging_sign.get(),  new Item.Properties().stacksTo(16)));
   public static final RegistryObject<Item> jubilee_hanging_sign   = ITEMS.register("jubilee_hanging_sign",   () -> new net.minecraft.world.item.HangingSignItem(BlockRegistry.jubilee_hanging_sign.get(),   BlockRegistry.jubilee_wall_hanging_sign.get(),   new Item.Properties().stacksTo(16)));

   public static final RegistryObject<Item> bi_crystal_ruby         = ITEMS.register("crystal_ruby",         () -> new BlockItem(BlockRegistry.crystal_ruby.get(),         new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_sunstone     = ITEMS.register("crystal_sunstone",     () -> new BlockItem(BlockRegistry.crystal_sunstone.get(),     new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_citrine      = ITEMS.register("crystal_citrine",      () -> new BlockItem(BlockRegistry.crystal_citrine.get(),      new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_peridot      = ITEMS.register("crystal_peridot",      () -> new BlockItem(BlockRegistry.crystal_peridot.get(),      new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_jade         = ITEMS.register("crystal_jade",         () -> new BlockItem(BlockRegistry.crystal_jade.get(),         new Item.Properties()));
   public static final RegistryObject<Item> bi_dark_crystal         = ITEMS.register("dark_crystal",         () -> new BlockItem(BlockRegistry.dark_crystal.get(),         new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_turquoise    = ITEMS.register("crystal_turquoise",    () -> new BlockItem(BlockRegistry.crystal_turquoise.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_aquamarine   = ITEMS.register("crystal_aquamarine",   () -> new BlockItem(BlockRegistry.crystal_aquamarine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_sapphire     = ITEMS.register("crystal_sapphire",     () -> new BlockItem(BlockRegistry.crystal_sapphire.get(),     new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_amethyst     = ITEMS.register("crystal_amethyst",     () -> new BlockItem(BlockRegistry.crystal_amethyst.get(),     new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_garnet       = ITEMS.register("crystal_garnet",       () -> new BlockItem(BlockRegistry.crystal_garnet.get(),       new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_rose_quartz  = ITEMS.register("crystal_rose_quartz",  () -> new BlockItem(BlockRegistry.crystal_rose_quartz.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_moonstone    = ITEMS.register("crystal_moonstone",    () -> new BlockItem(BlockRegistry.crystal_moonstone.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_putridite    = ITEMS.register("crystal_putridite",    () -> new BlockItem(BlockRegistry.crystal_putridite.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_opal         = ITEMS.register("crystal_opal",         () -> new BlockItem(BlockRegistry.crystal_opal.get(),         new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_onyx         = ITEMS.register("crystal_onyx",         () -> new BlockItem(BlockRegistry.crystal_onyx.get(),         new Item.Properties()));
   public static final RegistryObject<Item> bi_crystal_smoky_quartz = ITEMS.register("crystal_smoky_quartz", () -> new BlockItem(BlockRegistry.crystal_smoky_quartz.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_magic_leaves1 = ITEMS.register("magic_leaves1", () -> new BlockItem(BlockRegistry.magic_leaves1.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_magic_leaves2 = ITEMS.register("magic_leaves2", () -> new BlockItem(BlockRegistry.magic_leaves2.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_hanging_willow = ITEMS.register("leaves_hanging_willow", () -> new BlockItem(BlockRegistry.leaves_hanging_willow.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_log_ash    = ITEMS.register("log_ash",    () -> new BlockItem(BlockRegistry.log_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_log_elder  = ITEMS.register("log_elder",  () -> new BlockItem(BlockRegistry.log_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_log_pine   = ITEMS.register("log_pine",   () -> new BlockItem(BlockRegistry.log_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_log_willow = ITEMS.register("log_willow", () -> new BlockItem(BlockRegistry.log_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_log_yew    = ITEMS.register("log_yew",    () -> new BlockItem(BlockRegistry.log_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_log_verde  = ITEMS.register("log_verde",  () -> new BlockItem(BlockRegistry.log_verde.get(),  new Item.Properties()));

   public static final RegistryObject<Item> bi_stripped_log_ash    = ITEMS.register("stripped_log_ash",    () -> new BlockItem(BlockRegistry.stripped_log_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_log_elder  = ITEMS.register("stripped_log_elder",  () -> new BlockItem(BlockRegistry.stripped_log_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_log_pine   = ITEMS.register("stripped_log_pine",   () -> new BlockItem(BlockRegistry.stripped_log_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_log_willow = ITEMS.register("stripped_log_willow", () -> new BlockItem(BlockRegistry.stripped_log_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_log_yew    = ITEMS.register("stripped_log_yew",    () -> new BlockItem(BlockRegistry.stripped_log_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_log_verde  = ITEMS.register("stripped_log_verde",  () -> new BlockItem(BlockRegistry.stripped_log_verde.get(),  new Item.Properties()));

   public static final RegistryObject<Item> bi_wood_ash    = ITEMS.register("wood_ash",    () -> new BlockItem(BlockRegistry.wood_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_wood_elder  = ITEMS.register("wood_elder",  () -> new BlockItem(BlockRegistry.wood_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_wood_pine   = ITEMS.register("wood_pine",   () -> new BlockItem(BlockRegistry.wood_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_wood_willow = ITEMS.register("wood_willow", () -> new BlockItem(BlockRegistry.wood_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_wood_yew    = ITEMS.register("wood_yew",    () -> new BlockItem(BlockRegistry.wood_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_wood_verde  = ITEMS.register("wood_verde",  () -> new BlockItem(BlockRegistry.wood_verde.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_wood_ash    = ITEMS.register("stripped_wood_ash",    () -> new BlockItem(BlockRegistry.stripped_wood_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_wood_elder  = ITEMS.register("stripped_wood_elder",  () -> new BlockItem(BlockRegistry.stripped_wood_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_wood_pine   = ITEMS.register("stripped_wood_pine",   () -> new BlockItem(BlockRegistry.stripped_wood_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_wood_willow = ITEMS.register("stripped_wood_willow", () -> new BlockItem(BlockRegistry.stripped_wood_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_wood_yew    = ITEMS.register("stripped_wood_yew",    () -> new BlockItem(BlockRegistry.stripped_wood_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_stripped_wood_verde  = ITEMS.register("stripped_wood_verde",  () -> new BlockItem(BlockRegistry.stripped_wood_verde.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_ash    = ITEMS.register("leaves_ash",    () -> new BlockItem(BlockRegistry.leaves_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_elder  = ITEMS.register("leaves_elder",  () -> new BlockItem(BlockRegistry.leaves_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_pine   = ITEMS.register("leaves_pine",   () -> new BlockItem(BlockRegistry.leaves_pine.get(),   new Item.Properties()));

   public static final RegistryObject<Item> bi_leaves_palm   = ITEMS.register("leaves_palm",   () -> new BlockItem(BlockRegistry.leaves_palm.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_willow = ITEMS.register("leaves_willow", () -> new BlockItem(BlockRegistry.leaves_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_yew    = ITEMS.register("leaves_yew",    () -> new BlockItem(BlockRegistry.leaves_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_leaves_verde  = ITEMS.register("leaves_verde",  () -> new BlockItem(BlockRegistry.leaves_verde.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_log_palm = ITEMS.register("log_palm", () -> new BlockItem(BlockRegistry.log_palm.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_palm_top = ITEMS.register("palm_top", () -> new BlockItem(BlockRegistry.palm_top.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_planks_ash    = ITEMS.register("planks_ash",    () -> new com.paleimitations.schoolsofmagic.common.items.PlankBlockItem(BlockRegistry.planks.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.ASH,    new Item.Properties()));
   public static final RegistryObject<Item> bi_planks_elder  = ITEMS.register("planks_elder",  () -> new com.paleimitations.schoolsofmagic.common.items.PlankBlockItem(BlockRegistry.planks.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.ELDER,  new Item.Properties()));
   public static final RegistryObject<Item> bi_planks_pine   = ITEMS.register("planks_pine",   () -> new com.paleimitations.schoolsofmagic.common.items.PlankBlockItem(BlockRegistry.planks.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.PINE,   new Item.Properties()));
   public static final RegistryObject<Item> bi_planks_willow = ITEMS.register("planks_willow", () -> new com.paleimitations.schoolsofmagic.common.items.PlankBlockItem(BlockRegistry.planks.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.WILLOW, new Item.Properties()));
   public static final RegistryObject<Item> bi_planks_yew    = ITEMS.register("planks_yew",    () -> new com.paleimitations.schoolsofmagic.common.items.PlankBlockItem(BlockRegistry.planks.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.YEW,    new Item.Properties()));
   public static final RegistryObject<Item> bi_planks_verde  = ITEMS.register("planks_verde",  () -> new com.paleimitations.schoolsofmagic.common.items.PlankBlockItem(BlockRegistry.planks.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.VERDE,  new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_planks = ITEMS.register("rotted_planks", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.rotted_planks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_chest = ITEMS.register("rotted_chest", () -> new SOMBerBlockItem(
      BlockRegistry.rotted_chest.get(), new Item.Properties(),
      new net.minecraft.resources.ResourceLocation("som", "rotted_chest"),
      new net.minecraft.resources.ResourceLocation("som", "textures/entity/rotted_chest.png")));
   public static final RegistryObject<Item> bi_stair_ash = ITEMS.register("stair_ash", () -> new BlockItem(BlockRegistry.stair_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stair_elder = ITEMS.register("stair_elder", () -> new BlockItem(BlockRegistry.stair_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stair_pine = ITEMS.register("stair_pine", () -> new BlockItem(BlockRegistry.stair_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stair_willow = ITEMS.register("stair_willow", () -> new BlockItem(BlockRegistry.stair_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stair_yew = ITEMS.register("stair_yew", () -> new BlockItem(BlockRegistry.stair_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stair_verde = ITEMS.register("stair_verde", () -> new BlockItem(BlockRegistry.stair_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_ash = ITEMS.register("rotted_stair_ash", () -> new BlockItem(BlockRegistry.rotted_stair_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_elder = ITEMS.register("rotted_stair_elder", () -> new BlockItem(BlockRegistry.rotted_stair_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_pine = ITEMS.register("rotted_stair_pine", () -> new BlockItem(BlockRegistry.rotted_stair_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_willow = ITEMS.register("rotted_stair_willow", () -> new BlockItem(BlockRegistry.rotted_stair_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_yew = ITEMS.register("rotted_stair_yew", () -> new BlockItem(BlockRegistry.rotted_stair_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_verde = ITEMS.register("rotted_stair_verde", () -> new BlockItem(BlockRegistry.rotted_stair_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_oak = ITEMS.register("rotted_stair_oak", () -> new BlockItem(BlockRegistry.rotted_stair_oak.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_spruce = ITEMS.register("rotted_stair_spruce", () -> new BlockItem(BlockRegistry.rotted_stair_spruce.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_birch = ITEMS.register("rotted_stair_birch", () -> new BlockItem(BlockRegistry.rotted_stair_birch.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_jungle = ITEMS.register("rotted_stair_jungle", () -> new BlockItem(BlockRegistry.rotted_stair_jungle.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_acacia = ITEMS.register("rotted_stair_acacia", () -> new BlockItem(BlockRegistry.rotted_stair_acacia.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_stair_dark_oak = ITEMS.register("rotted_stair_dark_oak", () -> new BlockItem(BlockRegistry.rotted_stair_dark_oak.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_ash = ITEMS.register("doubleslab_ash", () -> new BlockItem(BlockRegistry.doubleslab_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_elder = ITEMS.register("doubleslab_elder", () -> new BlockItem(BlockRegistry.doubleslab_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_pine = ITEMS.register("doubleslab_pine", () -> new BlockItem(BlockRegistry.doubleslab_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_willow = ITEMS.register("doubleslab_willow", () -> new BlockItem(BlockRegistry.doubleslab_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_yew = ITEMS.register("doubleslab_yew", () -> new BlockItem(BlockRegistry.doubleslab_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_verde = ITEMS.register("doubleslab_verde", () -> new BlockItem(BlockRegistry.doubleslab_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_ash = ITEMS.register("rotted_doubleslab_ash", () -> new BlockItem(BlockRegistry.rotted_doubleslab_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_elder = ITEMS.register("rotted_doubleslab_elder", () -> new BlockItem(BlockRegistry.rotted_doubleslab_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_pine = ITEMS.register("rotted_doubleslab_pine", () -> new BlockItem(BlockRegistry.rotted_doubleslab_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_willow = ITEMS.register("rotted_doubleslab_willow", () -> new BlockItem(BlockRegistry.rotted_doubleslab_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_yew = ITEMS.register("rotted_doubleslab_yew", () -> new BlockItem(BlockRegistry.rotted_doubleslab_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_verde = ITEMS.register("rotted_doubleslab_verde", () -> new BlockItem(BlockRegistry.rotted_doubleslab_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_oak = ITEMS.register("rotted_doubleslab_oak", () -> new BlockItem(BlockRegistry.rotted_doubleslab_oak.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_spruce = ITEMS.register("rotted_doubleslab_spruce", () -> new BlockItem(BlockRegistry.rotted_doubleslab_spruce.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_birch = ITEMS.register("rotted_doubleslab_birch", () -> new BlockItem(BlockRegistry.rotted_doubleslab_birch.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_jungle = ITEMS.register("rotted_doubleslab_jungle", () -> new BlockItem(BlockRegistry.rotted_doubleslab_jungle.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_acacia = ITEMS.register("rotted_doubleslab_acacia", () -> new BlockItem(BlockRegistry.rotted_doubleslab_acacia.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_doubleslab_dark_oak = ITEMS.register("rotted_doubleslab_dark_oak", () -> new BlockItem(BlockRegistry.rotted_doubleslab_dark_oak.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_ash = ITEMS.register("halfslab_ash", () -> new BlockItem(BlockRegistry.halfslab_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_elder = ITEMS.register("halfslab_elder", () -> new BlockItem(BlockRegistry.halfslab_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_pine = ITEMS.register("halfslab_pine", () -> new BlockItem(BlockRegistry.halfslab_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_willow = ITEMS.register("halfslab_willow", () -> new BlockItem(BlockRegistry.halfslab_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_yew = ITEMS.register("halfslab_yew", () -> new BlockItem(BlockRegistry.halfslab_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_verde = ITEMS.register("halfslab_verde", () -> new BlockItem(BlockRegistry.halfslab_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_ash = ITEMS.register("rotted_halfslab_ash", () -> new BlockItem(BlockRegistry.rotted_halfslab_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_elder = ITEMS.register("rotted_halfslab_elder", () -> new BlockItem(BlockRegistry.rotted_halfslab_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_pine = ITEMS.register("rotted_halfslab_pine", () -> new BlockItem(BlockRegistry.rotted_halfslab_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_willow = ITEMS.register("rotted_halfslab_willow", () -> new BlockItem(BlockRegistry.rotted_halfslab_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_yew = ITEMS.register("rotted_halfslab_yew", () -> new BlockItem(BlockRegistry.rotted_halfslab_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_verde = ITEMS.register("rotted_halfslab_verde", () -> new BlockItem(BlockRegistry.rotted_halfslab_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_oak = ITEMS.register("rotted_halfslab_oak", () -> new BlockItem(BlockRegistry.rotted_halfslab_oak.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_spruce = ITEMS.register("rotted_halfslab_spruce", () -> new BlockItem(BlockRegistry.rotted_halfslab_spruce.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_birch = ITEMS.register("rotted_halfslab_birch", () -> new BlockItem(BlockRegistry.rotted_halfslab_birch.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_jungle = ITEMS.register("rotted_halfslab_jungle", () -> new BlockItem(BlockRegistry.rotted_halfslab_jungle.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_acacia = ITEMS.register("rotted_halfslab_acacia", () -> new BlockItem(BlockRegistry.rotted_halfslab_acacia.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_rotted_halfslab_dark_oak = ITEMS.register("rotted_halfslab_dark_oak", () -> new BlockItem(BlockRegistry.rotted_halfslab_dark_oak.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_door_ash = ITEMS.register("door_ash", () -> new BlockItem(BlockRegistry.door_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_door_elder = ITEMS.register("door_elder", () -> new BlockItem(BlockRegistry.door_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_door_pine = ITEMS.register("door_pine", () -> new BlockItem(BlockRegistry.door_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_door_willow = ITEMS.register("door_willow", () -> new BlockItem(BlockRegistry.door_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_door_yew = ITEMS.register("door_yew", () -> new BlockItem(BlockRegistry.door_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_door_verde = ITEMS.register("door_verde", () -> new BlockItem(BlockRegistry.door_verde.get(), new Item.Properties()));

   public static final RegistryObject<Item> bi_trapdoor_ash    = ITEMS.register("trapdoor_ash",    () -> new BlockItem(BlockRegistry.trapdoor_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_trapdoor_elder  = ITEMS.register("trapdoor_elder",  () -> new BlockItem(BlockRegistry.trapdoor_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_trapdoor_pine   = ITEMS.register("trapdoor_pine",   () -> new BlockItem(BlockRegistry.trapdoor_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_trapdoor_willow = ITEMS.register("trapdoor_willow", () -> new BlockItem(BlockRegistry.trapdoor_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_trapdoor_yew    = ITEMS.register("trapdoor_yew",    () -> new BlockItem(BlockRegistry.trapdoor_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_trapdoor_verde  = ITEMS.register("trapdoor_verde",  () -> new BlockItem(BlockRegistry.trapdoor_verde.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_pressure_plate_ash    = ITEMS.register("pressure_plate_ash",    () -> new BlockItem(BlockRegistry.pressure_plate_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_pressure_plate_elder  = ITEMS.register("pressure_plate_elder",  () -> new BlockItem(BlockRegistry.pressure_plate_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_pressure_plate_pine   = ITEMS.register("pressure_plate_pine",   () -> new BlockItem(BlockRegistry.pressure_plate_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_pressure_plate_willow = ITEMS.register("pressure_plate_willow", () -> new BlockItem(BlockRegistry.pressure_plate_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_pressure_plate_yew    = ITEMS.register("pressure_plate_yew",    () -> new BlockItem(BlockRegistry.pressure_plate_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_pressure_plate_verde  = ITEMS.register("pressure_plate_verde",  () -> new BlockItem(BlockRegistry.pressure_plate_verde.get(),  new Item.Properties()));

   public static final RegistryObject<Item> bi_button_ash    = ITEMS.register("button_ash",    () -> new BlockItem(BlockRegistry.button_ash.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_button_elder  = ITEMS.register("button_elder",  () -> new BlockItem(BlockRegistry.button_elder.get(),  new Item.Properties()));
   public static final RegistryObject<Item> bi_button_pine   = ITEMS.register("button_pine",   () -> new BlockItem(BlockRegistry.button_pine.get(),   new Item.Properties()));
   public static final RegistryObject<Item> bi_button_willow = ITEMS.register("button_willow", () -> new BlockItem(BlockRegistry.button_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_button_yew    = ITEMS.register("button_yew",    () -> new BlockItem(BlockRegistry.button_yew.get(),    new Item.Properties()));
   public static final RegistryObject<Item> bi_button_verde  = ITEMS.register("button_verde",  () -> new BlockItem(BlockRegistry.button_verde.get(),  new Item.Properties()));

   public static final RegistryObject<Item> bi_fence_copper = ITEMS.register("fence_copper", () -> new BlockItem(BlockRegistry.fence_copper.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_bronze = ITEMS.register("fence_bronze", () -> new BlockItem(BlockRegistry.fence_bronze.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_brass = ITEMS.register("fence_brass", () -> new BlockItem(BlockRegistry.fence_brass.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gold = ITEMS.register("fence_gold", () -> new BlockItem(BlockRegistry.fence_gold.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_silver = ITEMS.register("fence_silver", () -> new BlockItem(BlockRegistry.fence_silver.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_iron = ITEMS.register("fence_iron", () -> new BlockItem(BlockRegistry.fence_iron.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_steel = ITEMS.register("fence_steel", () -> new BlockItem(BlockRegistry.fence_steel.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_tenebrium = ITEMS.register("fence_tenebrium", () -> new BlockItem(BlockRegistry.fence_tenebrium.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_ash = ITEMS.register("fence_ash", () -> new BlockItem(BlockRegistry.fence_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_elder = ITEMS.register("fence_elder", () -> new BlockItem(BlockRegistry.fence_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_pine = ITEMS.register("fence_pine", () -> new BlockItem(BlockRegistry.fence_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_willow = ITEMS.register("fence_willow", () -> new BlockItem(BlockRegistry.fence_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_yew = ITEMS.register("fence_yew", () -> new BlockItem(BlockRegistry.fence_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_verde = ITEMS.register("fence_verde", () -> new BlockItem(BlockRegistry.fence_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gate_ash = ITEMS.register("fence_gate_ash", () -> new BlockItem(BlockRegistry.fence_gate_ash.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gate_elder = ITEMS.register("fence_gate_elder", () -> new BlockItem(BlockRegistry.fence_gate_elder.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gate_pine = ITEMS.register("fence_gate_pine", () -> new BlockItem(BlockRegistry.fence_gate_pine.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gate_willow = ITEMS.register("fence_gate_willow", () -> new BlockItem(BlockRegistry.fence_gate_willow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gate_yew = ITEMS.register("fence_gate_yew", () -> new BlockItem(BlockRegistry.fence_gate_yew.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_fence_gate_verde = ITEMS.register("fence_gate_verde", () -> new BlockItem(BlockRegistry.fence_gate_verde.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_magic_bookshelf = ITEMS.register("magic_bookshelf", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.magic_bookshelf.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_podium = ITEMS.register("podium", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.podium.get(), new Item.Properties()));

   public static final java.util.List<RegistryObject<Item>> PEDESTAL_ITEMS = new java.util.ArrayList<>();
   static {
      for (RegistryObject<net.minecraft.world.level.block.Block> block : BlockRegistry.PEDESTALS) {
         PEDESTAL_ITEMS.add(ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));
      }
   }
   public static final RegistryObject<Item> bi_planter = ITEMS.register("planter", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.planter.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_pyromancy = ITEMS.register("gem_pyromancy", () -> new BlockItem(BlockRegistry.gem_pyromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_heliomancy = ITEMS.register("gem_heliomancy", () -> new BlockItem(BlockRegistry.gem_heliomancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_aeromancy = ITEMS.register("gem_aeromancy", () -> new BlockItem(BlockRegistry.gem_aeromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_geomancy = ITEMS.register("gem_geomancy", () -> new BlockItem(BlockRegistry.gem_geomancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_animancy = ITEMS.register("gem_animancy", () -> new BlockItem(BlockRegistry.gem_animancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_electromancy = ITEMS.register("gem_electromancy", () -> new BlockItem(BlockRegistry.gem_electromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_hydromancy = ITEMS.register("gem_hydromancy", () -> new BlockItem(BlockRegistry.gem_hydromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_cryomancy = ITEMS.register("gem_cryomancy", () -> new BlockItem(BlockRegistry.gem_cryomancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_hieromancy = ITEMS.register("gem_hieromancy", () -> new BlockItem(BlockRegistry.gem_hieromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_chaotics = ITEMS.register("gem_chaotics", () -> new BlockItem(BlockRegistry.gem_chaotics.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_auramancy = ITEMS.register("gem_auramancy", () -> new BlockItem(BlockRegistry.gem_auramancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_astromancy = ITEMS.register("gem_astromancy", () -> new BlockItem(BlockRegistry.gem_astromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_infernality = ITEMS.register("gem_infernality", () -> new BlockItem(BlockRegistry.gem_infernality.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_spectromancy = ITEMS.register("gem_spectromancy", () -> new BlockItem(BlockRegistry.gem_spectromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_umbramancy = ITEMS.register("gem_umbramancy", () -> new BlockItem(BlockRegistry.gem_umbramancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_necromancy = ITEMS.register("gem_necromancy", () -> new BlockItem(BlockRegistry.gem_necromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> magic_diamond = ITEMS.register("magic_diamond",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemSuperchargedDiamond(
         new Item.Properties().stacksTo(64)));
   public static final RegistryObject<Item> bi_gem_block = ITEMS.register("gem_block", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.gem_block.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_gem_chunk_block = ITEMS.register("gem_chunk_block", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.gem_chunk_block.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_metal_block = ITEMS.register("metal_block", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.metal_block.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_metal_bricks = ITEMS.register("metal_bricks", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.metal_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stair_void = ITEMS.register("stair_void", () -> new BlockItem(BlockRegistry.stair_void.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_void = ITEMS.register("doubleslab_void", () -> new BlockItem(BlockRegistry.doubleslab_void.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_void = ITEMS.register("halfslab_void", () -> new BlockItem(BlockRegistry.halfslab_void.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_tile_jade = ITEMS.register("tile_jade", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.tile_jade.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_tile_turquoise = ITEMS.register("tile_turquoise", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.tile_turquoise.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_tile_lapis = ITEMS.register("tile_lapis", () -> new BlockItem(BlockRegistry.tile_lapis.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ziggurat_door_wall = ITEMS.register("ziggurat_door_wall", () -> new BlockItem(BlockRegistry.ziggurat_door_wall.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hardened_clay_bricks_default = ITEMS.register("hardened_clay_bricks_default", () -> new BlockItem(BlockRegistry.hardened_clay_bricks_default.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hardened_clay_bricks_default_cracked = ITEMS.register("hardened_clay_bricks_default_cracked", () -> new BlockItem(BlockRegistry.hardened_clay_bricks_default_cracked.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hardened_clay_bricks_default_chiseled = ITEMS.register("hardened_clay_bricks_default_chiseled", () -> new BlockItem(BlockRegistry.hardened_clay_bricks_default_chiseled.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hardened_clay_bricks = ITEMS.register("hardened_clay_bricks", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.hardened_clay_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hardened_clay_bricks_cracked = ITEMS.register("hardened_clay_bricks_cracked", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.hardened_clay_bricks_cracked.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_hardened_clay_bricks_chiseled = ITEMS.register("hardened_clay_bricks_chiseled", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.hardened_clay_bricks_chiseled.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks = ITEMS.register("stairs_hardened_clay_bricks", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_red = ITEMS.register("stairs_hardened_clay_bricks_red", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_red.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_orange = ITEMS.register("stairs_hardened_clay_bricks_orange", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_orange.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_yellow = ITEMS.register("stairs_hardened_clay_bricks_yellow", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_yellow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_lime = ITEMS.register("stairs_hardened_clay_bricks_lime", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_lime.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_green = ITEMS.register("stairs_hardened_clay_bricks_green", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_green.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_cyan = ITEMS.register("stairs_hardened_clay_bricks_cyan", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_cyan.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_lblue = ITEMS.register("stairs_hardened_clay_bricks_lblue", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_lblue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_blue = ITEMS.register("stairs_hardened_clay_bricks_blue", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_blue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_purple = ITEMS.register("stairs_hardened_clay_bricks_purple", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_purple.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_magenta = ITEMS.register("stairs_hardened_clay_bricks_magenta", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_magenta.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_pink = ITEMS.register("stairs_hardened_clay_bricks_pink", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_pink.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_white = ITEMS.register("stairs_hardened_clay_bricks_white", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_white.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_lgray = ITEMS.register("stairs_hardened_clay_bricks_lgray", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_lgray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_gray = ITEMS.register("stairs_hardened_clay_bricks_gray", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_gray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_black = ITEMS.register("stairs_hardened_clay_bricks_black", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_black.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_stairs_hardened_clay_bricks_brown = ITEMS.register("stairs_hardened_clay_bricks_brown", () -> new BlockItem(BlockRegistry.stairs_hardened_clay_bricks_brown.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks = ITEMS.register("doubleslab_hardened_clay_bricks", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_red = ITEMS.register("doubleslab_hardened_clay_bricks_red", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_red.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_orange = ITEMS.register("doubleslab_hardened_clay_bricks_orange", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_orange.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_yellow = ITEMS.register("doubleslab_hardened_clay_bricks_yellow", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_yellow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_lime = ITEMS.register("doubleslab_hardened_clay_bricks_lime", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_lime.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_green = ITEMS.register("doubleslab_hardened_clay_bricks_green", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_green.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_cyan = ITEMS.register("doubleslab_hardened_clay_bricks_cyan", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_cyan.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_blue = ITEMS.register("doubleslab_hardened_clay_bricks_blue", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_blue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_purple = ITEMS.register("doubleslab_hardened_clay_bricks_purple", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_purple.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_magenta = ITEMS.register("doubleslab_hardened_clay_bricks_magenta", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_magenta.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_pink = ITEMS.register("doubleslab_hardened_clay_bricks_pink", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_pink.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_white = ITEMS.register("doubleslab_hardened_clay_bricks_white", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_white.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_lgray = ITEMS.register("doubleslab_hardened_clay_bricks_lgray", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_lgray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_gray = ITEMS.register("doubleslab_hardened_clay_bricks_gray", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_gray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_black = ITEMS.register("doubleslab_hardened_clay_bricks_black", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_black.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_brown = ITEMS.register("doubleslab_hardened_clay_bricks_brown", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_brown.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_doubleslab_hardened_clay_bricks_lblue = ITEMS.register("doubleslab_hardened_clay_bricks_lblue", () -> new BlockItem(BlockRegistry.doubleslab_hardened_clay_bricks_lblue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks = ITEMS.register("halfslab_hardened_clay_bricks", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_red = ITEMS.register("halfslab_hardened_clay_bricks_red", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_red.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_orange = ITEMS.register("halfslab_hardened_clay_bricks_orange", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_orange.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_yellow = ITEMS.register("halfslab_hardened_clay_bricks_yellow", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_yellow.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_lime = ITEMS.register("halfslab_hardened_clay_bricks_lime", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_lime.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_green = ITEMS.register("halfslab_hardened_clay_bricks_green", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_green.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_cyan = ITEMS.register("halfslab_hardened_clay_bricks_cyan", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_cyan.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_blue = ITEMS.register("halfslab_hardened_clay_bricks_blue", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_blue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_purple = ITEMS.register("halfslab_hardened_clay_bricks_purple", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_purple.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_magenta = ITEMS.register("halfslab_hardened_clay_bricks_magenta", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_magenta.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_pink = ITEMS.register("halfslab_hardened_clay_bricks_pink", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_pink.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_white = ITEMS.register("halfslab_hardened_clay_bricks_white", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_white.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_lgray = ITEMS.register("halfslab_hardened_clay_bricks_lgray", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_lgray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_gray = ITEMS.register("halfslab_hardened_clay_bricks_gray", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_gray.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_black = ITEMS.register("halfslab_hardened_clay_bricks_black", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_black.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_brown = ITEMS.register("halfslab_hardened_clay_bricks_brown", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_brown.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_halfslab_hardened_clay_bricks_lblue = ITEMS.register("halfslab_hardened_clay_bricks_lblue", () -> new BlockItem(BlockRegistry.halfslab_hardened_clay_bricks_lblue.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_gem = ITEMS.register("ore_gem", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.ore_gem.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_gem_deepslate = ITEMS.register("ore_gem_deepslate", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.ore_gem_deepslate.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_gem_fae = ITEMS.register("ore_gem_fae", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.ore_gem_fae.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_gem_gypsum = ITEMS.register("ore_gem_gypsum", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.ore_gem_gypsum.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_gem_mud_marble = ITEMS.register("ore_gem_mud_marble", () -> new com.paleimitations.schoolsofmagic.common.items.BlockStateTagItem(BlockRegistry.ore_gem_mud_marble.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_silver = ITEMS.register("ore_silver", () -> new BlockItem(BlockRegistry.ore_silver.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_copper = ITEMS.register("ore_copper", () -> new BlockItem(BlockRegistry.ore_copper.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_ore_steel = ITEMS.register("ore_steel", () -> new BlockItem(BlockRegistry.ore_steel.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_spawn = ITEMS.register("spawn", () -> new BlockItem(BlockRegistry.spawn.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_spear = ITEMS.register("spear", () -> new BlockItem(BlockRegistry.spear.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_pyromancy = ITEMS.register("crop_pyromancy", () -> new BlockItem(BlockRegistry.crop_pyromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_heliomancy = ITEMS.register("crop_heliomancy", () -> new BlockItem(BlockRegistry.crop_heliomancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_aeromancy = ITEMS.register("crop_aeromancy", () -> new BlockItem(BlockRegistry.crop_aeromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_animancy = ITEMS.register("crop_animancy", () -> new BlockItem(BlockRegistry.crop_animancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_geomancy = ITEMS.register("crop_geomancy", () -> new BlockItem(BlockRegistry.crop_geomancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_electromancy = ITEMS.register("crop_electromancy", () -> new BlockItem(BlockRegistry.crop_electromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_hydromancy = ITEMS.register("crop_hydromancy", () -> new BlockItem(BlockRegistry.crop_hydromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_cryomancy = ITEMS.register("crop_cryomancy", () -> new BlockItem(BlockRegistry.crop_cryomancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_chaotics = ITEMS.register("crop_chaotics", () -> new BlockItem(BlockRegistry.crop_chaotics.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_hieromancy = ITEMS.register("crop_hieromancy", () -> new BlockItem(BlockRegistry.crop_hieromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_auramancy = ITEMS.register("crop_auramancy", () -> new BlockItem(BlockRegistry.crop_auramancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_astromancy = ITEMS.register("crop_astromancy", () -> new BlockItem(BlockRegistry.crop_astromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_infernality = ITEMS.register("crop_infernality", () -> new BlockItem(BlockRegistry.crop_infernality.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_spectromancy = ITEMS.register("crop_spectromancy", () -> new BlockItem(BlockRegistry.crop_spectromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_necromancy = ITEMS.register("crop_necromancy", () -> new BlockItem(BlockRegistry.crop_necromancy.get(), new Item.Properties()));
   public static final RegistryObject<Item> bi_crop_umbramancy = ITEMS.register("crop_umbramancy", () -> new BlockItem(BlockRegistry.crop_umbramancy.get(), new Item.Properties()));

   public static final RegistryObject<Item> aloe = ITEMS.register("aloe",
      () -> new SOMFood(new Item.Properties()));
   public static final RegistryObject<Item> aloe_paste = ITEMS.register("aloe_paste",
      () -> new SOMFood(new Item.Properties()));
   public static final RegistryObject<Item> poppy_seed_muffin = ITEMS.register("poppy_seed_muffin",
      () -> new Item(new Item.Properties().food(
         new net.minecraft.world.food.FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build())));
   public static final RegistryObject<Item> item_porcelain = ITEMS.register("item_porcelain",
      () -> new com.paleimitations.schoolsofmagic.common.items.SOMMaterial(new Item.Properties()));
   public static final RegistryObject<Item> item_diamond_dust = ITEMS.register("item_diamond_dust",
      () -> new com.paleimitations.schoolsofmagic.common.items.SOMMaterial(new Item.Properties()));
   public static final RegistryObject<Item> item_obsidian_shard = ITEMS.register("item_obsidian_shard",
      () -> new com.paleimitations.schoolsofmagic.common.items.SOMMaterial(new Item.Properties()));
   public static final RegistryObject<Item> item_ice_shard = ITEMS.register("item_ice_shard",
      () -> new com.paleimitations.schoolsofmagic.common.items.SOMMaterial(new Item.Properties()));

   public static final RegistryObject<Item> item_brittle_leaves = ITEMS.register("item_brittle_leaves",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));
   public static final RegistryObject<Item> item_creosote_leaves = ITEMS.register("item_creosote_leaves",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));
   public static final RegistryObject<Item> item_sage_leaves = ITEMS.register("item_sage_leaves",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));

   public static final RegistryObject<Item> item_ocotillo_flowers = ITEMS.register("item_ocotillo_flowers",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));
   public static final RegistryObject<Item> item_graveroot_pulp = ITEMS.register("item_graveroot_pulp",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));
   public static final RegistryObject<Item> item_lithop_pulp = ITEMS.register("item_lithop_pulp",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));
   public static final RegistryObject<Item> item_mandrake_pulp = ITEMS.register("item_mandrake_pulp",
      () -> new com.paleimitations.schoolsofmagic.common.items.ItemHerbology(new Item.Properties()));

   private static RegistryObject<Item> bannerPattern(String element) {

      return ITEMS.register("banner_pattern_" + element,
         () -> new net.minecraft.world.item.BannerPatternItem(
            com.paleimitations.schoolsofmagic.common.registries.PatternRegistry.patternTag(element),
            new Item.Properties().stacksTo(1).rarity(net.minecraft.world.item.Rarity.UNCOMMON)));
   }
   public static final RegistryObject<Item> banner_pattern_pyromancy   = bannerPattern("pyromancy");
   public static final RegistryObject<Item> banner_pattern_heliomancy  = bannerPattern("heliomancy");
   public static final RegistryObject<Item> banner_pattern_aeromancy   = bannerPattern("aeromancy");
   public static final RegistryObject<Item> banner_pattern_geomancy    = bannerPattern("geomancy");
   public static final RegistryObject<Item> banner_pattern_animancy    = bannerPattern("animancy");
   public static final RegistryObject<Item> banner_pattern_electromancy= bannerPattern("electromancy");
   public static final RegistryObject<Item> banner_pattern_hydromancy  = bannerPattern("hydromancy");
   public static final RegistryObject<Item> banner_pattern_cryomancy   = bannerPattern("cryomancy");
   public static final RegistryObject<Item> banner_pattern_hieromancy  = bannerPattern("hieromancy");
   public static final RegistryObject<Item> banner_pattern_chaotics    = bannerPattern("chaotics");
   public static final RegistryObject<Item> banner_pattern_auramancy   = bannerPattern("auramancy");
   public static final RegistryObject<Item> banner_pattern_astromancy  = bannerPattern("astromancy");
   public static final RegistryObject<Item> banner_pattern_infernality = bannerPattern("infernality");
   public static final RegistryObject<Item> banner_pattern_spectromancy= bannerPattern("spectromancy");
   public static final RegistryObject<Item> banner_pattern_umbramancy  = bannerPattern("umbramancy");
   public static final RegistryObject<Item> banner_pattern_necromancy  = bannerPattern("necromancy");

   @SuppressWarnings("unchecked")
   public static final RegistryObject<Item>[] BANNER_PATTERN_ITEMS = new RegistryObject[]{
      banner_pattern_pyromancy, banner_pattern_heliomancy, banner_pattern_aeromancy, banner_pattern_geomancy,
      banner_pattern_animancy, banner_pattern_electromancy, banner_pattern_hydromancy, banner_pattern_cryomancy,
      banner_pattern_hieromancy, banner_pattern_chaotics, banner_pattern_auramancy, banner_pattern_astromancy,
      banner_pattern_infernality, banner_pattern_spectromancy, banner_pattern_umbramancy, banner_pattern_necromancy
   };

   private static RegistryObject<Item> egg(String name, RegistryObject<? extends net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Mob>> type, int primary, int secondary) {
      return ITEMS.register(name + "_spawn_egg",
         () -> new net.minecraftforge.common.ForgeSpawnEggItem(type, primary, secondary, new Item.Properties()));
   }
   public static final RegistryObject<Item> toad_spawn_egg      = egg("toad",      EntityRegistry.TOAD,      4932905, 11249034);
   public static final RegistryObject<Item> dryad_spawn_egg     = egg("dryad",     EntityRegistry.DRYAD,     10734969, 5794117);
   public static final RegistryObject<Item> phoenix_spawn_egg   = egg("phoenix",   EntityRegistry.PHOENIX,   16372296, 15360531);
   public static final RegistryObject<Item> tarantula_spawn_egg = egg("tarantula", EntityRegistry.TARANTULA, 6700594, 3673867);
   public static final RegistryObject<Item> unicorn_spawn_egg   = egg("unicorn",   EntityRegistry.UNICORN,   16119807, 12763857);
   public static final RegistryObject<Item> human_spawn_egg     = egg("human",     EntityRegistry.HUMAN,     12369084, 10592673);
   public static final RegistryObject<Item> squeakard_spawn_egg = egg("squeakard", EntityRegistry.SQUEAKARD, 10734969, 5794117);
   public static final RegistryObject<Item> sphinx_spawn_egg    = egg("sphinx",    EntityRegistry.SPHINX,    10734969, 5794117);
   public static final RegistryObject<Item> demon_spawn_egg     = egg("demon",     EntityRegistry.DEMON,     3609361, 15185730);
   public static final RegistryObject<Item> flower_fae_spawn_egg = egg("flower_fae", EntityRegistry.FLOWER_FAE, 14906055, 6717235);

   public static final java.util.List<RegistryObject<Item>> FAIRY_EGGS = new java.util.ArrayList<>();
   static {
      for (com.paleimitations.schoolsofmagic.common.entity.FairyVariant v : com.paleimitations.schoolsofmagic.common.entity.FairyVariant.values()) {
         FAIRY_EGGS.add(ITEMS.register("fairy_" + v.id + "_spawn_egg",
            () -> new com.paleimitations.schoolsofmagic.common.items.ItemFairyEgg(v, new Item.Properties())));
      }
   }

   public static final RegistryObject<Item> fairy_dust_light_blue =
      ITEMS.register("fairy_dust_light_blue", () -> new Item(new Item.Properties()));

   public static final java.util.List<RegistryObject<Item>> FAIRY_DUSTS = new java.util.ArrayList<>();
   static {
      for (com.paleimitations.schoolsofmagic.common.entity.FairyVariant v : com.paleimitations.schoolsofmagic.common.entity.FairyVariant.values()) {
         FAIRY_DUSTS.add(ITEMS.register("fairy_dust_" + v.id,
            () -> new Item(new Item.Properties())));
      }
      FAIRY_DUSTS.add(fairy_dust_light_blue);
   }

   public static final RegistryObject<Item>[] SPAWN_EGGS = makeEggArray();
   @SuppressWarnings("unchecked")
   private static RegistryObject<Item>[] makeEggArray() {
      return new RegistryObject[]{ toad_spawn_egg, dryad_spawn_egg, phoenix_spawn_egg, tarantula_spawn_egg,
         unicorn_spawn_egg, human_spawn_egg, squeakard_spawn_egg, sphinx_spawn_egg, demon_spawn_egg, flower_fae_spawn_egg };
   }

   public static void register(IEventBus bus) {
      ITEMS.register(bus);
   }
}
