package com.paleimitations.schoolsofmagic.common.compat.jei.tea;

import java.util.List;

import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.util.References;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class TeapotRecipeCategory implements IRecipeCategory<RecipeTea> {

   public static final RecipeType<RecipeTea> TYPE =
      RecipeType.create(References.MODID, "tea_making", RecipeTea.class);
   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/teapot_jei.png");

   private static final int[][] INPUT_SLOTS = { {59, 21}, {59, 42}, {59, 63} };

   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;

   public TeapotRecipeCategory(IGuiHelper helper) {
      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 116, 98).setTextureSize(256, 256).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.teapot.get()));
      this.title = Component.literal("Tea Making");
   }

   @Override
   public RecipeType<RecipeTea> getRecipeType() {
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
   public void setRecipe(IRecipeLayoutBuilder builder, RecipeTea recipe, IFocusGroup focuses) {
      List<Ingredient> inputs = recipe.getInputs();
      for (int i = 0; i < inputs.size() && i < INPUT_SLOTS.length; i++) {
         Ingredient ing = inputs.get(i);
         if (ing != null && !ing.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, INPUT_SLOTS[i][0], INPUT_SLOTS[i][1]).addIngredients(ing);
         }
      }

      ItemStack tea = TeaUtils.appendEffects(new ItemStack(ItemRegistry.teacup.get()), recipe.getEffect());
      builder.addSlot(RecipeIngredientRole.OUTPUT, 6, 42).addItemStack(tea);
   }

   @Override
   public void draw(RecipeTea recipe, IRecipeSlotsView slots, GuiGraphics gg, double mouseX, double mouseY) {

   }
}
