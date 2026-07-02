package com.paleimitations.schoolsofmagic.common.crafting;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class DamageVariantIngredient extends AbstractIngredient {

   public static final ResourceLocation ID = new ResourceLocation("som", "damage_variant");

   private final Item item;
   private final int damage;
   private ItemStack[] display;

   public DamageVariantIngredient(Item item, int damage) {
      super();
      this.item = item;
      this.damage = damage;
   }

   @Override
   public boolean test(@Nullable ItemStack stack) {
      if (stack == null || stack.isEmpty() || stack.getItem() != this.item) {
         return false;
      }
      return stack.getDamageValue() == this.damage;
   }

   @Override
   public ItemStack[] getItems() {
      if (this.display == null) {
         ItemStack s = new ItemStack(this.item);
         s.setDamageValue(this.damage);
         this.display = new ItemStack[]{ s };
      }
      return this.display;
   }

   @Override
   public boolean isSimple() {
      return false;
   }

   @Override
   public IIngredientSerializer<? extends net.minecraft.world.item.crafting.Ingredient> getSerializer() {
      return SERIALIZER;
   }

   @Override
   public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("type", ID.toString());
      json.addProperty("item", ForgeRegistries.ITEMS.getKey(this.item).toString());
      json.addProperty("damage", this.damage);
      return json;
   }

   public static final IIngredientSerializer<DamageVariantIngredient> SERIALIZER =
      new IIngredientSerializer<>() {
         @Override
         public DamageVariantIngredient parse(JsonObject json) {
            Item item = ForgeRegistries.ITEMS.getValue(
               new ResourceLocation(GsonHelper.getAsString(json, "item")));
            int damage = GsonHelper.getAsInt(json, "damage", 0);
            return new DamageVariantIngredient(item, damage);
         }

         @Override
         public DamageVariantIngredient parse(FriendlyByteBuf buf) {
            Item item = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            int damage = buf.readVarInt();
            return new DamageVariantIngredient(item, damage);
         }

         @Override
         public void write(FriendlyByteBuf buf, DamageVariantIngredient ing) {
            buf.writeResourceLocation(ForgeRegistries.ITEMS.getKey(ing.item));
            buf.writeVarInt(ing.damage);
         }
      };
}
