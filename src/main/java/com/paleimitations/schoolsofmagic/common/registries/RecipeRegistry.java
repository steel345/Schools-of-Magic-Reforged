package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public final class RecipeRegistry {
    public static final List<RecipeMortNPest> mortnpestRecipes = new ArrayList<>();
    public static final List<RecipeTea> teaRecipes = new ArrayList<>();
    public static final List<RecipeCatalystBasin> catalystRecipes = new ArrayList<>();
    public static final List<RecipeRitualCrafting> ritualRecipes = new ArrayList<>();

    public static RecipeMortNPest registerMortarRecipe(Object input, Object input_secondary, int crush, ItemStack output, ItemStack output_secondary) {
        RecipeMortNPest recipe = new RecipeMortNPest(output, output_secondary, crush, input, input_secondary);
        mortnpestRecipes.add(recipe);
        return recipe;
    }

    public static RecipeRitualCrafting registerRitualRecipe(ItemStack output, int mana, int minMagicianLevelIn, int minRitualLevelIn,
                                                            Map<MagicSchool, Integer> minSchoolLevelsIn,
                                                            Map<MagicElement, Integer> minElementLevelsIn,
                                                            Object... inputs) {
        Preconditions.checkArgument(inputs.length <= 9);
        RecipeRitualCrafting recipe = new RecipeRitualCrafting(output, mana, minMagicianLevelIn, minRitualLevelIn, minSchoolLevelsIn, minElementLevelsIn, inputs);
        ritualRecipes.add(recipe);

        return recipe;
    }

    public static RecipeMortNPest getMortarRecipe(ItemStack output) {

        for (RecipeMortNPest r : mortnpestRecipes) {
            ItemStack ro = r.getOutput();
            ItemStack rs = r.getOutputSecondary();
            if ((ItemStack.isSameItem(ro, output) && ro.getDamageValue() == output.getDamageValue())
                || (ItemStack.isSameItem(rs, output) && rs.getDamageValue() == output.getDamageValue())) {
                return r;
            }
        }
        return null;
    }

    public static List<RecipeMortNPest> getMortarRecipeByInput(ItemStack input) {
        ArrayList<RecipeMortNPest> output = Lists.newArrayList();
        for (RecipeMortNPest r : mortnpestRecipes) {
            if (r.compareStacks(r.getInput(), input) || r.compareStacks(r.getInputSecondary(), input)) output.add(r);
        }
        return output;
    }

    public static RecipeMortNPest getMortarRecipe(EnumPlantType plantType) {

        for (RecipeMortNPest r : mortnpestRecipes) {
            ItemStack out = r.getOutput();
            if (out.getItem() == ItemRegistry.crushed_plant.get()
                && out.getDamageValue() == plantType.getIndex()) {
                return r;
            }
        }
        return null;
    }

    public static RecipeTea getTeaRecipe(String name) {
        for (RecipeTea tea : teaRecipes) {
            if (tea.getName().equalsIgnoreCase(name)) return tea;
        }
        return null;
    }

    public static RecipeTea registerTeaRecipe(String name, EnumPlantType input1, EnumPlantType input2, EnumPlantType input3, MobEffectInstance effect) {
        RecipeTea recipe = new RecipeTea(name, input1, input2, input3, effect);
        teaRecipes.add(recipe);
        return recipe;
    }

    public static RecipeTea registerTeaRecipe(String name, EnumPlantType input1, EnumPlantType input2, ItemStack input3, MobEffectInstance effect) {
        RecipeTea recipe = new RecipeTea(name, input1, input2, input3, effect);
        teaRecipes.add(recipe);
        return recipe;
    }

    public static RecipeRitualCrafting getRitualRecipe(ItemStack output) {

        for (RecipeRitualCrafting r : ritualRecipes) {
            ItemStack ro = r.getOutput();
            if (ItemStack.isSameItem(ro, output)
                && ro.getDamageValue() == output.getDamageValue()
                && java.util.Objects.equals(ro.getTag(), output.getTag())) {
                return r;
            }
        }
        for (RecipeRitualCrafting r : ritualRecipes) {
            if (ItemStack.isSameItem(r.getOutput(), output)) return r;
        }
        return null;
    }

    public static RecipeCatalystBasin registerCatalystBasin(Object substrate, Object catalyst, Object solution, Object reactant, float manaCost, int[] schoolLevels, int[] elementLevels, ItemStack output) {
        RecipeCatalystBasin recipe = new RecipeCatalystBasin(substrate, catalyst, solution, reactant, manaCost, schoolLevels, elementLevels, output);
        catalystRecipes.add(recipe);
        return recipe;
    }

    public static RecipeCatalystBasin registerCatalystBasin(Object substrate, Object catalyst, Object solution, Object reactant, float manaCost, int[] schoolLevels, int[] elementLevels, ItemStack output, ItemStack leftover) {
        RecipeCatalystBasin recipe = new RecipeCatalystBasin(substrate, catalyst, solution, reactant, manaCost, schoolLevels, elementLevels, output, leftover);
        catalystRecipes.add(recipe);
        return recipe;
    }

    public static RecipeCatalystBasin registerCatalystBasin(Object substrate, Object catalyst, Object solution, Object reactant, float manaCost, int[] schoolLevels, int[] elementLevels, ItemStack output, boolean requirementOr) {
        RecipeCatalystBasin recipe = new RecipeCatalystBasin(substrate, catalyst, solution, reactant, manaCost, schoolLevels, elementLevels, output, ItemStack.EMPTY, requirementOr);
        catalystRecipes.add(recipe);
        return recipe;
    }

    public static RecipeCatalystBasin getCatalystRecipe(ItemStack output) {

        for (RecipeCatalystBasin r : catalystRecipes) {
            ItemStack ro = r.getOutput();
            if (ItemStack.isSameItem(ro, output) && ro.getDamageValue() == output.getDamageValue()) {
                return r;
            }
        }
        return null;
    }
}
