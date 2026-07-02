package com.paleimitations.schoolsofmagic.common.registries;

import com.paleimitations.schoolsofmagic.common.util.References;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerRegistry {

   public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
      DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, References.MODID);

   public static final RegistryObject<RecipeSerializer<?>> SPELLBOOK_VARIANT =
      SERIALIZERS.register("spellbook_variant",
         () -> new com.paleimitations.schoolsofmagic.common.crafting.SpellbookVariantRecipe.Serializer());

   public static final RegistryObject<RecipeSerializer<?>> BOOK_SMITHING =
      SERIALIZERS.register("book_smithing",
         () -> new com.paleimitations.schoolsofmagic.common.crafting.BookSmithingRecipe.Serializer());

   public static final RegistryObject<RecipeSerializer<?>> POTION_CHARM =
      SERIALIZERS.register("potion_charm",
         () -> new com.paleimitations.schoolsofmagic.common.crafting.PotionCharmRecipe.Serializer());

   public static final RegistryObject<RecipeSerializer<?>> NBT_SMELTING =
      SERIALIZERS.register("nbt_smelting",
         () -> new com.paleimitations.schoolsofmagic.common.crafting.NbtSmeltingRecipe.Serializer());

   public static final RegistryObject<RecipeSerializer<?>> NBT_BLASTING =
      SERIALIZERS.register("nbt_blasting",
         () -> new com.paleimitations.schoolsofmagic.common.crafting.NbtBlastingRecipe.Serializer());

   public static final RegistryObject<RecipeSerializer<?>> NBT_STONECUTTING =
      SERIALIZERS.register("nbt_stonecutting",
         () -> new com.paleimitations.schoolsofmagic.common.crafting.NbtStonecuttingRecipe.Serializer());

   public static final RegistryObject<RecipeSerializer<?>> COCONUT_MILK =
      SERIALIZERS.register("coconut_milk",
         () -> new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(
            com.paleimitations.schoolsofmagic.common.crafting.CoconutMilkRecipe::new));

   public static final RegistryObject<RecipeSerializer<?>> WHISTLE_WAX =
      SERIALIZERS.register("whistle_wax",
         () -> new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(
            com.paleimitations.schoolsofmagic.common.crafting.WhistleWaxRecipe::new));

   public static final RegistryObject<RecipeSerializer<?>> MYSTERIOUS_APPLICATION =
      SERIALIZERS.register("mysterious_application",
         () -> new net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer<>(
            com.paleimitations.schoolsofmagic.common.crafting.MysteriousApplicationRecipe::new));

   public static void register(IEventBus bus) {
      SERIALIZERS.register(bus);
   }
}
