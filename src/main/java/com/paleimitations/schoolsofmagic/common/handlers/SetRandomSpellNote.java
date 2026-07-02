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

   protected SetRandomSpellNote(List<LootItemCondition> conditionsIn) {
      super(conditionsIn.toArray(new LootItemCondition[0]));
   }

   @Override
   protected ItemStack run(ItemStack stack, LootContext context) {
      ISpellNotes data = stack.getCapability(CapabilitySpellNotes.SPELL_NOTES_CAPABILITY).orElse(null);
      if (data != null) {
         RandomSource rand = context.getRandom();
         SpellNotes notes = data.getSpellNotes();

         int i = (1 + rand.nextInt(8)) * 5 + rand.nextInt(7) * 10 + rand.nextInt(4) * 20;
         while (i > 0) {
            i = this.applyNote(notes, i, rand);
         }
         SpellNoteHelper.getOptions(notes, notes.getOptions());
         stack.getOrCreateTag().put("note_data", data.serializeNBT());
      }
      return stack;
   }

   private int applyNote(SpellNotes notes, int value, RandomSource rand) {
      if (value <= 0) {
         return 0;
      }
      switch (rand.nextInt(7)) {
         case 0: {
            int a = Math.round((rand.nextFloat() * 0.5F + 0.15F) * (float) value);
            int n = rand.nextInt(MagicElementRegistry.ELEMENTS.size());
            notes.elementUnits[n] = notes.elementUnits[n] + (float) a;
            value -= a;
            break;
         }
         case 1: {
            int a = Math.round((rand.nextFloat() * 0.5F + 0.15F) * (float) value);
            int n = rand.nextInt(MagicSchoolRegistry.SCHOOLS.size());
            notes.schoolUnits[n] = notes.schoolUnits[n] + (float) a;
            value -= a;
            break;
         }
         case 2: {
            int a = Math.round((rand.nextFloat() * 0.5F + 0.15F) * (float) value);
            notes.potionUnits += (float) a;
            value -= a;
            break;
         }
         case 3: {
            int a = Math.round((rand.nextFloat() * 0.5F + 0.15F) * (float) value);
            notes.ritualUnits += (float) a;
            value -= a;
            break;
         }
         case 4: {
            int a = Math.round((rand.nextFloat() * 0.5F + 0.15F) * (float) value);
            notes.spellUnits += (float) a;
            value -= a;
            break;
         }
         case 5: {
            int a = Math.round((rand.nextFloat() * 0.5F + 0.15F) * (float) value);
            notes.magicianUnits += (float) a;
            value -= a;
            break;
         }
      }
      return value;
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
