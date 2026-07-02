package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPagePotionEffect;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.BookPageWriteable;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipeSmall;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BookPageRegistry {
    public static final List<BookPage> PAGES = Lists.newArrayList();
    public static final List<BookPage> BASIC_MAGIC_BOOK = Lists.newArrayList();
    public static final List<BookPage> INTERMEDIATE_MAGIC_BOOK = Lists.newArrayList();
    public static final List<BookPage> ADVANCED_MAGIC_BOOK = Lists.newArrayList();
    public static List<BookPagePotionEffect> POTION_EFFECT_PAGES = Lists.newArrayList();
    public static final List<BookPage> TEA_BOOK = Lists.newArrayList();
    public static final List<BookPage> SPELLWORK_BOOK = Lists.newArrayList();
    public static final List<BookPage> EXPLORER_CODEX = Lists.newArrayList();
    public static final List<BookPage> ALCHEMY_BOOK = Lists.newArrayList();

    private static ItemStack stack(RegistryObject<Item> item, int meta) {
        ItemStack s = new ItemStack(item.get());
        s.setDamageValue(meta);
        return s;
    }

    private static ItemStack stack(RegistryObject<Item> item) {
        return new ItemStack(item.get());
    }

    private static ItemStack blockStack(RegistryObject<Block> block) {
        return new ItemStack(block.get());
    }

    private static MobEffectInstance firstEffect(RegistryObject<net.minecraft.world.item.alchemy.Potion> potionRO) {
        if (potionRO == null || !potionRO.isPresent()) return null;
        java.util.List<MobEffectInstance> effects = potionRO.get().getEffects();
        return effects.isEmpty() ? null : effects.get(0);
    }

    private static BookPagePotionEffect tryPotionPage(RegistryObject<net.minecraft.world.item.alchemy.Potion> potionRO,
                                                     Supplier<ItemStack> ingredient,
                                                     MagicSchool school, MagicElement element,
                                                     List<BookPage> alsoAddTo) {
        MobEffectInstance eff = firstEffect(potionRO);
        if (eff == null || school == null || element == null) return null;
        ItemStack ing = ingredient.get();
        if (ing == null || ing.isEmpty()) return null;
        BookPagePotionEffect p = new BookPagePotionEffect(
            eff, ing,
            new ItemStack(ItemRegistry.potion_drinkable.get()),
            school, element);
        if (alsoAddTo != null) p.addToList(alsoAddTo);
        return p;
    }

    public static void init() {
        BookMagicBasic.init();
        BookMagicIntermediate.init();
        BookMagicAdvanced.init();
        BookTea.init();
        BookSpellwork.init();
        BookExplorer.init();
        BookAlchemy.init();

        new BookPage("ccw_letter_1", Lists.newArrayList(
            new PageElementParagraphs(
                "ccw_letter_1", 0.55F, 0, 0,
                new ParagraphBox(16, 28, 0, 124, 112))));

        tryPotionPage(PotionTypeRegistry.BLURRY_VISION,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.CATTAIL.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.hydromancy, null);

        tryPotionPage(PotionTypeRegistry.COLOR_BLIND,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.OLEANDER.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.auramancy, null);

        tryPotionPage(PotionTypeRegistry.CONFUSION,
            () -> blockStack(BlockRegistry.mushroom_crop_dark),
            MagicSchoolRegistry.illusion, MagicElementRegistry.infernality, null);

        tryPotionPage(PotionTypeRegistry.DAZE,
            () -> stack(ItemRegistry.bottle, EnumBottle.WORMWOOD.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.spectromancy, null);

        tryPotionPage(PotionTypeRegistry.DISLOCATION,
            () -> new ItemStack(net.minecraft.world.item.Items.CHORUS_FRUIT),
            MagicSchoolRegistry.conjuration, MagicElementRegistry.chaotics, null);

        tryPotionPage(PotionTypeRegistry.HALLUCINATION,
            () -> blockStack(BlockRegistry.plant_shrooms),
            MagicSchoolRegistry.illusion, MagicElementRegistry.chaotics, null);

        BookPagePotionEffect haze = tryPotionPage(PotionTypeRegistry.HAZE,
            () -> stack(ItemRegistry.seed_mushroom_white),
            MagicSchoolRegistry.illusion, MagicElementRegistry.infernality, ADVANCED_MAGIC_BOOK);
        if (haze != null) {
            haze.addElement(new PageElementCraftingRecipeSmall(
                Lists.newArrayList(new ItemStack[]{ blockStack(BlockRegistry.mushroom_white) }),
                stack(ItemRegistry.seed_mushroom_white),
                156, 154));
        }

        tryPotionPage(PotionTypeRegistry.OBSCURATION,
            () -> stack(ItemRegistry.ingredient, EnumIngredient.BAT_WING.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.umbramancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.PARALYSIS,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.MAYBELL.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.necromancy, null);

        tryPotionPage(PotionTypeRegistry.PIXELATION,
            () -> stack(ItemRegistry.item_algae),
            MagicSchoolRegistry.illusion, MagicElementRegistry.hieromancy, null);

        tryPotionPage(PotionTypeRegistry.SLEEP,
            () -> stack(ItemRegistry.bottle, EnumBottle.NIGHTBERRY.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.umbramancy, null);

        tryPotionPage(PotionTypeRegistry.SNEEZING,
            () -> stack(ItemRegistry.item_ocotillo_flowers),
            MagicSchoolRegistry.illusion, MagicElementRegistry.animancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.STENCH,
            () -> stack(ItemRegistry.item_graveroot_pulp),
            MagicSchoolRegistry.transfiguration, MagicElementRegistry.necromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.ABSORPTION,
            () -> new ItemStack(net.minecraft.world.item.Items.GOLDEN_APPLE),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.animancy, null);

        tryPotionPage(PotionTypeRegistry.BLINDNESS,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.TENTACLE.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.umbramancy, null);

        tryPotionPage(PotionTypeRegistry.CREEPERS_BANE,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.HELIOMANCY.getIndex()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.heliomancy, null);

        tryPotionPage(PotionTypeRegistry.FEAR,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.PIG_TAIL.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.infernality, null);

        tryPotionPage(PotionTypeRegistry.FLAMABILITY,
            () -> stack(ItemRegistry.bottle, EnumBottle.FIREBERRY.getIndex()),
            MagicSchoolRegistry.transfiguration, MagicElementRegistry.pyromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.TOADS_TONGUE,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.TOAD_TONGUE.getIndex()),
            MagicSchoolRegistry.transfiguration, MagicElementRegistry.animancy, null);

        tryPotionPage(PotionTypeRegistry.FROSTBITE,
            () -> stack(ItemRegistry.bottle, EnumBottle.SNOWBELL.getIndex()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.cryomancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.DOLPHINS_GRACE,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.FISH_TAIL.getIndex()),
            MagicSchoolRegistry.conjuration, MagicElementRegistry.hydromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.PUFFER_TOXIN,
            () -> new ItemStack(net.minecraft.world.item.Items.PUFFERFISH),
            MagicSchoolRegistry.evocation, MagicElementRegistry.hydromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.GLOWING,
            () -> stack(ItemRegistry.bottle, EnumBottle.SUNFLOWER.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.heliomancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.HASTE,
            () -> stack(ItemRegistry.bottle, EnumBottle.POPPY.getIndex()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.electromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.HEALTH_BOOST,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.BEAR_HEART.getIndex()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.animancy, null);

        tryPotionPage(PotionTypeRegistry.HUNGER,
            () -> {
                ItemStack s = blockStack(BlockRegistry.magic_plant);
                net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
                bs.putString("type", "necromancy");
                s.getOrCreateTag().put("BlockStateTag", bs);
                return s;
            },
            MagicSchoolRegistry.evocation, MagicElementRegistry.necromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.INFATUATION,
            () -> stack(ItemRegistry.bottle, EnumBottle.ROSE.getIndex()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.animancy, null);

        tryPotionPage(PotionTypeRegistry.LEVITATION,
            () -> stack(ItemRegistry.tree_item, EnumMagicWood.ASH.getIndex()),
            MagicSchoolRegistry.conjuration, MagicElementRegistry.aeromancy, null);

        tryPotionPage(PotionTypeRegistry.MANA,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.AURAMANCY.getIndex()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.auramancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.MANA_REGEN,
            () -> stack(ItemRegistry.crushed_horn_unicorn),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.auramancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.SPELL_CHARGE,
            () -> stack(ItemRegistry.gem_dust, 10),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.auramancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.SPELL_CHARGE_REGEN,
            () -> new ItemStack(ItemRegistry.FAIRY_DUSTS.get(0).get()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.auramancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.MINING_FATIGUE,
            () -> new ItemStack(net.minecraft.world.item.Items.PRISMARINE_SHARD),
            MagicSchoolRegistry.evocation, MagicElementRegistry.cryomancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.NAUSEA,
            () -> stack(ItemRegistry.item_bladderwort),
            MagicSchoolRegistry.illusion, MagicElementRegistry.hydromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.POISON_THORNED,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.PYROMANCY.getIndex()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.umbramancy, null);

        tryPotionPage(PotionTypeRegistry.REPELLANT,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.FLYTRAP.getIndex()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.animancy, null);

        tryPotionPage(PotionTypeRegistry.RESISTANCE,
            () -> stack(ItemRegistry.tree_item, EnumMagicWood.PINE.getIndex()),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.geomancy, null);

        tryPotionPage(PotionTypeRegistry.SATURATION,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.WHEAT.getIndex()),
            MagicSchoolRegistry.conjuration, MagicElementRegistry.animancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.SLOWFALL,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.BIRD_HEART.getIndex()),
            MagicSchoolRegistry.transfiguration, MagicElementRegistry.aeromancy, null);

        tryPotionPage(PotionTypeRegistry.SPINED,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.ROSE.getIndex()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.animancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.STUNNED,
            () -> stack(ItemRegistry.bottle, EnumBottle.STORMTHISTLE.getIndex()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.electromancy, null);

        tryPotionPage(PotionTypeRegistry.UNDEAD,
            () -> stack(ItemRegistry.bottle, EnumBottle.GRAVEROOT.getIndex()),
            MagicSchoolRegistry.transfiguration, MagicElementRegistry.necromancy, null);

        tryPotionPage(PotionTypeRegistry.VULNERABILITY,
            () -> blockStack(BlockRegistry.mushroom_white),
            MagicSchoolRegistry.evocation, MagicElementRegistry.infernality, null);

        tryPotionPage(PotionTypeRegistry.WEAKLING,
            () -> blockStack(BlockRegistry.mushroom_dark),
            MagicSchoolRegistry.evocation, MagicElementRegistry.umbramancy, null);

        tryPotionPage(PotionTypeRegistry.BEWILDERMENT,
            () -> blockStack(BlockRegistry.mushroom_stalk),
            MagicSchoolRegistry.illusion, MagicElementRegistry.chaotics, null);

        tryPotionPage(PotionTypeRegistry.CERTAIN_DEATH,
            () -> stack(ItemRegistry.ingredient, com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.DRAGON_HEART.getIndex()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.necromancy, ADVANCED_MAGIC_BOOK);

        tryPotionPage(PotionTypeRegistry.SPIDER,
            () -> new ItemStack(net.minecraft.world.item.Items.SPIDER_EYE),
            MagicSchoolRegistry.transfiguration, MagicElementRegistry.animancy, null);

        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.FIRE_RESISTANCE,
            () -> new ItemStack(net.minecraft.world.item.Items.MAGMA_CREAM),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.pyromancy, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.STRENGTH,
            () -> new ItemStack(net.minecraft.world.item.Items.BLAZE_POWDER),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.pyromancy, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.SWIFTNESS,
            () -> new ItemStack(net.minecraft.world.item.Items.SUGAR),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.electromancy, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.LEAPING,
            () -> new ItemStack(net.minecraft.world.item.Items.RABBIT_FOOT),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.aeromancy, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.WATER_BREATHING,
            () -> new ItemStack(net.minecraft.world.item.Items.SALMON),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.hydromancy, null);

        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.SLOWNESS,
            () -> new ItemStack(BlockRegistry.dynamic_web.get()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.cryomancy, null);

        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.POISON,
            () -> new ItemStack(net.minecraft.world.item.Items.LILY_OF_THE_VALLEY),
            MagicSchoolRegistry.evocation, MagicElementRegistry.chaotics, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.HEALING,
            () -> new ItemStack(net.minecraft.world.item.Items.GLISTERING_MELON_SLICE),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.auramancy, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.REGENERATION,
            () -> new ItemStack(net.minecraft.world.item.Items.GHAST_TEAR),
            MagicSchoolRegistry.abjuration, MagicElementRegistry.auramancy, null);

        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.HARMING,
            () -> stack(ItemRegistry.crushed_plant, EnumPlantType.NIGHTBERRY.getIndex()),
            MagicSchoolRegistry.evocation, MagicElementRegistry.umbramancy, null);
        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.NIGHT_VISION,
            () -> new ItemStack(net.minecraft.world.item.Items.GOLDEN_CARROT),
            MagicSchoolRegistry.illusion, MagicElementRegistry.umbramancy, null);

        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.INVISIBILITY,
            () -> stack(ItemRegistry.tree_item, EnumMagicWood.WILLOW.ordinal()),
            MagicSchoolRegistry.illusion, MagicElementRegistry.umbramancy, null);

        addVanillaPotionPage(net.minecraft.world.item.alchemy.Potions.WEAKNESS,
            () -> blockStack(BlockRegistry.mushroom_grey),
            MagicSchoolRegistry.evocation, MagicElementRegistry.infernality, null);
    }

    private static BookPagePotionEffect addVanillaPotionPage(net.minecraft.world.item.alchemy.Potion potion,
                                                              Supplier<ItemStack> ingredient,
                                                              MagicSchool school, MagicElement element,
                                                              List<BookPage> alsoAddTo) {
        java.util.List<MobEffectInstance> effects = potion.getEffects();
        if (effects.isEmpty() || school == null || element == null) return null;
        ItemStack ing = ingredient.get();
        if (ing == null || ing.isEmpty()) return null;
        BookPagePotionEffect p = new BookPagePotionEffect(
            effects.get(0), ing,
            new ItemStack(ItemRegistry.potion_drinkable.get()),
            school, element);
        if (alsoAddTo != null) p.addToList(alsoAddTo);
        return p;
    }

    public static BookPage getBookPage(String name) {
        if (name.equalsIgnoreCase("chapter")) {
            return new BookPageChapter(null);
        }
        if (name.equalsIgnoreCase("table_content")) {
            return new BookPageTableContent(null);
        }
        if (name.length() > 9 && name.substring(0, 9).equalsIgnoreCase("writeable")) {
            String title = "";
            String paragraph = "";
            if (name.substring(9).split("<title>").length > 1) {
                String[] strs = name.substring(9).split("<title>")[0].split("<paragraph>");
                if (strs.length > 1) {
                    paragraph = strs[1];
                }
                title = strs[0];
            }
            return new BookPageWriteable(title, paragraph);
        }
        for (BookPage page : PAGES) {
            if (page.getName().equalsIgnoreCase(name)) return page;
        }
        return null;
    }

    public static BookPage getPotionEffectPage(MobEffect potion) {

        for (BookPagePotionEffect p : POTION_EFFECT_PAGES) {
            if (p.potion.getEffect().equals(potion)) return p;
        }
        return null;
    }

    public static List<BookPage> getPotionEffectPages(IMagicType magicType) {
        ArrayList<BookPage> pages = Lists.newArrayList();
        for (BookPagePotionEffect p : POTION_EFFECT_PAGES) {
            if (p.element == magicType || p.school == magicType) pages.add(p);
        }
        return pages;
    }
}
