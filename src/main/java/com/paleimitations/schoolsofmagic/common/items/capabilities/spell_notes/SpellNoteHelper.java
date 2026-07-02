package com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.registries.BuyableRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.List;
import java.util.Random;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

public class SpellNoteHelper {
   public SpellNoteHelper() {
   }

   public static ItemStack createSpellNote(ItemStackHandler handler) {
      if (handler == null) {
         return null;
      } else {
         ItemStack spell_note = new ItemStack(ItemRegistry.spell_note.get());
         SpellNotes notes = spell_note.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null).getSpellNotes();

         for (int i = 0; i < handler.getSlots(); i++) {

            if (!applyIngredient(notes, handler.getStackInSlot(i))) {
               return null;
            }
         }

         if (notes.magicValue() == 0.0F) {
            return null;
         } else {
            for (int ix = 0; ix < 9; ix++) {
               handler.setStackInSlot(ix, ItemStack.EMPTY);
            }

            getOptions(notes, notes.getOptions());
            return spell_note;
         }
      }
   }

   public static boolean applyIngredient(SpellNotes notes, ItemStack ingred) {
      if (ingred.getItem() == ItemRegistry.seed_magic_plant.get()) {
         notes.elementUnits[ingred.getDamageValue()]++;
      } else if (ingred.getItem() == ItemRegistry.crushed_plant.get()) {
         if (ingred.getDamageValue() - 1 < 16) {
            notes.elementUnits[ingred.getDamageValue() - 1] += 5.0F;
         } else {
            notes.schoolUnits[(ingred.getDamageValue() - 17) / 4] += 5.0F;
         }
      } else if (ingred.getItem() == ItemRegistry.gem_dust.get()) {
         notes.elementUnits[ingred.getDamageValue()] += 20.0F;
      } else if (ingred.getItem() == ItemRegistry.crushed_horn_unicorn.get()) {
         notes.magicianUnits += 10.0F;
         notes.spellUnits += 8.0F;
         notes.potionUnits += 8.0F;
         notes.ritualUnits += 8.0F;
      } else if (ingred.getItem() == ItemRegistry.wand_core.get()) {
         notes.magicianUnits += 10.0F;
         notes.spellUnits += 20.0F;
      } else if (ingred.getItem() == Items.NETHER_WART) {
         notes.potionUnits += 8.0F;
      } else if (ingred.getItem() == ItemRegistry.shard_netherstar.get()) {
         notes.magicianUnits += 20.0F;
         notes.spellUnits += 12.0F;
         notes.potionUnits += 12.0F;
         notes.ritualUnits += 12.0F;
      } else if (ingred.getItem() == ItemRegistry.bottle_egg.get()) {
         notes.magicianUnits += 25.0F;
         notes.spellUnits += 15.0F;
         notes.potionUnits += 15.0F;
         notes.ritualUnits += 15.0F;
      } else if (ingred.getItem() == ItemRegistry.nugget.get()) {
         switch (ingred.getDamageValue()) {
            case 0:
         }
      } else if (fairyDustElement(ingred.getItem()) >= 0) {
         notes.elementUnits[fairyDustElement(ingred.getItem())] += 10.0F;
      } else if (ingred.getItem() == ItemRegistry.ingredient.get()) {
         if (ingred.getDamageValue() == com.paleimitations.schoolsofmagic.common.blocks.EnumIngredient.VILLAGER_HEART.getIndex()) {
            notes.elementUnits[com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry.infernality.getId()] += 20.0F;
         } else {
            return false;
         }
      } else if (!ingred.isEmpty()) {
         return false;
      }
      return true;
   }

   private static java.util.Map<net.minecraft.world.item.Item, Integer> FAIRY_ELEMENT = null;

   private static int fairyDustElement(net.minecraft.world.item.Item item) {
      if (FAIRY_ELEMENT == null) {
         java.util.Map<String, Integer> byId = new java.util.HashMap<>();
         byId.put("white", 11); byId.put("orange", 1); byId.put("magenta", 9); byId.put("yellow", 2);
         byId.put("lime", 3); byId.put("pink", 10); byId.put("gray", 13); byId.put("light_gray", 12);
         byId.put("cyan", 5); byId.put("purple", 8); byId.put("blue", 7); byId.put("brown", 15);
         byId.put("green", 4); byId.put("red", 0); byId.put("black", 14);
         java.util.Map<net.minecraft.world.item.Item, Integer> m = new java.util.HashMap<>();
         com.paleimitations.schoolsofmagic.common.entity.FairyVariant[] vars =
            com.paleimitations.schoolsofmagic.common.entity.FairyVariant.values();
         for (int i = 0; i < vars.length && i < ItemRegistry.FAIRY_DUSTS.size(); i++) {
            Integer el = byId.get(vars[i].id);
            if (el != null) m.put(ItemRegistry.FAIRY_DUSTS.get(i).get(), el);
         }
         m.put(ItemRegistry.fairy_dust_light_blue.get(), 6);
         FAIRY_ELEMENT = m;
      }
      Integer e = FAIRY_ELEMENT.get(item);
      return e == null ? -1 : e;
   }

   public static ItemStack makeNoteFor(ItemStack ingredient) {
      if (ingredient == null || ingredient.isEmpty()) return null;
      ItemStack spell_note = new ItemStack(ItemRegistry.spell_note.get());
      ISpellNotes cap = spell_note.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);
      if (cap == null) return null;
      SpellNotes notes = cap.getSpellNotes();
      if (!applyIngredient(notes, ingredient)) return null;
      if (notes.magicValue() == 0.0F) return null;
      spell_note.getOrCreateTag().put("note_data", cap.serializeNBT());
      return spell_note;
   }

   public static boolean isViableIngredient(ItemStack stack) {
      return stack.getItem() == ItemRegistry.seed_magic_plant.get()
         || stack.getItem() == ItemRegistry.crushed_plant.get()
         || stack.getItem() == ItemRegistry.gem_dust.get()
         || stack.getItem() == ItemRegistry.nugget.get()
         || stack.getItem() == ItemRegistry.crushed_horn_unicorn.get()
         || stack.getItem() == ItemRegistry.item.get()
         || stack.getItem() == ItemRegistry.wand_core.get()
         || stack.getItem() == Items.NETHER_WART
         || stack.getItem() == ItemRegistry.tree_item.get()
         || stack.getItem() == ItemRegistry.ingredient.get();
   }

   public static void getOptions(SpellNotes spellNotes, List<ItemStack> options) {
      Random rand = new Random();
      int desperate = 5;
      List<Buyable> optionProto = Lists.newArrayList();

      while (options.size() < 7) {
         desperate--;

         for (int i = 0; i < 7; i++) {
            List<Buyable> tierList = BuyableRegistry.getBuyablesByTier(i);
            if (!tierList.isEmpty() && BuyableRegistry.chanceOfTier(i, spellNotes.luck)) {
               int j = rand.nextInt(tierList.size());
               if (!optionProto.contains(tierList.get(j)) && tierList.get(j).isBuyable(spellNotes, desperate <= 0)) {
                  optionProto.add(tierList.get(j));
               }
            }
         }

         if (desperate < -20) {
            break;
         }
      }

      for (Buyable buy : optionProto) {
         options.add(buy.getItemStack().copy());
      }
   }
}
