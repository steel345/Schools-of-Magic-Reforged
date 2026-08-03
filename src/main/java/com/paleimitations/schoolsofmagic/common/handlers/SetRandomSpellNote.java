package com.paleimitations.schoolsofmagic.common.handlers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.CapabilitySpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.ISpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNoteHelper;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetRandomSpellNote extends LootItemConditionalFunction {

   public static LootItemFunctionType TYPE;

   // The kinds of point a note can carry. Elements and schools also need an index,
   // picking which of them the points belong to.
   private static final int KIND_ELEMENT = 0;
   private static final int KIND_SCHOOL = 1;
   private static final int KIND_POTION = 2;
   private static final int KIND_RITUAL = 3;
   private static final int KIND_SPELL = 4;
   private static final int KIND_MAGICIAN = 5;

   protected SetRandomSpellNote(List<LootItemCondition> conditionsIn) {
      super(conditionsIn.toArray(new LootItemCondition[0]));
   }

   private static void award(SpellNotes notes, int kind, int index, float amount) {
      if (amount <= 0.0F) return;
      switch (kind) {
         case KIND_ELEMENT -> notes.elementUnits[index] += amount;
         case KIND_SCHOOL -> notes.schoolUnits[index] += amount;
         case KIND_POTION -> notes.potionUnits += amount;
         case KIND_RITUAL -> notes.ritualUnits += amount;
         case KIND_SPELL -> notes.spellUnits += amount;
         case KIND_MAGICIAN -> notes.magicianUnits += amount;
         default -> { }
      }
   }

   @Override
   protected ItemStack run(ItemStack stack, LootContext context) {
      ISpellNotes data = stack.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);
      if (data != null) {
         RandomSource rand = context.getRandom();
         SpellNotes notes = data.getSpellNotes();

         // A found note is a modest thing: a small pool of points spread over only
         // one or two kinds, rather than a large one scattered across many.
         int budget = (1 + rand.nextInt(4)) * 5;
         int kinds = 1 + rand.nextInt(2);
         java.util.List<int[]> picks = new java.util.ArrayList<>();
         for (int attempt = 0; attempt < 32 && picks.size() < kinds; attempt++) {
            final int kind = rand.nextInt(6);
            final int index = kind == KIND_ELEMENT ? rand.nextInt(MagicElementRegistry.ELEMENTS.size())
               : kind == KIND_SCHOOL ? rand.nextInt(MagicSchoolRegistry.SCHOOLS.size()) : 0;
            boolean already = picks.stream().anyMatch(p -> p[0] == kind && p[1] == index);
            if (!already) picks.add(new int[]{kind, index});
         }

         int left = budget;
         for (int p = 0; p < picks.size(); p++) {
            int share = p == picks.size() - 1
               ? left
               : Math.min(left, Math.max(1, Math.round(left * (0.4F + rand.nextFloat() * 0.3F))));
            award(notes, picks.get(p)[0], picks.get(p)[1], share);
            left -= share;
         }
         SpellNoteHelper.getOptions(notes, notes.getOptions());
         stack.getOrCreateTag().put("note_data", data.serializeNBT());
      }
      return stack;
   }

   @Override
   public LootItemFunctionType getType() {
      return TYPE;
   }

   public static class Serializer extends LootItemConditionalFunction.Serializer<SetRandomSpellNote> {
      public void serialize(JsonObject object, SetRandomSpellNote functionClazz, JsonSerializationContext serializationContext) {
         super.serialize(object, functionClazz, serializationContext);
      }

      public SetRandomSpellNote deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
         return new SetRandomSpellNote(List.of(conditionsIn));
      }
   }
}
