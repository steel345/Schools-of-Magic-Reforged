package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.book.CapabilityBook;
import com.paleimitations.schoolsofmagic.common.items.capabilities.book.IBook;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.NonNullList;

public class ItemSpellbook extends ItemBookBase {
   public ItemSpellbook(Item.Properties props) {
      super(props);
   }

   public static boolean hasOwner(ItemStack stack) {
      return stack.hasTag() && stack.getTag().hasUUID("ownerID");
   }

   public static boolean isOwner(ItemStack stack, Player player) {
      if (!hasOwner(stack)) {
         return true;
      }
      if (player == null) {
         return false;
      }
      if (player.hasEffect(com.paleimitations.schoolsofmagic.common.registries.PotionRegistry.other_player.get())) {
         return false;
      }
      return stack.getTag().getUUID("ownerID").equals(player.getUUID());
   }

   public static boolean isCastingMode(ItemStack stack) {
      return stack.hasTag() && stack.getTag().getBoolean("CastingMode");
   }

   public static void setCastingMode(ItemStack stack, boolean on) {
      stack.getOrCreateTag().putBoolean("CastingMode", on);
   }

   public static com.paleimitations.schoolsofmagic.common.spells.Spell selectedSpell(ItemStack stack) {
      if (stack.hasTag() && stack.getTag().contains("SelectedSpellRL")) {
         return com.paleimitations.schoolsofmagic.common.spells.SpellHelper.getSpellInstance(
            new net.minecraft.resources.ResourceLocation(stack.getTag().getString("SelectedSpellRL")),
            stack.getTag().getCompound("SelectedSpellData"));
      }
      return null;
   }

   public static void setSelectedSpell(ItemStack stack, com.paleimitations.schoolsofmagic.common.spells.Spell spell) {
      if (spell == null) {
         return;
      }
      stack.getOrCreateTag().putString("SelectedSpellRL", spell.getResourceLocation().toString());
      stack.getTag().put("SelectedSpellData", spell.serializeNBT());
   }

   private static final java.util.Map<String, com.paleimitations.schoolsofmagic.common.spells.Spell> CAST_CACHE =
      new java.util.concurrent.ConcurrentHashMap<>();
   private static final java.util.Map<String, String> CAST_SIG = new java.util.concurrent.ConcurrentHashMap<>();

   private static String castKey(Player player) {
      return player.getUUID().toString() + (player.level().isClientSide ? "_C" : "_S");
   }

   public static com.paleimitations.schoolsofmagic.common.spells.Spell castingInstance(Player player, ItemStack stack) {
      String key = castKey(player);
      if (!isCastingMode(stack) || !stack.hasTag() || !stack.getTag().contains("SelectedSpellRL")) {
         return null;
      }
      String sig = stack.getTag().getString("SelectedSpellRL") + stack.getTag().getCompound("SelectedSpellData");
      if (!sig.equals(CAST_SIG.get(key)) || !CAST_CACHE.containsKey(key)) {
         com.paleimitations.schoolsofmagic.common.spells.Spell sp = selectedSpell(stack);
         if (sp == null) {
            return null;
         }
         sp.remainingUses = 0;
         sp.maxUses = 0;
         CAST_CACHE.put(key, sp);
         CAST_SIG.put(key, sig);
      }
      return CAST_CACHE.get(key);
   }

   public static void setOwner(ItemStack stack, Player player) {
      if (player == null || hasOwner(stack)) {
         return;
      }
      stack.getOrCreateTag().putUUID("ownerID", player.getUUID());
      stack.getTag().putString("ownerName", player.getGameProfile().getName());
   }

   @Override
   public void onCraftedBy(ItemStack stack, Level level, Player player) {
      super.onCraftedBy(stack, level, player);
      setOwner(stack, player);
   }

   @Override
   public void appendHoverText(ItemStack stack, Level level, List<Component> tip, TooltipFlag flag) {
      super.appendHoverText(stack, level, tip, flag);
      if (hasOwner(stack)) {
         tip.add(Component.literal("Owner: " + stack.getTag().getString("ownerName")).withStyle(ChatFormatting.GRAY));
      }
   }

   public static ItemStack initializeBook(ItemStack stack) {
      IBook book = CapabilityBook.getCapability(stack);
      if (book != null && !book.isEdited() && book.getColor() == null && book.getLinks() == 0) {
         book.setLinks(4);
      }
      return stack;
   }

   @Override
   public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
      for (int i = 0; i < 17; ++i) {
         DyeColor dyeColor = i == 0 ? null : DyeColor.values()[i - 1];
         ItemStack book = new ItemStack(this);
         IBook data = CapabilityBook.getCapability(book);
         data.setColor(dyeColor);
         data.setLinks(4);
         book.getOrCreateTag().putInt("BookLinks", 4);
         book.getOrCreateTag().putInt("BookColor", dyeColor == null ? -1 : dyeColor.getId());
         items.add(book);
      }
   }
}
