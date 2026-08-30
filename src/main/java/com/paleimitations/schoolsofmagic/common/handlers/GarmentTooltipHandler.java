package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// the plating only counts while the piece is actually worn, and which slot it goes in is not the
// same for all three, so the tooltip says both
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class GarmentTooltipHandler {
   @SubscribeEvent
   public static void onTooltip(ItemTooltipEvent event) {
      ItemStack stack = event.getItemStack();
      if (!GarmentSlots.isPlain(stack)) return;


      String slot;
      double plate;
      if (GarmentSlots.isCrown(stack)) {
         slot = "crown";
         plate = GarmentArmorHandler.CROWN;
      } else if (GarmentSlots.isNecklace(stack)) {
         slot = "talisman";
         plate = GarmentArmorHandler.NECKLACE;
      } else if (GarmentSlots.isRing(stack)) {
         slot = "ring";
         plate = GarmentArmorHandler.RING;
      } else {
         return;
      }

      event.getToolTip().add(Component.empty());
      event.getToolTip().add(Component.translatable("tooltip.som.when_worn",
         Component.translatable("tooltip.som.slot." + slot)).withStyle(ChatFormatting.GRAY));
      event.getToolTip().add(Component.translatable("attribute.modifier.plus.0",
         ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(plate),
         Component.translatable("attribute.name.generic.armor"))
         .withStyle(ChatFormatting.BLUE));
   }
}
