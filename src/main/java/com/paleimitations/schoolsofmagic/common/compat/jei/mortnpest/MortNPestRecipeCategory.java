package com.paleimitations.schoolsofmagic.common.compat.jei.mortnpest;

import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.util.References;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MortNPestRecipeCategory implements IRecipeCategory<RecipeMortNPest> {

   public static final RecipeType<RecipeMortNPest> TYPE =
      RecipeType.create(References.MODID, "mort_n_pest", RecipeMortNPest.class);
   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/mortnpest_jei.png");

   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;

   public MortNPestRecipeCategory(IGuiHelper helper) {
      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 96, 73).setTextureSize(256, 256).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.mortnpest.get()));
      this.title = Component.literal("Mortar & Pestle");
   }

   @Override
   public RecipeType<RecipeMortNPest> getRecipeType() {
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
   public void setRecipe(IRecipeLayoutBuilder builder, RecipeMortNPest recipe, IFocusGroup focuses) {

      Ingredient in1 = recipe.getInput();
      if (in1 != null && !in1.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.INPUT, 6, 17).addIngredients(in1);
      }
      Ingredient in2 = recipe.getInputSecondary();
      if (in2 != null && !in2.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.INPUT, 6, 40).addIngredients(in2);
      }
      ItemStack out1 = recipe.getOutput();
      if (out1 != null && !out1.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 17).addItemStack(out1);
      }
      ItemStack out2 = recipe.getOutputSecondary();
      if (out2 != null && !out2.isEmpty()) {
         builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 40).addItemStack(out2);
      }
   }
}
