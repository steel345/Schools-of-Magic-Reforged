package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.CapabilitySpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.ISpellNotes;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_notes.SpellNotes;
import com.paleimitations.schoolsofmagic.common.registries.MagicElementRegistry;
import com.paleimitations.schoolsofmagic.common.registries.MagicSchoolRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemSpellNotes extends Item {
   public ItemSpellNotes(Item.Properties props) {
      super(props);
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilitySpellNotes.createProvider();
   }

   public static void ensureNotes(ItemStack stack) {
      ISpellNotes data = CapabilitySpellNotes.getCapability(stack);
      if (data != null && stack.hasTag() && stack.getTag().contains("note_data")
            && data.getSpellNotes().magicValue() <= 0.0F) {
         data.deserializeNBT(stack.getTag().getCompound("note_data"));
      }
   }

   @Override
   public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slot, boolean selected) {
      ensureNotes(stack);
      super.inventoryTick(stack, level, entity, slot, selected);
   }

   @Override
   public net.minecraft.world.InteractionResultHolder<ItemStack> use(Level level,
         net.minecraft.world.entity.player.Player player, net.minecraft.world.InteractionHand hand) {
      ItemStack stack = player.getItemInHand(hand);
      if (stack.hasTag() && stack.getTag().contains("CustomSpell")) {
         if (level.isClientSide) {
            net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
               () -> () -> com.paleimitations.schoolsofmagic.client.guis.GuiNameSpell.open(hand, stack));
         }
         return net.minecraft.world.InteractionResultHolder.success(stack);
      }
      return super.use(level, player, hand);
   }

   @OnlyIn(Dist.CLIENT)
   public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
      ensureNotes(stack);
      ISpellNotes notes = CapabilitySpellNotes.getCapability(stack);
      if (notes != null) {
         int i;
         SpellNotes data = notes.getSpellNotes();
         if (data.magicianUnits > 0.0f) {
            tooltip.add(Component.literal(ChatFormatting.GRAY + String.valueOf(data.magicianUnits) + " " + I18n.get("modifier.magician_points.name")));
         }
         if (data.spellUnits > 0.0f) {
            tooltip.add(Component.literal(ChatFormatting.GRAY + String.valueOf(data.spellUnits) + " " + I18n.get("modifier.spell_points.name")));
         }
         if (data.ritualUnits > 0.0f) {
            tooltip.add(Component.literal(ChatFormatting.GRAY + String.valueOf(data.ritualUnits) + " " + I18n.get("modifier.ritual_points.name")));
         }
         if (data.potionUnits > 0.0f) {
            tooltip.add(Component.literal(ChatFormatting.GRAY + String.valueOf(data.potionUnits) + " " + I18n.get("modifier.potion_points.name")));
         }
         for (i = 0; i < 6; ++i) {
            if (!(data.schoolUnits[i] > 0.0f)) continue;
            tooltip.add(Component.literal(ChatFormatting.GRAY + String.valueOf(data.schoolUnits[i]) + " " + I18n.get("school." + MagicSchoolRegistry.getSchoolFromId(i).getName() + ".name") + " " + I18n.get("modifier.points.name")));
         }
         for (i = 0; i < 16; ++i) {
            if (!(data.elementUnits[i] > 0.0f)) continue;
            tooltip.add(Component.literal(ChatFormatting.GRAY + String.valueOf(data.elementUnits[i]) + " " + I18n.get("element." + MagicElementRegistry.getElementFromId(i).getName() + ".name") + " " + I18n.get("modifier.points.name")));
         }
         if (data.spark > -1) {
            tooltip.add(Component.literal(ChatFormatting.BOLD + I18n.get("element." + MagicElementRegistry.getElementFromId(data.spark).getName() + ".name") + " " + I18n.get("modifier.spark.name")));
         }
         if (tooltip.isEmpty()) {
            tooltip.add(Component.literal(ChatFormatting.GRAY + I18n.get("page.blank")));
         }
      }
   }

   @Nullable
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = super.getShareTag(stack);
      if (nbt == null) {
         nbt = new CompoundTag();
      }
      ISpellNotes data = CapabilitySpellNotes.getCapability(stack);
      if (data != null) {
         if (data == null && data.serializeNBT() != null) {
            return nbt;
         }
         nbt.put("note_data", (Tag)data.serializeNBT());
      }
      return nbt;
   }

   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      super.readShareTag(stack, nbt);
      ISpellNotes data = CapabilitySpellNotes.getCapability(stack);
      if (nbt != null && nbt.contains("note_data") && data != null) {
         data.deserializeNBT(nbt.getCompound("note_data"));
      }
   }
}
