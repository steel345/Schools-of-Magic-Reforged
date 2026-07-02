package com.paleimitations.schoolsofmagic.common.compat.config;

import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeMortNPest;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigRecipes {

   private static final Logger LOGGER = LogManager.getLogger("SOMConfigRecipes");
   public static final String NEW_LINE = System.getProperty("line.separator");

   public static File canonicalRecipeFolder;
   public static String canonicalRecipePath;
   public static File mortarAndPestleRecipeFile;
   public static File teaRecipeFile;

   public ConfigRecipes() {
   }

   public static void preinit() {
      File recipeFolder = new File(FMLPaths.CONFIGDIR.get().toFile(), "som_recipes");
      try {
         canonicalRecipeFolder = recipeFolder.getCanonicalFile();
         canonicalRecipePath = recipeFolder.getCanonicalPath();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }

      if (!canonicalRecipeFolder.exists()) {
         LOGGER.info("No mod directory found, creating one: {}", canonicalRecipePath);
         boolean dirMade = canonicalRecipeFolder.mkdir();
         if (!dirMade) {
            LOGGER.fatal("Unable to create the mod directory {}", canonicalRecipePath);
            throw new RuntimeException(String.format("Unable to create the mod directory %s", canonicalRecipePath));
         }
         LOGGER.info("Mod directory created successfully");
      }

      mortarAndPestleRecipeFile = new File(recipeFolder, "mortar_and_pestle.txt");
      teaRecipeFile = new File(recipeFolder, "tea.txt");
   }

   public static void init() {
      readTeaRecipeFile();
   }

   public static void readMortarRecipeFile() {
      try {
         if (mortarAndPestleRecipeFile.createNewFile() && mortarAndPestleRecipeFile.canWrite()) {
            FileOutputStream fos = new FileOutputStream(mortarAndPestleRecipeFile);
            BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            buffer.write("# Mortar & Pestle file");
            buffer.newLine();
            buffer.write("# notes: Each line represents one recipe. All outputs must be in ItemStack format." + NEW_LINE);
            buffer.write("         Standard formatting is: output, secondary output, number of pestle crushes, input, secondary input." + NEW_LINE);
            buffer.write("         The secondary input is the bottle input and the secondary output places in the output bottle slot." + NEW_LINE);
            buffer.write("         When describing an itemstack, count and data tags are optional." + NEW_LINE);
            buffer.write("         If a slot is not used in the recipe, do not include it in the recipe line." + NEW_LINE + NEW_LINE);
            buffer.write("# Default recipes" + NEW_LINE);

            for (RecipeMortNPest recipe : RecipeRegistry.mortnpestRecipes) {
               String s = "<start_recipe>  ";
               if (!recipe.getOutput().isEmpty()) {
                  s = s + generateStringFromItemstack("<output>", recipe.getOutput());
               }
               if (!recipe.getOutputSecondary().isEmpty()) {
                  s = s + generateStringFromItemstack("<output_secondary>", recipe.getOutputSecondary());
               }
               s = s + "<crush>" + recipe.getCrush() + ";  ";
               if (recipe.getInput().getItems().length > 0) {
                  s = s + generateStringFromObject("<input>", recipe.getInputO());
               }
               if (recipe.getInputSecondary().getItems().length > 0) {
                  s = s + generateStringFromObject("<input_secondary>", recipe.getInputSecondaryO());
               }
               buffer.write(s);
               buffer.newLine();
            }

            buffer.write(NEW_LINE + NEW_LINE + "# User recipes");
            buffer.close();
            fos.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      try {
         Scanner scanner = new Scanner(mortarAndPestleRecipeFile);
         RecipeRegistry.mortnpestRecipes.clear();
         for (int i = 0; scanner.hasNext(); i++) {
            String sx = scanner.nextLine();
            if (sx.contains("<start_recipe>")) {
               RecipeMortNPest recipeMortNPest = getMortNPestRecipeFromString(sx);
               if (recipeMortNPest != null) {
                  RecipeRegistry.mortnpestRecipes.add(recipeMortNPest);
               } else {
                  System.out.println("Invalid Mortar & Pestle recipe at line " + i);
               }
            }
         }
         scanner.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public static void readTeaRecipeFile() {
      try {
         if (teaRecipeFile.createNewFile() && teaRecipeFile.canWrite()) {
            FileOutputStream fos = new FileOutputStream(teaRecipeFile);
            BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            buffer.write("# Tea file");
            buffer.newLine();
            buffer.write("# notes: If you add additional tea potion effects, ensure to add to add it's name to the language file." + NEW_LINE + NEW_LINE);
            buffer.write("# Default recipes" + NEW_LINE);

            for (RecipeTea recipe : RecipeRegistry.teaRecipes) {
               String s = "<start_recipe>  ";
               s = s + "<name>" + recipe.getName() + ";  ";
               if (recipe.inputO1 != null) {
                  s = s + generateStringFromObject("<input1>", recipe.inputO1);
               }
               if (recipe.inputO2 != null) {
                  s = s + generateStringFromObject("<input2>", recipe.inputO2);
               }
               if (recipe.inputO3 != null) {
                  s = s + generateStringFromObject("<input3>", recipe.inputO3);
               }
               if (recipe.getEffect() != null) {
                  ResourceLocation effId = ForgeRegistries.MOB_EFFECTS.getKey(recipe.getEffect().getEffect());
                  s = s
                     + "<potion>" + effId
                     + ", <duration>" + recipe.getEffect().getDuration()
                     + ", <amplifier>" + recipe.getEffect().getAmplifier()
                     + ";  ";
               }
               buffer.write(s);
               buffer.newLine();
            }

            buffer.write(NEW_LINE + NEW_LINE + "# User recipes");
            buffer.close();
            fos.close();
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      try {
         Scanner scanner = new Scanner(teaRecipeFile);
         RecipeRegistry.teaRecipes.clear();
         for (int i = 0; scanner.hasNext(); i++) {
            String sx = scanner.nextLine();
            if (sx.contains("<start_recipe>")) {
               RecipeTea recipeTea = getTeaRecipeFromString(sx);
               if (recipeTea != null) {
                  RecipeRegistry.teaRecipes.add(recipeTea);
               } else {
                  System.out.println("Invalid Tea recipe at line " + i);
               }
            }
         }
         scanner.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public static String generateStringFromObject(String prefix, Object o) {
      String s = "";
      if (o instanceof String) {

         s = s + prefix + " <tag>" + o + ";  ";
      } else if (o instanceof EnumPlantType) {
         s = s + prefix + " <plant>" + ((EnumPlantType) o).getSerializedName() + ";  ";
      } else if (o instanceof Item) {
         s = s + prefix + " <item>" + ForgeRegistries.ITEMS.getKey((Item) o) + ";  ";
      } else if (o instanceof Block) {

         s = s + prefix + " <item>" + ForgeRegistries.ITEMS.getKey(((Block) o).asItem()) + ";  ";
      } else if (o instanceof ItemStack) {
         s = s + prefix + generateStringFromItemstack(" <itemstack>", (ItemStack) o);
      }
      return s;
   }

   public static Object generateObjectFromString(String s) {
      if (s.contains("<itemstack>")) {
         return generateItemstackFromString(s);
      } else if (s.contains("<tag>") || s.contains("<oredict>")) {
         String marker = s.contains("<tag>") ? "<tag>" : "<oredict>";
         int i = s.indexOf(marker) + marker.length();
         int i1 = s.indexOf(",", i);
         i1 = i1 < 0 ? s.length() : i1;
         return s.substring(i, i1).trim();
      } else if (s.contains("<plant>")) {
         int i = s.indexOf("<plant>") + 7;
         int i1 = s.indexOf(",", i);
         i1 = i1 < 0 ? s.length() : i1;
         return EnumPlantType.fromName(s.substring(i, i1).trim());
      } else {
         if (s.contains("<item>")) {
            int i = s.indexOf("<item>") + 6;
            int i1 = s.indexOf(",", i);
            i1 = i1 < 0 ? s.length() : i1;
            ResourceLocation rl = ResourceLocation.tryParse(s.substring(i, i1).trim());
            if (rl != null) {
               Item item = ForgeRegistries.ITEMS.getValue(rl);
               if (item != null) {
                  return item;
               }
            }
         }
         return null;
      }
   }

   public static String generateStringFromItemstack(String prefix, ItemStack stack) {
      String s = prefix + " <item>" + ForgeRegistries.ITEMS.getKey(stack.getItem());
      if (stack.getCount() > 1) {
         s = s + ", <count>" + stack.getCount();
      }
      if (stack.getDamageValue() > 0) {
         s = s + ", <data>" + stack.getDamageValue();
      }
      return s + ";  ";
   }

   public static ItemStack generateItemstackFromString(String s) {
      Item item = null;
      if (s.contains("<item>")) {
         int i = s.indexOf("<item>") + 6;
         int i1 = s.indexOf(",", i);
         i1 = i1 < 0 ? s.length() : i1;
         ResourceLocation rl = ResourceLocation.tryParse(s.substring(i, i1).trim());
         if (rl != null) {
            item = ForgeRegistries.ITEMS.getValue(rl);
         }
      }

      int count = 1;
      if (s.contains("<count>")) {
         int i = s.indexOf("<count>") + 7;
         int i1 = s.indexOf(",", i);
         i1 = i1 < 0 ? s.length() : i1;
         count = Integer.parseInt(s.substring(i, i1).trim());
      }

      int data = 0;
      if (s.contains("<data>")) {
         int i = s.indexOf("<data>") + 6;
         int i1 = s.indexOf(",", i);
         i1 = i1 < 0 ? s.length() : i1;
         data = Integer.parseInt(s.substring(i, i1).trim());
      }

      if (item == null) {
         return null;
      }
      ItemStack stack = new ItemStack(item, count);
      if (data > 0) {
         stack.setDamageValue(data);
      }
      return stack;
   }

   public static RecipeMortNPest getMortNPestRecipeFromString(String s) {
      ItemStack output = null;
      ItemStack outputSecondary = null;
      int crush = 0;
      Object input = null;
      Object inputSecondary = null;
      if (s.contains("<output>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<output>") + 8, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         output = generateItemstackFromString(all);
      }
      if (s.contains("<output_secondary>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<output_secondary>") + 18, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         outputSecondary = generateItemstackFromString(all);
      }
      if (s.contains("<crush>") && s.indexOf(";") > 0) {
         crush = Integer.parseInt(s.substring(s.indexOf("<crush>") + 7, s.indexOf(";")));
         s = s.substring(s.indexOf(";") + 1);
      }
      if (s.contains("<input>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<input>") + 7, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         input = generateObjectFromString(all);
      }
      if (s.contains("<input_secondary>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<input_secondary>") + 17, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         inputSecondary = generateObjectFromString(all);
      }

      return (input != null || inputSecondary != null) && (output != null || outputSecondary != null) && crush >= 1
         ? new RecipeMortNPest(
            output == null ? ItemStack.EMPTY : output,
            outputSecondary == null ? ItemStack.EMPTY : outputSecondary,
            crush,
            input == null ? ItemStack.EMPTY : input,
            inputSecondary == null ? ItemStack.EMPTY : inputSecondary)
         : null;
   }

   public static RecipeTea getTeaRecipeFromString(String s) {
      String name = null;
      Object input1 = null;
      Object input2 = null;
      Object input3 = null;
      MobEffectInstance effect = null;
      if (s.contains("<name>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<name>") + 6, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         name = all.trim();
      }
      if (s.contains("<input1>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<input1>") + 8, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         input1 = generateObjectFromString(all);
      }
      if (s.contains("<input2>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<input2>") + 8, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         input2 = generateObjectFromString(all);
      }
      if (s.contains("<input3>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<input3>") + 8, s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         input3 = generateObjectFromString(all);
      }
      if (s.contains("<potion>") && s.indexOf(";") > 0) {
         String all = s.substring(s.indexOf("<potion>"), s.indexOf(";"));
         s = s.substring(s.indexOf(";") + 1);
         MobEffect potion = null;
         if (all.contains("<potion>")) {
            int i = all.indexOf("<potion>") + 8;
            int i1 = all.indexOf(",", i);
            i1 = i1 < 0 ? all.length() : i1;
            ResourceLocation rl = ResourceLocation.tryParse(all.substring(i, i1).trim());
            if (rl != null) {
               potion = ForgeRegistries.MOB_EFFECTS.getValue(rl);
            }
         }

         int duration = 0;
         if (all.contains("<duration>")) {
            int i = all.indexOf("<duration>") + 10;
            int i1 = all.indexOf(",", i);
            i1 = i1 < 0 ? all.length() : i1;
            duration = Integer.parseInt(all.substring(i, i1).trim());
         }

         int amplifier = 0;
         if (all.contains("<amplifier>")) {
            int i = all.indexOf("<amplifier>") + 11;
            int i1 = all.indexOf(",", i);
            i1 = i1 < 0 ? all.length() : i1;
            amplifier = Integer.parseInt(all.substring(i, i1).trim());
         }

         if (potion != null) {
            effect = new MobEffectInstance(potion, duration, amplifier);
         }
      }

      return name != null && input1 != null && input2 != null && input3 != null && effect != null
         ? new RecipeTea(name, input1, input2, input3, effect)
         : null;
   }
}
