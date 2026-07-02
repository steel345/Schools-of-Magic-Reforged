package com.paleimitations.schoolsofmagic.common.registries;

import com.google.common.collect.Lists;
import java.util.List;
import com.paleimitations.schoolsofmagic.common.books.BookPage;
import com.paleimitations.schoolsofmagic.common.books.BookPageChapter;
import com.paleimitations.schoolsofmagic.common.books.BookPageCraftingRitualRecipe;
import com.paleimitations.schoolsofmagic.common.books.BookPageItemDescription;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandard;
import com.paleimitations.schoolsofmagic.common.books.BookPageStandardTitled;
import com.paleimitations.schoolsofmagic.common.books.BookPageTableContent;
import com.paleimitations.schoolsofmagic.common.books.PageElement;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementCraftingRecipeSmall;
import com.paleimitations.schoolsofmagic.common.books.PageElementImage;
import com.paleimitations.schoolsofmagic.common.books.PageElementItemStack;
import com.paleimitations.schoolsofmagic.common.books.PageElementParagraphs;
import com.paleimitations.schoolsofmagic.common.books.PageElementPotionRecipe;
import com.paleimitations.schoolsofmagic.common.books.PageElementStandardText;
import com.paleimitations.schoolsofmagic.common.books.PageElementString;
import com.paleimitations.schoolsofmagic.common.books.ParagraphBox;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.potions.SOMPotionUtils;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellEarthquake;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFieryBlessing;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellFuelFurnace;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellShadowSpy;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellSpectralHand;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellWitherBlight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;

public class BookMagicAdvanced {

   private static final ResourceLocation PAPER = new ResourceLocation("som", "textures/gui/books/paper_default.png");

   private static BookPage chartPage(String name, PageElement... chart) {
      List<PageElement> els = Lists.newArrayList();
      els.add(new PageElementStandardText("page." + name + ".title", 72, 58, 99, 16, 0, true));
      els.add(new PageElementParagraphs(name, 0.75F, 0, 0, new ParagraphBox(23, 75, 0, 99, 115)));
      for (PageElement e : chart) els.add(e);
      return new BookPage(name, els);
   }

   private static PageElementImage chartImage(String file) {
      return new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/" + file), 0, 0, 0, 0, 256, 256, 1.0F, false);
   }

   public static void init() {
      new BookPageTableContent(null).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageChapter(null).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPage(
            "bma_title",
            Lists.newArrayList(
               new PageElement[]{
                  new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bma_pg0_title.png"), 0, 0, 0, 0, 256, 256, 1.0F, false),

                  new PageElementParagraphs(
                     "bma_title", 0.75F, 0, 1,
                     new ParagraphBox(23, 95, 0, 99, 95),
                     new ParagraphBox(134, 50, 0, 99, 140),
                     new ParagraphBox(23, 50, 1, 99, 140),
                     new ParagraphBox(134, 50, 1, 99, 140)
                  )
               }
            )
         )
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page1").addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageItemDescription(new ItemStack(Blocks.BREWING_STAND))
         .addElement(new PageElementCraftingRecipe(
            Lists.newArrayList(new ItemStack[]{
               ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY,
               ItemStack.EMPTY, new ItemStack(Items.BLAZE_ROD), ItemStack.EMPTY,
               new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE)
            }),
            new ItemStack(Items.BREWING_STAND), 159, 110))
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      for (com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType ct
            : com.paleimitations.schoolsofmagic.common.blocks.EnumCauldronType.values()) {
         String t = ct.getSerializedName();
         RecipeRitualCrafting cauldronR = switch (ct) {
            case NORMAL -> com.paleimitations.schoolsofmagic.common.registries.RitualRecipeRegistry.CAULDRON_NORMAL;
            case GOLD   -> com.paleimitations.schoolsofmagic.common.registries.RitualRecipeRegistry.CAULDRON_GOLD;
            case LION   -> com.paleimitations.schoolsofmagic.common.registries.RitualRecipeRegistry.CAULDRON_LION;
         };
         if (cauldronR != null) {
            new BookPageCraftingRitualRecipe(cauldronR,
                  "crafting_ritual_cauldron_" + t,
                  "block.som.cauldron." + t,
                  "crafting_ritual_cauldron_" + t)
               .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
         }
      }

