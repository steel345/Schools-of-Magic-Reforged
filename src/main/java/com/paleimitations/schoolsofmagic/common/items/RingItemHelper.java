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

   public static IWandData.EnumGemType getGem(ItemStack ring) {
      if (ring == null || ring.isEmpty() || !ring.hasTag()) return null;
      String g = ring.getTag().getString("ring_gem");
      return g.isEmpty() ? null : IWandData.EnumGemType.fromName(g);
   }
}
