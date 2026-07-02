package com.paleimitations.schoolsofmagic.common.books;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PageElementTeapotRecipe extends PageElement {
   public static final ResourceLocation TEXTURE = new ResourceLocation("som", "textures/gui/teapot_jei.png");
   private static final int[][] INPUT_SLOTS = { {59, 21}, {59, 42}, {59, 63} };
   private static final int[] OUTPUT_SLOT = {6, 42};

   public final List<ItemStack> inputs;
   public final ItemStack output;

   public PageElementTeapotRecipe(List<ItemStack> inputs, ItemStack output, int x, int y) {
      super(x, y);
      this.inputs = inputs;
      this.output = output;
   }

   @OnlyIn(Dist.CLIENT)
   @Override
   public void drawElement(GuiGraphics gg, float mouseX, float mouseY, int xIn, int yIn, boolean isGUI, int target) {
      int px = this.x + xIn;
      int py = this.y + yIn;
      this.drawTexturedModalRect(gg, TEXTURE, px, py, 0, 0, 116, 98);
      for (int i = 0; i < INPUT_SLOTS.length && i < this.inputs.size(); ++i) {
         ItemStack s = this.inputs.get(i);
         if (s != null && !s.isEmpty()) {
            this.drawItemStack(gg, s, px + INPUT_SLOTS[i][0] + 1, py + INPUT_SLOTS[i][1] + 1, isGUI);
         }
      }
      if (this.output != null && !this.output.isEmpty()) {
         this.drawItemStack(gg, this.output, px + OUTPUT_SLOT[0] + 1, py + OUTPUT_SLOT[1] + 1, isGUI);
      }
   }
}
