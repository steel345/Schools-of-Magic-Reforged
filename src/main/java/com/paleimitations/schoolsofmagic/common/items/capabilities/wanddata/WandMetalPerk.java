package com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata;

import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.CapabilityRingData;
import com.paleimitations.schoolsofmagic.common.entity.capabilities.ring_data.IRingData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class WandMetalPerk {

   private WandMetalPerk() {}

   public static final float ALL_POWER = 2.0F * WandGemBuff.POWER_BONUS_LEVELS;
   public static final float ELEMENTAL_BOOST = WandGemBuff.POWER_BONUS_LEVELS;
   public static final float CAST_SPEED_MULT = 0.95F;
   public static final float PROJECTILE_SPEED_MULT = 1.05F;
   public static final float XP_MULT = 1.5F;
   public static final float DURATION_MULT = 1.5F;
   public static final float INSTABILITY = 0.5F;

   public static IWandData.EnumHandleType fromName(String name) {
      if (name == null || name.isEmpty()) return null;
      for (IWandData.EnumHandleType h : IWandData.EnumHandleType.values()) {
         if (h.getSerializedName().equals(name)) return h;
      }
      return null;
   }

   public static IWandData.EnumHandleType readMetal(ItemStack stack) {
      if (stack == null || stack.isEmpty()) return null;
      IWandData data = CapabilityWandData.getCapability(stack);
      if (data != null && data.getHandleType() != null) return data.getHandleType();
      CompoundTag tag = stack.getTag();
      if (tag != null) {
         if (tag.contains("wand_data")) {
            IWandData.EnumHandleType m = fromName(tag.getCompound("wand_data").getString("handleType"));
            if (m != null) return m;
         }
         if (tag.contains("ring_metal")) {
            return fromName(tag.getString("ring_metal"));
         }
      }
      return null;
   }

   public static IWandData.EnumHandleType getMetal(Player player) {
      for (InteractionHand h : InteractionHand.values()) {
         ItemStack s = player.getItemInHand(h);
         if (s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemBaseWand
               && !(s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand)) {
            IWandData.EnumHandleType m = readMetal(s);
            if (m != null) return m;
         }
      }
      IRingData ring = CapabilityRingData.get(player);
      if (ring != null && !ring.getRing().isEmpty()) {
         return readMetal(ring.getRing());
      }
      return null;
   }

   public static Component perkTooltip(IWandData.EnumHandleType metal) {
      if (metal == null) return null;
      String s;
      switch (metal) {
         case VOID:   s = "Increased Spell Power for All Spells"; break;
         case IRON:   s = "Faster Cast Time and Projectile Speed"; break;
         case STEEL:  s = "Unstable Spell Power"; break;
         case SILVER: s = "Improved Spell Accuracy"; break;
         case GOLD:   s = "Enhanced Elemental Specialization"; break;
         case BRASS:  s = "Enhanced Spell Visuals"; break;
         case BRONZE: s = "Increased Magical Experience Gain"; break;
         case COPPER: s = "Extended Spell Effect Duration"; break;
         default: return null;
      }
      return Component.literal(s).withStyle(ChatFormatting.GRAY);
   }
}
