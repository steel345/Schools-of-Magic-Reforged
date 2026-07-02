package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

    private static ItemStack metalBlock(EnumMetal m) {
        ItemStack s = new ItemStack(BlockRegistry.metal_block.get());

        CompoundTag bs = new CompoundTag();
        bs.putString("type", m.getSerializedName());
        s.getOrCreateTag().put("BlockStateTag", bs);
        return s;
    }

    public static void register() {

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
