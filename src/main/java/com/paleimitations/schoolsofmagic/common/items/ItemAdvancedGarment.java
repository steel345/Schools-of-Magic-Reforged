package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.IWandData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// a forged crown or necklace with a gem set into it. it keeps its metal and its stone the same way
// a ring does, so everything that already reads a ring can read one of these
public class ItemAdvancedGarment extends Item {
   private final String kind;

   public ItemAdvancedGarment(String kind, Properties properties) {
      super(properties.durability(250));
      this.kind = kind;
   }

   @Override
   public int getMaxDamage(ItemStack stack) {
      if (!com.paleimitations.schoolsofmagic.common.compat.SOMConfig.wandRingDurability()) {
         return 0;
      }
      return com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.readMetal(stack) != null
         ? 735 : 250;
   }

   @Override
   public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
      return ItemBaseWand.isMetalIngot(
         com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.readMetal(toRepair),
         repair);
   }

   public String kind() {
      return this.kind;
   }

   public static ItemStack of(Item item, IWandData.EnumHandleType metal, IWandData.EnumGemType gem) {
      ItemStack stack = new ItemStack(item);
      setData(stack, metal, gem);
      return stack;
   }

   public static void setData(ItemStack stack, IWandData.EnumHandleType metal, IWandData.EnumGemType gem) {
      CompoundTag tag = stack.getOrCreateTag();
      tag.putString("garment_metal", metal.getSerializedName());
      tag.putString("garment_gem", gem.getSerializedName());
      tag.putInt("CustomModelData", RingItemHelper.cmd(metal, gem));
   }

   public static IWandData.EnumHandleType getMetal(ItemStack stack) {
      if (stack.getTag() == null) return null;
      String metal = stack.getTag().getString("garment_metal");
      return metal.isEmpty() ? null : IWandData.EnumHandleType.fromName(metal);
   }

   public static IWandData.EnumGemType getGem(ItemStack stack) {
      if (stack.getTag() == null) return null;
      String gem = stack.getTag().getString("garment_gem");
      return gem.isEmpty() ? null : IWandData.EnumGemType.fromName(gem);
   }

   @Override
   public void appendHoverText(ItemStack stack, net.minecraft.world.level.Level level,
         java.util.List<Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
      IWandData.EnumGemType gem = getGem(stack);
      if (gem != null) {
         Component buff = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandGemBuff.buffTooltip(gem);
         if (buff != null) tooltip.add(buff);
      }
      Component perk = com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata.WandMetalPerk.perkTooltip(getMetal(stack));
      if (perk != null) tooltip.add(perk);
      super.appendHoverText(stack, level, tooltip, flag);
   }

   @Override
   public Component getName(ItemStack stack) {
      IWandData.EnumHandleType metal = getMetal(stack);
      if (metal == null) return super.getName(stack);
      return Component.translatable("item.som.advanced_" + this.kind,
         Component.translatable("metal.som." + metal.getSerializedName()));
   }
}
