package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.SpellHelper;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellBlaze;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellDefuse;
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
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellIceShell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellIgnite;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellIncinerate;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellInvisibility;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellIronHide;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLaunchStone;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLevitate;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLocateLava;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLocateWater;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLocateOre;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellMeteorStrike;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellMutateSkeleton;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellMutateZombie;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellPhantomFire;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellPollenCloud;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellPractice;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellRaiseSkeleton;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellRaiseZombie;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellSeaTrade;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellShadowSpy;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellShulkerBullet;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellSpectralHand;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellThornRing;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellTremor;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWaterJet;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWinterRoar;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWitherBlight;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellBreak;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFireBall;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFortifiedBreath;
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
        tryRegister("locate_water",   SpellLocateWater::new);
        tryRegister("fiery_blessing", SpellFieryBlessing::new);
        tryRegister("phantom_fire",   SpellPhantomFire::new);
        tryRegister("fuel_furnace",   SpellFuelFurnace::new);
        tryRegister("dry",            SpellDry::new);
        tryRegister("mend",           com.paleimitations.schoolsofmagic.common.spells.spells.SpellMend::new);
        tryRegister("prestidigitation", com.paleimitations.schoolsofmagic.common.spells.spells.SpellPrestidigitation::new);
        tryRegister("smoke_scry",     com.paleimitations.schoolsofmagic.common.spells.spells.SpellSmokeScry::new);
        tryRegister("scorch",         com.paleimitations.schoolsofmagic.common.spells.spells.SpellScorch::new);

        tryRegister("zephyr",         SpellZephyr::new);
        tryRegister("fortified_breath", SpellFortifiedBreath::new);
        tryRegister("fireball",       SpellFireBall::new);
        tryRegister("break",          SpellBreak::new);
        tryRegister("sea_trade",      SpellSeaTrade::new);
        tryRegister("practice",       SpellPractice::new);
        tryRegister("defuse",         SpellDefuse::new);
        tryRegister("levitate",       SpellLevitate::new);
        tryRegister("iron_hide",      SpellIronHide::new);
        tryRegister("ice_shell",  SpellIceShell::new);
        tryRegister("gale",           SpellGale::new);
        tryRegister("age",            com.paleimitations.schoolsofmagic.common.spells.spells.SpellAge::new);
        tryRegister("dazzling_light", com.paleimitations.schoolsofmagic.common.spells.spells.SpellDazzlingLight::new);
        tryRegister("teleport",       com.paleimitations.schoolsofmagic.common.spells.spells.SpellTeleport::new);
        tryRegister("gaseous_form",   com.paleimitations.schoolsofmagic.common.spells.spells.SpellGaseousForm::new);

        tryRegister("rift", com.paleimitations.schoolsofmagic.common.spells.spells.SpellRift::new);
        tryRegister("poseidons_fist", com.paleimitations.schoolsofmagic.common.spells.spells.SpellPoseidonsFist::new);
        tryRegister("decoy", com.paleimitations.schoolsofmagic.common.spells.spells.SpellDecoy::new);
        tryRegister("biome_scry", com.paleimitations.schoolsofmagic.common.spells.spells.SpellBiomeScry::new);
        tryRegister("animal_scry", com.paleimitations.schoolsofmagic.common.spells.spells.SpellAnimalScry::new);
        tryRegister("whirlwind", com.paleimitations.schoolsofmagic.common.spells.spells.SpellWhirlwind::new);
        tryRegister("fog", com.paleimitations.schoolsofmagic.common.spells.spells.SpellFog::new);
        tryRegister("silence", com.paleimitations.schoolsofmagic.common.spells.spells.SpellSilence::new);
        tryRegister("disrupt_flight", com.paleimitations.schoolsofmagic.common.spells.spells.SpellDisruptFlight::new);
        tryRegister("detect_breath", com.paleimitations.schoolsofmagic.common.spells.spells.SpellDetectBreath::new);
        tryRegister("alarm", com.paleimitations.schoolsofmagic.common.spells.spells.SpellAlarm::new);
        tryRegister("gaian_warrior", com.paleimitations.schoolsofmagic.common.spells.spells.SpellGaianWarrior::new);
        tryRegister("earthen_elevator", com.paleimitations.schoolsofmagic.common.spells.spells.SpellEarthenElevator::new);
        tryRegister("launch_stone",   SpellLaunchStone::new);
        tryRegister("locate_ore",     SpellLocateOre::new);
        tryRegister("tremor",         SpellTremor::new);
        tryRegister("earthquake",     SpellEarthquake::new);

        tryRegister("shroomlucination", com.paleimitations.schoolsofmagic.common.spells.spells.SpellShroomlucination::new);
        tryRegister("thorn_ring",     SpellThornRing::new);
        tryRegister("pollen_cloud",   SpellPollenCloud::new);
        tryRegister("growth",         SpellGrowth::new);
        tryRegister("grow_apple",     SpellGrowApple::new);

        tryRegister("electrocute",    SpellElectrocute::new);
        tryRegister("sun_blast",      com.paleimitations.schoolsofmagic.common.spells.spells.SpellSunBlast::new);
        tryRegister("solar_barrage", com.paleimitations.schoolsofmagic.common.spells.spells.SpellSolarBarrage::new);
        tryRegister("sundial", com.paleimitations.schoolsofmagic.common.spells.spells.SpellSundial::new);
        tryRegister("sun_screen", com.paleimitations.schoolsofmagic.common.spells.spells.SpellSunScreen::new);
        tryRegister("illuminate", com.paleimitations.schoolsofmagic.common.spells.spells.SpellIlluminate::new);
        tryRegister("solar_orb", com.paleimitations.schoolsofmagic.common.spells.spells.SpellSolarOrb::new);
        tryRegister("luster_tool", com.paleimitations.schoolsofmagic.common.spells.spells.SpellLusterTool::new);
        tryRegister("flash_decoy", com.paleimitations.schoolsofmagic.common.spells.spells.SpellFlashDecoy::new);
        tryRegister("shining_shield", com.paleimitations.schoolsofmagic.common.spells.spells.SpellShiningShield::new);
        tryRegister("energize",       SpellEnergize::new);

        tryRegister("water_jet",      SpellWaterJet::new);
        tryRegister("winter_roar",    SpellWinterRoar::new);

        tryRegister("magic_missile",  com.paleimitations.schoolsofmagic.common.spells.spells.SpellMagicMissile::new);
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
        tryRegister("precision_strike", com.paleimitations.schoolsofmagic.common.spells.spells.SpellPrecisionStrike::new);
        tryRegister("counterspell",   com.paleimitations.schoolsofmagic.common.spells.spells.SpellCounterspell::new);
    }

    public static final java.util.Comparator<Spell> BY_POWER =
        java.util.Comparator.comparingInt(Spell::getMinimumMagicianLevel)
            .thenComparingDouble(Spell::getCost)
            .thenComparing(Spell::getName);

    public static void addSorted(List<BookPage> book, Spell... spells) {
        ArrayList<Spell> list = new ArrayList<>(java.util.Arrays.asList(spells));
        list.sort(BY_POWER);
        for (Spell spell : list) new BookPageSpell(spell).addToList(book);
    }

    private static List<BookPage> pagesOf(List<Spell> spells) {
        spells.sort(BY_POWER);
        ArrayList<BookPage> pages = new ArrayList<>();
        for (Spell spell : spells) pages.add(new BookPageSpell(spell));
        return pages;
    }

    public static List<BookPage> getPagesBySchool(MagicSchool school) {
        ArrayList<Spell> found = new ArrayList<>();
        for (Spell spell : SPELLS) {
            if (spell.getSchools().contains(school)) found.add(spell);
        }
        return pagesOf(found);
    }

    public static List<BookPage> getPagesByElement(MagicElement element) {
        ArrayList<Spell> found = new ArrayList<>();
        for (Spell spell : SPELLS) {
            if (spell.getElements().contains(element)) found.add(spell);
        }
        return pagesOf(found);
    }

    public static List<BookPage> getPages() {
        ArrayList<Spell> sorted = new ArrayList<>(SPELLS);
        sorted.sort(java.util.Comparator.comparingInt(
            (Spell s) -> s.getElements().isEmpty() ? 999 : s.getElements().get(0).getId())
            .thenComparing(BY_POWER));
        ArrayList<BookPage> pages = new ArrayList<>();
        for (Spell spell : sorted) pages.add(new BookPageSpell(spell));
        return pages;
    }
}
