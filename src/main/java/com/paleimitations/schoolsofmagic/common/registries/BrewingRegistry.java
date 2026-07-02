package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BrewingRegistry {
   private BrewingRegistry() {}

   public static void register() {
      Ingredient awk = base(Potions.AWKWARD);
      Ingredient redstone = Ingredient.of(Items.REDSTONE);
      Ingredient glow = Ingredient.of(Items.GLOWSTONE_DUST);
      Ingredient spiderEye = Ingredient.of(Items.FERMENTED_SPIDER_EYE);

      add(awk, Ingredient.of(Items.GOLDEN_APPLE),                             P.ABSORPTION);
      add(awk, ing(EnumIngredient.TENTACLE),                                  P.BLINDNESS);
      add(awk, plant(EnumPlantType.CATTAIL),                                  P.BLURRY_VISION);
      add(awk, plant(EnumPlantType.OLEANDER),                                 P.COLOR_BLIND);
      add(awk, blockItem(ItemRegistry.bi_mushroom_stalk.get()),               P.CONFUSION);
      add(awk, plant(EnumPlantType.HELIOMANCY),                               P.CREEPERS_BANE);
      add(awk, Ingredient.of(Items.CHORUS_FRUIT),                             P.DISLOCATION);
      add(awk, ing(EnumIngredient.PIG_TAIL),                                  P.FEAR);
      add(awk, blockItem(ItemRegistry.bi_plant_shrooms.get()),                P.HALLUCINATION);
      add(awk, Ingredient.of(ItemRegistry.seed_mushroom_white.get()),         P.HAZE);
      add(awk, ing(EnumIngredient.BEAR_HEART),                                P.HEALTH_BOOST);
      add(awk, magicPlant(EnumMagicType.NECROMANCY),                          P.HUNGER);
      add(awk, tree(EnumMagicWood.ASH),                                       P.LEVITATION);
      add(awk, plant(EnumPlantType.AURAMANCY),                                P.MANA);
      add(awk, Ingredient.of(ItemRegistry.crushed_horn_unicorn.get()),        P.MANA_REGEN);
      add(awk, dust(10),                                                      P.SPELL_CHARGE);
      add(awk, fairyDusts(),                                                  P.SPELL_CHARGE_REGEN);
      add(awk, Ingredient.of(Items.PRISMARINE_CRYSTALS),                      P.MINING_FATIGUE);
      add(awk, Ingredient.of(ItemRegistry.item_bladderwort.get()),            P.NAUSEA);
      add(awk, ing(EnumIngredient.BAT_WING),                                  P.OBSCURATION);
      add(awk, plant(EnumPlantType.MAYBELL),                               P.PARALYSIS);
      add(awk, Ingredient.of(ItemRegistry.item_algae.get()),                  P.PIXELATION);
      add(awk, plant(EnumPlantType.PYROMANCY),                                P.POISON_THORNED);
      add(awk, plant(EnumPlantType.FLYTRAP),                                  P.REPELLANT);
      add(awk, tree(EnumMagicWood.PINE),                                      P.RESISTANCE);
      add(awk, plant(EnumPlantType.WHEAT),                                    P.SATURATION);
      add(awk, ing(EnumIngredient.BIRD_HEART),                                P.SLOWFALL);
      add(awk, Ingredient.of(ItemRegistry.item_ocotillo_flowers.get()),       P.SNEEZING);
      add(awk, plant(EnumPlantType.ROSE),                                     P.SPINED);
      add(awk, Ingredient.of(ItemRegistry.item_graveroot_pulp.get()),         P.STENCH);
      add(awk, blockItem(ItemRegistry.bi_mushroom_white.get()),              P.VULNERABILITY);
      add(awk, blockItem(ItemRegistry.bi_mushroom_dark.get()),               P.WEAKLING);
      add(awk, Ingredient.of(ItemRegistry.item_ice_shard.get()),             P.FROSTBITE);
      add(awk, ing(EnumIngredient.TOAD_TONGUE),                               P.TOADS_TONGUE);

      add(awk, blockItem(ItemRegistry.bi_mushroom_grey.get()),                P.WITHER);

      add(awk, plant(EnumPlantType.NIGHTBERRY),                Potions.HARMING);
      add(awk, blockItem(ItemRegistry.bi_mushroom_pink.get()), Potions.WEAKNESS);
      add(awk, blockItem(ItemRegistry.bi_dynamic_web.get()),   Potions.SLOWNESS);

      add(awk, tree(EnumMagicWood.WILLOW),                     Potions.INVISIBILITY);

      add(awk, ing(EnumIngredient.DRAGON_HEART),               PotionTypeRegistry.CERTAIN_DEATH);

      add(awk, Ingredient.of(Items.RABBIT_FOOT),                  Potions.LEAPING);
      add(awk, Ingredient.of(Items.SALMON),                       Potions.WATER_BREATHING);
      add(awk, Ingredient.of(Items.GOLDEN_CARROT),                Potions.NIGHT_VISION);
      add(awk, Ingredient.of(Items.GLISTERING_MELON_SLICE),       Potions.HEALING);
      add(awk, Ingredient.of(Items.MAGMA_CREAM),                  Potions.FIRE_RESISTANCE);
      add(awk, Ingredient.of(Items.BLAZE_POWDER),                 Potions.STRENGTH);
      add(awk, Ingredient.of(Items.GHAST_TEAR),                   Potions.REGENERATION);
      add(awk, Ingredient.of(Items.SUGAR),                        Potions.SWIFTNESS);
      add(awk, Ingredient.of(Items.SPIDER_EYE),                   PotionTypeRegistry.SPIDER);
      add(awk, Ingredient.of(Items.LILY_OF_THE_VALLEY),           Potions.POISON);

      add(awk, bottle(EnumBottle.FIREBERRY),     P.FLAMABILITY);
      add(awk, bottle(EnumBottle.WORMWOOD),      P.DAZE);
      add(awk, bottle(EnumBottle.SUNFLOWER),     P.GLOWING);
      add(awk, bottle(EnumBottle.POPPY),         P.HASTE);
      add(awk, bottle(EnumBottle.ROSE),          P.INFATUATION);
      add(awk, bottle(EnumBottle.NIGHTBERRY),    P.SLEEP);
      add(awk, bottle(EnumBottle.GRAVEROOT),     P.UNDEAD);

      add(awk, bottle(EnumBottle.STORMTHISTLE),  P.STUNNED);
      add(awk, bottle(EnumBottle.SNOWBELL),      P.FROSTBITE);

      add(awk, ing(EnumIngredient.FISH_TAIL),    PotionTypeRegistry.DOLPHINS_GRACE);
      add(base(PotionTypeRegistry.DOLPHINS_GRACE), redstone, PotionTypeRegistry.LONG_DOLPHINS_GRACE);

      add(awk, Ingredient.of(Items.PUFFERFISH), PotionTypeRegistry.PUFFER_TOXIN);
      add(base(PotionTypeRegistry.PUFFER_TOXIN), redstone, PotionTypeRegistry.LONG_PUFFER_TOXIN);

      add(base(P.SATURATION),  spiderEye, P.HUNGER);
      add(base(P.HEALTH_BOOST), spiderEye, P.VULNERABILITY);

      add(base(P.BLURRY_VISION),  redstone, P.LONG_BLURRY_VISION);
      add(base(P.COLOR_BLIND),    redstone, P.LONG_COLOR_BLIND);
      add(base(P.CONFUSION),      redstone, P.LONG_CONFUSION);
      add(base(P.DISLOCATION),    redstone, P.LONG_DISLOCATION);
      add(base(P.HALLUCINATION),  redstone, P.LONG_HALLUCINATION);
      add(base(P.HAZE),           redstone, P.LONG_HAZE);
      add(base(P.OBSCURATION),    redstone, P.LONG_OBSCURATION);
      add(base(P.PARALYSIS),      redstone, P.LONG_PARALYSIS);
      add(base(P.PIXELATION),     redstone, P.LONG_PIXELATION);
      add(base(P.SNEEZING),       redstone, P.LONG_SNEEZING);
      add(base(P.STENCH),         redstone, P.LONG_STENCH);
      add(base(P.DAZE),           redstone, P.LONG_DAZE);
      add(base(P.SLEEP),          redstone, P.LONG_SLEEP);

      add(base(P.SNEEZING),       glow, P.STRONG_SNEEZING);
      add(base(P.DISLOCATION),    glow, P.STRONG_DISLOCATION);
      add(base(P.SPELL_CHARGE),       glow, P.STRONG_SPELL_CHARGE);
      add(base(P.SPELL_CHARGE_REGEN), glow, P.STRONG_SPELL_CHARGE_REGEN);
   }

   private static final class P {
      static final RegistryObject<Potion> ABSORPTION=PotionTypeRegistry.ABSORPTION, BLINDNESS=PotionTypeRegistry.BLINDNESS,
         BLURRY_VISION=PotionTypeRegistry.BLURRY_VISION, LONG_BLURRY_VISION=PotionTypeRegistry.LONG_BLURRY_VISION,
         COLOR_BLIND=PotionTypeRegistry.COLOR_BLIND, LONG_COLOR_BLIND=PotionTypeRegistry.LONG_COLOR_BLIND,
         CONFUSION=PotionTypeRegistry.CONFUSION, LONG_CONFUSION=PotionTypeRegistry.LONG_CONFUSION,
         CREEPERS_BANE=PotionTypeRegistry.CREEPERS_BANE, DISLOCATION=PotionTypeRegistry.DISLOCATION,
         LONG_DISLOCATION=PotionTypeRegistry.LONG_DISLOCATION, STRONG_DISLOCATION=PotionTypeRegistry.STRONG_DISLOCATION,
         FEAR=PotionTypeRegistry.FEAR, HALLUCINATION=PotionTypeRegistry.HALLUCINATION, LONG_HALLUCINATION=PotionTypeRegistry.LONG_HALLUCINATION,
         HAZE=PotionTypeRegistry.HAZE, LONG_HAZE=PotionTypeRegistry.LONG_HAZE,
         HEALTH_BOOST=PotionTypeRegistry.HEALTH_BOOST, HUNGER=PotionTypeRegistry.HUNGER,
         LEVITATION=PotionTypeRegistry.LEVITATION, MANA=PotionTypeRegistry.MANA, MANA_REGEN=PotionTypeRegistry.MANA_REGEN,
         SPELL_CHARGE=PotionTypeRegistry.SPELL_CHARGE, SPELL_CHARGE_REGEN=PotionTypeRegistry.SPELL_CHARGE_REGEN,
         STRONG_SPELL_CHARGE=PotionTypeRegistry.STRONG_SPELL_CHARGE, STRONG_SPELL_CHARGE_REGEN=PotionTypeRegistry.STRONG_SPELL_CHARGE_REGEN,
         MINING_FATIGUE=PotionTypeRegistry.MINING_FATIGUE, NAUSEA=PotionTypeRegistry.NAUSEA,
         OBSCURATION=PotionTypeRegistry.OBSCURATION, LONG_OBSCURATION=PotionTypeRegistry.LONG_OBSCURATION,
         PARALYSIS=PotionTypeRegistry.PARALYSIS, LONG_PARALYSIS=PotionTypeRegistry.LONG_PARALYSIS,
         PIXELATION=PotionTypeRegistry.PIXELATION, LONG_PIXELATION=PotionTypeRegistry.LONG_PIXELATION,
         POISON_THORNED=PotionTypeRegistry.POISON_THORNED, REPELLANT=PotionTypeRegistry.REPELLANT,
         RESISTANCE=PotionTypeRegistry.RESISTANCE, SATURATION=PotionTypeRegistry.SATURATION,
         SLOWFALL=PotionTypeRegistry.SLOWFALL, SNEEZING=PotionTypeRegistry.SNEEZING,
         LONG_SNEEZING=PotionTypeRegistry.LONG_SNEEZING, STRONG_SNEEZING=PotionTypeRegistry.STRONG_SNEEZING,
         SPINED=PotionTypeRegistry.SPINED, STENCH=PotionTypeRegistry.STENCH, LONG_STENCH=PotionTypeRegistry.LONG_STENCH,
         VULNERABILITY=PotionTypeRegistry.VULNERABILITY, WEAKLING=PotionTypeRegistry.WEAKLING,
         FROSTBITE=PotionTypeRegistry.FROSTBITE, TOADS_TONGUE=PotionTypeRegistry.TOADS_TONGUE,
         FLAMABILITY=PotionTypeRegistry.FLAMABILITY, DAZE=PotionTypeRegistry.DAZE, LONG_DAZE=PotionTypeRegistry.LONG_DAZE,
         GLOWING=PotionTypeRegistry.GLOWING, HASTE=PotionTypeRegistry.HASTE, INFATUATION=PotionTypeRegistry.INFATUATION,
         SLEEP=PotionTypeRegistry.SLEEP, LONG_SLEEP=PotionTypeRegistry.LONG_SLEEP, STUNNED=PotionTypeRegistry.STUNNED,
         UNDEAD=PotionTypeRegistry.UNDEAD, WITHER=PotionTypeRegistry.WITHER;
   }

   private static void add(Ingredient input, Ingredient ingredient, RegistryObject<Potion> out) {
      BrewingRecipeRegistry.addRecipe(input, ingredient, PotionUtils.setPotion(new ItemStack(Items.POTION), out.get()));
   }

   private static void add(Ingredient input, Ingredient ingredient, Potion out) {
      BrewingRecipeRegistry.addRecipe(input, ingredient, PotionUtils.setPotion(new ItemStack(Items.POTION), out));
   }

   private static Ingredient base(Potion p) {
      CompoundTag nbt = new CompoundTag();
      nbt.putString("Potion", ForgeRegistries.POTIONS.getKey(p).toString());
      return PartialNBTIngredient.of(nbt, Items.POTION);
   }
   private static Ingredient base(RegistryObject<Potion> p) { return base(p.get()); }

   private static Ingredient variant(Item item, int dmg) {
      return new com.paleimitations.schoolsofmagic.common.crafting.DamageVariantIngredient(item, dmg);
   }
   private static Ingredient dust(int idx)          { return variant(ItemRegistry.gem_dust.get(), idx); }
   private static Ingredient fairyDusts() {
      return Ingredient.of(ItemRegistry.FAIRY_DUSTS.stream()
         .map(RegistryObject::get)
         .toArray(net.minecraft.world.level.ItemLike[]::new));
   }
   private static Ingredient plant(EnumPlantType t) { return variant(ItemRegistry.crushed_plant.get(), t.getIndex()); }
   private static Ingredient ing(EnumIngredient i)  { return variant(ItemRegistry.ingredient.get(), i.getIndex()); }
   private static Ingredient misc(EnumMisc m)       { return variant(ItemRegistry.item.get(), m.getIndex()); }
   private static Ingredient tree(EnumMagicWood w)  { return variant(ItemRegistry.tree_item.get(), w.getIndex()); }

   private static Ingredient bottle(EnumBottle b) {
      return new com.paleimitations.schoolsofmagic.common.crafting.DamageVariantIngredient(
         ItemRegistry.bottle.get(), b.getIndex());
   }
   private static Ingredient blockItem(Item item)   { return Ingredient.of(item); }
   private static Ingredient magicPlant(EnumMagicType t) {
      CompoundTag bs = new CompoundTag(); bs.putString("type", t.getSerializedName());
      CompoundTag nbt = new CompoundTag(); nbt.put("BlockStateTag", bs);
      return PartialNBTIngredient.of(nbt, ItemRegistry.bi_magic_plant.get());
   }
}
