package com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.util.References;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ScrollForgeCategory implements IRecipeCategory<ScrollForgeRecipe> {

   public static final RecipeType<ScrollForgeRecipe> TYPE =
      RecipeType.create(References.MODID, "scroll_forge", ScrollForgeRecipe.class);

   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/spell_forge_jei.png");

   private static final int[][] GRID = {
      {50, 52}, {68, 52}, {86, 52},
      {50, 70}, {68, 70}, {86, 70},
      {50, 88}, {68, 88}, {86, 88}
   };
   private static final int PARCHMENT_X = 68;
   private static final int PARCHMENT_Y = 15;
   private static final int OUTPUT_X = 126;
   private static final int OUTPUT_Y = 60;

   private final IDrawable background;
   private final IDrawable icon;
   private final IDrawableStatic slot;
   private final Component title;

   public ScrollForgeCategory(IGuiHelper helper) {
      this.background = helper.drawableBuilder(TEXTURE, 12, 0, 160, 122).setTextureSize(256, 256).build();
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.spell_forge.get()));
      this.slot = helper.getSlotDrawable();
      this.title = Component.literal("Spell Scrolls");
   }

   @Override
   public RecipeType<ScrollForgeRecipe> getRecipeType() {
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
   public void draw(ScrollForgeRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView slots,
                    GuiGraphics graphics, double mouseX, double mouseY) {
      this.slot.draw(graphics, PARCHMENT_X - 1, PARCHMENT_Y - 1);
      this.slot.draw(graphics, OUTPUT_X - 1, OUTPUT_Y - 1);
   }

   @Override
   public void setRecipe(IRecipeLayoutBuilder builder, ScrollForgeRecipe recipe, IFocusGroup focuses) {
      for (int i = 0; i < 9 && i < recipe.grid.size(); i++) {
         builder.addSlot(RecipeIngredientRole.INPUT, GRID[i][0], GRID[i][1]).addItemStack(recipe.grid.get(i));
      }
      builder.addSlot(RecipeIngredientRole.INPUT, PARCHMENT_X, PARCHMENT_Y).addItemStack(recipe.parchment);
      builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y).addItemStack(recipe.output);
   }
}
