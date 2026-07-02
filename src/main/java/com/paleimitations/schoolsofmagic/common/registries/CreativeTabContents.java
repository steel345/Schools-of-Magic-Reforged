package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;

public class CreativeTabContents {

    public enum Tab { EQUIPMENT, MATERIALS, BLOCKS, PLANTS }

    private static final Map<String, Tab> ITEM_TAB = Map.<String, Tab>ofEntries(

        Map.entry("ItemBaseWand", Tab.EQUIPMENT),
        Map.entry("ItemBell", Tab.EQUIPMENT),
        Map.entry("ItemBookBase", Tab.EQUIPMENT),

        Map.entry("ItemSpellbook", Tab.EQUIPMENT),
        Map.entry("ItemDoorWallKey", Tab.EQUIPMENT),
        Map.entry("ItemCopperKey", Tab.EQUIPMENT),
        Map.entry("ItemLetter", Tab.EQUIPMENT),
        Map.entry("ItemPageBase", Tab.EQUIPMENT),
        Map.entry("ItemQuest", Tab.EQUIPMENT),
        Map.entry("ItemSpellModifierScroll", Tab.EQUIPMENT),
        Map.entry("ItemSpellNotes", Tab.EQUIPMENT),
        Map.entry("ItemSticker", Tab.EQUIPMENT),
        Map.entry("ItemStickerPack", Tab.EQUIPMENT),
        Map.entry("ItemTea", Tab.EQUIPMENT),
        Map.entry("PotionBase", Tab.EQUIPMENT),
        Map.entry("SOMArmor", Tab.EQUIPMENT),
        Map.entry("MetalArmor", Tab.EQUIPMENT),
        Map.entry("SOMAxe", Tab.EQUIPMENT),
        Map.entry("SOMHoe", Tab.EQUIPMENT),
        Map.entry("SOMPickax", Tab.EQUIPMENT),
        Map.entry("SOMPickaxe", Tab.EQUIPMENT),
        Map.entry("SOMSandpaper", Tab.EQUIPMENT),
        Map.entry("SOMShovel", Tab.EQUIPMENT),
        Map.entry("SOMSword", Tab.EQUIPMENT),
        Map.entry("CreativeMaker", Tab.EQUIPMENT),

        Map.entry("SignItem", Tab.BLOCKS),
        Map.entry("HangingSignItem", Tab.BLOCKS),
        Map.entry("SOMBoatItem", Tab.BLOCKS),
        Map.entry("ItemBottle", Tab.MATERIALS),

        Map.entry("ItemGeomancy", Tab.MATERIALS),
        Map.entry("ItemIngredient", Tab.MATERIALS),
        Map.entry("ItemMagicGem", Tab.MATERIALS),
        Map.entry("ItemMetal", Tab.MATERIALS),
        Map.entry("ItemMisc", Tab.MATERIALS),
        Map.entry("ItemPotionry", Tab.MATERIALS),
        Map.entry("ItemWandCore", Tab.MATERIALS),
        Map.entry("ToadSpawnPlacer", Tab.MATERIALS),
        Map.entry("SOMFood", Tab.MATERIALS),

        Map.entry("SOMMaterial", Tab.MATERIALS),
        Map.entry("ItemSuperchargedDiamond", Tab.MATERIALS),
        Map.entry("ItemApprenticeRing", Tab.EQUIPMENT),
        Map.entry("ItemPotionCharm", Tab.EQUIPMENT),
        Map.entry("ItemTalismanOfKnowledge", Tab.EQUIPMENT),

        Map.entry("ItemHerbology", Tab.PLANTS),
        Map.entry("ItemMagicPlant", Tab.PLANTS),
        Map.entry("ItemMagicPlantSeed", Tab.PLANTS),
        Map.entry("ItemPlant", Tab.PLANTS),
        Map.entry("ItemTree", Tab.PLANTS),
        Map.entry("SOMSeed", Tab.PLANTS)
    );

