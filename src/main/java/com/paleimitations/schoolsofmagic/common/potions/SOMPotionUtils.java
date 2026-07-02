package com.paleimitations.schoolsofmagic.common.potions;

import com.google.common.collect.Lists;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicType;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMagicWood;
import com.paleimitations.schoolsofmagic.common.blocks.EnumMisc;
import com.paleimitations.schoolsofmagic.common.blocks.EnumPlantType;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SOMPotionUtils {

   private static ItemStack m(net.minecraft.world.item.Item item, int variant) {
      ItemStack s = new ItemStack(item);
      s.setDamageValue(variant);
      return s;
   }

   public static ItemStack DURATION_LVL_1 = new ItemStack(Items.REDSTONE);
   public static ItemStack DURATION_LVL_2 = new ItemStack(ItemRegistry.item_obsidian_shard.get());

   public static ItemStack DURATION_LVL_3 = m(ItemRegistry.tree_item.get(), EnumMagicWood.ELDER.getIndex());
   public static ItemStack DURATION_LVL_4 = new ItemStack(ItemRegistry.bottle_egg.get());
   public static ItemStack AMPLIFIER_LVL_1 = new ItemStack(Items.GLOWSTONE_DUST);
   public static ItemStack AMPLIFIER_LVL_2 = new ItemStack(Items.PRISMARINE_CRYSTALS);
   public static ItemStack AMPLIFIER_LVL_3 = new ItemStack(ItemRegistry.item_diamond_dust.get());
   public static ItemStack AMPLIFIER_LVL_4 = new ItemStack(ItemRegistry.shard_netherstar.get());
   public static ItemStack LENGTH_LVL_1 = new ItemStack(Items.BRICK);
   public static ItemStack LENGTH_LVL_2 = m(ItemRegistry.crushed_plant.get(), EnumPlantType.PEONY.getIndex());
   public static ItemStack LENGTH_LVL_3 = new ItemStack(ItemRegistry.toad_spawn.get());
   public static ItemStack LENGTH_LVL_4 = new ItemStack(BlockRegistry.gem_hydromancy.get());
   public static ItemStack DRINK_LVL_1 = new ItemStack(Items.EGG);
   public static ItemStack DRINK_LVL_2 = new ItemStack(Items.GREEN_DYE);
   public static ItemStack DRINK_LVL_3 = m(ItemRegistry.tree_item.get(), EnumMagicWood.VERDE.getIndex());
   public static ItemStack DRINK_LVL_4 = m(ItemRegistry.crushed_plant.get(), EnumPlantType.SAGE.getIndex());
   public static ItemStack RADIUS_LVL_1 = new ItemStack(Items.QUARTZ);
   public static ItemStack RADIUS_LVL_2 = m(ItemRegistry.gem_chunk.get(), EnumMagicType.AEROMANCY.getIndex());
   public static ItemStack RADIUS_LVL_3 = new ItemStack(Items.RABBIT_FOOT);
   public static ItemStack RADIUS_LVL_4 = new ItemStack(BlockRegistry.gem_aeromancy.get());
   public static ItemStack PARTICLE_SUPPRESSANT = new ItemStack(ItemRegistry.gem_chunk.get());

   public static ItemStack getPotionItem(ItemStack stack) {
      if (ItemStack.isSameItem(stack, new ItemStack(Items.NETHER_WART))) {
         return new ItemStack(ItemRegistry.potion_drinkable.get());
      }
      if (ItemStack.isSameItem(stack, new ItemStack(Items.GUNPOWDER))) {
         return new ItemStack(ItemRegistry.potion_throwable.get());
      }
      if (ItemStack.isSameItem(stack, new ItemStack(Items.DRAGON_BREATH))) {
         return new ItemStack(ItemRegistry.potion_lingering.get());
      }
      return null;
   }

   public static boolean isParticleSuppressant(ItemStack stackInSlot) {
      return ItemStack.isSameItem(stackInSlot, PARTICLE_SUPPRESSANT);
   }

   public static int getRadius(ItemStack stack) {
      if (ItemStack.isSameItem(stack, RADIUS_LVL_1)) {
         return 5;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_2)) {
         return 7;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_3)) {
         return 9;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_4)) {
         return 11;
      }
      return 3;
   }

   public static boolean isRadiusItem(ItemStack stack) {
      return ItemStack.isSameItem(stack, RADIUS_LVL_1) || ItemStack.isSameItem(stack, RADIUS_LVL_2) || ItemStack.isSameItem(stack, RADIUS_LVL_3) || ItemStack.isSameItem(stack, RADIUS_LVL_4);
   }

   public static int getLength(ItemStack stack) {
      if (ItemStack.isSameItem(stack, LENGTH_LVL_1)) {
         return 800;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_2)) {
         return 1000;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_3)) {
         return 1200;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_4)) {
         return 1600;
      }
      return 600;
   }

   public static boolean isLengthItem(ItemStack stack) {
      return ItemStack.isSameItem(stack, LENGTH_LVL_1) || ItemStack.isSameItem(stack, LENGTH_LVL_2) || ItemStack.isSameItem(stack, LENGTH_LVL_3) || ItemStack.isSameItem(stack, LENGTH_LVL_4);
   }

   public static int getDrinkTimeItem(ItemStack stack) {
      if (ItemStack.isSameItem(stack, DRINK_LVL_1)) {
         return 24;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_2)) {
         return 16;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_3)) {
         return 8;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_4)) {
         return 4;
      }
      return 32;
   }

   public static boolean isDrinkTimeItem(ItemStack stack) {
      return ItemStack.isSameItem(stack, DRINK_LVL_1) || ItemStack.isSameItem(stack, DRINK_LVL_2) || ItemStack.isSameItem(stack, DRINK_LVL_3) || ItemStack.isSameItem(stack, DRINK_LVL_4);
   }

   public static int getLevel(ItemStack stack) {
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_1)) {
         return 1;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_2)) {
         return 2;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_3)) {
         return 3;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_4)) {
         return 4;
      }
      return 0;
   }

   public static boolean isDurationItem(ItemStack stack) {
      return ItemStack.isSameItem(stack, DURATION_LVL_1) || ItemStack.isSameItem(stack, DURATION_LVL_2) || ItemStack.isSameItem(stack, DURATION_LVL_3) || ItemStack.isSameItem(stack, DURATION_LVL_4);
   }

   public static int getDuration(ItemStack stack) {
      if (ItemStack.isSameItem(stack, DURATION_LVL_1)) {
         return 1;
      }
      if (ItemStack.isSameItem(stack, DURATION_LVL_2)) {
         return 2;
      }
      if (ItemStack.isSameItem(stack, DURATION_LVL_3)) {
         return 3;
      }
      if (ItemStack.isSameItem(stack, DURATION_LVL_4)) {
         return 4;
      }
      return 0;
   }

   public static int getItemCost(ItemStack stack) {
      if (ItemStack.isSameItem(stack, DURATION_LVL_1)) {
         return 20;
      }
      if (ItemStack.isSameItem(stack, DURATION_LVL_2)) {
         return 50;
      }
      if (ItemStack.isSameItem(stack, DURATION_LVL_3)) {
         return 80;
      }
      if (ItemStack.isSameItem(stack, DURATION_LVL_4)) {
         return 120;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_1)) {
         return 20;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_2)) {
         return 50;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_3)) {
         return 80;
      }
      if (ItemStack.isSameItem(stack, AMPLIFIER_LVL_4)) {
         return 120;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_1)) {
         return 10;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_2)) {
         return 20;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_3)) {
         return 30;
      }
      if (ItemStack.isSameItem(stack, DRINK_LVL_4)) {
         return 40;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_1)) {
         return 10;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_2)) {
         return 20;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_3)) {
         return 50;
      }
      if (ItemStack.isSameItem(stack, RADIUS_LVL_4)) {
         return 80;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_1)) {
         return 10;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_2)) {
         return 20;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_3)) {
         return 50;
      }
      if (ItemStack.isSameItem(stack, LENGTH_LVL_4)) {
         return 80;
      }
      return 0;
   }

   public static String getPotionName(MobEffectInstance effect) {
      return effect.isVisible() ? I18n.get(effect.getDescriptionId()).trim() : "???";
   }

   public static void addPotionTooltip(ItemStack stack, List<MobEffectInstance> list, List<String> lores, float durationFactor) {
      ArrayList<Tuple<String, AttributeModifier>> list1 = Lists.newArrayList();
      if (list.isEmpty()) {
         String s = I18n.get("effect.none").trim();
         lores.add(ChatFormatting.GRAY + s);
      } else {
         for (MobEffectInstance potioneffect : list) {
            if (!potioneffect.isVisible()) continue;
            String s1 = I18n.get(potioneffect.getDescriptionId()).trim();
            MobEffect potion = potioneffect.getEffect();
            Map<Attribute, AttributeModifier> map = potion.getAttributeModifiers();
            if (!map.isEmpty()) {
               for (Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                  AttributeModifier attributemodifier = entry.getValue();
                  AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getId(), attributemodifier.getName(), potion.getAttributeModifierValue(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                  list1.add(new Tuple<String, AttributeModifier>(entry.getKey().getDescriptionId(), attributemodifier1));
               }
            }
            if (potioneffect.getAmplifier() > 0) {
               s1 = s1 + " " + I18n.get("potion.potency." + potioneffect.getAmplifier()).trim();
            }
            if (potioneffect.getDuration() > 20) {

               s1 = s1 + " (" + MobEffectUtil.formatDuration(potioneffect, durationFactor).getString() + ")";
            }
            if (potion.isBeneficial()) {
               lores.add(ChatFormatting.BLUE + s1);
               continue;
            }
            lores.add(ChatFormatting.RED + s1);
         }
      }

      if (!list1.isEmpty()) {
         lores.add("");
         lores.add(ChatFormatting.DARK_PURPLE + I18n.get("potion.whenDrank"));
         for (Tuple<String, AttributeModifier> tuple : list1) {
            AttributeModifier attributemodifier2 = tuple.getB();
            double d0 = attributemodifier2.getAmount();
            double d1 = (attributemodifier2.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE
                  || attributemodifier2.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL)
               ? attributemodifier2.getAmount() * 100.0 : attributemodifier2.getAmount();

            String attrName = I18n.get(tuple.getA());
            if (d0 > 0.0) {
               lores.add(ChatFormatting.BLUE + I18n.get("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(),
                  ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), attrName));
            } else if (d0 < 0.0) {
               lores.add(ChatFormatting.RED + I18n.get("attribute.modifier.take." + attributemodifier2.getOperation().toValue(),
                  ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(-d1), attrName));
            }
         }
      }

      IPotionData data = stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).orElse(null);
      if (data != null) {
         net.minecraft.world.item.Item item = stack.getItem();
         if (item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.potion_drinkable.get()
               || item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.potion_jug.get()) {
            lores.add(ChatFormatting.YELLOW + I18n.get("potion.info.drink_time") + ": " + data.getDrinkTime());
         } else if (item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.potion_throwable.get()) {
            lores.add(ChatFormatting.YELLOW + I18n.get("potion.info.radius") + ": " + data.getRadius());
         } else if (item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.potion_lingering.get()) {
            lores.add(ChatFormatting.YELLOW + I18n.get("potion.info.radius") + ": " + data.getRadius());
            lores.add(ChatFormatting.YELLOW + I18n.get("potion.info.length") + ": " + data.getLength());
         } else if (item == com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.potion_burst.get()) {
            lores.add(ChatFormatting.YELLOW + I18n.get("potion.info.radius") + ": " + data.getRadius());
            if (data.isLingering()) {
               lores.add(ChatFormatting.YELLOW + I18n.get("potion.info.length") + ": " + data.getLength());
            }
         }
      }
   }
}
