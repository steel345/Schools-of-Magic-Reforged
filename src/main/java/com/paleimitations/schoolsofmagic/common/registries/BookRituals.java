package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.paleimitations.schoolsofmagic.common.blocks.EnumBottle;
import com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementRitualRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.PageElementTitle;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BookRituals {

   private static final float TEXT_SCALE = 0.75F;
   private static final List<BookPage> BOOK = BookPageRegistry.RITUAL_BOOK;

   private static ItemStack meta(Item item, int dmg) {
      ItemStack s = new ItemStack(item);
      s.setDamageValue(dmg);
      return s;
   }

   private static RecipeRitualCrafting rite(int mana, Object... inputs) {
      return new RecipeRitualCrafting(ItemStack.EMPTY, mana, 0, 0, Maps.newHashMap(), Maps.newHashMap(), inputs);
   }

   private static void textPage(String name, String titleKey, RecipeRitualCrafting recipe) {
      List<PageElement> els = Lists.newArrayList();
      els.add(new PageElementStandardText(titleKey, 72, 56, 99, 16, 0, true));
      els.add(new PageElementParagraphs(name, TEXT_SCALE, 0, 0, new ParagraphBox(23, 74, 0, 100, 130)));
      if (recipe != null) {
         els.add(new PageElementRitualRecipe(recipe, 132, 47, 0));
      }
      new BookPage(name, els).addToList(BOOK);
   }

   public static void init() {
      new BookPageTableContent(null).addToList(BOOK);

      BookPageChapter ch1 = new BookPageChapter(null);
      ch1.elements.add(new PageElementTitle("page.rc_ch_rituals.title", 72, 58, 99, 16, 0, true));
      ch1.addToList(BOOK);

      new BookPage("rc_intro", Lists.newArrayList(new PageElement[]{
         new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmb_rituals.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),
         new PageElementParagraphs("rc_intro", TEXT_SCALE, 0, 0, new ParagraphBox(23, 78, 0, 100, 90))
      })).addToList(BOOK);

      textPage("rc_owners", "page.rc_owners.title", null);
      textPage("rc_flame", "page.rc_flame.title", rite(10, new ItemStack(Items.BLUE_DYE)));
      textPage("rc_crafting", "page.rc_crafting.title", null);

      RecipeRitualCrafting broomRitual = RecipeRegistry.getRitualRecipe(new ItemStack(ItemRegistry.broom.get()));
      textPage("rc_broom", "page.rc_broom.title", broomRitual);

      new BookPage("rc_bell", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.rc_bell.title", 72, 56, 99, 16, 0, true),
         new PageElementParagraphs("rc_bell", TEXT_SCALE, 0, 0, new ParagraphBox(23, 74, 0, 100, 130)),
         new PageElementCraftingRecipe(Lists.newArrayList(new ItemStack[]{
            ItemStack.EMPTY, new ItemStack(ItemRegistry.nugget.get()), ItemStack.EMPTY,
            new ItemStack(ItemRegistry.nugget.get()), new ItemStack(Items.STRING), new ItemStack(ItemRegistry.nugget.get()),
            new ItemStack(ItemRegistry.nugget.get()), new ItemStack(ItemRegistry.ingot.get()), new ItemStack(ItemRegistry.nugget.get())
         }), new ItemStack(ItemRegistry.silver_bell.get()), 150, 70)
      })).addToList(BOOK);

      RecipeRitualCrafting magicBroomRitual = RecipeRegistry.getRitualRecipe(new ItemStack(ItemRegistry.magic_broom.get()));
      textPage("rc_magic_broom", "page.rc_magic_broom.title", magicBroomRitual);

      BookPageChapter ch2 = new BookPageChapter(null);
      ch2.elements.add(new PageElementTitle("page.rc_ch_rites.title", 72, 58, 99, 16, 0, true));
      ch2.addToList(BOOK);

      new BookPage("rc_rites_intro", Lists.newArrayList(new PageElement[]{
         new PageElementStandardText("page.rc_rites_intro.title", 72, 58, 99, 16, 0, true),
         new PageElementParagraphs("rc_rites_intro", TEXT_SCALE, 0, 0,
            new ParagraphBox(23, 74, 0, 100, 130),
            new ParagraphBox(134, 50, 0, 99, 140))
      })).addToList(BOOK);

      textPage("rite_nature_spirit", "page.rite_nature_spirit.title", rite(150,
         new ItemStack(Items.OAK_SAPLING),
         new ItemStack(ItemRegistry.bi_trap_spike.get()),
         meta(ItemRegistry.gem_dust.get(), EnumMagicType.AURAMANCY.getIndex()),
         new ItemStack(Items.OAK_LOG),
         meta(ItemRegistry.tree_item.get(), com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.ASH.getIndex()),
         meta(ItemRegistry.bottle.get(), EnumBottle.WORMWOOD.getIndex())));

      textPage("rite_flaming_bird", "page.rite_flaming_bird.title", rite(200,
         new ItemStack(Items.BLAZE_ROD),
         new ItemStack(Items.LAVA_BUCKET),
         meta(ItemRegistry.ingredient.get(), EnumIngredient.BIRD_HEART.getIndex()),
         meta(ItemRegistry.gem_chunk.get(), EnumMagicType.PYROMANCY.getIndex()),
         new ItemStack(Items.MUTTON),
         meta(ItemRegistry.bottle.get(), EnumBottle.FIREBERRY.getIndex())).setNote("Chicken in range"));

      textPage("rite_storm", "page.rite_storm.title", rite(50,
         meta(ItemRegistry.bottle.get(), EnumBottle.STORMTHISTLE.getIndex()),
         meta(ItemRegistry.seed_magic_plant.get(), EnumMagicType.ANIMANCY.getIndex()),
         new ItemStack(ItemRegistry.bi_mushroom_dark.get())));

      textPage("rite_rain", "page.rite_rain.title", rite(50,
         meta(ItemRegistry.crushed_plant.get(), EnumPlantType.HYDROMANCY.getIndex()),
         new ItemStack(Items.WATER_BUCKET),
         meta(ItemRegistry.seed_magic_plant.get(), EnumMagicType.ANIMANCY.getIndex())));

      textPage("rite_clear_sky", "page.rite_clear_sky.title", rite(50,
         new ItemStack(Items.MILK_BUCKET),
         meta(ItemRegistry.crushed_plant.get(), EnumPlantType.ANIMANCY.getIndex())));

      textPage("rite_eclipse", "page.rite_eclipse.title", rite(50,
         meta(ItemRegistry.bottle.get(), EnumBottle.NIGHTBERRY.getIndex()),
         new ItemStack(Items.STONE_AXE)));

      textPage("rite_rising_sun", "page.rite_rising_sun.title", rite(50,
         meta(ItemRegistry.bottle.get(), EnumBottle.SUNFLOWER.getIndex()),
         meta(ItemRegistry.gem_dust.get(), EnumMagicType.HELIOMANCY.getIndex())));

      textPage("rite_luna", "page.rite_luna.title", rite(40,
         new ItemStack(ItemRegistry.magic_diamond.get()),
         meta(ItemRegistry.bottle.get(), EnumBottle.JIMSONWEED.getIndex())).setNote("1-8 Moon Dew, +10 mana each"));

   }
}