      new BookPage("bmb_pg25_title", Lists.newArrayList(new PageElement[]{
         new PageElementImage(new ResourceLocation("som", "textures/gui/books/images/bmb_pg25_title.png"), 0, 0, 0, 0, 256, 256, 1.0F, false)
      })).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      ItemStack potion_drinkable = new ItemStack(ItemRegistry.potion_drinkable.get());
      potion_drinkable.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(new MobEffectInstance(MobEffects.REGENERATION))));
      ItemStack potion_throwable = new ItemStack(ItemRegistry.potion_throwable.get());
      potion_throwable.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(new MobEffectInstance(MobEffects.REGENERATION))));
      ItemStack example_potion = new ItemStack(ItemRegistry.potion_throwable.get(), 3);
      example_potion.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(
            new MobEffectInstance(MobEffects.WEAKNESS),
            new MobEffectInstance(MobEffects.DIG_SLOWDOWN))));
      ItemStack potion_lingering = new ItemStack(ItemRegistry.potion_lingering.get());
      potion_lingering.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(new MobEffectInstance(MobEffects.REGENERATION))));
      ItemStack potion_burst = new ItemStack(ItemRegistry.potion_burst.get());
      potion_burst.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(new MobEffectInstance(MobEffects.REGENERATION))));
      ItemStack potion_jug = new ItemStack(ItemRegistry.potion_jug.get());
      potion_jug.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(new MobEffectInstance(MobEffects.REGENERATION))));
      ItemStack potion_crystal = new ItemStack(ItemRegistry.potion_crystal.get());
      potion_crystal.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY)
         .ifPresent(d -> d.setPotionEffects(Lists.newArrayList(new MobEffectInstance(MobEffects.REGENERATION))));
      ItemStack potion_arrow = new ItemStack(Items.TIPPED_ARROW, 9);
      PotionUtils.setPotion(potion_arrow, Potions.STRONG_HEALING);

      new BookPageChapter(null).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page10", PAPER).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page6", PAPER).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageStandardTitled("bma_page7", PAPER).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageStandardTitled("bma_page8", PAPER).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      chartPage("bma_page11", chartImage("duration_power_chart.png"),
         new PageElementItemStack(SOMPotionUtils.AMPLIFIER_LVL_1, 167, 63),
         new PageElementItemStack(SOMPotionUtils.AMPLIFIER_LVL_2, 167, 95),
         new PageElementItemStack(SOMPotionUtils.AMPLIFIER_LVL_3, 167, 127),
         new PageElementItemStack(SOMPotionUtils.AMPLIFIER_LVL_4, 167, 159),
         new PageElementString(SOMPotionUtils.AMPLIFIER_LVL_1, 163, 82, 68, 10, 0, false),
         new PageElementString(SOMPotionUtils.AMPLIFIER_LVL_2, 163, 114, 68, 10, 0, false),
         new PageElementString(SOMPotionUtils.AMPLIFIER_LVL_3, 163, 146, 68, 10, 0, false),
         new PageElementString(SOMPotionUtils.AMPLIFIER_LVL_4, 163, 178, 68, 10, 0, false)
      ).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      chartPage("bma_page12", chartImage("duration_power_chart.png"),
         new PageElementItemStack(SOMPotionUtils.DURATION_LVL_1, 167, 63),
         new PageElementItemStack(SOMPotionUtils.DURATION_LVL_2, 167, 95),
         new PageElementItemStack(SOMPotionUtils.DURATION_LVL_3, 167, 127),
         new PageElementItemStack(SOMPotionUtils.DURATION_LVL_4, 167, 159),
         new PageElementString(SOMPotionUtils.DURATION_LVL_1, 163, 82, 68, 10, 0, false),
         new PageElementString(SOMPotionUtils.DURATION_LVL_2, 163, 114, 68, 10, 0, false),
         new PageElementString(SOMPotionUtils.DURATION_LVL_3, 163, 146, 68, 10, 0, false),
         new PageElementString(SOMPotionUtils.DURATION_LVL_4, 163, 178, 68, 10, 0, false)
      ).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page13", PAPER).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page14", PAPER).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      chartPage("bma_page15", chartImage("drink_speed_chart.png"),
         new PageElementItemStack(new ItemStack(Items.NETHER_WART), 140, 63),
         new PageElementItemStack(SOMPotionUtils.DRINK_LVL_1, 182, 63),
         new PageElementItemStack(SOMPotionUtils.DRINK_LVL_2, 182, 95),
         new PageElementItemStack(SOMPotionUtils.DRINK_LVL_3, 182, 127),
         new PageElementItemStack(SOMPotionUtils.DRINK_LVL_4, 182, 159),
         new PageElementString(SOMPotionUtils.DRINK_LVL_1, 178, 82, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.DRINK_LVL_2, 178, 114, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.DRINK_LVL_3, 178, 146, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.DRINK_LVL_4, 178, 178, 53, 10, 0, false)
      ).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      chartPage("bma_page16", chartImage("radius_chart.png"),
         new PageElementItemStack(new ItemStack(Items.GUNPOWDER), 140, 63),
         new PageElementItemStack(new ItemStack(Items.DRAGON_BREATH), 140, 95),
         new PageElementItemStack(SOMPotionUtils.RADIUS_LVL_1, 182, 63),
         new PageElementItemStack(SOMPotionUtils.RADIUS_LVL_2, 182, 95),
         new PageElementItemStack(SOMPotionUtils.RADIUS_LVL_3, 182, 127),
         new PageElementItemStack(SOMPotionUtils.RADIUS_LVL_4, 182, 159),
         new PageElementString(SOMPotionUtils.RADIUS_LVL_1, 178, 82, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.RADIUS_LVL_2, 178, 114, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.RADIUS_LVL_3, 178, 146, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.RADIUS_LVL_4, 178, 178, 53, 10, 0, false)
      ).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      chartPage("bma_page17", chartImage("length_chart.png"),
         new PageElementItemStack(new ItemStack(Items.DRAGON_BREATH), 140, 63),
         new PageElementItemStack(SOMPotionUtils.LENGTH_LVL_1, 182, 63),
         new PageElementItemStack(SOMPotionUtils.LENGTH_LVL_2, 182, 95),
         new PageElementItemStack(SOMPotionUtils.LENGTH_LVL_3, 182, 127),
         new PageElementItemStack(SOMPotionUtils.LENGTH_LVL_4, 182, 159),
         new PageElementString(SOMPotionUtils.LENGTH_LVL_1, 178, 82, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.LENGTH_LVL_2, 178, 114, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.LENGTH_LVL_3, 178, 146, 53, 10, 0, false),
         new PageElementString(SOMPotionUtils.LENGTH_LVL_4, 178, 178, 53, 10, 0, false)
      ).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageChapter(null).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      chartPage("bma_page18",
         new PageElementCraftingRecipe(
            Lists.newArrayList(new ItemStack[]{
               new ItemStack(Blocks.GLASS),
               new ItemStack(Items.GOLD_NUGGET),
               new ItemStack(Blocks.GLASS),
               new ItemStack(Blocks.GLASS),
               ItemStack.EMPTY,
               new ItemStack(Blocks.GLASS),
               new ItemStack(Blocks.GLASS),
               new ItemStack(Blocks.GLASS),
               new ItemStack(Blocks.GLASS)
            }),
            new ItemStack(ItemRegistry.bottle_empty.get(), 3),
            150, 66
         ),
         new PageElementCraftingRecipeSmall(
            Lists.newArrayList(new ItemStack[]{
               new ItemStack(Blocks.REDSTONE_TORCH),
               new ItemStack(ItemRegistry.bottle_empty.get()),
               ItemStack.EMPTY,
               ItemStack.EMPTY
            }),
            new ItemStack(ItemRegistry.burst_bottle.get()),
            156, 150
         )
      ).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page19", PAPER)
         .addElements(Lists.newArrayList(new PageElement[]{
            chartImage("potion_tree_drinkable.png"),
            new PageElementItemStack(new ItemStack(Items.NETHER_WART), 164, 102),
            new PageElementItemStack(new ItemStack(Items.GLASS_BOTTLE), 194, 90),
            new PageElementItemStack(new ItemStack(ItemRegistry.bottle_empty.get()), 213, 90),
            new PageElementItemStack(potion_drinkable, 194, 114),
            new PageElementItemStack(potion_jug, 213, 114)
         }))
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page20", PAPER)
         .addElements(Lists.newArrayList(new PageElement[]{
            chartImage("potion_tree_splash.png"),
            new PageElementItemStack(new ItemStack(Items.NETHER_WART), 164, 77),
            new PageElementItemStack(new ItemStack(Items.GUNPOWDER), 164, 110),
            new PageElementItemStack(new ItemStack(Items.GLASS_BOTTLE), 194, 90),
            new PageElementItemStack(new ItemStack(ItemRegistry.burst_bottle.get()), 213, 90),
            new PageElementItemStack(potion_throwable, 194, 114),
            new PageElementItemStack(potion_burst, 213, 114)
         }))
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page21", PAPER)
         .addElements(Lists.newArrayList(new PageElement[]{
            chartImage("potion_tree_lingering.png"),
            new PageElementItemStack(new ItemStack(Items.NETHER_WART), 164, 53),
            new PageElementItemStack(new ItemStack(Items.GUNPOWDER), 164, 86),
            new PageElementItemStack(new ItemStack(Items.DRAGON_BREATH), 164, 110),
            new PageElementItemStack(new ItemStack(Items.GLASS_BOTTLE), 194, 66),
            new PageElementItemStack(new ItemStack(ItemRegistry.burst_bottle.get()), 213, 66),
            new PageElementItemStack(new ItemStack(Items.DIAMOND), 194, 146),
            new PageElementItemStack(new ItemStack(Items.ARROW, 9), 213, 146),
            new PageElementItemStack(potion_lingering, 194, 90),
            new PageElementItemStack(potion_burst, 213, 90),
            new PageElementItemStack(potion_crystal, 194, 122),
            new PageElementItemStack(potion_arrow, 213, 122)
         }))
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageStandardTitled("bma_page22", PAPER)
         .addElement(new PageElementPotionRecipe(
            Lists.newArrayList(new ItemStack[]{
               new ItemStack(Items.NETHER_WART),
               new ItemStack(BlockRegistry.mushroom_pink.get()),
               SOMPotionUtils.AMPLIFIER_LVL_1,
               SOMPotionUtils.DURATION_LVL_2,
               new ItemStack(BlockRegistry.dynamic_web.get()),
               SOMPotionUtils.AMPLIFIER_LVL_2,
               SOMPotionUtils.DURATION_LVL_1,
               new ItemStack(Items.GUNPOWDER),
               SOMPotionUtils.RADIUS_LVL_2
            }),
            example_potion))
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      RecipeRitualCrafting potionCrystalRitual = new RecipeRitualCrafting(
         new ItemStack(ItemRegistry.potion_crystal.get()),
         50, 0, 0,
         com.google.common.collect.Maps.newHashMap(),
         com.google.common.collect.Maps.newHashMap(),
         new ItemStack(ItemRegistry.potion_crystal.get()));
      new BookPageCraftingRitualRecipe(potionCrystalRitual, "ritual_potion_crystal",
            "page.ritual_potion_crystal.title", "ritual_potion_crystal")
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageItemDescription(new ItemStack(ItemRegistry.potion_charm.get()))
         .addElement(new PageElementCraftingRecipe(
            Lists.newArrayList(new ItemStack[]{
               ItemStack.EMPTY, new ItemStack(Items.STRING), ItemStack.EMPTY,
               new ItemStack(Items.STRING), ItemStack.EMPTY, new ItemStack(Items.STRING),
               ItemStack.EMPTY, new ItemStack(ItemRegistry.potion_crystal.get()), ItemStack.EMPTY
            }),
            new ItemStack(ItemRegistry.potion_charm.get()), 159, 110))
         .addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);

      new BookPageChapter(null).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new SpellWitherBlight()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new SpellShadowSpy()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellFangMangle()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new SpellFuelFurnace()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new SpellEarthquake()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new SpellFieryBlessing()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new SpellSpectralHand()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellThunderstroke()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellFastForward()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageSpell(new com.paleimitations.schoolsofmagic.common.spells.spells.SpellTranslocation()).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
      new BookPageChapter(null).addToList(BookPageRegistry.ADVANCED_MAGIC_BOOK);
   }
}
