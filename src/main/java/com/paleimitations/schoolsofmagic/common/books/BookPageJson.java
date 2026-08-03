package com.paleimitations.schoolsofmagic.common.books;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;

// A Patchouli-style, declaratively-authored page. Write
// assets/som/book_pages/<name>.json with an ordered "elements" array; the pieces
// auto-stack down the page and the body auto-flows into the standard two-column
// template (title + body on the SAME page, overflowing to further spreads).
//
//   { "elements": [
//       { "type": "image", "image": "som:textures/gui/books/header.png", "width": 116, "height": 31 },
//       { "type": "3d", "item": "minecraft:lodestone", "size": 24, "spin": true },
//       { "type": "multiblock", "size": 15, "build": true,
//         "legend": { "L": "minecraft:lapis_block", "G": "minecraft:gold_block" },
//         "pattern": [ ["LLL","L L","LLL"], ["   "," G ","   "] ] },
//       { "type": "title", "text": "page.som.my_page.title" },
//       { "type": "text",  "text": "page.som.my_page.text" }
//   ] }
//
// Any figure may carry x_pos/y_pos to place it absolutely; otherwise it is
// centred and stacked. The JSON is read off the classpath so the element
// structure exists on both sides (no desync); text is resolved from the "text"
// translation key on the client. Existing hand-built pages are unaffected.
public class BookPageJson extends BookPage {

   private static final int CENTER_X = 128;

   public BookPageJson(String name) {
      super(name, buildElements(name));
   }

