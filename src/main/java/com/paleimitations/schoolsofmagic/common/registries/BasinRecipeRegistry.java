package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

public class BasinRecipeRegistry {

    private static ItemStack stack(RegistryObject<Item> ro, int meta) {
        ItemStack s = new ItemStack(ro.get());
        s.setDamageValue(meta);
        return s;
    }

    private static ItemStack blockStack(RegistryObject<Block> ro, int meta) {
        ItemStack s = new ItemStack(ro.get());

        EnumMagicType type = EnumMagicType.values()[meta];
        net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
        bs.putString("type", type.getSerializedName());
        s.getOrCreateTag().put("BlockStateTag", bs);
        net.minecraft.resources.ResourceLocation id =
            net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(ro.get());
        if (id != null) {
            s.setHoverName(net.minecraft.network.chat.Component.translatable(
                  "block." + id.getNamespace() + "." + id.getPath() + "." + type.getSerializedName())
               .withStyle(st -> st.withItalic(false)));
        }
        return s;
    }

    private static ItemStack teacup(String teaName) {

        RecipeTea recipe = RecipeRegistry.getTeaRecipe(teaName);
        if (recipe == null) {
            com.paleimitations.schoolsofmagic.common.util.Utils.getLogger()
                .warn("BasinRecipeRegistry: missing tea recipe '{}', using empty teacup", teaName);
            return new ItemStack(ItemRegistry.teacup.get());
        }
        return TeaUtils.appendEffects(new ItemStack(ItemRegistry.teacup.get()), recipe.getEffect());
    }

    private static ItemStack ingot(EnumMetal m) {
        return stack(ItemRegistry.ingot, m.getIndex());
    }

