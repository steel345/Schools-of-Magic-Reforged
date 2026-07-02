package com.paleimitations.schoolsofmagic.common.handlers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetRandomMagicPlant extends LootItemConditionalFunction {

   public static LootItemFunctionType TYPE;

   protected SetRandomMagicPlant(List<LootItemCondition> conditionsIn) {
      super(conditionsIn.toArray(new LootItemCondition[0]));
   }

   @Override
   protected ItemStack run(ItemStack stack, LootContext context) {
      EnumMagicType type = EnumMagicType.values()[context.getRandom().nextInt(EnumMagicType.values().length)];
      stack.setDamageValue(type.getIndex() + 1);
      return stack;
   }

   @Override
   public LootItemFunctionType getType() {
      return TYPE;
   }

   public static class Serializer extends LootItemConditionalFunction.Serializer<SetRandomMagicPlant> {
      public void serialize(JsonObject object, SetRandomMagicPlant functionClazz, JsonSerializationContext serializationContext) {
         super.serialize(object, functionClazz, serializationContext);
      }

      public SetRandomMagicPlant deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
         return new SetRandomMagicPlant(List.of(conditionsIn));
      }
   }
}
