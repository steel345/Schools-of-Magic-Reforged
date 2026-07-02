package com.paleimitations.schoolsofmagic.common.compat.jei.catalyst_basin;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeCatalystBasin;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import com.paleimitations.schoolsofmagic.common.util.References;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class CatalystBasinRecipeCategory implements IRecipeCategory<RecipeCatalystBasin> {

   public static final RecipeType<RecipeCatalystBasin> TYPE =
      RecipeType.create(References.MODID, "catalyst_basin", RecipeCatalystBasin.class);
   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/catalyst_basin_jei.png");

   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;

   public CatalystBasinRecipeCategory(IGuiHelper helper) {

      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 104, 140).setTextureSize(256, 257).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.catalyst_basin.get()));
      this.title = Component.literal("Catalyst Basin");
   }

   @Override
   public RecipeType<RecipeCatalystBasin> getRecipeType() {
      return TYPE;
   }

   @Override
   public Component getTitle() {
      return this.title;
   }

   @Override
   public IDrawable getBackground() {
      return this.background;
   }

   @Override
   public IDrawable getIcon() {
      return this.icon;
   }

   @Override
   public void setRecipe(IRecipeLayoutBuilder builder, RecipeCatalystBasin recipe, IFocusGroup focuses) {

      addInput(builder, recipe.getSubstrate(), 14, 2);
      addInput(builder, recipe.getCatalyst(),  14, 21);
      addInput(builder, recipe.getSolution(),  74, 2);
      addInput(builder, recipe.getReactant(),  74, 21);
      ItemStack out = recipe.getOutput();
      if (out != null && !out.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.OUTPUT, 14, 66).addItemStack(out);
      }
   }

   @Override
   public void draw(RecipeCatalystBasin recipe, IRecipeSlotsView slots, GuiGraphics gg, double mouseX, double mouseY) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      int tick = (mc.player != null) ? mc.player.tickCount : 0;

      drawCycling(gg, recipe.getCatalyst(), 14, 85, tick);
      drawCycling(gg, recipe.getSolution(), 74, 66, tick);

      int mana = Math.round(recipe.getManaCost());
      if (mana > 0) {
         String s = String.valueOf(mana);
         gg.drawString(font, s, 87 - font.width(s) / 2, 47, Color.GRAY.getRGB(), false);

         Map<IMagicType, Integer> map = Maps.newHashMap();
         for (int i = 0; i < 6; i++) {
            if (i < recipe.getSchoolLevels().length && recipe.getSchoolLevels()[i] > 0) {
               map.put(MagicSchoolRegistry.getSchoolFromId(i), recipe.getSchoolLevels()[i]);
            }
         }
         for (int i = 0; i < 16; i++) {
            if (i < recipe.getElementLevels().length && recipe.getElementLevels()[i] > 0) {
               map.put(MagicElementRegistry.getElementFromId(i), recipe.getElementLevels()[i]);
            }
         }
         if (!map.isEmpty()) {
            List<IMagicType> keys = Lists.newArrayList(map.keySet());
            IMagicType type = keys.get(tick / 30 % keys.size());
            int level = map.get(type);

            Component line1 = Component.translatable("gui.jei.catalyst_basin.requires");
            Component line2 = Component.translatable(type.getFormattedName());
            Component line3 = Component.translatable("gui.jei.catalyst_basin.skill")
               .append(Component.literal(" " + (level + 1)));
            gg.pose().pushPose();
            gg.pose().scale(0.8F, 0.8F, 1.0F);
            int ty = 132;
            gg.drawString(font, line1, 16, ty, Color.DARK_GRAY.getRGB(), false);
            gg.drawString(font, line2, 16, ty + font.lineHeight, Color.DARK_GRAY.getRGB(), false);
            gg.drawString(font, line3, 16, ty + 2 * font.lineHeight, Color.DARK_GRAY.getRGB(), false);
            gg.pose().popPose();
         }
      }
   }

   private static void drawCycling(GuiGraphics gg, Ingredient ing, int x, int y, int tick) {
      if (ing == null || ing.isEmpty()) return;
      ItemStack[] items = ing.getItems();
      if (items.length == 0) return;
      List<ItemStack> list = new ArrayList<>();
      for (ItemStack s : items) if (s != null && !s.isEmpty()) list.add(s);
      if (list.isEmpty()) return;
      gg.renderItem(list.get(tick / 20 % list.size()), x, y);
   }

   private static void addInput(IRecipeLayoutBuilder builder, Ingredient ing, int x, int y) {
      if (ing != null && !ing.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ing);
      }
   }
}
