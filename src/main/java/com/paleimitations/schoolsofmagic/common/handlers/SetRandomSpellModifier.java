package com.paleimitations.schoolsofmagic.common.handlers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import java.util.List;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetRandomSpellModifier extends LootItemConditionalFunction {

   public static LootItemFunctionType TYPE;
   public static int[] weights = new int[Spell.EnumSpellModifier.values().length];
   public static int poolSize;

   protected SetRandomSpellModifier(List<LootItemCondition> conditionsIn) {
      super(conditionsIn.toArray(new LootItemCondition[0]));
   }

   @Override
   protected ItemStack run(ItemStack stack, LootContext context) {
      ISpellModifier data = stack.getCapability(CapabilitySpellModifier.SPELL_MODIFIER_CAPABILITY).orElse(null);
      if (data != null) {
         RandomSource rand = context.getRandom();
         int a = rand.nextInt(poolSize);
         Spell.EnumSpellModifier mod = null;
         for (int i = 0; i < weights.length; ++i) {
            if (a > weights[i]) {
               a -= weights[i];
               continue;
            }
            mod = Spell.EnumSpellModifier.values()[i];
            break;
         }
         data.setSpellModifier(mod, mod.defaultObj);
         stack.getOrCreateTag().put("modifier", data.serializeNBT());
      }
      return stack;
   }

   @Override
   public LootItemFunctionType getType() {
      return TYPE;
   }

   static {
      for (int i = 0; i < Spell.EnumSpellModifier.values().length; ++i) {
         Spell.EnumSpellModifier mod = Spell.EnumSpellModifier.values()[i];
         if (mod.id == 16) {
            weights[i] = 1;
            ++poolSize;
            continue;
         }
         weights[i] = 6 - mod.level;
         poolSize += 6 - mod.level;
      }
   }

   public static class Serializer extends LootItemConditionalFunction.Serializer<SetRandomSpellModifier> {
      public void serialize(JsonObject object, SetRandomSpellModifier functionClazz, JsonSerializationContext serializationContext) {
         super.serialize(object, functionClazz, serializationContext);
      }

      public SetRandomSpellModifier deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
         return new SetRandomSpellModifier(List.of(conditionsIn));
      }
   }
}
