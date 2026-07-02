package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionTypeRegistry {
   public static final DeferredRegister<Potion> POTIONS =
      DeferredRegister.create(ForgeRegistries.POTIONS, SchoolsOfMagic.MODID);

   public static final RegistryObject<Potion> STENCH = POTIONS.register("stench",
      () -> new Potion("stench", new MobEffectInstance(PotionRegistry.stench.get(), 900)));
   public static final RegistryObject<Potion> LONG_STENCH = POTIONS.register("long_stench",
      () -> new Potion("stench", new MobEffectInstance(PotionRegistry.stench.get(), 1800)));

   public static final RegistryObject<Potion> DAZE = POTIONS.register("daze",
      () -> new Potion("daze", new MobEffectInstance(PotionRegistry.daze.get(), 900)));
   public static final RegistryObject<Potion> LONG_DAZE = POTIONS.register("long_daze",
      () -> new Potion("daze", new MobEffectInstance(PotionRegistry.daze.get(), 1800)));

   public static final RegistryObject<Potion> PARALYSIS = POTIONS.register("paralysis",
      () -> new Potion("paralysis", new MobEffectInstance(PotionRegistry.paralysis.get(), 900)));
   public static final RegistryObject<Potion> LONG_PARALYSIS = POTIONS.register("long_paralysis",
      () -> new Potion("paralysis", new MobEffectInstance(PotionRegistry.paralysis.get(), 1800)));

   public static final RegistryObject<Potion> SLEEP = POTIONS.register("sleep",
      () -> new Potion("sleep", new MobEffectInstance(PotionRegistry.sleep.get(), 900)));
   public static final RegistryObject<Potion> LONG_SLEEP = POTIONS.register("long_sleep",
      () -> new Potion("sleep", new MobEffectInstance(PotionRegistry.sleep.get(), 1800)));

   public static final RegistryObject<Potion> SNEEZING = POTIONS.register("sneezing",
      () -> new Potion("sneezing", new MobEffectInstance(PotionRegistry.sneezing.get(), 900)));
   public static final RegistryObject<Potion> LONG_SNEEZING = POTIONS.register("long_sneezing",
      () -> new Potion("sneezing", new MobEffectInstance(PotionRegistry.sneezing.get(), 1800)));
   public static final RegistryObject<Potion> STRONG_SNEEZING = POTIONS.register("strong_sneezing",
      () -> new Potion("sneezing", new MobEffectInstance(PotionRegistry.sneezing.get(), 600, 1)));

   public static final RegistryObject<Potion> BEWILDERMENT = POTIONS.register("bewilderment",
      () -> new Potion("bewilderment", new MobEffectInstance(PotionRegistry.bewilderment.get(), 900)));

   public static final RegistryObject<Potion> CERTAIN_DEATH = POTIONS.register("certain_death",
      () -> new Potion("certain_death", new MobEffectInstance(PotionRegistry.certain_death.get(), 600)));

   public static final RegistryObject<Potion> DISLOCATION = POTIONS.register("dislocation",
      () -> new Potion("dislocation", new MobEffectInstance(PotionRegistry.dislocation.get(), 1200)));
   public static final RegistryObject<Potion> LONG_DISLOCATION = POTIONS.register("long_dislocation",
      () -> new Potion("dislocation", new MobEffectInstance(PotionRegistry.dislocation.get(), 2400)));
   public static final RegistryObject<Potion> STRONG_DISLOCATION = POTIONS.register("strong_dislocation",
      () -> new Potion("dislocation", new MobEffectInstance(PotionRegistry.dislocation.get(), 600, 1)));

   public static final RegistryObject<Potion> SPIDER = POTIONS.register("spider",
      () -> new Potion("spider", new MobEffectInstance(PotionRegistry.spider.get(), 3600)));

   public static final RegistryObject<Potion> WITHER = POTIONS.register("wither",
      () -> new Potion("wither", new MobEffectInstance(net.minecraft.world.effect.MobEffects.WITHER, 800)));

   public static final RegistryObject<Potion> HALLUCINATION = POTIONS.register("hallucination",
      () -> new Potion("hallucination", new MobEffectInstance(PotionRegistry.hallucination.get(), 900)));
   public static final RegistryObject<Potion> LONG_HALLUCINATION = POTIONS.register("long_hallucination",
      () -> new Potion("hallucination", new MobEffectInstance(PotionRegistry.hallucination.get(), 1800)));

   public static final RegistryObject<Potion> BLURRY_VISION = POTIONS.register("blurry_vision",
      () -> new Potion("blurry_vision", new MobEffectInstance(PotionRegistry.blurry_vision.get(), 900)));
   public static final RegistryObject<Potion> LONG_BLURRY_VISION = POTIONS.register("long_blurry_vision",
      () -> new Potion("blurry_vision", new MobEffectInstance(PotionRegistry.blurry_vision.get(), 1800)));

   public static final RegistryObject<Potion> OBSCURATION = POTIONS.register("obscuration",
      () -> new Potion("obscuration", new MobEffectInstance(PotionRegistry.obscuration.get(), 900)));
   public static final RegistryObject<Potion> LONG_OBSCURATION = POTIONS.register("long_obscuration",
      () -> new Potion("obscuration", new MobEffectInstance(PotionRegistry.obscuration.get(), 1800)));

   public static final RegistryObject<Potion> COLOR_BLIND = POTIONS.register("color_blind",
      () -> new Potion("color_blind", new MobEffectInstance(PotionRegistry.color_blind.get(), 900)));
   public static final RegistryObject<Potion> LONG_COLOR_BLIND = POTIONS.register("long_color_blind",
      () -> new Potion("color_blind", new MobEffectInstance(PotionRegistry.color_blind.get(), 1800)));

   public static final RegistryObject<Potion> CONFUSION = POTIONS.register("confusion",
      () -> new Potion("confusion", new MobEffectInstance(PotionRegistry.confusion2.get(), 900)));
   public static final RegistryObject<Potion> LONG_CONFUSION = POTIONS.register("long_confusion",
      () -> new Potion("confusion", new MobEffectInstance(PotionRegistry.confusion2.get(), 1800)));

   public static final RegistryObject<Potion> PIXELATION = POTIONS.register("pixelation",
      () -> new Potion("pixelation", new MobEffectInstance(PotionRegistry.pixelation.get(), 900)));
   public static final RegistryObject<Potion> LONG_PIXELATION = POTIONS.register("long_pixelation",
      () -> new Potion("pixelation", new MobEffectInstance(PotionRegistry.pixelation.get(), 1800)));

   public static final RegistryObject<Potion> HAZE = POTIONS.register("haze",
      () -> new Potion("haze", new MobEffectInstance(PotionRegistry.haze.get(), 900)));
   public static final RegistryObject<Potion> LONG_HAZE = POTIONS.register("long_haze",
      () -> new Potion("haze", new MobEffectInstance(PotionRegistry.haze.get(), 1800)));

   public static final RegistryObject<Potion> ABSORPTION = POTIONS.register("absorption",
      () -> new Potion("absorption", new MobEffectInstance(net.minecraft.world.effect.MobEffects.ABSORPTION, 1800)));
   public static final RegistryObject<Potion> BLINDNESS = POTIONS.register("blindness",
      () -> new Potion("blindness", new MobEffectInstance(net.minecraft.world.effect.MobEffects.BLINDNESS, 900)));
   public static final RegistryObject<Potion> CREEPERS_BANE = POTIONS.register("creepers_bane",
      () -> new Potion("creepers_bane", new MobEffectInstance(PotionRegistry.creepers_bane.get(), 1800)));
   public static final RegistryObject<Potion> FEAR = POTIONS.register("fear",
      () -> new Potion("fear", new MobEffectInstance(PotionRegistry.fear.get(), 900)));
   public static final RegistryObject<Potion> FLAMABILITY = POTIONS.register("flamability",
      () -> new Potion("flamability", new MobEffectInstance(PotionRegistry.flamability.get(), 900)));
   public static final RegistryObject<Potion> TOADS_TONGUE = POTIONS.register("toads_tongue",
      () -> new Potion("toads_tongue", new MobEffectInstance(PotionRegistry.toads_tongue.get(), 1800)));
   public static final RegistryObject<Potion> FROSTBITE = POTIONS.register("frostbite",
      () -> new Potion("frostbite", new MobEffectInstance(PotionRegistry.frostbite.get(), 900)));

   public static final RegistryObject<Potion> DOLPHINS_GRACE = POTIONS.register("dolphins_grace",
      () -> new Potion("dolphins_grace", new MobEffectInstance(net.minecraft.world.effect.MobEffects.DOLPHINS_GRACE, 900)));
   public static final RegistryObject<Potion> LONG_DOLPHINS_GRACE = POTIONS.register("long_dolphins_grace",
      () -> new Potion("dolphins_grace", new MobEffectInstance(net.minecraft.world.effect.MobEffects.DOLPHINS_GRACE, 1800)));

   public static final RegistryObject<Potion> PUFFER_TOXIN = POTIONS.register("puffer_toxin",
      () -> new Potion("puffer_toxin", new MobEffectInstance(PotionRegistry.puffer_toxin.get(), 900)));
   public static final RegistryObject<Potion> LONG_PUFFER_TOXIN = POTIONS.register("long_puffer_toxin",
      () -> new Potion("puffer_toxin", new MobEffectInstance(PotionRegistry.puffer_toxin.get(), 1800)));
   public static final RegistryObject<Potion> GLOWING = POTIONS.register("glowing",
      () -> new Potion("glowing", new MobEffectInstance(net.minecraft.world.effect.MobEffects.GLOWING, 1800)));
   public static final RegistryObject<Potion> HASTE = POTIONS.register("haste",
      () -> new Potion("haste", new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SPEED, 1800)));
   public static final RegistryObject<Potion> HEALTH_BOOST = POTIONS.register("health_boost",
      () -> new Potion("health_boost", new MobEffectInstance(net.minecraft.world.effect.MobEffects.HEALTH_BOOST, 1800)));
   public static final RegistryObject<Potion> HUNGER = POTIONS.register("hunger",
      () -> new Potion("hunger", new MobEffectInstance(net.minecraft.world.effect.MobEffects.HUNGER, 900)));
   public static final RegistryObject<Potion> INFATUATION = POTIONS.register("infatuation",
      () -> new Potion("infatuation", new MobEffectInstance(PotionRegistry.infatuation.get(), 1800)));
   public static final RegistryObject<Potion> LEVITATION = POTIONS.register("levitation",
      () -> new Potion("levitation", new MobEffectInstance(net.minecraft.world.effect.MobEffects.LEVITATION, 600)));
   public static final RegistryObject<Potion> MANA = POTIONS.register("mana",
      () -> new Potion("mana", new MobEffectInstance(PotionRegistry.mana.get(), 1, 0)));
   public static final RegistryObject<Potion> MANA_REGEN = POTIONS.register("mana_regen",
      () -> new Potion("mana_regen", new MobEffectInstance(PotionRegistry.mana_regen.get(), 1800)));
   public static final RegistryObject<Potion> SPELL_CHARGE = POTIONS.register("spell_charge",
      () -> new Potion("spell_charge", new MobEffectInstance(PotionRegistry.spell_charge.get(), 1, 0)));
   public static final RegistryObject<Potion> STRONG_SPELL_CHARGE = POTIONS.register("strong_spell_charge",
      () -> new Potion("spell_charge", new MobEffectInstance(PotionRegistry.spell_charge.get(), 1, 1)));
   public static final RegistryObject<Potion> SPELL_CHARGE_REGEN = POTIONS.register("spell_charge_regen",
      () -> new Potion("spell_charge_regen", new MobEffectInstance(PotionRegistry.spell_charge_regen.get(), 1800)));
   public static final RegistryObject<Potion> STRONG_SPELL_CHARGE_REGEN = POTIONS.register("strong_spell_charge_regen",
      () -> new Potion("spell_charge_regen", new MobEffectInstance(PotionRegistry.spell_charge_regen.get(), 900, 1)));
   public static final RegistryObject<Potion> MINING_FATIGUE = POTIONS.register("mining_fatigue",
      () -> new Potion("mining_fatigue", new MobEffectInstance(net.minecraft.world.effect.MobEffects.DIG_SLOWDOWN, 900)));
   public static final RegistryObject<Potion> NAUSEA = POTIONS.register("nausea",
      () -> new Potion("nausea", new MobEffectInstance(net.minecraft.world.effect.MobEffects.CONFUSION, 900)));
   public static final RegistryObject<Potion> POISON_THORNED = POTIONS.register("poison_thorned",
      () -> new Potion("poison_thorned", new MobEffectInstance(PotionRegistry.poison_thorned.get(), 1800)));
   public static final RegistryObject<Potion> REPELLANT = POTIONS.register("repellant",
      () -> new Potion("repellant", new MobEffectInstance(PotionRegistry.repellant.get(), 3600)));
   public static final RegistryObject<Potion> RESISTANCE = POTIONS.register("resistance",
      () -> new Potion("resistance", new MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE, 1800)));
   public static final RegistryObject<Potion> SATURATION = POTIONS.register("saturation",
      () -> new Potion("saturation", new MobEffectInstance(net.minecraft.world.effect.MobEffects.SATURATION, 1, 0)));
   public static final RegistryObject<Potion> SLOWFALL = POTIONS.register("slowfall",
      () -> new Potion("slowfall", new MobEffectInstance(PotionRegistry.slowfall.get(), 1800)));
   public static final RegistryObject<Potion> SPINED = POTIONS.register("spined",
      () -> new Potion("spined", new MobEffectInstance(PotionRegistry.spined.get(), 1800)));
   public static final RegistryObject<Potion> STUNNED = POTIONS.register("stunned",
      () -> new Potion("stunned", new MobEffectInstance(PotionRegistry.stunned.get(), 900)));
   public static final RegistryObject<Potion> UNDEAD = POTIONS.register("undead",
      () -> new Potion("undead", new MobEffectInstance(PotionRegistry.undead.get(), 1800)));
   public static final RegistryObject<Potion> VULNERABILITY = POTIONS.register("vulnerability",
      () -> new Potion("vulnerability", new MobEffectInstance(PotionRegistry.vulnerability.get(), 900)));
   public static final RegistryObject<Potion> WEAKLING = POTIONS.register("weakling",
      () -> new Potion("weakling", new MobEffectInstance(PotionRegistry.weakling.get(), 900)));

   public static void register(IEventBus bus) {
      POTIONS.register(bus);
   }
}
