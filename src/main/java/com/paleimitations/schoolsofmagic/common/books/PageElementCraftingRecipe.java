package com.paleimitations.schoolsofmagic.common.books;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementCraftingRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/crafting_recipe.png");
   public final List<ItemStack> inputs;
   public final ItemStack output;

   public PageElementCraftingRecipe(List<ItemStack> inputs, ItemStack output, int x, int y) {
      super(x, y);
      this.inputs = inputs;
      this.output = output;
   }

   public PageElementCraftingRecipe(List<ItemStack> inputs, ItemStack output, int x, int y, int target) {
      super(x, y, target);
      this.inputs = inputs;
      this.output = output;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 54, 76);
      int[][] slots = {{1,1},{19,1},{37,1},{1,19},{19,19},{37,19},{1,37},{19,37},{37,37}};
      for (int i = 0; i < slots.length && i < this.inputs.size(); ++i) {
         ItemStack s = this.inputs.get(i);
         if (s != null && !s.isEmpty()) {
            this.drawItemStack(gg, s, this.x + xIn + slots[i][0], this.y + yIn + slots[i][1], isGUI);
         }
      }
      this.drawItemStack(gg, this.output, this.x + xIn + 19, this.y + yIn + 59, isGUI);
   }
}