    public static void register() {

        RecipeRegistry.registerCatalystBasin(
            "ingotIron",
            Items.BLAZE_ROD,
            Items.LAVA_BUCKET,
            new ItemStack(Items.CHARCOAL),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            ingot(EnumMetal.STEEL));

        RecipeRegistry.registerCatalystBasin(
            stack(ItemRegistry.ingot, EnumMetal.SILVER.getIndex()),
            BlockRegistry.mushroom_dark.get(),
            stack(ItemRegistry.bottle, EnumBottle.JIMSONWEED.getIndex()),
            new ItemStack(ItemRegistry.item_obsidian_shard.get()),
            30.0F,
            MagicSchoolRegistry.generateArray(0, 19, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            ingot(EnumMetal.TENEBRIUM));

        for (EnumMagicType mt : EnumMagicType.values()) {

            EnumPlantType plant = EnumPlantType.values()[mt.getIndex() + 1];
            RecipeRegistry.registerCatalystBasin(
                "gemDiamond",
                stack(ItemRegistry.gem_dust, mt.getIndex()),
                Items.MAGMA_CREAM,
                stack(ItemRegistry.crushed_plant, plant.getIndex()),
                50.0F,
                MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
                MagicElementRegistry.generateArraySingle(mt.getIndex(), 4),
                stack(ItemRegistry.gem_chunk, mt.getIndex()));
        }

        RecipeRegistry.registerCatalystBasin(
            "ingotGold", Items.BLAZE_ROD, Items.LAVA_BUCKET,
            stack(ItemRegistry.nugget, EnumMetal.COPPER.getIndex()),
            50.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            ingot(EnumMetal.BRASS));

        RecipeRegistry.registerCatalystBasin(
            stack(ItemRegistry.ingot, EnumMetal.COPPER.getIndex()),
            new ItemStack(ItemRegistry.item_obsidian_shard.get()),
            Items.MAGMA_CREAM,
            stack(ItemRegistry.nugget, EnumMetal.SILVER.getIndex()),
            100.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            ingot(EnumMetal.BRONZE));
        RecipeRegistry.registerCatalystBasin(
            "ingotIron",

            new ItemStack(Items.GOLDEN_SHOVEL),
            Items.MAGMA_CREAM,
            "dustRedstone",
            20.0F,
            MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            ingot(EnumMetal.COPPER));
        RecipeRegistry.registerCatalystBasin(
            "ingotGold",
            stack(ItemRegistry.toad_spawn, 1),
            stack(ItemRegistry.bottle, EnumBottle.POPPY.getIndex()),

            Blocks.GLASS_PANE,
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            ingot(EnumMetal.SILVER));

        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", Items.BLAZE_POWDER, Items.LAVA_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumMagicType.PYROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.PYROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.PYROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", "dustGlowstone", stack(ItemRegistry.bottle, EnumBottle.SUNFLOWER.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.HELIOMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.HELIOMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.HELIOMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", "feather", Items.DRAGON_BREATH,
            stack(ItemRegistry.crushed_plant, EnumMagicType.AEROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.AEROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.AEROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", "dustRedstone", "slimeball",
            stack(ItemRegistry.crushed_plant, EnumMagicType.GEOMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.GEOMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.GEOMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", stack(ItemRegistry.toad_spawn, 3), stack(ItemRegistry.bottle, EnumBottle.MANDRAKE.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.ANIMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.ANIMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.ANIMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", "nuggetCopper", stack(ItemRegistry.bottle, EnumBottle.STORMTHISTLE.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.ELECTROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.ELECTROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.ELECTROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", new ItemStack(Items.PRISMARINE_CRYSTALS), Items.WATER_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumMagicType.HYDROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.HYDROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.HYDROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", new ItemStack(ItemRegistry.item_ice_shard.get()),
            stack(ItemRegistry.bottle, EnumBottle.SNOWBELL.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.CRYOMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.CRYOMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.CRYOMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", stack(ItemRegistry.item, EnumMisc.SHAMROCK.getIndex()),
            teacup("tea_hieromancy"),
            stack(ItemRegistry.crushed_plant, EnumMagicType.HIEROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.HIEROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.HIEROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", Items.GUNPOWDER, Items.DRAGON_BREATH,
            stack(ItemRegistry.crushed_plant, EnumMagicType.CHAOTICS.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.CHAOTICS.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.CHAOTICS.getIndex()));

        RecipeRegistry.registerCatalystBasin(
            "dustDiamond",
            stack(ItemRegistry.tree_item, com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.ASH.getIndex()),
            stack(ItemRegistry.bottle, EnumBottle.ROSE.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.AURAMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.AURAMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.AURAMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", new ItemStack(ItemRegistry.item_obsidian_shard.get()),
            stack(ItemRegistry.bottle, EnumBottle.JIMSONWEED.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.ASTROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.ASTROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.ASTROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", Items.FERMENTED_SPIDER_EYE, Items.GHAST_TEAR,
            stack(ItemRegistry.crushed_plant, EnumMagicType.INFERNALITY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.INFERNALITY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.INFERNALITY.getIndex()));

        RecipeRegistry.registerCatalystBasin(
            "dustDiamond",
            stack(ItemRegistry.tree_item, com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.WILLOW.getIndex()),
            stack(ItemRegistry.bottle, EnumBottle.WORMWOOD.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.SPECTROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.SPECTROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.SPECTROMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", new ItemStack(ItemRegistry.item_obsidian_shard.get()),
            stack(ItemRegistry.bottle, EnumBottle.NIGHTBERRY.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.UMBRAMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.UMBRAMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.UMBRAMANCY.getIndex()));
        RecipeRegistry.registerCatalystBasin(
            "dustDiamond", BlockRegistry.mushroom_stalk.get(),
            stack(ItemRegistry.bottle, EnumBottle.GRAVEROOT.getIndex()),
            stack(ItemRegistry.crushed_plant, EnumMagicType.NECROMANCY.getIndex() + 1),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.NECROMANCY.getIndex(), 4),
            stack(ItemRegistry.gem_dust, EnumMagicType.NECROMANCY.getIndex()));

        RecipeRegistry.registerCatalystBasin(
            blockStack(BlockRegistry.magic_plant, EnumMagicType.GEOMANCY.getIndex()),
            Items.GOLDEN_CARROT,
            new ItemStack(ItemRegistry.aloe_paste.get()),
            ItemRegistry.crushed_horn_unicorn.get(),
            20.0F,
            MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            stack(ItemRegistry.item, EnumMisc.SHAMROCK.getIndex()));

        RecipeRegistry.registerCatalystBasin(
            "slimeball", Items.BONE, Items.LAVA_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumPlantType.FIREBERRY.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.MAGMA_CREAM));

        RecipeRegistry.registerCatalystBasin(
            "dustGlowstone",
            new ItemStack(BlockRegistry.gem_pyromancy.get()),
            Items.LAVA_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumPlantType.FIREBERRY.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.BLAZE_POWDER));

        RecipeRegistry.registerCatalystBasin(
            "gemDiamond",
            new ItemStack(BlockRegistry.gem_hydromancy.get()),
            Items.WATER_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumPlantType.HYDROMANCY.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.PRISMARINE_CRYSTALS));

        RecipeRegistry.registerCatalystBasin(
            Items.QUARTZ,
            new ItemStack(BlockRegistry.gem_hydromancy.get()),
            Items.WATER_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumPlantType.HYDROMANCY.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.PRISMARINE_SHARD));

        RecipeRegistry.registerCatalystBasin(
            Items.ROTTEN_FLESH,
            new ItemStack(BlockRegistry.gem_auramancy.get()),
            Items.MILK_BUCKET,
            stack(ItemRegistry.crushed_plant, EnumPlantType.ANIMANCY.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.BEEF));

        RecipeRegistry.registerCatalystBasin(
            "egg",
            stack(ItemRegistry.ingredient, EnumIngredient.DRAGON_CLAW.getIndex()),
            Items.DRAGON_BREATH,
            Items.NETHER_STAR,
            100.0F,
            MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Blocks.DRAGON_EGG));

        RecipeRegistry.registerCatalystBasin(
            Items.APPLE,
            stack(ItemRegistry.toad_spawn, 11),
            stack(ItemRegistry.bottle, EnumBottle.SUNFLOWER.getIndex()),
            "ingotGold",
            20.0F,
            MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.GOLDEN_APPLE));

        RecipeRegistry.registerCatalystBasin(
            Items.BUCKET, Items.BLAZE_ROD, Items.MAGMA_CREAM,
            stack(ItemRegistry.crushed_plant, EnumPlantType.PYROMANCY.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.LAVA_BUCKET));

        RecipeRegistry.registerCatalystBasin(
            Items.BEETROOT_SEEDS,
            BlockRegistry.mushroom_dark.get(),
            stack(ItemRegistry.bottle, EnumBottle.GRAVEROOT.getIndex()),
            ItemRegistry.seed_mushroom_pink.get(),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.NETHER_WART));

        RecipeRegistry.registerCatalystBasin(
            "ingotBrickNether",
            Items.QUARTZ,
            stack(ItemRegistry.bottle, EnumBottle.NIGHTBERRY.getIndex()),
            "nuggetIron",
            5.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.FLINT));

        RecipeRegistry.registerCatalystBasin(
            "feather",
            ItemRegistry.shard_netherstar.get(),
            Items.MAGMA_CREAM,
            new ItemStack(BlockRegistry.gem_pyromancy.get()),
            50.0F,
            MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(0, 4),
            new ItemStack(ItemRegistry.feather_phoenix.get()));

        RecipeRegistry.registerCatalystBasin(
            "feather",
            ItemRegistry.shard_netherstar.get(),
            stack(ItemRegistry.bottle, EnumBottle.STORMTHISTLE.getIndex()),
            stack(ItemRegistry.gem_dust, EnumMagicType.ELECTROMANCY.getIndex()),
            50.0F,
            MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0),
            MagicElementRegistry.generateArraySingle(EnumMagicType.ELECTROMANCY.getIndex(), 4),
            new ItemStack(ItemRegistry.feather_thunderbird.get()));

