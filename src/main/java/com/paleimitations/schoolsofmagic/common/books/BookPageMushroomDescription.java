package com.paleimitations.schoolsofmagic.common.books;

import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BookPageMushroomDescription extends BookPageItemDescription {
   public BookPageMushroomDescription(ItemStack stack) {
      super(stack);
      if (stack.getItem() == BlockRegistry.mushroom_crop_dark.get().asItem()) {
         this.addElement(new PageElementItemStack(new ItemStack(ItemRegistry.seed_mushroom_dark.get()), 46, 62));
         this.addElement(new PageElementItemStack(new ItemStack(BlockRegistry.mushroom_dark.get()), 82, 62));
      }
      if (stack.getItem() == BlockRegistry.mushroom_crop_grey.get().asItem()) {
         this.addElement(new PageElementItemStack(new ItemStack(ItemRegistry.seed_mushroom_grey.get()), 46, 62));
         this.addElement(new PageElementItemStack(new ItemStack(BlockRegistry.mushroom_grey.get()), 82, 62));
      }
      if (stack.getItem() == BlockRegistry.mushroom_crop_white.get().asItem()) {
         this.addElement(new PageElementItemStack(new ItemStack(ItemRegistry.seed_mushroom_white.get()), 46, 62));
         this.addElement(new PageElementItemStack(new ItemStack(BlockRegistry.mushroom_white.get()), 82, 62));
      }
      if (stack.getItem() == BlockRegistry.mushroom_crop_pink.get().asItem()) {
         this.addElement(new PageElementItemStack(new ItemStack(ItemRegistry.seed_mushroom_pink.get()), 46, 62));
         this.addElement(new PageElementItemStack(new ItemStack(BlockRegistry.mushroom_pink.get()), 82, 62));
      }
   }
}
