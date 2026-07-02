package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.IMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import net.minecraft.world.item.ItemStack;

public class BookPagePlantDescription extends BookPage {
   public final EnumPlantType plantType;
   public final IMagicType type;

   private static String resolveTitleKey(EnumPlantType plantType) {

      switch (plantType) {
         case PYROMANCY: case HELIOMANCY: case AEROMANCY: case GEOMANCY:
         case ANIMANCY: case ELECTROMANCY: case HYDROMANCY: case CRYOMANCY:
         case HIEROMANCY: case CHAOTICS: case AURAMANCY: case ASTROMANCY:
         case INFERNALITY: case SPECTROMANCY: case UMBRAMANCY: case NECROMANCY:
            return "block.som.magic_plant." + plantType.getSerializedName();

         case FIREBERRY:   return "item.som.seed_magic_plant_pyromancy";
         case MANDRAKE:    return "item.som.seed_magic_plant_animancy";
         case NIGHTBERRY:  return "item.som.seed_magic_plant_umbramancy";
         case GRAVEROOT:   return "item.som.seed_magic_plant_necromancy";

         default:
            return PageElementHerbalTwineRecipe.getUndried(plantType).getDescriptionId();
      }
   }

   public BookPagePlantDescription(final EnumPlantType plantType) {
      super(
         "plant_" + plantType.getSerializedName(),
         Lists.newArrayList(new PageElement[]{
            new PageElementStandardText(resolveTitleKey(plantType), 72, 55, 99, 10, 0, true),
            new PageElementItemStack(PageElementHerbalTwineRecipe.getUndried(plantType), 64, 62),
            new PageElementParagraphs("plant_" + plantType.getSerializedName(), 0.75F, 0, 1,
               new ParagraphBox(23, 82, 0, 99, 108),
               new ParagraphBox(134, 50, 0, 99, 45),
               new ParagraphBox(23, 50, 1, 99, 140),
               new ParagraphBox(23, 50, 2, 99, 140)),
            new PageElementMortarRecipe(RecipeRegistry.getMortarRecipe(plantType), 139, 126),
            new PageElementHerbalTwineRecipe(plantType, 157, 91),
            new PageElementWorldConnector()
         })
      );
      this.plantType = plantType;
      this.type = plantType.getType();
      int i = 0;

      for (RecipeMortNPest recipe : RecipeRegistry.getMortarRecipeByInput(PageElementHerbalTwineRecipe.getUndried(plantType))) {
         this.addElement(new PageElementMortarRecipe(recipe, i / 2 == 0 ? 139 : 28, i % 2 == 0 ? 50 : 126, 1 + i / 4));
         i++;
      }

      if (plantType.getIndex() < 18 && plantType.getIndex() > 0) {

         ItemStack seedKey = new ItemStack(ItemRegistry.seed_magic_plant.get());
         seedKey.setDamageValue(plantType.getIndex() - 1);
         for (RecipeMortNPest recipe : RecipeRegistry.getMortarRecipeByInput(seedKey)) {
            this.addElement(new PageElementMortarRecipe(recipe, i / 2 == 0 ? 139 : 28, i % 2 == 0 ? 50 : 126, 1 + i / 4));
            i++;
         }
      }
   }
}
