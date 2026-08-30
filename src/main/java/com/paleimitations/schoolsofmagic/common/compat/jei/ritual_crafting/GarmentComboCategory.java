package com.paleimitations.schoolsofmagic.common.compat.jei.ritual_crafting;

import java.util.List;

import com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment;
import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeRitualCrafting;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

// the same page a ring gets, told twice over for the other two
public class GarmentComboCategory implements IRecipeCategory<RecipeRitualCrafting> {
   public static final RecipeType<RecipeRitualCrafting> CROWN =
      RecipeType.create(References.MODID, "crown_combinations", RecipeRitualCrafting.class);
   public static final RecipeType<RecipeRitualCrafting> NECKLACE =
      RecipeType.create(References.MODID, "necklace_combinations", RecipeRitualCrafting.class);

   private static final ResourceLocation TEXTURE =
      new ResourceLocation(References.MODID, "textures/gui/ritual_jei.png");

   private static final int[][] SLOTS = {
      {46, 63}, {25, 52}, {4, 45}, {2, 24}, {23, 17}, {44, 23}, {65, 33}, {86, 23}, {80, 2}
   };

   private final RecipeType<RecipeRitualCrafting> type;
   private final IDrawable background;
   private final IDrawable icon;
   private final Component title;
   private final ItemStack shown;

   public GarmentComboCategory(IGuiHelper helper, RecipeType<RecipeRitualCrafting> type,
                               Item item, String name) {
      this.type = type;
      this.background = helper.drawableBuilder(TEXTURE, 0, 0, 104, 140).setTextureSize(256, 256).build();
      this.shown = build(item, name);
      this.icon = helper.createDrawableItemStack(this.shown);
      this.title = Component.literal(name + " Combinations");
   }

   private static ItemStack build(Item item, String name) {
      ItemStack stack = ItemAdvancedGarment.of(item,
         IWandData.EnumHandleType.GOLD, IWandData.EnumGemType.SAPPHIRE);
      stack.setHoverName(Component.literal("Magic " + name).withStyle(s -> s.withItalic(false)));
      return stack;
   }

   public static Item itemFor(RecipeType<RecipeRitualCrafting> type) {
      return type == CROWN ? ItemRegistry.advanced_crown.get() : ItemRegistry.advanced_necklace.get();
   }

   @Override
   public RecipeType<RecipeRitualCrafting> getRecipeType() {
      return this.type;
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
         builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 44, 105).addItemStack(out);
      }

      builder.addSlot(RecipeIngredientRole.OUTPUT, 2, 2).addItemStack(this.shown);
   }

   @Override
   public void draw(RecipeRitualCrafting recipe, IRecipeSlotsView slots, GuiGraphics gg, double mouseX, double mouseY) {
   }
}
