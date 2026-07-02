package com.paleimitations.schoolsofmagic.common.items.capabilities.wanddata;

import com.paleimitations.schoolsofmagic.common.spells.Spell;
import com.paleimitations.schoolsofmagic.common.spells.spells.SpellCustom;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class WandPersonality {

   private WandPersonality() {}

   public enum Personality { WANDERING, ADVENTUROUS, AMBIVALENT }

   private static final String TAG = "personality";
   private static final float MOOD = WandGemBuff.POWER_BONUS_LEVELS;
   private static final long FRESH_BIOME_TICKS = 1200L;
   private static final long STALE_BIOME_TICKS = 18000L;
   private static final int BORED_REPEAT = 10;
   private static final int FORGET_NIGHTS = 2;

   public static Personality of(IWandData.EnumCoreType core) {
      if (core == null) return Personality.AMBIVALENT;
      switch (core) {
         case DARK_OAK: case SPRUCE: case PINE: case YEW:
            return Personality.WANDERING;
         case VERDE: case ELDER: case JUNGLE: case ACACIA:
            return Personality.ADVENTUROUS;
         default:
            return Personality.AMBIVALENT;
      }
   }

   public static IWandData.EnumCoreType readCore(ItemStack stack) {
      if (stack == null || stack.isEmpty()) return null;
      IWandData data = CapabilityWandData.getCapability(stack);
      if (data != null && data.getCoreType() != null) return data.getCoreType();
      CompoundTag t = stack.getTag();
      if (t != null && t.contains("wand_data")) {
         return IWandData.EnumCoreType.fromName(t.getCompound("wand_data").getString("coreType"));
      }
      return null;
   }

   public static Component nameTooltip(ItemStack stack) {
      IWandData.EnumCoreType core = readCore(stack);
      if (core == null) return null;
      String n;
      switch (of(core)) {
         case WANDERING:   n = "Wandering"; break;
         case ADVENTUROUS: n = "Adventurous"; break;
         default:          n = "Ambivalent"; break;
      }
      return Component.literal(n).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
   }

   private static ItemStack heldWand(Player player) {
      for (InteractionHand h : InteractionHand.values()) {
         ItemStack s = player.getItemInHand(h);
         if (s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemBaseWand
               && !(s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand)) {
            return s;
         }
      }
      return null;
   }

   private static String spellId(Spell spell) {
      if (spell instanceof SpellCustom sc) {
         return "c:" + sc.getCustomName() + ":" + sc.getShape() + ":"
            + (sc.getEffect() != null ? sc.getEffect().getDescriptionId() : "");
      }
      return spell == null ? "" : spell.getName();
   }

   public static void recordCast(Player player, Spell spell) {
      ItemStack wand = heldWand(player);
      if (wand == null || of(readCore(wand)) != Personality.ADVENTUROUS) return;
      CompoundTag p = wand.getOrCreateTagElement(TAG);
      String id = spellId(spell);
      if (id.equals(p.getString("lastSpell"))) {
         p.putInt("repeat", p.getInt("repeat") + 1);
      } else {
         p.putString("lastSpell", id);
         p.putInt("repeat", 0);
      }
   }

   public static void onSlept(Player player) {
      java.util.List<ItemStack> all = new java.util.ArrayList<>();
      all.addAll(player.getInventory().items);
      all.addAll(player.getInventory().offhand);
      for (ItemStack s : all) {
         if (!(s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemBaseWand)
               || s.getItem() instanceof com.paleimitations.schoolsofmagic.common.items.ItemApprenticeWand) continue;
         if (of(readCore(s)) != Personality.ADVENTUROUS) continue;
         CompoundTag p = s.getTagElement(TAG);
         if (p == null) continue;
         int nights = p.getInt("nights") + 1;
         if (nights >= FORGET_NIGHTS) {
            p.remove("lastSpell");
            p.remove("repeat");
            p.remove("nights");
         } else {
            p.putInt("nights", nights);
         }
      }
   }

   public static void tickWandering(Player player, ItemStack wand) {
      if (of(readCore(wand)) != Personality.WANDERING) return;
      String biome = player.level().getBiome(player.blockPosition()).unwrapKey()
         .map(k -> k.location().toString()).orElse("");
      CompoundTag p = wand.getOrCreateTagElement(TAG);
      if (!biome.equals(p.getString("biome"))) {
         p.putString("biome", biome);
         p.putLong("biomeTime", player.level().getGameTime());
      }
   }

   public static float powerBonus(Player player) {
      ItemStack wand = heldWand(player);
      if (wand == null) return 0.0F;
      Personality pers = of(readCore(wand));
      if (pers == Personality.AMBIVALENT) return 0.0F;
      CompoundTag p = wand.getTagElement(TAG);
      if (p == null) return 0.0F;
      if (pers == Personality.WANDERING) {
         long t = player.level().getGameTime() - p.getLong("biomeTime");
         if (t < FRESH_BIOME_TICKS) return MOOD;
         if (t > STALE_BIOME_TICKS) return -MOOD;
         return 0.0F;
      }
      int r = p.getInt("repeat");
      if (r == 0) return MOOD;
      if (r >= BORED_REPEAT) return -MOOD;
      return 0.0F;
   }
}
