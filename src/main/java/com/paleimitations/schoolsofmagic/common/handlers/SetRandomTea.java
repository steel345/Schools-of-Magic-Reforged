package com.paleimitations.schoolsofmagic.common.handlers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetRandomTea extends LootItemConditionalFunction {

   public static LootItemFunctionType TYPE;

   protected SetRandomTea(List<LootItemCondition> conditionsIn) {
      super(conditionsIn.toArray(new LootItemCondition[0]));
   }

   @Override
   protected ItemStack run(ItemStack stack, LootContext context) {
      List<RecipeTea> teas = new ArrayList<>();
      for (RecipeTea r : RecipeRegistry.teaRecipes) {
         if (r.getEffect() != null) {
            teas.add(r);
         }
      }
      if (!teas.isEmpty()) {
         RecipeTea r = teas.get(context.getRandom().nextInt(teas.size()));
         TeaUtils.appendEffects(stack, new MobEffectInstance(r.getEffect()));
      }
      return stack;
   }

   @Override
   public LootItemFunctionType getType() {
      return TYPE;
   }

   public static class Serializer extends LootItemConditionalFunction.Serializer<SetRandomTea> {
      public void serialize(JsonObject object, SetRandomTea functionClazz, JsonSerializationContext serializationContext) {
         super.serialize(object, functionClazz, serializationContext);
      }

      public SetRandomTea deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootItemCondition[] conditionsIn) {
         return new SetRandomTea(List.of(conditionsIn));
      }
   }
}