        RecipeRegistry.registerCatalystBasin(
            Blocks.VINE,
            stack(ItemRegistry.crushed_plant, EnumPlantType.BLADDERWORT.getIndex()),
            stack(ItemRegistry.bottle, EnumBottle.ROSE.getIndex()),
            ItemRegistry.brambleberry.get(),
            100.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(BlockRegistry.plant_mistletoe.get()));

        registerToadSpawn(ItemRegistry.seed_mushroom_white,  "tea_infernality",     7);
        registerToadSpawn(ItemRegistry.seed_mushroom_white,  "tea_glowing",         11);
        registerToadSpawn(ItemRegistry.seed_mushroom_white,  "tea_speed",           2);
        registerToadSpawn(ItemRegistry.seed_mushroom_dark,   "tea_chaotics",        0);
        registerToadSpawn(ItemRegistry.seed_mushroom_dark,   "tea_umbramancy",      6);
        registerToadSpawn(ItemRegistry.seed_mushroom_dark,   "tea_hallucination",   8);
        registerToadSpawn(ItemRegistry.seed_mushroom_pink,   "tea_poison",          9);
        registerToadSpawn(ItemRegistry.seed_mushroom_pink,   "tea_fire_resistance", 4);
        registerToadSpawn(ItemRegistry.seed_mushroom_pink,   "tea_health",          5);
        registerToadSpawn(ItemRegistry.seed_mushroom_grey,   "tea_jump_boost",      10);
        registerToadSpawn(ItemRegistry.seed_mushroom_grey,   "tea_water_breathing", 1);
        registerToadSpawn(ItemRegistry.seed_mushroom_grey,   "tea_animancy",        3);

