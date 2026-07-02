package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.CapabilityPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.potiondata.IPotionData;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.registries.BlockRegistry;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.util.TeaUtils;
import java.awt.Color;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.FoliageColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class SOMItemColorizer implements ItemColor {
   public static final SOMItemColorizer INSTANCE = new SOMItemColorizer();

   @Override
   public int getColor(ItemStack stack, int tintIndex) {

      if (stack.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemPotionCharm charm) {
         return tintIndex == 0 ? charm.getColorFor(stack) : 0xFFFFFFFF;
      }

      int i = 0x385DC6;
      IPotionData potionData = stack.getCapability(CapabilityPotionData.POTION_DATA_CAPABILITY).orElse(null);
      if (potionData != null && !potionData.getPotionEffects().isEmpty()) {
         i = PotionUtils.getColor(potionData.getPotionEffects());
      }
      if (stack.getItem() == ItemRegistry.teacup.get()) {
         if (TeaUtils.getEffect(stack) != null
               || com.paleimitations.schoolsofmagic.common.brewing.DynamicTea.get(stack) != null) {
            i = TeaUtils.resolveTeaColor(stack);
         }
      }
      if (stack.getItem() == ItemRegistry.spell_modifier_scroll.get()) {
         com.paleimitations.schoolsofmagic.common.spells.Spell.EnumSpellModifier modifier =
            com.paleimitations.schoolsofmagic.common.items.ItemSpellModifierScroll.getModifier(stack);
         i = Color.WHITE.getRGB();
         if (modifier != null) {
            if (modifier.id == 16) {
               i = MagicElementRegistry.getElementFromId(modifier.level - 1).getColor();
            } else {
               switch (modifier.level) {
                  case 1 -> i = new Color(200, 188, 40).getRGB();
                  case 2 -> i = new Color(60, 180, 28).getRGB();
                  case 3 -> i = new Color(15, 120, 160).getRGB();
                  case 4 -> i = new Color(60, 60, 170).getRGB();
                  case 5 -> i = new Color(115, 39, 177).getRGB();
               }
            }
         }
      }

      if (stack.getItem() == ItemRegistry.complete_spellbook.get()
          || stack.getItem() == ItemRegistry.spellbook.get()) {
         return -1;
      }
      if (stack.getItem() == ItemRegistry.basic_spellbook.get()
          || stack.getItem() == ItemRegistry.intermediate_spellbook.get()
          || stack.getItem() == ItemRegistry.advanced_spellbook.get()) {
         IBook bookData = CapabilityBook.getCapability(stack);
         if (bookData != null && bookData.getColor() != null) {
            float[] rgb = bookData.getColor().getTextureDiffuseColors();
            return tintIndex > 0 ? -1 : new Color(rgb[0], rgb[1], rgb[2]).getRGB();
         }
         return -1;
      }

      if (ItemStack.isSameItem(stack, new ItemStack(BlockRegistry.magic_leaves1.get()))) {
         return FoliageColor.get(0.5, 1.0);
      }

      if (ItemStack.isSameItem(stack, new ItemStack(BlockRegistry.leaves_ash.get()))
            || ItemStack.isSameItem(stack, new ItemStack(BlockRegistry.leaves_pine.get()))) {
         return tintIndex > 0 ? -1 : FoliageColor.get(0.5, 1.0);
      }
      return tintIndex > 0 ? -1 : i;
   }

   @SubscribeEvent
   public static void registerColors(RegisterColorHandlersEvent.Item event) {
      event.register(INSTANCE,
         ItemRegistry.potion_drinkable.get(), ItemRegistry.potion_throwable.get(), ItemRegistry.potion_lingering.get(),
         ItemRegistry.potion_jug.get(), ItemRegistry.potion_burst.get(), ItemRegistry.potion_crystal.get(),
         ItemRegistry.potion_charm.get(),
         ItemRegistry.teacup.get(), ItemRegistry.spell_modifier_scroll.get(),
         ItemRegistry.spellbook.get(),
         ItemRegistry.basic_spellbook.get(), ItemRegistry.intermediate_spellbook.get(),
         ItemRegistry.advanced_spellbook.get(), ItemRegistry.complete_spellbook.get());
      event.register(INSTANCE, BlockRegistry.magic_leaves1.get(),
         BlockRegistry.leaves_ash.get(), BlockRegistry.leaves_pine.get());
   }

   public static void registerItemColors() {
   }
}
