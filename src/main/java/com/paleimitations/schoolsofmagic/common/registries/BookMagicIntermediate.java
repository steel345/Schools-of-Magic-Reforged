package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageCraftingRitualRecipe;
import com.paleimitations.schoolsofmagic.common.books.BookPageMushroomDescription;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandardTitled;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementCatalystBasinRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementMortarRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.PageElementItemStack;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellEnergize;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFirering;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellInvisibility;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellRaiseZombie;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellShulkerBullet;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellThornRing;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class BookMagicIntermediate {
   private static ItemStack stack(net.minecraft.world.item.Item item, int meta) {
      ItemStack s = new ItemStack(item);
      s.setDamageValue(meta);
      return s;
   }

   private static ItemStack exampleWand() {
      ItemStack s = new ItemStack(ItemRegistry.wand_advanced.get());
      com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData wd =
         new com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandData();
      wd.setCoreType(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumCoreType.OAK);
      wd.setHandleType(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.GOLD);
      wd.setGemType(com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType.DIAMOND);
      s.getOrCreateTag().put("wand_data", wd.serializeNBT());
      return s;
   }

   public static void init() {
      new BookPageTableContent(null).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageChapter(null).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPage(
            "bmi_title",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmi_pg0_title.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
                  new PageElementParagraphs(
                     "bmi_title", 0.75F, 0, 1,
                     new ParagraphBox(23, 95, 0, 99, 95),
                     new ParagraphBox(134, 50, 0, 99, 140),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      ItemStack grimoireNugget = stack(ItemRegistry.nugget.get(), 4);
      ItemStack grimoirePaper = new ItemStack(Items.PAPER);
      ItemStack grimoireOut = new ItemStack(ItemRegistry.spellbook.get());
      grimoireOut.getOrCreateTag().putInt("BookColor", net.minecraft.world.item.DyeColor.BLACK.getId());
      grimoireOut.getOrCreateTag().putInt("BookLinks", 6);
      new BookPage(
            "bmi_grimoire",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmi_grimoire.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
                     grimoireNugget, grimoirePaper, grimoireNugget,
                     new ItemStack(Items.LEATHER), grimoirePaper, new ItemStack(Items.BLACK_DYE),
                     grimoireNugget, grimoirePaper, grimoireNugget
                  }), grimoireOut, 159, 110, 0),
                  new PageElementParagraphs(
                     "bmi_grimoire", 0.75F, 0, 0,
                     new ParagraphBox(23, 76, 0, 99, 110)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage(
            "bmi_grimoiredecor",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmi_grimoiredecor.title", 72, 58, 99, 16, 0, true),
                  new PageElementParagraphs(
                     "bmi_grimoiredecor", 0.7F, 0, 1,
                     new ParagraphBox(23, 76, 0, 99, 114),
                     new ParagraphBox(134, 50, 0, 99, 140),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      if (MortarRecipeRegistry.UNICORN_HORN != null) {
         new BookPageStandardTitled("bmi_page1")
            .addElement(new PageElementMortarRecipe(MortarRecipeRegistry.UNICORN_HORN, 139, 126))
            .addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      } else {
         new BookPageStandardTitled("bmi_page1").addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      }

      new BookPage(
            "bmi_page2",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmi_page2.title", 72, 58, 99, 16, 0, true),
                  new PageElementParagraphs(
                     "bmi_page2", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 40),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmb_dryad.png"), 0, 0, 0, 0, 255, 255, 1.0F, true)
               }
            )
         )
         .addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPageStandardTitled("bmi_page3").addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageStandardTitled("bmi_phoenix").addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      ItemStack whistleNugget = new ItemStack(ItemRegistry.nugget.get());
      whistleNugget.setDamageValue(EnumMetal.BRASS.getIndex());
      ItemStack whistleIngot = new ItemStack(ItemRegistry.ingot.get());
      whistleIngot.setDamageValue(EnumMetal.BRASS.getIndex());
      ItemStack whistleEmpty = ItemStack.EMPTY;
      new BookPage("bmi_whistle", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_whistle.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            whistleNugget, new ItemStack(Items.REDSTONE), whistleNugget,
            whistleEmpty, whistleNugget, whistleEmpty,
            whistleEmpty, whistleIngot, whistleEmpty
         }), new ItemStack(ItemRegistry.brass_whistle.get()), 44, 110),
         new PageElementParagraphs("bmi_whistle", 0.75F, 0, 0,
            new ParagraphBox(134, 55, 0, 99, 140))
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageMushroomDescription(new ItemStack(BlockRegistry.mushroom_crop_dark.get())).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageMushroomDescription(new ItemStack(BlockRegistry.mushroom_crop_pink.get())).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageMushroomDescription(new ItemStack(BlockRegistry.mushroom_crop_white.get())).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageMushroomDescription(new ItemStack(BlockRegistry.mushroom_crop_grey.get())).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageStandardTitled("bmi_page4").addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageChapter(null).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest pyroDustMortar =
         RecipeRegistry.getMortarRecipe(stack(ItemRegistry.gem_dust.get(), EnumMagicType.PYROMANCY.getIndex()));
      java.util.List<PageElement> page5Els = Lists.newArrayList();
      page5Els.add(new PageElementStandardText("page.bmi_page5.title", 72, 58, 99, 16, 0, true));
      page5Els.add(new PageElementParagraphs(
         "bmi_page5", 0.75F, 0, 2,
         new ParagraphBox(23, 65, 0, 99, 45),
         new ParagraphBox(134, 50, 0, 99, 60),
         new ParagraphBox(23, 50, 1, 99, 60),
         new ParagraphBox(134, 50, 1, 99, 140),
         new ParagraphBox(23, 50, 2, 99, 140),
         new ParagraphBox(134, 50, 2, 99, 140)
      ));
      page5Els.add(new PageElementCraftingRecipe(
         Lists.newArrayList(new ItemStack[]{
            new ItemStack(Items.PAPER),
            new ItemStack(Blocks.SAND),
            ItemStack.EMPTY,
            new net.minecraft.world.item.ItemStack(ItemRegistry.item_diamond_dust.get())
         }),
         new ItemStack(ItemRegistry.sandpaper.get()),
         42, 110
      ));
      page5Els.add(new PageElementCraftingRecipe(
         Lists.newArrayList(new ItemStack[]{
            ItemStack.EMPTY,
            stack(ItemRegistry.gem_chunk.get(), EnumMagicType.PYROMANCY.getIndex()),
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            new ItemStack(ItemRegistry.sandpaper.get()),
            ItemStack.EMPTY
         }),
         new ItemStack(BlockRegistry.gem_pyromancy.get()),
         159, 110
      ));
      if (pyroDustMortar != null) {
         page5Els.add(new PageElementMortarRecipe(pyroDustMortar, 29, 126, 1));
      }
      new BookPage("bmi_page5", page5Els).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      RecipeRitualCrafting catBasinRitual = RecipeRegistry.getRitualRecipe(new ItemStack(BlockRegistry.catalyst_basin.get()));
      if (catBasinRitual != null) {
         new BookPageCraftingRitualRecipe(catBasinRitual).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      }

      java.util.List<PageElement> page6Els = Lists.newArrayList();
      page6Els.add(new PageElementStandardText("page.bmi_page6.title", 72, 58, 99, 16, 0, true));
      page6Els.add(new PageElementParagraphs(
         "bmi_page6", 0.75F, 0, 4,
         new ParagraphBox(23, 65, 0, 99, 125),
         new ParagraphBox(134, 50, 0, 99, 20),
         new ParagraphBox(23, 50, 1, 99, 20),
         new ParagraphBox(134, 50, 1, 99, 20),
         new ParagraphBox(23, 50, 2, 99, 20),
         new ParagraphBox(134, 50, 2, 99, 20),
         new ParagraphBox(23, 50, 3, 99, 20),
         new ParagraphBox(134, 50, 3, 99, 140),
         new ParagraphBox(23, 50, 4, 99, 140),
         new ParagraphBox(134, 50, 4, 99, 140)
      ));
      int[][] basinSlots = {{132, 74, 0}, {21, 74, 1}, {132, 74, 1}, {21, 74, 2}, {132, 74, 2}, {21, 74, 3}};
      EnumMetal[] basinMetals = {EnumMetal.COPPER, EnumMetal.SILVER, EnumMetal.BRASS, EnumMetal.BRONZE, EnumMetal.STEEL, EnumMetal.TENEBRIUM};
      for (int i = 0; i < basinMetals.length; i++) {
         RecipeCatalystBasin r = RecipeRegistry.getCatalystRecipe(stack(ItemRegistry.ingot.get(), basinMetals[i].getIndex()));
         if (r != null) {
            page6Els.add(new PageElementCatalystBasinRecipe(r, basinSlots[i][0], basinSlots[i][1], basinSlots[i][2]));
         }
      }
      new BookPage("bmi_page6", page6Els).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_supercharged", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_supercharged.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
               new ItemStack(ItemRegistry.item_diamond_dust.get()), new ItemStack(Items.LAPIS_LAZULI), new ItemStack(ItemRegistry.item_diamond_dust.get()),
               new ItemStack(Items.LAPIS_LAZULI), new ItemStack(Items.DIAMOND), new ItemStack(Items.LAPIS_LAZULI),
               new ItemStack(ItemRegistry.item_diamond_dust.get()), new ItemStack(Items.LAPIS_LAZULI), new ItemStack(ItemRegistry.item_diamond_dust.get())
            }), new ItemStack(ItemRegistry.magic_diamond.get()), 159, 110, 0),
         new PageElementParagraphs("bmi_supercharged", 0.75F, 0, 0,
            new ParagraphBox(23, 76, 0, 99, 100))
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_metalarmor", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_metalarmor.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("bmi_metalarmor", 0.75F, 0, 2,
            new ParagraphBox(23, 65, 0, 99, 125),
            new ParagraphBox(134, 50, 0, 99, 130),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140))
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      java.util.List<PageElement> shardEls = Lists.newArrayList();
      shardEls.add(new PageElementStandardText("page.bmi_obsidianshards.title", 72, 58, 99, 16, 0, true));
      if (MortarRecipeRegistry.OBSIDIAN != null) {
         shardEls.add(new PageElementMortarRecipe(MortarRecipeRegistry.OBSIDIAN, 139, 100));
      }
      shardEls.add(new PageElementParagraphs("bmi_obsidianshards", 0.75F, 0, 0,
         new ParagraphBox(23, 76, 0, 99, 100)));
      new BookPage("bmi_obsidianshards", shardEls).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      ItemStack shard = new ItemStack(ItemRegistry.item_obsidian_shard.get());
      ItemStack tenebrium = stack(ItemRegistry.ingot.get(), EnumMetal.TENEBRIUM.getIndex());
      ItemStack E = ItemStack.EMPTY;

      new BookPage("bmi_obsidianarmor", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_obsidianarmor.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
               E, shard.copy(), E,
               shard.copy(), tenebrium.copy(), shard.copy(),
               tenebrium.copy(), E, tenebrium.copy()
            }), new ItemStack(ItemRegistry.helmet_obsidian.get()), 30, 86, 0),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
               tenebrium.copy(), E, tenebrium.copy(),
               tenebrium.copy(), tenebrium.copy(), tenebrium.copy(),
               shard.copy(), tenebrium.copy(), shard.copy()
            }), new ItemStack(ItemRegistry.chestplate_obsidian.get()), 159, 86, 0)
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_obsidianarmor2", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_obsidianarmor.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
               tenebrium.copy(), tenebrium.copy(), tenebrium.copy(),
               shard.copy(), E, shard.copy(),
               tenebrium.copy(), E, tenebrium.copy()
            }), new ItemStack(ItemRegistry.leggings_obsidian.get()), 30, 86, 0),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
               shard.copy(), E, shard.copy(),
               tenebrium.copy(), E, tenebrium.copy(),
               tenebrium.copy(), E, tenebrium.copy()
            }), new ItemStack(ItemRegistry.boots_obsidian.get()), 159, 86, 0)
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      RecipeRitualCrafting divRitual = RecipeRegistry.getRitualRecipe(new ItemStack(BlockRegistry.divination_crystal.get()));
      if (divRitual != null) {
         new BookPageCraftingRitualRecipe(divRitual).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      }

      BookPageChapter wandChapter = new BookPageChapter(null);
      wandChapter.elements.add(new PageElementTitle("page.bmi_wand_chapter.title", 72, 58, 99, 16, 0, true));
      wandChapter.addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      RecipeRitualCrafting wandRitual = RecipeRegistry.getRitualRecipe(exampleWand());
      java.util.List<PageElement> wandEls = Lists.newArrayList();
      wandEls.add(new PageElementStandardText("page.bmi_wandmaking.title", 72, 58, 99, 16, 0, true));
      if (wandRitual != null) {
         wandEls.add(new com.paleimitations.schoolsofmagic.common.books.PageElementRitualRecipe(wandRitual, 132, 47, 0));
      }
      wandEls.add(new PageElementParagraphs("bmi_wandmaking", 0.75F, 0, 0,
         new ParagraphBox(23, 76, 0, 99, 104)));
      new BookPage("bmi_wandmaking", wandEls).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_elemental", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_elemental.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("bmi_elemental", 0.75F, 0, 2,
            new ParagraphBox(23, 65, 0, 99, 125),
            new ParagraphBox(134, 50, 0, 99, 60),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140))
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_metalperks", Lists.newArrayList(new PageElement[]{
         new PageElementImage(new net.minecraft.resources.ResourceLocation("som", "textures/gui/books/images/bmb_frame.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementStandardText("page.bmi_metalperks.title", 72, 58, 99, 16, 0, true),
         new PageElementItemStack(stack(ItemRegistry.ingot.get(), EnumMetal.COPPER.getIndex()), 26, 92),
         new PageElementStandardText("page.bmi_metalperks.copper", 44, 94, 70, 10, 0x202020, false),
         new PageElementItemStack(stack(ItemRegistry.ingot.get(), EnumMetal.BRONZE.getIndex()), 26, 110),
         new PageElementStandardText("page.bmi_metalperks.bronze", 44, 112, 70, 10, 0x202020, false),
         new PageElementItemStack(stack(ItemRegistry.ingot.get(), EnumMetal.BRASS.getIndex()), 26, 128),
         new PageElementStandardText("page.bmi_metalperks.brass", 44, 130, 70, 10, 0x202020, false),
         new PageElementItemStack(new ItemStack(Items.GOLD_INGOT), 26, 146),
         new PageElementStandardText("page.bmi_metalperks.gold", 44, 148, 70, 10, 0x202020, false),
         new PageElementStandardText("page.bmi_metalperks.empty", 44, 166, 70, 10, 0x202020, false),
         new PageElementItemStack(stack(ItemRegistry.ingot.get(), EnumMetal.SILVER.getIndex()), 140, 92),
         new PageElementStandardText("page.bmi_metalperks.silver", 158, 94, 70, 10, 0x202020, false),
         new PageElementItemStack(new ItemStack(Items.IRON_INGOT), 140, 110),
         new PageElementStandardText("page.bmi_metalperks.iron", 158, 112, 70, 10, 0x202020, false),
         new PageElementItemStack(stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()), 140, 128),
         new PageElementStandardText("page.bmi_metalperks.steel", 158, 130, 70, 10, 0x202020, false),
         new PageElementItemStack(stack(ItemRegistry.ingot.get(), EnumMetal.TENEBRIUM.getIndex()), 140, 146),
         new PageElementStandardText("page.bmi_metalperks.tenebrium", 158, 148, 70, 10, 0x202020, false),
         new PageElementStandardText("page.bmi_metalperks.empty", 158, 166, 70, 10, 0x202020, false)
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_personalities", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_personalities.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("bmi_personalities", 0.75F, 0, 2,
            new ParagraphBox(23, 65, 0, 99, 125),
            new ParagraphBox(134, 50, 0, 99, 60),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140))
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPage("bmi_spellring", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmi_spellring.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
               ItemStack.EMPTY, new ItemStack(Items.DIAMOND), ItemStack.EMPTY,
               new ItemStack(Items.GOLD_INGOT), stack(ItemRegistry.gem_dust.get(), 10), new ItemStack(Items.GOLD_INGOT),
               ItemStack.EMPTY, new ItemStack(Items.GOLD_INGOT), ItemStack.EMPTY
            }), new ItemStack(ItemRegistry.apprentice_ring.get()), 159, 110, 0),
         new PageElementParagraphs("bmi_spellring", 0.75F, 0, 2,
            new ParagraphBox(23, 65, 0, 99, 125),
            new ParagraphBox(134, 50, 0, 99, 60),
            new ParagraphBox(23, 50, 1, 99, 140),
            new ParagraphBox(134, 50, 1, 99, 140),
            new ParagraphBox(23, 50, 2, 99, 140),
            new ParagraphBox(134, 50, 2, 99, 140))
      })).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      ItemStack exampleRing = new ItemStack(ItemRegistry.apprentice_ring.get());
      com.paleimitations.schoolsofmagic.common.items.RingItemHelper.setData(exampleRing,
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumHandleType.SILVER,
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData.EnumGemType.RUBY);
      exampleRing.setHoverName(net.minecraft.network.chat.Component.literal("Magic Ring").withStyle(s -> s.withItalic(false)));
      RecipeRitualCrafting ringRitual = RecipeRegistry.getRitualRecipe(exampleRing);
      java.util.List<PageElement> ringEls = Lists.newArrayList();
      ringEls.add(new PageElementStandardText("page.bmi_advring.title", 72, 58, 99, 16, 0, true));
      if (ringRitual != null) {
         ringEls.add(new com.paleimitations.schoolsofmagic.common.books.PageElementRitualRecipe(ringRitual, 132, 47, 0));
      }
      ringEls.add(new PageElementParagraphs("bmi_advring", 0.75F, 0, 0,
         new ParagraphBox(23, 76, 0, 99, 114)));
      new BookPage("bmi_advring", ringEls).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);

      new BookPageChapter(null).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new SpellThornRing()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new SpellEnergize()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new SpellFirering()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new SpellRaiseZombie()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new SpellInvisibility()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new SpellShulkerBullet()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellHealing()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellSummonBee()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellRumor()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellCounterspell()).addToList(BookPageRegistry.INTERMEDIATE_MAGIC_BOOK);
   }
}
