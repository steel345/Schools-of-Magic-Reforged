package com.paleimitations.schoolsofmagic.common.handlers;

import com.paleimitations.schoolsofmagic.SchoolsOfMagic;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.CapabilityGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.IGarmentData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import com.paleimitations.schoolsofmagic.common.items.ItemAdvancedGarment;
import com.paleimitations.schoolsofmagic.common.items.ItemApprenticeRing;
import com.paleimitations.schoolsofmagic.common.items.ItemMetalGarment;
import com.paleimitations.schoolsofmagic.common.registries.ItemRegistry;
import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// all counts, and one of each stacks
@Mod.EventBusSubscriber(modid = SchoolsOfMagic.MODID)
public class GarmentArmorHandler {
   private static final UUID PLATING = UUID.fromString("6f2a1c48-3b7d-4c11-9f5e-2ad3c7b91e04");

   public static final double RING = 2.0D;
   public static final double NECKLACE = 4.0D;
   public static final double CROWN = 5.0D;

   private static boolean isRing(ItemStack stack) { return GarmentSlots.isPlain(stack) && GarmentSlots.isRing(stack); }

   private static boolean isCrown(ItemStack stack) { return GarmentSlots.isPlain(stack) && GarmentSlots.isCrown(stack); }

   private static boolean isNecklace(ItemStack stack) { return GarmentSlots.isPlain(stack) && GarmentSlots.isNecklace(stack); }

   private static double worn(Player player) {
      double plate = 0.0D;

      IRingData rings = CapabilityRingData.get(player);
      if (rings != null && isRing(rings.getRing())) plate += RING;

      IGarmentData garments = CapabilityGarmentData.get(player);
      if (garments != null && isCrown(garments.getGarment(IGarmentData.CROWN))) plate += CROWN;

      com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.ITalismanData talisman =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.talisman_data.CapabilityTalismanData.get(player);
      if (talisman != null && isNecklace(talisman.getTalisman())) plate += NECKLACE;

      if (isRing(GarmentSlots.findCharmPouch(player, GarmentArmorHandler::isRing))) plate += RING;
      if (isCrown(GarmentSlots.findCharmPouch(player, GarmentArmorHandler::isCrown))) plate += CROWN;
      if (isNecklace(GarmentSlots.findCharmPouch(player, GarmentArmorHandler::isNecklace))) plate += NECKLACE;

      return plate;
   }

   @SubscribeEvent
   public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
      if (event.phase != TickEvent.Phase.END) return;
      Player player = event.player;
      if (player.level().isClientSide || player.tickCount % 10 != 0) return;

      AttributeInstance armour = player.getAttribute(Attributes.ARMOR);
      if (armour == null) return;

      double want = worn(player);
      AttributeModifier had = armour.getModifier(PLATING);
      if (had != null && had.getAmount() == want) return;

      if (had != null) armour.removeModifier(PLATING);
      if (want > 0.0D) {
         armour.addTransientModifier(new AttributeModifier(
            PLATING, "som garments", want, AttributeModifier.Operation.ADDITION));
      }
   }
}
