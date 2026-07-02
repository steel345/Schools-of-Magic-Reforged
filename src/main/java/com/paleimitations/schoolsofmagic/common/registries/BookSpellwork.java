package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageCraftingRitualRecipe;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandardTitled;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.items.ScrollForgeHelper;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public class BookSpellwork {

   private static final float TEXT_SCALE = 0.75F;

   private static ItemStack stack(RegistryObject<Item> item) {
      return new ItemStack(item.get());
   }

   private static ItemStack stack(RegistryObject<Item> item, int meta) {
      ItemStack s = new ItemStack(item.get());
      s.setDamageValue(meta);
      return s;
   }

   private static void scrollSide(java.util.List<PageElement> els, int group, String titleKey, boolean rightSide) {
      ItemStack catalyst = ScrollForgeHelper.catalystFor(group);
      ItemStack seal = stack(ItemRegistry.scroll_seal);
      ItemStack lapis = new ItemStack(Items.LAPIS_LAZULI);
      ItemStack output = ScrollForgeHelper.makeScroll(Spell.EnumSpellModifier.fromIDs(group, 1));
      int titleX = rightSide ? 184 : 72;
      int recipeX = rightSide ? 150 : 38;
      els.add(new PageElementStandardText(titleKey, titleX, 58, 99, 16, 0, true));
      els.add(new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
         catalyst, lapis, catalyst,
         lapis, seal, lapis,
         catalyst, lapis, catalyst
      }), output, recipeX, 90, 0));
   }

   private static void scrollSpread(String pageName, int leftGroup, String leftTitle, int rightGroup, String rightTitle) {
      java.util.List<PageElement> els = Lists.newArrayList();
      scrollSide(els, leftGroup, leftTitle, false);
      scrollSide(els, rightGroup, rightTitle, true);
      new BookPage(pageName, els).addToList(BookPageRegistry.SPELLWORK_BOOK);
   }

   private static void scrollSingle(String pageName, int group, String title) {
      java.util.List<PageElement> els = Lists.newArrayList();
      scrollSide(els, group, title, false);
      new BookPage(pageName, els).addToList(BookPageRegistry.SPELLWORK_BOOK);
   }

   private static void elementalSide(java.util.List<PageElement> els, int elementIndex, String titleKey, boolean rightSide) {
      ItemStack dust = ScrollForgeHelper.elementalDust(elementIndex);
      ItemStack seal = stack(ItemRegistry.scroll_seal);
      ItemStack lapis = new ItemStack(Items.LAPIS_LAZULI);
      ItemStack output = ScrollForgeHelper.makeScroll(Spell.EnumSpellModifier.fromIDs(16, elementIndex + 1));
      int titleX = rightSide ? 184 : 72;
      int recipeX = rightSide ? 150 : 38;
      els.add(new PageElementStandardText(titleKey, titleX, 58, 99, 16, 0, true));
      els.add(new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
         dust, lapis, dust,
         lapis, seal, lapis,
         dust, lapis, dust
      }), output, recipeX, 90, 0));
   }

   private static void elementalSpread(String pageName, int leftEl, String leftTitle, int rightEl, String rightTitle) {
      java.util.List<PageElement> els = Lists.newArrayList();
      elementalSide(els, leftEl, leftTitle, false);
      elementalSide(els, rightEl, rightTitle, true);
      new BookPage(pageName, els).addToList(BookPageRegistry.SPELLWORK_BOOK);
   }

   public static void init() {
      ItemStack e = ItemStack.EMPTY;

      new BookPageTableContent(null).addToList(BookPageRegistry.SPELLWORK_BOOK);

      BookPageChapter chapter1 = new BookPageChapter(null);
      chapter1.elements.add(new PageElementTitle("page.spellwork_ch_1.title", 72, 58, 99, 16, 0, true));
      chapter1.addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPage("spellwork_intro", Lists.newArrayList(new PageElement[]{
         new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmb_pg32_title.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementStandardText("page.spellwork_intro.title", 184, 58, 92, 14, 0, true),
         new PageElementParagraphs("spellwork_intro", TEXT_SCALE, 0, 0,
            new ParagraphBox(136, 72, 0, 92, 120))
      })).addToList(BookPageRegistry.SPELLWORK_BOOK);

      RecipeRitualCrafting spellForgeRitual = new RecipeRitualCrafting(
         new ItemStack(BlockRegistry.spell_forge.get()),
         150, 4, 0, Maps.newHashMap(), Maps.newHashMap(),
         "blockGlass",
         "blockGlass",
         new ItemStack(BlockRegistry.fence_steel.get()),
         stack(ItemRegistry.ingot, EnumMetal.STEEL.getIndex()),
         stack(ItemRegistry.crushed_plant, EnumPlantType.HYDRANGEA.getIndex()));
      new BookPageCraftingRitualRecipe(spellForgeRitual, "crafting_ritual_spell_forge",
            "page.spellwork_forge.title", "crafting_ritual_spell_forge")
         .addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPageStandardTitled("spellwork_notes").addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPageStandardTitled("spellwork_points").addToList(BookPageRegistry.SPELLWORK_BOOK);
      new BookPageStandardTitled("spellwork_points_2").addToList(BookPageRegistry.SPELLWORK_BOOK);

      BookPageChapter chapter2 = new BookPageChapter(null);
      chapter2.elements.add(new PageElementTitle("page.spellwork_ch_2.title", 72, 58, 99, 16, 0, true));
      chapter2.addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPageStandardTitled("spellwork_custom").addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPageStandardTitled("spellwork_shapes_1").addToList(BookPageRegistry.SPELLWORK_BOOK);
      new BookPageStandardTitled("spellwork_shapes_2").addToList(BookPageRegistry.SPELLWORK_BOOK);
      new BookPageStandardTitled("spellwork_shapes_3").addToList(BookPageRegistry.SPELLWORK_BOOK);

      ItemStack snowball = new ItemStack(Items.SNOWBALL);
      ItemStack bow = new ItemStack(Items.BOW);
      ItemStack catalyst = stack(ItemRegistry.crushed_plant, EnumPlantType.NIGHTBERRY.getIndex());
      new BookPage("spellwork_custom_example", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.spellwork_custom_example.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            snowball, bow, catalyst,
            ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
            ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY
         }), stack(ItemRegistry.spell_note), 159, 110, 0),
         new PageElementParagraphs("spellwork_custom_example", TEXT_SCALE, 0, 1,
            new ParagraphBox(23, 65, 0, 99, 170))
      })).addToList(BookPageRegistry.SPELLWORK_BOOK);

      BookPageChapter chapter3 = new BookPageChapter(null);
      chapter3.elements.add(new PageElementTitle("page.spellwork_ch_3.title", 72, 58, 99, 16, 0, true));
      chapter3.addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPageStandardTitled("spellwork_scroll_about").addToList(BookPageRegistry.SPELLWORK_BOOK);

      new BookPage("spellwork_scroll_intro", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.spellwork_scroll_intro.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            new ItemStack(Items.HONEYCOMB), new ItemStack(Items.LAPIS_LAZULI), e,
            e, e, e,
            e, e, e
         }), stack(ItemRegistry.scroll_seal, 0), 38, 90, 0),
         new PageElementStandardText("page.spellwork_scroll_parchment.title", 184, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            e, new ItemStack(Items.STICK), e,
            e, new ItemStack(Items.PAPER), e,
            e, new ItemStack(Items.STICK), e
         }), stack(ItemRegistry.spell_parchment), 150, 90, 0)
      })).addToList(BookPageRegistry.SPELLWORK_BOOK);

      scrollSpread("spellwork_scroll_cost_material", 0, "page.spellwork_scroll_cost.title", 1, "page.spellwork_scroll_material.title");
      scrollSpread("spellwork_scroll_power_duration", 2, "page.spellwork_scroll_power.title", 3, "page.spellwork_scroll_duration.title");
      scrollSpread("spellwork_scroll_aoe_strength", 4, "page.spellwork_scroll_aoe.title", 5, "page.spellwork_scroll_strength.title");
      scrollSpread("spellwork_scroll_aggression_articulation", 6, "page.spellwork_scroll_aggression.title", 7, "page.spellwork_scroll_articulation.title");
      scrollSpread("spellwork_scroll_insight_generosity", 8, "page.spellwork_scroll_insight.title", 9, "page.spellwork_scroll_generosity.title");
      scrollSpread("spellwork_scroll_creativity_deception", 10, "page.spellwork_scroll_creativity.title", 11, "page.spellwork_scroll_deception.title");
      scrollSpread("spellwork_scroll_pet_passive", 12, "page.spellwork_scroll_pet.title", 13, "page.spellwork_scroll_passive.title");
      scrollSingle("spellwork_scroll_additive", 14, "page.spellwork_scroll_additive.title");

      elementalSpread("spellwork_el_1", 0, "page.spellwork_el_pyromancy.title", 1, "page.spellwork_el_heliomancy.title");
      elementalSpread("spellwork_el_2", 2, "page.spellwork_el_aeromancy.title", 3, "page.spellwork_el_geomancy.title");
      elementalSpread("spellwork_el_3", 4, "page.spellwork_el_animancy.title", 5, "page.spellwork_el_electromancy.title");
      elementalSpread("spellwork_el_4", 6, "page.spellwork_el_hydromancy.title", 7, "page.spellwork_el_cryomancy.title");
      elementalSpread("spellwork_el_5", 8, "page.spellwork_el_hieromancy.title", 9, "page.spellwork_el_chaotics.title");
      elementalSpread("spellwork_el_6", 10, "page.spellwork_el_auramancy.title", 11, "page.spellwork_el_astromancy.title");
      elementalSpread("spellwork_el_7", 12, "page.spellwork_el_infernality.title", 13, "page.spellwork_el_spectromancy.title");
      elementalSpread("spellwork_el_8", 14, "page.spellwork_el_umbramancy.title", 15, "page.spellwork_el_necromancy.title");
   }
}
