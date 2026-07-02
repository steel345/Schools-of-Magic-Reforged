package com.paleimitations.schoolsofmagic.common.compat.jei.drying;

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

public class DryingRecipeCategory implements IRecipeCategory<DryingRecipe> {

   public static final RecipeType<DryingRecipe> TYPE =
      RecipeType.create(References.MODID, "drying", DryingRecipe.class);
   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/herbal_twine_jei.png");

   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;

   public DryingRecipeCategory(IGuiHelper helper) {
      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 52, 31).setTextureSize(256, 256).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.herbal_twine.get()));
      this.title = Component.literal("Herbal Twine");
   }

   @Override
   public RecipeType<DryingRecipe> getRecipeType() {
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
   public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focuses) {
      if (recipe.getInput() != null && !recipe.getInput().isEmpty()) {
         builder.addSlot(RecipeIngredientRole.INPUT, 1, 8).addItemStack(recipe.getInput());
      }
      if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
         builder.addSlot(RecipeIngredientRole.OUTPUT, 35, 8).addItemStack(recipe.getOutput());
      }
   }
}
