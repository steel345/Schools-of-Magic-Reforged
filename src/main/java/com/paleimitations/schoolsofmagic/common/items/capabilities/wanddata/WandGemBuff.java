package com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata;

import com.paleimitations.schoolsofmagic.common.MagicElement;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class WandGemBuff {

   private WandGemBuff() {}

   public static final float POWER_BONUS_LEVELS = 1.0F / 3.0F;

   public static final int BUFF_PERCENT = 5;

   private static final String[] GEM_ELEMENT = {
      "pyromancy", "heliomancy", "aeromancy", "geomancy", "animancy", "electromancy",
      "hydromancy", "cryomancy", "hieromancy", "chaotics", "auramancy", "astromancy",
      "infernality", "spectromancy", "umbramancy", "necromancy"
   };

   public static IWandData.EnumGemType readGem(ItemStack wand) {
      if (wand == null || wand.isEmpty()) return null;
      IWandData data = CapabilityWandData.getCapability(wand);
      if (data != null && data.getGemType() != null) return data.getGemType();
      CompoundTag tag = wand.getTag();
      if (tag != null && tag.contains("wand_data")) {
         String g = tag.getCompound("wand_data").getString("gemType");
         if (!g.isEmpty()) return IWandData.EnumGemType.fromName(g);
      }
      return null;
   }

   public static boolean isAllSchools(IWandData.EnumGemType gem) {
      return gem == IWandData.EnumGemType.DIAMOND;
   }

   public static MagicElement getElement(IWandData.EnumGemType gem) {
      if (gem == null) return null;
      int o = gem.ordinal();
      if (o >= 0 && o < GEM_ELEMENT.length) {
         return MagicElementRegistry.getElementFromName(GEM_ELEMENT[o]);
      }
      return null;
   }

   public static int getElementId(IWandData.EnumGemType gem) {
      MagicElement el = getElement(gem);
      return el != null ? el.getId() : -1;
   }

   public static Component buffTooltip(IWandData.EnumGemType gem) {
      if (gem == null) return null;
      if (isAllSchools(gem)) {
         return Component.literal("+" + BUFF_PERCENT + "% Magic Power to All Schools")
            .withStyle(ChatFormatting.GRAY);
      }
      MagicElement el = getElement(gem);
      if (el == null) return null;
      return Component.literal("+" + BUFF_PERCENT + "% ")
         .append(Component.translatable(el.getFormattedName()))
         .append(Component.literal(" Spell Power"))
         .withStyle(ChatFormatting.GRAY);
   }
}