    private static final Map<String, Tab> BLOCK_TAB = Map.<String, Tab>ofEntries(

        Map.entry("BlockDemonicHeart", Tab.EQUIPMENT),
        Map.entry("BlockHerbalTwine", Tab.EQUIPMENT),
        Map.entry("BlockRottedChest", Tab.EQUIPMENT),

        Map.entry("BlockCoconut", Tab.MATERIALS),
        Map.entry("Gem", Tab.MATERIALS),
        Map.entry("BlockGemCluster", Tab.MATERIALS),

        Map.entry("BlockBrazier", Tab.BLOCKS),

        Map.entry("BlockConnectedTextures", Tab.BLOCKS),
        Map.entry("BlockCauldron", Tab.BLOCKS),

        Map.entry("BlockTeapot", Tab.BLOCKS),
        Map.entry("BlockSpellForge", Tab.BLOCKS),

        Map.entry("BlockSacrificialAltar", Tab.BLOCKS),
        Map.entry("BlockCatalystBasin", Tab.BLOCKS),
        Map.entry("BlockCrystalBall", Tab.BLOCKS),
        Map.entry("BlockDesertBrazier", Tab.BLOCKS),

        Map.entry("BlockDynamicWeb", Tab.BLOCKS),
        Map.entry("BlockFaeStone", Tab.BLOCKS),
        Map.entry("BlockGoldCoins", Tab.BLOCKS),
        Map.entry("BlockHardClayBricks", Tab.BLOCKS),
        Map.entry("BlockMagicBookshelf", Tab.BLOCKS),
        Map.entry("BlockMortnpest", Tab.BLOCKS),
        Map.entry("BlockMagicGemBlock", Tab.BLOCKS),
        Map.entry("BlockMagicOre", Tab.BLOCKS),

        Map.entry("BlockMagicPlanks", Tab.BLOCKS),
        Map.entry("BlockMetal", Tab.BLOCKS),
        Map.entry("BlockObsidianPressurePlate", Tab.BLOCKS),
        Map.entry("BlockPlate", Tab.BLOCKS),
        Map.entry("BlockPalmLog", Tab.BLOCKS),
        Map.entry("BlockPlanter", Tab.BLOCKS),
        Map.entry("BlockPodium", Tab.BLOCKS),
        Map.entry("BlockRottedPlanks", Tab.BLOCKS),
        Map.entry("BlockStandardOres", Tab.BLOCKS),

        Map.entry("BlockSandstoneTablet", Tab.BLOCKS),
        Map.entry("BlockZigMural", Tab.BLOCKS),
        Map.entry("BlockDoorWall", Tab.BLOCKS),
        Map.entry("BlockTrapSpike", Tab.BLOCKS),
        Map.entry("BlockTrapSpikeBase", Tab.BLOCKS),
        Map.entry("BlockTile", Tab.BLOCKS),

        Map.entry("BlockVaseSmall", Tab.BLOCKS),
        Map.entry("BlockVaseMedium", Tab.BLOCKS),
        Map.entry("BlockVaseLarge", Tab.BLOCKS),

        Map.entry("SOMFence", Tab.BLOCKS),
        Map.entry("SOMFenceGate", Tab.BLOCKS),

        Map.entry("SOMDoor", Tab.BLOCKS),

        Map.entry("TrapDoorBlock", Tab.BLOCKS),
        Map.entry("PressurePlateBlock", Tab.BLOCKS),

        Map.entry("ButtonBlock", Tab.BLOCKS),

        Map.entry("RotatedPillarBlock", Tab.BLOCKS),

        Map.entry("BlockCharcoal", Tab.BLOCKS),
        Map.entry("BlockMushroomStalk", Tab.BLOCKS),
        Map.entry("BlockPillar", Tab.BLOCKS),
        Map.entry("BlockWalls", Tab.BLOCKS),
        Map.entry("BlockDirectional", Tab.BLOCKS),
        Map.entry("BlockPalmTop", Tab.BLOCKS),

        Map.entry("SOMHalfSlab", Tab.BLOCKS),

        Map.entry("SOMStair", Tab.BLOCKS),
        Map.entry("SOMStairs", Tab.BLOCKS),

        Map.entry("BlockHangingBranch", Tab.PLANTS),
        Map.entry("BlockMagicPlant", Tab.PLANTS),
        Map.entry("BlockMushroomCrop", Tab.PLANTS),
        Map.entry("BlockOrefalling", Tab.PLANTS),
        Map.entry("BlockPalmLeaves", Tab.PLANTS),
        Map.entry("BlockPalmSapling", Tab.PLANTS),
        Map.entry("BlockPlantAloe", Tab.PLANTS),
        Map.entry("BlockPlantBarrel", Tab.PLANTS),
        Map.entry("BlockPlantBeanstalk", Tab.PLANTS),
        Map.entry("BlockPlantBush", Tab.PLANTS),
        Map.entry("BlockPlantGrowingBush", Tab.PLANTS),
        Map.entry("BlockPlantMistletoe", Tab.PLANTS),
        Map.entry("BlockPlantPitcher", Tab.PLANTS),
        Map.entry("BlockPlantPrickly", Tab.PLANTS),
        Map.entry("BlockPlantShrooms", Tab.PLANTS),
        Map.entry("BlockConnectedBush", Tab.PLANTS),
        Map.entry("BlockPlantHydrangea", Tab.PLANTS),

        Map.entry("BlockPlantSage", Tab.PLANTS),
        Map.entry("BlockWallPlant", Tab.PLANTS),
        Map.entry("BlockWallPyracantha", Tab.PLANTS),
        Map.entry("BlockMushroom", Tab.PLANTS),

        Map.entry("SOMLeaves1", Tab.PLANTS),
        Map.entry("SOMLeaves2", Tab.PLANTS),
        Map.entry("LeavesBlock", Tab.PLANTS),

        Map.entry("SOMSapling", Tab.PLANTS),
        Map.entry("SOMPlant", Tab.PLANTS)
    );

