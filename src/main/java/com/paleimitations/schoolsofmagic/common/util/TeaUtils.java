package com.paleimitations.schoolsofmagic.common.util;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TeaUtils {
   public static ItemStack appendEffects(ItemStack itemIn, MobEffectInstance effect) {
      CompoundTag nbttagcompound = MoreObjects.firstNonNull(itemIn.getTag(), new CompoundTag());
      nbttagcompound.put("PotionEffect", effect.save(new CompoundTag()));
      itemIn.setTag(nbttagcompound);
      return itemIn;
   }

   public static MobEffectInstance getEffect(ItemStack stack) {
      CompoundTag tag = stack.getTag();
      if (tag != null && tag.contains("PotionEffect")) {
         MobEffectInstance potioneffect = MobEffectInstance.load(tag.getCompound("PotionEffect"));
         if (potioneffect != null) {
            return potioneffect;
         }
      }
      return null;
   }

   public static int resolveTeaColor(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.brewing.BrewResult dyn =
         com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.get(stack);
      if (dyn != null) {
         return dyn.finalTeaColor;
      }
      MobEffectInstance eff = getEffect(stack);
      if (eff == null || eff.getEffect() == null) {
         return 0xFFFFFF;
      }
      for (com.paleimitations.schoolsofmagic.common.recipes.RecipeTea tea :
            com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry.teaRecipes) {
         MobEffectInstance te = tea.getEffect();
         if (te != null && te.getEffect() == eff.getEffect()
               && te.getAmplifier() == eff.getAmplifier() && tea.overlayColor != -1) {
            return tea.overlayColor;
         }
      }
      return eff.getEffect().getColor();
   }

   @OnlyIn(Dist.CLIENT)
   public static void addTeaTooltip(ItemStack itemIn, List<String> lores, float durationFactor) {
      MobEffectInstance potioneffect = getEffect(itemIn);

      if (potioneffect == null) {
         return;
      }
      ArrayList<Tuple<String, AttributeModifier>> list1 = Lists.newArrayList();
      String s1 = I18n.get(potioneffect.getDescriptionId()).trim();
      MobEffect potion = potioneffect.getEffect();
      Map<Attribute, AttributeModifier> map = potion.getAttributeModifiers();
      if (!map.isEmpty()) {
         for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
            AttributeModifier attributemodifier = entry.getValue();
            AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getId(), attributemodifier.getName(), potion.getAttributeModifierValue(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
            list1.add(new Tuple<>(entry.getKey().getDescriptionId(), attributemodifier1));
         }
      }
      if (potioneffect.getAmplifier() > 0) {
         s1 = s1 + " " + I18n.get("potion.potency." + potioneffect.getAmplifier()).trim();
      }
      if (potioneffect.getDuration() > 20) {
         s1 = s1 + " (" + MobEffectUtil.formatDuration(potioneffect, durationFactor).getString() + ")";
      }
      if (potion.getCategory() == MobEffectCategory.HARMFUL) {
         lores.add(ChatFormatting.RED + s1);
      } else {
         lores.add(ChatFormatting.BLUE + s1);
      }
      if (!list1.isEmpty()) {
         lores.add("");
         lores.add(ChatFormatting.DARK_PURPLE + I18n.get("potion.whenDrank"));
         for (Tuple<String, AttributeModifier> tuple : list1) {
            AttributeModifier attributemodifier2 = tuple.getB();
            double d0 = attributemodifier2.getAmount();
            double d1 = attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL
               ? attributemodifier2.getAmount()
               : attributemodifier2.getAmount() * 100.0;
            if (d0 > 0.0) {
               lores.add(ChatFormatting.BLUE + I18n.get("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), I18n.get(tuple.getA())));
               continue;
            }
            if (!(d0 < 0.0)) {
               continue;
            }
            lores.add(ChatFormatting.RED + I18n.get("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1 *= -1.0), I18n.get(tuple.getA())));
         }
      }
   }
}
