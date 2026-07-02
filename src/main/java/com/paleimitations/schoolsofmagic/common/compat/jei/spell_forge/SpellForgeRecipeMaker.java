package com.paleimitations.schoolsofmagic.common.compat.jei.spell_forge;

import com.paleimitations.schoolsofmagic.common.items.IItemMetaHandler;
import com.paleimitations.schoolsofmagic.common.items.ItemPlant;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNoteHelper;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

public class SpellForgeRecipeMaker {
   private SpellForgeRecipeMaker() {}

   public static List<SpellForgePointsRecipe> getRecipes() {
      List<ItemStack> inputs = new ArrayList<>();

      addVariants(inputs, ItemRegistry.seed_magic_plant);
      addVariants(inputs, ItemRegistry.crushed_plant);
      addVariants(inputs, ItemRegistry.gem_dust);

      inputs.add(new ItemStack(ItemRegistry.wand_core.get()));
      inputs.add(new ItemStack(ItemRegistry.crushed_horn_unicorn.get()));
      inputs.add(new ItemStack(ItemRegistry.shard_netherstar.get()));
      inputs.add(new ItemStack(ItemRegistry.bottle_egg.get()));
      inputs.add(new ItemStack(Items.NETHER_WART));

      for (RegistryObject<Item> ro : ItemRegistry.FAIRY_DUSTS) {
         if (ro != null && ro.isPresent()) inputs.add(new ItemStack(ro.get()));
      }

      List<SpellForgePointsRecipe> recipes = new ArrayList<>();
      for (ItemStack in : inputs) {
         ItemStack note = SpellNoteHelper.makeNoteFor(in);
         if (note != null) {
            recipes.add(new SpellForgePointsRecipe(in, note));
         }
      }
      return recipes;
   }

   private static void addVariants(List<ItemStack> out, RegistryObject<Item> ro) {
      if (ro == null || !ro.isPresent()) return;
      Item item = ro.get();
      if (item instanceof IItemMetaHandler handler) {
         int count = Math.max(1, handler.getDamage());
         int start = (item instanceof ItemPlant) ? 1 : 0;
         for (int i = start; i < count; i++) {
            ItemStack s = new ItemStack(item);
            s.setDamageValue(i);
            out.add(s);
         }
      } else {
         out.add(new ItemStack(item));
      }
   }
}
