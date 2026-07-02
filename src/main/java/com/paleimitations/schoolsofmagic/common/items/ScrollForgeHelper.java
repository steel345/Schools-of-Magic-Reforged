package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

public class ScrollForgeHelper {

   public static final class Result {
      public final ItemStack scroll;
      public final boolean leavesFlask;

      Result(ItemStack scroll, boolean leavesFlask) {
         this.scroll = scroll;
         this.leavesFlask = leavesFlask;
      }
   }

   public static boolean isScrollRecipe(ItemStackHandler h) {
      return tryCraft(h) != null;
   }

   public static Result tryCraft(ItemStackHandler h) {
      ItemStack c0 = h.getStackInSlot(0), c2 = h.getStackInSlot(2), c6 = h.getStackInSlot(6), c8 = h.getStackInSlot(8);
      ItemStack e1 = h.getStackInSlot(1), e3 = h.getStackInSlot(3), e5 = h.getStackInSlot(5), e7 = h.getStackInSlot(7);
      ItemStack center = h.getStackInSlot(4);

      if (!isLapis(e1) || !isLapis(e3) || !isLapis(e5) || !isLapis(e7)) return null;
      if (c0.isEmpty()
            || !ItemStack.isSameItemSameTags(c0, c2)
            || !ItemStack.isSameItemSameTags(c0, c6)
            || !ItemStack.isSameItemSameTags(c0, c8)) return null;

      Spell.EnumSpellModifier mod;
      int elem = elementalDustIndex(c0);
      if (elem >= 0) {
         if (!isScrollSeal(center)) return null;
         mod = Spell.EnumSpellModifier.fromIDs(16, elem + 1);
      } else {
         int group = catalystGroup(c0);
         if (group < 0) return null;
         int level;
         if (isScrollSeal(center)) {
            level = 1;
         } else if (center.getItem() instanceof ItemSpellModifierScroll) {
            Spell.EnumSpellModifier cur = ItemSpellModifierScroll.getModifier(center);
            if (cur == null || cur.id != group) return null;
            int max = maxLevel(group);
            if (max <= 1 || cur.level >= max) return null;
            level = cur.level + 1;
         } else {
            return null;
         }
         mod = Spell.EnumSpellModifier.fromIDs(group, level);
      }
      if (mod == null) return null;

      ItemStack scroll = makeScroll(mod);
      if (CapabilitySpellModifier.getCapability(scroll) == null) return null;

      boolean flask = c0.getItem() == ItemRegistry.bottle.get() && c0.getDamageValue() == 1;
      return new Result(scroll, flask);
   }

   public static ItemStack makeScroll(Spell.EnumSpellModifier mod) {
      ItemStack scroll = new ItemStack(ItemRegistry.spell_modifier_scroll.get());
      ISpellModifier data = CapabilitySpellModifier.getCapability(scroll);
      if (data != null && mod != null) {
         data.setSpellModifier(mod, mod.getDefaultObj());
         scroll.getOrCreateTag().put("modifier", data.serializeNBT());
      }
      return scroll;
   }

   public static ItemStack catalystFor(int group) {
      switch (group) {
         case 0: return new ItemStack(ItemRegistry.crushed_horn_unicorn.get());
         case 1: return new ItemStack(ItemRegistry.bi_gem_geomancy.get());
         case 2: return new ItemStack(ItemRegistry.item_obsidian_shard.get());
         case 3: { ItemStack s = new ItemStack(ItemRegistry.tree_item.get()); s.setDamageValue(com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.ELDER.getIndex()); return s; }
         case 4: return new ItemStack(ItemRegistry.toad_spawn.get());
         case 5: return new ItemStack(Items.BLAZE_POWDER);
         case 6: return new ItemStack(ItemRegistry.bi_gem_infernality.get());
         case 7: return new ItemStack(ItemRegistry.bi_dynamic_web.get());
         case 8: return new ItemStack(ItemRegistry.magic_diamond.get());
         case 9: { ItemStack s = new ItemStack(ItemRegistry.ingredient.get()); s.setDamageValue(com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.BIRD_HEART.getIndex()); return s; }
         case 11: return new ItemStack(ItemRegistry.bi_gem_spectromancy.get());
         case 10: { ItemStack s = new ItemStack(ItemRegistry.ingot.get()); s.setDamageValue(4); return s; }
         case 12: { ItemStack s = new ItemStack(ItemRegistry.bottle.get()); s.setDamageValue(1); return s; }
         case 13: return new ItemStack(Items.WHITE_BANNER);
         case 14: { ItemStack s = new ItemStack(ItemRegistry.gem_dust.get()); s.setDamageValue(10); return s; }
         case 15: return new ItemStack(Items.COMPARATOR);
         case 17: return new ItemStack(Items.ENCHANTED_BOOK);
         default: return ItemStack.EMPTY;
      }
   }

   public static ItemStack elementalDust(int elementIndex) {
      ItemStack s = new ItemStack(ItemRegistry.gem_dust.get());
      s.setDamageValue(elementIndex);
      return s;
   }

   public static int maxLevel(int group) {
      int max = 0;
      for (Spell.EnumSpellModifier m : Spell.EnumSpellModifier.values()) {
         if (m.id == group && m.level > max) max = m.level;
      }
      return max;
   }

   private static boolean isLapis(ItemStack s) {
      return s.is(Items.LAPIS_LAZULI);
   }

   private static boolean isScrollSeal(ItemStack s) {
      return s.getItem() == ItemRegistry.scroll_seal.get();
   }

   private static int elementalDustIndex(ItemStack s) {
      if (s.getItem() != ItemRegistry.gem_dust.get()) return -1;
      int d = s.getDamageValue();
      if (d == 10) return -1;
      return (d >= 0 && d <= 15) ? d : -1;
   }

   private static int catalystGroup(ItemStack s) {
      Item i = s.getItem();
      if (i == ItemRegistry.crushed_horn_unicorn.get()) return 0;
      if (i == ItemRegistry.bi_gem_geomancy.get()) return 1;
      if (i == ItemRegistry.item_obsidian_shard.get()) return 2;
      if (i == ItemRegistry.tree_item.get()
            && s.getDamageValue() == com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood.ELDER.getIndex()) return 3;
      if (i == ItemRegistry.toad_spawn.get()) return 4;
      if (i == Items.BLAZE_POWDER) return 5;
      if (i == ItemRegistry.bi_gem_infernality.get()) return 6;
      if (i == ItemRegistry.bi_dynamic_web.get()) return 7;
      if (i == ItemRegistry.bi_gem_spectromancy.get()) return 11;
      if (i == ItemRegistry.ingredient.get()
            && s.getDamageValue() == com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.BIRD_HEART.getIndex()) return 9;
      if (i == ItemRegistry.magic_diamond.get()) return 8;
      if (i == ItemRegistry.ingot.get() && s.getDamageValue() == 4) return 10;
      if (i == ItemRegistry.bottle.get() && s.getDamageValue() == 1) return 12;
      if (i == Items.WHITE_BANNER) return 13;
      if (i == ItemRegistry.gem_dust.get() && s.getDamageValue() == 10) return 14;
      if (i == Items.COMPARATOR) return 15;
      if (i == Items.ENCHANTED_BOOK) return 17;
      return -1;
   }
}
