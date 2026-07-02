package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellBlaze;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellDry;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellEarthquake;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellElectrocute;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellEnergize;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFieryBlessing;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFirering;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFuelFurnace;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellGale;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellGrowApple;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellGrowth;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellIgnite;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellIncinerate;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellInvisibility;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLaunchStone;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLocateLava;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLocateOre;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellMeteorStrike;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellMutateSkeleton;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellMutateZombie;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellPhantomFire;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellPollenCloud;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellRaiseSkeleton;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellRaiseZombie;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellShadowSpy;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellShulkerBullet;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellSpectralHand;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellThornRing;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellTremor;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWaterJet;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWinterRoar;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWitherBlight;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellZephyr;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpellRegistry {
    public static final List<Spell> SPELLS = new ArrayList<>();

    private static void tryRegister(String name, Supplier<Spell> supplier) {
        try {
            SpellHelper.registerSpellHelpers(supplier.get());
        } catch (Throwable t) {
            com.paleimitations.schoolsofmagic.common.util.Utils.getLogger()
                .error("Failed to register spell '{}': {}", name, t.toString());
        }
    }

    public static void init() {

        com.paleimitations.schoolsofmagic.common.spells.SpellHelper.registerHelperOnly(
            new com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom());

        tryRegister("blaze",          SpellBlaze::new);
        tryRegister("firering",       SpellFirering::new);
        tryRegister("incinerate",     SpellIncinerate::new);
        tryRegister("ignite",         SpellIgnite::new);
        tryRegister("locate_lava",    SpellLocateLava::new);
        tryRegister("fiery_blessing", SpellFieryBlessing::new);
        tryRegister("phantom_fire",   SpellPhantomFire::new);
        tryRegister("fuel_furnace",   SpellFuelFurnace::new);
        tryRegister("dry",            SpellDry::new);

        tryRegister("zephyr",         SpellZephyr::new);
        tryRegister("gale",           SpellGale::new);

        tryRegister("launch_stone",   SpellLaunchStone::new);
        tryRegister("locate_ore",     SpellLocateOre::new);
        tryRegister("tremor",         SpellTremor::new);
        tryRegister("earthquake",     SpellEarthquake::new);

        tryRegister("thorn_ring",     SpellThornRing::new);
        tryRegister("pollen_cloud",   SpellPollenCloud::new);
        tryRegister("growth",         SpellGrowth::new);
        tryRegister("grow_apple",     SpellGrowApple::new);

        tryRegister("electrocute",    SpellElectrocute::new);
        tryRegister("energize",       SpellEnergize::new);

        tryRegister("water_jet",      SpellWaterJet::new);
        tryRegister("winter_roar",    SpellWinterRoar::new);

        tryRegister("shulker_bullet", SpellShulkerBullet::new);
        tryRegister("meteor_strike",  SpellMeteorStrike::new);
        tryRegister("spectral_hand",  SpellSpectralHand::new);

        tryRegister("shadow_spy",     SpellShadowSpy::new);
        tryRegister("invisibility",   SpellInvisibility::new);
        tryRegister("wither_blight",  SpellWitherBlight::new);

        tryRegister("raise_skeleton", SpellRaiseSkeleton::new);
        tryRegister("raise_zombie",   SpellRaiseZombie::new);
        tryRegister("mutate_skeleton", SpellMutateSkeleton::new);
        tryRegister("mutate_zombie",  SpellMutateZombie::new);

        tryRegister("snowball",       com.paleimitations.schoolsofmagic.common.spells.spells.SpellSnowball::new);
        tryRegister("thunderstroke",  com.paleimitations.schoolsofmagic.common.spells.spells.SpellThunderstroke::new);
        tryRegister("fang_mangle",    com.paleimitations.schoolsofmagic.common.spells.spells.SpellFangMangle::new);
        tryRegister("healing",        com.paleimitations.schoolsofmagic.common.spells.spells.SpellHealing::new);
        tryRegister("summon_bee",     com.paleimitations.schoolsofmagic.common.spells.spells.SpellSummonBee::new);
        tryRegister("translocation",  com.paleimitations.schoolsofmagic.common.spells.spells.SpellTranslocation::new);
        tryRegister("rumor",          com.paleimitations.schoolsofmagic.common.spells.spells.SpellRumor::new);
        tryRegister("fast_forward",   com.paleimitations.schoolsofmagic.common.spells.spells.SpellFastForward::new);
        tryRegister("counterspell",   com.paleimitations.schoolsofmagic.common.spells.spells.SpellCounterspell::new);
    }

    public static List<BookPage> getPagesBySchool(MagicSchool school) {
        ArrayList<BookPage> pages = new ArrayList<>();
        for (Spell spell : SPELLS) {
            if (spell.getSchools().contains(school)) pages.add(new BookPageSpell(spell));
        }
        return pages;
    }

    public static List<BookPage> getPagesByElement(MagicElement element) {
        ArrayList<BookPage> pages = new ArrayList<>();
        for (Spell spell : SPELLS) {
            if (spell.getElements().contains(element)) pages.add(new BookPageSpell(spell));
        }
        return pages;
    }

    public static List<BookPage> getPages() {
        ArrayList<Spell> sorted = new ArrayList<>(SPELLS);
        sorted.sort(java.util.Comparator.comparingInt(
            s -> s.getElements().isEmpty() ? 999 : s.getElements().get(0).getId()));
        ArrayList<BookPage> pages = new ArrayList<>();
        for (Spell spell : sorted) pages.add(new BookPageSpell(spell));
        return pages;
    }
}