        RecipeRegistry.registerCatalystBasin(
            Items.STICK, stack(ItemRegistry.gem_chunk, EnumMagicType.PYROMANCY.getIndex()), Items.LAVA_BUCKET, Items.BLAZE_POWDER,
            100.0F, MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0), MagicElementRegistry.generateArraySingle(0, 4),
            new ItemStack(Items.BLAZE_ROD), true);

        RecipeRegistry.registerCatalystBasin(
            Items.GUNPOWDER, Items.BLAZE_POWDER, Items.NETHER_WART, Items.WATER_BUCKET,
            80.0F, MagicSchoolRegistry.generateArray(0, 4, 0, 0, 0, 0), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.GHAST_TEAR), new ItemStack(Items.BUCKET));

        RecipeRegistry.registerCatalystBasin(
            Items.APPLE, Items.ENDER_PEARL, stack(ItemRegistry.gem_chunk, EnumMagicType.CHAOTICS.getIndex()), ItemRegistry.item_lithop_pulp.get(),
            50.0F, MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0), MagicElementRegistry.generateArraySingle(9, 4),
            new ItemStack(Items.CHORUS_FRUIT), true);

        ItemStack regenPotion = net.minecraft.world.item.alchemy.PotionUtils.setPotion(
            new ItemStack(Items.POTION), net.minecraft.world.item.alchemy.Potions.REGENERATION);
        RecipeRegistry.registerCatalystBasin(
            regenPotion, teacup("tea_dislocation"), Items.POPPED_CHORUS_FRUIT, stack(ItemRegistry.gem_dust, EnumMagicType.CHAOTICS.getIndex()),
            100.0F, MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0), MagicElementRegistry.generateArraySingle(9, 4),
            new ItemStack(Items.DRAGON_BREATH), true);

        RecipeRegistry.registerCatalystBasin(
            Items.POTATO, Items.SPIDER_EYE, stack(ItemRegistry.bottle, EnumBottle.MANDRAKE.getIndex()), teacup("tea_poison"),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.POISONOUS_POTATO), new ItemStack(ItemRegistry.teacup_empty.get()));

        RecipeRegistry.registerCatalystBasin(
            Items.ROTTEN_FLESH, new ItemStack(ItemRegistry.bi_gem_auramancy.get()), Items.MILK_BUCKET, stack(ItemRegistry.crushed_plant, EnumPlantType.MANDRAKE.getIndex()),
            20.0F, MagicSchoolRegistry.generateEmptyArray(), MagicElementRegistry.generateEmptyArray(),
            new ItemStack(Items.LEATHER));
    }

    private static void registerToadSpawn(RegistryObject<Item> mushroomSeed, String teaName, int toadMeta) {
        RecipeRegistry.registerCatalystBasin(
            mushroomSeed.get(),
            ItemRegistry.horn_unicorn.get(),
            teacup(teaName),
            stack(ItemRegistry.seed_magic_plant, EnumMagicType.ASTROMANCY.getIndex()),
            50.0F,
            MagicSchoolRegistry.generateArray(0, 9, 0, 0, 0, 0),
            MagicElementRegistry.generateEmptyArray(),
            stack(ItemRegistry.toad_spawn, toadMeta));
    }
}
