package com.paleimitations.schoolsofmagic.common.crafting;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockStateVariantIngredient extends AbstractIngredient {

   public static final ResourceLocation ID = new ResourceLocation("som", "blockstate_variant");

   private final Item item;
   private final String key;
   private final String value;
   private ItemStack[] display;

   public BlockStateVariantIngredient(Item item, String key, String value) {
      super();
      this.item = item;
      this.key = key;
      this.value = value;
   }

   @Override
   public boolean test(@Nullable ItemStack stack) {
      if (stack == null || stack.isEmpty() || stack.getItem() != this.item) {
         return false;
      }
      CompoundTag tag = stack.getTag();
      if (tag == null || !tag.contains("BlockStateTag")) {
         return false;
      }
      return this.value.equals(tag.getCompound("BlockStateTag").getString(this.key));
   }

   @Override
   public ItemStack[] getItems() {
      if (this.display == null) {
         ItemStack s = new ItemStack(this.item);
         CompoundTag bs = new CompoundTag();
         bs.putString(this.key, this.value);
         s.getOrCreateTag().put("BlockStateTag", bs);
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
      json.addProperty("key", this.key);
      json.addProperty("value", this.value);
      return json;
   }

   public static final IIngredientSerializer<BlockStateVariantIngredient> SERIALIZER =
      new IIngredientSerializer<>() {
         @Override
         public BlockStateVariantIngredient parse(JsonObject json) {
            Item item = ForgeRegistries.ITEMS.getValue(
               new ResourceLocation(GsonHelper.getAsString(json, "item")));
            String key = GsonHelper.getAsString(json, "key", "variant");
            String value = GsonHelper.getAsString(json, "value");
            return new BlockStateVariantIngredient(item, key, value);
         }

         @Override
         public BlockStateVariantIngredient parse(FriendlyByteBuf buf) {
            Item item = ForgeRegistries.ITEMS.getValue(buf.readResourceLocation());
            String key = buf.readUtf();
            String value = buf.readUtf();
            return new BlockStateVariantIngredient(item, key, value);
         }

         @Override
         public void write(FriendlyByteBuf buf, BlockStateVariantIngredient ing) {
            buf.writeResourceLocation(ForgeRegistries.ITEMS.getKey(ing.item));
            buf.writeUtf(ing.key);
            buf.writeUtf(ing.value);
         }
      };
}
