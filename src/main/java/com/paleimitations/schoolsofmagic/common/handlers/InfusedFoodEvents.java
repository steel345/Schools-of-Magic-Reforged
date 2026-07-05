package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.InfusedFood;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InfusedFoodEvents {

   @SubscribeEvent
   public static void onTooltip(ItemTooltipEvent event) {
      ItemStack stack = event.getItemStack();
      if (!InfusedFood.isInfused(stack)) {
         return;
      }
      Player player = event.getEntity();
      UUID infuser = InfusedFood.getInfuser(stack);
      if (player != null && infuser != null && player.getUUID().equals(infuser)) {
         for (MobEffectInstance e : InfusedFood.getEffects(stack)) {
            MutableComponent line = Component.translatable(e.getEffect().getDescriptionId());
            if (e.getAmplifier() > 0) {
               line = Component.translatable("potion.withAmplifier", line, Component.translatable("potion.potency." + e.getAmplifier()));
            }
            if (!e.getEffect().isInstantenous()) {
               line = Component.translatable("potion.withDuration", line, MobEffectUtil.formatDuration(e, 1.0F));
            }
            ChatFormatting color = e.getEffect().getCategory() == MobEffectCategory.BENEFICIAL ? ChatFormatting.BLUE : ChatFormatting.RED;
            event.getToolTip().add(line.withStyle(color));
         }
      }
   }

   @SubscribeEvent
   public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
      ItemStack stack = event.getItem();
      if (!InfusedFood.isInfused(stack)) {
         return;
      }
      LivingEntity entity = event.getEntity();
      if (!entity.level().isClientSide) {
         applyEffects(entity, stack);
      }
   }

   public static void applyEffects(LivingEntity entity, ItemStack stack) {
      for (MobEffectInstance e : InfusedFood.getEffects(stack)) {
         if (e.getEffect().isInstantenous()) {
            e.getEffect().applyInstantenousEffect(null, null, entity, e.getAmplifier(), 1.0);
         } else {
            entity.addEffect(new MobEffectInstance(e));
         }
      }
   }
}
