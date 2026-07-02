package com.paleimitations.schoolsofmagic.common.spells;

import com.paleimitations.schoolsofmagic.common.potions.BrewResult;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandler;

public class SpellCraftingHelper {

   private static List<Recipe> RECIPES = null;

   private static final class Recipe {
      final EnumSpellShape shape;
      final Ingredient[] ingredients;
      Recipe(EnumSpellShape shape, Ingredient... ingredients) {
         this.shape = shape;
         this.ingredients = ingredients;
      }
   }

   private static void ensureRecipes() {
      if (RECIPES != null) return;
      List<Recipe> list = new ArrayList<>();
      list.add(new Recipe(EnumSpellShape.PROJECTILE, Ingredient.of(Items.SNOWBALL), Ingredient.of(Items.BOW)));
      list.add(new Recipe(EnumSpellShape.BOLT, Ingredient.of(Items.ARROW), Ingredient.of(ItemRegistry.item_ice_shard.get())));
      list.add(new Recipe(EnumSpellShape.TOUCH, Ingredient.of(ItemTags.WOOL), Ingredient.of(Items.GLASS)));
      list.add(new Recipe(EnumSpellShape.WALL, Ingredient.of(Items.BRICKS), Ingredient.of(ItemRegistry.bi_gem_geomancy.get())));
      list.add(new Recipe(EnumSpellShape.CHAIN, Ingredient.of(Items.CHAIN), Ingredient.of(ItemRegistry.bi_gem_animancy.get())));
      list.add(new Recipe(EnumSpellShape.WAVE, Ingredient.of(Items.ARMOR_STAND), Ingredient.of(ItemRegistry.bi_gem_hydromancy.get())));
      list.add(new Recipe(EnumSpellShape.BEAM, Ingredient.of(ItemRegistry.shard_netherstar.get()), Ingredient.of(Items.CHAIN)));
      list.add(new Recipe(EnumSpellShape.RUNE, Ingredient.of(Items.ENCHANTED_BOOK), Ingredient.of(BlockRegistry.leaves_palm.get().asItem())));
      list.add(new Recipe(EnumSpellShape.STARFALL, Ingredient.of(ItemRegistry.shard_netherstar.get()), Ingredient.of(ItemRegistry.bi_gem_astromancy.get()), Ingredient.of(ItemRegistry.bi_gem_spectromancy.get()), Ingredient.of(Items.CAMPFIRE)));
      list.add(new Recipe(EnumSpellShape.PLASMA, Ingredient.of(Items.LAPIS_LAZULI), Ingredient.of(Items.FIRE_CHARGE), Ingredient.of(ItemRegistry.bi_gem_electromancy.get())));
      list.add(new Recipe(EnumSpellShape.FOCUS, Ingredient.of(Items.LAPIS_BLOCK)));
      list.add(new Recipe(EnumSpellShape.SELF, Ingredient.of(Items.ARMOR_STAND)));
      list.sort((a, b) -> b.ingredients.length - a.ingredients.length);
      RECIPES = list;
   }

   public static SpellCustom parse(IItemHandler handler) {
      try {
         if (!com.paleimitations.schoolsofmagic.common.compat.SOMConfig.enable_custom_spell_making.get()) return null;
      } catch (IllegalStateException ignored) {
      }
      List<ItemStack> items = new ArrayList<>();
      for (int i = 0; i < handler.getSlots(); i++) {
         ItemStack s = handler.getStackInSlot(i);
         if (!s.isEmpty() && !s.is(Items.PAPER)) items.add(s);
      }
      if (items.isEmpty()) return null;
      ensureRecipes();

      Recipe match = null;
      for (Recipe r : RECIPES) {
         if (matchesPrefix(items, r)) { match = r; break; }
      }
      if (match == null) return null;

      int consumed = match.ingredients.length;
      if (items.size() <= consumed) return null;

      int compIdx = -1;
      MobEffectInstance eff = null;
      for (int i = consumed; i < items.size(); i++) {
         MobEffectInstance e = brewEffect(items.get(i));
         if (e != null) { eff = e; compIdx = i; break; }
      }
      if (eff == null) return null;

      boolean modDuration = false, modMagnitude = false, modSpeed = false, modRecharge = false, hasTarget = false, hasRose = false;
      for (int i = consumed; i < items.size(); i++) {
         if (i == compIdx) continue;
         ItemStack s = items.get(i);
         if (s.is(Items.CLOCK)) modDuration = true;
         else if (isPutridite(s)) modMagnitude = true;
         else if (s.is(Items.COMPARATOR)) modSpeed = true;
         else if (s.is(Items.SUGAR)) modRecharge = true;
         else if (s.is(Items.TARGET)) hasTarget = true;
         else if (isRoseExtract(s)) hasRose = true;
      }
      boolean modHoming = hasTarget && hasRose;

      return SpellCustom.build(match.shape, eff.getEffect(), eff.getDuration(), eff.getAmplifier(),
         modDuration, modMagnitude, modSpeed, modHoming, modRecharge);
   }

   private static boolean matchesPrefix(List<ItemStack> items, Recipe r) {
      if (items.size() < r.ingredients.length) return false;
      for (int i = 0; i < r.ingredients.length; i++) {
         if (!r.ingredients[i].test(items.get(i))) return false;
      }
      return true;
   }

   private static boolean isPutridite(ItemStack s) {
      return s.is(ItemRegistry.bi_gem_infernality.get());
   }

   private static boolean isRoseExtract(ItemStack s) {
      return s.getItem() == ItemRegistry.crushed_plant.get()
         && s.getDamageValue() == com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.ROSE.ordinal();
   }

   public static MobEffectInstance brewEffect(ItemStack component) {
      ItemStack awkward = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
      ItemStack brewed = BrewResult.brewOutput(awkward, component);
      if (brewed.isEmpty()) return null;
      List<MobEffectInstance> effects = PotionUtils.getMobEffects(brewed);
      return effects.isEmpty() ? null : effects.get(0);
   }
}
