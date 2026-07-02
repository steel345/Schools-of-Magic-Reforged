package com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
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

public class RitualRecipeCategory implements IRecipeCategory<RecipeRitualCrafting> {

   public static final RecipeType<RecipeRitualCrafting> TYPE =
      RecipeType.create(References.MODID, "ritual_crafting", RecipeRitualCrafting.class);
   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/ritual_jei.png");

   private static final int[][] SLOTS = {
      {46, 63}, {25, 52}, {4, 45}, {2, 24}, {23, 17}, {44, 23}, {65, 33}, {86, 23}, {80, 2}
   };

   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;

   public RitualRecipeCategory(IGuiHelper helper) {
      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 104, 140).setTextureSize(256, 256).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.brazier.get()));
      this.title = Component.literal("Ritual Crafting");
   }

   @Override
   public RecipeType<RecipeRitualCrafting> getRecipeType() {
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
   public void setRecipe(IRecipeLayoutBuilder builder, RecipeRitualCrafting recipe, IFocusGroup focuses) {
      List<Ingredient> inputs = recipe.getInputs();
      for (int i = 0; i < inputs.size() && i < SLOTS.length; i++) {
         Ingredient ing = inputs.get(i);
         if (ing != null && !ing.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, SLOTS[i][0], SLOTS[i][1]).addIngredients(ing);
         }
      }
      ItemStack out = recipe.getOutput();
      if (out != null && !out.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.OUTPUT, 44, 105).addItemStack(out);
      }
   }

   @Override
   public void draw(RecipeRitualCrafting recipe, IRecipeSlotsView slots, GuiGraphics gg, double mouseX, double mouseY) {
      Minecraft mc = Minecraft.getInstance();
      Font font = mc.font;
      int tick = (mc.player != null) ? mc.player.tickCount : 0;

      int mana = recipe.getManaUsage();
      if (mana <= 0) return;
      String s = String.valueOf(mana);
      gg.drawString(font, s, 87 - font.width(s) / 2, 65, Color.GRAY.getRGB(), false);

      Map<IMagicType, Integer> map = Maps.newHashMap();
      for (int i = 0; i < 6; i++) {
         if (i < recipe.getMinSchoolLevels().length && recipe.getMinSchoolLevels()[i] > 0) {
            map.put(MagicSchoolRegistry.getSchoolFromId(i), recipe.getMinSchoolLevels()[i]);
         }
      }
      for (int i = 0; i < 16; i++) {
         if (i < recipe.getMinElementLevels().length && recipe.getMinElementLevels()[i] > 0) {
            map.put(MagicElementRegistry.getElementFromId(i), recipe.getMinElementLevels()[i]);
         }
      }
      if (map.isEmpty()) return;
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
