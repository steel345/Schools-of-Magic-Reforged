package com.paleimitations.schoolsofmagic.common.brewing;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/**
 * Data-driven roster of brewing modifiers keyed by the item that infuses them.
 * Water-tint colours are deliberately constrained to the blue/cyan/purple-blue range so the
 * infused water still reads as water.
 */
public final class ModifierRegistry {

   private static final Map<Item, TeaModifier> MODIFIERS = new HashMap<>();

   static {
      // descriptor, waterTint, potencyBonus, durationMult, stabilityBonus, toxicityBonus, secChanceBonus, corruptionChance
      put(Items.HONEY_BOTTLE,        new TeaModifier("Sweet",       0x9FD0E8,  0, 1.25F,  2, -2,  0.05F, 0.00F));
      put(Items.SUGAR,               new TeaModifier("Quick",       0x4FC3FF,  1, 0.75F, -1,  0,  0.00F, 0.00F));
      put(Items.GLOWSTONE_DUST,      new TeaModifier("Empowered",   0xBFE6FF,  3, 0.90F, -1,  0,  0.00F, 0.00F));
      put(Items.REDSTONE,            new TeaModifier("Lingering",   0x2A5DA8,  0, 1.75F,  0,  0,  0.00F, 0.00F));
      put(Items.FERMENTED_SPIDER_EYE,new TeaModifier("Corrupted",   0x6A6FA8,  1, 1.00F, -3,  2,  0.10F, 0.45F));
      put(Items.MILK_BUCKET,         new TeaModifier("Soothing",    0xC9DCE8, -1, 0.90F,  3, -4, -0.10F, 0.00F));
      put(Items.BLAZE_POWDER,        new TeaModifier("Overcharged", 0x1E4F8A,  4, 0.80F, -3,  1,  0.00F, 0.15F));
      put(Items.AMETHYST_SHARD,      new TeaModifier("Refined",     0x8A7FD0,  1, 1.10F,  2,  0,  0.35F, 0.00F));
   }

   private static void put(Item item, TeaModifier modifier) {
      MODIFIERS.put(item, modifier);
   }

   public static TeaModifier get(Item item) {
      return item == null ? null : MODIFIERS.get(item);
   }

   /** Combines up to several modifiers into one: bonuses add, duration multiplies, tints average. */
   public static TeaModifier combine(java.util.List<Item> items) {
      if (items == null || items.isEmpty()) return null;
      int potency = 0, stability = 0, toxicity = 0;
      float durMult = 1.0F, secBonus = 0.0F, corruption = 0.0F;
      String descriptor = "";
      int r = 0, g = 0, b = 0, n = 0;
      boolean any = false;
      for (Item it : items) {
         TeaModifier m = get(it);
         if (m == null) continue;
         any = true;
         potency += m.potencyBonus;
         stability += m.stabilityBonus;
         toxicity += m.toxicityBonus;
         durMult *= m.durationMultiplier;
         secBonus += m.secondaryEffectChanceBonus;
         corruption += m.corruptionChance;
         if (descriptor.isEmpty() && !m.descriptor.isEmpty()) descriptor = m.descriptor;
         r += (m.waterTintColor >> 16) & 0xFF;
         g += (m.waterTintColor >> 8) & 0xFF;
         b += m.waterTintColor & 0xFF;
         n++;
      }
      if (!any) return null;
      int tint = n > 0 ? ((r / n) << 16) | ((g / n) << 8) | (b / n) : TeaModifier.NEUTRAL.waterTintColor;
      return new TeaModifier(descriptor, tint, potency, durMult, stability, toxicity, secBonus, corruption);
   }

   public static boolean isModifier(Item item) {
      return item != null && MODIFIERS.containsKey(item);
   }

   private ModifierRegistry() {}
}