    private static final Set<String> FORCE_HIDDEN_PATHS = Set.of(
        "phantom_fire",

        "plant_algae",
        "plant_duckweed",
        "plant_bladderwort",
        "plant_cattail",

        "spawn",

        "magic_leaves1",
        "magic_leaves2",

        "leaves_hanging_willow"
    );

    private static final String[] HIDDEN_PREFIXES = {
        "doubleslab_", "rotted_doubleslab_"
    };

    public static void populate(Tab target, net.minecraft.world.item.CreativeModeTab.Output output) {
        for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
            Item item = entry.get();
            ResourceLocation id = entry.getId();
            String path = id.getPath();

            if (isHidden(path)) continue;

            Tab tab = resolveTab(item);

            if (tab == null && EXTRA_BLOCKS_TAB.contains(path)) tab = Tab.BLOCKS;
            if (tab == null || tab != target) continue;

            try {
                emitEntries(output, item, id);
            } catch (Throwable t) {
                org.slf4j.LoggerFactory.getLogger("SchoolsOfMagic").error("Failed to add creative tab entry for " + id, t);
            }
        }

        if (target == Tab.MATERIALS) {
            output.accept(new net.minecraft.world.item.ItemStack(ItemRegistry.brass_whistle.get()));
            output.accept(new net.minecraft.world.item.ItemStack(ItemRegistry.spell_parchment.get()));
            output.accept(new net.minecraft.world.item.ItemStack(ItemRegistry.scroll_seal.get()));
            output.accept(com.paleimitations.schoolsofmagic.common.crafting.CoconutMilkRecipe.coconutMilk());

            for (RegistryObject<Item> dust : ItemRegistry.FAIRY_DUSTS) {
                output.accept(new net.minecraft.world.item.ItemStack(dust.get()));
            }

            for (RegistryObject<Item> bp : ItemRegistry.BANNER_PATTERN_ITEMS) {
                output.accept(new net.minecraft.world.item.ItemStack(bp.get()));
            }

            for (RegistryObject<Item> egg : ItemRegistry.SPAWN_EGGS) {
                output.accept(new net.minecraft.world.item.ItemStack(egg.get()));
            }
            for (RegistryObject<Item> egg : ItemRegistry.FAIRY_EGGS) {
                output.accept(new net.minecraft.world.item.ItemStack(egg.get()));
            }
        }
    }

    private static final Set<String> EXTRA_BLOCKS_TAB = Set.of("tile_lapis", "ore_silver", "ore_copper", "block_mud", "unlit_torch");

    private static boolean isHidden(String path) {
        if (FORCE_HIDDEN_PATHS.contains(path)) return true;
        for (String prefix : HIDDEN_PREFIXES) if (path.startsWith(prefix)) return true;
        return false;
    }

    private static Tab resolveTab(Item item) {

        if (item instanceof BlockItem bi) {
            Block b = bi.getBlock();
            for (Class<?> c = b.getClass(); c != null && c != Block.class && c != Object.class; c = c.getSuperclass()) {
                Tab t = BLOCK_TAB.get(c.getSimpleName());
                if (t != null) return t;
            }

            for (Class<?> c = item.getClass(); c != null && c != Item.class && c != Object.class; c = c.getSuperclass()) {
                Tab t = ITEM_TAB.get(c.getSimpleName());
                if (t != null) return t;
            }
            return null;
        }

        for (Class<?> c = item.getClass(); c != null && c != Item.class && c != Object.class; c = c.getSuperclass()) {
            Tab t = ITEM_TAB.get(c.getSimpleName());
            if (t != null) return t;
        }
        return null;
    }

    private static void emitEntries(net.minecraft.world.item.CreativeModeTab.Output output, Item item, ResourceLocation id) {

        if (item == ItemRegistry.magic_letter.get()) {
            output.accept(new net.minecraft.world.item.ItemStack(item));
            output.accept(new net.minecraft.world.item.ItemStack(ItemRegistry.mysterious_application.get()));
            return;
        }
        if (item == ItemRegistry.bi_cauldron.get()) { emitCauldronVariants(output, item); return; }
        if (item == ItemRegistry.bi_podium.get())   { emitPodiumVariants(output, item); return; }
        if (item == ItemRegistry.bi_magic_plant.get()) { emitMagicPlantVariants(output, item); return; }
        if (item == ItemRegistry.bi_planter.get())  { emitPlanterVariants(output, item); return; }
        if (item == ItemRegistry.bi_magic_bookshelf.get()) { emitBookshelfVariants(output, item); return; }
        if (item == ItemRegistry.toad_spawn.get()) { emitToadSpawnVariants(output, item); return; }
        if (item == ItemRegistry.bi_rotted_planks.get()) { emitRottedPlanksVariants(output, item); return; }
        if (item == ItemRegistry.bi_metal_block.get())   { emitMetalBlockVariants(output, item); return; }
        if (item == ItemRegistry.bi_metal_bricks.get())  { emitMetalBricksVariants(output, item); return; }
        if (item == ItemRegistry.ingot.get())            { emitMetalItemVariants(output, item); return; }
        if (item == ItemRegistry.nugget.get())           { emitMetalItemVariants(output, item); return; }
        if (item == ItemRegistry.bi_fae_stone.get())     { emitFaeStoneVariants(output, item, "fae_stone"); return; }
        if (item == ItemRegistry.bi_gypsum.get())        { emitFaeStoneVariants(output, item, "gypsum"); return; }

        if (item == ItemRegistry.bi_mud_marble.get())    { emitFaeStoneVariants(output, item, "mud_marble"); return; }
        if (item == ItemRegistry.bi_hardened_clay_bricks.get())          { emitHardClayVariants(output, item, "hardened_clay_bricks"); return; }
        if (item == ItemRegistry.bi_hardened_clay_bricks_cracked.get())  { emitHardClayVariants(output, item, "hardened_clay_bricks_cracked"); return; }
        if (item == ItemRegistry.bi_hardened_clay_bricks_chiseled.get()) { emitHardClayVariants(output, item, "hardened_clay_bricks_chiseled"); return; }
        if (item == ItemRegistry.bi_tile_jade.get())      { emitTileVariants(output, item, "tile_jade"); return; }
        if (item == ItemRegistry.bi_tile_turquoise.get()) { emitTileVariants(output, item, "tile_turquoise"); return; }

        if (item == ItemRegistry.bi_gem_block.get())      { emitGemBlockVariants(output, item, "gem_block"); return; }
        if (item == ItemRegistry.bi_gem_chunk_block.get()){ emitGemBlockVariants(output, item, "gem_chunk_block"); return; }
        if (item == ItemRegistry.bi_magic_sapling.get()) { emitMagicSaplingVariants(output, item); return; }
        if (item == ItemRegistry.bi_fae_ore.get())       { emitStandardOreVariants(output, item, "fae_ore"); return; }
        if (item == ItemRegistry.bi_gypsum_ore.get())    { emitStandardOreVariants(output, item, "gypsum_ore"); return; }
        if (item == ItemRegistry.bi_mud_marble_ore.get()){ emitStandardOreVariants(output, item, "mud_marble_ore"); return; }
        if (item == ItemRegistry.bi_ore_gem.get())       { emitMagicOreVariants(output, item, "block.som.ore_gem"); return; }
        if (item == ItemRegistry.bi_ore_gem_deepslate.get()) { emitMagicOreVariants(output, item, "block.som.ore_gem_deepslate"); return; }

        if (item == ItemRegistry.bi_ore_gem_fae.get())       { emitMagicOreVariants(output, item, "block.som.ore_gem_fae"); return; }
        if (item == ItemRegistry.bi_ore_gem_gypsum.get())    { emitMagicOreVariants(output, item, "block.som.ore_gem_gypsum"); return; }
        if (item == ItemRegistry.bi_ore_gem_mud_marble.get()){ emitMagicOreVariants(output, item, "block.som.ore_gem_mud_marble"); return; }
        if (item == ItemRegistry.quest_note.get())  { emitQuestVariants(output, item); return; }
        if (item == ItemRegistry.sticker.get())     { emitStickerVariants(output, item); return; }
        if (item == ItemRegistry.spell_modifier_scroll.get()) { emitScrollVariants(output, item); return; }
        if (item == ItemRegistry.teacup.get()) {
            emitTeaVariants(output, item);
            output.accept(new ItemStack(ItemRegistry.poppy_seed_muffin.get()));
            return;
        }

        if (item == ItemRegistry.bottle.get()) { emitBottleVariants(output, item); return; }

        if (item == ItemRegistry.apprentice_ring.get()) {
            output.accept(new ItemStack(item));
            com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType[] metals = {
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.COPPER,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.BRONZE,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.BRASS,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.GOLD,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.SILVER,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.IRON,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.STEEL,
                com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.VOID };
            int idx = 0;
            for (com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType gem :
                    com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType.values()) {
                if (gem == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType.DIAMOND
                        || gem == com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType.EMERALD) continue;
                ItemStack r = new ItemStack(item);
                com.paleimitations.schoolsofmagic.common.items.RingItemHelper.setData(r, metals[idx % metals.length], gem);
                com.paleimitations.schoolsofmagic.common.MagicElement el =
                    com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.getElement(gem);
                String en = el != null ? el.getName() : gem.getSerializedName();
                en = en.substring(0, 1).toUpperCase(java.util.Locale.ROOT) + en.substring(1);
                final String label = en + " Ring";
                r.setHoverName(net.minecraft.network.chat.Component.literal(label).withStyle(s -> s.withItalic(false)));
                output.accept(r);
                idx++;
            }
            return;
        }

        if (item instanceof BlockItem gembi
              && gembi.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.Gem) {
            emitGemStateVariants(output, item); return;
        }

        if (item instanceof com.paleimitations.schoolsofmagic.common.items.ICreativeTabFiller filler) {
            net.minecraft.core.NonNullList<ItemStack> presets = net.minecraft.core.NonNullList.create();
            filler.fillItemCategory(null, presets);
            for (ItemStack s : presets) output.accept(s);
            return;
        }

        if (item instanceof com.paleimitations.schoolsofmagic.common.items.IItemMetaHandler handler) {
            int count = Math.max(1, handler.getDamage());

            int start = (item instanceof com.paleimitations.schoolsofmagic.common.items.ItemPlant) ? 1 : 0;

            int end = (item == ItemRegistry.nugget.get()) ? Math.min(count, 10) : count;
            for (int i = start; i < end; i++) {
                if (!handler.hasMeta(i)) continue;
                ItemStack variant = new ItemStack(item);
                variant.setDamageValue(i);
                output.accept(variant);
            }
            return;
        }

        output.accept(item);
    }

    private static void emitCauldronVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (EnumCauldronType t : EnumCauldronType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", t.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.cauldron." + t.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitPodiumVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (EnumWoodType w : EnumWoodType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", w.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.podium." + w.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
        for (RegistryObject<Item> pedestal : ItemRegistry.PEDESTAL_ITEMS) {
            output.accept(new ItemStack(pedestal.get()));
        }
    }

    private static void emitMagicPlantVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (EnumMagicType t : EnumMagicType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", t.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);

            stack.setHoverName(Component.translatable("block.som.magic_plant." + t.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitTeaVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {

        for (com.paleimitations.schoolsofmagic.common.recipes.RecipeTea recipe :
             com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.teaRecipes) {
            if (recipe.getEffect() == null) continue;
            ItemStack stack = new ItemStack(item);
            com.paleimitations.schoolsofmagic.common.util.TeaUtils.appendEffects(
                stack, new net.minecraft.world.effect.MobEffectInstance(recipe.getEffect()));
            output.accept(stack);
        }
    }

    private static void emitStandardOreVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item, String registryBase) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres ore :
             com.paleimitations.schoolsofmagic.common.blocks.EnumStandardOres.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag();
            bs.putString("type", ore.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som." + registryBase + "." + ore.getSerializedName())
                .withStyle(st -> st.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitMagicOreVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item, String langKey) {
        for (EnumMagicType t : EnumMagicType.values()) {

            if (langKey.endsWith("ore_gem_deepslate") && !isDeepslateGemElement(t)) {
                continue;
            }
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag();
            bs.putString("type", t.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable(langKey + "." + t.getSerializedName())
                .withStyle(st -> st.withItalic(false)));
            output.accept(stack);
        }
    }

    private static boolean isDeepslateGemElement(EnumMagicType t) {
        switch (t) {
            case PYROMANCY:
            case AEROMANCY:
            case GEOMANCY:
            case ANIMANCY:
            case HYDROMANCY:
            case CRYOMANCY:
            case HIEROMANCY:
            case ASTROMANCY:
            case UMBRAMANCY:
                return true;
            default:
                return false;
        }
    }

    private static void emitMagicSaplingVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood w :
             com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", w.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.magic_sapling." + w.getSerializedName())
                .withStyle(st -> st.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitBottleVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumBottle b :
             com.paleimitations.schoolsofmagic.common.blocks.EnumBottle.values()) {
            ItemStack stack = new ItemStack(item);
            stack.setDamageValue(b.getIndex());
            output.accept(stack);
        }
    }

    private static void emitToadSpawnVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (int i = 0; i < 12; i++) {
            ItemStack stack = new ItemStack(item);
            stack.setDamageValue(i);
            output.accept(stack);
        }
    }

    private static void emitRottedPlanksVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (EnumWoodType w : EnumWoodType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", w.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.rotted_planks." + w.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static boolean isHiddenMetal(com.paleimitations.schoolsofmagic.common.blocks.EnumMetal m) {
        String n = m.getSerializedName();
        return n.equals("simulite") || n.equals("magnillion") || n.equals("celestium")
            || n.equals("iridion") || n.equals("eonium") || n.equals("abrasium");

    }

    private static void emitMetalItemVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumMetal m :
             com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.values()) {
            ItemStack stack = new ItemStack(item);
            stack.setDamageValue(m.getIndex());
            output.accept(stack);
        }
    }

    private static void emitMetalBlockVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumMetal m :
             com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.values()) {
            if (isHiddenMetal(m)) continue;
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", m.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.metal_block." + m.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitMetalBricksVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumMetal m :
             com.paleimitations.schoolsofmagic.common.blocks.EnumMetal.values()) {
            if (isHiddenMetal(m)) continue;
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", m.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.metal_bricks." + m.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitFaeStoneVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item, String langBase) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone v :
             com.paleimitations.schoolsofmagic.common.blocks.EnumFaeStone.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("variant", v.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som." + langBase + "." + v.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitMagicPlanksVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood w :
             com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", w.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.planks." + w.getSerializedName())
                .withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitGemStateVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        net.minecraft.resources.ResourceLocation id =
            net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(((BlockItem) item).getBlock());
        if (id == null) { output.accept(item); return; }
        String base = "block." + id.getNamespace() + "." + id.getPath();

        for (String type : new String[] { "polished" }) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag();
            bs.putString("type", type);
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable(base + "." + type).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitGemBlockVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item, String langBase) {
        for (EnumMagicType t : EnumMagicType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag();
            bs.putString("type", t.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som." + langBase + "." + t.getSerializedName())
                .withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitTileVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item, String langBase) {
        for (com.paleimitations.schoolsofmagic.common.blocks.EnumTileStyles s :
             com.paleimitations.schoolsofmagic.common.blocks.EnumTileStyles.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("variant", s.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som." + langBase + "." + s.getSerializedName()).withStyle(st -> st.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitHardClayVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item, String langBase) {
        for (EnumMagicType t : EnumMagicType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", t.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som." + langBase + "." + t.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitBookshelfVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {

        for (EnumWoodType w : EnumWoodType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", w.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.magic_bookshelf." + w.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitPlanterVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (EnumWoodType w : EnumWoodType.values()) {
            ItemStack stack = new ItemStack(item);
            CompoundTag bs = new CompoundTag(); bs.putString("type", w.getSerializedName());
            stack.getOrCreateTag().put("BlockStateTag", bs);
            stack.setHoverName(Component.translatable("block.som.planter." + w.getSerializedName()).withStyle(s -> s.withItalic(false)));
            output.accept(stack);
        }
    }

    private static void emitScrollVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        for (com.paleimitations.schoolsofmagic.common.spells.Spell.EnumSpellModifier m :
             com.paleimitations.schoolsofmagic.common.spells.Spell.EnumSpellModifier.values()) {
            ItemStack stack = new ItemStack(item);
            com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier data =
                com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier.getCapability(stack);
            if (data != null) data.setSpellModifier(m, m.getDefaultObj());

            if (data != null) stack.getOrCreateTag().put("modifier", data.serializeNBT());
            output.accept(stack);
        }
    }


    private static void emitQuestVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {
        String[] questIds = { "som:brew_potion", "som:build_golem", "som:enchant_item",
                              "som:intermediate_arcana", "som:advanced_arcana" };
        for (String q : questIds) {
            ItemStack stack = new ItemStack(item);
            stack.getOrCreateTag().putString("quest", q);

            output.accept(stack);
        }
    }

    private static void emitStickerVariants(net.minecraft.world.item.CreativeModeTab.Output output, Item item) {

        for (String s : STICKER_NAMES) {
            output.accept(com.paleimitations.schoolsofmagic.common.items.ItemSticker.makeSticker(s));
        }
    }

    private static final String[] STICKER_NAMES = {
        "skull", "fang", "bone", "rib_bone", "tooth", "shell",
        "peacock_feather", "phoenix_feather", "thunderbird_feather", "pheasant_feather",
        "flamingo_feather", "pigeon_feather", "raven_feather",
        "clover", "white_tulip", "lavender", "daisey", "snow_bells", "thistle",
        "globe_mallow", "fig_leaf",
        "bee", "queen_bee", "wolf_spider", "royal_spider",
        "monarch_butterfly", "bluebright_butterfly", "moth", "lunar_moth",
        "blue_dragonfly", "striped_dragonfly", "ladybug", "stag_beetle",
        "white_butterfly", "tropical_butterfly", "horn_beetle", "scarab_beetle",
        "strawberry", "watermelon", "bramble_berry", "blueberry", "kiwi", "orange",
        "gold_stamp", "silver_stamp", "bronze_stamp", "red_stamp",
        "grape_cap", "cola_cap",
        "blue_crayon", "green_crayon", "red_crayon", "black_button", "copper_coin",
        "bandaid", "gold_star",
        "saguaro_cactus", "prickly_pear_cactus", "pin_cushion_cactus", "barrel_cactus",
        "aloe_vera"
    };
}
