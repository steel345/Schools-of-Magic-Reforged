package com.paleimitations.schoolsofmagic.common.books;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public class PageElementData extends PageElement {
   public final float podiumGameScore;
   public final List<ItemStack> craftingInputs;

   public PageElementData(float podiumGameScore, List<ItemStack> craftingInputs) {
      super(0, 0);
      this.podiumGameScore = podiumGameScore;
      this.craftingInputs = craftingInputs;
   }
}
