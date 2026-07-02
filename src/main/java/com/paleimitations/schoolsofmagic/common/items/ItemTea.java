package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.recipes.RecipeTea;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.RecipeRegistry;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemTea extends Item {
   public ItemTea(Item.Properties props) {
      super(props);
   }

   public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
      Player entityplayer = entityLiving instanceof Player ? (Player)entityLiving : null;
      if (entityplayer == null || !entityplayer.getAbilities().instabuild) {
         stack.shrink(1);
      }

      if (entityplayer instanceof ServerPlayer) {
         CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)entityplayer, stack);
      }

      if (!worldIn.isClientSide) {
         com.paleimitations.schoolsofmagic.common.brewing.BrewResult dyn =
            com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.get(stack);
         if (dyn != null) {
            for (MobEffectInstance inst : dyn.buildEffects()) {
               MobEffect potion = inst.getEffect();
               if (potion.isInstantenous()) {
                  potion.applyInstantenousEffect(entityplayer, entityplayer, entityLiving, inst.getAmplifier(), 1.0);
               } else {
                  entityLiving.addEffect(new MobEffectInstance(inst));
               }
            }
         } else {
            MobEffectInstance potioneffect = TeaUtils.getEffect(stack);
            if (potioneffect != null) {
               MobEffect potion = potioneffect.getEffect();
               if (potion.isInstantenous()) {
                  potion.applyInstantenousEffect(entityplayer, entityplayer, entityLiving, potioneffect.getAmplifier(), 1.0);
               } else {
                  entityLiving.addEffect(new MobEffectInstance(potioneffect));
               }
            }
         }
      }

      if (entityplayer != null) {
         entityplayer.awardStat(Stats.ITEM_USED.get(this));
      }

      if (entityplayer == null || !entityplayer.getAbilities().instabuild) {
         if (stack.isEmpty()) {
            return new ItemStack(ItemRegistry.teacup_empty.get());
         }

         if (entityplayer != null) {
            entityplayer.getInventory().add(new ItemStack(ItemRegistry.teacup_empty.get()));
         }
      }

      return stack;
   }

   public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
      playerIn.startUsingItem(handIn);
      return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
   }

   public Component getName(ItemStack stack) {
      com.paleimitations.schoolsofmagic.common.brewing.BrewResult dyn =
         com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.get(stack);
      if (dyn != null && dyn.displayName != null && !dyn.displayName.isEmpty()) {
         return Component.literal(dyn.displayName);
      }
      MobEffectInstance inst = TeaUtils.getEffect(stack);
      if (inst == null || inst.getEffect() == null) return super.getName(stack);
      String descId = inst.getEffect().getDescriptionId();
      String key = "tea." + descId;
      String name = I18n.get(key);
      if (name.equals(key)) {

         String shortDescId = descId;
         if (shortDescId.startsWith("effect.som.")) shortDescId = "effect." + shortDescId.substring("effect.som.".length());
         else if (shortDescId.startsWith("effect.minecraft.")) shortDescId = "effect." + shortDescId.substring("effect.minecraft.".length());
         String shortKey = "tea." + shortDescId;
         String shortName = I18n.get(shortKey);
         if (!shortName.equals(shortKey)) {
            return Component.literal(shortName);
         }

         return Component.literal(I18n.get(descId) + " Tea");
      }
      return Component.literal(name);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      com.paleimitations.schoolsofmagic.common.brewing.BrewResult dyn =
         com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.get(stack);
      if (dyn != null) {
         appendEffectLine(tooltip, dyn.primaryEffect, dyn.amplifier, dyn.durationSeconds);
         if (dyn.secondaryEffect != null) {
            appendEffectLine(tooltip, dyn.secondaryEffect, Math.max(0, dyn.amplifier - 1), dyn.durationSeconds);
         }
         for (String side : dyn.sideEffects) {
            tooltip.add(Component.translatable(effectKey(side)).withStyle(net.minecraft.ChatFormatting.DARK_RED));
         }
         return;
      }
      List<String> lores = new ArrayList<>();
      TeaUtils.addTeaTooltip(stack, lores, 1.0F);
      for (String s : lores) {
         tooltip.add(Component.literal(s));
      }
   }

   @OnlyIn(Dist.CLIENT)
   private static void appendEffectLine(List<Component> tooltip, String key, int amplifier, int durationSeconds) {
      MobEffect effect = com.paleimitations.schoolsofmagic.common.brewing.BrewEffects.get(key);
      if (effect == null) return;
      net.minecraft.network.chat.MutableComponent line = Component.translatable(effect.getDescriptionId());
      if (amplifier > 0) {
         line = Component.translatable("potion.withAmplifier", line, Component.translatable("potion.potency." + amplifier));
      }
      if (!effect.isInstantenous()) {
         String time = String.format("%d:%02d", durationSeconds / 60, durationSeconds % 60);
         line = Component.translatable("potion.withDuration", line, time);
      }
      net.minecraft.ChatFormatting col = effect.getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL
         ? net.minecraft.ChatFormatting.RED : net.minecraft.ChatFormatting.BLUE;
      tooltip.add(line.withStyle(col));
   }

   private static String effectKey(String key) {
      MobEffect effect = com.paleimitations.schoolsofmagic.common.brewing.BrewEffects.get(key);
      return effect != null ? effect.getDescriptionId() : ("effect." + key);
   }

   public int getUseDuration(ItemStack stack) {
      return 32;
   }

   public UseAnim getUseAnimation(ItemStack stack) {
      return UseAnim.DRINK;
   }
}
