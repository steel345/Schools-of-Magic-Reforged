package com.paleimitations.schoolsofmagic.common.crafting;

import com.google.gson.JsonObject;
import com.paleimitations.schoolsofmagic.common.items.ItemPotionCrystal;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeSerializerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class PotionCharmRecipe extends ShapedRecipe {

   public PotionCharmRecipe(ResourceLocation id, ShapedRecipe base, ItemStack result) {
      super(id, base.getGroup(), base.category(), base.getWidth(), base.getHeight(),
            base.getIngredients(), result, base.showNotification());
   }

   @Override
   public ItemStack assemble(CraftingContainer container, net.minecraft.core.RegistryAccess access) {
      ItemStack result = new ItemStack(ItemRegistry.potion_charm.get());
      for (int slot = 0; slot < container.getContainerSize(); ++slot) {
         ItemStack stack = container.getItem(slot);
         if (stack.getItem() instanceof ItemPotionCrystal) {
            IPotionData crystal = CapabilityPotionData.getCapability(stack);
            IPotionData charm = CapabilityPotionData.getCapability(result);
            if (crystal != null && charm != null) {
               charm.deserializeNBT(crystal.serializeNBT());
            }
            break;
         }
      }
      return result;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return RecipeSerializerRegistry.POTION_CHARM.get();
   }

   public static class Serializer implements RecipeSerializer<PotionCharmRecipe> {
      @Override
      public PotionCharmRecipe fromJson(ResourceLocation id, JsonObject json) {
         ShapedRecipe base = RecipeSerializer.SHAPED_RECIPE.fromJson(id, json);
         return new PotionCharmRecipe(id, base, new ItemStack(ItemRegistry.potion_charm.get()));
      }

      @Override
      public PotionCharmRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
         ShapedRecipe base = RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buf);
         return new PotionCharmRecipe(id, base, base.getResultItem(null));
      }

      @Override
      public void toNetwork(FriendlyByteBuf buf, PotionCharmRecipe recipe) {
         RecipeSerializer.SHAPED_RECIPE.toNetwork(buf, recipe);
      }
   }
}
