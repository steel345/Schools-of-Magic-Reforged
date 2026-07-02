package com.paleimitations.schoolsofmagic.common.books;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementCraftingRecipeSmall extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/books/crafting_recipe_small.png");
   public final List<ItemStack> inputs;
   public final ItemStack output;

   public PageElementCraftingRecipeSmall(List<ItemStack> inputs, ItemStack output, int x, int y) {
      super(x, y);
      this.inputs = inputs;
      this.output = output;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      this.drawTexturedModalRect(gg, TEXTURE, this.x + xIn, this.y + yIn, 0, 0, 60, 36);
      int[][] slots = {{1,1},{1,19},{19,1},{19,19}};
      for (int i = 0; i < slots.length && i < this.inputs.size(); ++i) {
         ItemStack s = this.inputs.get(i);
         if (s != null && !s.isEmpty()) {
            this.drawItemStack(gg, s, this.x + xIn + slots[i][0], this.y + yIn + slots[i][1], isGUI);
         }
      }
      this.drawItemStack(gg, this.output, this.x + xIn + 41, this.y + yIn + 10, isGUI);
   }
}
