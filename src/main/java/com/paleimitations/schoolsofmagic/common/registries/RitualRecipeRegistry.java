package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class RitualRecipeRegistry {

    public static com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting CAULDRON_NORMAL;
    public static com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting CAULDRON_GOLD;
    public static com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting CAULDRON_LION;

    private static ItemStack stack(net.minecraft.world.item.Item item, int meta) {
        ItemStack s = new ItemStack(item);
        s.setDamageValue(meta);
        return s;
    }

    private static ItemStack cauldron(EnumCauldronType type) {
        ItemStack s = new ItemStack(BlockRegistry.cauldron.get());
        CompoundTag bs = new CompoundTag();
        bs.putString("type", type.getSerializedName());
        s.getOrCreateTag().put("BlockStateTag", bs);
        return s;
    }

    private static ItemStack magicSapling(String type) {
        ItemStack s = new ItemStack(ItemRegistry.bi_magic_sapling.get());
        CompoundTag bs = new CompoundTag();
        bs.putString("type", type);
        s.getOrCreateTag().put("BlockStateTag", bs);
        return s;
    }

    private static ItemStack metalBlock(EnumMetal m) {
        ItemStack s = new ItemStack(BlockRegistry.metal_block.get());

        CompoundTag bs = new CompoundTag();
        bs.putString("type", m.getSerializedName());
        s.getOrCreateTag().put("BlockStateTag", bs);
        return s;
    }

    public static void register() {

        Ingredient anySapling = net.minecraftforge.common.crafting.CompoundIngredient.of(
            Ingredient.of(ItemTags.SAPLINGS),
            Ingredient.of(
                magicSapling("ash"),
                magicSapling("elder"),
                magicSapling("pine"),
                magicSapling("willow"),
                magicSapling("yew"),
                magicSapling("verde"),
                new ItemStack(ItemRegistry.bi_sapling_palm.get())
            )
        );
        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY,
            150, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            anySapling,
            Ingredient.of(new ItemStack(ItemRegistry.bi_trap_spike.get()), new ItemStack(ItemRegistry.bi_spear.get())),
            stack(ItemRegistry.gem_dust.get(), EnumMagicType.AURAMANCY.getIndex()),
            Ingredient.of(ItemTags.LOGS),
            stack(ItemRegistry.tree_item.get(), EnumMagicWood.ASH.getIndex()),
            stack(ItemRegistry.bottle.get(), EnumBottle.WORMWOOD.getIndex())
        );

        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY,
            200, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.BLAZE_ROD),
            new ItemStack(Items.LAVA_BUCKET),
            stack(ItemRegistry.ingredient.get(), EnumIngredient.BIRD_HEART.getIndex()),
            stack(ItemRegistry.gem_chunk.get(), EnumMagicType.PYROMANCY.getIndex()),
            new ItemStack(Items.MUTTON),
            stack(ItemRegistry.bottle.get(), EnumBottle.FIREBERRY.getIndex())
        ).setNote("Chicken in range");

        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY, 50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.bottle.get(), EnumBottle.STORMTHISTLE.getIndex()),
            stack(ItemRegistry.seed_magic_plant.get(), EnumMagicType.ANIMANCY.getIndex()),
            new ItemStack(ItemRegistry.bi_mushroom_dark.get())
        );
        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY, 50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.crushed_plant.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.HYDROMANCY.getIndex()),
            new ItemStack(Items.WATER_BUCKET),
            stack(ItemRegistry.seed_magic_plant.get(), EnumMagicType.ANIMANCY.getIndex())
        );
        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY, 50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.MILK_BUCKET),
            stack(ItemRegistry.crushed_plant.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.ANIMANCY.getIndex())
        );
        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY, 50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.bottle.get(), EnumBottle.NIGHTBERRY.getIndex()),
            new ItemStack(Items.STONE_AXE)
        );
        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY, 50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.bottle.get(), EnumBottle.SUNFLOWER.getIndex()),
            stack(ItemRegistry.gem_dust.get(), EnumMagicType.HELIOMANCY.getIndex())
        );
        RecipeRegistry.registerRitualRecipe(
            ItemStack.EMPTY, 40, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(ItemRegistry.magic_diamond.get()),
            stack(ItemRegistry.bottle.get(), EnumBottle.JIMSONWEED.getIndex())
        ).setNote("1-8 Moon Dew, +10 mana each");


        RecipeRegistry.registerRitualRecipe(
            new ItemStack(ItemRegistry.exploration_book.get()),
            50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.BOOK),
            new ItemStack(Items.SPRUCE_SAPLING),
            new ItemStack(Items.LAPIS_LAZULI),
            new ItemStack(Items.MAP),
            new ItemStack(Items.INK_SAC)
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(ItemRegistry.spellworkers_handbook.get()),
            50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.BOOK),
            new ItemStack(Items.RED_DYE),
            stack(ItemRegistry.ingot.get(), EnumMetal.BRASS.getIndex()),
            stack(ItemRegistry.crushed_plant.get(), EnumPlantType.HYDRANGEA.getIndex())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(ItemRegistry.magic_diamond.get()),
            50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.DIAMOND),
            new ItemStack(Items.REDSTONE),
            new ItemStack(Items.GLOWSTONE_DUST),
            new ItemStack(Items.GUNPOWDER),
            stack(ItemRegistry.seed_magic_plant.get(), EnumMagicType.ANIMANCY.getIndex())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(ItemRegistry.broom.get()),
            100, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.HAY_BLOCK),
            new ItemStack(Items.STICK),
            new ItemStack(ItemRegistry.crushed_horn_unicorn.get())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(ItemRegistry.magic_broom.get()),
            250, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            Ingredient.of(ItemRegistry.broom.get()),
            new ItemStack(ItemRegistry.flying_ointment.get())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(BlockRegistry.divination_crystal.get()),
            100, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.ingot.get(), EnumMetal.BRASS.getIndex()),

            new ItemStack(net.minecraft.world.level.block.Blocks.WHITE_STAINED_GLASS),
            "plankWood",
            stack(ItemRegistry.crushed_plant.get(), EnumPlantType.HYDRANGEA.getIndex())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(BlockRegistry.spell_forge.get()),
            150, 4, 0, Maps.newHashMap(), Maps.newHashMap(),
            "blockGlass",
            "blockGlass",
            new ItemStack(BlockRegistry.fence_steel.get()),
            stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
            stack(ItemRegistry.crushed_plant.get(), EnumPlantType.HYDRANGEA.getIndex())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(BlockRegistry.catalyst_basin.get()),
            100, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            metalBlock(EnumMetal.STEEL),
            stack(ItemRegistry.crushed_plant.get(), EnumPlantType.SUNFLOWER.getIndex())
        );

        CAULDRON_NORMAL = RecipeRegistry.registerRitualRecipe(
            cauldron(EnumCauldronType.NORMAL),
            150, 4, 0, Maps.newHashMap(), Maps.newHashMap(),
            new ItemStack(Items.CAULDRON),
            stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
            new ItemStack(Items.STICK),
            stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
            new ItemStack(Items.STICK)
        );

        CAULDRON_GOLD = RecipeRegistry.registerRitualRecipe(
            cauldron(EnumCauldronType.GOLD),
            250, 14, 0, Maps.newHashMap(), Maps.newHashMap(),
            cauldron(EnumCauldronType.NORMAL),
            new ItemStack(Items.GOLD_INGOT),
            new ItemStack(Items.GOLD_INGOT),
            stack(ItemRegistry.crushed_plant.get(), EnumPlantType.MANDRAKE.getIndex())
        );

        CAULDRON_LION = RecipeRegistry.registerRitualRecipe(
            cauldron(EnumCauldronType.LION),
            350, 29, 0, Maps.newHashMap(), Maps.newHashMap(),
            cauldron(EnumCauldronType.GOLD),
            stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
            stack(ItemRegistry.ingot.get(), EnumMetal.BRASS.getIndex()),
            stack(ItemRegistry.ingredient.get(), EnumIngredient.VILLAGER_HEART.getIndex())
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(BlockRegistry.mystic_gold_block.get()),
            200, 19, 0, Maps.newHashMap(), Maps.newHashMap(),
            "blockGold",
            "dustGlowstone",
            Items.BLAZE_POWDER,
            "dustGlowstone",
            Items.BLAZE_POWDER
        );

        RecipeRegistry.registerRitualRecipe(
            stack(ItemRegistry.wand_apprentice.get(), 1),
            50, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.wand_apprentice.get(), 0),
            "gemDiamond"
        );
        RecipeRegistry.registerRitualRecipe(
            stack(ItemRegistry.wand_apprentice.get(), 2),
            75, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.wand_apprentice.get(), 1),
            "gemDiamond"
        );
        RecipeRegistry.registerRitualRecipe(
            stack(ItemRegistry.wand_apprentice.get(), 3),
            100, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            stack(ItemRegistry.wand_apprentice.get(), 2),
            "gemDiamond"
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(Items.EXPERIENCE_BOTTLE),
            200, 19, 4, Maps.newHashMap(), Maps.newHashMap(),
            Items.GLASS_BOTTLE
        );

        RecipeRegistry.registerRitualRecipe(
            new ItemStack(ItemRegistry.potion_bag.get()),
            100, 0, 0, Maps.newHashMap(), Maps.newHashMap(),
            Items.LEATHER,
            Items.LEATHER,
            new ItemStack(ItemRegistry.bi_herbal_twine.get()),
            Items.LEATHER,
            Items.LEATHER,
            new ItemStack(ItemRegistry.bi_herbal_twine.get()),
            Items.CHEST,
            new ItemStack(ItemRegistry.crushed_horn_unicorn.get())
        );

        for (IWandData.EnumCoreType core : IWandData.EnumCoreType.values()) {
            for (IWandData.EnumHandleType handle : IWandData.EnumHandleType.values()) {
                for (IWandData.EnumGemType gem : IWandData.EnumGemType.values()) {
                    ItemStack out = new ItemStack(ItemRegistry.wand_advanced.get());

                    com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData wd =
                        new com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData();
                    wd.setCoreType(core);
                    wd.setHandleType(handle);
                    wd.setGemType(gem);
                    out.getOrCreateTag().put("wand_data", wd.serializeNBT());
                    IWandData live = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.CapabilityWandData.getCapability(out);
                    if (live != null) {
                        live.setCoreType(core);
                        live.setHandleType(handle);
                        live.setGemType(gem);
                    }

                    RecipeRegistry.registerRitualRecipe(
                        out,
                        150, 4, 0, Maps.newHashMap(), Maps.newHashMap(),
                        gem.item,
                        handle.item,
                        stack(ItemRegistry.wand_core.get(), core.ordinal())
                    );
                }
            }
        }
    }
}
