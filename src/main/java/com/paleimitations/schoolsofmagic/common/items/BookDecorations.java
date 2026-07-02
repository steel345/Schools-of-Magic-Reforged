package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BookDecorations {

   public static final String TAG = "BookDecor";

   private static final String[] ELEMENT_TEX = {
      "pyromancy", "heliomancy", "aeromancy", "geomancy", "herbalmancy", "electromancy",
      "hydromancy", "cryomancy", "hieromancy", "chaotimancy", "auramancy", "astromancy",
      "infernality", "spectromancy", "umbramancy", "necromancy"
   };

   public static String ingotMetal(ItemStack stack) {
      if (stack.is(Items.IRON_INGOT)) return "iron";
      if (stack.is(Items.GOLD_INGOT)) return "gold";
      if (stack.is(Items.NETHERITE_INGOT)) return "netherite";
      if (stack.getItem() == ItemRegistry.ingot.get()) {
         switch (stack.getDamageValue()) {
            case 0: return "silver";
            case 2: return "copper";
            case 4: return "bronze";
            case 6: return "brass";
            case 8: return "netherite";
            case 9: return "tenebrium";
            default: return null;
         }
      }
      return null;
   }

   public static String elementTex(ItemStack gemDust) {
      if (gemDust.getItem() != ItemRegistry.gem_dust.get()) return null;
      int d = gemDust.getDamageValue();
      return d >= 0 && d < ELEMENT_TEX.length ? ELEMENT_TEX[d] : null;
   }

   private static CompoundTag decor(ItemStack stack) {
      return stack.getOrCreateTagElement(TAG);
   }

   public static boolean hasAny(ItemStack stack) {
      return stack.hasTag() && stack.getTag().contains(TAG) && !stack.getTagElement(TAG).isEmpty();
   }

   public static boolean hasSwirl(ItemStack stack) {
      return hasAny(stack) && stack.getTagElement(TAG).contains("swirl");
   }

   public static boolean hasFrame(ItemStack stack) {
      return hasAny(stack) && stack.getTagElement(TAG).contains("frame");
   }

   public static boolean hasJewel(ItemStack stack) {
      return hasAny(stack) && "jewel".equals(stack.getTagElement(TAG).getString("emblem"));
   }

   public static String getShape(ItemStack stack) {
      if (!hasAny(stack)) {
         return null;
      }
      CompoundTag t = stack.getTagElement(TAG);
      return t.contains("shape") ? t.getString("shape") : null;
   }

   public static String getEmblem(ItemStack stack) {
      if (!hasAny(stack)) {
         return null;
      }
      CompoundTag t = stack.getTagElement(TAG);
      return t.contains("emblem") ? t.getString("emblem") : null;
   }

   public static String emblemMetal(ItemStack stack) {
      if (!hasAny(stack)) {
         return null;
      }
      CompoundTag t = stack.getTagElement(TAG);
      return t.contains("emblemMetal") ? t.getString("emblemMetal") : null;
   }

   public static void setSwirl(ItemStack stack, DyeColor color) {
      decor(stack).putString("swirl", color.getName());
   }

   public static void setFrame(ItemStack stack, String metal) {
      decor(stack).putString("frame", metal);
   }

   public static void setShape(ItemStack stack, String shape, String metal) {
      CompoundTag t = decor(stack);
      t.putString("shape", shape);
      t.putString("shapeMetal", metal);
   }

   public static void setJewel(ItemStack stack, String metal) {
      CompoundTag t = decor(stack);
      t.putString("emblem", "jewel");
      t.putString("emblemMetal", metal);
      t.remove("emblemElement");
   }

   public static void setElement(ItemStack stack, String metal, int elementId) {
      CompoundTag t = decor(stack);
      t.putString("emblem", "element");
      t.putString("emblemMetal", metal);
      t.putInt("emblemElementId", elementId);
      t.remove("emblemElement");
   }

   public static int getElementId(ItemStack stack) {
      if (!hasAny(stack)) {
         return -1;
      }
      CompoundTag t = stack.getTagElement(TAG);
      if ("element".equals(t.getString("emblem")) && t.contains("emblemElementId")) {
         return t.getInt("emblemElementId");
      }
      return -1;
   }

   public static List<ResourceLocation> layers(ItemStack stack) {
      List<ResourceLocation> list = new ArrayList<>();
      if (!hasAny(stack)) return list;
      CompoundTag t = stack.getTagElement(TAG);
      if (t.contains("swirl")) {
         list.add(tex("decor_" + t.getString("swirl") + "_swirl"));
      }
      if (t.contains("frame")) {
         list.add(tex("decor_" + t.getString("frame") + "_frame"));
      }
      if (t.contains("shape")) {
         list.add(tex("decor_" + t.getString("shapeMetal") + "_" + t.getString("shape")));
      }
      if (t.contains("emblem")) {
         if ("jewel".equals(t.getString("emblem"))) {
            list.add(tex("decor_" + t.getString("emblemMetal") + "_jewel"));
         } else {
            int id = t.getInt("emblemElementId");
            if (id >= 0 && id < ELEMENT_TEX.length) {
               list.add(tex("decor_" + t.getString("emblemMetal") + "_" + ELEMENT_TEX[id]));
            }
         }
      }
      return list;
   }

   private static ResourceLocation tex(String name) {
      return new ResourceLocation("som", "items/book/" + name);
   }
}
