package com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.items.ScrollForgeHelper;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ScrollForgeRecipeMaker {

   private static final int[] GROUPS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17};

   public static List<ScrollForgeRecipe> getRecipes() {
      List<ScrollForgeRecipe> out = new ArrayList<>();
      ItemStack lapis = new ItemStack(Items.LAPIS_LAZULI);
      ItemStack parchment = new ItemStack(ItemRegistry.spell_parchment.get());
      ItemStack seal = new ItemStack(ItemRegistry.scroll_seal.get());

      for (int group : GROUPS) {
         ItemStack cat = ScrollForgeHelper.catalystFor(group);
         if (cat.isEmpty()) continue;
         int max = ScrollForgeHelper.maxLevel(group);
         for (int lvl = 1; lvl <= max; lvl++) {
            Spell.EnumSpellModifier mod = Spell.EnumSpellModifier.fromIDs(group, lvl);
            if (mod == null) continue;
            ItemStack center = lvl == 1
               ? seal.copy()
               : ScrollForgeHelper.makeScroll(Spell.EnumSpellModifier.fromIDs(group, lvl - 1));
            out.add(build(cat, lapis, center, parchment, ScrollForgeHelper.makeScroll(mod)));
         }
      }

      for (int elem = 0; elem <= 15; elem++) {
         if (elem == 10) continue;
         Spell.EnumSpellModifier mod = Spell.EnumSpellModifier.fromIDs(16, elem + 1);
         if (mod == null) continue;
         out.add(build(ScrollForgeHelper.elementalDust(elem), lapis, seal.copy(), parchment,
            ScrollForgeHelper.makeScroll(mod)));
      }
      return out;
   }

   private static ScrollForgeRecipe build(ItemStack cat, ItemStack lapis, ItemStack center, ItemStack parchment, ItemStack output) {
      List<ItemStack> grid = Lists.newArrayList(
         cat.copy(), lapis.copy(), cat.copy(),
         lapis.copy(), center, lapis.copy(),
         cat.copy(), lapis.copy(), cat.copy());
      return new ScrollForgeRecipe(grid, parchment.copy(), output);
   }
}
