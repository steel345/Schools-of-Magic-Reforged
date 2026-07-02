package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;

public class TeaRecipeRegistry {
    public static void init() {
        RecipeRegistry.registerTeaRecipe("tea_pyromancy", EnumPlantType.PYROMANCY, EnumPlantType.PYROMANCY, EnumPlantType.PYROMANCY, new MobEffectInstance(PotionRegistry.pyromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_heliomancy", EnumPlantType.HELIOMANCY, EnumPlantType.HELIOMANCY, EnumPlantType.HELIOMANCY, new MobEffectInstance(PotionRegistry.heliomancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_aeromancy", EnumPlantType.AEROMANCY, EnumPlantType.AEROMANCY, EnumPlantType.AEROMANCY, new MobEffectInstance(PotionRegistry.aeromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_geomancy", EnumPlantType.GEOMANCY, EnumPlantType.GEOMANCY, EnumPlantType.GEOMANCY, new MobEffectInstance(PotionRegistry.geomancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_animancy", EnumPlantType.ANIMANCY, EnumPlantType.ANIMANCY, EnumPlantType.ANIMANCY, new MobEffectInstance(PotionRegistry.animancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_electromancy", EnumPlantType.ELECTROMANCY, EnumPlantType.ELECTROMANCY, EnumPlantType.ELECTROMANCY, new MobEffectInstance(PotionRegistry.electromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_hydromancy", EnumPlantType.HYDROMANCY, EnumPlantType.HYDROMANCY, EnumPlantType.HYDROMANCY, new MobEffectInstance(PotionRegistry.hydromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_spectromancy", EnumPlantType.SPECTROMANCY, EnumPlantType.SPECTROMANCY, EnumPlantType.SPECTROMANCY, new MobEffectInstance(PotionRegistry.spectromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_chaotics", EnumPlantType.CHAOTICS, EnumPlantType.CHAOTICS, EnumPlantType.CHAOTICS, new MobEffectInstance(PotionRegistry.chaotics.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_auramancy", EnumPlantType.AURAMANCY, EnumPlantType.AURAMANCY, EnumPlantType.AURAMANCY, new MobEffectInstance(PotionRegistry.auramancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_astromancy", EnumPlantType.ASTROMANCY, EnumPlantType.ASTROMANCY, EnumPlantType.ASTROMANCY, new MobEffectInstance(PotionRegistry.astromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_infernality", EnumPlantType.INFERNALITY, EnumPlantType.INFERNALITY, EnumPlantType.INFERNALITY, new MobEffectInstance(PotionRegistry.infernality.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_hieromancy", EnumPlantType.HIEROMANCY, EnumPlantType.HIEROMANCY, EnumPlantType.HIEROMANCY, new MobEffectInstance(PotionRegistry.hieromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_umbramancy", EnumPlantType.UMBRAMANCY, EnumPlantType.UMBRAMANCY, EnumPlantType.UMBRAMANCY, new MobEffectInstance(PotionRegistry.umbramancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_necromancy", EnumPlantType.NECROMANCY, EnumPlantType.NECROMANCY, EnumPlantType.NECROMANCY, new MobEffectInstance(PotionRegistry.necromancy.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_cryomancy", EnumPlantType.CRYOMANCY, EnumPlantType.CRYOMANCY, EnumPlantType.CRYOMANCY, new MobEffectInstance(PotionRegistry.cryomancy.get(), 1200));

        RecipeRegistry.registerTeaRecipe("tea_fire_resistance", EnumPlantType.PYROMANCY, EnumPlantType.FIREBERRY, EnumPlantType.SAGE, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400));
        RecipeRegistry.registerTeaRecipe("tea_water_breathing", EnumPlantType.HYDROMANCY, EnumPlantType.HYDRANGEA, EnumPlantType.SAGE, new MobEffectInstance(MobEffects.WATER_BREATHING, 600));
        RecipeRegistry.registerTeaRecipe("tea_night_vision", EnumPlantType.HELIOMANCY, EnumPlantType.CREOSOTE, EnumPlantType.CARROT, new MobEffectInstance(MobEffects.NIGHT_VISION, 600));
        RecipeRegistry.registerTeaRecipe("tea_jump_boost", EnumPlantType.ELECTROMANCY, EnumPlantType.AURAMANCY, EnumPlantType.CARROT, new MobEffectInstance(MobEffects.JUMP, 400));
        RecipeRegistry.registerTeaRecipe("tea_haste", EnumPlantType.ELECTROMANCY, EnumPlantType.FIREBERRY, EnumPlantType.SUGARCANE, new MobEffectInstance(MobEffects.DIG_SPEED, 400));
        RecipeRegistry.registerTeaRecipe("tea_speed", EnumPlantType.ELECTROMANCY, EnumPlantType.BEANSTALK, EnumPlantType.SUGARCANE, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600));
        RecipeRegistry.registerTeaRecipe("tea_regeneration", EnumPlantType.SPECTROMANCY, EnumPlantType.AURAMANCY, EnumPlantType.MANDRAKE, new MobEffectInstance(MobEffects.REGENERATION, 400));
        RecipeRegistry.registerTeaRecipe("tea_health", EnumPlantType.SPECTROMANCY, EnumPlantType.SPECTROMANCY, EnumPlantType.BRITTLEBUSH, new MobEffectInstance(MobEffects.HEAL, 1));
        RecipeRegistry.registerTeaRecipe("tea_resistance", EnumPlantType.GEOMANCY, EnumPlantType.ROSE, EnumPlantType.FIREBERRY, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400));
        RecipeRegistry.registerTeaRecipe("tea_invisibility", EnumPlantType.ASTROMANCY, EnumPlantType.UMBRAMANCY, EnumPlantType.SUNFLOWER, new MobEffectInstance(MobEffects.INVISIBILITY, 400));
        RecipeRegistry.registerTeaRecipe("tea_saturation", EnumPlantType.ANIMANCY, EnumPlantType.WHEAT, EnumPlantType.CARROT, new MobEffectInstance(MobEffects.SATURATION, 600, 3));

        RecipeRegistry.registerTeaRecipe("tea_luck", EnumPlantType.HELIOMANCY, EnumPlantType.CATTAIL, shamrock(), new MobEffectInstance(MobEffects.LUCK, 400));
        RecipeRegistry.registerTeaRecipe("tea_wither", EnumPlantType.NECROMANCY, EnumPlantType.INFERNALITY, EnumPlantType.BRAMBLE, new MobEffectInstance(MobEffects.WITHER, 300));
        RecipeRegistry.registerTeaRecipe("tea_blindness", EnumPlantType.UMBRAMANCY, EnumPlantType.GRAVEROOT, EnumPlantType.OLEANDER, new MobEffectInstance(MobEffects.BLINDNESS, 400));
        RecipeRegistry.registerTeaRecipe("tea_weakness", EnumPlantType.HIEROMANCY, EnumPlantType.MAYBELL, EnumPlantType.BLADDERWORT, new MobEffectInstance(MobEffects.WEAKNESS, 400));
        RecipeRegistry.registerTeaRecipe("tea_hunger", EnumPlantType.SPECTROMANCY, EnumPlantType.GRAVEROOT, EnumPlantType.NIGHTBERRY, new MobEffectInstance(MobEffects.HUNGER, 400));
        RecipeRegistry.registerTeaRecipe("tea_poison", EnumPlantType.INFERNALITY, EnumPlantType.NIGHTBERRY, EnumPlantType.MAYBELL, new MobEffectInstance(MobEffects.POISON, 400));
        RecipeRegistry.registerTeaRecipe("tea_nausea", EnumPlantType.HIEROMANCY, EnumPlantType.MISTLETOE, EnumPlantType.OLEANDER, new MobEffectInstance(MobEffects.CONFUSION, 400));
        RecipeRegistry.registerTeaRecipe("tea_damage", EnumPlantType.NECROMANCY, EnumPlantType.SPECTROMANCY, EnumPlantType.OLEANDER, new MobEffectInstance(MobEffects.HARM, 1));
        RecipeRegistry.registerTeaRecipe("tea_mining_fatigue", EnumPlantType.CRYOMANCY, EnumPlantType.GRAVEROOT, EnumPlantType.WHEAT, new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 400));
        RecipeRegistry.registerTeaRecipe("tea_slowness", EnumPlantType.CRYOMANCY, EnumPlantType.PEONY, EnumPlantType.SUNFLOWER, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400));
        RecipeRegistry.registerTeaRecipe("tea_levitation", EnumPlantType.CHAOTICS, EnumPlantType.AEROMANCY, EnumPlantType.PEONY, new MobEffectInstance(MobEffects.LEVITATION, 300));
        RecipeRegistry.registerTeaRecipe("tea_slowfall", EnumPlantType.AEROMANCY, EnumPlantType.PALM, EnumPlantType.BRITTLEBUSH, new MobEffectInstance(PotionRegistry.slowfall.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_repellant", EnumPlantType.AURAMANCY, EnumPlantType.MISTLETOE, EnumPlantType.FLYTRAP, new MobEffectInstance(PotionRegistry.repellant.get(), 400));

        RecipeRegistry.registerTeaRecipe("tea_creepers_bane", EnumPlantType.HELIOMANCY, EnumPlantType.SAGE, shamrock(), new MobEffectInstance(PotionRegistry.creepers_bane.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_blurry_vision", EnumPlantType.HIEROMANCY, EnumPlantType.CARROT, EnumPlantType.MISTLETOE, new MobEffectInstance(PotionRegistry.blurry_vision.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_mana", EnumPlantType.SPECTROMANCY, EnumPlantType.ELECTROMANCY, shamrock(), new MobEffectInstance(PotionRegistry.mana.get(), 1));
        RecipeRegistry.registerTeaRecipe("tea_mana_regen", EnumPlantType.ELECTROMANCY, EnumPlantType.SAGE, EnumPlantType.FIREBERRY, new MobEffectInstance(PotionRegistry.mana_regen.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_dislocation", EnumPlantType.CHAOTICS, EnumPlantType.PALM, EnumPlantType.MAYBELL, new MobEffectInstance(PotionRegistry.dislocation.get(), 300));
        RecipeRegistry.registerTeaRecipe("tea_spined", EnumPlantType.ANIMANCY, EnumPlantType.ROSE, EnumPlantType.BRAMBLE, new MobEffectInstance(PotionRegistry.spined.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_stench", EnumPlantType.INFERNALITY, EnumPlantType.GRAVEROOT, EnumPlantType.AEROMANCY, new MobEffectInstance(PotionRegistry.stench.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_obscuration", EnumPlantType.UMBRAMANCY, EnumPlantType.UMBRAMANCY, EnumPlantType.MISTLETOE, new MobEffectInstance(PotionRegistry.obscuration.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_color_blind", EnumPlantType.UMBRAMANCY, EnumPlantType.BLADDERWORT, EnumPlantType.MAYBELL, new MobEffectInstance(PotionRegistry.color_blind.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_earth_protection", EnumPlantType.GEOMANCY, EnumPlantType.PALM, EnumPlantType.SAGE, new MobEffectInstance(PotionRegistry.earth_protection.get(), 1200));
        RecipeRegistry.registerTeaRecipe("tea_flamability", EnumPlantType.PYROMANCY, EnumPlantType.BRITTLEBUSH, EnumPlantType.INFERNALITY, new MobEffectInstance(PotionRegistry.flamability.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_frostbite", EnumPlantType.CRYOMANCY, EnumPlantType.BRITTLEBUSH, EnumPlantType.INFERNALITY, new MobEffectInstance(PotionRegistry.frostbite.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_glowing", EnumPlantType.HELIOMANCY, EnumPlantType.SUNFLOWER, EnumPlantType.CREOSOTE, new MobEffectInstance(MobEffects.GLOWING, 400));
        RecipeRegistry.registerTeaRecipe("tea_stunned", EnumPlantType.ELECTROMANCY, EnumPlantType.ELECTROMANCY, EnumPlantType.NIGHTBERRY, new MobEffectInstance(PotionRegistry.stunned.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_undead", EnumPlantType.NECROMANCY, EnumPlantType.NECROMANCY, EnumPlantType.NIGHTBERRY, new MobEffectInstance(PotionRegistry.undead.get(), 600));
        RecipeRegistry.registerTeaRecipe("tea_hallucination", EnumPlantType.HIEROMANCY, EnumPlantType.HIEROMANCY, EnumPlantType.MISTLETOE, new MobEffectInstance(PotionRegistry.hallucination.get(), 400));
        RecipeRegistry.registerTeaRecipe("tea_sleep", EnumPlantType.ASTROMANCY, EnumPlantType.ASTROMANCY, EnumPlantType.CREOSOTE, new MobEffectInstance(PotionRegistry.sleep.get(), 200));
        RecipeRegistry.registerTeaRecipe("tea_charging", EnumPlantType.AURAMANCY, EnumPlantType.PEONY, EnumPlantType.CHAOTICS, new MobEffectInstance(PotionRegistry.spell_charge_regen.get(), 900));
        RecipeRegistry.registerTeaRecipe("tea_spellweaver", EnumPlantType.SUNFLOWER, EnumPlantType.HIEROMANCY, shamrock(), new MobEffectInstance(PotionRegistry.spell_charge.get(), 1, 1)).setOverlayColor(0x18A7B5);
    }

    private static ItemStack shamrock() {
        ItemStack s = new ItemStack(ItemRegistry.item.get());
        s.setDamageValue(EnumMisc.SHAMROCK.getIndex());
        return s;
    }

}
