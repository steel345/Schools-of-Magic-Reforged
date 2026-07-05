package com.paleimitations.schoolsofmagic.common.compat.jei.cauldron_brew;

import java.util.List;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.util.References;
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

public class CauldronBrewCategory implements IRecipeCategory<CauldronBrewRecipe> {

   public static final RecipeType<CauldronBrewRecipe> TYPE =
      RecipeType.create(References.MODID, "cauldron_brew", CauldronBrewRecipe.class);
   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/cauldron_gui.png");

   private static final int[][] SLOTS = {
      {60, 73}, {37, 58}, {11, 44}, {26, 20}, {54, 26},
      {80, 41}, {107, 47}, {118, 23}, {93, 12}
   };

   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;

   public CauldronBrewCategory(IGuiHelper helper) {
      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 142, 138).setTextureSize(256, 256).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.cauldron.get()));
      this.title = Component.literal("Cauldron Crafting");
   }

   @Override
   public RecipeType<CauldronBrewRecipe> getRecipeType() {
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
   public void setRecipe(IRecipeLayoutBuilder builder, CauldronBrewRecipe recipe, IFocusGroup focuses) {
      List<ItemStack> inputs = recipe.getInputs();
      for (int i = 0; i < inputs.size() && i < SLOTS.length; i++) {
         ItemStack in = inputs.get(i);
         if (in != null && !in.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, SLOTS[i][0], SLOTS[i][1]).addItemStack(in);
         }
      }
      if (recipe.getOutput() != null && !recipe.getOutput().isEmpty()) {
         builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 100).addItemStack(recipe.getOutput());
      }
   }

   @Override
   public void draw(CauldronBrewRecipe recipe, IRecipeSlotsView slots, GuiGraphics gg, double mouseX, double mouseY) {
   }
}
