package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public final class MortarRecipeRegistry {
    public static RecipeMortNPest UNICORN_HORN;
    public static RecipeMortNPest GRAVEL;
    public static RecipeMortNPest GLOWSTONE;
    public static RecipeMortNPest WOOL;
    public static RecipeMortNPest DIAMOND;
    public static RecipeMortNPest DIAMOND_ORE;
    public static RecipeMortNPest EMERALD_ORE;
    public static RecipeMortNPest REDSTONE_ORE;
    public static RecipeMortNPest LAPIS_ORE;
    public static RecipeMortNPest COAL_ORE;
    public static RecipeMortNPest BONE;
    public static RecipeMortNPest OBSIDIAN;
    public static RecipeMortNPest RED_FLOWER_0;
    public static RecipeMortNPest RED_FLOWER_1;
    public static RecipeMortNPest RED_FLOWER_2;
    public static RecipeMortNPest RED_FLOWER_3;
    public static RecipeMortNPest RED_FLOWER_4;
    public static RecipeMortNPest RED_FLOWER_5;
    public static RecipeMortNPest RED_FLOWER_6;
    public static RecipeMortNPest RED_FLOWER_7;
    public static RecipeMortNPest RED_FLOWER_8;
    public static RecipeMortNPest YELLOW_FLOWER;
    public static RecipeMortNPest DOUBLE_PLANT_0;
    public static RecipeMortNPest DOUBLE_PLANT_1;
    public static RecipeMortNPest DOUBLE_PLANT_4;
    public static RecipeMortNPest DOUBLE_PLANT_5;
    public static RecipeMortNPest ALOE;
    public static RecipeMortNPest FOXGLOVE;
    public static RecipeMortNPest MALLOW;
    public static RecipeMortNPest YARROW;
    public static RecipeMortNPest OLEANDER;
    public static RecipeMortNPest HEMLOCK;
    public static RecipeMortNPest VALLEYLILY;
    public static RecipeMortNPest LAVENDER;
    public static RecipeMortNPest SNOWBELL;
    public static RecipeMortNPest MARIGOLD;
    public static RecipeMortNPest LITHOP;
    public static RecipeMortNPest MANDRAKE;
    public static RecipeMortNPest GRAVEROOT;
    public static RecipeMortNPest BEETROOT;
    public static RecipeMortNPest ICE;
    public static RecipeMortNPest SHARD_ICE;
    public static RecipeMortNPest REEDS;
    public static RecipeMortNPest BOTTLE_WORMWOOD;
    public static RecipeMortNPest BOTTLE_MANDRAKE;
    public static RecipeMortNPest BOTTLE_NIGHTBERRY;
    public static RecipeMortNPest BOTTLE_GRAVEROOT;
    public static RecipeMortNPest BOTTLE_FIREBERRY;
    public static RecipeMortNPest BOTTLE_POPPY;
    public static RecipeMortNPest BOTTLE_ROSE;
    public static RecipeMortNPest BOTTLE_SUNFLOWER;
    public static RecipeMortNPest BOTTLE_STORMTHISTLE;
    public static RecipeMortNPest BOTTLE_LOTUS;
    public static RecipeMortNPest BOTTLE_SNOWBELL;
    public static RecipeMortNPest BOTTLE_JIMSONWEED;
    public static RecipeMortNPest BOTTLE_BRAMBLE;
    public static RecipeMortNPest CACTUS_1;
    public static RecipeMortNPest CACTUS_2;
    public static RecipeMortNPest CACTUS_3;
    public static RecipeMortNPest OCOTILLO;
    public static RecipeMortNPest BLAZE;
    public static RecipeMortNPest NETHER_STAR;

    private static ItemStack stack(Item item, int count, int meta) {
        ItemStack s = new ItemStack(item, count);
        s.setDamageValue(meta);
        return s;
    }

    private static net.minecraft.world.item.crafting.Ingredient oresOf(net.minecraft.world.item.Item vanilla, net.minecraft.world.item.Item deepslate, String type) {
        return net.minecraft.world.item.crafting.Ingredient.of(
            new ItemStack(vanilla), new ItemStack(deepslate),
            gemTyped(BlockRegistry.gypsum_ore, type),
            gemTyped(BlockRegistry.fae_ore, type),
            gemTyped(BlockRegistry.mud_marble_ore, type));
    }
    private static net.minecraft.world.item.crafting.Ingredient gemAnyState(net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block> block) {
        return net.minecraft.world.item.crafting.Ingredient.of(gemTyped(block, "rough"), gemTyped(block, "polished"));
    }
    private static ItemStack gemTyped(net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block> block, String type) {
        ItemStack s = new ItemStack(block.get());
        net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
        bs.putString("type", type);
        s.getOrCreateTag().put("BlockStateTag", bs);
        return s;
    }

    private static ItemStack metaItem(net.minecraftforge.registries.RegistryObject<Item> ro, int meta) {
        ItemStack s = new ItemStack(ro.get());
        s.setDamageValue(meta);
        return s;
    }

    private static ItemStack metaItem(net.minecraftforge.registries.RegistryObject<Item> ro, int count, int meta) {
        ItemStack s = new ItemStack(ro.get(), count);
        s.setDamageValue(meta);
        return s;
    }

    private static ItemStack metaBlock(net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block> ro, int meta) {
        ItemStack s = new ItemStack(ro.get());
        s.setDamageValue(meta);

        if (ro == BlockRegistry.magic_plant && meta >= 0
              && meta < com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType.values().length) {
            String elementName = com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType
               .values()[meta].getSerializedName();
            net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
            bs.putString("type", elementName);
            s.getOrCreateTag().put("BlockStateTag", bs);
        }
        return s;
    }

    public static void init() {
        UNICORN_HORN = RecipeRegistry.registerMortarRecipe(
            ItemRegistry.horn_unicorn.get(), ItemStack.EMPTY, 10,
            new ItemStack(ItemRegistry.crushed_horn_unicorn.get(), 2), ItemStack.EMPTY);
        GRAVEL = RecipeRegistry.registerMortarRecipe(
            Blocks.GRAVEL, ItemStack.EMPTY, 10,
            new ItemStack(Items.FLINT), ItemStack.EMPTY);
        GLOWSTONE = RecipeRegistry.registerMortarRecipe(
            Blocks.GLOWSTONE, ItemStack.EMPTY, 5,
            new ItemStack(Items.GLOWSTONE_DUST, 4), ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(
            Items.GLOW_BERRIES, ItemStack.EMPTY, 5,
            new ItemStack(Items.GLOWSTONE_DUST, 1), ItemStack.EMPTY);
        WOOL = RecipeRegistry.registerMortarRecipe(
            Blocks.WHITE_WOOL, ItemStack.EMPTY, 5,
            new ItemStack(Items.STRING, 4), ItemStack.EMPTY);

        for (EnumPlantType type : EnumPlantType.values()) {
            if (type != EnumPlantType.NONE) {
                RecipeRegistry.registerMortarRecipe(
                    metaItem(ItemRegistry.dried_plant, type.getIndex()),
                    ItemStack.EMPTY, 6,
                    metaItem(ItemRegistry.crushed_plant, type.getIndex()),
                    ItemStack.EMPTY);
            }
        }

        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_pyromancy),    ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.PYROMANCY.getIndex()),    ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_heliomancy),   ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.HELIOMANCY.getIndex()),   ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_aeromancy),    ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.AEROMANCY.getIndex()),    ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_geomancy),     ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.GEOMANCY.getIndex()),     ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_animancy),     ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.ANIMANCY.getIndex()),     ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_electromancy), ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.ELECTROMANCY.getIndex()), ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_hydromancy),   ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.HYDROMANCY.getIndex()),   ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_cryomancy),    ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.CRYOMANCY.getIndex()),    ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_hieromancy),   ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.HIEROMANCY.getIndex()),   ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_chaotics),     ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.CHAOTICS.getIndex()),     ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_auramancy),    ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.AURAMANCY.getIndex()),    ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_astromancy),   ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.ASTROMANCY.getIndex()),   ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_infernality),  ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.INFERNALITY.getIndex()),  ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_spectromancy), ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.SPECTROMANCY.getIndex()), ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_umbramancy),   ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.UMBRAMANCY.getIndex()),   ItemStack.EMPTY);
        RecipeRegistry.registerMortarRecipe(gemAnyState(BlockRegistry.gem_necromancy),   ItemStack.EMPTY, 12, metaItem(ItemRegistry.gem_dust, 2, EnumMagicType.NECROMANCY.getIndex()),   ItemStack.EMPTY);

        DIAMOND      = RecipeRegistry.registerMortarRecipe(Items.DIAMOND,  ItemStack.EMPTY, 12, new ItemStack(ItemRegistry.item_diamond_dust.get(), 2), ItemStack.EMPTY);
        DIAMOND_ORE  = RecipeRegistry.registerMortarRecipe(oresOf(Items.DIAMOND_ORE, Items.DEEPSLATE_DIAMOND_ORE, "diamond"),  ItemStack.EMPTY, 12, new ItemStack(Items.DIAMOND, 2),     ItemStack.EMPTY);
        EMERALD_ORE  = RecipeRegistry.registerMortarRecipe(oresOf(Items.EMERALD_ORE, Items.DEEPSLATE_EMERALD_ORE, "emerald"),  ItemStack.EMPTY, 12, new ItemStack(Items.EMERALD, 2),     ItemStack.EMPTY);
        REDSTONE_ORE = RecipeRegistry.registerMortarRecipe(net.minecraft.world.item.crafting.Ingredient.of(Items.REDSTONE_ORE, Items.DEEPSLATE_REDSTONE_ORE), ItemStack.EMPTY, 12, new ItemStack(Items.REDSTONE, 6),    ItemStack.EMPTY);
        COAL_ORE     = RecipeRegistry.registerMortarRecipe(oresOf(Items.COAL_ORE, Items.DEEPSLATE_COAL_ORE, "coal"),     ItemStack.EMPTY, 12, new ItemStack(Items.COAL, 3),        ItemStack.EMPTY);
        LAPIS_ORE    = RecipeRegistry.registerMortarRecipe(oresOf(Items.LAPIS_ORE, Items.DEEPSLATE_LAPIS_ORE, "lapis"),    ItemStack.EMPTY, 12, new ItemStack(Items.LAPIS_LAZULI, 10), ItemStack.EMPTY);

        OBSIDIAN = RecipeRegistry.registerMortarRecipe(
            Blocks.OBSIDIAN, ItemStack.EMPTY, 20,
            new ItemStack(ItemRegistry.item_obsidian_shard.get(), 2), ItemStack.EMPTY);

        BONE = RecipeRegistry.registerMortarRecipe(
            Items.BONE, ItemStack.EMPTY, 10,
            new ItemStack(Items.BONE_MEAL, 4), ItemStack.EMPTY);

        BLAZE = RecipeRegistry.registerMortarRecipe(
            Items.BLAZE_ROD, ItemStack.EMPTY, 10,
            new ItemStack(Items.BLAZE_POWDER, 3), ItemStack.EMPTY);

        RED_FLOWER_0 = RecipeRegistry.registerMortarRecipe(Blocks.POPPY,         ItemStack.EMPTY, 6, new ItemStack(Items.RED_DYE, 2),         ItemStack.EMPTY);
        RED_FLOWER_1 = RecipeRegistry.registerMortarRecipe(Blocks.BLUE_ORCHID,   ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_BLUE_DYE, 2),  ItemStack.EMPTY);
        RED_FLOWER_2 = RecipeRegistry.registerMortarRecipe(Blocks.ALLIUM,        ItemStack.EMPTY, 6, new ItemStack(Items.MAGENTA_DYE, 2),     ItemStack.EMPTY);
        RED_FLOWER_3 = RecipeRegistry.registerMortarRecipe(Blocks.AZURE_BLUET,   ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_GRAY_DYE, 2),  ItemStack.EMPTY);
        RED_FLOWER_4 = RecipeRegistry.registerMortarRecipe(Blocks.RED_TULIP,     ItemStack.EMPTY, 6, new ItemStack(Items.RED_DYE, 2),         ItemStack.EMPTY);
        RED_FLOWER_5 = RecipeRegistry.registerMortarRecipe(Blocks.ORANGE_TULIP,  ItemStack.EMPTY, 6, new ItemStack(Items.ORANGE_DYE, 2),      ItemStack.EMPTY);
        RED_FLOWER_6 = RecipeRegistry.registerMortarRecipe(Blocks.WHITE_TULIP,   ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_GRAY_DYE, 2),  ItemStack.EMPTY);
        RED_FLOWER_7 = RecipeRegistry.registerMortarRecipe(Blocks.PINK_TULIP,    ItemStack.EMPTY, 6, new ItemStack(Items.PINK_DYE, 2),        ItemStack.EMPTY);
        RED_FLOWER_8 = RecipeRegistry.registerMortarRecipe(Blocks.OXEYE_DAISY,   ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_GRAY_DYE, 2),  ItemStack.EMPTY);
        YELLOW_FLOWER = RecipeRegistry.registerMortarRecipe(Blocks.DANDELION,    ItemStack.EMPTY, 6, new ItemStack(Items.YELLOW_DYE, 2),      ItemStack.EMPTY);

        DOUBLE_PLANT_0 = RecipeRegistry.registerMortarRecipe(Blocks.SUNFLOWER, ItemStack.EMPTY, 10, new ItemStack(Items.YELLOW_DYE, 4),  ItemStack.EMPTY);
        DOUBLE_PLANT_1 = RecipeRegistry.registerMortarRecipe(Blocks.LILAC,     ItemStack.EMPTY, 10, new ItemStack(Items.MAGENTA_DYE, 4), ItemStack.EMPTY);
        DOUBLE_PLANT_4 = RecipeRegistry.registerMortarRecipe(Blocks.ROSE_BUSH, ItemStack.EMPTY, 10, new ItemStack(Items.RED_DYE, 4),     ItemStack.EMPTY);
        DOUBLE_PLANT_5 = RecipeRegistry.registerMortarRecipe(Blocks.PEONY,     ItemStack.EMPTY, 10, new ItemStack(Items.PINK_DYE, 4),    ItemStack.EMPTY);

        OCOTILLO = RecipeRegistry.registerMortarRecipe(
            new ItemStack(ItemRegistry.item_ocotillo_flowers.get()),
            ItemStack.EMPTY, 8,
            new ItemStack(Items.ORANGE_DYE, 2), ItemStack.EMPTY);

        CACTUS_1 = RecipeRegistry.registerMortarRecipe(BlockRegistry.plant_barrel.get(),  ItemStack.EMPTY, 8, new ItemStack(Items.GREEN_DYE, 1), ItemStack.EMPTY);
        CACTUS_2 = RecipeRegistry.registerMortarRecipe(BlockRegistry.plant_prickly.get(), ItemStack.EMPTY, 8, new ItemStack(Items.GREEN_DYE, 1), ItemStack.EMPTY);
        CACTUS_3 = RecipeRegistry.registerMortarRecipe(Blocks.CACTUS,                     ItemStack.EMPTY, 8, new ItemStack(Items.GREEN_DYE, 1), ItemStack.EMPTY);

        ALOE       = RecipeRegistry.registerMortarRecipe(BlockRegistry.plant_aloe.get(),   ItemStack.EMPTY, 8, new ItemStack(ItemRegistry.aloe_paste.get()), ItemStack.EMPTY);
        FOXGLOVE   = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.ELECTROMANCY.getIndex()), ItemStack.EMPTY, 6, new ItemStack(Items.CYAN_DYE, 2),       ItemStack.EMPTY);
        SNOWBELL   = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.CRYOMANCY.getIndex()),    ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_BLUE_DYE, 2), ItemStack.EMPTY);
        MARIGOLD   = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.HELIOMANCY.getIndex()),   ItemStack.EMPTY, 6, new ItemStack(Items.ORANGE_DYE, 2),     ItemStack.EMPTY);
        MALLOW     = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.AURAMANCY.getIndex()),    ItemStack.EMPTY, 6, new ItemStack(Items.PINK_DYE, 2),       ItemStack.EMPTY);
        YARROW     = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.AEROMANCY.getIndex()),    ItemStack.EMPTY, 6, new ItemStack(Items.YELLOW_DYE, 2),     ItemStack.EMPTY);
        OLEANDER   = RecipeRegistry.registerMortarRecipe(BlockRegistry.plant_oleander.get(), ItemStack.EMPTY, 6, new ItemStack(Items.PINK_DYE, 2),       ItemStack.EMPTY);
        HEMLOCK    = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.INFERNALITY.getIndex()),  ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_GRAY_DYE, 2), ItemStack.EMPTY);
        VALLEYLILY = RecipeRegistry.registerMortarRecipe(BlockRegistry.plant_valleylily.get(),ItemStack.EMPTY, 6, new ItemStack(Items.LIGHT_GRAY_DYE, 2), ItemStack.EMPTY);
        LAVENDER   = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.HIEROMANCY.getIndex()),   ItemStack.EMPTY, 6, new ItemStack(Items.PURPLE_DYE, 2),     ItemStack.EMPTY);
        LITHOP     = RecipeRegistry.registerMortarRecipe(metaBlock(BlockRegistry.magic_plant, EnumMagicType.CHAOTICS.getIndex()),     ItemStack.EMPTY, 6, new ItemStack(ItemRegistry.item_lithop_pulp.get()),    ItemStack.EMPTY);
        MANDRAKE   = RecipeRegistry.registerMortarRecipe(metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.ANIMANCY.getIndex()),   ItemStack.EMPTY, 6, new ItemStack(ItemRegistry.item_mandrake_pulp.get()),  ItemStack.EMPTY);
        GRAVEROOT  = RecipeRegistry.registerMortarRecipe(metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.NECROMANCY.getIndex()), ItemStack.EMPTY, 6, new ItemStack(ItemRegistry.item_graveroot_pulp.get()), ItemStack.EMPTY);

        BEETROOT = RecipeRegistry.registerMortarRecipe(
            Items.BEETROOT, ItemStack.EMPTY, 6,
            new ItemStack(Items.RED_DYE, 2), ItemStack.EMPTY);

        ICE       = RecipeRegistry.registerMortarRecipe(Blocks.ICE, ItemStack.EMPTY, 10, new ItemStack(ItemRegistry.item_ice_shard.get(), 3), ItemStack.EMPTY);
        SHARD_ICE = RecipeRegistry.registerMortarRecipe(new ItemStack(ItemRegistry.item_ice_shard.get()), ItemStack.EMPTY, 10, new ItemStack(Items.SNOWBALL), ItemStack.EMPTY);

        NETHER_STAR = RecipeRegistry.registerMortarRecipe(
            Items.NETHER_STAR, ItemStack.EMPTY, 20,
            new ItemStack(ItemRegistry.shard_netherstar.get(), 9), ItemStack.EMPTY);

        REEDS = RecipeRegistry.registerMortarRecipe(
            Items.SUGAR_CANE, ItemStack.EMPTY, 6,
            new ItemStack(Items.SUGAR, 2), ItemStack.EMPTY);

        BOTTLE_WORMWOOD = RecipeRegistry.registerMortarRecipe(
            metaBlock(BlockRegistry.magic_plant, EnumMagicType.SPECTROMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.WORMWOOD.getIndex()));
        BOTTLE_STORMTHISTLE = RecipeRegistry.registerMortarRecipe(
            metaBlock(BlockRegistry.magic_plant, EnumMagicType.ELECTROMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            new ItemStack(Items.CYAN_DYE, 2),
            metaItem(ItemRegistry.bottle, EnumBottle.STORMTHISTLE.getIndex()));
        BOTTLE_SNOWBELL = RecipeRegistry.registerMortarRecipe(
            metaBlock(BlockRegistry.magic_plant, EnumMagicType.CRYOMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            new ItemStack(Items.LIGHT_BLUE_DYE, 2),
            metaItem(ItemRegistry.bottle, EnumBottle.SNOWBELL.getIndex()));
        BOTTLE_LOTUS = RecipeRegistry.registerMortarRecipe(
            metaBlock(BlockRegistry.magic_plant, EnumMagicType.HYDROMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.LOTUS.getIndex()));
        BOTTLE_BRAMBLE = RecipeRegistry.registerMortarRecipe(
            ItemRegistry.brambleberry.get(), ItemRegistry.bottle_empty.get(), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.BRAMBLEBERRY.getIndex()));
        BOTTLE_MANDRAKE = RecipeRegistry.registerMortarRecipe(
            metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.ANIMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.MANDRAKE.getIndex()));
        BOTTLE_POPPY = RecipeRegistry.registerMortarRecipe(
            Blocks.POPPY, new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            new ItemStack(Items.RED_DYE, 2),
            metaItem(ItemRegistry.bottle, EnumBottle.POPPY.getIndex()));
        BOTTLE_NIGHTBERRY = RecipeRegistry.registerMortarRecipe(
            metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.UMBRAMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.NIGHTBERRY.getIndex()));
        BOTTLE_FIREBERRY = RecipeRegistry.registerMortarRecipe(
            metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.PYROMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.FIREBERRY.getIndex()));
        BOTTLE_JIMSONWEED = RecipeRegistry.registerMortarRecipe(
            metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.ASTROMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.JIMSONWEED.getIndex()));
        BOTTLE_GRAVEROOT = RecipeRegistry.registerMortarRecipe(
            metaItem(ItemRegistry.seed_magic_plant, EnumMagicType.NECROMANCY.getIndex()),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            ItemStack.EMPTY,
            metaItem(ItemRegistry.bottle, EnumBottle.GRAVEROOT.getIndex()));
        BOTTLE_ROSE = RecipeRegistry.registerMortarRecipe(
            Blocks.ROSE_BUSH, new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            new ItemStack(Items.RED_DYE, 4),
            metaItem(ItemRegistry.bottle, EnumBottle.ROSE.getIndex()));
        RecipeRegistry.registerMortarRecipe(
            com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.plant_rose.get(),
            new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            new ItemStack(Items.RED_DYE, 4),
            metaItem(ItemRegistry.bottle, EnumBottle.ROSE.getIndex()));
        BOTTLE_SUNFLOWER = RecipeRegistry.registerMortarRecipe(
            Blocks.SUNFLOWER, new ItemStack(ItemRegistry.bottle_empty.get()), 6,
            new ItemStack(Items.YELLOW_DYE, 4),
            metaItem(ItemRegistry.bottle, EnumBottle.SUNFLOWER.getIndex()));
    }
}
