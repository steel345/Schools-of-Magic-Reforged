package com.paleimitations.schoolsofmagic.common.compat.jei.drying;

import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.tileentity.TileEntityHerbalTwine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public final class DryingRecipeMaker {
   private DryingRecipeMaker() {}

   public static List<DryingRecipe> getRecipes() {
      List<ItemStack> inputs = new ArrayList<>();

      for (EnumMagicType e : EnumMagicType.values()) {
         ItemStack s = new ItemStack(BlockRegistry.magic_plant.get());
         CompoundTag bs = new CompoundTag();
         bs.putString("type", e.name().toLowerCase());
         s.getOrCreateTag().put("BlockStateTag", bs);
         inputs.add(s);
      }

      for (EnumMagicType e : EnumMagicType.values()) {
         ItemStack s = new ItemStack(ItemRegistry.seed_magic_plant.get());
         s.setDamageValue(e.ordinal());
         inputs.add(s);
      }

      inputs.add(new ItemStack(ItemRegistry.item_brittle_leaves.get()));
      inputs.add(new ItemStack(ItemRegistry.item_creosote_leaves.get()));

      inputs.add(new ItemStack(Blocks.PEONY));
      inputs.add(new ItemStack(Blocks.ROSE_BUSH));
      inputs.add(new ItemStack(Blocks.SUNFLOWER));
      inputs.add(new ItemStack(Blocks.LILAC));
      inputs.add(new ItemStack(Items.CARROT));
      inputs.add(new ItemStack(Items.WHEAT));
      inputs.add(new ItemStack(Items.SUGAR_CANE));

      inputs.add(new ItemStack(BlockRegistry.plant_rose.get()));
      inputs.add(new ItemStack(BlockRegistry.bush.get()));
      inputs.add(new ItemStack(BlockRegistry.plant_sage.get()));
      inputs.add(new ItemStack(BlockRegistry.hydrangea.get()));
      inputs.add(new ItemStack(BlockRegistry.plant_beanstalk.get()));
      inputs.add(new ItemStack(BlockRegistry.leaves_palm.get()));
      inputs.add(new ItemStack(BlockRegistry.plant_mistletoe.get()));
      inputs.add(new ItemStack(BlockRegistry.plant_oleander.get()));
      inputs.add(new ItemStack(BlockRegistry.plant_valleylily.get()));
      inputs.add(new ItemStack(BlockRegistry.plant_venus.get()));

      inputs.add(new ItemStack(ItemRegistry.item_bladderwort.get()));
      inputs.add(new ItemStack(ItemRegistry.item_cattail.get()));

      List<DryingRecipe> recipes = new ArrayList<>();
      for (ItemStack in : inputs) {
         ItemStack out = TileEntityHerbalTwine.getDriedItem(in);
         if (out != null && !out.isEmpty()) {
            recipes.add(new DryingRecipe(in, out));
         }
      }
      return recipes;
   }
}
