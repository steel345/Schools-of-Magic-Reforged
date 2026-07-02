package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.rituals.Ritual;
import com.paleimitations.schoolsofmagic.common.rituals.RitualHelper;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualCrafting;
import com.paleimitations.schoolsofmagic.common.rituals.rituals.RitualPotionCrystal;

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
        tryRegister("crafting",       RitualCrafting::new);

    }
}
