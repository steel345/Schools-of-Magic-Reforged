package com.paleimitations.schoolsofmagic.common.recipes;

import com.paleimitations.imitationcore.common.utils.NBTUtils;
import java.util.Locale;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

final class IngredientFactory {

   private IngredientFactory() {
   }

   static Ingredient of(Object obj) {

      if (obj instanceof Ingredient ing) {
         return ing;
      }
      if (obj instanceof ItemStack stack) {
         return Ingredient.of(stack);
      }
      if (obj instanceof Item item) {
         return Ingredient.of((ItemLike) item);
      }
      if (obj instanceof Block block) {
         return Ingredient.of((ItemLike) block);
      }
      if (obj instanceof String name) {
         Ingredient mod = modMaterial(name);
         if (mod != null) {
            return mod;
         }
         return Ingredient.of(oreNameToTag(name));
      }
      return Ingredient.EMPTY;
   }

   private static Ingredient modMaterial(String name) {
      switch (name) {

         case "ingotSilver":  return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot, 0));
         case "ingotCopper":  return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot, 2));
         case "ingotBronze":  return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot, 4));
         case "ingotBrass":   return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot, 6));
         case "ingotSteel":   return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot, 8));

         case "ingotObsidian": return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.ingot, 9));

         case "blockGlass":   return Ingredient.of(
            net.minecraft.world.level.block.Blocks.GLASS,
            net.minecraft.world.level.block.Blocks.TINTED_GLASS,
            net.minecraft.world.level.block.Blocks.WHITE_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.ORANGE_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.MAGENTA_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.LIGHT_BLUE_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.YELLOW_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.LIME_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.PINK_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.GRAY_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.LIGHT_GRAY_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.CYAN_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.PURPLE_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.BLUE_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.BROWN_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.GREEN_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.RED_STAINED_GLASS,
            net.minecraft.world.level.block.Blocks.BLACK_STAINED_GLASS);
         case "nuggetSilver": return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.nugget, 0));
         case "nuggetCopper": return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.nugget, 2));
         case "nuggetIron":   return Ingredient.of(new ItemStack(net.minecraft.world.item.Items.IRON_NUGGET));

         case "dustRuby":        return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.gem_dust, 0));
         case "dustSunstone":    return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.gem_dust, 1));
         case "dustTurquoise":   return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.gem_dust, 5));
         case "dustSmokyQuartz": return Ingredient.of(metaStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.gem_dust, 15));
         case "dustDiamond":     return Ingredient.of(new ItemStack(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.item_diamond_dust.get()));
         case "dustPrismarine":  return Ingredient.of(new ItemStack(net.minecraft.world.item.Items.PRISMARINE_CRYSTALS));

         case "gemRuby":         return Ingredient.of(gemPolished(com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_pyromancy));
         case "gemAquamarine":   return Ingredient.of(gemPolished(com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_hydromancy));
         case "oreRoseQuartz":   return Ingredient.of(gemPolished(com.paleimitations.schoolsofmagic.common.registries.BlockRegistry.gem_auramancy));
         case "ingotBrickNether":return Ingredient.of(new ItemStack(net.minecraft.world.item.Items.NETHER_BRICK));
         default: return null;
      }
   }

   private static ItemStack metaStack(net.minecraftforge.registries.RegistryObject<Item> ro, int meta) {
      ItemStack s = new ItemStack(ro.get());
      s.setDamageValue(meta);
      return s;
   }

   private static ItemStack gemPolished(net.minecraftforge.registries.RegistryObject<net.minecraft.world.level.block.Block> ro) {
      ItemStack s = new ItemStack(ro.get());
      net.minecraft.nbt.CompoundTag bs = new net.minecraft.nbt.CompoundTag();
      bs.putString("type", "polished");
      s.getOrCreateTag().put("BlockStateTag", bs);
      return s;
   }

   static TagKey<Item> oreNameToTag(String oreName) {

      switch (oreName) {
         case "plankWood": return ItemTags.create(new ResourceLocation("minecraft", "planks"));
         case "stickWood": return ItemTags.create(new ResourceLocation("forge", "rods/wooden"));
         case "logWood":   return ItemTags.create(new ResourceLocation("minecraft", "logs"));
         default: break;
      }
      int idx = 0;
      while (idx < oreName.length() && !Character.isUpperCase(oreName.charAt(idx))) {
         idx++;
      }
      String prefix = oreName.substring(0, idx).toLowerCase(Locale.ROOT);
      String material = idx < oreName.length() ? oreName.substring(idx).toLowerCase(Locale.ROOT) : "";
      String path;
      if (material.isEmpty()) {

         switch (prefix) {
            case "slimeball": path = "slimeballs"; break;
            case "bone":      path = "bones";      break;
            case "egg":       path = "eggs";       break;
            case "feather":   path = "feathers";   break;

            case "plankwood":
            case "planks":
               path = "planks";   break;
            default:          path = prefix;       break;
         }
      } else {
         String category = prefix.equals("block") ? "storage_blocks" : prefix + "s";
         path = category + "/" + material;
      }
      return ItemTags.create(new ResourceLocation("forge", path));
   }

   static boolean compareStacks(Ingredient recipe, ItemStack supplied) {
      ItemStack[] items = recipe.getItems();
      if (items.length == 0) {
         return supplied.isEmpty();
      }
      boolean anyTagged = false;
      boolean sameItemPresent = false;
      for (ItemStack stack : items) {
         if (matchesStack(stack, supplied)) {
            return true;
         }
         if (stack.getTag() != null && stack.getTag().contains("BlockStateTag")) {
            anyTagged = true;
         }
         if (net.minecraft.world.item.ItemStack.isSameItem(stack, supplied)) {
            sameItemPresent = true;
         }
      }

      if (anyTagged || sameItemPresent) {
         return false;
      }
      return recipe.test(supplied);
   }

   private static boolean matchesStack(ItemStack stack, ItemStack supplied) {
      if (stack.isEmpty()) {
         return supplied.isEmpty();
      }
      if (!ItemStack.isSameItem(stack, supplied) || stack.getDamageValue() != supplied.getDamageValue()) {
         return false;
      }

      net.minecraft.nbt.CompoundTag rTag = stack.getTag();
      if (rTag != null && rTag.contains("BlockStateTag")) {
         net.minecraft.nbt.CompoundTag sTag = supplied.getTag();
         net.minecraft.nbt.CompoundTag suppliedBs = (sTag != null)
            ? sTag.getCompound("BlockStateTag") : new net.minecraft.nbt.CompoundTag();
         return rTag.getCompound("BlockStateTag").equals(suppliedBs);
      }
      return true;
   }
}
