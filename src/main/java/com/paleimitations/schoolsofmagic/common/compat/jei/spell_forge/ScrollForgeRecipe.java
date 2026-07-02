package com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public class ScrollForgeRecipe {
   public final List<ItemStack> grid;
   public final ItemStack parchment;
   public final ItemStack output;

   public ScrollForgeRecipe(List<ItemStack> grid, ItemStack parchment, ItemStack output) {
      this.grid = grid;
      this.parchment = parchment;
      this.output = output;
   }
}
