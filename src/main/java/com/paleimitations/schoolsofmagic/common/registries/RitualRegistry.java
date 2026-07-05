package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.rituals.RitualHelper;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualCrafting;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualFlamingBird;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualLuna;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualNatureSpirit;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualPotionCrystal;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualWeather;

import java.util.List;
import java.util.function.Supplier;

public class RitualRegistry {
    public static List<Ritual> RITUALS = Lists.newArrayList();

    private static void tryRegister(String name, Supplier<Ritual> supplier) {
        try {
            RitualHelper.registerRitualHelper(supplier.get());
        } catch (Throwable t) {
            com.paleimitations.schoolsofmagic.common.util.Utils.getLogger()
                .error("Failed to register ritual '{}': {}", name, t.toString());
        }
    }

    public static void init() {
        tryRegister("potion_crystal", RitualPotionCrystal::new);
        tryRegister("nature_spirit",  RitualNatureSpirit::new);
        tryRegister("flaming_bird",   RitualFlamingBird::new);
        tryRegister("storm",          () -> new RitualWeather("storm_ritual"));
        tryRegister("rain",           () -> new RitualWeather("rain_ritual"));
        tryRegister("clear_sky",      () -> new RitualWeather("clear_sky_ritual"));
        tryRegister("total_eclipse",  () -> new RitualWeather("total_eclipse_ritual"));
        tryRegister("rising_sun",     () -> new RitualWeather("rising_sun_ritual"));
        tryRegister("luna",           RitualLuna::new);
        tryRegister("crafting",       RitualCrafting::new);

    }
}
