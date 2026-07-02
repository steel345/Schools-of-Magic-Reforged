package com.paleimitations.schoolsofmagic.common.books;

import com.google.common.collect.Lists;
import com.paleimitations.imitationcore.common.utils.FloatRange;
import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.MagicSchool;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.items.ItemPageBase;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.Buyable;
import com.paleimitations.schoolsofmagic.common.registries.BookPageRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public class BookPagePotionEffect extends BookPage {
   public final MobEffectInstance potion;
   public final ItemStack ingredient;
   public final MagicSchool school;
   public final MagicElement element;

   public BookPagePotionEffect(MobEffectInstance potion, ItemStack ingredient, ItemStack potionItem, MagicSchool school, MagicElement category) {
      super(
         potion.getDescriptionId(),
         Lists.newArrayList(new PageElement[]{
            new PageElementStandardText(potion.getDescriptionId(), 72, 55, 99, 10, category.getColor(), true),
            new PageElementImageColorized(new ResourceLocation("som", "textures/gui/books/potion_lock.png"), 0, 0, 0, 0, 256, 256, 1.0F, category.getColor()),
            new PageElementImageColorized(new ResourceLocation("som", "textures/gui/books/potion_school_" + school.getName() + ".png"), 0, 0, 0, 0, 256, 256, 1.0F, category.getColor()),
            new PageElementString(
               potion.getDuration() / 1200
                  + ":"
                  + (potion.getDuration() / 20 % 60 < 10 ? "0" + potion.getDuration() / 20 % 60 : String.valueOf(potion.getDuration() / 20 % 60)),
               72, 121, 16, 16, category.getColor(), true),
            new PageElementStandardText("page.duration.element", 72, 136, 32, 16, category.getColor(), true),
            new PageElementItemStack(ingredient, 38, 97),

            new PageElementItemStack(buildColoredPotionStack(potion), 90, 97),
            new PageElementPotionIcon(potion, 63, 67)
         })
      );
      this.potion = potion;
      this.ingredient = ingredient;
      this.school = school;
      this.element = category;

      FloatRange[] schoolRanges = new FloatRange[6];
      for (int i = 0; i < 6; i++) schoolRanges[i] = FloatRange.EMPTY;
      schoolRanges[this.school.getId()] = new FloatRange(5.0F, 30.0F);

      FloatRange[] elementRanges = new FloatRange[16];
      for (int i = 0; i < 16; i++) elementRanges[i] = FloatRange.EMPTY;
      elementRanges[this.element.getId()] = new FloatRange(5.0F, 50.0F);

      new Buyable(ItemPageBase.getPage(this),
         FloatRange.EMPTY, FloatRange.EMPTY, FloatRange.EMPTY,
         new FloatRange(5.0F, 100.0F),
         schoolRanges, elementRanges, false,
         new FloatRange(20.0F, 80.0F), 2);

      int i = 0;
      int j = 0;
      if (ingredient.getItem() == ItemRegistry.crushed_plant.get()) {

         int plantIdx = ingredient.getDamageValue();
         if (plantIdx < 0 || plantIdx >= EnumPlantType.values().length) plantIdx = 0;
         this.addElement(new PageElementHerbalTwineRecipe(EnumPlantType.values()[plantIdx], 157, 91));
         this.addElement(new PageElementMortarRecipe(RecipeRegistry.getMortarRecipe(ingredient), 139, 126, 0));
         i = 95;
      } else if (RecipeRegistry.getMortarRecipe(ingredient) != null) {
         this.addElement(new PageElementMortarRecipe(RecipeRegistry.getMortarRecipe(ingredient), 139, 126, 0));
         i = 70;
      }

      if (RecipeRegistry.getCatalystRecipe(ingredient) != null) {
         if (i == 0) {
            this.addElement(new PageElementCatalystBasinRecipe(RecipeRegistry.getCatalystRecipe(ingredient), 132, 74, 0));
            i = 120;
         } else {
            this.addElement(new PageElementCatalystBasinRecipe(RecipeRegistry.getCatalystRecipe(ingredient), 21, 50, 1));
            j = 120;
         }
      }

      String textKey = potion.getEffect().getDescriptionId();
      if (textKey.startsWith("effect.som."))            textKey = "effect." + textKey.substring("effect.som.".length());
      else if (textKey.startsWith("effect.minecraft.")) textKey = "effect." + textKey.substring("effect.minecraft.".length());
      textKey = remapVanillaEffectKey(textKey);
      this.addElement(new PageElementParagraphs(
         textKey,
         0.75F, 0, 1,
         new ParagraphBox(23, 150, 0, 99, 40),
         new ParagraphBox(134, 50, 0, 99, 140 - i),
         new ParagraphBox(23, 50 + j, 1, 99, 140 - j),
         new ParagraphBox(134, 50, 1, 99, 140)));
      BookPageRegistry.POTION_EFFECT_PAGES.add(this);
   }

   private static ItemStack buildColoredPotionStack(MobEffectInstance effect) {
      ItemStack stack = new ItemStack(net.minecraft.world.item.Items.POTION);
      net.minecraft.world.item.alchemy.Potion match = findPotionFor(effect.getEffect());
      if (match != null) {
         net.minecraft.world.item.alchemy.PotionUtils.setPotion(stack, match);
      } else {
         net.minecraft.world.item.alchemy.PotionUtils.setCustomEffects(stack, Lists.newArrayList(effect));
      }
      return stack;
   }

   private static net.minecraft.world.item.alchemy.Potion findPotionFor(net.minecraft.world.effect.MobEffect target) {
      net.minecraft.world.item.alchemy.Potion fallback = null;
      String effectName = net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getKey(target) == null
         ? "" : net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS.getKey(target).getPath();
      for (net.minecraft.world.item.alchemy.Potion potion :
           net.minecraftforge.registries.ForgeRegistries.POTIONS.getValues()) {
         java.util.List<net.minecraft.world.effect.MobEffectInstance> effects = potion.getEffects();
         if (effects.isEmpty()) continue;
         if (effects.get(0).getEffect() != target) continue;
         net.minecraft.resources.ResourceLocation id =
            net.minecraftforge.registries.ForgeRegistries.POTIONS.getKey(potion);
         if (id != null && id.getPath().equals(effectName)) return potion;
         if (fallback == null) fallback = potion;
      }
      return fallback;
   }

   private static String remapVanillaEffectKey(String key) {
      switch (key) {
         case "effect.fire_resistance": return "effect.fireresistance";
         case "effect.water_breathing": return "effect.waterbreathing";
         case "effect.night_vision":    return "effect.nightvision";
         case "effect.health_boost":    return "effect.healthboost";
         case "effect.strength":        return "effect.damageboost";
         case "effect.slowness":        return "effect.moveslowdown";
         case "effect.speed":           return "effect.movespeed";
         case "effect.haste":           return "effect.digspeed";
         case "effect.mining_fatigue":  return "effect.digslowdown";
         case "effect.leaping":
         case "effect.jump_boost":      return "effect.jump";
         case "effect.healing":
         case "effect.instant_health":  return "effect.heal";
         case "effect.harming":
         case "effect.instant_damage":  return "effect.harm";
         case "effect.nausea":          return "effect.confusion";
         default: return key;
      }
   }

   public MobEffectInstance getPotion() { return this.potion; }
   public ItemStack getIngredient() { return this.ingredient; }
   public MagicSchool getSchool() { return this.school; }
   public MagicElement getElement() { return this.element; }
}
