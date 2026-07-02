package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.imitationcore.common.utils.FloatRange;
import com.paleimitations.imitationcore.common.utils.IntegerRange;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.Buyable;
import com.paleimitations.schoolsofmagic.common.spells.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.item.ItemStack;

public class BuyableRegistry {
    public static List<Buyable> BUYABLES = Lists.newArrayList();
    private static final Random rand = new Random();

    private static FloatRange[] emptySchools() {
        FloatRange[] r = new FloatRange[6];
        for (int i = 0; i < 6; i++) r[i] = FloatRange.EMPTY;
        return r;
    }

    private static FloatRange[] emptyElements() {
        FloatRange[] r = new FloatRange[16];
        for (int i = 0; i < 16; i++) r[i] = FloatRange.EMPTY;
        return r;
    }

    private static void persistModifierCap(ItemStack stack) {
        stack.getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).ifPresent(d -> {
            net.minecraft.nbt.CompoundTag fc = stack.getOrCreateTag().getCompound("ForgeCaps");
            fc.put("Parent", d.serializeNBT());
            stack.getOrCreateTag().put("ForgeCaps", fc);
        });
    }

    public static void register() {
        for (Spell.EnumSpellModifier mod : Spell.EnumSpellModifier.values()) {
            ItemStack scroll = new ItemStack(ItemRegistry.spell_modifier_scroll.get());

            scroll.getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY)
                .ifPresent(data -> data.setSpellModifier(mod, mod.defaultObj));
            persistModifierCap(scroll);

            if (mod.id == 16) {

                FloatRange[] schoolRanges = emptySchools();
                FloatRange[] elementRanges = emptyElements();
                elementRanges[mod.level - 1] = new FloatRange(10.0F, 50.0F);
                new Buyable(
                    scroll,
                    new FloatRange(10.0F, 50.0F),
                    new FloatRange(10.0F, 50.0F),
                    FloatRange.EMPTY,
                    FloatRange.EMPTY,
                    schoolRanges, elementRanges, false,
                    new FloatRange(50.0F, 100.0F),
                    5);
            } else if (new IntegerRange(6, 11).inRange(mod.id)) {

                FloatRange[] schoolRanges = emptySchools();
                schoolRanges[mod.id - 6] = new FloatRange(5.0F, 30.0F);
                new Buyable(
                    scroll,
                    new FloatRange(10.0F, 50.0F),
                    new FloatRange(10.0F, 50.0F),
                    FloatRange.EMPTY,
                    FloatRange.EMPTY,
                    schoolRanges, emptyElements(), false,
                    new FloatRange(50.0F, 100.0F),
                    7);
            } else if (new IntegerRange(12, 13).inRange(mod.id)) {

                FloatRange[] schoolRanges = emptySchools();
                schoolRanges[MagicSchoolRegistry.abjuration.getId()] = new FloatRange(5.0F, 30.0F);
                new Buyable(
                    scroll,
                    new FloatRange(10.0F, 50.0F),
                    new FloatRange(10.0F, 50.0F),
                    FloatRange.EMPTY,
                    FloatRange.EMPTY,
                    schoolRanges, emptyElements(), false,
                    new FloatRange(50.0F, 100.0F),
                    5);
            } else if (mod.id == 14) {

                new Buyable(
                    scroll,
                    new FloatRange(10.0F, 50.0F),
                    new FloatRange(10.0F, 50.0F),
                    FloatRange.EMPTY,
                    new FloatRange(10.0F, 50.0F),
                    emptySchools(), emptyElements(), false,
                    new FloatRange(50.0F, 100.0F),
                    5);
            } else {

                FloatRange[] schoolRanges = emptySchools();
                if (mod.id == 1) {
                    schoolRanges[MagicSchoolRegistry.transfiguration.getId()] =
                        new FloatRange(mod.level * 5.0F, (mod.level + 2) * 5.0F);
                } else if (mod.id == 4) {
                    schoolRanges[MagicSchoolRegistry.conjuration.getId()] =
                        new FloatRange(mod.level * 5.0F, (mod.level + 2) * 5.0F);
                } else if (mod.id == 17) {
                    schoolRanges[MagicSchoolRegistry.divination.getId()] =
                        new FloatRange(mod.level * 5.0F, (mod.level + 2) * 5.0F);
                }
                FloatRange[] elementRanges = emptyElements();

                switch (mod.level) {
                    case 1 -> new Buyable(scroll,
                        new FloatRange(5.0F, 50.0F),  FloatRange.EMPTY,
                        FloatRange.EMPTY, FloatRange.EMPTY,
                        schoolRanges, elementRanges, false,
                        new FloatRange(10.0F, 40.0F), 0);
                    case 2 -> new Buyable(scroll,
                        new FloatRange(10.0F, 60.0F), new FloatRange(5.0F, 50.0F),
                        FloatRange.EMPTY, FloatRange.EMPTY,
                        schoolRanges, elementRanges, false,
                        new FloatRange(20.0F, 60.0F), 2);
                    case 3 -> new Buyable(scroll,
                        new FloatRange(20.0F, 70.0F), new FloatRange(10.0F, 60.0F),
                        FloatRange.EMPTY, FloatRange.EMPTY,
                        schoolRanges, elementRanges, false,
                        new FloatRange(20.0F, 60.0F), 4);
                    case 4 -> new Buyable(scroll,
                        new FloatRange(30.0F, 90.0F), new FloatRange(20.0F, 70.0F),
                        FloatRange.EMPTY, FloatRange.EMPTY,
                        schoolRanges, elementRanges, false,
                        new FloatRange(20.0F, 60.0F), 5);
                    case 5 -> new Buyable(scroll,
                        new FloatRange(50.0F, 100.0F), new FloatRange(30.0F, 90.0F),
                        FloatRange.EMPTY, FloatRange.EMPTY,
                        schoolRanges, elementRanges, false,
                        new FloatRange(80.0F, 100.0F), 7);
                }
            }
        }
    }

    public static List<Buyable> getBuyablesByTier(int tier) {
        ArrayList<Buyable> buyables = Lists.newArrayList();
        for (Buyable buy : BUYABLES) {
            if (buy.getTierValue() == tier) buyables.add(buy);
        }
        return buyables;
    }

    public static boolean chanceOfTier(int tier, float luck) {
        switch (tier) {
            case 0: return rand.nextFloat() < 0.3f;
            case 1: return rand.nextFloat() < 0.2f;
            case 2: return rand.nextFloat() < 0.1f;
            case 3: return rand.nextFloat() < 0.1f  + luck * 0.3f;
            case 4: return rand.nextFloat() < 0.08f + luck * 0.3f;
            case 5: return rand.nextFloat() < 0.08f + luck * 0.75f * 0.3f;
            case 6: return rand.nextFloat() < 0.05f + luck * 0.5f * 0.3f;
            case 7: return rand.nextFloat() < 0.01f + luck * 0.25f * 0.3f;
        }
        return false;
    }
}
