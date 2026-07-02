package com.paleimitations.schoolsofmagic.common.items;

import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.CapabilitySpellModifier;
import com.paleimitations.schoolsofmagic.common.items.capabilities.spell_modifier.ISpellModifier;
import com.paleimitations.schoolsofmagic.common.spells.Spell;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemSpellModifierScroll extends Item {
   public ItemSpellModifierScroll(Item.Properties props) {
      super(props);
   }

   public ICapabilityProvider initCapabilities(ItemStack item, @Nullable CompoundTag nbt) {
      return CapabilitySpellModifier.createProvider();
   }

   public static Spell.EnumSpellModifier getModifier(ItemStack stack) {
      ISpellModifier data = CapabilitySpellModifier.getCapability(stack);
      if (data != null && data.getSpellModifier() != null) {
         return data.getSpellModifier();
      }
      if (stack.hasTag() && stack.getTag().contains("modifier")) {
         CompoundTag m = stack.getTag().getCompound("modifier");
         if (m.contains("modifier")) {
            return Spell.EnumSpellModifier.fromName(m.getString("modifier"));
         }
      }
      return null;
   }

   public Component getName(ItemStack stack) {
      Spell.EnumSpellModifier modifier = getModifier(stack);
      if (modifier != null) {
         String title = I18n.get("modifier." + modifier.getSerializedName() + ".name") + " " + I18n.get("modifier.title.name");
         if (!title.isEmpty()) {
            return Component.literal(title);
         }
      }
      return super.getName(stack);
   }

   @Nullable
   public CompoundTag getShareTag(ItemStack stack) {
      CompoundTag nbt = super.getShareTag(stack);
      if (nbt == null) {
         nbt = new CompoundTag();
      }
      ISpellModifier data = CapabilitySpellModifier.getCapability(stack);
      if (data != null) {
         nbt.put("modifier", (Tag)data.serializeNBT());
      }
      return nbt;
   }

   public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
      super.readShareTag(stack, nbt);
      ISpellModifier data = CapabilitySpellModifier.getCapability(stack);
      if (nbt != null && nbt.contains("modifier") && data != null) {
         data.deserializeNBT(nbt.getCompound("modifier"));
      }
   }
}
