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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SpellForgePointsCategory implements IRecipeCategory<SpellForgePointsRecipe> {

   public static final RecipeType<SpellForgePointsRecipe> TYPE =
      RecipeType.create(References.MODID, "spell_forge", SpellForgePointsRecipe.class);

   private static final int WIDTH = 92;
   private static final int HEIGHT = 26;
   private static final int INPUT_X = 4;
   private static final int FORGE_X = 30;
   private static final int ARROW_X = 50;
   private static final int OUTPUT_X = 70;
   private static final int SLOT_Y = 4;

   private final IDrawable background;
   private final IDrawable icon;
   private final IDrawableStatic slot;
   private final IDrawable forgeItem;
   private final Component title;

   public SpellForgePointsCategory(IGuiHelper helper) {
      this.background = helper.createBlankDrawable(WIDTH, HEIGHT);
      this.icon = helper.createDrawableItemStack(new ItemStack(BlockRegistry.spell_forge.get()));
      this.forgeItem = helper.createDrawableItemStack(new ItemStack(BlockRegistry.spell_forge.get()));
      this.slot = helper.getSlotDrawable();
      this.title = Component.literal("Spell Forge");
   }

   @Override
   public RecipeType<SpellForgePointsRecipe> getRecipeType() {
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
   public void draw(SpellForgePointsRecipe recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView slots,
                    GuiGraphics graphics, double mouseX, double mouseY) {

      this.slot.draw(graphics, INPUT_X - 1, SLOT_Y - 1);
      this.slot.draw(graphics, OUTPUT_X - 1, SLOT_Y - 1);

      this.forgeItem.draw(graphics, FORGE_X, SLOT_Y);

      graphics.drawString(Minecraft.getInstance().font, "→", ARROW_X, SLOT_Y + 4, 0xFF555555, false);
   }

   @Override
   public void setRecipe(IRecipeLayoutBuilder builder, SpellForgePointsRecipe recipe, IFocusGroup focuses) {
      builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X, SLOT_Y).addItemStack(recipe.getInput());
      builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, SLOT_Y).addItemStack(recipe.getNote());
   }
}
