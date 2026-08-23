package com.paleimitations.schoolsofmagic.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class BookTemplates {
   public static final class Template {
      public final String name;
      public final String label;
      public final ResourceLocation texture;
      public final int width;
      public final int height;

      public final int[][] slots;

      public final int hitX;
      public final int hitY;
      public final int hitW;
      public final int hitH;

      public final int fixedSlot;
      public final String fixedItem;

      public int homeX = 134;
      public int homeY = 60;

      Template home(int x, int y) {
         this.homeX = x;
         this.homeY = y;
         return this;
      }

      Template(String name, String label, String texture, int width, int height, int[][] slots) {
         this(name, label, texture, width, height, slots, 0, 0, width, height, -1, "");
      }

      Template(String name, String label, String texture, int width, int height, int[][] slots,
            int hitX, int hitY, int hitW, int hitH, int fixedSlot, String fixedItem) {
         this.name = name;
         this.label = label;
         this.texture = new ResourceLocation("som", texture);
         this.width = width;
         this.height = height;
         this.slots = slots;
         this.hitX = hitX;
         this.hitY = hitY;
         this.hitW = hitW;
         this.hitH = hitH;
         this.fixedSlot = fixedSlot;
         this.fixedItem = fixedItem;
      }
   }

   public static final List<Template> ALL = new ArrayList<>();

   static {
      ALL.add(new Template("crafting", "Crafting", "textures/gui/books/crafting_recipe.png", 54, 76,
         new int[][]{{1,1},{19,1},{37,1},{1,19},{19,19},{37,19},{1,37},{19,37},{37,37},{19,59}}));
      ALL.add(new Template("crafting_small", "Small Crafting", "textures/gui/books/crafting_recipe_small.png", 60, 36,
         new int[][]{{1,1},{1,19},{19,1},{19,19},{41,10}}));

      ALL.add(new Template("furnace", "Furnace", "textures/gui/books/furnace_recipe.png", 100, 123,
         new int[][]{{1,1},{19,1},{37,1}},
         0, 0, 55, 20, 1, "minecraft:furnace"));
      ALL.add(new Template("herbal_twine", "Herbal Twine", "textures/gui/books/herbal_twine_recipe.png", 100, 123,
         new int[][]{{1,8},{35,8}},
         0, 0, 55, 30, -1, ""));

      ALL.add(new Template("teapot", "Teapot", "textures/gui/teapot_book.png", 116, 98,
         new int[][]{{60,21},{60,42},{60,62},{7,42}}).home(24, 62));

      ALL.add(new Template("mortar", "Mortar & Pestle", "textures/gui/books/mortnpest_recipe.png", 88, 62,
         new int[][]{{2,11},{2,34},{70,11},{70,34}}));
      ALL.add(new Template("ritual", "Ritual", "textures/gui/books/crafting_ritual_recipe.png", 106, 123,
         new int[][]{{46,63},{25,52},{4,45},{2,24},{23,17},{44,23},{65,33},{86,23},{80,2},{44,105}}));
      ALL.add(new Template("catalyst_basin", "Catalyst Basin", "textures/gui/books/catalyst_basin_recipe.png", 104, 105,
         new int[][]{{14,1},{14,20},{74,1},{74,20},{14,65},{14,84},{74,65}}));

      ALL.add(new Template("potion", "Potion", "textures/gui/books/potion_recipe.png", 256, 256,
         new int[][]{{180,131},{157,120},{134,108},{136,85},{159,74},
                     {182,86},{205,96},{220,73},{197,61},{178,163}},
         130, 55, 110, 126, -1, "").home(0, 0));
   }

   private BookTemplates() {}

   public static Template byName(String name) {
      for (Template t : ALL) {
         if (t.name.equalsIgnoreCase(name)) return t;
      }
      return null;
   }
}
