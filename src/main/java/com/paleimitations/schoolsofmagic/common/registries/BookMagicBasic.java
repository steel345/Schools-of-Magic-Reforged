package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMetal;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumWoodType;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageCraftingRitualRecipe;
import com.paleimitations.schoolsofmagic.common.books.BookPagePlantDescription;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandardTitled;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementFurnaceRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipeSmall;
import com.paleimitations.schoolsofmagic.common.books.PageElementItemStack;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.books.PageElementRitualRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.PageElementWorldConnector;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellBlaze;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellGrowApple;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellLocateOre;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWaterJet;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWinterRoar;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellZephyr;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BookMagicBasic {
   private static ItemStack stack(net.minecraft.world.item.Item item, int meta) {
      ItemStack s = new ItemStack(item);
      s.setDamageValue(meta);
      return s;
   }

   public static void init() {
      new BookPageTableContent(null).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageChapter(null).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_title",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmb_pg0_title.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
                  new PageElementParagraphs(
                     "bmb_title",
                     0.75F,
                     0,
                     2,
                     new ParagraphBox(23, 95, 0, 99, 95),
                     new ParagraphBox(134, 50, 0, 99, 140),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_page1").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_page2").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_spellcharges").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new com.paleimitations.schoolsofmagic.common.books.BookPageItemDescription(
            new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.talisman_of_knowledge.get()))
         .addElement(new PageElementCraftingRecipe(
            Lists.newArrayList(new ItemStack[]{
               ItemStack.EMPTY, new ItemStack(Items.STRING), ItemStack.EMPTY,
               new ItemStack(Items.STRING), ItemStack.EMPTY, new ItemStack(Items.STRING),
               ItemStack.EMPTY, new ItemStack(Items.EXPERIENCE_BOTTLE), ItemStack.EMPTY
            }),
            new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.talisman_of_knowledge.get()), 159, 110))
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_page3").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_page4").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_more_spells").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_page5",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_page5.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, ItemStack.EMPTY,
                           new ItemStack(Items.STICK),
                           ItemStack.EMPTY,
                           new ItemStack(Items.GOLD_INGOT),
                           ItemStack.EMPTY,
                           new ItemStack(Items.DIAMOND),
                           ItemStack.EMPTY, ItemStack.EMPTY
                        }
                     ),
                     new ItemStack(ItemRegistry.wand_apprentice.get()),
                     159,
                     110
                  ),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           new ItemStack(Blocks.OAK_LOG),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.OAK_LOG),
                           ItemStack.EMPTY,
                           new ItemStack(Blocks.BOOKSHELF),
                           ItemStack.EMPTY,
                           new ItemStack(Blocks.OAK_LOG),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.OAK_LOG)
                        }
                     ),
                     stack(ItemRegistry.bi_podium.get(), EnumWoodType.OAK.getIndex()),
                     159, 110, 1
                  ),
                  new PageElementParagraphs(
                     "bmb_page5", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 60),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() == BlockRegistry.podium.get();
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new BookPageStandardTitled("bmb_podium1").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageStandardTitled("bmb_podium2").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_podium3",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_podium3.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           new ItemStack(Items.PAPER), ItemStack.EMPTY, ItemStack.EMPTY,
                           new ItemStack(Items.SLIME_BALL), ItemStack.EMPTY, ItemStack.EMPTY,
                           ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY
                        }
                     ),
                     new ItemStack(ItemRegistry.sticker_pack.get()),
                     159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_podium3", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_bone_knife",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_bone_knife.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, new ItemStack(ItemRegistry.item_obsidian_shard.get()), ItemStack.EMPTY,
                           new ItemStack(Items.BONE), ItemStack.EMPTY, ItemStack.EMPTY,
                           ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY
                        }
                     ),
                     new ItemStack(ItemRegistry.bone_knife.get()), 159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_bone_knife", 0.75F, 0, 1,
                     new ParagraphBox(23, 65, 0, 99, 170)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new BookPage(
            "bmb_athame",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_athame.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, ItemStack.EMPTY, new ItemStack(ItemRegistry.item_obsidian_shard.get()),
                           ItemStack.EMPTY, new ItemStack(Items.GOLD_INGOT), ItemStack.EMPTY,
                           new ItemStack(ItemRegistry.bi_gem_pyromancy.get()), ItemStack.EMPTY, ItemStack.EMPTY
                        }
                     ),
                     new ItemStack(ItemRegistry.athame.get()), 159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_athame", 0.75F, 0, 1,
                     new ParagraphBox(23, 65, 0, 99, 170)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new BookPage(
            "bmb_page6",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_page6.title", 72, 58, 99, 16, 0, true),
                  new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/steel_amalgam.png"), 140, 98, 0, 0, 88, 50, 1.0F, false),
                  new PageElementItemStack(new ItemStack(Items.IRON_INGOT), 164, 98),
                  new PageElementFurnaceRecipe(
                     new ItemStack(BlockRegistry.ore_steel.get()),
                     stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                     157, 160, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_page6", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 50),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() == BlockRegistry.block_charcoal.get();
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_page7",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_page7.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                           new ItemStack(BlockRegistry.fence_steel.get()),
                           new ItemStack(BlockRegistry.block_charcoal.get()),
                           new ItemStack(BlockRegistry.fence_steel.get()),
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           ItemStack.EMPTY,
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex())
                        }
                     ),
                     new ItemStack(BlockRegistry.brazier.get()),
                     159, 110, 0
                  ),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           stack(ItemRegistry.nugget.get(), EnumMetal.STEEL.getIndex()),
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           stack(ItemRegistry.nugget.get(), EnumMetal.STEEL.getIndex()),
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex())
                        }
                     ),
                     new ItemStack(BlockRegistry.fence_steel.get()),
                     159, 110, 1
                  ),
                  new PageElementParagraphs(
                     "bmb_page7", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 60),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() == BlockRegistry.brazier.get();
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting wr1 = RecipeRegistry.getRitualRecipe(stack(ItemRegistry.wand_apprentice.get(), 1));
      com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting wr2 = RecipeRegistry.getRitualRecipe(stack(ItemRegistry.wand_apprentice.get(), 2));
      com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting wr3 = RecipeRegistry.getRitualRecipe(stack(ItemRegistry.wand_apprentice.get(), 3));
      if (wr1 != null && wr2 != null && wr3 != null) {
         new BookPageCraftingRitualRecipe(wr1)
            .addElements(Lists.newArrayList(new PageElement[]{
               new PageElementRitualRecipe(wr2, 20, 70, 1),
               new PageElementRitualRecipe(wr3, 132, 70, 1)
            }))
            .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      }
      new BookPage(
            "bmb_pedestal",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_pedestal.title", 72, 58, 99, 16, 0, true),
                  new PageElementItemStack(new ItemStack(Blocks.DIORITE), 160, 105),
                  new PageElementItemStack(new ItemStack(Blocks.STONECUTTER), 182, 105),
                  new PageElementItemStack(
                     new ItemStack(net.minecraftforge.registries.ForgeRegistries.BLOCKS.getValue(
                        new ResourceLocation("som", "diorite_pedestal"))),
                     204, 105),
                  new PageElementParagraphs(
                     "bmb_pedestal", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() instanceof com.paleimitations.schoolsofmagic.common.blocks.BlockPedestal;
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_page8",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_page8.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           new ItemStack(Items.STICK),
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           ItemStack.EMPTY,
                           stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()),
                           ItemStack.EMPTY
                        }
                     ),
                     new ItemStack(BlockRegistry.mortnpest.get()),
                     159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_page8", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() == BlockRegistry.mortnpest.get();
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_page9",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_page9.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           new ItemStack(Items.STRING),
                           new ItemStack(Blocks.FERN),
                           new ItemStack(Items.STRING),
                           new ItemStack(Items.WHEAT),
                           new ItemStack(Items.STRING),
                           new ItemStack(Items.WHEAT),
                           new ItemStack(Items.STRING),
                           new ItemStack(Blocks.FERN),
                           new ItemStack(Items.STRING)
                        }
                     ),
                     new ItemStack(BlockRegistry.herbal_twine.get()),
                     159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_page9", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() == BlockRegistry.herbal_twine.get();
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_planter",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_planter.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.DIRT),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Items.BONE_MEAL),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.OAK_SLAB),
                           new ItemStack(Blocks.OAK_SLAB)
                        }
                     ),
                     new ItemStack(ItemRegistry.bi_planter.get()),
                     159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_planter", 0.75F, 0, 2,
                     new ParagraphBox(23, 65, 0, 99, 125),
                     new ParagraphBox(134, 50, 0, 99, 60),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140),
                     new ParagraphBox(23, 50, 2, 99, 140),
                     new ParagraphBox(134, 50, 2, 99, 140)
                  ),
                  new PageElementWorldConnector() {
                     @Override
                     public boolean connects(Level world, BlockState state, BlockPos pos) {
                        return state.getBlock() == BlockRegistry.planter.get();
                     }
                  }
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPage(
            "bmb_copper_key",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementStandardText("page.bmb_copper_key.title", 72, 58, 99, 16, 0, true),
                  new PageElementCraftingRecipe(
                     Lists.newArrayList(
                        new ItemStack[]{
                           ItemStack.EMPTY, stack(ItemRegistry.ingot.get(), 2), stack(ItemRegistry.nugget.get(), 2),
                           ItemStack.EMPTY, stack(ItemRegistry.ingot.get(), 2), stack(ItemRegistry.nugget.get(), 2),
                           ItemStack.EMPTY, stack(ItemRegistry.ingot.get(), 2), ItemStack.EMPTY
                        }
                     ),
                     new ItemStack(ItemRegistry.copper_key.get()),
                     159, 110, 0
                  ),
                  new PageElementParagraphs(
                     "bmb_copper_key", 0.75F, 0, 0,
                     new ParagraphBox(23, 65, 0, 99, 140)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageChapter(null).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      for (EnumPlantType plant : EnumPlantType.values()) {
         if (plant != EnumPlantType.NONE) {
            new BookPagePlantDescription(plant).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
         }
      }

      new BookPageChapter(null).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new SpellBlaze()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new SpellGrowApple()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new SpellLocateOre()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new SpellZephyr()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new SpellWaterJet()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new SpellWinterRoar()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellSnowball()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellDry()).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      BookPageChapter otherBooks = new BookPageChapter(null);
      otherBooks.elements.add(new PageElementTitle("page.other_books_chapter.title", 72, 58, 99, 16, 0, true));
      otherBooks.addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new BookPageStandardTitled("other_books").addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new BookPage("tea_guide_book", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.tea_guide_book.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("tea_guide_book", 0.75F, 0, 0,
            new ParagraphBox(23, 65, 0, 99, 140)),
         new PageElementCraftingRecipeSmall(Lists.newArrayList(new ItemStack[]{
            new ItemStack(Items.BOOK),
            new ItemStack(Items.PURPLE_DYE),
            new ItemStack(ItemRegistry.item_porcelain.get()),
            stack(ItemRegistry.crushed_plant.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.ROSE.getIndex())
         }), new ItemStack(ItemRegistry.tea_makers_guide.get()), 150, 95)
      })).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting codexRitual =
         RecipeRegistry.getRitualRecipe(new ItemStack(ItemRegistry.exploration_book.get()));
      java.util.List<PageElement> codexEls = Lists.newArrayList();
      codexEls.add(new PageElementStandardText("page.exploration_book_guide.title", 72, 58, 99, 16, 0, true));
      codexEls.add(new PageElementParagraphs("exploration_book_guide", 0.75F, 0, 0,
         new ParagraphBox(23, 75, 0, 99, 115)));
      if (codexRitual != null) {
         codexEls.add(new com.paleimitations.schoolsofmagic.common.books.PageElementRitualRecipe(codexRitual, 132, 47, 0));
      }
      new BookPage("exploration_book_guide", codexEls).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      new BookPage("bmb_alchemy_book", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.bmb_alchemy_book.title", 72, 58, 99, 16, 0, true),
         new PageElementCraftingRecipe(
            Lists.newArrayList(new ItemStack[]{
               new ItemStack(Items.BOOK), stack(ItemRegistry.ingot.get(), EnumMetal.STEEL.getIndex()), ItemStack.EMPTY,
               new ItemStack(ItemRegistry.item_diamond_dust.get()), stack(ItemRegistry.ingot.get(), EnumMetal.BRONZE.getIndex()), ItemStack.EMPTY,
               ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY
            }),
            new ItemStack(ItemRegistry.alchemical_manual.get()),
            159, 110, 0
         ),
         new PageElementParagraphs("bmb_alchemy_book", 0.75F, 0, 0,
            new ParagraphBox(23, 74, 0, 99, 108))
      })).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);

      com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting handbookRitual =
         RecipeRegistry.getRitualRecipe(new ItemStack(ItemRegistry.spellworkers_handbook.get()));
      java.util.List<PageElement> handbookEls = Lists.newArrayList();
      handbookEls.add(new PageElementStandardText("page.spellworkers_handbook_guide.title", 72, 58, 99, 16, 0, true));
      handbookEls.add(new PageElementParagraphs("spellworkers_handbook_guide", 0.75F, 0, 0,
         new ParagraphBox(23, 75, 0, 99, 115)));
      if (handbookRitual != null) {
         handbookEls.add(new com.paleimitations.schoolsofmagic.common.books.PageElementRitualRecipe(handbookRitual, 132, 47, 0));
      }
      new BookPage("spellworkers_handbook_guide", handbookEls).addToList(BookPageRegistry.BASIC_MAGIC_BOOK);
   }
}
