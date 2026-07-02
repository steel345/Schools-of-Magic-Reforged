package com.paleimitations.schoolsofmagic.common.handlers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.paleimitations.schoolsofmagic.common.books.BookPageSpell;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.CapabilityPage;
import com.paleimitations.schoolsofmagic.common.items.capabilities.page.IPage;
import com.paleimitations.schoolsofmagic.common.registries.SpellRegistry;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.List;

public class SetRandomSpell extends LootItemConditionalFunction {

   public static LootItemFunctionType TYPE;

   protected SetRandomSpell(List<LootItemCondition> conditionsIn) {
      super(conditionsIn.toArray(new LootItemCondition[0]));
   }

   @Override
   protected ItemStack run(ItemStack stack, LootContext context) {
      RandomSource rand = context.getRandom();
      IPage data = stack.getCapability(CapabilityPage.PAGE_CAPABILITY).orElse(null);
      if (data != null) {
         Spell spell = SpellRegistry.SPELLS.get(rand.nextInt(SpellRegistry.SPELLS.size()));
         data.setBookPage(new BookPageSpell(spell.copy()));
         stack.getOrCreateTag().put("page_data", data.serializeNBT());
      }

      return stack;
   }

   @Override
   public LootItemFunctionType getType() {
      return TYPE;
   }

   public static class Serializer extends LootItemConditionalFunction.Serializer<SetRandomSpell> {
      public void serialize(JsonObject object, SetRandomSpell functionClazz, JsonSerializationContext serializationContext) {
         super.serialize(object, functionClazz, serializationContext);
      }

      public SetRandomSpell deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
         return new SetRandomSpell(List.of(conditionsIn));
      }
   }
}