   private static JsonObject read(String name) {
      String path = "assets/som/book_pages/" + name + ".json";
      try (InputStream is = BookPageJson.class.getClassLoader().getResourceAsStream(path)) {
         if (is == null) return null;
         JsonElement el = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8));
         return el != null && el.isJsonObject() ? el.getAsJsonObject() : null;
      } catch (Exception e) {
         return null;
      }
   }

   private static int i(JsonObject o, String key, int def) {
      return o.has(key) && o.get(key).isJsonPrimitive() ? o.get(key).getAsInt() : def;
   }

   private static float f(JsonObject o, String key, float def) {
      return o.has(key) && o.get(key).isJsonPrimitive() ? o.get(key).getAsFloat() : def;
   }

   private static boolean b(JsonObject o, String key, boolean def) {
      return o.has(key) && o.get(key).isJsonPrimitive() ? o.get(key).getAsBoolean() : def;
   }

   private static String s(JsonObject o, String key) {
      return o.has(key) && o.get(key).isJsonPrimitive() ? o.get(key).getAsString() : null;
   }

   // True when a page of this name is authored as JSON, so other page types can
   // prefer it over their built-in layout.
   public static boolean exists(String name) {
      return read(name) != null;
   }

   // The elements of a JSON-authored page, for pages that are assembled in code but
   // want to be overridable by a JSON file.
   public static List<PageElement> elementsFor(String name) {
      return buildElements(name);
   }

   private static List<PageElement> buildElements(String name) {
      List<PageElement> els = Lists.newArrayList();
      JsonObject root = read(name);
      if (root == null || !root.has("elements") || !root.get("elements").isJsonArray()) return els;

      int cursorY = 6;
      int rightTop = 50; // right page has no title, so its column starts near the top
      // A spell/potion figure occupies the whole left page, so the body starts on the
      // right instead of underneath it.
      boolean leftFigure = false;
      // Pre-pass: a recipe/template on the right half must be known BEFORE any text is
      // laid out, regardless of element order, so the body never flows under it.
      boolean rightTemplate = false;
      for (JsonElement je : root.getAsJsonArray("elements")) {
         if (!je.isJsonObject()) continue;
         JsonObject o = je.getAsJsonObject();
         String t = s(o, "type");
         if (t == null) continue;
         String kind = "template".equals(t) ? (s(o, "kind") == null ? "crafting" : s(o, "kind")) : t;
         if (isRecipeKind(kind) && i(o, "x_pos", defaultRecipeX(kind)) >= 110) { rightTemplate = true; break; }
      }
      for (JsonElement je : root.getAsJsonArray("elements")) {
         if (!je.isJsonObject()) continue;
         JsonObject o = je.getAsJsonObject();
         String type = s(o, "type");
         if (type == null) continue;

         switch (type) {
            case "image": {
               String img = s(o, "image");
               if (img == null) break;
               int w = i(o, "width", 116);
               int h = i(o, "height", 31);
               int xp = i(o, "x_pos", (256 - w) / 2);
               int yp = i(o, "y_pos", -1);
               int y = yp >= 0 ? yp : cursorY;
               els.add(new PageElementImage(new ResourceLocation(img), xp, y, 0, 0, w, h, 1.0F, false));
               // A full-canvas header overlay (its art baked near the top) reserves a
               // header band; a small banner just advances by its own height.
               if (h >= 150) { cursorY = Math.max(cursorY, 90); rightTop = Math.max(rightTop, 90); }
               else cursorY = y + h + 5;
               break;
            }
            case "title": {
               String key = s(o, "text");
               if (key == null) break;
               int yp = i(o, "y_pos", -1);
               // Match the rest of the book's pages (title y58, body starts y65).
               int y = yp >= 0 ? yp : (cursorY <= 6 ? 58 : cursorY);
               els.add(new PageElementStandardText(key, 72, y, 99, 16, 0, true));
               cursorY = y + 7;
               break;
            }
            case "3d": {
               String itemId = s(o, "item");
               ItemStack st = ItemStack.EMPTY;
               if (itemId != null) {
                  Item it = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
                  if (it != null) st = new ItemStack(it);
               }
               float size = f(o, "size", 24.0F);
               float tilt = f(o, "tilt", 30.0F);
               boolean spin = b(o, "spin", true);
               int bandH = Math.round(size * 1.5F) + 10;
               int xp = i(o, "x_pos", CENTER_X);
               int yp = i(o, "y_pos", -1);
               int cy = yp >= 0 ? yp : cursorY + bandH / 2;
               els.add(new PageElement3DModel(st, xp, cy, 0, size, tilt, spin, f(o, "lift", -8.0F)));
               cursorY = (yp >= 0 ? cy + bandH / 2 : cursorY + bandH) + 4;
               break;
            }
            case "multiblock": {
               Map<Character, BlockState> legend = new HashMap<>();
               if (o.has("legend") && o.get("legend").isJsonObject()) {
                  for (Map.Entry<String, JsonElement> e : o.getAsJsonObject("legend").entrySet()) {
                     if (e.getKey().isEmpty() || !e.getValue().isJsonPrimitive()) continue;
                     Block blk = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(e.getValue().getAsString()));
                     if (blk != null) legend.put(e.getKey().charAt(0), blk.defaultBlockState());
                  }
               }
               String[][] layers = readPattern(o);
               float size = f(o, "size", 15.0F);
               float tilt = f(o, "tilt", 30.0F);
               boolean spin = b(o, "spin", true);
               boolean build = b(o, "build", true);
               int bandH = Math.round(size * Math.max(1, layers.length) * 1.1F) + 14;
               int xp = i(o, "x_pos", CENTER_X);
               int yp = i(o, "y_pos", -1);
               int cy = yp >= 0 ? yp : cursorY + bandH / 2;
               els.add(new PageElementMultiblock(xp, cy, 0, size, tilt, spin, build, legend, layers, f(o, "lift", -11.0F)));
               cursorY = (yp >= 0 ? cy + bandH / 2 : cursorY + bandH) + 4;
               break;
            }
            case "item": {
               ItemStack st = itemStack(s(o, "item"), i(o, "count", 1));
               int xp = i(o, "x_pos", 150);
               int yp = i(o, "y_pos", cursorY);
               els.add(new PageElementItemStack(st, xp, yp));
               break;
            }
            case "spotlight": {
               // A centred row of enlarged item icons under the title; the body then
               // makes room and flows below (like the mushroom pages).
               List<ItemStack> items = itemList(o);
               if (items.isEmpty()) break;
               float scale = f(o, "scale", 2.0F);
               int iconW = Math.round(16.0F * scale);
               int gap = Math.round(8.0F * scale);
               int total = items.size() * iconW + (items.size() - 1) * gap;
               int startX = i(o, "x_pos", (256 - total) / 2);
               int y = i(o, "y_pos", cursorY + 2);
               for (int k = 0; k < items.size(); k++) {
                  els.add(new PageElementItemStackScaled(items.get(k), startX + k * (iconW + gap), y, scale));
               }
               int bottom = y + iconW + 8;
               cursorY = bottom;
               rightTop = Math.max(rightTop, bottom);
               break;
            }
            case "template": {
               String kind = s(o, "kind");
               if (addRecipe(kind == null ? "crafting" : kind, o, els)) rightTemplate = true;
               break;
            }
            case "crafting": case "crafting_small":
            case "ritual": case "furnace": case "smelting":
            case "mortar": case "catalyst": case "catalyst_basin":
            case "tea": case "teapot": case "brewing": case "potion":
            case "herbal_twine": {
               if (addRecipe(type, o, els)) rightTemplate = true;
               break;
            }
            case "spell": {
               // The spell's own info block fills the left page; the body text then
               // flows down the right-hand column.
               String id = s(o, "spell");
               com.paleimitations.schoolsofmagic.common.spells.Spell spell = findSpell(id);
               if (spell == null) break;
               els.add(new PageElementSpellInfo(spell));
               leftFigure = true;
               break;
            }
            case "potion_effect": {
               // A full potion figure (lock, sigil, icon, ingredient, bottle, timer)
               // on the left, plus whatever recipes make that ingredient.
               net.minecraft.world.effect.MobEffect effect =
                  ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(s(o, "effect") == null ? "" : s(o, "effect")));
               if (effect == null) break;
               com.paleimitations.schoolsofmagic.common.MagicSchool school =
                  com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry.getSchoolFromName(s(o, "school"));
               com.paleimitations.schoolsofmagic.common.MagicElement element =
                  com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry.getElementFromName(s(o, "element"));
               if (school == null || element == null) break;
               net.minecraft.world.effect.MobEffectInstance inst = new net.minecraft.world.effect.MobEffectInstance(
                  effect, i(o, "duration", 3600), i(o, "amplifier", 0));
               // The ingredient is whatever the cauldron brews this effect from; an
               // explicit "ingredient" only acts as an override.
               ItemStack ingredient = o.has("ingredient") ? itemStack(s(o, "ingredient"), 1) : brewingIngredient(effect);
               els.addAll(BookPagePotionEffect.visualElements(inst, ingredient, school, element));
               addIngredientRecipes(ingredient, els);
               leftFigure = true;
               break;
            }
            case "text": {
               String key = s(o, "text");
               if (key == null) break;
               boolean explicitY = o.has("y_pos");
               int textTop = i(o, "y_pos", cursorY);
               // The left column sits below the title; the right page has no title, so
               // its column starts near the top (unless the author pinned text y_pos,
               // in which case both columns honour it).
               int rightColTop = explicitY ? textTop : rightTop;
               int hL = Math.max(24, 188 - textTop);
               int hR = Math.max(24, 188 - rightColTop);
               List<ParagraphBox> boxes = Lists.newArrayList();
               // A spell/potion figure owns the left page, so the body starts in the
               // right column; otherwise it starts on the left as usual.
               if (leftFigure) {
                  boxes.add(new ParagraphBox(134, rightColTop, 0, 99, hR));
               } else {
                  boxes.add(new ParagraphBox(23, textTop, 0, 99, hL));
                  // A side template (recipe on the right half) keeps the first page one
                  // column so the body doesn't run under it; later spreads use both.
                  if (!rightTemplate) boxes.add(new ParagraphBox(134, rightColTop, 0, 99, hR));
               }
               boxes.add(new ParagraphBox(23, 50, 1, 99, 140));
               boxes.add(new ParagraphBox(134, 50, 1, 99, 140));
               boxes.add(new ParagraphBox(23, 50, 2, 99, 140));
               boxes.add(new ParagraphBox(134, 50, 2, 99, 140));
               els.add(PageElementParagraphs.fromLangKey(key, 0.75F, 0, 2, boxes.toArray(new ParagraphBox[0])));
               break;
            }
            default: break;
         }
      }
      return els;
   }

   // Builds a recipe template element from JSON. Returns true if it sits on the
   // right half (so the body text stays in the left column). Positions default to
   // the right side and can be overridden with x_pos/y_pos.
   private static boolean addRecipe(String kind, JsonObject o, List<PageElement> els) {
      switch (kind) {
         case "crafting": {
            List<ItemStack> in = patternInputs(o);
            while (in.size() < 9) in.add(ItemStack.EMPTY);
            ItemStack out = itemStack(s(o, "result"), i(o, "count", 1));
            int xp = i(o, "x_pos", 150), yp = i(o, "y_pos", 74);
            els.add(new PageElementCraftingRecipe(in, out, xp, yp));
            return xp >= 110;
         }
         case "crafting_small": {
            List<ItemStack> in = patternInputs(o);
            while (in.size() < 4) in.add(ItemStack.EMPTY);
            ItemStack out = itemStack(s(o, "result"), i(o, "count", 1));
            int xp = i(o, "x_pos", 150), yp = i(o, "y_pos", 80);
            els.add(new PageElementCraftingRecipeSmall(in, out, xp, yp));
            return xp >= 110;
         }
         case "furnace": case "smelting": {
            ItemStack in = itemStack(s(o, "input"), 1);
            ItemStack out = itemStack(s(o, "result"), i(o, "count", 1));
            int xp = i(o, "x_pos", 150), yp = i(o, "y_pos", 78);
            els.add(new PageElementFurnaceRecipe(in, out, xp, yp, 0));
            return xp >= 110;
         }
         case "mortar": {
            ItemStack out = itemStack(s(o, "result"), 1);
            com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest r = out.isEmpty() ? null
               : com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.getMortarRecipe(out);
            if (r == null) return false;
            int xp = i(o, "x_pos", 138), yp = i(o, "y_pos", 95);
            els.add(new PageElementMortarRecipe(r, xp, yp, 0));
            return xp >= 110;
         }
         case "catalyst": case "catalyst_basin": {
            ItemStack out = itemStack(s(o, "result"), 1);
            com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin r = out.isEmpty() ? null
               : com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.getCatalystRecipe(out);
            if (r == null) return false;
            int xp = i(o, "x_pos", 138), yp = i(o, "y_pos", 95);
            els.add(new PageElementCatalystBasinRecipe(r, xp, yp, 0));
            return xp >= 110;
         }
         case "tea": case "teapot": {
            List<ItemStack> in = patternInputs(o);
            ItemStack out = itemStack(s(o, "result"), i(o, "count", 1));
            int xp = i(o, "x_pos", 138), yp = i(o, "y_pos", 85);
            els.add(new PageElementTeapotRecipe(in, out, xp, yp));
            return xp >= 110;
         }
         case "brewing": case "potion": {
            List<ItemStack> in = patternInputs(o);
            ItemStack out = itemStack(s(o, "result"), i(o, "count", 1));
            els.add(new PageElementPotionRecipe(in, out));
            return true;
         }
         case "herbal_twine": {
            ItemStack in = itemStack(s(o, "input"), 1);
            ItemStack out = itemStack(s(o, "result"), i(o, "count", 1));
            int xp = i(o, "x_pos", 140), yp = i(o, "y_pos", 95);
            els.add(new PageElementHerbalTwineRecipe(in, out, xp, yp));
            return xp >= 110;
         }
         case "ritual": {
            ItemStack out = itemStack(s(o, "result"), 1);
            RecipeRitualCrafting recipe = out.isEmpty() ? null
               : com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.getRitualRecipe(out);
            if (recipe == null) return false;
            int xp = i(o, "x_pos", 132), yp = i(o, "y_pos", 47);
            els.add(new PageElementRitualRecipe(recipe, xp, yp, 0));
            return xp >= 110;
         }
         default: return false;
      }
   }

   private static com.paleimitations.schoolsofmagic.common.spells.Spell findSpell(String name) {
      if (name == null || name.isEmpty()) return null;
      String bare = name.contains(":") ? name.substring(name.indexOf(':') + 1) : name;
      for (com.paleimitations.schoolsofmagic.common.spells.Spell spell
           : com.paleimitations.schoolsofmagic.common.registries.SpellRegistry.SPELLS) {
         if (spell.getName().equalsIgnoreCase(bare)) return spell;
      }
      return null;
   }

   // Finds what the cauldron brews a given effect from, by scanning the brewing
   // recipes for one whose result carries that effect and taking its catalyst.
   private static ItemStack brewingIngredient(net.minecraft.world.effect.MobEffect effect) {
      for (net.minecraftforge.common.brewing.IBrewingRecipe recipe
           : net.minecraftforge.common.brewing.BrewingRecipeRegistry.getRecipes()) {
         if (!(recipe instanceof net.minecraftforge.common.brewing.BrewingRecipe brewing)) continue;
         ItemStack out = brewing.getOutput();
         if (out.isEmpty()) continue;
         boolean matches = false;
         for (net.minecraft.world.effect.MobEffectInstance e
              : net.minecraft.world.item.alchemy.PotionUtils.getMobEffects(out)) {
            if (e.getEffect() == effect) { matches = true; break; }
         }
         if (!matches) continue;
         ItemStack[] items = brewing.getIngredient().getItems();
         if (items.length > 0) return items[0].copy();
      }
      return ItemStack.EMPTY;
   }

   // Mirrors the potion pages: show how the brewing ingredient itself is made.
   private static void addIngredientRecipes(ItemStack ingredient, List<PageElement> els) {
      if (ingredient.isEmpty()) return;
      if (ingredient.getItem() == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.crushed_plant.get()) {
         int plantIdx = ingredient.getDamageValue();
         com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType[] plants =
            com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType.values();
         if (plantIdx < 0 || plantIdx >= plants.length) plantIdx = 0;
         els.add(new PageElementHerbalTwineRecipe(plants[plantIdx], 157, 91));
      }
      com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest mortar =
         com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.getMortarRecipe(ingredient);
      if (mortar != null) els.add(new PageElementMortarRecipe(mortar, 139, 126, 0));
      com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin basin =
         com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.getCatalystRecipe(ingredient);
      if (basin != null) els.add(new PageElementCatalystBasinRecipe(basin, 21, 50, 1));
   }

   private static boolean isRecipeKind(String k) {
      switch (k) {
         case "crafting": case "crafting_small": case "furnace": case "smelting":
         case "mortar": case "catalyst": case "catalyst_basin": case "tea": case "teapot":
         case "brewing": case "potion": case "herbal_twine": case "ritual":
            return true;
         default:
            return false;
      }
   }

   private static int defaultRecipeX(String k) {
      switch (k) {
         case "mortar": case "catalyst": case "catalyst_basin": case "tea": case "teapot":
            return 138;
         case "herbal_twine":
            return 140;
         case "ritual":
            return 132;
         default:
            return 150;
      }
   }

   private static List<ItemStack> patternInputs(JsonObject o) {
      List<ItemStack> list = Lists.newArrayList();
      if (o.has("pattern") && o.get("pattern").isJsonArray()) {
         for (JsonElement e : o.getAsJsonArray("pattern")) {
            list.add(e.isJsonPrimitive() ? itemStack(e.getAsString(), 1) : ItemStack.EMPTY);
         }
      }
      return list;
   }

   private static List<ItemStack> itemList(JsonObject o) {
      List<ItemStack> list = Lists.newArrayList();
      if (o.has("items") && o.get("items").isJsonArray()) {
         for (JsonElement e : o.getAsJsonArray("items")) {
            if (e.isJsonPrimitive()) {
               ItemStack st = itemStack(e.getAsString(), 1);
               if (!st.isEmpty()) list.add(st);
            }
         }
      } else {
         ItemStack st = itemStack(s(o, "item"), 1);
         if (!st.isEmpty()) list.add(st);
      }
      return list;
   }

   private static ItemStack itemStack(String id, int count) {
      if (id == null || id.isEmpty()) return ItemStack.EMPTY;
      Item it = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
      if (it == null) return ItemStack.EMPTY;
      ItemStack st = new ItemStack(it);
      st.setCount(Math.max(1, count));
      return st;
   }

   private static String[][] readPattern(JsonObject o) {
      if (!o.has("pattern") || !o.get("pattern").isJsonArray()) return new String[0][];
      JsonArray arr = o.getAsJsonArray("pattern");
      String[][] layers = new String[arr.size()][];
      for (int li = 0; li < arr.size(); li++) {
         JsonArray layer = arr.get(li).getAsJsonArray();
         String[] rows = new String[layer.size()];
         for (int ri = 0; ri < layer.size(); ri++) rows[ri] = layer.get(ri).getAsString();
         layers[li] = rows;
      }
      return layers;
   }
}
