package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class RingItemHelper {
   private RingItemHelper() {}

   public static int cmd(IWandData.EnumHandleType metal, IWandData.EnumGemType gem) {
      return metal.ordinal() * 100 + gem.ordinal() + 1;
   }

   public static void setData(ItemStack ring, IWandData.EnumHandleType metal, IWandData.EnumGemType gem) {
      CompoundTag t = ring.getOrCreateTag();
      t.putString("ring_metal", metal.getSerializedName());
      t.putString("ring_gem", gem.getSerializedName());
      t.putInt("CustomModelData", cmd(metal, gem));
   }

   // from the charm slot as well as from its own
   public static boolean casts(ItemStack stack) {
      if (stack.isEmpty()) return false;
      if (stack.getItem() instanceof ItemApprenticeRing) return true;
      if (stack.is(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.apprentice_crown.get())
            || stack.is(com.paleimitations.schoolsofmagic.common.registries.ItemRegistry.apprentice_necklace.get())) {
         return true;
      }
      return stack.getItem() instanceof ItemAdvancedGarment set
         && ("crown".equals(set.kind()) || "necklace".equals(set.kind()));
   }

   public static ItemStack getWorn(net.minecraft.world.entity.player.Player player) {
      if (player == null) return ItemStack.EMPTY;
      com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData data =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
      ItemStack worn = data == null ? ItemStack.EMPTY : data.getRing();
      if (casts(worn)) return worn;

      ItemStack charm = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .findCharmPouch(player, RingItemHelper::casts);
      if (casts(charm)) return charm;

      ItemStack crown = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .wornCrown(player);
      if (casts(crown)) return crown;

      ItemStack neck = com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .wornNecklace(player);
      return casts(neck) ? neck : ItemStack.EMPTY;
   }

   public static ItemStack getWornRing(net.minecraft.world.entity.player.Player player) {
      if (player == null) return ItemStack.EMPTY;
      com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData data =
         com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData.get(player);
      ItemStack worn = data == null ? ItemStack.EMPTY : data.getRing();
      if (com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots.isRing(worn)) return worn;
      return com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots
         .findCharmPouch(player, com.paleimitations.schoolsofmagic.common.entity.capabilities.garment_data.GarmentSlots::isRing);
   }

   public static IWandData.EnumHandleType getMetal(ItemStack ring) {
      if (ring == null || ring.isEmpty() || !ring.hasTag()) return null;
      String m = ring.getTag().getString("ring_metal");
      if (m.isEmpty()) m = ring.getTag().getString("garment_metal");
      return m.isEmpty() ? null : IWandData.EnumHandleType.fromName(m);
   }

   public static IWandData.EnumGemType getGem(ItemStack ring) {
      if (ring == null || ring.isEmpty() || !ring.hasTag()) return null;
      String g = ring.getTag().getString("ring_gem");
      if (g.isEmpty()) g = ring.getTag().getString("garment_gem");
      return g.isEmpty() ? null : IWandData.EnumGemType.fromName(g);
   }
}
